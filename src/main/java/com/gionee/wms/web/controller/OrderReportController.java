package com.gionee.wms.web.controller;

import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.service.stock.OrderReportService;
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

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/5/26.
 */
@Controller
@RequestMapping("/report")
public class OrderReportController {

    private static Logger logger = LoggerFactory.getLogger(OrderReportController.class);

    @Autowired
    private OrderReportService orderReportService;

    /**
     * @param modelMap
     * @param queryMap
     * @param page
     * @return
     */
    @RequestMapping("/list.do")
    public String list(ModelMap modelMap, QueryMap queryMap, Page page) {
        int total = orderReportService.rightOrderTimelineQueryCount(queryMap.getMap());
        page.setTotalRow(total);
        page.calculate();

        List<Map<String, Object>> list;
        if (total == 0) {
            list = new ArrayList<>(0);
        } else {
            queryMap.put("page", page);
            list = orderReportService.rightOrderTimelineQuery(queryMap.getMap());
        }

        modelMap.addAllAttributes(queryMap.getMap());
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("page", page);
        return "report/rightOrderReport";
    }

    /**
     * 导出
     * @param queryMap
     * @return
     */
    @RequestMapping("/export.do")
    public ResponseEntity<?> export(QueryMap queryMap) {
        {
            HttpHeaders httpHeaders = new HttpHeaders();
            try {
                Page page = new Page();
                page.setStartRow(0);
                page.setEndRow(10000);
                queryMap.put("page", page);
                List<Map<String, String>> exportList = orderReportService.exportQuery(queryMap.getMap());
                if (CollectionUtils.isEmpty(exportList)) {
                    httpHeaders.add("Content-Type", "text/html; charset=utf-8");
                    return new ResponseEntity<>("没有要导出的记录！", httpHeaders, HttpStatus.OK);
                }
                String templeteFile = System.getProperty("WEBCONTENT.BASE.PASH") + "export/order_time_template.xls";
                String tempFile = System.getProperty("WEBCONTENT.BASE.PASH") + "export/order_time_" + System.currentTimeMillis() + ".xls";
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
    }
}
