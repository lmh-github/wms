package com.gionee.wms.service.stock;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.BackStatus;
import com.gionee.wms.common.WmsConstants.IndivFlowType;
import com.gionee.wms.common.WmsConstants.IndivStockStatus;
import com.gionee.wms.common.WmsConstants.IndivWaresStatus;
import com.gionee.wms.common.WmsConstants.NotifyStatus;
import com.gionee.wms.common.WmsConstants.OrderPushStatusEnum;
import com.gionee.wms.common.WmsConstants.OrderStatus;
import com.gionee.wms.common.WmsConstants.StockBizType;
import com.gionee.wms.common.WmsConstants.StockType;
import com.gionee.wms.common.WmsConstants.WarehouseCodeEnum;
import com.gionee.wms.dao.BackDao;
import com.gionee.wms.dao.IndivDao;
import com.gionee.wms.dao.SalesOrderDao;
import com.gionee.wms.dao.SalesOrderLogDao;
import com.gionee.wms.dao.WaresDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.StockRequest;
import com.gionee.wms.entity.Back;
import com.gionee.wms.entity.BackGoods;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.entity.SalesOrderLog;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.facade.request.OperateOrderRequest;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.common.CommonService;
import com.gionee.wms.service.common.CommonServiceImpl;
import com.gionee.wms.vo.BackGoodsVo;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Service("backService")
public class BackServiceImpl extends CommonServiceImpl implements BackService {
	private static Logger logger = LoggerFactory.getLogger(BackServiceImpl.class);

	@Autowired
	private WarehouseService warehouseService;

	@Autowired
	private BackDao backDao;
	@Autowired
	private SalesOrderDao orderDao;
	@Autowired
	private IndivDao indivDao;
	@Autowired
	public SalesOrderLogDao salesOrderLogDao;
	@Autowired
	private WaresDao waresDao;
	@Autowired
	private StockService stockService;

	@Override
	public void addBack(Back back) {
		backDao.addBack(back);
	}

	@Override
	public void addBackGoods(List<BackGoods> goodsList) {
		backDao.addBackGoods(goodsList);
	}

	@Override
	public void cancelBack(OperateOrderRequest req) {
		Back back = new Back();
		back.setBackCode(req.getBackCode());
		back.setBackStatus(BackStatus.CANCELED.getCode());
		backDao.updateBack(back);
	}

	@Override
	public int getBackTotal(Map<String, Object> criteria) {
		return backDao.getBackTotal(criteria);
	}

