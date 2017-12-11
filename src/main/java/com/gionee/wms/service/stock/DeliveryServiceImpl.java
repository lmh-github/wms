package com.gionee.wms.service.stock;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.*;
import com.gionee.wms.dao.*;
import com.gionee.wms.dto.DeliverySummary;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.StockRequest;
import com.gionee.wms.entity.*;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.ShippingInfoService;
import com.gionee.wms.service.basis.ShippingService;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.common.CommonServiceImpl;
import com.gionee.wms.service.stat.OrderStatService;
import com.gionee.wms.vo.DeliveryDetails;
import com.gionee.wms.vo.SalesOrderVo;
import com.gionee.wms.web.client.OrderCenterClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;

import java.util.*;

@Component("deliveryService")
public class DeliveryServiceImpl extends CommonServiceImpl implements DeliveryService {
    private static Logger logger = LoggerFactory.getLogger(DeliveryServiceImpl.class);
    @Autowired
    public SalesOrderLogDao salesOrderLogDao = null;
    @Autowired
    private DeliveryBatchDao deliveryBatchDao;
    @Autowired
    private DeliveryDao deliveryDao;
    @Autowired
    private SalesOrderDao orderDao;
    @Autowired
    private IndivDao indivDao;
    @Autowired
    private ShippingService shippingService;
    @Autowired
    private StockService stockService;
    @Autowired
    private ShippingInfoService shippingInfoService;
    @Autowired
    private SalesOrderService salesOrderService;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private OrderCenterClient orderCenterClient;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private OrderStatService orderStatService;
    @Autowired
    private SalesOrderNodeInfoService salesOrderNodeInfoService;

