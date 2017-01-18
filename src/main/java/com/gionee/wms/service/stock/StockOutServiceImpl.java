package com.gionee.wms.service.stock;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.DeliveryStatus;
import com.gionee.wms.common.WmsConstants.IndivFlowType;
import com.gionee.wms.common.WmsConstants.IndivStockStatus;
import com.gionee.wms.common.WmsConstants.IndivWaresStatus;
import com.gionee.wms.common.WmsConstants.OrderStatus;
import com.gionee.wms.common.WmsConstants.StockOutType;
import com.gionee.wms.dao.DeliveryDao;
import com.gionee.wms.dao.IndivDao;
import com.gionee.wms.dao.SalesOrderDao;
import com.gionee.wms.dao.StockOutDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Delivery;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.entity.StockOut;
import com.gionee.wms.entity.StockOutItem;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.common.CommonServiceImpl;
import com.gionee.wms.web.client.OrderCenterClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service("stockOutService")
public class StockOutServiceImpl extends CommonServiceImpl implements StockOutService {
	private static Logger logger = LoggerFactory.getLogger(StockOutServiceImpl.class);
	private IndivDao indivDao;
	private StockOutDao stockOutDao;
	private SalesOrderDao orderDao;
	private DeliveryDao deliveryDao;
	private StockService stockService;
	private TaskExecutor taskExecutor;
	private OrderCenterClient orderCenterClient;

