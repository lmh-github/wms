package com.gionee.wms.service.stock;

import com.gionee.wms.vo.ServiceCtrlMessage;

/**
 * 51fapiao二维码生成
 * Created by Pengbin on 2017/3/29.
 */
public interface EwmGenerateService {

    /**
     * 二维码生成返回短码
     * @param orderCode
     * @return
     */
    ServiceCtrlMessage ewmGenerate(String orderCode);
}
