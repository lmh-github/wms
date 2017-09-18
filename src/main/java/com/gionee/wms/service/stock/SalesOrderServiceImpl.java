package com.gionee.wms.service.stock;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.*;
import com.gionee.wms.dao.*;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.SendRequest;
import com.gionee.wms.dto.StockRequest;
import com.gionee.wms.entity.*;
import com.gionee.wms.facade.dto.OrderNotifyDTO;
import com.gionee.wms.facade.dto.OrderNotifyGoodsDTO;
import com.gionee.wms.facade.result.WmsResult.WmsCodeEnum;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.ShippingService;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.log.LogService;
import com.gionee.wms.service.wares.SkuMapService;
import com.gionee.wms.vo.OrderStatusStatVo;
import com.gionee.wms.vo.OrderStatusVo;
import com.gionee.wms.vo.SalesOrderVo;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.gionee.wms.web.client.OCClient;
import com.gionee.wms.web.client.OrderCenterClient;
import com.gionee.wms.web.client.SyncVipOrderClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.routdata.zzfw.webservice.service.OrderCodeUtils;
import com.sf.integration.warehouse.request.*;
import com.sf.integration.warehouse.response.*;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.http.impl.cookie.DateUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.TaskRejectedException;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

@Service("orderService")
public class SalesOrderServiceImpl implements SalesOrderService {
    private static Logger logger = LoggerFactory.getLogger(SalesOrderServiceImpl.class);
    @Autowired
    public SalesOrderLogDao salesOrderLogDao = null;
    JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
    @Autowired
    private LogService logService;
    @Autowired
    private SalesOrderDao orderDao;
    @Autowired
    private WaresDao waresDao;
    @Autowired
    private DeliveryBatchDao deliveryBatchDao;
    @Autowired
    private IndivDao indivDao;
    @Autowired
    private StockService stockService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private TaskExecutor taskExecutor;
    @Autowired
    private OrderCenterClient orderCenterClient;
    @Autowired
    private OCClient oCClient;
    @Autowired
    private ShippingService shippingService;
    @Autowired
    private BackService backService;
    @Autowired
    private SkuMapService skuMapService;
    @Autowired
    private SFWebService sfWebService; // 顺丰WebService
    @Autowired
    private SalesOrderMapService salesOrderMapService;
    @Autowired
    private SystemConfigService configService; // 系统配置
    @Autowired
    private InvoiceInfoService invoiceInfoSerivce;
    @Autowired
    private SalesOrderNodeInfoService salesOrderNodeInfoService;

