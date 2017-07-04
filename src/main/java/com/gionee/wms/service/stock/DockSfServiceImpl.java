package com.gionee.wms.service.stock;

import com.gionee.wms.common.JaxbUtil;
import com.gionee.wms.common.LinkMapUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dao.IndivDao;
import com.gionee.wms.dao.SalesOrderDao;
import com.gionee.wms.dao.SalesOrderPushInfoDao;
import com.gionee.wms.entity.*;
import com.gionee.wms.service.log.SalesOrderLogService;
import com.google.common.collect.Lists;
import com.sf.integration.warehouse.request.WmsSailOrderPushInfo;
import com.sf.integration.warehouse.request.WmsSailOrderPushInfoContainerItem;
import com.sf.integration.warehouse.request.WmsSailOrderPushInfoHeader;
import com.sf.integration.warehouse.response.DockSFResponse;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.*;

import static com.gionee.wms.common.WmsConstants.IndivStockStatus.OUT_WAREHOUSE;
import static org.apache.commons.lang.StringUtils.trimToNull;

/**
 * Created by Pengbin on 2017/5/28.
 */
@Service
public class DockSfServiceImpl implements DockSfService {

    private static final Map<String, Integer> STATUS_MAP = new HashMap<String, Integer>() {
        {
            put("10001", WmsConstants.OrderStatus.FILTERED.getCode()); // 生效 ==> 已筛单
            put("10007", WmsConstants.OrderStatus.FILTERED.getCode()); // 待确认 ==> 已筛单
            put("10008", WmsConstants.OrderStatus.FILTERED.getCode()); // 已确认 ==> 已筛单
            put("10003", WmsConstants.OrderStatus.FILTERED.getCode()); // 已下发 ==> 已筛单
            put("300", WmsConstants.OrderStatus.PRINTED.getCode()); // 正在检货 ==> 已打单
            put("400", WmsConstants.OrderStatus.PRINTED.getCode()); // 拣货完成 ==> 已打单
            put("700", WmsConstants.OrderStatus.PICKING.getCode()); // 包装完成 ==> 配货中
            put("10017", WmsConstants.OrderStatus.SHIPPING.getCode()); // 装车完成 ==> 待出库
            put("10018", WmsConstants.OrderStatus.SHIPPING.getCode()); // 封车完成 ==> 待出库
            put("900", WmsConstants.OrderStatus.SHIPPED.getCode()); // 发货确认 ==> 已出库
            put("10016", WmsConstants.OrderStatus.RECEIVED.getCode()); // 已完成 ==> 已签收
            put("10011", WmsConstants.OrderStatus.FILTERED.getCode()); // 已冻结 ==>
            put("10012", WmsConstants.OrderStatus.FILTERED.getCode()); // 已作废 ==>
            put("10013", WmsConstants.OrderStatus.CANCELED.getCode()); // 已取消 ==> 已取消
        }
    };

    private static final List<Integer> STATUS_SEQUECE = new ArrayList<>();

    static {
        STATUS_SEQUECE.add(WmsConstants.OrderStatus.FILTERED.getCode()); // 生效 ==> 已筛单
        STATUS_SEQUECE.add(WmsConstants.OrderStatus.FILTERED.getCode()); // 待确认 ==> 已筛单
        STATUS_SEQUECE.add(WmsConstants.OrderStatus.FILTERED.getCode()); // 已确认 ==> 已筛单
        STATUS_SEQUECE.add(WmsConstants.OrderStatus.FILTERED.getCode()); // 已下发 ==> 已筛单
        STATUS_SEQUECE.add(WmsConstants.OrderStatus.PRINTED.getCode()); // 正在检货 ==> 已打单
        STATUS_SEQUECE.add(WmsConstants.OrderStatus.PRINTED.getCode()); // 拣货完成 ==> 已打单
        STATUS_SEQUECE.add(WmsConstants.OrderStatus.PICKING.getCode()); // 包装完成 ==> 配货中
        STATUS_SEQUECE.add(WmsConstants.OrderStatus.SHIPPING.getCode()); // 装车完成 ==> 待出库
        STATUS_SEQUECE.add(WmsConstants.OrderStatus.SHIPPING.getCode()); // 封车完成 ==> 待出库
        STATUS_SEQUECE.add(WmsConstants.OrderStatus.SHIPPED.getCode()); // 发货确认 ==> 已出库
        STATUS_SEQUECE.add(WmsConstants.OrderStatus.RECEIVED.getCode()); // 已完成 ==> 已签收
        STATUS_SEQUECE.add(WmsConstants.OrderStatus.FILTERED.getCode()); // 已冻结 ==>
        STATUS_SEQUECE.add(WmsConstants.OrderStatus.FILTERED.getCode()); // 已作废 ==>
        STATUS_SEQUECE.add(WmsConstants.OrderStatus.CANCELED.getCode()); // 已取消 ==> 已取消
    }

    private final Logger logger = LoggerFactory.getLogger(DockSfServiceImpl.class);

    @Autowired
    private SalesOrderService orderService;
    @Autowired
    private SalesOrderLogService salesOrderLogService;
    @Autowired
    private SalesOrderMapService salesOrderMapService;
    @Autowired
    private SalesOrderNodeInfoService salesOrderNodeInfoService;
    @Autowired
    private InvoiceInfoService invoiceInfoService;
    @Autowired
    private SalesOrderPushInfoDao salesOrderPushInfoDao;
    @Autowired
    private SalesOrderDao salesOrderDao;
    @Autowired
    private IndivDao indivDao;

