package com.gionee.wms.service.stock;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.IndivFlowType;
import com.gionee.wms.common.WmsConstants.IndivStockStatus;
import com.gionee.wms.common.WmsConstants.IndivWaresStatus;
import com.gionee.wms.common.WmsConstants.NotifyStatus;
import com.gionee.wms.common.WmsConstants.OrderStatus;
import com.gionee.wms.common.WmsConstants.ReceiveType;
import com.gionee.wms.common.WmsConstants.RemoteCallStatus;
import com.gionee.wms.common.WmsConstants.StockBizType;
import com.gionee.wms.common.WmsConstants.StockInStatus;
import com.gionee.wms.common.WmsConstants.StockType;
import com.gionee.wms.dao.DeliveryDao;
import com.gionee.wms.dao.IndivDao;
import com.gionee.wms.dao.SalesOrderDao;
import com.gionee.wms.dao.StockInDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.ReceiveSummary;
import com.gionee.wms.dto.StockRequest;
import com.gionee.wms.entity.DeliveryGoods;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.StockIn;
import com.gionee.wms.entity.StockInItem;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.common.CommonServiceImpl;
import com.gionee.wms.web.client.OrderCenterClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service("stockInService")
public class StockInServiceImpl extends CommonServiceImpl implements StockInService {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	private StockInDao stockInDao;
	private SalesOrderDao orderDao;
	private DeliveryDao deliveryDao;
	private IndivDao indivDao;
	private StockService stockService;
	private TaskExecutor taskExecutor;
	private OrderCenterClient orderCenterClient;
	@Autowired
	private SalesOrderService salesOrderService;

