package com.gionee.wms.web.action.api;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dto.KpContentResp;
import com.gionee.wms.dto.XzContentResp;
import com.gionee.wms.service.stock.EInvoiceService;
import com.gionee.wms.service.stock.EwmGenerateService;
import com.gionee.wms.service.stock.InvoiceInfoService;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.opensymphony.xwork2.ActionSupport;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

/**
 * 开票外部接口
 * Created by Pengbin on 2017/3/9.
 */
@Controller("EInvoiceApiAction")
@Scope("prototype")
public class EInvoiceApiAction extends ActionSupport {

    private static final Logger LOGGER = LoggerFactory.getLogger(EInvoiceApiAction.class);

    @Autowired
    private EInvoiceService eInvoiceService;
    @Autowired
    private InvoiceInfoService invoiceInfoService;
    @Autowired
    private EwmGenerateService ewmGenerateService;

    /**
     * 请求体转JSON
     * @return JSONObject
     * @throws Exception IoException
     */
    private <T> T request2Json(Class<T> clazz) throws JSONException {
        try {
            String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream(), WmsConstants.DEFAULT_ENCODING);
            JSONObject jsonObject = JSONObject.fromObject(jsonStr);
            if (clazz == null) {
                return (T) jsonObject;
            }
            return (T) JSONObject.toBean(jsonObject, clazz);

        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            String json = JSONObject.fromObject(new ServiceCtrlMessage<>(false, "解析请求参数有误！")).toString();
            ActionUtils.outputJson(json);
            throw new JSONException();
        }
    }

    /**
     * 开票
     * <br>!@param orderCode 订单号
     */
    public void makeEInvoice() {
        try {
            JSONObject jsonObject = request2Json(null);
            String orderCode = jsonObject.optString("orderCode");
            ServiceCtrlMessage<KpContentResp> serviceCtrlMessage = eInvoiceService.makeEInvoice(orderCode);

            ActionUtils.outputJson(JSONObject.fromObject(serviceCtrlMessage).toString());
        } catch (JSONException e) {
        } catch (Exception e) {
            e.printStackTrace();
            ActionUtils.outputJson(JSONObject.fromObject(new ServiceCtrlMessage<>(false, e.getMessage())).toString());
        }
    }

    /**
     * 下载发票
     */
    public void downloadEInvoice() {
        try {
            JSONObject jsonObject = request2Json(null);
            String orderCode = jsonObject.optString("orderCode");
            ServiceCtrlMessage<XzContentResp> serviceCtrlMessage = eInvoiceService.downEInvoice(orderCode);
            ActionUtils.outputJson(JSONObject.fromObject(serviceCtrlMessage).toString());
        } catch (JSONException e) {
        } catch (Exception e) {
            e.printStackTrace();
            ActionUtils.outputJson(JSONObject.fromObject(new ServiceCtrlMessage<>(false, e.getMessage())).toString());
        }
    }

    /**
     * 查询发票
     * <br>!@param orderCode 订单号
     * <br>!@param mobile    手机号
     */
    public void queryEInvoice() {
        try {
            Map<String, Object> paramMap = request2Json(Map.class);
            List<Map<String, Object>> mapList = invoiceInfoService.extQuery(paramMap);
            ActionUtils.outputJson(JSONObject.fromObject(new ServiceCtrlMessage(true, null, mapList)).toString());
        } catch (JSONException e) {
        } catch (Exception e) {
            e.printStackTrace();
            ActionUtils.outputJson(JSONObject.fromObject(new ServiceCtrlMessage<>(false, e.getMessage())).toString());
        }
    }

    /**
     * 冲红
     * <br>!@param orderCode 订单号
     */
    public void chEInvoice() {
        try {
            JSONObject jsonObject = request2Json(null);
            String orderCode = jsonObject.optString("orderCode");
            ServiceCtrlMessage<XzContentResp> serviceCtrlMessage = eInvoiceService.invalidEIvoice(orderCode, true);
            ActionUtils.outputJson(JSONObject.fromObject(serviceCtrlMessage).toString());
        } catch (JSONException e) {
        } catch (Exception e) {
            e.printStackTrace();
            ActionUtils.outputJson(JSONObject.fromObject(new ServiceCtrlMessage<>(false, e.getMessage())).toString());
        }
    }

    /**
     * 微信二维码生成
     * <br>!@param orderCode 订单号
     */
    public void ewmGenerate() {
        try {
            JSONObject jsonObject = request2Json(null);
            String orderCode = jsonObject.optString("orderCode");
            ServiceCtrlMessage<XzContentResp> serviceCtrlMessage = ewmGenerateService.ewmGenerate(orderCode);
            ActionUtils.outputJson(JSONObject.fromObject(serviceCtrlMessage).toString());
        } catch (JSONException e) {
        } catch (Exception e) {
            e.printStackTrace();
            ActionUtils.outputJson(JSONObject.fromObject(new ServiceCtrlMessage<>(false, e.getMessage())).toString());
        }
    }

}