    @Override
    public List<SalesOrder> getSalesOrderList(Map<String, Object> criteria, Page page) throws ServiceException {
        if (criteria == null) {
            criteria = Maps.newHashMap();
        }
        criteria.put("page", page);
        try {
            return orderDao.queryOrderByPage(criteria);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Integer getSalesOrderTotal(Map<String, Object> criteria) {
        return orderDao.queryOrderTotal(criteria);
    }

    @Override
    public List<SalesOrder> getSalesOrderList(String stockOutCode) throws ServiceException {
        if (StringUtils.isBlank(stockOutCode)) {
            throw new ServiceException("参数错误");
        }
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("stockOutCode", stockOutCode);
        Page page = new Page();
        page.setStartRow(1);
        page.setEndRow(Integer.MAX_VALUE);
        return getSalesOrderList(criteria, page);
    }

    @Override
    public SalesOrder getSalesOrder(Long id) {
        try {
            return orderDao.queryOrder(id);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<SalesOrder> getSalesOrdersNeedToBeNotified() {
        try {
            return orderDao.queryOrderListNeedToBeNotified();
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    /**
     * 获取指定发货批次内的销售订单(包含商品清单)列表
     */
    @Override
    public List<SalesOrderVo> queryGoodsListByOrderIds(String[] ids) {
        try {
            return orderDao.queryOrderListByOrderIds(ids);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<SalesOrderGoods> getOrderGoodsList(Long orderId) throws ServiceException {
        try {
            return orderDao.queryGoodsListByOrderId(orderId);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<SalesOrderGoods> getOrderGoodsListForPrepare(Long orderId) throws ServiceException {
        try {
            return orderDao.queryGoodsListForPrepare(orderId);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<SalesOrderGoods> getOrderGoodsListByOrderCode(String orderCode) throws ServiceException {
        try {
            return orderDao.queryGoodsListByOrderCodes(Lists.newArrayList(orderCode));
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void addSalesOrder(SalesOrder order, List<SalesOrderGoods> orderGoodsList) throws ServiceException {
        // 初始化订单状态、配送状态、通知状态、通知次数、接收时间、发票状态
        if ("2".equals(WmsConstants.WMS_COMPANY)) {
            order.setOrderStatus(OrderStatus.UNFILTER.getCode());
        } else {
            order.setOrderStatus(OrderStatus.FILTERED.getCode());
        }
        order.setOrderNotifyStatus(NotifyStatus.UNNOTIFIED.getCode());
        order.setOrderNotifyCount(0);
        order.setJoinTime(new Date());
        if (order.getPaymentTime() == null) {
            order.setPaymentTime(order.getOrderTime());
        }
        if (WmsConstants.ENABLED_TRUE == order.getInvoiceEnabled()) {
            order.setInvoiceStatus(WmsConstants.ENABLED_FALSE);
        }

        try {
            // 保存订单
            if (orderDao.addOrder(order) == 0) {
                throw new ServiceException(WmsCodeEnum.DUPLICATE_ORDER.getCode());
            }
            // 记录一张发票信息
            invoiceInfoSerivce.saveByOrder(order);
            // 记录订单节点信息
            salesOrderNodeInfoService.saveFromSalesOrder(order, orderGoodsList);
            // 保存操作日志
            saveOperateLog(order);


            // 保存订单商品清单
            Map<String, Sku> skuMap = getSkuMap(orderGoodsList);
            if (skuMap != null && skuMap.size() > 0) {
                convertSku(order, orderGoodsList, skuMap);
            }
            // 如果是来自唯品会订单，为了保证同步数据到数据库，需另行处理
            else if (WmsConstants.OrderSource.VIP_GIONEE.getCode().equals(orderGoodsList.get(0).getOrderSource())) {
                for (SalesOrderGoods orderGoods : orderGoodsList) {
                    orderGoods.setOrder(order);
                    orderGoods.setSkuId(-1L);
                    // orderGoods.setSkuCode(sku.getSkuCode());
                    orderGoods.setSkuName("-1");
                    orderGoods.setMeasureUnit("-1");
                    orderGoods.setIndivEnabled(-1);
                }

            }
            orderDao.batchAddOrderGoods(orderGoodsList);
        } catch (DataAccessException e) {
            throw new ServiceException(WmsCodeEnum.SYSTEM_ERROR.getCode(), e);
        }
    }

    private void saveOperateLog(SalesOrder order) {
        // 保存操作日志
        SalesOrderLog salesOrderLog = new SalesOrderLog();
        try {
            salesOrderLog.setOrderId(order.getId());
            salesOrderLog.setOrderStatus(order.getOrderStatus().intValue());
            salesOrderLog.setOpUser(ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
            salesOrderLog.setOpTime(new Date());
            salesOrderLog.setRemark("新增订单");
            salesOrderLogDao.insertSalesOrderLog(salesOrderLog);
        } catch (Exception e) {
            logger.error("业务日志记录异常", e);
        }
    }

    private void convertSku(SalesOrder order, List<SalesOrderGoods> orderGoodsList, Map<String, Sku> skuMap) {
        for (SalesOrderGoods orderGoods : orderGoodsList) {
            orderGoods.setOrder(order);
            if (skuMap.containsKey(orderGoods.getSkuCode())) {
                Sku sku = skuMap.get(orderGoods.getSkuCode());
                orderGoods.setSkuId(sku.getId());
                orderGoods.setSkuCode(sku.getSkuCode());
                orderGoods.setSkuName(sku.getSkuName());
                orderGoods.setMeasureUnit(sku.getWares().getMeasureUnit());
                orderGoods.setIndivEnabled(sku.getWares().getIndivEnabled());
                if (null == orderGoods.getSubtotalPrice()) {
                    orderGoods.setSubtotalPrice(new BigDecimal(orderGoods.getUnitPrice().floatValue() * orderGoods.getQuantity().intValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
                }
            }
        }
    }

    @Override
    public void updateSalesOrder(SalesOrder order, List<SalesOrderGoods> orderGoodsList) throws ServiceException {
        // 若订单状态只有未发货状态可以更新
        SalesOrder oldOrder = orderDao.queryOrderByOrderCode(order.getOrderCode());
        if (oldOrder == null) {
            throw new ServiceException(WmsCodeEnum.ORDER_NOT_EXIST.getCode());
        }
        if (!(oldOrder.getOrderStatus().intValue() == WmsConstants.OrderStatus.FILTERED.getCode() || oldOrder.getOrderStatus().intValue() == WmsConstants.OrderStatus.UNFILTER.getCode())) {
            throw new ServiceException(WmsCodeEnum.CURRENT_STATUS_FORBIDDEN_UPDATE.getCode());
        }

        if (WmsConstants.ENABLED_TRUE == order.getInvoiceEnabled()) {
            order.setInvoiceStatus(WmsConstants.ENABLED_FALSE);
        }

        if (order.getPaymentTime() == null) {
            order.setPaymentTime(order.getOrderTime());
        }

        try {
            // 更新订单
            if (orderDao.updateSalesOrder(order) == 0) {
                throw new ServiceException(WmsCodeEnum.ORDER_NOT_EXIST.getCode());
            }

            // 保存操作日志
            SalesOrderLog salesOrderLog = new SalesOrderLog();
            try {
                salesOrderLog.setOrderId(oldOrder.getId());
                salesOrderLog.setOrderStatus(oldOrder.getOrderStatus());
                salesOrderLog.setOpUser(ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
                salesOrderLog.setOpTime(new Date());
                salesOrderLog.setRemark("更新订单");
                salesOrderLogDao.insertSalesOrderLog(salesOrderLog);
            } catch (Exception e) {
                logger.error("业务日志记录异常", e);
            }

            if (null != orderGoodsList && orderGoodsList.size() > 0) {
                // 删除原有商品清单
                orderDao.deleteOrderGoodsList(oldOrder.getId());

                // 保存商品清单，调整新的库存占用信息
                Map<String, Sku> skuMap = getSkuMap(orderGoodsList);
                order.setId(oldOrder.getId());
                convertSku(order, orderGoodsList, skuMap);
                orderDao.batchAddOrderGoods(orderGoodsList);
                salesOrderNodeInfoService.updateFromSalesOrder(order, orderGoodsList); // 更新节点信息
            }
        } catch (DataAccessException e) {
            throw new ServiceException(WmsCodeEnum.SYSTEM_ERROR.getCode(), e);
        }
    }

    @Override
    public void cancelSalesOrder(String orderCode) throws ServiceException {
        SalesOrder order;
        try {
            order = orderDao.queryOrderByOrderCode(orderCode);
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
        if (order == null) {
            throw new ServiceException(WmsCodeEnum.ORDER_NOT_EXIST.getCode());
        }

        // 如果是已经推送过顺丰的订单，则采取如下取消方式
        if (order.getOrderPushStatus() != null && order.getOrderPushStatus().intValue() == OrderPushStatusEnum.PUSHED.getCode()) {
            // 更新订单状态和配送状态
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("orderCode", orderCode);
            criteria.put("orderStatus", OrderStatus.CANCELED.getCode());// 更新订单状态为已取消
            orderDao.updateOrder(criteria);

            invoiceInfoSerivce.cancelOrder(orderCode);

            // 保存操作日志
            SalesOrderLog salesOrderLog = new SalesOrderLog();
            try {
                salesOrderLog.setOrderId(order.getId());
                salesOrderLog.setOrderStatus(WmsConstants.OrderStatus.CANCELED.getCode());
                salesOrderLog.setOpUser(ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
                salesOrderLog.setOpTime(new Date());
                salesOrderLog.setRemark("更新订单为已取消状态，原来状态为：" + order.getOrderStatus());
                salesOrderLogDao.insertSalesOrderLog(salesOrderLog);
            } catch (Exception e) {
                logger.error("业务日志记录异常", e);
            }

            return;
        }

        // 除了已出库，和已取消，其他状态的订单均可取消
        if (OrderStatus.SHIPPED.getCode() != order.getOrderStatus() && OrderStatus.CANCELED.getCode() != order.getOrderStatus()) {
            try {
                // 更新订单状态和配送状态
                Map<String, Object> criteria = Maps.newHashMap();
                criteria.put("orderCode", orderCode);
                criteria.put("orderStatus", OrderStatus.CANCELED.getCode());// 更新订单状态为已取消
                orderDao.updateOrder(criteria);
                invoiceInfoSerivce.cancelOrder(orderCode);

                // 保存操作日志
                SalesOrderLog salesOrderLog = new SalesOrderLog();
                try {
                    salesOrderLog.setOrderId(order.getId());
                    salesOrderLog.setOrderStatus(WmsConstants.OrderStatus.CANCELED.getCode());
                    salesOrderLog.setOpUser(ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
                    salesOrderLog.setOpTime(new Date());
                    salesOrderLog.setRemark("更新订单为已取消状态");
                    salesOrderLogDao.insertSalesOrderLog(salesOrderLog);
                } catch (Exception e) {
                    logger.error("业务日志记录异常", e);
                }

                // 若订单状态为已配货、待出库，则应更改商品个体状态为在库
                if (OrderStatus.PICKED.getCode() == order.getOrderStatus() || OrderStatus.SHIPPING.getCode() == order.getOrderStatus()) {
                    Map<String, Object> params = Maps.newHashMap();
                    params.put("outId", order.getId());
                    params.put("stockStatus", WmsConstants.IndivStockStatus.IN_WAREHOUSE.getCode());
                    indivDao.updateIndivStatusByOutId(params);
                    // 释放销售订单占用库存(配货或者待出库的情况下)
                    Warehouse warehouse = warehouseService.getWarehouseByOrderSource(order.getOrderSource());
                    if (warehouse == null) {
                        throw new ServiceException("无仓库信息");
                    }
                    List<SalesOrderGoods> goodsList = getOrderGoodsListByOrderCode(orderCode);
                    for (SalesOrderGoods goods : goodsList) {
                        StockRequest stockRequest = new StockRequest(warehouse.getId(), goods.getSkuId(), StockType.STOCK_OCCUPY, StockType.STOCK_SALES, goods.getQuantity(), StockBizType.CONVERT_CANCEL_ORDER, goods.getOrder().getOrderCode());
                        stockService.convertStock(stockRequest);
                    }
                }
            } catch (DataAccessException e) {
                throw new ServiceException(WmsCodeEnum.SYSTEM_ERROR.getCode(), e);
            } catch (ServiceException e) {
                throw new ServiceException(WmsCodeEnum.STOCK_RELEASE_FAILED.getCode(), e);
            }
        } else if (OrderStatus.SHIPPED.getCode() == order.getOrderStatus()) {
            throw new ServiceException(WmsCodeEnum.ORDER_SHIPPED.getCode());
        } else {
            throw new ServiceException(WmsCodeEnum.NOT_ALLOWED_CANCEL.getCode());
        }
    }

    @Override
    public void updateSalesOrder(SalesOrder order) throws ServiceException {
        try {
            order.setOrderStatus(null);
            orderDao.batchUpdateOrder(Lists.newArrayList(order));
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    private Map<String, Sku> getSkuMap(List<SalesOrderGoods> goodsList) {
        Map<String, Sku> skuMap = Maps.newHashMap();
        Set<String> skuCodes = Sets.newHashSet();
        for (SalesOrderGoods orderGoods : goodsList) {
            skuCodes.add(orderGoods.getSkuCode());
        }
        List<Sku> skuList = null;
        // 如果是来自唯品会订单，则查询唯品69码与金立SKU码的映射关系
        if (WmsConstants.OrderSource.VIP_GIONEE.getCode().equals(goodsList.get(0).getOrderSource())) {
            List<SkuMap> skuMapList = skuMapService.findSkuMapsByOuterSkuCodes(Lists.newArrayList(skuCodes));
            if (CollectionUtils.isNotEmpty(skuMapList)) {
                List<String> skuCodeList = new ArrayList<String>();
                Map<String, String> skuCodeMap = new HashMap<String, String>();
                for (SkuMap sm : skuMapList) {
                    skuCodeList.add(sm.getSkuCode());
                    skuCodeMap.put(sm.getOuterSkuCode(), sm.getSkuCode());
                }
                for (SalesOrderGoods orderGoods : goodsList) {
                    if (skuCodeMap.containsKey(orderGoods.getSkuCode())) {
                        orderGoods.setSkuCode(skuCodeMap.get(orderGoods.getSkuCode()));
                    }
                }
                skuList = waresDao.querySkuListByCodes(skuCodeList);
                if (CollectionUtils.isNotEmpty(skuList)) {
                    for (Sku sku : skuList) {
                        skuMap.put(sku.getSkuCode(), sku);
                    }
                }
            }
        } else {
            skuList = waresDao.querySkuListByCodes(Lists.newArrayList(skuCodes));
            if (CollectionUtils.isEmpty(skuList) || skuList.size() != skuCodes.size()) {
                throw new ServiceException(WmsCodeEnum.SKU_NOT_EXISTS.getCode());
            }
            for (Sku sku : skuList) {
                skuMap.put(sku.getSkuCode(), sku);
            }
        }
        return skuMap;
    }

    @Override
    public List<SalesOrder> getSalesOrderListByIds(List<Long> orderIds) {
        // TODO Auto-generated method stub
        return orderDao.queryOrderListByIds(orderIds);
    }

    @Override
    public void updateShippingInfoList(List<SalesOrder> orderList) {
        // TODO Auto-generated method stub
        orderDao.updateShippingInfoList(orderList);
    }

    @Override
    public void setInvoiceStatus(List<Long> orderIds) {
        for (Long id : orderIds) {
            invoiceInfoSerivce.successZInvoice(String.valueOf(id));
        }
        orderDao.setInvoiceStatus(orderIds);
    }

    @Override
    public List<SalesOrderGoods> getOrderGoodsListByIds(String ids) throws ServiceException {
        try {
            return orderDao.queryGoodsListByOrderIds(ids.split(","));
        } catch (DataAccessException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<SalesOrder> getSalesOrderListByBatchId(Long batchId) {
        if (batchId == null) {
            throw new ServiceException("参数错误");
        }
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("batchId", batchId);
        return orderDao.queryOrderList(criteria);
    }

    @Override
    public List<SalesOrderVo> getSalesOrderListSearch(Map<String, Object> criteria) {
        // TODO Auto-generated method stub
        return orderDao.queryOrderListSearch(criteria);
    }

    @Override
    public SalesOrder getSalesOrderByDeliveryCode(String deliveryCode) {
        return orderDao.queryOrderByDeliveryCode(deliveryCode);
    }

    /**
     * 配货确认
     */
    @Override
    public void confirmPrepare(Long orderId, String shippingNo, String weight) {
        try {
            SalesOrder salesOrder = orderDao.queryOrder(orderId);
            if (salesOrder.getOrderStatus() != OrderStatus.PICKING.getCode()) { // 如果不是已配货的订单则拒绝任何操作
                return;
            }
            Warehouse warehouse = warehouseService.getWarehouseByOrderSource(salesOrder.getOrderSource());
            if (warehouse == null) {
                throw new ServiceException("未设置默认出货仓库");
            }

            Map<String, Object> criteria = Maps.newHashMap();
            List<SalesOrderGoods> goodsList = getOrderGoodsList(orderId);
            for (SalesOrderGoods goods : goodsList) {
                StockRequest stockRequest = new StockRequest(warehouse.getId(), goods.getSkuId(), StockType.STOCK_SALES, StockType.STOCK_OCCUPY, goods.getQuantity(), StockBizType.CONVERT_ORDER_OCCUPY, salesOrder.getOrderCode());
                stockService.convertStock(stockRequest);
            }
            List<Indiv> indivList = getIndivList(orderId);
            if (null != indivList && indivList.size() > 0) {
                // 将关联的商品个体更新为配货(出库)中状态，建立与发货单的关联关系
                criteria.put("outId", salesOrder.getId());
                criteria.put("stockStatus", WmsConstants.IndivStockStatus.STOCKOUT_HANDLING.getCode());

                criteria.put("orderId", orderId);
                criteria.put("orderCode", salesOrder.getOrderCode());
                criteria.put("indivCodes", indivList);
                indivDao.batchUpdateIndivsStock(criteria);
            }
            // 更新订单状态和配送状态，订单状态更新为已配货，前置条件为配货中
            criteria.clear();
            criteria = Maps.newHashMap();
            criteria.put("orderCode", salesOrder.getOrderCode());
            criteria.put("shippingNo", shippingNo);
            criteria.put("weight", weight);
            criteria.put("orderStatus", OrderStatus.PICKED.getCode());
            criteria.put("orderStatusWhere", OrderStatus.PICKING.getCode());
            orderDao.updateOrder(criteria);

            // 通知聚石塔发货
            salesOrder.setShippingNo(shippingNo);
            notifyTOP(Lists.newArrayList(salesOrder));
            // 保存操作日志
            SalesOrderLog salesOrderLog = new SalesOrderLog();
            try {
                salesOrderLog.setOrderId(salesOrder.getId());
                salesOrderLog.setOrderStatus(WmsConstants.OrderStatus.PICKED.getCode());
                salesOrderLog.setOpUser(ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
                salesOrderLog.setOpTime(new Date());
                salesOrderLog.setRemark("更新订单为已配货状态");
                salesOrderLogDao.insertSalesOrderLog(salesOrderLog);
            } catch (Exception e) {
                logger.error("业务日志记录异常", e);
            }

        } catch (Exception e) {
            throw new ServiceException(e.getMessage());
        }
    }

    @Override
    public SalesOrder getSalesOrderByShippingNo(Long shippingId, String shippingNo) {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("shippingId", shippingId);
        criteria.put("shippingNo", shippingNo);
        List<SalesOrder> list = orderDao.queryOrderList(criteria);
        if (list.size() == 0) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new ServiceException("数据异常，超过一条记录被找到");
        }
    }

    // 更新订单状态为已筛单
    @Override
    public void updateSalesOrderFilterStatus(String[] ids) {
        orderDao.updateSalesOrderFilterStatus(ids);
    }

    @Override
    public SalesOrder getSalesOrderByCode(String orderCode) {
        return orderDao.queryOrderByOrderCode(orderCode);
    }

    @Override
    public void notifyOrder(List<SalesOrder> orderList) {
        try {
            if (CollectionUtils.isNotEmpty(orderList)) {
                taskExecutor.execute(new ProcExtBizTask(orderList, null));
            }
        } catch (TaskRejectedException e) {
            logger.error("异步通知订单中心出错", e);
        }
    }

    @Override
    public void notifyOrder(ArrayList<SalesOrder> orderList, Map<String, Object> params) {
        try {
            if (CollectionUtils.isNotEmpty(orderList)) {
                taskExecutor.execute(new ProcExtBizTask(orderList, params));
            }
        } catch (TaskRejectedException e) {
            logger.error("异步通知订单中心出错", e);
        }
    }

    @Override
    public void updateSalesOrderStatus(Map<String, Object> params) {
        orderDao.updateSalesOrderStatus(params);
    }

    @Override
    public SalesOrder getSalesOrderByShipAndStatus(Integer orderStatus, String shippingNo) {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("orderStatus", orderStatus);
        criteria.put("shippingNo", shippingNo);
        List<SalesOrder> list = orderDao.queryOrderList(criteria);
        if (list.size() == 0) {
            return null;
        } else if (list.size() == 1) {
            return list.get(0);
        } else {
            throw new ServiceException("数据异常，超过一条记录被找到");
        }
    }

    @Override
    public List<SalesOrder> getSalesOrderList(Map<String, Object> criteria) {
        return orderDao.queryOrderList(criteria);
    }

    @Override
    public List<SalesOrderVo> getSalesOrderAndGoods(Map<String, Object> criteria) {
        return orderDao.queryOrderListAndGoods(criteria);
    }

    @Override
    public List<SalesOrderVo> getSalesOrderAndGoodsForStat(Map<String, Object> criteria) {
        return orderDao.queryOrderListAndGoodsForStat(criteria);
    }

    @Override
    public void notifyTOP(List<SalesOrder> orderList) {
        try {
            if (CollectionUtils.isNotEmpty(orderList)) {
                taskExecutor.execute(new TOPTask(orderList));
            }
        } catch (TaskRejectedException e) {
            logger.error("异步通知订单中心出错", e);
        }
    }

    @Override
    public void makeInvoice(String[] ids, String ipFrom) {
        try {
            if (ids != null && ids.length > 0) {
                taskExecutor.execute(new InvoiceTask(ids, ipFrom));
            }
        } catch (TaskRejectedException e) {
            logger.error("异步开票出错", e);
        }
    }

    private String getHeadInfo(SalesOrderVo order) {
        StringBuilder headInfo = new StringBuilder();
        headInfo.append(WmsConstants.INVOICE_TYPE);// 普通发票
        if (StringUtils.isBlank(order.getInvoiceTitle())) {
            headInfo.append(";").append(order.getConsignee());// 客户名称 最大长度100字节
        } else {
            headInfo.append(";").append(order.getInvoiceTitle());// 客户名称
            // 最大长度100字节
        }
        headInfo.append(";");// 客户税号 最大长度20字节，专用发票必须传入15、17、20位，普通发票发票可为空
        headInfo.append(";");// 客户地址电话 最大长度100字节
        headInfo.append(";");// 客户银行帐号 最大长度100字节
        headInfo.append(";").append(WmsConstants.INVOICE_SELLER_ADDRESS);// 销方地址电话
        // 最大长度100字节
        // 可为空有开票系统决定
        headInfo.append(";").append(WmsConstants.INVOICE_SELLER_ACCOUNT);// 销方银行帐号
        // 最大长度100字节
        // 可为空有开票系统决定
        headInfo.append(";").append(WmsConstants.INVOICE_RATE);// 税率
        // 整数可为17、11、6等税局规定的税率
        String markInfo = order.getOrderCode();// 备注信息（订单号+渠道来源）
        if (order.getOrderSource() == null || order.getOrderSource().equals(OrderSource.OFFICIAL_GIONEE.getCode())) {
            markInfo = markInfo + "  G";// 官网
        } else if (order.getOrderSource().equals(OrderSource.TMALL_GIONEE.getCode())) {
            markInfo = markInfo + "  T";// 天猫
        } else if (order.getOrderSource().equals(OrderSource.VIP_GIONEE.getCode())) {
            markInfo = markInfo + "  W";// 唯品会
        }

        if ("在线支付".equals(order.getPaymentName())) {
            markInfo = markInfo + "  OP";
        } else if ("货到付款".equals(order.getPaymentName())) {
            markInfo = markInfo + "  COD";
        } else if ("支付宝".equals(order.getPaymentName())) {
            markInfo = markInfo + "  ALIP";
        } else {
        }
        markInfo = markInfo + "  OT";

        for (SalesOrderGoods goods : order.getGoodsList()) {
            Sku sku = waresDao.querySku(goods.getSkuId());// 根据sku信息获取商品信息
            if (StringUtils.isNotEmpty(sku.getWares().getWaresRemark())) {
                markInfo = markInfo + " " + sku.getWares().getWaresRemark();
            }
        }

        headInfo.append(";").append(markInfo);// 备注 最大长度160字节
        headInfo.append(";").append(WmsConstants.INVOICE_MAKER);// 开票人 最大长度10字节
        headInfo.append(";").append(WmsConstants.INVOICE_COLLECT);// 收款人
        // 最大长度10字节
        headInfo.append(";").append(WmsConstants.INVOICE_CHECK);// 复核人 最大长度10字节
        headInfo.append(";").append(WmsConstants.INVOICE_SELLER_NO);// 税号
        return Base64.encodeBase64String(headInfo.toString().getBytes());
    }

    private String getLineListInfo(SalesOrderVo order) {
        StringBuilder lineListInfo = new StringBuilder();
        for (SalesOrderGoods goods : order.getGoodsList()) {
            if (goods.getUnitPrice().intValue() == 0 && goods.getSubtotalPrice().intValue() == 0) {
                continue;
            }
            if (goods.getIndivEnabled().equals(0)) {
                // lineListInfo.append("手机配件");// 商品名称 最大长度60字节
                lineListInfo.append(goods.getSkuName());// 商品名称 最大长度60字节
            } else {
                if (WmsConstants.WMS_COMPANY.equals("1")) {
                    // lineListInfo.append("GIONEE手机");// 商品名称 最大长度60字节
                    lineListInfo.append(goods.getSkuName());// 商品名称 最大长度60字节
                } else {
                    // lineListInfo.append("IUNI手机");// 商品名称 最大长度60字节
                    lineListInfo.append(goods.getSkuName());// 商品名称 最大长度60字节
                }
            }
            Sku sku = waresDao.querySku(goods.getSkuId());// 根据sku信息获取商品信息
            lineListInfo.append(";").append(sku.getWares().getWaresModel());// 规格型号
            // 最大长度30字节
            lineListInfo.append(";").append(goods.getMeasureUnit());// 计量单位
            // 最大长度16字节
            lineListInfo.append(";").append(goods.getQuantity());// 数量
            lineListInfo.append(";").append(goods.getUnitPrice());// 单价
            // 修改说明：系统出现商品明细，金额合计不对应。直接采用 单价*数量，重新计算
            // lineListInfo.append(";").append(goods.getSubtotalPrice());// 金额
            lineListInfo.append(";").append(goods.getUnitPrice().multiply(new BigDecimal(goods.getQuantity())).doubleValue());// 金额
            // 最小单位到分，小数点2位
            lineListInfo.append(";").append("0");// 税额 最小单位到分，小数点2位
            lineListInfo.append(";").append("1");// 含税价标志 0：不含税 1：含税
            // ，上面的单价和金额是否含税必须保持一致
            lineListInfo.append(";").append("17");
            lineListInfo.append(";").append("1.0");// 版本号
            lineListInfo.append(";").append("1090505010000000000");// 税收分类编码，统一用 1090505010000000000
            lineListInfo.append(";").append("0").append(";;;;");
            lineListInfo.append("\r\n");// 多行明细时增加换行符号
        }
        // 当发票金额与订单总金额不一致时，则添加折扣信息，折扣信息
        if (order.getInvoiceAmount() != null && order.getInvoiceAmount().intValue() != 0) {
            // 该写法用于取消计算折扣 date:2015-08-11 penbin
        } else {
            // order.setInvoiceAmount(order.getOrderAmount());
            if ((!order.getInvoiceAmount().equals(order.getOrderAmount())) && (order.getInvoiceAmount().doubleValue() < order.getOrderAmount().doubleValue())) {
                lineListInfo.append("折扣");// 商品名称 最大长度60字节
                lineListInfo.append(";");// 规格型号 最大长度30字节
                lineListInfo.append(";");// 计量单位 最大长度16字节
                lineListInfo.append(";0");// 数量
                lineListInfo.append(";0");// 单价
                // 计算金额和税额（金额=折扣/1.17）税额=（折扣-金额）
                BigDecimal zk = order.getInvoiceAmount().subtract(order.getOrderAmount());
                BigDecimal je = zk.divide(new BigDecimal("1.17"), 2, BigDecimal.ROUND_DOWN);
                BigDecimal se = zk.subtract(je);
                lineListInfo.append(";").append(je.doubleValue());// 金额
                // 最小单位到分，小数点2位
                lineListInfo.append(";").append(se.doubleValue());// 税额
                // 最小单位到分，小数点2位
                lineListInfo.append(";").append("0");// 含税价标志 0：不含税 1：含税
                // ，上面的单价和金额是否含税必须保持一致
            } else if ((!order.getInvoiceAmount().equals(order.getGoodsAmount())) && (order.getInvoiceAmount().doubleValue() > order.getGoodsAmount().doubleValue())) {
                lineListInfo.append("其他");// 商品名称 最大长度60字节
                lineListInfo.append(";");// 规格型号 最大长度30字节
                lineListInfo.append(";无");// 计量单位 最大长度16字节
                lineListInfo.append(";0");// 数量
                lineListInfo.append(";0");// 单价
                // 计算金额和税额（金额=折扣/1.17）税额=（折扣-金额）
                // BigDecimal zk = order.getInvoiceAmount().subtract(order.getOrderAmount()); //修改getOrderAmount ->getGoodsAmount
                BigDecimal zk = order.getInvoiceAmount().subtract(order.getGoodsAmount());
                BigDecimal je = zk.divide(new BigDecimal("1.17"), 2, BigDecimal.ROUND_DOWN);
                BigDecimal se = zk.subtract(je);
                lineListInfo.append(";").append(je.doubleValue());// 金额
                // 最小单位到分，小数点2位
                lineListInfo.append(";").append(se.doubleValue());// 税额
                // 最小单位到分，小数点2位
                lineListInfo.append(";").append("0");// 含税价标志 0：不含税 1：含税
                // ，上面的单价和金额是否含税必须保持一致
            } else if (order.getGoodsAmount().doubleValue() > order.getInvoiceAmount().doubleValue()) {
                lineListInfo.append("折扣");// 商品名称 最大长度60字节
                lineListInfo.append(";");// 规格型号 最大长度30字节
                lineListInfo.append(";");// 计量单位 最大长度16字节
                lineListInfo.append(";0");// 数量
                lineListInfo.append(";0");// 单价
                // 计算金额和税额（金额=折扣/1.17）税额=（折扣-金额）
                BigDecimal zk = order.getInvoiceAmount().subtract(order.getGoodsAmount());
                BigDecimal je = zk.divide(new BigDecimal("1.17"), 2, BigDecimal.ROUND_DOWN);
                BigDecimal se = zk.subtract(je);
                lineListInfo.append(";").append(je.doubleValue());// 金额
                // 最小单位到分，小数点2位
                lineListInfo.append(";").append(se.doubleValue());// 税额
                // 最小单位到分，小数点2位
                lineListInfo.append(";").append("0");// 含税价标志 0：不含税 1：含税
                // ，上面的单价和金额是否含税必须保持一致
            }
        }
        return Base64.encodeBase64String(lineListInfo.toString().getBytes());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handBatch(Map<String, Long> batchCodeMap) {
        // 若所有订单发票打印成功，则修改拣货批次状态为已处理，根据拣货批次号来获取判断
        for (Map.Entry<String, Long> entry : batchCodeMap.entrySet()) {
            String batchCode = entry.getKey().toString();
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("batchCode", batchCode);
            List<SalesOrderVo> batchOrders = getSalesOrderAndGoods(criteria);
            if (batchOrders != null) {
                int handledNum = 0;// 记录已处理发票的订单数目
                for (SalesOrderVo orderVo : batchOrders) {
                    if (orderVo.getInvoiceStatus().equals(1)) {
                        handledNum += 1;
                    }
                }
                if (handledNum == batchOrders.size()) {
                    // 更新批次处理状态为已处理
                    DeliveryBatch deliveryBatch = new DeliveryBatch();
                    deliveryBatch.setHandlingStatus(DeliveryBatchStatus.FINISHED.getCode());
                    deliveryBatch.setHandledTime(new Date());
                    deliveryBatch.setHandledBy(ActionUtils.getLoginName());
                    deliveryBatch.setId(entry.getValue());
                    deliveryBatchDao.updateDeliveryBatch(deliveryBatch);
                }
            }
        }
    }

    @Override
    public String queryLastSyncOrderTime(Map<String, Object> paramsMap) throws ServiceException {
        return orderDao.queryLastSyncOrderTime(paramsMap);
    }

    /**
     * 按照物流公司和订单状态分组查询
     */
    @Override
    public List<OrderStatusStatVo> queryOrderStatusAndCountGroupByShippingName(Map<String, Object> paramsMap) throws ServiceException {
        List<OrderStatusVo> list = orderDao.queryOrderStatusAndCountGroupByShippingName(paramsMap);
        if (CollectionUtils.isEmpty(list)) {
            List<Shipping> shippings = shippingService.getValidShippings();
            List<OrderStatusStatVo> resultList = new ArrayList<OrderStatusStatVo>();
            for (Shipping shipping : shippings) {
                resultList.add(createOrderStatusStatVo(shipping.getId(), shipping.getShippingName()));
            }
            resultList.add(createOrderStatusStatVo(null, "合计"));
            return resultList;
        }
        Map<String, OrderStatusStatVo> map = new HashMap<String, OrderStatusStatVo>();
        // 合计
        OrderStatusStatVo total = new OrderStatusStatVo();
        total.setShippingName("合计");
        for (OrderStatusVo orderStatusVo : list) {
            if (map.isEmpty() || !map.containsKey(orderStatusVo.getShippingName())) {
                OrderStatusStatVo ossVo = new OrderStatusStatVo();
                ossVo.setShippingId(orderStatusVo.getShippingId());
                ossVo.setShippingName(orderStatusVo.getShippingName());
                ossVo.setOrderCount(orderStatusVo);
                map.put(orderStatusVo.getShippingName(), ossVo);
            } else {
                OrderStatusStatVo ossVo = map.get(orderStatusVo.getShippingName());
                ossVo.setShippingId(orderStatusVo.getShippingId());
                ossVo.setShippingName(orderStatusVo.getShippingName());
                ossVo.setOrderCount(orderStatusVo);

            }
            if (orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.CANCELED.getCode()) {
                total.setCanceledCount(total.getCanceledCount() + orderStatusVo.getOrderCount());
            } else if (orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.FILTERED.getCode()) {
                total.setFilteredCount(total.getFilteredCount() + orderStatusVo.getOrderCount());
            } else if (orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.SHIPPED.getCode()) {
                total.setShippedCount(total.getShippedCount() + orderStatusVo.getOrderCount());
            } else if (orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.PRINTED.getCode()) {
                total.setPrintedCount(total.getPrintedCount() + orderStatusVo.getOrderCount());
            } else if (orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.PICKED.getCode()) {
                total.setPickedCount(total.getPickedCount() + orderStatusVo.getOrderCount());
            } else if (orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.BACKED.getCode()) {
                total.setBackedCount(total.getBackedCount() + orderStatusVo.getOrderCount());
            } else if (orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.RECEIVED.getCode()) {
                total.setReceivedCount(total.getReceivedCount() + orderStatusVo.getOrderCount());
            } else if (orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.BACKING.getCode()) {
                total.setBackingCount(total.getBackingCount() + orderStatusVo.getOrderCount());
            } else if (orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.REFUSEING.getCode()) {
                total.setRefuseingCount(total.getRefuseingCount() + orderStatusVo.getOrderCount());
            } else if (orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.PICKING.getCode()) {
                total.setPickingCount(total.getPickingCount() + orderStatusVo.getOrderCount());
            } else if (orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.SHIPPING.getCode()) {
                total.setShippingCount(total.getShippingCount() + orderStatusVo.getOrderCount());
            } else if (orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.REFUSED.getCode()) {
                total.setRefusedCount(total.getRefusedCount() + orderStatusVo.getOrderCount());
            }
        }
        List<OrderStatusStatVo> results = Lists.newArrayList(map.values());
        List<Shipping> shippings = shippingService.getValidShippings();
        for (Shipping shipping : shippings) {
            boolean isFound = false;
            for (OrderStatusStatVo orderStatusStatVo : results) {
                if (shipping.getId().longValue() == orderStatusStatVo.getShippingId().longValue()) {
                    isFound = true;
                    continue;
                }
            }
            if (!isFound) {
                results.add(createOrderStatusStatVo(shipping.getId(), shipping.getShippingName()));
            }
        }
        results.add(total);
        return results;
    }

    public OrderStatusStatVo createOrderStatusStatVo(Long shippingId, String shippingName) {
        OrderStatusStatVo ossVo = new OrderStatusStatVo();
        ossVo.setShippingId(shippingId);
        ossVo.setShippingName(shippingName);
        return ossVo;
    }

    @Override
    public void updateSalesOrderGoods(SalesOrderGoods salesOrderGoods) {
        orderDao.updateOrderGoods(salesOrderGoods);
    }

    @Override
    public void addIndiv(Long orderId, String orderCode, Long indivId) {
        Map<String, Object> params = Maps.newHashMap();
        params.put("id", indivId);
        params.put("prepareId", orderId);
        params.put("prepareCode", orderCode);
        params.put("stockStatus", WmsConstants.IndivStockStatus.STOCKOUT_HANDLING.getCode());
        indivDao.updateIndivPrepare(params);
    }

    @Override
    public List<Indiv> getIndivList(Long orderId) {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("prepareId", orderId);
        return indivDao.queryIndivList(criteria);
    }

    @Override
    public SalesOrderGoods getSalesOrderGoods(Long id) {
        return orderDao.queryGoodsById(id);
    }

    @Override
    public void updateSalesOrderStatus(List<SalesOrder> orders, int destStatus, int srcStatus) {
        Map<String, Object> params = Maps.newHashMap();
        for (SalesOrder order : orders) {
            params.clear();
            params.put("orderCode", order.getOrderCode());
            params.put("orderStatus", destStatus);
            params.put("orderStatusWhere", srcStatus);
            orderDao.updateSalesOrderStatus(params);
            // 保存操作日志
            SalesOrderLog salesOrderLog = new SalesOrderLog();
            try {
                salesOrderLog.setOrderId(order.getId());
                salesOrderLog.setOrderStatus(WmsConstants.OrderStatus.FILTERED.getCode());
                salesOrderLog.setOpUser(WmsConstants.DEFAULT_USERNAME_LOG);
                salesOrderLog.setOpTime(new Date());
                salesOrderLog.setRemark("地址筛选");
                salesOrderLogDao.insertSalesOrderLog(salesOrderLog);
            } catch (Exception e) {
                logger.error("业务日志记录异常", e);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int updateSalesOrderPushStatus(SalesOrder order) {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("orderCode", order.getOrderCode());
        criteria.put("orderPushStatus", order.getOrderPushStatus());
        criteria.put("orderPushTime", new Date());

        return orderDao.updateOrder(criteria);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public ServiceCtrlMessage copy(Long id) {
        SalesOrder salesOrder = getSalesOrder(id);

        SystemConfig systemConfig = configService.getByKey("ORDER_COPY_CHECK"); // 订单复制校验，源订单是否是取消状态
        if (systemConfig != null && org.apache.commons.lang3.BooleanUtils.toBoolean(systemConfig.getValue())) {
            if (salesOrder.getOrderStatus().intValue() != OrderStatus.CANCELED.getCode()) {
                return new ServiceCtrlMessage(false, "源订单未取消，无法复制！");
            }
        }

        String orderCode = salesOrder.getOrderCode();

        int seq = 1; // 序号
        String orderCodePrefix = orderCode; // 前缀
        if (orderCode.indexOf("-") > -1) {
            seq = Integer.parseInt(orderCode.substring(orderCode.indexOf("-") + 1)) + 1;
            orderCodePrefix = orderCode.substring(0, orderCode.indexOf("-"));
        }

        String newOrderCode = null;
        // 最多查询10次
        for (int i = 0; i < 10; i++, seq++) {
            newOrderCode = orderCodePrefix + "-" + seq;
            if (getSalesOrderByCode(newOrderCode) == null) {
                break;
            }
        }

        salesOrder.setId(null);
        salesOrder.setOrderCode(newOrderCode);
        salesOrder.setOrderStatus(OrderStatus.FILTERED.getCode()); // 已筛单状态
        salesOrder.setOrderNotifyStatus(NotifyStatus.UNNOTIFIED.getCode()); // 未通知
        salesOrder.setOrderNotifyCount(0); //
        salesOrder.setDeliveryCode(newOrderCode + "01");
        if (WmsConstants.ENABLED_TRUE == salesOrder.getInvoiceEnabled()) {
            salesOrder.setInvoiceStatus(WmsConstants.ENABLED_FALSE);
        }
        salesOrder.setOrderPushStatus(OrderPushStatusEnum.UN_PUSHED.getCode());
        salesOrder.setShippingNo(null); // 配送单号
        orderDao.addOrder(salesOrder);

        List<SalesOrderGoods> goodsList = getOrderGoodsListByOrderCode(orderCode);
        for (SalesOrderGoods g : goodsList) {
            g.setId(null);
            g.setOrder(salesOrder); // 关联新的订单
        }
        orderDao.batchAddOrderGoods(goodsList);

        // 记录一张发票信息
        InvoiceInfo oldInvoiceInfo = invoiceInfoSerivce.get(orderCode);
        salesOrder.setInvoiceMobile(oldInvoiceInfo.getMobile());
        salesOrder.setInvoiceEmail(oldInvoiceInfo.getEmail());
        invoiceInfoSerivce.saveByOrder(salesOrder);

        salesOrderNodeInfoService.saveFromSalesOrder(salesOrder, goodsList);

        // 保存操作日志
        SalesOrderLog salesOrderLog = new SalesOrderLog();
        salesOrderLog.setOrderId(salesOrder.getId());
        salesOrderLog.setOrderStatus(salesOrder.getOrderStatus().intValue());
        salesOrderLog.setOpUser(ActionUtils.getLoginName() == null ? WmsConstants.DEFAULT_USERNAME_LOG : ActionUtils.getLoginName());
        salesOrderLog.setOpTime(new Date());
        salesOrderLog.setRemark("一键复制订单");
        salesOrderLogDao.insertSalesOrderLog(salesOrderLog);

        return new ServiceCtrlMessage(true, "操作成功！");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceCtrlMessage pushOrderToSF(SalesOrder order) {
        if (OrderStatus.FILTERED.getCode() != order.getOrderStatus()) {
            return new ServiceCtrlMessage(false, "只有已筛单的订单才能推送！");
        }
        if (!ShippingEnum.SF.getName().equals(order.getShippingName())) {
            return new ServiceCtrlMessage(false, "配送方式非顺丰速运，不能推送！");
        }
        if (order.getOrderPushStatus() != null && OrderPushStatusEnum.PUSHED.getCode() == order.getOrderPushStatus()) {
            return new ServiceCtrlMessage(false, "该订单已经推送过，不能再次推送！");
        }

        String donotPushSfSkus = StringUtils.trimToEmpty(configService.getByKey("ORDER_DONOT_PUSH_SF_SKUS").getValue());

        List<SalesOrderGoods> orderGoodsList = getOrderGoodsList(order.getId()); // 订单商品列表
        if (OrderSource.TMALL_GIONEE.getCode().equals(order.getOrderSource())) {
            for (SalesOrderGoods g : orderGoodsList) {
                if (donotPushSfSkus.contains(g.getSkuCode())) {
                    return new ServiceCtrlMessage(false, "(SKU：" + g.getSkuCode() + ")订单不能推送到顺丰！");
                }
            }
        }

        SystemConfig config = configService.getByKey(WmsConstants.ORDER_PUSH_CHECK_STOCK);
        if (config != null && BooleanUtils.toBoolean(config.getValue())) {
            List<Warehouse> warehouseList = warehouseService.getValidWarehouses();

            boolean hasStock = false;
            for (Warehouse warehouse : warehouseList) {
                // 非东莞仓库的顺丰仓，都有库存的时候才可以推送
                if (!WarehouseCodeEnum.DONG_GUAN_WAREHOUSE.getCode().equals(warehouse.getWarehouseCode()) && checkOrderRealTimeInventoryBalance(order, orderGoodsList, warehouse.getWarehouseCode())) {
                    hasStock = true;
                    break;
                }
            }
            if (!hasStock) {
                return new ServiceCtrlMessage(false, "库存不足，推送失败！");
            }
        }

        ServiceCtrlMessage msg = pushToSF(order, orderGoodsList);
        if (msg.isResult()) {
            return new ServiceCtrlMessage(true, "推送成功！");
        } else {
            return new ServiceCtrlMessage(false, msg.getMessage());
        }
    }

    /**
     * 检查订单在顺丰仓的实时库存，是否只是推送到顺丰
     *
     * @param order order
     * @return true:可以推送
     */
    private boolean checkOrderRealTimeInventoryBalance(SalesOrder order, List<SalesOrderGoods> orderGoodsList, String warehouse) {
        try {
            List<String> skuList = Lists.newArrayList();
            for (SalesOrderGoods g : orderGoodsList) {
                skuList.add(g.getSkuCode());
            }

            Map<String, String> skuMap = Maps.newHashMap();
            List<SkuMap> skuMapList = skuMapService.querySkuMapBySkuCodes(skuList, SkuMapOuterCodeEnum.SF.getCode());
            if (CollectionUtils.isEmpty(skuMapList) || skuMapList.size() < orderGoodsList.size()) {
                logger.info(MessageFormat.format("订单号为：{0}，的订单存在没映射的SKU！", order.getOrderCode()));
                return false;
            }
            for (SkuMap s : skuMapList) {
                skuMap.put(s.getOuterSkuCode(), s.getSkuCode());
            }

            WmsRealTimeInventoryBalanceQueryRequest request = new WmsRealTimeInventoryBalanceQueryRequest();
            request.setCompany(WmsConstants.SF_COMPANY); // 货主
            request.setWarehouse(warehouse); // 仓库
            request.setInventory_sts(InventoryStatusEnum.ZHENGPIN.getCode());
            request.setItemList(new ArrayList<>(skuMap.keySet()));
            WmsRealTimeInventoryBalanceQueryResponse response = sfWebService.outsideToLscmService(WmsRealTimeInventoryBalanceQueryRequest.class, WmsRealTimeInventoryBalanceQueryResponse.class, request);
            if (response == null || CollectionUtils.isEmpty(response.getList())) {
                logger.info(MessageFormat.format("订单号为：{0}，的订单在顺丰仓没有库存！", order.getOrderCode()));
                return false;
            }

            for (SalesOrderGoods g : orderGoodsList) {
                boolean existsInv = false; // 是否存在库存
                for (WmsRealTimeInventoryBalanceQueryResponseItem i : response.getList()) {
                    if (g.getSkuCode().equals(skuMap.get(i.getSku_no())) && i.getAvailable_stock() != null && i.getAvailable_stock() >= g.getQuantity()) { // 如果存在顺丰库存，并且顺丰库存量大于等于订单购买量
                        existsInv = true;
                        break;
                    }
                }

                if (!existsInv) { // 该商品在顺丰仓没有库存或者库存不足，不能发往顺丰
                    logger.info(MessageFormat.format("商品：{0},{1}，在顺丰仓没有库存！", g.getSkuCode(), g.getSkuName()));
                    return false;
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("检查实时库存时候出现异常！", e);
            return false;
        }
    }

    /**
     * 检查订单在顺丰仓的非实时库存，是否只是推送到顺丰
     *
     * @param order order
     * @return true:可以推送
     */
    private boolean checkOrderInventoryBalance(SalesOrder order, List<SalesOrderGoods> orderGoodsList) {
        try {
            boolean existsInv = false; // 是否存在库存
            for (SalesOrderGoods g : orderGoodsList) {
                existsInv = false;
                SkuMap skuMap = skuMapService.getSkuMapBySkuCode(g.getSkuCode(), SkuMapOuterCodeEnum.SF.getCode());
                if (skuMap == null) {
                    logger.info(MessageFormat.format("SKU:{0}，没有对应是顺丰SKU", g.getSkuCode()));
                    return false;
                }

                WmsInventoryBalancePageQueryRequest request = new WmsInventoryBalancePageQueryRequest();
                request.setCompany(WmsConstants.SF_COMPANY); // 货主
                request.setWarehouse(WmsConstants.SF_WAREHOUSE); // 仓库
                request.setItem(skuMap.getOuterSkuCode());

                WmsInventoryBalancePageQueryResponse response = sfWebService.outsideToLscmService(WmsInventoryBalancePageQueryRequest.class, WmsInventoryBalancePageQueryResponse.class, request);
                if (response == null || CollectionUtils.isEmpty(response.getList())) {
                    logger.info(MessageFormat.format("订单号为：{0}，的订单在顺丰仓没有库存！", order.getOrderCode()));
                    return false;
                }

                WmsInventoryBalancePageQueryResponseItem item = response.getList().get(0); // 默认取第一个商品
                if (g.getQuantity() <= item.getOn_hand_qty()) {
                    existsInv = true;
                } else {
                    logger.info(MessageFormat.format("商品：{0},{1}，在顺丰仓没有库存！", g.getSkuCode(), g.getSkuName()));
                    return false;
                }
            }

            return existsInv;

        } catch (Exception e) {
            e.printStackTrace();
            logger.error("检查库存时候出现异常！", e);
            return false;
        }
    }

    /**
     * 发往顺丰
     *
     * @param order
     * @param orderGoodsList
     */
    private ServiceCtrlMessage pushToSF(SalesOrder order, List<SalesOrderGoods> orderGoodsList) {
        try {
            WmsSailOrderRequestHeader header = new WmsSailOrderRequestHeader();
            header.setCompany(WmsConstants.SF_COMPANY); // 货主
            // header.setWarehouse(WmsConstants.SF_WAREHOUSE); // 仓库 Note:2017-04-19 启用分仓后，不再指定仓库发货
            header.setShop_name(order.getOrderSource()); // 商家店铺名称
            header.setErp_order(order.getOrderCode()); // 订单号码
            header.setOrder_type(OutOrderTypeEnum.SALE_ORDER.getCode()); // 订单类型
            header.setOrder_date(DateUtils.formatDate(order.getOrderTime(), "yyyy-MM-dd HH:mm:ss"));
            header.setShip_to_name(order.getConsignee());
            header.setShip_to_attention_to(order.getConsignee()); // 收货人姓名
            header.setShip_to_country(null); // 默认为中国
            header.setShip_to_province(order.getProvince()); // 收货人的省份
            header.setShip_to_city(order.getCity()); // 收货人的城市
            header.setShip_to_area(order.getDistrict()); // 收货人的地区
            header.setShip_to_address(order.getAddress()); // 收货人的详细地址
            header.setShip_to_postal_code(order.getZipcode()); // 邮编
            header.setShip_to_phone_num(order.getMobile()); // 收货手机
            header.setShip_to_tel_num(order.getTel()); // 收货电话
            header.setCarrier("顺丰速运");
            header.setCarrier_service("顺丰隔日");
            Integer paymentType = order.getPaymentType(); // 支付类型
            if (paymentType != null && PaymentType.COD.getCode() == paymentType) { // 线下支付
                // header.setPayment_of_charge(order.getPaymentName());// 支付方式名称
                if (order.getPayableAmount() != null && order.getPayableAmount().doubleValue() > 0) {
                    header.setCod("Y"); // 线下支付 Y
                    header.setAmount(order.getPayableAmount().doubleValue() + ""); // 金额
                }
            }
            // header.setDelivery_date(DateUtils.formatDate(order.getShippingTime(), "yyyy-MM-dd HH:mm:ss")); // 发货时间

            if (order.getInvoiceEnabled() != null && order.getInvoiceEnabled().intValue() == 1 && order.getInvoiceAmount().doubleValue() > 0) { // 需要发票的时候
                // header.setInvoice("N"); // 是否需要发票
                header.setInvoice("N"); // 是否需要发票
                header.setInvoice_title("系统"); // 发票抬头
                header.setInvoice_content("手机"); // 发票内容
                header.setInvoice_type(com.gionee.wms.common.WmsConstants.InvoiceType.PLAIN.getName());
                // // 发票类型

                // 添加收款人、复合人、开票人
                // header.setUser_def1(WmsConstants.INVOICE_COLLECT); // 收款人
                // header.setUser_def3(WmsConstants.INVOICE_MAKER); // 开票人
                // header.setUser_def2(WmsConstants.INVOICE_CHECK); // 复合人

            } else {
                header.setInvoice("N"); // 是否需要发票
            }
            header.setOrder_note(order.getPostscript()); // 订单备注
            header.setOrder_total_amount(order.getOrderAmount().doubleValue() + "");// 订单总金额
            header.setActual_amount(order.getOrderAmount().doubleValue() + ""); // 订单总金额
            //header.setMonthly_account(WmsConstants.SF_MONTHLY_ACCOUNT); // 月结账号 // NOTE:启用分仓后，将不再使用月结账号
            // add 2015.10.19 pengbin //
            if (order.getOrderAmount().doubleValue() > 100) { // 大于100金额时保价
                header.setValue_insured("Y"); // 保价
                header.setDeclared_value(order.getOrderAmount().toString()); // 保价金额，取发票金额
            }
            // add end //

            WmsSailOrderRequest request = new WmsSailOrderRequest();
            request.setHeader(header);

            List<WwmsSailOrderRequestItem> detailList = Lists.newArrayList();
            request.setDetailList(detailList);
            for (int i = 0; i < orderGoodsList.size(); i++) {
                SalesOrderGoods g = orderGoodsList.get(i);
                // SKU 转成顺丰SKU
                SkuMap skuMap = skuMapService.getSkuMapBySkuCode(g.getSkuCode(), SkuMapOuterCodeEnum.SF.getCode());
                if (skuMap == null) {
                    logger.info(MessageFormat.format("sku:{0},对应顺丰侧映射不存在！", g.getSkuCode()));
                    return new ServiceCtrlMessage(false, MessageFormat.format("sku:{0},对应顺丰侧映射不存在！", g.getSkuCode()));
                }
                WwmsSailOrderRequestItem item = new WwmsSailOrderRequestItem();
                item.setErp_order_line_num(String.valueOf(i)); // 行号 TODO 行号
                item.setItem(skuMap.getOuterSkuCode()); // SKU
                // item.setItem("GIONEESF1049"); // SKU
                item.setItem_name(g.getSkuName()); // 商品名称
                item.setUom(g.getMeasureUnit()); // 单位
                item.setItem_price(g.getUnitPrice().doubleValue() + ""); // 价格
                item.setQty(g.getQuantity().toString()); // 数量

                detailList.add(item);
            }

            WmsSailOrderResponse response = sfWebService.outsideToLscmService(WmsSailOrderRequest.class, WmsSailOrderResponse.class, request);
            if (response == null) {
                logger.info("调用“出库单（销售订单）接口”出现异常，未返回数据！");
                return new ServiceCtrlMessage(false, "调用“出库单（销售订单）接口”出现异常，未返回数据！");
            }
            if (response.getResult()) {
                logger.info(MessageFormat.format("订单号：{0} ，推送成功，返回ERP订单号：{1}！", order.getOrderCode(), response.getOrderid()));

                order.setOrderPushStatus(OrderPushStatusEnum.PUSHED.getCode());
                updateSalesOrderPushStatus(order); // 修改订单推送状态

                // 修改订单状态
                Map<String, Object> params = Maps.newHashMap();
                params.put("orderStatus", OrderStatus.FILTERED.getCode());
                params.put("orderCode", order.getOrderCode());
                updateSalesOrderStatus(params);

                String orderid = response.getOrderid();
                salesOrderMapService.add(new SalesOrderMap(order.getOrderCode(), orderid)); // 记录订单是顺丰ERP订单号的关联，用于在回调时候确认关系 @see com.gionee.wms.web.servlet.DockSFServlet
                salesOrderNodeInfoService.updatePushTime(order.getOrderCode(), new Date()); // 记录订单推送时间

                return new ServiceCtrlMessage(true, MessageFormat.format("订单号：{0} ，推送成功，返回ERP订单号：{1}！", order.getOrderCode(), response.getOrderid()));
            } else {

                logger.info(MessageFormat.format("调用“出库单（销售订单）接口”失败，原因如下：{0}{1}", SystemUtils.LINE_SEPARATOR, response.getRemark()));
                return new ServiceCtrlMessage(false, MessageFormat.format("调用“出库单（销售订单）接口”失败，原因如下：{0}", response.getRemark()));
            }
        } catch (Exception e) {
            logger.error("订单推送到顺丰出现异常！", e);
            e.printStackTrace();
            return new ServiceCtrlMessage(false, "订单推送到顺丰出现异常！" + e.getMessage());
        }

    }

    @Override
    public WmsRealTimeInventoryBalanceQueryResponse queryRealTimeInventoryBalance(String sku) {

        return null;
    }

    @Override
    public List<Map<String, Object>> getPrintInvoiceData(String[] ids) {
        List<SalesOrderVo> orderList = queryGoodsListByOrderIds(ids);
        List<Map<String, Object>> list = Lists.newArrayList();
        for (SalesOrderVo order : orderList) {
            Map<String, Object> map = Maps.newHashMap();
            map.put("ID", order.getOrderCode());
            map.put("HeadInfo", getHeadInfo(order));
            map.put("LineListInfo", getLineListInfo(order));
            map.put("PrintFlag", "1");
            map.put("orderId", order.getId());
            map.put("batchCode", order.getBatchCode());
            map.put("batchId", order.getBatchId());
            list.add(map);
        }
        return list;
    }

    /**
     * 异步通知外部系统订单情况
     */
    private class ProcExtBizTask implements Runnable {
        private List<SalesOrder> orders;
        private Map<String, Object> params;

        public ProcExtBizTask(List<SalesOrder> orders, Map<String, Object> params) {
            this.orders = orders;
            this.params = params;
        }

        @Override
        public void run() {
            for (SalesOrder order : orders) {
                if (org.apache.commons.lang.StringUtils.isBlank(order.getOrderSource()) || order.getOrderSource().equals(OrderSource.OFFICIAL_GIONEE.getCode())) {
                    // 金立官网
                    Map<String, String> parameters = Maps.newHashMap();
                    parameters.put("orderSn", order.getOrderCode());
                    if (OrderStatus.BACKED.getCode() == order.getOrderStatus()) {
                        // 远程通知订单中心已退货
                        parameters.put("orderStatus", RemoteOrderStatus.REFUNDING.getCode());
                        parameters.put("validCode", DigestUtils.md5Hex(order.getOrderCode() + RemoteOrderStatus.REFUNDING.getCode() + WmsConstants.ECSHOP_SALT));
                        // 查询该订单下的退货商品

                        parameters.put("goods", "");
                        parameters.put("remark", "");
                    } else if (OrderStatus.SHIPPED.getCode() == order.getOrderStatus()) {
                        // 远程通知订单中心已发货
                        String shippingCode = shippingService.getShipping(order.getShippingId()).getShippingCode();
                        String shippingSn = order.getShippingNo();
                        parameters.put("orderStatus", RemoteOrderStatus.SENDING.getCode());
                        parameters.put("shippingCode", shippingCode);
                        parameters.put("shippingSn", shippingSn);
                        parameters.put("validCode", DigestUtils.md5Hex(order.getOrderCode() + RemoteOrderStatus.SENDING.getCode() + shippingCode + shippingSn + WmsConstants.ECSHOP_SALT));
                    } else if (OrderStatus.PRINTED.getCode() == order.getOrderStatus()) {
                        // 远程通知订单中心已打单
                        parameters.put("orderStatus", RemoteOrderStatus.PREPARING.getCode());
                        parameters.put("validCode", DigestUtils.md5Hex(order.getOrderCode() + RemoteOrderStatus.PREPARING.getCode() + WmsConstants.ECSHOP_SALT));
                    } else if (OrderStatus.RECEIVED.getCode() == order.getOrderStatus()) {
                        // 远程通知订单中心已签收
                        parameters.put("orderStatus", RemoteOrderStatus.SHIPPED.getCode());
                        parameters.put("validCode", DigestUtils.md5Hex(order.getOrderCode() + RemoteOrderStatus.SHIPPED.getCode() + WmsConstants.ECSHOP_SALT));
                    } else if (OrderStatus.REFUSED.getCode() == order.getOrderStatus()) {
                        // 远程通知订单中心已拒收
                        parameters.put("orderStatus", RemoteOrderStatus.REFUSED.getCode());
                        parameters.put("validCode", DigestUtils.md5Hex(order.getOrderCode() + RemoteOrderStatus.REFUSED.getCode() + WmsConstants.ECSHOP_SALT));
                    }
                    try {
                        orderCenterClient.notifyOrder(order, parameters);
                        order.setOrderNotifyStatus(NotifyStatus.NOTIFIED_SUCCESS.getCode());
                    } catch (Exception e) {
                        order.setOrderNotifyStatus(NotifyStatus.NOTIFIED_FAIL.getCode());
                    }
                    // 更新本次通知结果
                    order.setOrderNotifyTime(new Date());
                    order.setOrderNotifyCount(order.getOrderNotifyCount() + 1);
                    try {
                        updateSalesOrder(order);
                    } catch (DataAccessException ex) {
                        // 屏蔽异常
                        logger.error("通知订单时出错-OFFICIAL_GIONEE", ex);
                    }
                } else if (order.getOrderSource().equals(OrderSource.OFFICIAL_IUNI.getCode())) {
                    // IUNI官网
                    String jsonReq = "";
                    OrderNotifyDTO orderDTO = new OrderNotifyDTO();
                    orderDTO.setOrderSn(order.getOrderCode());
                    if (OrderStatus.BACKED.getCode() == order.getOrderStatus()) {
                        // 远程通知订单中心已退货
                        // 根据订单号查询退货单及退货商品
                        Map<String, Object> criteria = Maps.newHashMap();
                        criteria.put("orderCode", order.getOrderCode());
                        criteria.put("backStatus", BackStatus.BACKED.getCode());
                        Back back = backService.getBack(criteria);
                        orderDTO.setDelverySn(back.getBackCode());
                        orderDTO.setOrderSn(back.getOrderCode());
                        orderDTO.setOrderStatus(RemoteOrderStatus.REFUNDING.getCode());
                        orderDTO.setRemark(back.getRemarkBacked());
                        orderDTO.setShippingCode(back.getShippingCode());
                        orderDTO.setShippingSn(back.getShippingNo());
                        // 从商品流转信息中查询退货商品和数量
                        Map<String, Integer> goodsMap = Maps.newHashMap();
                        criteria.clear();
                        criteria.put("flowId", back.getId());
                        criteria.put("flowType", IndivFlowType.IN_RMA.getCode());
                        List<IndivFlow> indivList = indivDao.queryIndivFlowList(criteria);
                        for (IndivFlow indiv : indivList) {
                            String skuCode = indiv.getSkuCode();
                            if (goodsMap.containsKey(skuCode)) {
                                goodsMap.put(skuCode, goodsMap.get(indiv.getSkuCode()) + 1);
                            } else {
                                goodsMap.put(skuCode, 1);
                            }
                        }

                        List<OrderNotifyGoodsDTO> goods = Lists.newArrayList();
                        for (Map.Entry<String, Integer> entry : goodsMap.entrySet()) {
                            OrderNotifyGoodsDTO good = new OrderNotifyGoodsDTO();
                            good.setQuantity(Integer.valueOf(entry.getValue().toString()));
                            good.setSkuCode(entry.getKey().toString());
                            goods.add(good);
                        }
                        orderDTO.setGoods(goods);
                    } else if (OrderStatus.SHIPPED.getCode() == order.getOrderStatus()) {
                        // 远程通知订单中心已发货
                        Shipping shipping = shippingService.getShipping(order.getShippingId());
                        String shippingSn = order.getShippingNo();
                        orderDTO.setOrderStatus(RemoteOrderStatus.SENDING.getCode());
                        orderDTO.setShippingCode(shipping.getShippingCode());
                        orderDTO.setShippingSn(shippingSn);
                        orderDTO.setShippingName(shipping.getShippingName());
                    } else if (OrderStatus.PRINTED.getCode() == order.getOrderStatus()) {
                        // 远程通知订单中心已打单
                        orderDTO.setOrderStatus(RemoteOrderStatus.PREPARING.getCode());
                    } else if (OrderStatus.RECEIVED.getCode() == order.getOrderStatus()) {
                        // 远程通知订单中心已签收
                        orderDTO.setOrderStatus(RemoteOrderStatus.SHIPPED.getCode());
                    } else if (OrderStatus.REFUSEING.getCode() == order.getOrderStatus()) {
                        // 远程通知订单中心拒收中
                        orderDTO.setOrderStatus(RemoteOrderStatus.REJECTING.getCode());
                    } else if (OrderStatus.REFUSED.getCode() == order.getOrderStatus()) {
                        // 远程通知订单中心已拒收
                        orderDTO.setOrderStatus(RemoteOrderStatus.REFUSED.getCode());
                    }
                    try {
                        jsonReq = jsonUtils.toJson(orderDTO);
                        oCClient.notifyOrder(jsonReq);
                        order.setOrderNotifyStatus(NotifyStatus.NOTIFIED_SUCCESS.getCode());
                    } catch (Exception e) {
                        order.setOrderNotifyStatus(NotifyStatus.NOTIFIED_FAIL.getCode());
                    }
                    // 更新本次通知结果
                    order.setOrderNotifyTime(new Date());
                    order.setOrderNotifyCount(order.getOrderNotifyCount() + 1);
                    try {
                        updateSalesOrder(order);
                    } catch (DataAccessException ex) {
                        // 屏蔽异常
                        logger.error("通知订单时出错-OFFICIAL_IUNI", ex);
                    }
                }
            }
        }
    }

    /**
     * 异步通知聚石塔
     */
    private class TOPTask implements Runnable {
        private List<SalesOrder> orders;

        public TOPTask(List<SalesOrder> orders) {
            this.orders = orders;
        }

        @Override
        public void run() {
            for (SalesOrder order : orders) {
                try {
                    // 获取物流公司编码
                    Shipping shipping = shippingService.getShipping(order.getShippingId());
                    SendRequest req = new SendRequest();
                    req.setOrderCode(order.getOrderCode());
                    req.setShippingCode(shipping.getShippingCode());
                    req.setShippingNo(order.getShippingNo());
                    if (order.getOrderSource().equals(OrderSource.TMALL_GIONEE.getCode())) {
                        orderCenterClient.notifyTOPToSend(req);
                    } else if (order.getOrderSource().equals(OrderSource.VIP_GIONEE.getCode())) {
                        SyncVipOrderClient syncVipOrderClient = new SyncVipOrderClient();
                        syncVipOrderClient.notifyToSend(req);
                    }
                } catch (Exception e) {
                    logger.error("notify TOP error", e);
                }
            }
        }
    }

    /**
     * 异步开票
     */
    private class InvoiceTask implements Runnable {
        private String[] ids;
        private String ipFrom;

        public InvoiceTask(String[] ids, String ipFrom) {
            this.ids = ids;
            this.ipFrom = ipFrom;
        }

        @Override
        public void run() {
            URL url = null;
            HttpURLConnection conn = null;
            OutputStream outStream = null;
            InputStream inStream = null;
            try {
                String address = "";
                if (StringUtils.isBlank(ipFrom)) {
                    address = WmsConstants.INVOICE_WS_URL;
                } else {
                    address = "http://" + ipFrom + "/wsdl/";
                }
                logger.info("makeInvoice--ipFrom--" + address);
                logService.insertLog(new Log(LogType.BIZ_LOG.getCode(), "打印发票", "makeInvoice--ipFrom--:" + address, "admin", new Date()));
                url = new URL(address);
                // 记录成功打印发票的订单
                List<Long> hasMakedIds = Lists.newArrayList();
                // 记录拣货批次
                Map<String, Long> batchCodeMap = Maps.newHashMap();
                // 获取订单数据
                List<SalesOrderVo> orderList = queryGoodsListByOrderIds(ids);
                // 按选择顺序打发票
                for (String id : ids) {
                    for (SalesOrderVo order : orderList) {
                        if (String.valueOf(order.getId()).equals(id)) {
                            if (order.getInvoiceEnabled().equals(0) || order.getInvoiceAmount() == null || order.getInvoiceAmount().intValue() == 0) {
                                continue;
                            }
                            StringBuffer soap = new StringBuffer("");// 封装soap
                            soap.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
                            soap.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">");
                            soap.append("<soapenv:Body>");
                            soap.append("<ns1:MakeInvoice xmlns:ns1=\"uuid:2816E500-86FB-11D5-A05A-802E45844855\">");
                            soap.append("<ID>" + order.getOrderCode() + "</ID>");// 单据编号，传入唯一的单据编号
                            soap.append("<HeadInfo>" + getHeadInfo(order) + "</HeadInfo>"); // 发票头传入
                            // base64编码
                            soap.append("<LineListInfo>" + getLineListInfo(order) + "</LineListInfo>");// 发票行传入
                            // base64编码
                            soap.append("<PrintFlag>" + "1" + "</PrintFlag>"); // 是否在服务端直接打印
                            // 0：不打印
                            // 1：打印
                            soap.append("</ns1:MakeInvoice>");
                            soap.append("</soapenv:Body>");
                            soap.append("</soapenv:Envelope>");
                            logger.info("makeInvoice--request--" + soap.toString());
                            logService.insertLog(new Log(LogType.BIZ_LOG.getCode(), "打印发票", "makeInvoice--request--" + soap.toString(), "admin", new Date()));
                            // 发起请求

                            conn = (HttpURLConnection) url.openConnection();
                            conn.setRequestMethod("POST");
                            conn.setRequestProperty("Content-Type", "text/xml;charset=UTF-8");
                            conn.addRequestProperty("TaxNo:", WmsConstants.INVOICE_SELLER_NO);
                            conn.addRequestProperty("PassWord:", WmsConstants.INVOICE_SELLER_PASSWORD);
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            outStream = conn.getOutputStream();
                            outStream.write(soap.toString().getBytes("UTF-8"));
                            int code = 0;
                            code = conn.getResponseCode();

                            if (code == 200) {
                                inStream = conn.getInputStream();
                                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                                Document document = dBuilder.parse(inStream);
                                NodeList nl = document.getElementsByTagName("Result");
                                Element el = (Element) nl.item(0);
                                StringBuffer sb = new StringBuffer("");
                                sb.append(el.getTextContent());
                                sb.append(";");
                                nl = document.getElementsByTagName("InfoTypeCode");
                                el = (Element) nl.item(0);
                                sb.append(el.getTextContent());
                                sb.append(";");
                                nl = document.getElementsByTagName("InfoNumber");
                                el = (Element) nl.item(0);
                                sb.append(el.getTextContent());
                                sb.append(";");
                                nl = document.getElementsByTagName("InfoAmount");
                                el = (Element) nl.item(0);
                                sb.append(el.getTextContent());
                                sb.append(";");
                                nl = document.getElementsByTagName("InfoTaxAmount");
                                el = (Element) nl.item(0);
                                sb.append(el.getTextContent());
                                sb.append(";");
                                nl = document.getElementsByTagName("GoodsListFlag");
                                el = (Element) nl.item(0);
                                sb.append(el.getTextContent());
                                sb.append(";");
                                nl = document.getElementsByTagName("InfoDate");
                                el = (Element) nl.item(0);
                                sb.append(el.getTextContent());
                                sb.append(";");
                                nl = document.getElementsByTagName("InfoMonth");
                                el = (Element) nl.item(0);
                                sb.append(el.getTextContent());
                                sb.append(";");
                                logger.info("makeInvoice--response--" + sb.toString());
                                logService.insertLog(new Log(LogType.BIZ_LOG.getCode(), "打印发票", "makeInvoice--response--" + sb.toString(), "admin", new Date()));

                                // 记录成功打印发票的订单
                                if (sb.toString().startsWith("5011")) {
                                    hasMakedIds.add(order.getId());
                                    batchCodeMap.put(order.getBatchCode(), order.getBatchId());
                                } else {
                                    break;
                                }
                            } else {
                                logger.info("makeInvoice--err--httpCode:" + code);
                                logService.insertLog(new Log(LogType.BIZ_LOG.getCode(), "打印发票", "makeInvoice--err--httpCode:" + code, "admin", new Date()));
                            }
                        }
                    }
                }

                // 发票打印成功后设置发票已出
                if (CollectionUtils.isNotEmpty(hasMakedIds)) {
                    setInvoiceStatus(hasMakedIds);
                    handBatch(batchCodeMap);
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("makeInvoice--exception：" + e.getMessage(), e);
                try {
                    logService.insertLog(new Log(LogType.BIZ_LOG.getCode(), "打印发票", "makeInvoice--exception：" + e.getMessage(), "admin", new Date()));
                } catch (Exception e1) {
                }
            } finally {
                if (null != conn) {
                    conn.disconnect();
                }
                try {
                    if (inStream != null) {
                        inStream.close();
                    }
                    if (outStream != null) {
                        outStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    @Override
    public void addInsertBatch(List<SalesOrder> list) throws Exception {
        if (!CollectionUtils.isEmpty(list)) {

            List<SalesOrder> salesOrders = new ArrayList<>();
            List<SalesOrderGoods> salesOrderGoods = new ArrayList<>();
            String prefix = new SimpleDateFormat("yyyyMMdd").format(new Date());

            // 转换同一个订单多个sku的数据
            for (int i = 0; i < list.size(); i++) {
                list.get(i).setPaymentName("在线支付");
                list.get(i).setOrderSource(WmsConstants.getOrderCodeByName(list.get(i).getOrderSource()));
                list.get(i).setType("普通订单");
                list.get(i).setShippingId(23559737L);
                list.get(i).setShippingName("东莞直发");
                list.get(i).setPaymentTime(new Date());
                list.get(i).setOrderTime(new Date());
                list.get(i).setHandledBy(ActionUtils.getLoginName());
                list.get(i).setHandledTime(new Date());
                list.get(i).setOrderStatus(OrderStatus.FILTERED.getCode());
                list.get(i).setOrderCode(prefix + OrderCodeUtils.generateRadix());

                if (salesOrders.size() == 0) {
                    salesOrders.add(list.get(i));
                } else {
                    if (list.get(i).getMobile().equals(list.get(i - 1).getMobile())) {
                        list.get(i - 1).getGoodsList().addAll(list.get(i).getGoodsList());
                    } else {
                        salesOrders.add(list.get(i));
                    }
                }
            }
            // 合并orderGoods

            for (SalesOrder salesOrder : salesOrders) {
                //计算费用信息
                BigDecimal totalPrice = new BigDecimal(0);
                for (SalesOrderGoods orderGoods : salesOrder.getGoodsList()) {
                    totalPrice = totalPrice.add(BigDecimal.valueOf(orderGoods.getQuantity()).multiply(orderGoods.getUnitPrice()));
                }
                salesOrder.setGoodsAmount(totalPrice);
                salesOrder.setOrderAmount(totalPrice);
                salesOrder.setPayableAmount(totalPrice);

                orderDao.addOrder(salesOrder);
                Map<String, Sku> skuMap = getSkuMap(salesOrder.getGoodsList());
                if (skuMap != null && skuMap.size() > 0) {
                    convertSku(salesOrder, salesOrder.getGoodsList(), skuMap);
                    salesOrderGoods.addAll(salesOrder.getGoodsList());
                }
                //保存操作日志
                saveOperateLog(salesOrder);
            }
            if (CollectionUtils.isEmpty(salesOrderGoods)) {
                throw new Exception("上传失败");
            }
            orderDao.batchAddOrderGoods(salesOrderGoods);
        }
    }
}
