package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.vo.OrderStatusVo;
import com.gionee.wms.vo.SalesOrderVo;

@BatisDao
public interface SalesOrderDao {
	/**
	 * 分页查询符合条件的销售订单列表
	 */
	List<SalesOrder> queryOrderByPage(Map<String, Object> criteria);

	/**
	 * 查询符合条件的销售订单列表总数
	 */
	int queryOrderTotal(Map<String, Object> criteria);
	
	/**
	 * 根据订单ID获取销售订单
	 */
	SalesOrder queryOrder(Long id);
	
	/**
	 * 根据订单号获取销售订单
	 */
	SalesOrder queryOrderByOrderCode(String orderCode);
	
	/**
	 * 根据订单号获取销售订单列表
	 */
	List<SalesOrder> queryOrderListByOrderCodes(List<String> orderCodes);
	
	/**
	 * 根据订单ID获取销售订单列表
	 */
	List<SalesOrder> queryOrderListByIds(List<Long> orderIds);
	
	/**
	 * 获取待通知(已发货或已退货)的销售订单列表
	 */
	List<SalesOrder> queryOrderListNeedToBeNotified();
	
	/**
	 * 获取指定发货批次内的销售订单列表
	 */
	List<SalesOrder> queryOrderListByDelyBatchId(Long batchId);
	
	/**
	 * 获取指定发货批次内的销售订单(包含商品清单)列表
	 */
	List<SalesOrderVo> queryOrderListByOrderIds(String[] ids);
	
	/**
	 * 根据销售订单ID获取商品清单
	 */
	List<SalesOrderGoods> queryGoodsListByOrderId(Long orderId);
	
	/**
	 * 根据销售订单ID获取商品清单(配货用)
	 */
	List<SalesOrderGoods> queryGoodsListForPrepare(Long orderId);
	
	/**
	 * 根据销售订单号获取商品清单
	 */
	List<SalesOrderGoods> queryGoodsListByOrderCodes(List<String> orderCodes);
	
	/**
	 * 根据订单ID获取购物商品清单(商品数量以实际发货数量为准)
	 
	List<SalesOrderGoods> queryShoppingListByOrderId(Long orderId);*/

	/**
	 * 添加销售订单
	 */
	int addOrder(SalesOrder order);

	/**
	 * 批量添加订单商品
	 */
	int batchAddOrderGoods(List<SalesOrderGoods> orderGoodsList);
	
	/**
	 * 更新销售订单信息
	 */
	int updateOrder(Map<String, Object> criteria);
	
	/**
	 * 批量更新销售订单信息
	 */
	int batchUpdateOrder(List<SalesOrder> list);
	
	/**
	 * 更新出库批次内销售订单的已发货信息
	 */
	int updateShippedInfoByDelyBatchId(Map<String, Object> criteria);
	
	/**
	 * 更新出库批次内的订单配送状态为未发货状态
	 */
	int updateToUnshippedByDelyBatchId(Long delyBatchId);

	List<SalesOrder> getSalesOrderListByIds(String orderIds);

	void updateShippingInfoList(List<SalesOrder> orderList);

	void setInvoiceStatus(List<Long> orderIds);

	List<SalesOrderGoods> queryGoodsListByOrderIds(String[] ids);

	void updateOrderByIds(Map<String, Object> criteria);

	List<SalesOrder> queryOrderList(Map<String, Object> criteria);

	List<SalesOrderVo> queryOrderListSearch(Map<String, Object> criteria);

	void updateSalesOrderFilterStatus(String[] ids);
	
	SalesOrder queryOrderByDeliveryCode(String delivery);
	
	void updateOrderOutByIds(Map<String, Object> criteria);

	int updateSalesOrder(SalesOrder order);

	void deleteOrderGoodsList(Long id);

	void updateSalesOrderStatus(Map<String, Object> params);
	
	List<SalesOrderVo> queryOrderListAndGoods(Map<String, Object> criteria);
	
	List<SalesOrderVo> queryOrderListAndGoodsForStat(Map<String, Object> criteria);
	
	String queryLastSyncOrderTime(Map<String, Object> paramsMap);
	
	List<OrderStatusVo> queryOrderStatusAndCountGroupByShippingName(Map<String, Object> paramsMap);
	
	void updateOrderGoods(SalesOrderGoods salesOrderGoods);
	
	SalesOrderGoods queryGoodsById(Long id);
}