	@Override
	public void addRmaInItem(StockInItem stockInItem) throws ServiceException {
		SalesOrder salesOrder = getSalesOrder(stockInItem.getOriginalCode());
		if (salesOrder == null) {
			throw new ServiceException("源订单不存在");
		}
		if (OrderStatus.BACKED.getCode() == salesOrder.getOrderStatus()) {
			throw new ServiceException("不能重复退货");
		}
//		if (ShippingStatus.SHIPPED.getCode() != salesOrder.getShippingStatus()) {
//			throw new ServiceException("源订单尚未发货");
//		}
		if (OrderStatus.SHIPPED.getCode() != salesOrder.getOrderStatus()) {
			throw new ServiceException("源订单状态异常");
		}
		// 根据出库明细来判断退货入库的合法性
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("originalCode", salesOrder.getOrderCode());
		List<DeliveryGoods> deliveryGoodsList = deliveryDao.queryDeliveryGoodsList(criteria);
		if (CollectionUtils.isEmpty(deliveryGoodsList)) {
			throw new ServiceException("原订单没有发货商品");
		}
		boolean backGoodsExisted = false;
		for (DeliveryGoods item : deliveryGoodsList) {
			if (item.getSkuId().longValue() == stockInItem.getSkuId().longValue()) {
				if (stockInItem.getQuantity() > item.getQuantity()) {
					throw new ServiceException("退货数量不能大于原订单发货数量");
				}
				backGoodsExisted = true;
			}
		}
		if (!backGoodsExisted) {
			throw new ServiceException("退货商品与原订单发货商品不符");
		}
		try {
			// 检测入库单是否已存在，不存在则先初始化入库单
			StockIn stockIn = getStockInByCode(stockInItem.getStockIn().getStockInCode());
			if (stockIn == null) {// 不存在
				stockIn = new StockIn();
				stockIn.setStockInCode(stockInItem.getStockIn().getStockInCode());
				stockIn.setStockInType(ReceiveType.RMA.getCode());
				stockIn.setHandlingStatus(StockInStatus.PENDING.getCode());
				stockIn.setPreparedBy(ActionUtils.getLoginName());
				stockIn.setPreparedTime(new Date());
				stockIn.setEnabled(true);
				stockInDao.addInitialStockIn(stockIn);
			}
			// 添加入库明细项
			stockInItem.setStockIn(stockIn);
			stockInItem.setIndivEnabled(WmsConstants.ENABLED_FALSE);
			stockInDao.addStockInItem(stockInItem);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	private SalesOrder getSalesOrder(String orderCode) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("orderCode", orderCode);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		criteria.put("page", page);
		List<SalesOrder> list;
		try {
			list = orderDao.queryOrderByPage(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new ServiceException("数据异常");
		}
	}

	@Override
	public void addPurchaseInItem(StockInItem stockInItem, List<IndivFlow> indivList) throws ServiceException {
		Integer indivFinished = WmsConstants.ENABLED_FALSE;
		if (stockInItem.getIndivEnabled() == WmsConstants.ENABLED_TRUE) {
			// 有编码商品需进行商品编码校验
			if (CollectionUtils.isEmpty(indivList)) {
				throw new ServiceException("商品编码不能为空");
			}
			if (indivList.size() != stockInItem.getQuantity()) {
				throw new ServiceException("输入商品编码数目与商品数量不符");
			}

			// 效验商品个体信息
			Map<String, IndivFlow> indivMap = Maps.newHashMap();
			for (IndivFlow indivFlow : indivList) {
				indivMap.put(indivFlow.getIndivCode(), indivFlow);
			}
			if (indivMap.size() < indivList.size()) {
				throw new ServiceException("输入的商品身份编码重复");
			}
			List<Indiv> currIndivList;
			try {
				currIndivList = indivDao.queryIndivListByCodes(Lists.newArrayList(indivMap.keySet()));
			} catch (DataAccessException e) {
				throw new ServiceException(e);
			}
			if (CollectionUtils.isNotEmpty(currIndivList)) {
				throw new ServiceException("商品身份编码不能重复添加");
			}
			indivFinished = WmsConstants.ENABLED_TRUE;
		}

		// 添加入库明细项
		try {
			// --根据入库编号判断入库单是否已存在。不存在，则先初始化入库单，否则为入库明细项关联起入库单 --
			StockIn stockIn = getStockInByCode(stockInItem.getStockIn().getStockInCode());
			if (stockIn == null) {// 不存在
				stockIn = new StockIn();
				stockIn.setStockInCode(stockInItem.getStockIn().getStockInCode());
				stockIn.setStockInType(ReceiveType.PURCHASE.getCode());
				stockIn.setHandlingStatus(StockInStatus.PENDING.getCode());
				stockIn.setPreparedBy(ActionUtils.getLoginName());
				stockIn.setPreparedTime(new Date());
				stockIn.setEnabled(true);
				// 初始化入库单
				stockInDao.addInitialStockIn(stockIn);
				stockInItem.setStockIn(stockIn);
			} else {
				stockInItem.setStockIn(stockIn);
			}
			stockInItem.setIndivFinished(indivFinished);
			stockInItem.setSkuId(stockInItem.getSku().getId());
			stockInItem.setSkuCode(stockInItem.getSku().getSkuCode());
			stockInItem.setSkuName(stockInItem.getSku().getSkuName());
			stockInDao.addStockInItem(stockInItem);
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		if (stockInItem.getIndivEnabled() == WmsConstants.ENABLED_TRUE) {
			// 有编码商品需记录商品个体流转信息
			try {
				for (IndivFlow indivFlow : indivList) {
					indivFlow.setFlowType(IndivFlowType.IN_PURCHASE.getCode());
					indivFlow.setFlowCode(stockInItem.getStockIn().getStockInCode());
					indivFlow.setWaresStatus(Integer.parseInt(stockInItem.getWaresStatus()));
					indivFlow.setSkuId(stockInItem.getSku().getId());
					indivFlow.setSkuCode(stockInItem.getSku().getSkuCode());
					indivFlow.setSkuName(stockInItem.getSku().getSkuName());
					indivFlow.setFlowGoodsId(stockInItem.getId());
					indivFlow.setMeasureUnit(stockInItem.getMeasureUnit());
					indivFlow.setProductBatchNo(stockInItem.getBatchNumber());
					indivFlow.setEnabled(WmsConstants.ENABLED_FALSE);
				}
				indivDao.addIndivFlows(indivList);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
		}
	}

	// @Override
	// public void addRmaInItemAndIndivs(StockInItem stockInItem,
	// List<IndivFlow> indivList) throws ServiceException {
	// if (CollectionUtils.isEmpty(indivList)) {
	// return;
	// }
	// if (indivList.size() != stockInItem.getQuantity()) {
	// throw new ServiceException("输入商品编码数目与商品数量不符");
	// }
	//
	// // 效验商品个体信息
	// Map<String, IndivFlow> indivFlowMap = Maps.newHashMap();
	// for (IndivFlow indiv : indivList) {
	// indivFlowMap.put(indiv.getIndivCode(), indiv);
	// }
	// if (indivFlowMap.size() < indivList.size()) {
	// throw new ServiceException("输入的商品身份编码重复");
	// }
	// List<Indiv> currIndivList;
	// try {
	// currIndivList =
	// indivDao.queryIndivListByCodes(Lists.newArrayList(indivFlowMap.keySet()));
	// } catch (DataAccessException e) {
	// throw new ServiceException(e);
	// }
	// if (CollectionUtils.isEmpty(currIndivList)) {
	// throw new ServiceException("商品编码不存在");
	// }
	// Map<String, Indiv> indivMap = Maps.newHashMap();
	// for (Indiv indiv : currIndivList) {
	// indivMap.put(indiv.getIndivCode(), indiv);
	// }
	// for (IndivFlow div : indivList) {
	// if (!indivMap.containsKey(div.getIndivCode())) {
	// throw new ServiceException("此商品个体编号不存在：" + div.getIndivCode());
	// }
	// Indiv currIndiv = indivMap.get(div.getIndivCode());
	// if (currIndiv.getSku().getId().longValue() !=
	// stockInItem.getSku().getId().longValue()) {
	// throw new ServiceException("此编号对应的商品个体不归属于对应SKU：" +
	// currIndiv.getIndivCode());
	// }
	// if
	// (!IndivStockStatus.OUT_WAREHOUSE.getCode().equals(currIndiv.getStockStatus()))
	// {
	// throw new ServiceException("此编号对应的商品尚未出库：" + currIndiv.getIndivCode());
	// }
	// if (!currIndiv.getOrderCode().equals(stockInItem.getOriginalCode())) {
	// throw new ServiceException("此编号商品与原订单不符：" + currIndiv.getIndivCode());
	// }
	// }
	//
	// // 添加明细项
	// try {
	// // --根据入库编码判断入库单是否已存在?不存在，则先初始化入库单，否则为入库明细项关联起入库单 --
	// StockIn stockIn =
	// getStockInByCode(stockInItem.getStockIn().getStockInCode());
	// if (stockIn == null) {// 不存在
	// stockIn = new StockIn();
	// stockIn.setStockInCode(stockInItem.getStockIn().getStockInCode());
	// stockIn.setStockInType(StockInType.RMA.getCode());
	// stockIn.setHandlingStatus(StockInStatus.PENDING.getCode());
	// stockIn.setPreparedBy(ActionUtils.getLoginName());
	// stockIn.setPreparedTime(new Date());
	// stockIn.setEnabled(true);
	// // 初始化入库单
	// stockInDao.addInitialStockIn(stockIn);
	// // 为入库明细项关联起入库单
	// stockInItem.setStockIn(stockIn);
	// } else {
	// stockInItem.setStockIn(stockIn);
	// }
	// stockInItem.setIndivFinished(WmsConstants.ENABLED_TRUE);
	// // 保存入库明细项信息
	// stockInDao.addStockInItem(stockInItem);
	//
	// // 添加商品个体流转信息
	// for (IndivFlow indivFlow : indivList) {
	// indivFlow.setFlowType(IndivFlowType.IN_RMA.getCode());
	// indivFlow.setFlowCode(stockInItem.getStockIn().getStockInCode());
	// indivFlow.setWaresStatus(stockInItem.getWaresStatus());
	// indivFlow.setSkuId(stockInItem.getSku().getId());
	// indivFlow.setFlowItemId(stockInItem.getId());
	// indivFlow.setMeasureUnit(stockInItem.getMeasureUnit());
	// indivFlow.setBatchNumber(stockInItem.getBatchNumber());
	// indivFlow.setEnabled(WmsConstants.ENABLED_FALSE);
	// }
	// indivDao.addIndivFlows(indivList);
	// } catch (DataAccessException e) {
	// throw new ServiceException(e);
	// }
	// }

	@Override
	public void updateStockInItemAndIndivs(StockInItem stockInItem, List<IndivFlow> indivList) throws ServiceException {
		if (CollectionUtils.isEmpty(indivList)) {
			return;
		}
		if (indivList.size() != stockInItem.getQuantity()) {
			throw new ServiceException("输入商品编码数目与商品数量不符");
		}
		StockIn stockIn = stockInItem.getStockIn();
		// if
		// (!StockOutStatus.PENDING.getCode().equals(stockIn.getHandlingStatus()))
		// {
		// throw new ServiceException("入库单状态异常");
		// }

		// 效验商品个体信息
		Map<String, IndivFlow> indivMap = Maps.newHashMap();
		for (IndivFlow indivFlow : indivList) {
			indivMap.put(indivFlow.getIndivCode(), indivFlow);
		}
		if (indivMap.size() < indivList.size()) {
			throw new ServiceException("输入商品编码存在重复");
		}

		// 更新入库明细项信息
		try {
			stockInDao.updateStockInItem(stockInItem);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

		// 删除旧的商品个体信息
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("flowType", IndivFlowType.IN_PURCHASE.getCode());
		criteria.put("flowItemId", stockInItem.getId());
		try {
			indivDao.deleteIndivFlowsByFlowGoodsId(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

		// 批量添加新的入库商品身份信息
		try {
			for (IndivFlow indivFlow : indivList) {
				indivFlow.setFlowType(IndivFlowType.IN_PURCHASE.getCode());
				indivFlow.setFlowCode(stockInItem.getStockIn().getStockInCode());
				indivFlow.setWaresStatus(Integer.parseInt(stockInItem.getWaresStatus()));
				indivFlow.setSkuId(stockInItem.getSku().getId());
				indivFlow.setFlowGoodsId(stockInItem.getId());
				indivFlow.setMeasureUnit(stockInItem.getMeasureUnit());
				indivFlow.setProductBatchNo(stockInItem.getBatchNumber());
				indivFlow.setEnabled(WmsConstants.ENABLED_FALSE);
			}
			indivDao.addIndivFlows(indivList);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateStockInItem(StockInItem stockInItem) throws ServiceException {
		StockIn stockIn = stockInItem.getStockIn();
		// if
		// (!StockOutStatus.PENDING.getCode().equals(stockIn.getHandlingStatus()))
		// {
		// throw new ServiceException("入库单状态异常");
		// }
		try {
			stockInDao.updateStockInItem(stockInItem);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void confirmStockIn(StockIn stockIn) throws ServiceException {
		if (!StockInStatus.PENDING.getCode().equals(stockIn.getHandlingStatus())) {
			throw new ServiceException("入库单状态异常");
		}
		try {
			// 1.更新入库单确认信息
			stockIn.setPreparedBy(ActionUtils.getLoginName());
			stockIn.setFinishedTime(new Date());
			stockIn.setHandlingStatus(WmsConstants.StockInStatus.FINISHED.getCode());
			stockIn.setHandledBy(ActionUtils.getLoginName());
			stockIn.setHandledDate(new Date());
			stockInDao.updateStockIn(stockIn);

			// 2.如果存在商品个体信息，则进行个体信息的相关处理
			List<IndivFlow> indivFlowList = getIndivFlowsByStockInCode(IndivFlowType.IN_PURCHASE.getCode(), stockIn
					.getStockInCode());
			if (CollectionUtils.isNotEmpty(indivFlowList)) {
				// 判断商品编码是否存在重复添加
				List<String> indivCodes = Lists.newArrayList();
				for (IndivFlow flow : indivFlowList) {
					indivCodes.add(flow.getIndivCode());
				}
				List<Indiv> currIndivList = indivDao.queryIndivListByCodes(indivCodes);
				if (CollectionUtils.isNotEmpty(currIndivList)) {
					throw new ServiceException("此商品编号已经存在：" + currIndivList.get(0).getIndivCode());
				}

				// 更新商品个体流转信息
				Map<String, Object> criteria = Maps.newHashMap();
				criteria.put("flowType", IndivFlowType.IN_PURCHASE.getCode());
				criteria.put("flowCode", stockIn.getStockInCode());
				criteria.put("warehouseCode", stockIn.getWarehouse().getWarehouseCode());
				criteria.put("flowTime", new Date());
				criteria.put("enabled", WmsConstants.ENABLED_TRUE);
				indivDao.updateIndivFlowsByFlowCode(criteria);
				// 添加商品个体信息
				List<Indiv> indivList = Lists.newArrayList();
				for (IndivFlow flow : indivFlowList) {
					Indiv indiv = new Indiv();
					indiv.setIndivCode(flow.getIndivCode());
					// indiv.setSku(flow.getSku());
					indiv.setSkuId(flow.getSkuId());
					indiv.setSkuCode(flow.getSkuCode());
					indiv.setSkuName(flow.getSkuName());
					// indiv.setWarehouse(stockIn.getWarehouse());
					// indiv.setWarehouseCode(stockIn.getWarehouse().getWarehouseCode());
					indiv.setWarehouseName(stockIn.getWarehouse().getWarehouseName());
					indiv.setMeasureUnit(flow.getMeasureUnit());
					// indiv.setStockInTime(new Date());
					// indiv.setStockInCode(flow.getFlowCode());
					// indiv.setBatchNumber(StringUtils.isBlank(flow.getBatchNumber())
					// ? "" : flow.getBatchNumber());
					indiv.setRmaCount(0);
					indiv.setWaresStatus(flow.getWaresStatus());
					indiv.setStockStatus(IndivStockStatus.IN_WAREHOUSE.getCode());
					indiv.setPushStatus(RemoteCallStatus.PENDING.getCode());
					indiv.setPushCount(0);
					indivList.add(indiv);
				}
				indivDao.addIndivs(indivList);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		// 3.遍历入库明细项，增加库存
		List<ReceiveSummary> detailSummaryList = getStockInSummaryList(stockIn.getStockInCode());
		if (CollectionUtils.isEmpty(detailSummaryList)) {
			throw new ServiceException("入库汇总为空");
		}
		for (ReceiveSummary summary : detailSummaryList) {
			StockType stockType;
			if (IndivWaresStatus.NON_DEFECTIVE.getCode() == summary.getWaresStatus()) {
				stockType = StockType.STOCK_SALES;
			} else {
				stockType = StockType.STOCK_UNSALES;
			}
			StockRequest stockRequest = new StockRequest(summary.getWarehouseId(), summary.getSkuId(), stockType,
					summary.getQuantity(), StockBizType.IN_PURCHASE, stockIn.getStockInCode());
			stockService.increaseStock(stockRequest);
		}

		// TODO:同步库存到外围系统

	}

	@Override
	public void confirmRmaIn(StockIn stockIn) throws ServiceException {
		if (!StockInStatus.PENDING.getCode().equals(stockIn.getHandlingStatus())) {
			throw new ServiceException("入库单状态异常");
		}
		List<SalesOrder> backOrders=null;
		try {
			// 1.更新入库单确认信息
			stockIn.setFinishedTime(new Date());
			stockIn.setHandlingStatus(WmsConstants.StockInStatus.FINISHED.getCode());
			stockIn.setHandledBy(ActionUtils.getLoginName());
			stockIn.setHandledDate(new Date());
			stockInDao.updateStockIn(stockIn);

			// 更新销售订单退货信息
//			backOrders = orderDao.querySalesOrdersByInCode(stockIn.getStockInCode());
			for (SalesOrder order : backOrders) {
				order.setOrderStatus(OrderStatus.BACKED.getCode());
			}
			orderDao.batchUpdateOrder(backOrders);

			// 2.如果存在商品个体信息，则进行个体信息的相关处理
			List<IndivFlow> indivFlowList = getIndivFlowsByStockInCode(IndivFlowType.IN_RMA.getCode(), stockIn
					.getStockInCode());
			if (CollectionUtils.isNotEmpty(indivFlowList)) {
				// 校验商品个体信息
				List<String> indivCodes = Lists.newArrayList();
				for (IndivFlow flow : indivFlowList) {
					indivCodes.add(flow.getIndivCode());
				}
				List<Indiv> currIndivList = indivDao.queryIndivListByCodes(indivCodes);
				if (CollectionUtils.isEmpty(currIndivList)) {
					throw new ServiceException("商品编号不存在");
				}
				Map<String, Indiv> indivMap = Maps.newHashMap();
				for (Indiv indiv : currIndivList) {
					indivMap.put(indiv.getIndivCode(), indiv);
				}
				for (IndivFlow flow : indivFlowList) {
					if (!indivMap.containsKey(flow.getIndivCode())) {
						throw new ServiceException("此商品个体编号不存在：" + flow.getIndivCode());
					}
					Indiv currIndiv = indivMap.get(flow.getIndivCode());
					if (currIndiv.getSkuId().longValue() != flow.getSkuId().longValue()) {
						throw new ServiceException("此编号对应的商品个体不归属于对应SKU：" + currIndiv.getIndivCode());
					}
					if (IndivStockStatus.OUT_WAREHOUSE.getCode() != currIndiv.getStockStatus()) {
						throw new ServiceException("此编号对应的商品个体未出库：" + currIndiv.getIndivCode());
					}
				}

				// 更新商品个体流转信息
				Map<String, Object> criteria = Maps.newHashMap();
				criteria.put("flowType", IndivFlowType.IN_RMA.getCode());
				criteria.put("flowCode", stockIn.getStockInCode());
				criteria.put("warehouseCode", stockIn.getWarehouse().getWarehouseCode());
				criteria.put("flowTime", new Date());
				criteria.put("enabled", WmsConstants.ENABLED_TRUE);
				indivDao.updateIndivFlowsByFlowCode(criteria);

				// 更新商品个体退货信息
				criteria.clear();
				criteria.put("flowType", IndivFlowType.IN_RMA.getCode());
				criteria.put("flowCode", stockIn.getStockInCode());
				criteria.put("stockStatus", IndivStockStatus.IN_WAREHOUSE.getCode());
				indivDao.updateIndivsRmaInfo(criteria);
			}
		} catch (Exception e) {
			throw new ServiceException(e);
		}

		// 3.遍历退货入库明细项，增加库存
		List<ReceiveSummary> detailSummaryList = getStockInSummaryList(stockIn.getStockInCode());
		if (CollectionUtils.isEmpty(detailSummaryList)) {
			throw new ServiceException("入库汇总为空");
		}
		for (ReceiveSummary summary : detailSummaryList) {
			StockType stockType;
			if (IndivWaresStatus.NON_DEFECTIVE.getCode() == summary.getWaresStatus()) {
				stockType = StockType.STOCK_SALES;
			} else {
				stockType = StockType.STOCK_UNSALES;
			}
			StockRequest stockRequest = new StockRequest(summary.getWarehouseId(), summary.getSkuId(), stockType,
					summary.getQuantity(), StockBizType.IN_RMA, stockIn.getStockInCode());
			stockService.increaseStock(stockRequest);
		}

		// 通知订单中心已退货
		salesOrderService.notifyOrder(backOrders);
	}

	/**
	 * 取指定入库编号的汇总列表
	 */
	private List<ReceiveSummary> getStockInSummaryList(String stockInCode) throws ServiceException {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("stockInCode", stockInCode);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		return getStockInSummaryList(criteria, page);
	}

	/**
	 * 查询指定入库编号的所有商品个体流转信息
	 */
	private List<IndivFlow> getIndivFlowsByStockInCode(String stockInType, String stockInCode) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("flowType", stockInType);
		criteria.put("flowCode", stockInCode);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		criteria.put("page", page);
		try {
			return indivDao.queryIndivFlowByPage(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void deleteStockIn(String stockInCode) throws ServiceException {
		deleteStockIn(getStockInByCode(stockInCode));
	}

	@Override
	public void deleteStockIn(Long id) throws ServiceException {
		deleteStockIn(getStockIn(id));
	}

	private void deleteStockIn(StockIn stockIn) throws ServiceException {
		if (stockIn == null) {
			return;
		}
		if (!StockInStatus.PENDING.getCode().equals(stockIn.getHandlingStatus())) {
			throw new ServiceException("入库单状态异常");
		}
		if (ReceiveType.PURCHASE.getCode().equals(stockIn.getStockInType())) {
			deletePurchaseIn(stockIn);
		} else if (ReceiveType.RMA.getCode().equals(stockIn.getStockInType())) {
			deleteRmaIn(stockIn);
		}
	}

	private void deletePurchaseIn(StockIn stockIn) throws ServiceException {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("flowType", IndivFlowType.IN_PURCHASE.getCode());
		criteria.put("flowCode", stockIn.getStockInCode());
		try {
			// 删除入库单的所有商品个体流转信息
			indivDao.deleteIndivFlowsByFlowCode(criteria);
			// 删除入库单的所有明细项
			stockInDao.deleteStockInItems(stockIn.getStockInCode());
			// 删除入库单
			stockInDao.deleteStockIn(stockIn.getId());
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	private void deleteRmaIn(StockIn stockIn) throws ServiceException {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("flowType", IndivFlowType.IN_RMA.getCode());
		criteria.put("flowCode", stockIn.getStockInCode());
		try {
			// 删除入库单的所有商品个体流转信息
			indivDao.deleteIndivFlowsByFlowCode(criteria);
			// 删除入库单的所有明细项
			stockInDao.deleteStockInItems(stockIn.getStockInCode());
			// 删除入库单
			stockInDao.deleteStockIn(stockIn.getId());
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<StockIn> getStockInList(Map<String, Object> criteria, Page page) {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		try {
			return stockInDao.queryStockInList(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer getStockInListTotal(Map<String, Object> criteria) {
		return stockInDao.queryStockInListTotal(criteria);
	}

	// @Override
	// public StockIn getStockInWithDetail(Long stockInId) {
	// StockIn stockIn = getStockIn(stockInId);
	// if (stockIn != null) {
	// stockIn.setStockInDetail(getStockInDetail(stockInId));
	// }
	// return stockIn;
	// }

	@Override
	public StockIn getStockIn(Long id) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("id", id);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		List<StockIn> list = getStockInList(criteria, page);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	@Override
	public StockIn getStockInByCode(String stockInCode) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("stockInCode", stockInCode);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		List<StockIn> list = getStockInList(criteria, page);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	@Override
	public List<StockInItem> getStockInDetailList(Map<String, Object> criteria, Page page) {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		try {
			return stockInDao.queryStockInDetailList(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer getStockInDetailListTotal(Map<String, Object> criteria) {
		return stockInDao.queryStockInDetailListTotal(criteria);
	}

	@Override
	public List<StockInItem> getStockInDetail(Long stockInId) throws ServiceException {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("stockInId", stockInId);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		return getStockInDetailList(criteria, page);
	}

	@Override
	public StockInItem getStockInItem(Long stockInItemId) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("stockInItemId", stockInItemId);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		List<StockInItem> list = getStockInDetailList(criteria, page);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	@Override
	public void deleteStockInItem(Long stockInItemId) throws ServiceException {
		StockInItem stockInItem = getStockInItem(stockInItemId);
		if (stockInItem == null) {
			return;
		}
		if (!StockInStatus.PENDING.getCode().equals(stockInItem.getStockIn().getHandlingStatus())) {
			throw new ServiceException("入库单状态异常");
		}
		if (ReceiveType.PURCHASE.getCode().equals(stockInItem.getStockIn().getStockInType())) {
			deletePurchaseInItem(stockInItem);
		} else if (ReceiveType.RMA.getCode().equals(stockInItem.getStockIn().getStockInType())) {
			deleteRmaInItem(stockInItem);
		}
	}

	private void deletePurchaseInItem(StockInItem stockInItem) throws ServiceException {
		try {
			// 删除入库明细项
			stockInDao.deleteStockInItem(stockInItem.getId());
			// 删除商品个体流转信息
			Map<String, Object> criteria = Maps.newHashMap();
			criteria.put("flowType", IndivFlowType.IN_PURCHASE.getCode());
			criteria.put("flowItemId", stockInItem.getId());
			indivDao.deleteIndivFlowsByFlowGoodsId(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	private void deleteRmaInItem(StockInItem stockInItem) throws ServiceException {
		try {
			// 删除入库明细项
			stockInDao.deleteStockInItem(stockInItem.getId());
			// 删除商品个体流转信息
			Map<String, Object> criteria = Maps.newHashMap();
			criteria.put("flowType", IndivFlowType.IN_RMA.getCode());
			criteria.put("flowItemId", stockInItem.getId());
			indivDao.deleteIndivFlowsByFlowGoodsId(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<ReceiveSummary> getStockInSummaryList(Map<String, Object> criteria, Page page) throws ServiceException {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		try {
			return stockInDao.queryStockInSummaryList(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer getStockInSummaryListTotal(Map<String, Object> criteria) {
		return stockInDao.queryStockInSummaryListTotal(criteria);
	}

	@Override
	public void addRmaIndiv(IndivFlow indivFlow) throws ServiceException {
//		SalesOrder salesOrder = getSalesOrder(indivFlow.getOriginalCode());
//		if (salesOrder == null) {
//			throw new ServiceException("源订单不存在");
//		}
////		if (ShippingStatus.SHIPPED.getCode() != salesOrder.getShippingStatus()) {
////			throw new ServiceException("源订单尚未完成出库");
////		}
//		try {
//			// 检测入库单是否已存在，不存在则先初始化入库单
//			StockIn stockIn = getStockInByCode(indivFlow.getFlowCode());
//			if (stockIn == null) {// 不存在
//				stockIn = new StockIn();
//				stockIn.setStockInCode(indivFlow.getFlowCode());
//				stockIn.setStockInType(ReceiveType.RMA.getCode());
//				stockIn.setHandlingStatus(StockInStatus.PENDING.getCode());
//				stockIn.setPreparedBy(ActionUtils.getLoginName());
//				stockIn.setPreparedTime(new Date());
//				stockIn.setEnabled(true);
//				stockInDao.addInitialStockIn(stockIn);
//			}
//			// 添加入库明细项
//			StockInItem stockInItem = new StockInItem();
//			stockInItem.setStockIn(stockIn);
//			stockInItem.setSkuId(indivFlow.getSkuId());
//			stockInItem.setMeasureUnit(indivFlow.getMeasureUnit());
//			stockInItem.setQuantity(1);
//			stockInItem.setIndivEnabled(WmsConstants.ENABLED_TRUE);
//			stockInItem.setIndivFinished(WmsConstants.ENABLED_TRUE);
//			stockInItem.setWaresStatus(indivFlow.getWaresStatus() + "");
//			stockInItem.setOriginalCode(indivFlow.getOriginalCode());
//			stockInItem.setRemark(indivFlow.getIndivCode());
//			stockInDao.addStockInItem(stockInItem);
//
//			// 添加商品个体流转信息
//			indivFlow.setFlowType(IndivFlowType.IN_RMA.getCode());
//			indivFlow.setFlowGoodsId(stockInItem.getId());
//			indivFlow.setEnabled(WmsConstants.ENABLED_FALSE);
//			indivFlow.setSkuId(indivFlow.getSkuId());
//			indivDao.addIndivFlows(Lists.newArrayList(indivFlow));
//		} catch (Exception e) {
//			throw new ServiceException(e);
//		}
	}

	@Autowired
	public void setStockInDao(StockInDao stockInDao) {
		this.stockInDao = stockInDao;
	}

	@Autowired
	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

	@Autowired
	public void setIndivDao(IndivDao indivDao) {
		this.indivDao = indivDao;
	}

	@Autowired
	public void setSalesOrderDao(SalesOrderDao orderDao) {
		this.orderDao = orderDao;
	}

	@Autowired
	public void setDeliveryDao(DeliveryDao deliveryDao) {
		this.deliveryDao = deliveryDao;
	}

	@Autowired
	public void setTaskExecutor(TaskExecutor taskExecutor) {
		this.taskExecutor = taskExecutor;
	}

	@Autowired
	public void setOrderCenterClient(OrderCenterClient orderCenterClient) {
		this.orderCenterClient = orderCenterClient;
	}
}
