package com.gionee.wms.service.stock;

import com.gionee.wms.common.WmsConstants.OrderStatus;
import com.gionee.wms.dao.SalesOrderNodeInfoDao;
import com.gionee.wms.dao.WarehouseDao;
import com.gionee.wms.dao.WaresDao;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.entity.SalesOrderNodeInfo;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.vo.ServiceCtrlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static org.apache.commons.lang.StringUtils.defaultIfEmpty;

/**
 * Created by Pengbin on 2017/5/24.
 */
@Service
public class SalesOrderNodeInfoServiceImpl implements SalesOrderNodeInfoService {

    @Autowired
    private SalesOrderNodeInfoDao salesOrderNodeInfoDao;
    @Autowired
    private WarehouseDao warehouseDao;
    @Autowired
    private WaresDao waresDao;

    /** {@inheritDoc} */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ServiceCtrlMessage saveFromSalesOrder(SalesOrder salesOrder, List<SalesOrderGoods> orderGoodsList) {
        if (salesOrder == null || orderGoodsList == null) {
            return new ServiceCtrlMessage(false, "");
        }
        SalesOrderNodeInfo salesOrderNodeInfo = new SalesOrderNodeInfo();
        salesOrderNodeInfo.setOrderCode(salesOrder.getOrderCode());
        salesOrderNodeInfo.setOrderCreateTime(salesOrder.getOrderTime()); // 订单创建时间
        salesOrderNodeInfo.setOrderFilterTime(salesOrder.getJoinTime()); // 筛单时间
        salesOrderNodeInfo.setExpress(salesOrder.getShippingName()); // 物流公司

        setOrderSkuInfo(orderGoodsList, salesOrderNodeInfo);
        salesOrderNodeInfoDao.insert(salesOrderNodeInfo);
        return new ServiceCtrlMessage();
    }

    /** {@inheritDoc} */
    @Override
    public ServiceCtrlMessage updateFromSalesOrder(SalesOrder salesOrder, List<SalesOrderGoods> orderGoodsList) {
        SalesOrderNodeInfo salesOrderNodeInfo = salesOrderNodeInfoDao.get(salesOrder.getOrderCode());
        if (salesOrderNodeInfo == null) {
            return saveFromSalesOrder(salesOrder, orderGoodsList);
        }
        salesOrderNodeInfo.setExpress(salesOrder.getShippingName());
        setOrderSkuInfo(orderGoodsList, salesOrderNodeInfo);
        salesOrderNodeInfoDao.update(salesOrderNodeInfo);
        return new ServiceCtrlMessage();
    }

    /** {@inheritDoc} */
    @Override
    public ServiceCtrlMessage updateFromDockSf(String orderCode, Integer orderStatus, Date statusTime, String warehouseCode, String carrier, String expressNo) {
        if (orderStatus == null) {
            return new ServiceCtrlMessage(false, "");
        }

        SalesOrderNodeInfo salesOrderNodeInfo = salesOrderNodeInfoDao.get(orderCode);
        if (salesOrderNodeInfo == null) {
            return new ServiceCtrlMessage(false, "");
        }

        if (warehouseCode != null) {
            String warehouseName = warehouseDao.getWarehouseNameByCode(warehouseCode);
            salesOrderNodeInfo.setRealWarehouse(defaultIfEmpty(warehouseName, warehouseCode));
            salesOrderNodeInfo.setTargetWarehouse(defaultIfEmpty(warehouseName, warehouseCode));
        }

        salesOrderNodeInfo.setExpress(defaultIfEmpty(carrier, salesOrderNodeInfo.getExpress()));
        salesOrderNodeInfo.setExpressNo(defaultIfEmpty(expressNo, salesOrderNodeInfo.getExpressNo()));

        if (orderStatus == OrderStatus.PRINTED.getCode()) { // 已打单
            salesOrderNodeInfo.setOrderPrintTime(statusTime);
        } else if (orderStatus == OrderStatus.SHIPPED.getCode()) { // 已出库
            salesOrderNodeInfo.setOrderSendTime(statusTime);
        } else if (orderStatus == OrderStatus.RECEIVED.getCode()) { // 已签收
            salesOrderNodeInfo.setOrderFinishTime(statusTime);
        }

        salesOrderNodeInfoDao.update(salesOrderNodeInfo);

        return new ServiceCtrlMessage(true, null);
    }

    /**
     * @param orderGoodsList
     * @param salesOrderNodeInfo
     */
    private void setOrderSkuInfo(List<SalesOrderGoods> orderGoodsList, SalesOrderNodeInfo salesOrderNodeInfo) {
        StringBuilder sb = new StringBuilder();
        for (SalesOrderGoods goods : orderGoodsList) {
            if ((goods.getUnitPrice() != null && goods.getUnitPrice().doubleValue() > 0) || goods.getSkuCode().startsWith("1")) {
                Sku sku = waresDao.querySkuBySkuCode(goods.getSkuCode());
                if (sku != null) {
                    sb.append(System.lineSeparator()).append(sku.getSkuName());
                }
            }
        }
        salesOrderNodeInfo.setOrderSkuInfo(sb.toString().replaceFirst(System.lineSeparator(), ""));
    }

    /**
     * @param orderCode
     * @param pushTime
     * @return
     */
    @Override
    public ServiceCtrlMessage updatePushTime(String orderCode, Date pushTime) {
        SalesOrderNodeInfo salesOrderNodeInfo = salesOrderNodeInfoDao.get(orderCode);
        if (salesOrderNodeInfo == null) {
            return new ServiceCtrlMessage(false, "");
        }

        salesOrderNodeInfo.setOrderPushTime(pushTime);
        salesOrderNodeInfoDao.update(salesOrderNodeInfo);

        return new ServiceCtrlMessage(true, null);
    }
}
