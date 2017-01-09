package com.gionee.wms.service.stock;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.vo.OrderStatusStatVo;
import com.gionee.wms.vo.SalesOrderVo;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.sf.integration.warehouse.response.WmsRealTimeInventoryBalanceQueryResponse;

public interface SalesOrderService {
	/**
	 * 分页取销售订单列表.
	 */
	List<SalesOrder> getSalesOrderList(Map<String, Object> criteria, Page page) throws ServiceException;

	/**
	 * 取销售订单列表总数.
	 */
	Integer getSalesOrderTotal(Map<String, Object> criteria);

	/**
	 * 根据出库单号取销售订单列表
	 */
	List<SalesOrder> getSalesOrderList(String stockOutCode) throws ServiceException;

	/**
	 * 取指定的销售订单.
	 */
	SalesOrder getSalesOrder(Long id);

	/**
	 * 获取待通知的已发货和已退货的销售订单列表
	 */
	List<SalesOrder> getSalesOrdersNeedToBeNotified();

	/**
	 * 获取指定发货批次内的销售订单(包含商品清单)列表
	 */
	List<SalesOrderVo> queryGoodsListByOrderIds(String[] ids);

	/**
	 * 取指定的销售订单商品清单.
	 */
	List<SalesOrderGoods> getOrderGoodsList(Long orderId);

	List<SalesOrderGoods> getOrderGoodsListForPrepare(Long orderId);

	/**
	 * 根据销售订单号取订单商品清单.
	 */
	List<SalesOrderGoods> getOrderGoodsListByOrderCode(String orderCode);

	/**
	 * 保存销售订单及商品明细
	 */
	void addSalesOrder(SalesOrder salesOrder, List<SalesOrderGoods> orderGoodsList) throws ServiceException;

	// /**
	// * 批量保存销售订单
	// */
	// void addSalesOrders(List<SalesOrder> salesOrders) throws
	// ServiceException;

	/**
	 * 取消销售订单
	 */
	void cancelSalesOrder(String orderCode) throws ServiceException;

	/**
	 * 更新指定的销售订单信息
	 */
	void updateSalesOrder(SalesOrder order) throws ServiceException;

	/**
	 * 根据ids获取订单列表
	 * @param substring
	 * @return
	 */
	List<SalesOrder> getSalesOrderListByIds(List<Long> orderIds);

	/**
	 * 更新物流信息
	 * @param orderList
	 */
	void updateShippingInfoList(List<SalesOrder> orderList);

	/**
	 * 设置发票状态
	 * @param orderIds
	 */
	void setInvoiceStatus(List<Long> orderIds);

	/**
	 * 根据订单id获取商品汇总
	 * @param ids
	 * @return
	 * @throws ServiceException
	 */
	public List<SalesOrderGoods> getOrderGoodsListByIds(String ids) throws ServiceException;

	List<SalesOrder> getSalesOrderListByBatchId(Long batchId);

	List<SalesOrderVo> getSalesOrderListSearch(Map<String, Object> criteria);

	SalesOrder getSalesOrderByDeliveryCode(String deliveryCode);

	public void confirmPrepare(Long orderId, String shippingNo, String weight);

	public SalesOrder getSalesOrderByShippingNo(Long shippingId, String shippingNo);

	void updateSalesOrderFilterStatus(String[] ids);

	/**
	 * 更新订单信息
	 * @param salesOrder
	 * @param orderGoodsList
	 */
	void updateSalesOrder(SalesOrder salesOrder, List<SalesOrderGoods> orderGoodsList);

	SalesOrder getSalesOrderByCode(String orderCode);

	/**
	 * 通知订单中心
	 * @param orderList
	 * @param notifyStatus 商城侧对应的状态
	 */
	void notifyOrder(List<SalesOrder> orderList);

	/**
	 * 更新订单状态
	 * @param params
	 */
	void updateSalesOrderStatus(Map<String, Object> params);

	SalesOrder getSalesOrderByShipAndStatus(Integer orderStatus, String shippingNo);

	void notifyOrder(ArrayList<SalesOrder> newArrayList, Map<String, Object> params);

	public List<SalesOrder> getSalesOrderList(Map<String, Object> criteria);

	List<SalesOrderVo> getSalesOrderAndGoods(Map<String, Object> criteria);

	List<SalesOrderVo> getSalesOrderAndGoodsForStat(Map<String, Object> criteria);

	/**
	 * 通知淘宝开放平台
	 * @param orderList
	 */
	void notifyTOP(List<SalesOrder> orderList);

	/**
	 * 开票
	 * @param ids
	 */
	void makeInvoice(String[] ids, String ipFrom);

	/**
	 * 查询最后一次同步入库时间，并增加一秒返回-
	 * @param paramsMap
	 * @return
	 * @throws ServiceException
	 */
	String queryLastSyncOrderTime(Map<String, Object> paramsMap) throws ServiceException;

	/**
	 * 按照物流公司和订单状态分组查询
	 */
	List<OrderStatusStatVo> queryOrderStatusAndCountGroupByShippingName(Map<String, Object> paramsMap) throws ServiceException;

	void updateSalesOrderGoods(SalesOrderGoods salesOrderGoods);

	SalesOrderGoods getSalesOrderGoods(Long id);

	void addIndiv(Long orderId, String orderCode, Long indivId);

	List<Indiv> getIndivList(Long orderId);

	void updateSalesOrderStatus(List<SalesOrder> orders, int destStatus, int srcStatus);

	/**
	 * 修改订单推送状态
	 * @param order
	 * @return
	 */
	int updateSalesOrderPushStatus(SalesOrder order);

	/**
	 * 订单流向顺丰
	 * @param order order
	 */
	ServiceCtrlMessage pushOrderToSF(SalesOrder order);

	/**
	 * 一键复制订单
	 * @param id 订单ID
	 * @return
	 */
	ServiceCtrlMessage copy(Long id);

	/**
	 * 查询实时库存
	 * @param sku
	 * @return
	 */
	WmsRealTimeInventoryBalanceQueryResponse queryRealTimeInventoryBalance(String sku);

	/**
	 * 加载打印发票需要的数据
	 * @param ids 订单ID
	 * @return
	 */
	List<Map<String, Object>> getPrintInvoiceData(String[] ids);

	/**
	 * 处理将发货批次改成已完成
	 * @param batchCodeMap
	 */
	void handBatch(Map<String, Long> batchCodeMap);

}