	@Override
	public List<Back> getBackList(Map<String, Object> criteria, Page page) {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		try {
			return backDao.getBackList(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	/** {@inheritDoc} */
	@Override
	public List<Back> getBackListForCascade(Map<String, Object> criteria) {
		return backDao.getBackListForCascade(criteria);
	}

	@Override
	public Back getBack(Map<String, Object> criteria) {
		return backDao.getBack(criteria);
	}

	@Override
	public List<BackGoodsVo> getBackGoodsList(Map<String, Object> criteria) {
		return backDao.getBackGoodsList(criteria);
	}

	@Override
	public void handleBacked(Long warehouseId, Back back, String[] goodIds, Integer[] nonDefectives, Integer[] defectives, List<IndivFlow> indivList1, List<IndivFlow> indivList2, String[] skuCodes, Integer[] indivEnabled) {
		Warehouse warehouse = warehouseService.getWarehouse(warehouseId);
		// 更新退货单状态为已退货
		back.setBackStatus(BackStatus.BACKED.getCode());
		back.setWarehouseCode(warehouse.getWarehouseCode());
		back.setHandledBy(ActionUtils.getLoginName());
		back.setHandledTime(new Date());
		backDao.updateBack(back);
		// 更新订单状态为已退货
		Map<String, Object> params = Maps.newHashMap();
		params.put("orderCode", back.getOrderCode());
		params.put("orderStatus", OrderStatus.BACKED.getCode());
		orderDao.updateSalesOrderStatus(params);
		SalesOrder order = orderDao.queryOrderByOrderCode(back.getOrderCode());

		// 保存操作日志
		SalesOrderLog salesOrderLog = new SalesOrderLog();
		try {
			salesOrderLog.setOrderId(order.getId());
			salesOrderLog.setOrderStatus(WmsConstants.OrderStatus.BACKED.getCode());
			salesOrderLog.setOpUser(ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
			salesOrderLog.setOpTime(new Date());
			salesOrderLog.setRemark("更新订单为已退货状态");
			salesOrderLogDao.insertSalesOrderLog(salesOrderLog);
		} catch (Exception e) {
			logger.error("业务日志记录异常", e);
		}

		// 更新商品个体并添加个体流转信息
		List<IndivFlow> indivFlowList = Lists.newArrayList();
		if (CollectionUtils.isNotEmpty(indivList1)) {
			for (IndivFlow flow : indivList1) {
				// 良品个体处理
				Indiv indiv = indivDao.queryIndivByCode(flow.getIndivCode());
				if (indiv == null) {
					throw new ServiceException("商品个体不存在");
				}
				indiv.setRmaTime(new Date());
				indiv.setRmaId(back.getId());// 此处切换为退货单ID
				indiv.setRmaCode(back.getBackCode());
				indiv.setRmaCount(indiv.getRmaCount() + 1);
				indiv.setStockStatus(IndivStockStatus.IN_WAREHOUSE.getCode());
				indiv.setWaresStatus(IndivWaresStatus.NON_DEFECTIVE.getCode());
				// 更新商品个体
				indivDao.updateIndiv(indiv);

				flow.setIndivCode(indiv.getIndivCode());
				flow.setSkuId(indiv.getSkuId());
				flow.setSkuCode(indiv.getSkuCode());
				flow.setSkuName(indiv.getSkuName());
				flow.setWarehouseId(indiv.getWarehouseId());
				flow.setWarehouseName(indiv.getWarehouseName());
				flow.setMeasureUnit(indiv.getMeasureUnit());
				flow.setFlowTime(new Date());
				flow.setFlowType(IndivFlowType.IN_RMA.getCode());
				flow.setFlowId(back.getId());
				flow.setFlowCode(back.getBackCode());
				flow.setFlowGoodsId(null);
				flow.setWaresStatus(indiv.getWaresStatus());
				flow.setEnabled(WmsConstants.ENABLED_TRUE);
				indivFlowList.add(flow);
			}
		}
		if (CollectionUtils.isNotEmpty(indivList2)) {
			for (IndivFlow flow : indivList2) {
				// 次品个体处理
				Indiv indiv = indivDao.queryIndivByCode(flow.getIndivCode());
				if (indiv == null) {
					throw new ServiceException("商品个体不存在");
				}
				indiv.setRmaTime(new Date());
				indiv.setRmaId(back.getId());// 此处切换为退货单ID
				indiv.setRmaCode(back.getBackCode());
				indiv.setRmaCount(indiv.getRmaCount() + 1);
				indiv.setStockStatus(IndivStockStatus.IN_WAREHOUSE.getCode());
				indiv.setWaresStatus(IndivWaresStatus.DEFECTIVE.getCode());
				// 更新商品个体
				indivDao.updateIndiv(indiv);

				flow.setIndivCode(indiv.getIndivCode());
				flow.setSkuId(indiv.getSkuId());
				flow.setSkuCode(indiv.getSkuCode());
				flow.setSkuName(indiv.getSkuName());
				flow.setWarehouseId(indiv.getWarehouseId());
				flow.setWarehouseName(indiv.getWarehouseName());
				flow.setMeasureUnit(indiv.getMeasureUnit());
				flow.setFlowTime(new Date());
				flow.setFlowType(IndivFlowType.IN_RMA.getCode());
				flow.setFlowId(back.getId());
				flow.setFlowCode(back.getBackCode());
				flow.setFlowGoodsId(null);
				flow.setWaresStatus(indiv.getWaresStatus());
				flow.setEnabled(WmsConstants.ENABLED_TRUE);
				indivFlowList.add(flow);
			}
		}

		// 更新退货商品信息
		for (String goodIdTemp : goodIds) {
			String goodId = goodIdTemp.split("_")[0];
			int index = Integer.valueOf(goodIdTemp.split("_")[1]);
			params.clear();
			params.put("goodId", goodId);
			params.put("nonDefectiveQuantity", nonDefectives[index]);
			params.put("defectiveQuantity", defectives[index]);
			backDao.updateBackGoods(params);
			// 处理配件良次品信息，并将其加入个体流转信息，便于退货通知时可以查询
			Sku sku = waresDao.querySkuBySkuCode(skuCodes[index]);
			if (indivEnabled[index].intValue() == 0) {
				for (int i = 0; i < nonDefectives[index].intValue(); i++) {
					IndivFlow flow = new IndivFlow();
					flow.setIndivCode("");
					flow.setSkuId(sku.getId());
					flow.setSkuCode(sku.getSkuCode());
					flow.setSkuName(sku.getSkuName());
					flow.setWarehouseId(warehouse.getId());
					flow.setWarehouseName(warehouse.getWarehouseName());
					flow.setMeasureUnit(sku.getWares().getMeasureUnit());
					flow.setFlowTime(new Date());
					flow.setFlowType(IndivFlowType.IN_RMA.getCode());
					flow.setFlowId(back.getId());
					flow.setFlowCode(back.getBackCode());
					flow.setFlowGoodsId(null);
					flow.setWaresStatus(IndivWaresStatus.NON_DEFECTIVE.getCode());
					flow.setEnabled(WmsConstants.ENABLED_TRUE);
					indivFlowList.add(flow);
				}

				for (int i = 0; i < defectives[index].intValue(); i++) {
					IndivFlow flow = new IndivFlow();
					flow.setIndivCode("");
					flow.setSkuId(sku.getId());
					flow.setSkuCode(sku.getSkuCode());
					flow.setSkuName(sku.getSkuName());
					flow.setWarehouseId(warehouse.getId());
					flow.setWarehouseName(warehouse.getWarehouseName());
					flow.setMeasureUnit(sku.getWares().getMeasureUnit());
					flow.setFlowTime(new Date());
					flow.setFlowType(IndivFlowType.IN_RMA.getCode());
					flow.setFlowId(back.getId());
					flow.setFlowCode(back.getBackCode());
					flow.setFlowGoodsId(null);
					flow.setWaresStatus(IndivWaresStatus.DEFECTIVE.getCode());
					flow.setEnabled(WmsConstants.ENABLED_TRUE);
					indivFlowList.add(flow);
				}
			}
		}

		if (CollectionUtils.isNotEmpty(indivFlowList)) {
			// 添加个体流转信息
			indivDao.addIndivFlows(indivFlowList);
		}
	}

	/** {@inheritDoc} */
	@Override
	public void handleAddBack(Back back, List<BackGoods> backGoods) {
		checkBacking(back);
		String backCode = getBizCode(CommonService.ORDER_BACK);
		back.setBackCode(backCode); // 生成单号
		back.setBackStatus(BackStatus.BACKING.getCode()); // 退货中
		backDao.addBack(back); // 新增退货单

		for (BackGoods goods : backGoods) {
			goods.setBackCode(backCode); // 关联上单号
		}
		backDao.addBackGoods(backGoods); // 新增退货商品

	}

	/**
	 * 检查订单是否申请退换货
	 * @param back
	 */
	private void checkBacking(Back back) {
		String orderCode = back.getOrderCode();
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("orderCode", orderCode);
		criteria.put("page", Page.getPage(1, 20, 20));
		List<Back> backList = backDao.getBackList(criteria); // 校验订单是否已经申请了退换货
		for (Back b : backList) {
			if (b.getBackStatus().intValue() != BackStatus.CANCELED.getCode()) {
				throw new ServiceException("该订单已经申请了退换货，请不要重复申请！");
			}
		}
	}

	/** {@inheritDoc} */
	@Override
	public void handleConfirmBack(Back back) {
		if (back.getBackStatus() == BackStatus.BACKED.getCode()) {
			back.setBackedTime(new Date());
		}
		backDao.updateBack(back);
	}

	/** {@inheritDoc} */
	@Override
	public void handleConfirmBack(Back back, List<BackGoods> backGoods, List<IndivFlow> indivFlowList) {
		back.setBackStatus(BackStatus.BACKED.getCode()); // 已退货
		backDao.updateBack(back);

		String backCode = back.getBackCode();
		backDao.deleteBackGoods(backCode);
		for (BackGoods goods : backGoods) {
			goods.setBackCode(backCode); // 关联上单号
		}
		backDao.addBackGoods(backGoods); // 新增退货商品

		// WarehouseCodeEnum
		Warehouse warehouse = warehouseService.getWarehouseByCode(WarehouseCodeEnum.DONG_GUAN_WAREHOUSE.getCode()); // 退换货都是退到东莞仓

		List<StockRequest> stockRequests = Lists.newArrayList();
		List<SalesOrderGoods> goodsList = Lists.newArrayList(); // 订单商品用户处理换货的时候用到
		for (BackGoods g : backGoods) {
			String skuCode = g.getSkuCode();
			Sku sku = waresDao.querySkuBySkuCode(skuCode);
			if (WmsConstants.ENABLED_FALSE == sku.getWares().getIndivEnabled()) { // 配件
				if (g.getNonDefectiveQuantity() != null && g.getNonDefectiveQuantity() > 0) { // 良品
					stockRequests.add(new StockRequest(warehouse.getId(), sku.getId(), StockType.STOCK_SALES, g.getNonDefectiveQuantity(), StockBizType.IN_RMA, back.getOrderCode()));
				} else if (g.getDefectiveQuantity() != null && g.getDefectiveQuantity() > 0) { // 次品
					stockRequests.add(new StockRequest(warehouse.getId(), sku.getId(), StockType.STOCK_UNSALES, g.getNonDefectiveQuantity(), StockBizType.IN_RMA, back.getOrderCode()));
				}
			}

			SalesOrderGoods goods = new SalesOrderGoods();
			goods.setSkuId(sku.getId());
			goods.setSkuCode(g.getSkuCode());
			goods.setSkuName(g.getSkuName());
			goods.setUnitPrice(new BigDecimal(0)); // 价格0
			goods.setSubtotalPrice(new BigDecimal(0));
			goods.setMeasureUnit(sku.getWares().getMeasureUnit());
			goods.setIndivEnabled(sku.getWares().getIndivEnabled());
			goods.setQuantity(g.getQuantity());
			goodsList.add(goods);
		}

		Map<String, StockRequest> indivStockMap = Maps.newConcurrentMap();
		for (IndivFlow i : indivFlowList) { // 个体以输入的串号为准
			Indiv indiv = indivDao.queryIndivByCode(i.getIndivCode());
			indiv.setRmaTime(new Date());
			indiv.setRmaId(back.getId());// 此处切换为退货单ID
			indiv.setRmaCode(backCode);
			indiv.setRmaCount(indiv.getRmaCount() + 1);
			indiv.setStockStatus(IndivStockStatus.IN_WAREHOUSE.getCode());
			indiv.setWaresStatus(i.getWaresStatus()); // 品质
			indivDao.updateIndiv(indiv); // 更新商品个体

			i.setWarehouseId(indiv.getWarehouseId());
			i.setWarehouseName(indiv.getWarehouseName());
			i.setMeasureUnit(indiv.getMeasureUnit());
			i.setFlowTime(new Date());
			i.setFlowType(IndivFlowType.IN_RMA.getCode());
			i.setFlowId(back.getId());
			i.setFlowCode(backCode);
			i.setFlowGoodsId(null);
			i.setWaresStatus(indiv.getWaresStatus());
			i.setEnabled(WmsConstants.ENABLED_TRUE);

			Long skuId = indiv.getSkuId();
			Integer waresStatus = i.getWaresStatus(); // 品质
			if (indivStockMap.containsKey(skuId + "_" + waresStatus)) {
				StockRequest stockRequest = indivStockMap.get(skuId);
				stockRequest.setQuantity(stockRequest.getQuantity() + 1); // 串号是一个一个加的
			} else {
				StockRequest stockRequest = new StockRequest(warehouse.getId(), skuId, StockType.STOCK_SALES, 1, StockBizType.IN_RMA, back.getOrderCode());
				if (waresStatus == 1) { // 良品
					stockRequest.setSrcStockType(StockType.STOCK_SALES);
				} else { // 次品
					stockRequest.setSrcStockType(StockType.STOCK_UNSALES);
				}
				indivStockMap.put(skuId + "_" + waresStatus, stockRequest);
			}
		}

		if (!indivStockMap.isEmpty()) {
			stockRequests.addAll(indivStockMap.values()); // 合并
		}
		for (StockRequest stockRequest : stockRequests) {
			stockService.increaseStock(stockRequest, true); // 增加库存
		}

		indivDao.addIndivFlows(indivFlowList); // 添加个体流水

		if ("exchange".equals(back.getBackType())) { // 换货新生成一张订单
			createExChangeOrder(back, goodsList);
		}

		// 给原始订单记录操作日志
		String orderCode = back.getOrderCode();
		SalesOrder salesOrder = orderDao.queryOrderByOrderCode(orderCode); // 查询出原来的订单
		// TODO 修改原来订单为已退货

		SalesOrderLog salesOrderLog = new SalesOrderLog();
		salesOrderLog.setOrderId(salesOrder.getId());
		salesOrderLog.setOrderStatus(salesOrder.getOrderStatus().intValue());
		salesOrderLog.setOpUser(ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
		salesOrderLog.setOpTime(new Date());
		salesOrderLog.setRemark("订单已经\"" + ("exchange".equals(back.getBackType()) ? "换货" : "退货") + "\"，退换货单号：" + backCode);
		salesOrderLogDao.insertSalesOrderLog(salesOrderLog);

	}

	/**
	 * 创建一张换货订单
	 * @param back
	 * @param backGoods
	 */
	private void createExChangeOrder(Back back, List<SalesOrderGoods> goodsList) {
		String orderCode = back.getOrderCode();
		SalesOrder salesOrder = orderDao.queryOrderByOrderCode(orderCode); // 查询出原来的订单

		int seq = 1; // 序号
		String orderCodePrefix = orderCode; // 前缀
		if (orderCode.indexOf("-") > -1) {
			seq = Integer.parseInt(orderCode.substring(orderCode.indexOf("-") + 1)) + 1;
			orderCodePrefix = orderCode.substring(0, orderCode.indexOf("-"));
		}

		String exChangeOrderCode = null;
		// 最多查询10次
		for (int i = 0; i < 10; i++, seq++) {
			exChangeOrderCode = orderCodePrefix + "-" + StringUtils.leftPad(seq + "", 2, "2"); // 换货订单以2打头
			if (orderDao.queryOrderByOrderCode(exChangeOrderCode) == null) {
				break;
			}
		}

		salesOrder.setOrderCode(exChangeOrderCode); // 换货订单
		salesOrder.setId(null);
		salesOrder.setOrderTime(new Date());
		salesOrder.setOrderStatus(OrderStatus.FILTERED.getCode()); // 已筛单状态
		salesOrder.setOrderNotifyStatus(NotifyStatus.UNNOTIFIED.getCode()); // 未通知
		salesOrder.setOrderNotifyTime(null);
		salesOrder.setOrderNotifyCount(0); //
		salesOrder.setDeliveryCode(null);
		salesOrder.setJoinTime(new Date());
		salesOrder.setHandledTime(new Date());
		if (WmsConstants.ENABLED_TRUE == salesOrder.getInvoiceEnabled()) {
			salesOrder.setInvoiceStatus(WmsConstants.ENABLED_FALSE);
		}
		salesOrder.setOrderPushStatus(OrderPushStatusEnum.UN_PUSHED.getCode());
		salesOrder.setShippingNo(null); // 配送单号

		orderDao.addOrder(salesOrder); // 新增一个换货订单

		for (SalesOrderGoods g : goodsList) {
			g.setOrder(salesOrder);
		}
		orderDao.batchAddOrderGoods(goodsList);

		// 保存操作日志
		SalesOrderLog salesOrderLog = new SalesOrderLog();
		salesOrderLog.setOrderId(salesOrder.getId());
		salesOrderLog.setOrderStatus(salesOrder.getOrderStatus().intValue());
		salesOrderLog.setOpUser(ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
		salesOrderLog.setOpTime(new Date());
		salesOrderLog.setRemark("系统自动生成的换货订单");
		salesOrderLogDao.insertSalesOrderLog(salesOrderLog);

	}

	/** {@inheritDoc} */
	@Override
	public void handleDelete(String backCode) {
		backDao.delete(backCode); // 删除主单
		backDao.deleteBackGoods(backCode);
	}

	/** {@inheritDoc} */
	@Override
	public void handleUpdate(Back back) {
		backDao.updateBack(back);
		if (back.getBackGoods() != null && back.getBackGoods().size() > 0) {
			backDao.deleteBackGoods(back.getBackCode());

			backDao.addBackGoods(back.getBackGoods()); // 重新写入
		}
	}

}
