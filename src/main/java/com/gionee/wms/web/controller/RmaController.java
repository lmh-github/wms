package com.gionee.wms.web.controller;

import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.Receive;
import com.gionee.wms.entity.RmaSalesOrder;
import com.gionee.wms.service.stock.ReceiveService;
import com.gionee.wms.service.wares.IndivService;
import com.gionee.wms.web.extend.DwzMessage;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * RMA（退换货） 操作相关
 * @author
 */
@Controller
@RequestMapping("/rma")
@Slf4j
public class RmaController {

    @Autowired
    private ReceiveService receiveService;
    @Autowired
    private IndivService indivService;

    /**
     * List列表查询
     * @param queryMap queryMap
     * @param page     分页对象
     * @param modelMap modelMap
     * @return
     */
    @RequestMapping("/list.do")
    public String query(QueryMap queryMap, Page page, ModelMap modelMap) {
        preProcessData(queryMap);

        int total = receiveService.getReceiveTotal(queryMap.getMap());
        page.setTotalRow(total);
        page.calculate();
        modelMap.addAttribute("page", page);
        if (total == 0) {
            modelMap.addAttribute("receiveList", new ArrayList<>(0));
        } else {
            List<Receive> receiveList = receiveService.getReceiveList(queryMap.getMap(), page);
            modelMap.addAttribute("receiveList", receiveList);
        }
        modelMap.addAllAttributes(queryMap.getMap());

        return "stock/rmaRecvList";
    }

    @RequestMapping(value = "/export.do")
    public ResponseEntity<?> export(QueryMap queryMap) {
        preProcessData(queryMap);
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            List<Map<String, String>> exportList = receiveService.exportQuery(queryMap.getMap());
            if (CollectionUtils.isEmpty(exportList)) {
                httpHeaders.add("Content-Type", "text/html; charset=utf-8");
                return new ResponseEntity<>("没有要导出的记录！", httpHeaders, HttpStatus.OK);
            }
            String templeteFile = System.getProperty("WEBCONTENT.BASE.PASH") + "export/rma_order_exp_template.xls";
            String tempFile = System.getProperty("WEBCONTENT.BASE.PASH") + "export/RMA_Order_" + System.currentTimeMillis() + ".xls";
            File file = ExcelExpUtil.expExcel(new ExcelModule(exportList), templeteFile, tempFile);
            FileSystemResource resource = new FileSystemResource(file);

            httpHeaders.add("Content-Disposition", "attachment; filename=\"" + file.getName() + "\"");
            httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

            return new ResponseEntity(resource, httpHeaders, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            httpHeaders.add("Content-Type", "text/html; charset=utf-8");
            return new ResponseEntity<>("下载出现异常！", httpHeaders, HttpStatus.OK);
        }
    }

    /**
     * 预处理结束日期
     * @param queryMap
     */
    private void preProcessData(QueryMap queryMap) {
        if (queryMap.getMap().isEmpty() || queryMap.getMap().containsKey("_")) {
            Calendar calendar = Calendar.getInstance();
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH) + 1;
            try {
                queryMap.getMap()
                    .put("preparedTimeBegin", new SimpleDateFormat("yyyyMMdd").parse(String.format("%s%s01", year, month)));
            } catch (ParseException e) {
                // ignore
                e.printStackTrace();
            }
        }
        if (queryMap.getMap().get("preparedTimeEnd") != null) {
            Date preparedTimeEnd = new Date(((Date) queryMap.getMap()
                .get("preparedTimeEnd")).getTime() + (24 * 3600 - 1) * 1000);
            queryMap.getMap().put("preparedTimeEnd", preparedTimeEnd);
        }
    }

    /**
     * @param rmaSalesOrder
     * @return
     */
    @RequestMapping("/create.do")
    @ResponseBody
    public Object create(@RequestBody RmaSalesOrder rmaSalesOrder) {
        try {
            receiveService.addRmaSalesOrder(rmaSalesOrder);

            return DwzMessage.success("操作成功！", null);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return DwzMessage.error("操作异常：" + e.getMessage(), null);
        }
    }

    /**
     * 查找IMEI
     * @param imei
     * @return
     */
    @RequestMapping("/lookup.do")
    @ResponseBody
    public Object lookupImei(String imei) {
        Indiv indiv = indivService.getIndivByCode(imei);
        if (indiv != null) {
            return indiv;
        }
        return "null";
    }
}