    /** {@inheritDoc} */
    @Override
    @Transactional
    public DockSFResponse dock(String sailOrderPushInfoXML) {
        WmsSailOrderPushInfo sailOrderPushInfo = JaxbUtil.unmarshToObjBinding(WmsSailOrderPushInfo.class, sailOrderPushInfoXML);
        if (sailOrderPushInfo == null) {
            logger.error("“出库单状态与明细推送接口”推送内容格式错误！");
            return new DockSFResponse(false, "推送的格式内容错误！");
        }

        WmsSailOrderPushInfoHeader header = sailOrderPushInfo.getHeader();
        String erp_order = header.getErp_order();
        SalesOrderMap salesOrderMap = salesOrderMapService.getByErpOrderCode(header.getErp_order());
        if (salesOrderMap == null) {
            logger.error(MessageFormat.format("“出库单状态与明细推送接口”，顺丰ERP订单号：{0}不存在！", erp_order));
            return new DockSFResponse(false, MessageFormat.format("erp_order：{0}在系统中没有对应数据！", erp_order));
        }

        // 记录推送信息
        salesOrderPushInfoDao.insert(new SalesOrderPushInfo(salesOrderMap.getOrder_code(), new Date(), sailOrderPushInfoXML));

        SalesOrder salesOrder = salesOrderDao.queryOrderByOrderCode(salesOrderMap.getOrder_code());
        String status_code = header.getStatus_code();
        // 是否确认出库，通知外部系统
        boolean notifyFlag = false;
        if ("900".equals(status_code)) { // 已出库状态
            List<SalesOrderImei> imeiList = salesOrderMapService.queryImeis(LinkMapUtils.<String, String>newHashMap().put("order_code", salesOrderMap.getOrder_code()).getMap());
            List<WmsSailOrderPushInfoContainerItem> containerList = sailOrderPushInfo.getContainerList();

            if (CollectionUtils.isNotEmpty(containerList) && CollectionUtils.isEmpty(imeiList)) {
                // 该部分为串号操作
                // WmsSailOrderPushInfoContainerItem item = containerList.get(0);
                List<String> serial_number_list = Lists.newArrayList();
                for (WmsSailOrderPushInfoContainerItem item : containerList) {
                    if (CollectionUtils.isEmpty(item.getSerial_number())) {
                        continue;
                    } else {
                        serial_number_list.addAll(item.getSerial_number());
                    }
                }
                if (CollectionUtils.isNotEmpty(serial_number_list)) {
                    List<SalesOrderImei> imeis = Lists.newArrayList();
                    List<Indiv> indivs = Lists.newArrayList();
                    for (String imei : serial_number_list) {
                        imeis.add(new SalesOrderImei(salesOrder.getOrderCode(), erp_order, imei));
                        // 个体订单标示指向此订单，退货时可根据此关联进行，退货入库状态修改
                        Indiv indiv = new Indiv();
                        indiv.setIndivCode(imei);
                        indivs.add(indiv);
                    }
                    salesOrderMapService.batchAddImes(imeis);
                    // 更新个体订单关联
                    indivDao.batchUpdateIndivsStock(LinkMapUtils.<String, Object>newHashMap().put("orderId", salesOrder.getId()).put("orderCode", salesOrder.getOrderCode()).put("indivCodes", indivs).put("stockStatus", OUT_WAREHOUSE.getCode()).getMap());
                }
                // End 串号操作
            }
            notifyFlag = true;
        } else {
            salesOrderMap.setActual_ship_date_time(header.getActual_ship_date_time());
            salesOrderMap.setCarrier(trimToNull(header.getCarrier()));
            salesOrderMap.setCarrier_service(trimToNull(header.getCarrier_service()));
            salesOrderMapService.update(salesOrderMap);
        }

        int newOrderStatus = STATUS_MAP.get(status_code);
        if (STATUS_SEQUECE.indexOf(newOrderStatus) < STATUS_SEQUECE.indexOf(salesOrder.getOrderStatus())) {
            newOrderStatus = salesOrder.getOrderStatus();
        }

        // 修改订单状态和运单号
        salesOrderDao.updateOrder(LinkMapUtils.<String, Object>newHashMap().put("orderStatus", newOrderStatus).put("orderCode", salesOrderMap.getOrder_code()).put("shippingNo", trimToNull(header.getWaybill_no())).put("orderId", salesOrder.getId()).getMap());
        salesOrderNodeInfoService.updateFromDockSf(salesOrderMap.getOrder_code(), newOrderStatus, header.getStatus_time(), header.getWarehouse(), header.getCarrier(), header.getWaybill_no());
        addOpLog(newOrderStatus, salesOrder.getId()); // 记录操作日志
        // 订单被取消
        if (WmsConstants.OrderStatus.CANCELED.getCode() == newOrderStatus) {
            invoiceInfoService.cancelOrder(salesOrderMap.getOrder_code());
        }
        // 通知外部系统，修改订单状态
        if (notifyFlag) {
            if (salesOrder.getOrderSource().equals(WmsConstants.OrderSource.OFFICIAL_GIONEE)) {
                // 官网
                orderService.notifyOrder(Lists.newArrayList(salesOrder));
            } else {
                // 其他电商平台
                orderService.notifyTOP(Lists.newArrayList(salesOrder));
            }
        }

        return new DockSFResponse(true, "成功！");
    }

    /**
     * 记录操作日志
     * @throws Exception
     */
    private void addOpLog(Integer orderStatus, Long orderId) {
        SalesOrderLog lg = new SalesOrderLog();
        lg.setOpTime(new Date());
        lg.setOpUser("WMS业务记录者(sf推送)");
        lg.setOrderStatus(orderStatus);
        lg.setOrderId(orderId);
        lg.setRemark("出库单状态与明细推送接口");
        salesOrderLogService.insertSalesOrderLog(lg);
    }
}
