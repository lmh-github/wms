package com.gionee.wms.web.action.stock;

import com.gionee.wms.common.WmsConstants.EInvoiceStatus;
import com.gionee.wms.dto.KpContentResp;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.PageResult;
import com.gionee.wms.entity.InvoiceInfo;
import com.gionee.wms.service.stock.EInvoiceService;
import com.gionee.wms.service.stock.InvoiceInfoService;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.gionee.wms.web.action.AjaxActionSupport;
import com.google.common.collect.Maps;
import net.sf.json.JSONException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/3/14.
 */
@Controller("InvoiceInfoAction")
@Scope("prototype")
public class InvoiceInfoAction extends AjaxActionSupport {

    private static Logger logger = LoggerFactory.getLogger(InvoiceInfoAction.class);

    @Autowired
    private InvoiceInfoService invoiceInfoSerivce;
    @Autowired
    private EInvoiceService eInvoiceService;

    private Page page = new Page();
    private String orderCode;
    private String mobile;
    private Date kprqBegin;
    private Date kprqEnd;
    private String fpHm;
    private String fpDm;
    private String status;
    private List<InvoiceInfo> invoiceInfoList;

    /**
     * 起始列表查询
     * @return result
     * @throws Exception Exception
     */
    @Override
    public String execute() throws Exception {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("orderCode", StringUtils.trimToNull(orderCode));
        criteria.put("mobile", StringUtils.trimToNull(mobile));
        criteria.put("opDateBegin", kprqBegin);
        criteria.put("opDateEnd", kprqEnd);
        criteria.put("fpHm", StringUtils.trimToNull(fpHm));
        criteria.put("fpDm", StringUtils.trimToNull(fpDm));
        PageResult<InvoiceInfo> pageResult = invoiceInfoSerivce.query(criteria, page);
        invoiceInfoList = pageResult.getRows();
        return SUCCESS;
    }

    /**
     * 修改发票状态
     */
    public void update() {
        try {
            if (StringUtils.isBlank(orderCode)) {
                ajaxError("没有要操作的订单号！");
            }

            EInvoiceStatus eInvoiceStatus = null;
            if (StringUtils.isBlank(status) || (eInvoiceStatus = EInvoiceStatus.valueOf(status)) == null) {
                ajaxError("未知的状态！");
            }
            InvoiceInfo invoiceInfo = new InvoiceInfo();
            invoiceInfo.setOrderCode(orderCode);
            invoiceInfo.setStatus(eInvoiceStatus.toString());
            invoiceInfo.setOpDate(new Date());
            invoiceInfoSerivce.saveOrUpdate(invoiceInfo, true);
            ajaxSuccess("操作成功！");

        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage(), e);
            ajaxSuccess(e.getMessage());
        }
    }

    /**
     * 开票
     * <br>!@param orderCode 订单号
     */
    public void makeEInvoice() {
        try {
            ServiceCtrlMessage<KpContentResp> serviceCtrlMessage = eInvoiceService.makeEInvoice(orderCode);
            if (serviceCtrlMessage.isResult()) {
                ajaxSuccess("操作成功！");
            } else {
                ajaxError("开票失败：" + serviceCtrlMessage.getMessage());
            }
        } catch (JSONException e) {
        } catch (Exception e) {
            e.printStackTrace();
            ajaxError("开票失败：" + e.getMessage());
        }
    }

    /**
     * 冲红发票
     */
    public void ch() {
        try {
            if (StringUtils.isBlank(orderCode)) {
                ajaxError("没有要操作的订单号！");
            }
            ServiceCtrlMessage serviceCtrlMessage = eInvoiceService.invalidEIvoice(orderCode, true);
            if (serviceCtrlMessage.isResult()) {
                ajaxSuccess("操作成功");
            } else {
                ajaxError(serviceCtrlMessage.getMessage());
            }

        } catch (Exception e) {
            logger.error(e.getMessage());
            ajaxError(e.getMessage());
        }
    }

    public void downPDF() {
        try {
            ServiceCtrlMessage serviceCtrlMessage = eInvoiceService.downloadInvoicePdfAnd2Img(orderCode);
            if (serviceCtrlMessage.isResult()) {
                ajaxSuccess("操作成功");
            } else {
                ajaxError(serviceCtrlMessage.getMessage());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
            ajaxError(e.getMessage());
        }
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public List<InvoiceInfo> getInvoiceInfoList() {
        return invoiceInfoList;
    }

    public void setInvoiceInfoList(List<InvoiceInfo> invoiceInfoList) {
        this.invoiceInfoList = invoiceInfoList;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public Date getKprqBegin() {
        return kprqBegin;
    }

    public void setKprqBegin(Date kprqBegin) {
        this.kprqBegin = kprqBegin;
    }

    public Date getKprqEnd() {
        return kprqEnd;
    }

    public void setKprqEnd(Date kprqEnd) {
        this.kprqEnd = kprqEnd;
    }

    public String getFpHm() {
        return fpHm;
    }

    public void setFpHm(String fpHm) {
        this.fpHm = fpHm;
    }

    public String getFpDm() {
        return fpDm;
    }

    public void setFpDm(String fpDm) {
        this.fpDm = fpDm;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
