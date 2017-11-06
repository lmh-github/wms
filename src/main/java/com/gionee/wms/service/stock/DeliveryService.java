package com.gionee.wms.service.stock;

import com.gionee.wms.dto.DeliverySummary;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.*;
import com.gionee.wms.service.ServiceException;

import java.util.List;
import java.util.Map;

/**
 * @author Kevin
 * @ClassName: DeliveryService
 * @Description: 发货单服务
 */
public interface DeliveryService {
    /**
     * 添加发货批次
     * @param deliveryBatch 发货批次对象
     * @param orderIds      订单ID列表
     */
    void addDeliveryBatch(DeliveryBatch deliveryBatch, List<SalesOrder> orders);

    /**
     * 取消发货批次
     */
    void cancelDeliveryBatch(Long batchId);

    /**
     * 确认发货批次
     */
    void confirmDeliveryBatch(DeliveryBatch deliveryBatch);

    /**
     * 分页取符合条件的发货批次列表
     */
    List<DeliveryBatch> getDeliveryBatchList(Map<String, Object> criteria, Page page);

    /**
     * 取符合条件的发货批次总数
     */
    int getDeliveryBatchTotal(Map<String, Object> criteria);

    /**
     * 取发货批次
     */
    DeliveryBatch getDeliveryBatch(Long id);

    /**
     * 分页取发货订单列表
     */
    List<Delivery> getDeliveryList(Map<String, Object> criteria, Page page);

    /**
     * 取发货订单列表总数
     */
    int getDeliveryListTotal(Map<String, Object> criteria);

    /**
     * 取指定的发货单
     */
    Delivery getDelivery(Long id);

    /**
     * 根据销售订单ID取发货单
     */
    Delivery getDeliveryByOrderId(Long orderId);

    /**
     * 根据出库批次ID取发货单列表
     */
    List<Delivery> getDeliveryListByBatchId(Long batchId);

    /**
     * 根据出库批次取发货单列表
     */
    List<Delivery> getDeliveryListByBatchCode(String batchCode);

    /**
     * 分页取发货商品列表
     */
    List<DeliveryGoods> getDeliveryGoodsByPage(Map<String, Object> criteria, Page page);

    /**
     * 取发货商品总数
     */
    int getDeliveryGoodsTotal(Map<String, Object> criteria);

    /**
     * 根据发货单ID取发货商品清单.
     */
    List<DeliveryGoods> getDeliveryGoodsList(Long deliveryId);

    /**
     * 根据销售订单ID取发货商品清单.
     */
    List<DeliveryGoods> getDeliveryGoodsListByOrderId(Long orderId);

    /**
     * 取指定的发货商品
     */
    DeliveryGoods getDeliveryGoods(Long id);

    /**
     * 分页取发货商品汇总列表
     */
    List<DeliverySummary> getDeliverySummaryList(Map<String, Object> criteria, Page page) throws ServiceException;

    /**
     * 取发货商品汇总列表总数
     */
    Integer getDeliverySummaryTotal(Map<String, Object> criteria);

    /**
     * 根据发货批次ID取发货商品汇总列表
     */
    List<DeliverySummary> getDeliverySummaryListByBatchId(Long deliveryBatchId) throws ServiceException;

    /**
     * 更新发货个体
     */
    void updateDeliveryIndivs(DeliveryGoods goods, List<IndivFlow> indivList) throws ServiceException;

    /**
     * 更新配送信息
     */
    void updateShippingInfo(Delivery delivery) throws ServiceException;

    /**
     * 更新发货批次内的配送信息列表
     */
    void updateShippingInfoList(Long batchId, List<Delivery> deliveryList);

    /**
     * 添加发票信息
     */
    void addInvoiceInfo(Delivery delivery);

    /**
     * 获取所有符合条件的订单
     * @param criteria
     * @return
     */
    List<Delivery> getDeliveryListSearch(Map<String, Object> criteria);

    void addDeliveryBatchByIds(DeliveryBatch deliveryBatch, String ids);


    /**
     * 零售订单确认发货操作
     * @param warehouseId
     * @param batchCode
     */
    String confirmDeliveryWap(Long warehouseId, String batchCode);

    void addDelivery(Map<String, Object> params);

}
