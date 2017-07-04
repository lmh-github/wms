package com.gionee.wms.service.stock;

import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.vo.ServiceCtrlMessage;

import java.util.Date;
import java.util.List;

/**
 * Created by Pengbin on 2017/5/24.
 */
public interface SalesOrderNodeInfoService {

    /**
     * @param salesOrder
     * @return
     */
    ServiceCtrlMessage saveFromSalesOrder(SalesOrder salesOrder, List<SalesOrderGoods> orderGoodsList);

    /**
     * @param salesOrder
     * @param orderGoodsList
     * @return
     */
    ServiceCtrlMessage updateFromSalesOrder(SalesOrder salesOrder, List<SalesOrderGoods> orderGoodsList);

    /**
     * @param orderStatus
     * @param statusTime
     * @param warehouseCode
     * @param expressNo
     * @return
     */
    ServiceCtrlMessage updateFromDockSf(String orderCode, Integer orderStatus, Date statusTime, String warehouseCode, String carrier, String expressNo);

    /**
     * @param orderCode
     * @param pushDate
     * @return
     */
    ServiceCtrlMessage updatePushTime(String orderCode, Date pushDate);
}
