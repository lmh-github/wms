package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.DeliverySummary;
import com.gionee.wms.entity.Delivery;
import com.gionee.wms.entity.DeliveryGoods;

@BatisDao
public interface DeliveryDao {
	/**
	 * 批量添加发货单
	 */
	int addDeliveryList(List<Delivery> deliverys);

	/**
	 * 根据销售订单ID批量添加发货商品清单
	 */
	int addDeliveryGoodsListByOrderIds(List<Long> orderIds);

	/**
	 * 分页查询符合条件的发货单列表
	 */
	List<Delivery> queryDeliveryByPage(Map<String, Object> criteria);

	/**
	 * 查询符合条件的发货单总数
	 */
	int queryDeliveryTotal(Map<String, Object> criteria);

	/**
	 * 查询符合条件的发货单列表
	 */
	List<Delivery> queryDeliveryList(Map<String, Object> criteria);
	
	/**
	 * 根据批量源单ID获取发货单列表
	 */
	List<Delivery> queryDeliveryListByOriginalIds(List<Long> originalIds);
	
	/**
	 * 根据批量源单号获取发货单列表
	 */
	List<Delivery> queryDeliveryListByOriginalCodes(List<String> originalCodes);

	/**
	 * 根据ID获取发货单
	 */
	Delivery queryDelivery(Long id);
	
	/**
	 * 根据源单号获取发货单
	 */
	Delivery queryDeliveryByOriginalId(Long originalId);

	/**
	 * 判断出库批次是否是可取消的(不存在发票、配送单号、编码的操作)
	 */
	String queryBatchWhetherIsCancelable(Long batchId);

	/**
	 * 分页获取发货商品列表
	 */
	List<DeliveryGoods> queryDeliveryGoodsByPage(Map<String, Object> criteria);

	/**
	 * 获取发货商品总数
	 */
	int queryDeliveryGoodsTotal(Map<String, Object> criteria);

	/**
	 * 获取发货商品清单
	 */
	List<DeliveryGoods> queryDeliveryGoodsList(Map<String, Object> criteria);

	/**
	 * 获取指定的发货商品
	 */
	DeliveryGoods queryDeliveryGoods(Long id);

	/**
	 * 更新发货商品
	 */
	int updateDeliveryGoods(DeliveryGoods deliveryGoods);
	
	/**
	 * 根据发货单ID更新发货商品
	 */
	int updateDeliveryGoodsByDeliveryId(Map<String, Object> criteria);

	/**
	 * 更新发货单的配送信息
	 */
	int updateShippingInfo(Delivery delivery);

	/**
	 * 更新发货单的发票信息
	 */
	int updateInvoiceInfo(Delivery delivery);

	/**
	 * 获取出库批次内未完成配送信息和发票操作的发货单总数
	 */
	int queryUnfinishedDeliveryTotal(Long batchId);

	/**
	 * 获取出库批次内未完成商品编码录入的商品数目
	 */
	int queryIndivUnfinishedGoodsTotal(Long batchId);

	/**
	 * 更新发货单的出库确认信息
	 */
	int updateDeliveryConfirmInfo(Map<String, Object> criteria);

	/**
	 * 分页查询符合条件的发货商品汇总列表
	 */
	List<DeliverySummary> queryDeliverySummaryByPage(Map<String, Object> criteria);

	/**
	 * 查询符合条件的发货商品汇总列表总数
	 */
	int queryDeliverySummaryTotal(Map<String, Object> criteria);

	/**
	 * 查询符合条件的发货商品汇总列表
	 */
	List<DeliverySummary> queryDeliverySummaryList(Map<String, Object> criteria);

	/**
	 * 批量更新发货商品的确认状态
	 */
	int updateDeliveryGoodsConfirmStatus(Map<String, Object> criteria);

	/**
	 * 批量更新发货单
	 */
	int updateDeliveryList(List<Delivery> deliveryList);

	/**
	 * 根据发货批次ID删除所有发货单
	 */
	int deleteDeliveryByBatchId(Long batchId);

	/**
	 * 根据发货批次ID删除所有发货商品
	 */
	int deleteDeliveryGoodsByBatchId(Long batchId);

	/**
	 * 根据条件查询发货单
	 * @param criteria
	 * @return
	 */
	List<Delivery> queryDeliveryListSearch(Map<String, Object> criteria);
	
	void addDelivery(Map<String, Object> params);

	void deleteDeliveryByBatchCode(String batchCode);

	void deleteDeliveryByOrderCode(String orderCode);

}
