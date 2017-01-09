package com.gionee.wms.service.stock;

import com.gionee.wms.entity.Transfer;
import com.gionee.wms.vo.ServiceCtrlMessage;

public interface QimenBusinessService {
    /**
     * 入库单创建接口*
     */
    public ServiceCtrlMessage entryOrderCreate(Transfer transfer);

    /**
     * 入库单确认接口*
     */
    public ServiceCtrlMessage entryOrderConfirm(Transfer transfer);

    /**
     * 订单流水通知接口
     */
    public ServiceCtrlMessage orderProcessReport(Transfer transfer);

    /**
     * 出库单创建接口
     */
    public ServiceCtrlMessage stockOutCreate(Transfer transfer);

    /**
     * 出库单确认接口
     */
    public ServiceCtrlMessage stockOutConfirm(Transfer transfer);

}
