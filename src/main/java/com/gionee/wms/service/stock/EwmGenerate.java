package com.gionee.wms.service.stock;

import com.dxhy.openApi.SdkException;
import com.dxhy.openApi.bean.protocol.Data;
import com.dxhy.openApi.bean.protocol.GlobalInfo;
import com.dxhy.openApi.service.OpenApiSdk;
import com.dxhy.openApi.service.SdkInitialize;
import com.gionee.wms.entity.InvoiceInfo;
import com.gionee.wms.entity.Log;
import com.gionee.wms.service.log.LogService;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.google.common.collect.Maps;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import static com.gionee.wms.common.EInvoiceConfig.*;
import static com.gionee.wms.common.WmsConstants.LogType.BIZ_LOG;

/**
 * 生成添加到微信卡包二维码
 * Created by Pengbin on 2017/3/29.
 */
@Component
public class EwmGenerate implements EwmGenerateService {

    private static Logger logger = LoggerFactory.getLogger(EwmGenerate.class);

    @Autowired
    private InvoiceInfoService invoiceInfoService;
    @Qualifier("orderService")
    private SalesOrderService salesOrderService;
    @Autowired
    private LogService logService;

    public EwmGenerate() {
        try {
            GlobalInfo globalInfo = new GlobalInfo(W_APP_ID, W_VERSION).enterpriseInfo(W_TAX_PAYER_ID, W_ENTERPRISE_CODE).userInfo(W_USER_NAME, W_PASSWORD);
            SdkInitialize sdkInitialize = SdkInitialize.getInstance();
            sdkInitialize.init(globalInfo);
        } catch (SdkException e) {
            logger.error("初始化微信开票接口异常：", e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ServiceCtrlMessage ewmGenerate(String orderCode) {
        try {
            String response = OpenApiSdk.getInstance().deal(W_REQUEST_URL, new Data(content(orderCode)), dataExchangeId());
            JSONObject jsonResp = JSONObject.fromObject(response);
            if ("0000".equals(jsonResp.optString("reply_code"))) {
                InvoiceInfo invoiceInfo = new InvoiceInfo();
                invoiceInfo.setOrderCode(orderCode);
                invoiceInfo.setEwmUrl(jsonResp.optString("ewmurl"));
                invoiceInfoService.saveOrUpdate(invoiceInfo, true);
                return new ServiceCtrlMessage<>(true, "操作成功！", jsonResp.optString("ewmurl"));
            } else {
                logService.insertLog(new Log(BIZ_LOG.getCode(), orderCode, String.format("returnCode:%s,%s", jsonResp.optString("reply_code"), jsonResp.optString("reply_message")), "system", new Date()));
                return new ServiceCtrlMessage<>(false, String.format("returnCode:%s,%s", jsonResp.optString("reply_code"), jsonResp.optString("reply_message")));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            logService.insertLog(new Log(BIZ_LOG.getCode(), orderCode, e.getMessage(), "system", new Date()));
            return new ServiceCtrlMessage<>(false, e.getMessage());
        }
    }

    /**
     * dataExchangeId
     * @return
     */
    private String dataExchangeId() {
        String date = new SimpleDateFormat("yyMMdd").format(new Date());
        String randomString = RandomStringUtils.random(25 - date.length() - W_ENTERPRISE_CODE.length() - W_SERIAL_ID.length(), "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789");
        String dataExchangeId = W_ENTERPRISE_CODE + date + randomString + W_SERIAL_ID;
        return dataExchangeId;
    }

    /**
     * content 报文
     * @param orderCode 订单号
     * @return content
     */
    private String content(String orderCode) {
        InvoiceInfo invoiceInfo = invoiceInfoService.get(orderCode);
        JSONObject jsonObject = JSONObject.fromObject(invoiceInfo.getJsonData());
        Map<String, Object> dataMap = Maps.newHashMap();
        dataMap.put("kprq", jsonObject.optString("KPRQ"));
        dataMap.put("fpdm", jsonObject.optString("FP_DM"));
        dataMap.put("fphm", jsonObject.optString("FP_HM"));
        dataMap.put("kpfmc", "深圳市金立通信设备有限公司");
        dataMap.put("spfmc", salesOrderService.getSalesOrderByCode(orderCode).getConsignee());
        dataMap.put("jshj", jsonObject.optString("HJSE"));

        return JSONObject.fromObject(dataMap).toString();
    }

}
