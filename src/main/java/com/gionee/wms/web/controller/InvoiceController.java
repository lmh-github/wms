package com.gionee.wms.web.controller;

import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.PageResult;
import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.entity.InvoiceInfo;
import com.gionee.wms.service.stock.EInvoiceService;
import com.gionee.wms.service.stock.InvoiceInfoService;
import com.gionee.wms.web.action.stock.InvoiceInfoAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Pengbin on 2017/4/19.
 */
@Controller
@RequestMapping("/invoice")
public class InvoiceController {

    private static Logger logger = LoggerFactory.getLogger(InvoiceInfoAction.class);

    @Autowired
    private InvoiceInfoService invoiceInfoSerivce;
    @Autowired
    private EInvoiceService eInvoiceService;

    @RequestMapping("/list.do")
    public String query(QueryMap queryMap, Page page, ModelMap modelMap) {
        page = page == null ? new Page() : page;
        PageResult<InvoiceInfo> pageResult = invoiceInfoSerivce.query(queryMap.getMap(), page);
        modelMap.addAttribute("invoiceInfoList", pageResult.getRows());
        modelMap.addAttribute("page", page);

        return "invoice/invoiceInfoList";
    }

}
