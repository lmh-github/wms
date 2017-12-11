package com.gionee.wms.web.controller;

import com.gionee.wms.common.ApplicationContextHelper;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dto.*;
import com.gionee.wms.entity.InvoiceInfo;
import com.gionee.wms.service.stock.EInvoiceService;
import com.gionee.wms.service.stock.InvoiceInfoService;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.gionee.wms.web.action.stock.InvoiceInfoAction;
import com.gionee.wms.web.extend.DwzMessage;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.defaultString;

/**
 * Created by Pengbin on 2017/4/19.
 */
@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    private static Logger logger = LoggerFactory.getLogger(InvoiceInfoAction.class);

    @Autowired
    private InvoiceInfoService invoiceInfoSerivce;

    @RequestMapping("/list.do")
    public String query(QueryMap queryMap, Page page, ModelMap modelMap) {
        PageResult<InvoiceInfo> pageResult = invoiceInfoSerivce.query(queryMap.getMap(), page);
        modelMap.addAttribute("invoiceInfoList", pageResult.getRows());
        modelMap.addAttribute("page", page);
        modelMap.addAllAttributes(queryMap.getMap());

        return "invoice/invoiceInfoList";
    }

    /**
     * 导出
     * @param queryMap
     * @return
     */
    @RequestMapping(value = "/export.do")
    public ResponseEntity<?> export(QueryMap queryMap) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            List<Map<String, String>> exportList = invoiceInfoSerivce.exprotQuery(queryMap.getMap());
            if (CollectionUtils.isEmpty(exportList)) {
                httpHeaders.add("Content-Type", "text/html; charset=utf-8");
                return new ResponseEntity<>("没有要导出的记录！", httpHeaders, HttpStatus.OK);
            }
            String templeteFile = System.getProperty("WEBCONTENT.BASE.PASH") + "export/invoice_exp_template.xls";
            String tempFile = System.getProperty("WEBCONTENT.BASE.PASH") + "export/invoice_" + System.currentTimeMillis() + ".xls";
            File file = ExcelExpUtil.expExcel(new ExcelModule(exportList), templeteFile, tempFile);
            FileSystemResource resource = new FileSystemResource(file);

            httpHeaders.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity(resource, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            httpHeaders.add("Content-Type", "text/html; charset=utf-8");
            return new ResponseEntity<>("下载出现异常！", httpHeaders, HttpStatus.OK);
        }
    }

    /**
     * 开具发票
     * @param queryMap
     * @param orderCode
     * @return
     */
    @RequestMapping("/makeEInvoice.do")
    @ResponseBody
    public Object makeInvoice(QueryMap queryMap, String orderCode) {
        try {
            ServiceCtrlMessage<KpContentResp> message = ApplicationContextHelper.getBean(EInvoiceService.class).makeEInvoice(orderCode);
            if (message.isResult()) {
                return DwzMessage.success(defaultString(message.getMessage(), "开票成功！"), queryMap);
            } else {
                return DwzMessage.error(defaultString(message.getMessage(), "开票失败！"), queryMap);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("操作异常：" + e.getMessage(), queryMap);
        }
    }

    /**
     * 下载发票
     * @param queryMap
     * @param orderCode
     * @return
     */
    @RequestMapping("/downloadEInvoice.do")
    @ResponseBody
    public Object downloadEInvoice(QueryMap queryMap, String orderCode) {
        try {
            ServiceCtrlMessage<XzContentResp> message = ApplicationContextHelper.getBean(EInvoiceService.class).downEInvoice(orderCode);
            if (message.isResult()) {
                return DwzMessage.success(message.getMessage(), queryMap);
            } else {
                return DwzMessage.error(message.getMessage(), queryMap);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("操作异常：" + e.getMessage(), queryMap);
        }
    }

    /**
     * 冲红电子发票
     * @param queryMap
     * @param orderCode
     * @return
     */
    @RequestMapping("/chEInvoice.do")
    @ResponseBody
    public Object chEInvoice(QueryMap queryMap, String orderCode) {
        try {
            ServiceCtrlMessage message = ApplicationContextHelper.getBean(EInvoiceService.class).invalidEIvoice(orderCode, true);
            if (message.isResult()) {
                return DwzMessage.success(defaultString(message.getMessage(), "冲红成功！"), queryMap);
            } else {
                return DwzMessage.error(defaultString(message.getMessage(), "冲红失败！"), queryMap);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("操作异常：" + e.getMessage(), queryMap);
        }
    }

    @RequestMapping("/downPDF.do")
    @ResponseBody
    public Object downPDF(QueryMap queryMap, String orderCode) {
        try {
            ServiceCtrlMessage message = ApplicationContextHelper.getBean(EInvoiceService.class).downloadInvoicePdfAnd2Img(orderCode);
            if (message.isResult()) {
                return DwzMessage.success(defaultString(message.getMessage(), "操作成功！"), queryMap);
            } else {
                return DwzMessage.error(defaultString(message.getMessage(), "操作失败！"), queryMap);
            }

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("操作异常：" + e.getMessage(), queryMap);
        }
    }
}