	public void addStockOut(StockOut stockOut, List<String> orderCodes) throws ServiceException {
		if (CollectionUtils.isEmpty(orderCodes)) {
			throw new ServiceException("销售订单为空");
		}
		// 添加出库单
		stockOut.setStockOutType(StockOutType.SALES.getCode());
		stockOut.setStockOutCode(getBizCode(BATCH_OUT));
		stockOut.setPreparedTime(new Date());
		stockOut.setEnabled(WmsConstants.ENABLED_TRUE);
//		stockOut.setHandlingStatus(StockOutStatus.PENDING.getCode());
		try {
			stockOutDao.addStockOut(stockOut);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

		// 根据销售订单批量创建发货单
		createDeliverys(orderCodes, stockOut);
	}

	public void createDeliverys(List<String> orderCodes, StockOut stockOut) throws ServiceException {
		// 检测待发货销售订单是否满足发货条件
		try {
			List<SalesOrder> orders = orderDao.queryOrderListByOrderCodes(orderCodes);
			if (CollectionUtils.isEmpty(orders) || orders.size() != orderCodes.size()) {
				throw new ServiceException("订单号不存在");
			}
			for (SalesOrder order : orders) {
				if (OrderStatus.FILTERED.getCode() != order.getOrderStatus()) {
					throw new ServiceException("订单配送状态异常");
				}
			}

			// 批量创建发货单
			List<Delivery> deliverys = Lists.newArrayList();
			for (SalesOrder order : orders) {
				Delivery delivery = new Delivery();
				BeanUtils.copyProperties(delivery, order);
				delivery.setDeliveryCode(getBizCode(DELIVERY));
				delivery.setBatchId(stockOut.getId());
				delivery.setBatchCode(stockOut.getStockOutCode());
				delivery.setWarehouseId(stockOut.getWarehouseId());
//				delivery.setWarehouseCode(stockOut.getWarehouseCode());
				delivery.setWarehouseName(stockOut.getWarehouseName());
				delivery.setOriginalCode(order.getOrderCode());
				delivery.setInvoiceStatus(WmsConstants.ENABLED_FALSE);
				delivery.setHandlingStatus(DeliveryStatus.UNSHIPPED.getCode());
				delivery.setPreparedBy(stockOut.getPreparedBy());
				delivery.setPreparedTime(new Date());
				deliverys.add(delivery);
			}
			deliveryDao.addDeliveryList(deliverys);

			// 批量添加发货商品
//			deliveryDao.addDeliveryGoodsListByOrderCodes(orderCodes);

			// 批量更新销售订单状态
			for (SalesOrder order : orders){
				order.setOrderStatus(OrderStatus.PICKED.getCode());
			}
			orderDao.batchUpdateOrder(orders);
		} catch (Exception e) {
			throw new ServiceException(e);
		}
	}

	public List<StockOut> getStockOutList(Map<String, Object> criteria, Page page) {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		try {
			return stockOutDao.queryStockOutList(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	public Integer getStockOutListTotal(Map<String, Object> criteria) {
		return stockOutDao.queryStockOutListTotal(criteria);
	}

	public StockOut getStockOut(Long id) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("id", id);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		List<StockOut> list = getStockOutList(criteria, page);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	public StockOut getStockOutByCode(String stockOutCode) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("stockOutCode", stockOutCode);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		List<StockOut> list = getStockOutList(criteria, page);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	public void updateStockOut(StockOut stockOut) throws ServiceException {
		try {
			stockOutDao.updateStockOut(stockOut);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	public void deleteDeliveryOrder(String orderCode) throws ServiceException {
		SalesOrder deliveryOrder =null;
		try {
//			deliveryOrder = orderDao.queryOrderByOrderCode(orderCode);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		if (deliveryOrder == null) {
			throw new ServiceException("发货订单不存在");
		}
		if (OrderStatus.PICKED.getCode() != deliveryOrder.getOrderStatus()) {
			throw new ServiceException("订单状态异常");
		}

		// 移除发货订单
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("orderCode", orderCode);
		criteria.put("stockOutCode", "");
		criteria.put("orderStatus", OrderStatus.FILTERED.getCode());// 更改订单状态
		try {
			orderDao.updateOrder(criteria);

			// 删除订单的发货商品
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

//	/**
//	 * 判断是否能取消
//	 */
//	private boolean canCancel(String stockOutCode) {
//		try {
//			return StringUtils.isNotBlank(deliveryDao.queryBatchWhetherIsCancelable(stockOutCode));
//		} catch (DataAccessException e) {
//			throw new ServiceException(e);
//		}
//	}

//	@Override
//	public void cancelStockOutByCode(String stockOutCode) {
//		StockOut stockOut = getStockOutByCode(stockOutCode);
//		if (stockOut == null) {
//			return;
//		}
////		if (StockOutStatus.FINISHED.getCode().equals(stockOut.getHandlingStatus())) {
////			throw new ServiceException("出库单状态异常");
////		}
//		if (!canCancel(stockOutCode)) {
//			throw new ServiceException("存在发票、配送单号、商品编码的操作，不能取消");
//		}
//
//		try {
//			// 更新出库批次内的订单配送状态为未发货状态
//			orderDao.updateToUnshippedByBatch(stockOutCode);
//
//			// 删除批次内的发货单
//			deliveryDao.deleteDeliveryByBatch(stockOutCode);
//
//			// 删除批次内的发货商品
//			deliveryDao.deleteDeliveryGoodsByBatch(stockOutCode);
//
//			// 删除出库批次
//			stockOutDao.deleteStockOut(stockOut.getId());
//		} catch (DataAccessException e) {
//			throw new ServiceException(e);
//		}
//	}

	public List<StockOutItem> getStockOutDetailList(Map<String, Object> criteria, Page page) {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		try {
			return stockOutDao.queryStockOutDetailList(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	public Integer getStockOutDetailListTotal(Map<String, Object> criteria) {
		return stockOutDao.queryStockOutDetailListTotal(criteria);
	}

	public List<StockOutItem> getStockOutDetail(String orderCode) throws ServiceException {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("orderCode", orderCode);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		return getStockOutDetailList(criteria, page);
	}

	public List<SalesOrderGoods> getSalesOrderOutDetail(String orderCode) throws ServiceException {
		List<SalesOrderGoods> orderOutDetail = Lists.newArrayList();
		List<StockOutItem> stockOutDetail = getStockOutDetail(orderCode);
		for (StockOutItem stockOutItem : stockOutDetail) {
			SalesOrderGoods orderItem = new SalesOrderGoods();
			try {
				BeanUtils.copyProperties(orderItem, stockOutItem);
			} catch (Exception e) {
				throw new ServiceException(e);
			}
			orderOutDetail.add(orderItem);
		}
		return orderOutDetail;
	}

	public StockOutItem getStockOutItem(Long stockOutItemId) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("stockOutItemId", stockOutItemId);
		Page page = new Page();
		page.setStartRow(1);
		page.setEndRow(Integer.MAX_VALUE);
		List<StockOutItem> list = getStockOutDetailList(criteria, page);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	public void addStockOutItemAndIndivs(StockOutItem stockOutItem, List<Indiv> indivList) throws ServiceException {
		StockOut stockOut = getStockOutByCode(stockOutItem.getStockOut().getStockOutCode());
		// 效验商品个体信息
		Map<String, Indiv> indivMap = Maps.newHashMap();
		for (Indiv indiv : indivList) {
			indivMap.put(indiv.getIndivCode(), indiv);
		}
		if (indivMap.size() < indivList.size()) {
			throw new ServiceException("商品个体编码重复");
		}
		List<Indiv> currIndivList;
		try {
			currIndivList = indivDao.queryIndivListByCodes(Lists.newArrayList(indivMap.keySet()));
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
		if (CollectionUtils.isEmpty(currIndivList)) {
			throw new ServiceException("商品个体编号不存在");
		}
		indivMap.clear();
		for (Indiv indiv : currIndivList) {
			indivMap.put(indiv.getIndivCode(), indiv);
		}
		for (Indiv div : indivList) {
			if (!indivMap.containsKey(div.getIndivCode())) {
				throw new ServiceException("此商品个体编号不存在：" + div.getIndivCode());
			}
			Indiv currIndiv = indivMap.get(div.getIndivCode());
			if (IndivStockStatus.OUT_WAREHOUSE.getCode()==currIndiv.getStockStatus()) {
				throw new ServiceException("此编号对应的商品个体已经出库：" + currIndiv.getIndivCode());
			}
			if (IndivWaresStatus.DEFECTIVE.getCode() == currIndiv.getWaresStatus()) {
				throw new ServiceException("此编号对应的商品个体是次品：" + currIndiv.getIndivCode());
			}
			if (currIndiv.getWarehouseId().longValue() != stockOut.getWarehouseId().longValue()) {
				throw new ServiceException("此编号与出库仓不符：" + currIndiv.getIndivCode());
			}
			if (StringUtils.isNotBlank(currIndiv.getOrderCode())) {
				throw new ServiceException("此编号商品个体已经被其他订单占用：" + currIndiv.getIndivCode());
			}
		}
		// 添加出库明细项
		addStockOutItem(stockOutItem);

		// 为商品个体绑定出库相关信息
		for (Indiv indiv : currIndivList) {
			indiv.setStockOut(stockOut);
			// indiv.setStockOutItem(stockOutItem);
		}
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("stockOutCode", stockOut.getStockOutCode());
		criteria.put("stockOutItemId", stockOutItem.getId());
		criteria.put("orderCode", stockOutItem.getOriginalCode());
		criteria.put("indivCodes", indivMap.keySet());
		try {
			indivDao.updateIndivsOutInfo(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

	}

	public void addStockOutItem(StockOutItem stockOutItem) throws ServiceException {
		try {
			StockOut stockOut = getStockOutByCode(stockOutItem.getStockOut().getStockOutCode());
//			if (stockOut == null || StockOutStatus.FINISHED.getCode().equals(stockOut.getHandlingStatus())) {// 不存在
//				throw new ServiceException("出库单不存在或状态异常");
//			}
			// 保存出库明细项信息
			stockOutDao.addStockOutItem(stockOutItem);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

//	@Override
//	public void updateStockOutIndivs(StockOutItem stockOutItem, List<IndivFlow> indivList) throws ServiceException {
//		if (CollectionUtils.isEmpty(indivList)) {
//			return;
//		}
//		if (indivList.size() > stockOutItem.getQuantity()) {
//			throw new ServiceException("商品身份码数目大于商品实际数目");
//		}
//		StockOut stockOut = stockOutItem.getStockOut();
//		if (!StockOutStatus.PENDING.getCode().equals(stockOut.getHandlingStatus())) {
//			throw new ServiceException("出库单状态异常");
//		}
//		SalesOrder salesOrder = stockOutItem.getSalesOrder();
//		if (ShippingStatus.PICKED.getCode() != salesOrder.getShippingStatus()) {
//			throw new ServiceException("订单状态异常");
//		}
//		// 效验商品个体信息
//		Map<String, IndivFlow> indivFlowMap = Maps.newHashMap();
//		for (IndivFlow indiv : indivList) {
//			indivFlowMap.put(indiv.getIndivCode(), indiv);
//		}
//		if (indivFlowMap.size() < indivList.size()) {
//			throw new ServiceException("商品个体编码存在重复");
//		}
//
//		// 校验新的商品个体身份信息
//		List<Indiv> currIndivList;
//		try {
//			currIndivList = indivDao.queryIndivListByCodes(Lists.newArrayList(indivFlowMap.keySet()));
//		} catch (DataAccessException e) {
//			throw new ServiceException(e);
//		}
//		if (CollectionUtils.isEmpty(currIndivList)) {
//			throw new ServiceException("商品个体编号不存在");
//		}
//		Map<String, Indiv> indivMap = Maps.newHashMap();
//		for (Indiv indiv : currIndivList) {
//			indivMap.put(indiv.getIndivCode(), indiv);
//		}
//		for (IndivFlow flow : indivList) {
//			if (!indivMap.containsKey(flow.getIndivCode())) {
//				throw new ServiceException("此商品个体编号不存在：" + flow.getIndivCode());
//			}
//			Indiv currIndiv = indivMap.get(flow.getIndivCode());
//			if (currIndiv.getSkuId().longValue() != stockOutItem.getSku().getId().longValue()) {
//				throw new ServiceException("此编号对应的商品个体不归属于对应SKU：" + currIndiv.getIndivCode());
//			}
//			if (!currIndiv.getWarehouse().getWarehouseCode().equals(stockOut.getWarehouseCode())) {
//				throw new ServiceException("此编号商品所在仓库与出仓不符：" + currIndiv.getIndivCode());
//			}
//			if (IndivStockStatus.OUT_WAREHOUSE.getCode()==currIndiv.getStockStatus()) {
//				throw new ServiceException("此编号对应的商品个体已经出库：" + currIndiv.getIndivCode());
//			}
//			if (IndivStockStatus.IN_WAREHOUSE.getCode()!=currIndiv.getStockStatus()) {
//				throw new ServiceException("此编号对应的商品个体不存在：" + currIndiv.getIndivCode());
//			}
//			if (IndivWaresStatus.DEFECTIVE.getCode() == currIndiv.getWaresStatus()) {
//				throw new ServiceException("此编号对应的商品个体是次品：" + currIndiv.getIndivCode());
//			}
//			if (StringUtils.isNotBlank(currIndiv.getOrderCode())
//					&& !currIndiv.getOrderCode().equals(stockOutItem.getOriginalCode())) {
//				throw new ServiceException("此编号商品个体已经被其他订单占用：" + currIndiv.getIndivCode());
//			}
//		}
//
//		// 清空与当前出库明细项相关联的商品个体的出库信息
//		// Map<String, Object> criteria = Maps.newHashMap();
//		// criteria.put("stockOutCode", "");
//		// criteria.put("stockOutItemId", "");
//		// criteria.put("orderCode", "");
//		// criteria.put("stockOutItemIds",
//		// Lists.newArrayList(stockOutItem.getId()));
//		// try {
//		// indivDao.updateIndivsByOutItemIds(criteria);
//		// } catch (DataAccessException e) {
//		// throw new ServiceException(e);
//		// }
//		// 删除旧的商品个体信息
//		Map<String, Object> criteria = Maps.newHashMap();
//		criteria.put("flowType", IndivFlowType.OUT_SALES.getCode());
//		criteria.put("flowItemId", stockOutItem.getId());
//		try {
//			indivDao.deleteIndivFlowsByFlowItemId(criteria);
//		} catch (DataAccessException e) {
//			throw new ServiceException(e);
//		}
//
//		// // 重新绑定新的商品个体出库信息
//		// criteria.clear();
//		// criteria = Maps.newHashMap();
//		// criteria.put("stockOutCode", stockOut.getStockOutCode());
//		// criteria.put("stockOutItemId", stockOutItem.getId());
//		// criteria.put("orderCode", stockOutItem.getOrderCode());
//		// criteria.put("indivCodes", Lists.newArrayList(indivMap.keySet()));
//		// try {
//		// indivDao.updateIndivsByCodes(criteria);
//		// } catch (DataAccessException e) {
//		// throw new ServiceException(e);
//		// }
//		// 批量添加新的入库商品身份信息
//		try {
//			for (IndivFlow indivFlow : indivList) {
//				indivFlow.setFlowType(IndivFlowType.OUT_SALES.getCode());
//				indivFlow.setFlowCode(stockOutItem.getStockOut().getStockOutCode());
//				// indivFlow.setWaresStatus(stockOutItem.getWaresStatus());
//				indivFlow.setSkuId(stockOutItem.getSku().getId());
//				indivFlow.setFlowItemId(stockOutItem.getId());
//				indivFlow.setMeasureUnit(stockOutItem.getMeasureUnit());
//				indivFlow.setWarehouseCode(stockOutItem.getStockOut().getWarehouseCode());
//				indivFlow.setOriginalCode(stockOutItem.getOriginalCode());
//				indivFlow.setEnabled(WmsConstants.ENABLED_FALSE);
//			}
//			indivDao.addIndivFlows(indivList);
//		} catch (Exception e) {
//			throw new ServiceException(e);
//		}
//
//		// 更新出库明细项的商品个体绑定情况
//		stockOutItem.setIndivFinished(WmsConstants.ENABLED_TRUE);// 设置为已完成绑定
//		try {
//			stockOutDao.updateStockOutItem(stockOutItem);
//		} catch (DataAccessException e) {
//			throw new ServiceException(e);
//		}
//
//	}

	private void checkConfirm(StockOut stockOut) throws ServiceException {
		// 检测出库批次状态是否正常
//		if (!StockOutStatus.PENDING.getCode().equals(stockOut.getHandlingStatus())) {
//			throw new ServiceException("出库单状态异常");
//		}

		try {
			// 检测是否还有发货单尚未完成发票或配送信息操作
			if (deliveryDao.queryUnfinishedDeliveryTotal(stockOut.getId()) > 0) {
				throw new ServiceException("存在发票信息或配送信息不全的发货单");
			}
			// 检测是否还有发货单尚未完成商品编码的绑定
			if (deliveryDao.queryIndivUnfinishedGoodsTotal(stockOut.getId()) > 0) {
				throw new ServiceException("还有发货商品未完成商品编码的绑定");
			}
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

//	@Override
//	public void confirmStockOut(StockOut stockOut) throws ServiceException {
//		// 检测批次出库是否满足确认出库的条件
//		checkConfirm(stockOut);
//
//		try {
//			// 更新出库批次的确认信息
////			stockOut.setHandlingStatus(StockOutStatus.FINISHED.getCode());
//			stockOut.setFinishedTime(new Date());
//			stockOut.setHandledBy(ActionUtils.getLoginName());
//			stockOut.setHandledDate(new Date());
//			stockOutDao.updateStockOut(stockOut);
//
//			// 批量更新发货单确认信息
//			Map<String, Object> criteria = Maps.newHashMap();
//			criteria.put("batchCode", stockOut.getStockOutCode());
//			criteria.put("handledTime", new Date());
//			criteria.put("handledBy", ActionUtils.getLoginName());
//			deliveryDao.updateDeliveryConfirmInfo(criteria);
//
//			// 批量更新发货商品状态//
//			criteria.clear();
//			criteria.put("batchCode", stockOut.getStockOutCode());
//			deliveryDao.updateDeliveryGoodsConfirmStatus(criteria);
//
//			// 批量更新销售订单发货信息//
//			criteria.clear();
//			criteria.put("batchCode", stockOut.getStockOutCode());
//			criteria.put("shippingTime", new Date());
//			orderDao.updateShippedInfoByBatch(criteria);
//
//			// 批量更新发货商品个体的出库确认信息
//			criteria.clear();
//			criteria.put("outTime", new Date());
//			criteria.put("batchCode", stockOut.getStockOutCode());
//			indivDao.updateIndivsOutConfirmInfo(criteria);
//
//			// 批量更新发货商品个体的流转确认信息
//			criteria.clear();
//			criteria.put("flowType", IndivFlowType.OUT_SALES.getCode());
//			criteria.put("flowTime", new Date());
//			criteria.put("batchCode", stockOut.getStockOutCode());
//			indivDao.updateIndivsFlowConfirmInfo(criteria);
//
//			// 根据发货商品数量更新库存
//			List<DeliverySummary> detailSummaryList;
//			criteria.clear();
//			criteria.put("batchCode", stockOut.getStockOutCode());
//			Page page = new Page();
//			page.setStartRow(1);
//			page.setEndRow(Integer.MAX_VALUE);
//			criteria.put("page", page);
//			try {
//				detailSummaryList = deliveryDao.queryDeliverySummaryByPage(criteria);
//			} catch (DataAccessException e) {
//				throw new ServiceException(e);
//			}
//			if (CollectionUtils.isEmpty(detailSummaryList)) {
//				throw new ServiceException("发货商品汇总为空");
//			}
//			for (DeliverySummary summary : detailSummaryList) {
//				StockRequest stockRequest = new StockRequest(summary.getWarehouseCode(), summary.getSkuId(),
//						StockType.STOCK_OCCUPY, summary.getQuantity(), StockBizType.OUT_SALES, stockOut
//								.getStockOutCode());
//				stockService.decreaseStock(stockRequest);
//			}
//		} catch (ServiceException e) {
//			throw e;
//		} catch (Exception e) {
//			throw new ServiceException(e);
//		}
//
//		// // ---4.如果存在商品个体信息，则进行个体信息的相关处理---//
//		// List<IndivFlow> indivFlowList =
//		// getIndivFlowsByStockOutCode(stockOut.getStockOutCode());
//		// if (CollectionUtils.isNotEmpty(indivFlowList)) {
//		// // 校验出库商品个体信息
//		// List<String> indivCodes = Lists.newArrayList();
//		// for (IndivFlow flow : indivFlowList) {
//		// indivCodes.add(flow.getIndivCode());
//		// }
//		// List<Indiv> currIndivList =
//		// indivDao.queryIndivListByCodes(indivCodes);
//		// if (CollectionUtils.isEmpty(currIndivList)) {
//		// throw new ServiceException("商品编号不存在");
//		// }
//		// Map<String, Indiv> indivMap = Maps.newHashMap();
//		// for (Indiv indiv : currIndivList) {
//		// indivMap.put(indiv.getIndivCode(), indiv);
//		// }
//		// for (IndivFlow flow : indivFlowList) {
//		// if (!indivMap.containsKey(flow.getIndivCode())) {
//		// throw new ServiceException("此商品个体编号不存在：" + flow.getIndivCode());
//		// }
//		// Indiv currIndiv = indivMap.get(flow.getIndivCode());
//		// if (currIndiv.getSku().getId().longValue() !=
//		// flow.getSku().getId().longValue()) {
//		// throw new ServiceException("此编号对应的商品个体不归属于对应SKU：" +
//		// currIndiv.getIndivCode());
//		// }
//		// if
//		// (!currIndiv.getWarehouse().getWarehouseCode().equals(flow.getWarehouse().getWarehouseCode()))
//		// {
//		// throw new ServiceException("此编号商品所在仓库与出仓不符：" +
//		// currIndiv.getIndivCode());
//		// }
//		// if
//		// (IndivStockStatus.OUT_WAREHOUSE.getCode().equals(currIndiv.getStockStatus()))
//		// {
//		// throw new ServiceException("此编号对应的商品个体已经出库：" +
//		// currIndiv.getIndivCode());
//		// }
//		// if
//		// (!IndivStockStatus.IN_WAREHOUSE.getCode().equals(currIndiv.getStockStatus()))
//		// {
//		// throw new ServiceException("此编号对应的商品个体不存在：" +
//		// currIndiv.getIndivCode());
//		// }
//		// if
//		// (IndivWaresStatus.DEFECTIVE.getCode().equals(currIndiv.getWaresStatus()))
//		// {
//		// throw new ServiceException("此编号对应的商品个体是次品：" +
//		// currIndiv.getIndivCode());
//		// }
//		// }
//		// }
//
//		// TODO:同步库存到外围系统
//
//		// 通知订单中心已发货
//		handleSalesOrderShipped(stockOut.getId());
//	}

	/**
	 * 查询指定出库编号的所有商品个体流转信息
	 */
	private List<IndivFlow> getIndivFlowsByStockOutCode(String stockOutCode) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("flowType", IndivFlowType.OUT_SALES.getCode());
		criteria.put("flowCode", stockOutCode);
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

	// @Override
	// public void updateInvoiceInfo(InvoiceInfo invoiceInfo)throws
	// ServiceException{
	// try {
	// if(stockOutDao.updateOrderInvoiceInfo(invoiceInfo)==0){
	// throw new ServiceException("订单状态异常");
	// }
	// } catch (DataAccessException e) {
	// throw new ServiceException(e);
	// }
	// }

	@Autowired
	public void setStockOutDao(StockOutDao stockOutDao) {
		this.stockOutDao = stockOutDao;
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
	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

	@Autowired
	public void setIndivDao(IndivDao indivDao) {
		this.indivDao = indivDao;
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