    @Override
    public void addDeliveryBatch(DeliveryBatch deliveryBatch, List<SalesOrder> orders) throws ServiceException {
        if (CollectionUtils.isEmpty(orders)) {
            throw new ServiceException("销售订单为空");
        }
        try {
            // 添加发货批次
            deliveryBatch.setBatchCode(getBizCode(BATCH_OUT));
            deliveryBatch.setPreparedTime(new Date());
            deliveryBatch.setEnabled(WmsConstants.ENABLED_TRUE);
            deliveryBatch.setHandlingStatus(DeliveryBatchStatus.PENDING.getCode());
            deliveryBatchDao.addDeliveryBatch(deliveryBatch);

            //			List<SalesOrder> orders = orderDao.queryOrderListByIds(Lists.newArrayList(orderIds));
            //			if (CollectionUtils.isEmpty(orders)) {
            //				throw new ServiceException("订单号不存在");
            //			}
            // 检测待发货销售订单是否满足发货条件
            Set<String> orderCodes = Sets.newHashSet();    // 订单号
            for (SalesOrder order : orders) {
                if (OrderStatus.FILTERED.getCode() != order.getOrderStatus()) {
                    throw new ServiceException("订单状态异常");
                }
                orderCodes.add(order.getOrderCode());
            }

            // 生成发货编号(orderCode+两位增长因子)
            Map<String, Integer> suffixMap = Maps.newHashMap();
            List<Delivery> existingDeliveryList = deliveryDao.queryDeliveryListByOriginalCodes(Lists.newArrayList(orderCodes));
            for (Delivery delivery : existingDeliveryList) {
                Integer suffix = Integer.valueOf(StringUtils.right(delivery.getDeliveryCode(), 2));
                if (suffixMap.containsKey(delivery.getOriginalCode())) {
                    // StringUtils.stripStart(suffix, "0");
                    if (suffix > suffixMap.get(delivery.getOriginalCode())) {
                        suffixMap.put(delivery.getOriginalCode(), suffix);
                    }
                } else {
                    suffixMap.put(delivery.getOriginalCode(), suffix);
                }
            }

            // 批量添加发货单
            List<Delivery> deliverys = Lists.newArrayList();
            for (SalesOrder order : orders) {
                Delivery delivery = new Delivery();
                BeanUtils.copyProperties(delivery, order);
                Integer existingSuffix = suffixMap.get(order.getOrderCode());
                String suffixStr = existingSuffix == null ? "01" : StringUtils.leftPad((++existingSuffix).toString(), 2, "0");
                delivery.setDeliveryCode(order.getOrderCode() + suffixStr);
                delivery.setBatchId(deliveryBatch.getId());
                delivery.setBatchCode(deliveryBatch.getBatchCode());
                delivery.setWarehouseId(deliveryBatch.getWarehouseId());
                delivery.setWarehouseName(deliveryBatch.getWarehouseName());
                delivery.setOriginalId(order.getId());
                delivery.setOriginalCode(order.getOrderCode());
                delivery.setInvoiceStatus(WmsConstants.ENABLED_FALSE);
                delivery.setHandlingStatus(DeliveryStatus.UNSHIPPED.getCode());
                delivery.setPreparedBy(deliveryBatch.getPreparedBy());
                delivery.setPreparedTime(new Date());
                deliverys.add(delivery);
            }
            deliveryDao.addDeliveryList(deliverys);

            List<Long> orderIds = Lists.newArrayList();
            for (SalesOrder order : orders) {
                orderIds.add(order.getId());
            }

            // 批量添加发货商品
            deliveryDao.addDeliveryGoodsListByOrderIds(Lists.newArrayList(orderIds));

            // 批量更新销售订单状态
            for (SalesOrder order : orders) {
                order.setOrderStatus(OrderStatus.PICKED.getCode());
            }
            orderDao.batchUpdateOrder(orders);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void cancelDeliveryBatch(Long batchId) {
        DeliveryBatch deliveryBatch = getDeliveryBatch(batchId);
        if (deliveryBatch == null) {
            return;
        }
        if (DeliveryBatchStatus.FINISHED.getCode() == deliveryBatch.getHandlingStatus()) {
            throw new ServiceException("发货批次状态异常");
        }
        if (!canCancel(batchId)) {
            throw new ServiceException("存在发票、配送单号、商品编码的操作，不能取消");
        }

        try {
            // 更新出库批次内的订单配送状态为未发货状态
            orderDao.updateToUnshippedByDelyBatchId(batchId);
            // 保存操作日志
            if (deliveryBatch.getId() == null) {
                throw new ServiceException("参数错误");
            }
            Map<String, Object> paramsMap = Maps.newHashMap();
            paramsMap.put("batchId", batchId);
            List<SalesOrder> orderList = orderDao.queryOrderList(paramsMap);
            if (CollectionUtils.isNotEmpty(orderList)) {
                List<Long> orderIds = new ArrayList<Long>();
                for (SalesOrder order : orderList) {
                    orderIds.add(order.getId());
                }

                //批量保存操作日志
                Map<String, Object> paramsMap2 = Maps.newHashMap();
                try {
                    paramsMap2.put("orderIds", orderIds);
                    paramsMap2.put("orderStatus", WmsConstants.OrderStatus.CANCELED.getCode());
                    paramsMap2.put("opUser", ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
                    paramsMap2.put("opTime", new Date());
                    paramsMap2.put("remark", "更新订单为已取消状态");
                    salesOrderLogDao.batchInsertSalesOrderLog(paramsMap2);
                } catch (Exception e) {
                    logger.error("业务日志记录异常", e);
                }

            }

            // 删除批次内的发货商品
            deliveryDao.deleteDeliveryGoodsByBatchId(batchId);

            // 删除批次内的发货单
            deliveryDao.deleteDeliveryByBatchId(batchId);

            // 删除出库批次
            deliveryBatchDao.deleteDeliveryBatch(batchId);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 判断是否能取消
     */
    private boolean canCancel(Long delyBatchId) {
        try {
            return StringUtils.isNotBlank(deliveryDao.queryBatchWhetherIsCancelable(delyBatchId));
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 确认发货
     */
    @Override
    public void confirmDeliveryBatch(DeliveryBatch deliveryBatch) {
        // 检测批次出库是否满足确认出库的条件
        checkConfirm(deliveryBatch);
        List<SalesOrder> shippedOrderList = null;
        try {
            // 更新出库批次的确认信息
            deliveryBatch.setHandlingStatus(DeliveryBatchStatus.FINISHED.getCode());
            deliveryBatch.setHandledTime(new Date());
            deliveryBatch.setHandledBy(ActionUtils.getLoginName());
            deliveryBatchDao.updateDeliveryBatch(deliveryBatch);

            // 批量更新发货单确认信息
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("batchId", deliveryBatch.getId());
            criteria.put("handledTime", new Date());
            criteria.put("handledBy", ActionUtils.getLoginName());
            deliveryDao.updateDeliveryConfirmInfo(criteria);

            // 批量更新发货商品状态//
            criteria.clear();
            criteria.put("batchId", deliveryBatch.getId());
            deliveryDao.updateDeliveryGoodsConfirmStatus(criteria);

            // 批量更新销售订单发货信息//
            criteria.clear();
            criteria.put("batchId", deliveryBatch.getId());
            criteria.put("shippingTime", new Date());
            orderDao.updateShippedInfoByDelyBatchId(criteria);
            // 保存操作日志
            if (deliveryBatch.getId() == null) {
                throw new ServiceException("参数错误");
            }
            Map<String, Object> paramsMap = Maps.newHashMap();
            criteria.put("batchId", deliveryBatch.getId());
            List<SalesOrder> orderList = orderDao.queryOrderList(paramsMap);
            if (CollectionUtils.isNotEmpty(orderList)) {
                List<Long> orderIds = new ArrayList<Long>();
                for (SalesOrder order : orderList) {
                    orderIds.add(order.getId());
                }

                //批量保存操作日志
                Map<String, Object> paramsMap2 = Maps.newHashMap();
                try {
                    paramsMap2.put("orderIds", orderIds);
                    paramsMap2.put("orderStatus", WmsConstants.OrderStatus.SHIPPED.getCode());
                    paramsMap2.put("opUser", ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
                    paramsMap2.put("opTime", new Date());
                    paramsMap2.put("remark", "更新订单为已出库状态");
                    salesOrderLogDao.batchInsertSalesOrderLog(paramsMap2);
                } catch (Exception e) {
                    logger.error("业务日志记录异常", e);
                }

            }
            // 批量更新发货商品个体的出库确认信息
            criteria.clear();
            criteria.put("outTime", new Date());
            criteria.put("batchId", deliveryBatch.getId());
            indivDao.updateIndivsOutConfirmInfo(criteria);

            // 批量更新发货商品个体的流转确认信息
            criteria.clear();
            criteria.put("flowType", IndivFlowType.OUT_SALES.getCode());
            criteria.put("flowTime", new Date());
            criteria.put("batchId", deliveryBatch.getId());
            indivDao.updateIndivsFlowConfirmInfo(criteria);

            // 根据发货商品数量更新库存
            List<DeliverySummary> goodsSummaryList = getDeliverySummaryListByBatchId(deliveryBatch.getId());
            if (CollectionUtils.isEmpty(goodsSummaryList)) {
                throw new ServiceException("发货商品汇总为空");
            }
            for (DeliverySummary summary : goodsSummaryList) {
                StockRequest stockRequest = new StockRequest(summary.getWarehouseId(), summary.getSkuId(), StockType.STOCK_SALES, summary.getQuantity(), StockBizType.OUT_SALES, deliveryBatch.getBatchCode());
                stockService.decreaseStock(stockRequest);
            }
            // 获得所有配送方式
            List<Shipping> validShippings = shippingService.getValidShippings();
            Map<Long, Shipping> validShippingMap = Maps.newHashMap();
            for (Shipping shipping : validShippings) {
                validShippingMap.put(shipping.getId(), shipping);
            }
            shippedOrderList = orderDao.queryOrderListByDelyBatchId(deliveryBatch.getId());
            // 添加快递订阅信息,为每一个订单添加一条快递推送记录，如果出错，终止整个出货工作并回滚
            for (SalesOrder order : shippedOrderList) {
                if (null != order.getShippingNo() && !"".equals(order.getShippingNo())) {
                    Shipping shipping = validShippingMap.get(order.getShippingId());
                    if (null == shipping) {
                        throw new ServiceException("配送方式不存在");
                    }
                    try {
                        ShippingInfo shippingInfo = shippingInfoService.getShippingInfo(shipping.getCompanyCode(), order.getShippingNo());
                        if (null == shippingInfo) {
                            shippingInfo = new ShippingInfo();
                            shippingInfo.setOrderCode(order.getOrderCode());    // 订单号
                            shippingInfo.setCompany(shipping.getCompanyCode());    // 物流公司编码
                            shippingInfo.setShippingNo(order.getShippingNo());    // 物流快递单号
                            shippingInfo.setToAddr(order.getProvince() + order.getCity() + order.getDistrict());
                            shippingInfoService.addShippingInfo(shippingInfo);    // 增加一条订阅记录(如果增加失败,将在过后的轮询中继续增加)
                        } else {
                            logger.info("物流信息已存在" + order.getShippingNo());
                        }
                    } catch (DataAccessException ex) {
                        logger.error("更新快递消息失败", ex);
                        throw new ServiceException("更新快递消息失败");
                    }
                }
            }
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(e);
        }

        // 通知订单中心已发货
        salesOrderService.notifyOrder(shippedOrderList);
    }

    private void checkConfirm(DeliveryBatch deliveryBatch) throws ServiceException {
        // 检测出库批次状态是否正常
        if (DeliveryBatchStatus.PENDING.getCode() != deliveryBatch.getHandlingStatus()) {
            throw new ServiceException("发货批次状态异常");
        }
        try {
            // 检测是否还有发货单尚未完成发票或配送信息操作
            if (deliveryDao.queryUnfinishedDeliveryTotal(deliveryBatch.getId()) > 0) {
                throw new ServiceException("存在发票信息或配送信息不全的发货单");
            }
            // 检测是否还有发货单尚未完成商品编码的绑定
            if (deliveryDao.queryIndivUnfinishedGoodsTotal(deliveryBatch.getId()) > 0) {
                throw new ServiceException("还有发货商品未完成商品编码的绑定");
            }
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<DeliveryBatch> getDeliveryBatchList(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        try {
            return deliveryBatchDao.queryDeliveryBatchByPage(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int getDeliveryBatchTotal(Map<String, Object> criteria) {
        try {
            return deliveryBatchDao.queryDeliveryBatchTotal(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public DeliveryBatch getDeliveryBatch(Long id) {
        return deliveryBatchDao.queryDeliveryBatch(id);
    }

    @Override
    public List<Delivery> getDeliveryList(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        try {
            return deliveryDao.queryDeliveryByPage(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int getDeliveryListTotal(Map<String, Object> criteria) {
        try {
            return deliveryDao.queryDeliveryTotal(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Delivery getDelivery(Long id) {
        return deliveryDao.queryDelivery(id);
    }

    @Override
    public Delivery getDeliveryByOrderId(Long orderId) {
        return deliveryDao.queryDeliveryByOriginalId(orderId);
    }

    @Override
    public List<Delivery> getDeliveryListByBatchCode(String batchCode) {
        if (StringUtils.isBlank(batchCode)) {
            throw new ServiceException("参数错误");
        }
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("batchCode", batchCode);
        Page page = new Page();
        page.setStartRow(1);
        page.setEndRow(Integer.MAX_VALUE);
        return getDeliveryList(criteria, page);
    }

    @Override
    public List<Delivery> getDeliveryListByBatchId(Long batchId) {
        if (batchId == null) {
            throw new ServiceException("参数错误");
        }
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("batchId", batchId);
        return deliveryDao.queryDeliveryList(criteria);
    }

    @Override
    public List<Delivery> getDeliveryListSearch(Map<String, Object> criteria) {
        return deliveryDao.queryDeliveryListSearch(criteria);
    }

    @Override
    public List<DeliveryGoods> getDeliveryGoodsByPage(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        try {
            return deliveryDao.queryDeliveryGoodsByPage(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public int getDeliveryGoodsTotal(Map<String, Object> criteria) {
        try {
            return deliveryDao.queryDeliveryGoodsTotal(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<DeliveryGoods> getDeliveryGoodsList(Long deliveryId) {
        if (deliveryId == null) {
            throw new ServiceException("参数错误");
        }
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("deliveryId", deliveryId);
        try {
            return deliveryDao.queryDeliveryGoodsList(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<DeliveryGoods> getDeliveryGoodsListByOrderId(Long orderId) {
        if (orderId == null) {
            throw new ServiceException("参数错误");
        }
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("originalId", orderId);
        try {
            return deliveryDao.queryDeliveryGoodsList(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public DeliveryGoods getDeliveryGoods(Long id) throws ServiceException {
        return deliveryDao.queryDeliveryGoods(id);
    }

    @Override
    public List<DeliverySummary> getDeliverySummaryList(Map<String, Object> criteria, Page page) {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        try {
            return deliveryDao.queryDeliverySummaryByPage(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Integer getDeliverySummaryTotal(Map<String, Object> criteria) {
        return deliveryDao.queryDeliverySummaryTotal(criteria);
    }

    @Override
    public List<DeliverySummary> getDeliverySummaryListByBatchId(Long deliveryBatchId) throws ServiceException {
        if (deliveryBatchId == null) {
            throw new ServiceException("参数错误");
        }
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("batchId", deliveryBatchId);
        return deliveryDao.queryDeliverySummaryList(criteria);
    }

    @Override
    public void updateDeliveryIndivs(DeliveryGoods goods, List<IndivFlow> indivList) throws ServiceException {
        // 检测发货单状态是否正常
        Delivery delivery = goods.getDelivery();
        if (DeliveryStatus.UNSHIPPED.getCode() != delivery.getHandlingStatus()) {
            throw new ServiceException("发货状态异常");
        }

        // 检测录入的商品编码是否合法
        if (CollectionUtils.isEmpty(indivList)) {
            throw new ServiceException("商品编码为空");
        }
        if (indivList.size() > goods.getQuantity()) {
            throw new ServiceException("编码录入个数大于发货数量");
        }
        Map<String, IndivFlow> currentIndivMap = Maps.newHashMap();
        for (IndivFlow indivFlow : indivList) {
            currentIndivMap.put(indivFlow.getIndivCode(), indivFlow);
        }
        if (currentIndivMap.size() < indivList.size()) {
            throw new ServiceException("录入的商品编码存在重复");
        }

        List<Indiv> existingIndivList;
        try {
            existingIndivList = indivDao.queryIndivListByCodes(Lists.newArrayList(currentIndivMap.keySet()));
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
        if (CollectionUtils.isEmpty(existingIndivList)) {
            throw new ServiceException("商品编号不存在");
        }
        Map<String, Indiv> existingIndivMap = Maps.newHashMap();
        for (Indiv indiv : existingIndivList) {
            existingIndivMap.put(indiv.getIndivCode(), indiv);
        }
        for (IndivFlow currentIndiv : indivList) {
            if (!existingIndivMap.containsKey(currentIndiv.getIndivCode())) {
                throw new ServiceException("此商品编码不存在：" + currentIndiv.getIndivCode());
            }
            Indiv existingIndiv = existingIndivMap.get(currentIndiv.getIndivCode());
            if (existingIndiv.getSkuId().longValue() != goods.getSkuId().longValue()) {
                throw new ServiceException("此商品编码不属于当前SKU：" + existingIndiv.getIndivCode());
            }
            if (existingIndiv.getWarehouseId().longValue() != delivery.getWarehouseId().longValue()) {
                throw new ServiceException("此编码对应的商品不属于当前仓库：" + existingIndiv.getIndivCode());
            }
            if (IndivStockStatus.STOCKIN_HANDLING.getCode() == existingIndiv.getStockStatus()) {
                throw new ServiceException("此编码对应的商品未完成入库：" + existingIndiv.getIndivCode());
            }
            if (IndivStockStatus.OUT_WAREHOUSE.getCode() == existingIndiv.getStockStatus()) {
                throw new ServiceException("此编码对应的商品已经出库：" + existingIndiv.getIndivCode());
            }
            if (IndivStockStatus.STOCKOUT_HANDLING.getCode() == existingIndiv.getStockStatus() && !delivery.getDeliveryCode().equals(existingIndiv.getOutCode())) {
                throw new ServiceException("此商品编码已被其它发货单绑定：" + existingIndiv.getIndivCode());
            }
            if (IndivWaresStatus.DEFECTIVE.getCode() == existingIndiv.getWaresStatus()) {
                throw new ServiceException("此编码对应的商品是次品：" + existingIndiv.getIndivCode());
            }
            // if (StringUtils.isNotBlank(existingIndiv.getOrderCode())
            // && !existingIndiv.getOrderCode().equals(goods.getOriginalCode()))
            // {
            // throw new ServiceException("此编码商品个体已经被其他订单占用：" +
            // existingIndiv.getIndivCode());
            // }
        }

        try {
            // 解除发货单商品与当前个体的预绑定关系
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("outId", delivery.getId());
            criteria.put("skuId", goods.getSkuId());
            indivDao.clearIndivDeliveryInfo(criteria);

            // 删除当前发货商品个体的流转信息
            criteria.clear();
            criteria.put("flowType", IndivFlowType.OUT_SALES.getCode());
            criteria.put("flowGoodsId", goods.getId());
            indivDao.deleteIndivFlowsByFlowGoodsId(criteria);

            // 重新为发货商品绑定新个体
            criteria.clear();
            criteria = Maps.newHashMap();
            criteria.put("skuId", goods.getSkuId());
            criteria.put("indivCodes", Lists.newArrayList(currentIndivMap.keySet()));
            criteria.put("outId", delivery.getId());
            criteria.put("outCode", delivery.getDeliveryCode());
            criteria.put("orderId", goods.getDelivery().getOriginalId());
            criteria.put("orderCode", goods.getDelivery().getOriginalCode());
            indivDao.addIndivDeliveryInfo(criteria);

            // 添加发货个体的流转信息
            for (IndivFlow indivFlow : indivList) {
                indivFlow.setFlowType(IndivFlowType.OUT_SALES.getCode());
                indivFlow.setFlowId(delivery.getId());
                indivFlow.setFlowCode(delivery.getDeliveryCode());
                indivFlow.setFlowGoodsId(goods.getId());
                indivFlow.setWaresStatus(goods.getWaresStatus());
                indivFlow.setSkuId(goods.getSkuId());
                indivFlow.setSkuCode(goods.getSkuCode());
                indivFlow.setSkuName(goods.getSkuName());
                indivFlow.setMeasureUnit(goods.getMeasureUnit());
                indivFlow.setWarehouseId(delivery.getWarehouseId());
                indivFlow.setWarehouseName(delivery.getWarehouseName());
                indivFlow.setEnabled(WmsConstants.ENABLED_FALSE);
            }
            indivDao.addIndivFlows(indivList);

            // 更新发货商品的个体绑定情况
            goods.setIndivFinished(WmsConstants.ENABLED_TRUE);
            deliveryDao.updateDeliveryGoods(goods);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateShippingInfo(Delivery delivery) throws ServiceException {
        if (DeliveryStatus.UNSHIPPED.getCode() != delivery.getHandlingStatus()) {
            throw new ServiceException("发货单状态异常");
        }
        Shipping shipping = shippingService.getShipping(delivery.getShippingId());
        if (shipping == null) {
            throw new ServiceException("配送方式不存在");
        }
        try {
            delivery.setShippingName(shipping.getShippingName());
            deliveryDao.updateShippingInfo(delivery);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateShippingInfoList(Long batchId, List<Delivery> deliveryList) throws ServiceException {
        if (CollectionUtils.isEmpty(deliveryList)) {
            throw new ServiceException("参数错误");
        }
        try {
            DeliveryBatch deliveryBatch = deliveryBatchDao.queryDeliveryBatch(batchId);
            if (DeliveryBatchStatus.PENDING.getCode() != deliveryBatch.getHandlingStatus()) {
                throw new ServiceException("发货批次状态异常");
            }

            // 检测请求参数是否正确(配送方式合法、配送单号未重复)
            Map<String, Delivery> deliveryMap = Maps.newHashMap();
            Set<String> shippingNoSet = Sets.newHashSet();
            List<Shipping> validShippings = shippingService.getValidShippings();
            Map<Long, Shipping> validShippingMap = Maps.newHashMap();
            for (Shipping shipping : validShippings) {
                validShippingMap.put(shipping.getId(), shipping);
            }
            for (Delivery delivery : deliveryList) {
                if (validShippingMap.containsKey(delivery.getShippingId())) {
                    delivery.setShippingName(validShippingMap.get(delivery.getShippingId()).getShippingName());
                } else {
                    throw new ServiceException("配送方式不存在");
                }
                deliveryMap.put(delivery.getDeliveryCode(), delivery);
                shippingNoSet.add(delivery.getShippingNo());
            }
            if (deliveryMap.size() != deliveryList.size()) {
                throw new ServiceException("参数错误");
            }
            if (shippingNoSet.size() < deliveryList.size()) {
                throw new ServiceException("输入的配送单号存在重复");
            }

            // 批量更新配送信息
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("batchId", batchId);
            List<Delivery> trustedDeliveryList = deliveryDao.queryDeliveryList(criteria);
            Iterator<Delivery> itr = trustedDeliveryList.iterator();
            while (itr.hasNext()) {
                Delivery trustedDelivery = itr.next();
                if (deliveryMap.containsKey(trustedDelivery.getDeliveryCode())) {
                    Delivery delivery = deliveryMap.get(trustedDelivery.getDeliveryCode());
                    trustedDelivery.setShippingId(delivery.getShippingId());
                    trustedDelivery.setShippingName(delivery.getShippingName());
                    trustedDelivery.setShippingNo(delivery.getShippingNo());
                } else {
                    itr.remove();
                }
            }
            deliveryDao.updateDeliveryList(trustedDeliveryList);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void addInvoiceInfo(Delivery delivery) throws ServiceException {
        if (DeliveryStatus.UNSHIPPED.getCode() != delivery.getHandlingStatus()) {
            throw new ServiceException("发货单状态异常");
        }
        try {
            deliveryDao.updateInvoiceInfo(delivery);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void addDeliveryBatchByIds(DeliveryBatch deliveryBatch, String ids) {
        try {
            // 添加发货批次
            deliveryBatch.setBatchCode(getBizCode(BATCH_OUT));
            deliveryBatch.setPreparedTime(new Date());
            deliveryBatch.setEnabled(WmsConstants.ENABLED_TRUE);
            deliveryBatch.setHandlingStatus(DeliveryBatchStatus.PENDING.getCode());
            deliveryBatchDao.addDeliveryBatch(deliveryBatch);

            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("ids", ids.split(","));
            criteria.put("batchId", deliveryBatch.getId());
            criteria.put("batchCode", deliveryBatch.getBatchCode());
            orderDao.updateOrderByIds(criteria);

            //根据ids更新订单为已筛单状态
            orderDao.updateSalesOrderFilterStatus(ids.split(","));

            //保存操作日志
            Map<String, Object> paramsMap = Maps.newHashMap();
            try {
                paramsMap.put("orderIds", ids.split(","));
                paramsMap.put("orderStatus", WmsConstants.OrderStatus.PRINTED.getCode());
                paramsMap.put("opUser", ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
                paramsMap.put("opTime", new Date());
                paramsMap.put("remark", "更新订单为已打单状态");
                salesOrderLogDao.batchInsertSalesOrderLog(paramsMap);
            } catch (Exception e) {
                logger.error("业务日志记录异常", e);
            }

        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }


    /** {@inheritDoc} */
    @Override
    public String confirmDeliveryWap(Long warehouseId, String batchCode) {
        String resultMsg = "发货成功";
        // 获得所有配送方式
        List<Shipping> validShippings = shippingService.getValidShippings();
        Map<Long, Shipping> validShippingMap = Maps.newHashMap();
        for (Shipping shipping : validShippings) {
            validShippingMap.put(shipping.getId(), shipping);
        }
        Map<String, Object> criteria = Maps.newHashMap();
        try {
            Date nowTime = new Date();
            criteria.put("batchCode", batchCode);
            List<Delivery> deliveryList = deliveryDao.queryDeliveryList(criteria);
            String[] orderIds = new String[deliveryList.size()];
            List<String> orderIdList = Lists.newArrayList();
            for (int i = 0; i < deliveryList.size(); i++) {
                orderIds[i] = deliveryList.get(i).getOriginalId() + "";
            }
            List<SalesOrderVo> orderVoList = orderDao.queryOrderListByOrderIds(orderIds);    // 包含商品列表的订单
            StringBuilder canceledOrderStr = new StringBuilder("");
            for (SalesOrderVo salesOrderVo : orderVoList) {
                //检查处理已取消订单
                if (salesOrderVo.getOrderStatus().equals(OrderStatus.CANCELED.getCode())) {
                    deliveryDao.deleteDeliveryByOrderCode(salesOrderVo.getOrderCode());
                    canceledOrderStr.append(salesOrderVo.getOrderCode() + salesOrderVo.getConsignee() + "-");
                } else {
                    orderIdList.add(String.valueOf(salesOrderVo.getId()));
                }
            }
            orderIds = new String[orderIdList.size()];
            if (canceledOrderStr.length() > 0) {
                resultMsg = canceledOrderStr.toString() + "这些单已取消，系统已将其从该批次移出，请手工将取消单检出";
            }
            for (int i = 0; i < orderIdList.size(); i++) {
                orderIds[i] = orderIdList.get(i);
            }
            List<SalesOrder> orderList = new ArrayList<SalesOrder>();    // 需要更新状态并推送给订单中心的订单
            List<SalesOutStat> statList = new ArrayList<SalesOutStat>();
            for (SalesOrderVo salesOrderVo : orderVoList) {
                if (salesOrderVo.getOrderStatus().equals(OrderStatus.CANCELED.getCode())) {
                    continue;
                }
                criteria.clear();
                criteria.put("stockStatus", WmsConstants.IndivStockStatus.OUT_WAREHOUSE.getCode());    // 设置为出库
                criteria.put("outId", salesOrderVo.getId());    // 发货(订)单数据库id
                criteria.put("outCode", salesOrderVo.getDeliveryCode());
                criteria.put("orderId", salesOrderVo.getId());
                criteria.put("orderCode", salesOrderVo.getOrderCode());
                criteria.put("outTime", nowTime);    // 出库日期
                indivDao.updateIndivStatusByOutId(criteria);
                Map<Long, Integer> sumSkuMap = new HashMap<Long, Integer>();    // 记录本批订单中出货每个sku的总量

                // 将所有发货商品数量汇总
                List<SalesOrderGoods> goods = salesOrderVo.getGoodsList();    // 商品列表
                for (SalesOrderGoods salesOrderGoods : goods) {
                    SalesOutStat outStat = new SalesOutStat();
                    Long skuId = salesOrderGoods.getSkuId();
                    Integer quantity = salesOrderGoods.getQuantity();
                    Integer quanInSum = sumSkuMap.get(skuId);    // 某skuId下的总量
                    if (null == quanInSum) {
                        sumSkuMap.put(skuId, quantity);    // 设置skuId起始值
                    } else {
                        quanInSum = quanInSum + quantity;    // 累加该skuId的数量
                        sumSkuMap.put(skuId, quanInSum);
                    }
                    outStat.setSkuId(skuId);    // skuid
                    outStat.setSkuName(salesOrderGoods.getSkuName());    //	sku名称
                    outStat.setSkuCode(salesOrderGoods.getSkuCode());    // sku编码
                    outStat.setQuantity(quantity);
                    outStat.setIndivEnabled(salesOrderGoods.getIndivEnabled());
                    outStat.setUnitPrice(salesOrderGoods.getUnitPrice());    // 单价
                    outStat.setPartnerCode(null);                    // 渠道编号
                    outStat.setPartnerName(WmsConstants.getOrderSource(salesOrderVo.getOrderSource()));    // 渠道名称
                    outStat.setOrderId(salesOrderVo.getId());    // 订单id
                    outStat.setOrderCode(salesOrderVo.getOrderCode());    // 订单号
                    outStat.setPayNo(salesOrderVo.getPayNo());    // 支付流水号
                    outStat.setShippingTime(nowTime);
                    outStat.setOutType(0);    // 记录类型(0:订单 1:调拨)
                    outStat.setInvoiceAmount(salesOrderVo.getInvoiceAmount());
                    outStat.setOrderAmount(salesOrderVo.getOrderAmount());
                    outStat.setInvoiceStatus(salesOrderVo.getInvoiceStatus());
                    statList.add(outStat);
                }

                warehouseId = warehouseService.getWarehouseByOrderSource(salesOrderVo.getOrderSource()).getId();
                // 将汇总的sku扣减库存
                for (Iterator<Long> iterator = sumSkuMap.keySet().iterator(); iterator.hasNext(); ) {
                    Long skuId = iterator.next();
                    Integer quantity = sumSkuMap.get(skuId);
                    StockRequest stockRequest = new StockRequest(warehouseId, skuId, StockType.STOCK_OCCUPY, quantity, StockBizType.OUT_SALES, salesOrderVo.getOrderCode());
                    stockService.decreaseStock(stockRequest);
                }

                if (null != salesOrderVo.getShippingNo() && !"".equals(salesOrderVo.getShippingNo())) {
                    Shipping shipping = validShippingMap.get(salesOrderVo.getShippingId());
                    if (null == shipping) {
                        throw new ServiceException(salesOrderVo.getOrderCode() + " 订单配送方式异常, 无法订阅快递信息");
                    }
                    try {
                        ShippingInfo shippingInfo = shippingInfoService.getShippingInfo(shipping.getCompanyCode(), salesOrderVo.getShippingNo());
                        if (null == shippingInfo) {
                            shippingInfo = new ShippingInfo();
                            shippingInfo.setOrderCode(salesOrderVo.getOrderCode());    // 订单号
                            shippingInfo.setShippingId(salesOrderVo.getShippingId());    // 设置物流公司id
                            shippingInfo.setShippingNo(salesOrderVo.getShippingNo());    // 物流快递单号
                            shippingInfo.setToAddr(salesOrderVo.getProvince() + salesOrderVo.getCity() + salesOrderVo.getDistrict());
                            shippingInfoService.addShippingInfo(shippingInfo);    // 增加一条订阅记录(如果增加失败,将在过后的轮询中继续增加)
                        } else {
                            logger.info("物流信息已存在" + salesOrderVo.getShippingNo());
                        }
                    } catch (DataAccessException ex) {
                        logger.error("更新快递消息失败", ex);
                        throw new ServiceException("更新快递消息失败");
                    }
                }
                orderList.add(transToOrder(salesOrderVo));
            }

            if (orderIds.length > 0) {
                criteria.clear();
                criteria.put("handledBy", ActionUtils.getLoginName());
                criteria.put("handledTime", nowTime);
                criteria.put("shippingTime", nowTime);
                criteria.put("ids", orderIds);
                orderDao.updateOrderOutByIds(criteria);

                //保存操作日志
                Map<String, Object> paramsMap = Maps.newHashMap();
                try {
                    paramsMap.put("orderIds", orderIds);
                    paramsMap.put("orderStatus", WmsConstants.OrderStatus.SHIPPED.getCode());
                    paramsMap.put("opUser", ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
                    paramsMap.put("opTime", nowTime);
                    paramsMap.put("remark", "更新订单为已出库状态");
                    salesOrderLogDao.batchInsertSalesOrderLog(paramsMap);

                    // TODO 订单节点记录

                } catch (Exception e) {
                    logger.error("业务日志记录异常", e);
                }

                salesOrderService.notifyOrder(orderList);
                orderStatService.addSalesOutStat(statList);

                //删除对应的批次信息
                deliveryDao.deleteDeliveryByBatchCode(batchCode);
            }
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException(e);
        }

        return resultMsg;
    }

    private SalesOrder transToOrder(SalesOrderVo salesOrderVo) {
        try {
            SalesOrder order = new SalesOrder();
            //		BeanUtils.copyProperties(order, salesOrderVo);
            order.setId(salesOrderVo.getId());
            order.setOrderUser(salesOrderVo.getOrderUser());
            order.setOrderCode(salesOrderVo.getOrderCode());
            order.setOrderStatus(WmsConstants.OrderStatus.SHIPPED.getCode());
            order.setOrderSource(salesOrderVo.getOrderSource());
            order.setPaymentType(salesOrderVo.getPaymentType());
            order.setShippingId(salesOrderVo.getShippingId());
            order.setShippingNo(salesOrderVo.getShippingNo());
            order.setConsignee(salesOrderVo.getConsignee());
            order.setProvince(salesOrderVo.getProvince());
            order.setCity(salesOrderVo.getCity());
            order.setDistrict(salesOrderVo.getDistrict());
            order.setAddress(salesOrderVo.getAddress());
            order.setFullAddress(salesOrderVo.getFullAddress());
            order.setOrderNotifyCount(salesOrderVo.getOrderNotifyCount());
            return order;
        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public void addDelivery(Map<String, Object> params) {
        deliveryDao.addDelivery(params);
    }

    /** {@inheritDoc} */
    @Override
    public List<DeliveryDetails> queryDeliveryDetailsList(Map<String, Object> params) {
        return deliveryDao.queryDeliveryDetailsList(params);
    }

    /** {@inheritDoc} */
    @Override
    public Integer queryDeliveryDetailsCount(Map<String, Object> params) {
        return deliveryDao.queryDeliveryDetailsCount(params);
    }

}
