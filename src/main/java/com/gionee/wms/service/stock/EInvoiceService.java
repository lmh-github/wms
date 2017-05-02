package com.gionee.wms.service.stock;

import com.gionee.wms.dto.KpContentResp;
import com.gionee.wms.dto.XzContentResp;
import com.gionee.wms.vo.ServiceCtrlMessage;

/**
 * 电子发票处理类
 * Created by Pengbin on 2017/2/20.
 */
public interface EInvoiceService {

    /**
     * 开电子票
     * @param orderCode 订单号
     * @return ServiceCtrlMessage
     */
    ServiceCtrlMessage<KpContentResp> makeEInvoice(String orderCode);

    /**
     * 下载电子发票
     * @param orderCode 订单号
     * @return ServiceCtrlMessage
     */
    ServiceCtrlMessage<XzContentResp> downEInvoice(String orderCode);

    /**
     * 作废发票
     * @param orderCode 订单号
     * @param forced    是否强制冲红
     * @return ServiceCtrlMessage
     */
    ServiceCtrlMessage invalidEIvoice(String orderCode, boolean forced);

    /**
     * 下载电子发票PDF，并且转换成jpeg格式图片
     * @param orderCode 订单号
     * @return ServiceCtrlMessage
     */
    ServiceCtrlMessage downloadInvoicePdfAnd2Img(String orderCode);
}
