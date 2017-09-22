package com.gionee.wms.web.controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.FileUtil;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.entity.UcUser;
import com.gionee.wms.entity.WorkOrder;
import com.gionee.wms.service.stock.UcUserService;
import com.gionee.wms.service.stock.WorkOrderService;
import com.gionee.wms.vo.ServiceCtrlMessage;
import com.gionee.wms.web.extend.DwzMessage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/6/6.
 */
@Controller
@RequestMapping("/workorder/")
public class WorkOrderController {
    private final Logger logger = LoggerFactory.getLogger(WorkOrderController.class);

    @Autowired
    private WorkOrderService workOrderService;
    @Autowired
    private UcUserService ucUserService;

    /**
     * @param modelMap
     * @param queryMap
     * @param page
     * @return
     */
    @RequestMapping("list.do")
    public String list(ModelMap modelMap, QueryMap queryMap, Page page) {
        int total = workOrderService.queryCount(queryMap.getMap());
        page.setTotalRow(total);
        page.calculate();

        List<WorkOrder> list;
        if (total == 0) {
            list = new ArrayList<>(0);
        } else {
            queryMap.put("page", page);
            list = workOrderService.query(queryMap.getMap());
        }

        QueryMap countQueryMap = new QueryMap();
        Page tmpPage = new Page();
        tmpPage.setPageSize(9999);
        tmpPage.setCurrentPage(1);
        tmpPage.setTotalRow(workOrderService.queryCount(countQueryMap.getMap()));
        tmpPage.calculate();
        countQueryMap.put("page", tmpPage);
        List<WorkOrder> workOrders = workOrderService.query(countQueryMap.getMap());
        Map<String, Integer> countMap = initCountMap(workOrders);

        modelMap.addAllAttributes(queryMap.getMap());
        modelMap.addAttribute("list", list);
        modelMap.addAttribute("page", page);
        modelMap.addAttribute("countMap", countMap);
        modelMap.addAttribute("currentUser", getCurrentUser().getUserName());
        return "workorder/list";
    }

    private Map<String, Integer> initCountMap(List<WorkOrder> workOrders) {
        Map<String, Integer> countMap = new HashMap<>();
        countMap.put("receive", 0);
        countMap.put("flowUp", 0);
        countMap.put("finish", 0);
        countMap.put("cancel", 0);
        if (!CollectionUtils.isEmpty(workOrders)) {
            String currentUserName = getCurrentUser().getUserName();
            for (WorkOrder workOrder : workOrders) {
                if ("待处理".equals(workOrder.getStatus()) && currentUserName.equals(workOrder.getWorker())) {
                    countMap.put("receive", countMap.get("receive") + 1);
                }
                if ("跟进中".equals(workOrder.getStatus()) && currentUserName.equals(workOrder.getWorker())) {
                    countMap.put("flowUp", countMap.get("flowUp") + 1);
                }
                if ("已完成".equals(workOrder.getStatus()) && currentUserName.equals(workOrder.getWorker())) {
                    countMap.put("finish", countMap.get("finish") + 1);
                }
                if ("已作废".equals(workOrder.getStatus())) {
                    countMap.put("cancel", countMap.get("cancel") + 1);
                }
            }
        }
        return countMap;
    }

    /**
     * @return
     */
    @RequestMapping("to_add.do")
    public String toAdd() {
        return "workorder/add";
    }

    /**
     * @param queryMap
     * @param workOrder
     * @return
     */
    @RequestMapping("save.json")
    @ResponseBody
    public Object save(QueryMap queryMap, WorkOrder workOrder, @RequestParam("files") MultipartFile[] files) {
        try {
            FileUtil.FileInfo[] filesInfo = FileUtil.upload(files);

            StringBuilder filesPath = new StringBuilder();
            for (FileUtil.FileInfo fileInfo : filesInfo) {
                filesPath.append(fileInfo == null ? "" : fileInfo.toString());
                filesPath.append("^_^");
            }

            if (filesPath.length() > 3) {
                workOrder.setFilesPath(filesPath.substring(0, filesPath.length() - 3));
            }

            UcUser ucUser = getCurrentUser();
            workOrder.setSponsor(ucUser.getUserName());
            ServiceCtrlMessage message = workOrderService.save(workOrder);
            return message.isResult() ? DwzMessage.success("", queryMap) : DwzMessage.error(message.getMessage(), queryMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("程序异常：【 " + e.getMessage() + "】", queryMap);
        }
    }

    @RequestMapping("/downZip.json")
    public ResponseEntity<?> downFiles(Long id, HttpServletRequest request) {
        WorkOrder workOrder = workOrderService.get(id);

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.add("Content-Type", "text/html; charset=utf-8");

        if (workOrder == null) {
            return new ResponseEntity<>("下载出现异常！", httpHeaders, HttpStatus.OK);
        }

        if (StringUtils.isEmpty(workOrder.getFilesPath())) {
            return new ResponseEntity<>("该记录未上传文件！", httpHeaders, HttpStatus.OK);
        }

        httpHeaders.clear();

        String[] filesPathArray = workOrder.getFilesPath().split("\\^_\\^");
        File[] files = new File[filesPathArray.length];
        String[] downName = new String[filesPathArray.length];

        for (int i = 0; i < filesPathArray.length; i++) {
            if (!StringUtils.isEmpty(filesPathArray[i])) {
                files[i] = new File(filesPathArray[i].split(",")[0]);
                downName[i] = filesPathArray[i].split(",")[1];
            }
        }

        String zipName = request.getSession().getServletContext().getRealPath("/") + id + ".zip";
        File file = FileUtil.downZip(files, downName, new File(zipName));

        FileSystemResource resource = new FileSystemResource(file);
        httpHeaders.add("Content-Disposition", "attachment; filename=\"" +  id + ".zip" + "\"");
        httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity(resource, httpHeaders, HttpStatus.OK);
    }

    private UcUser getCurrentUser() {
        return ucUserService.get(ActionUtils.getLoginName(), null);
    }

    /**
     * @param queryMap
     * @param id
     * @return
     */
    @RequestMapping("accept.do")
    @ResponseBody
    public Object accept(QueryMap queryMap, Long id) {
        try {
            ServiceCtrlMessage message = workOrderService.accept(id);
            return message.isResult() ? DwzMessage.success("接收完成！", queryMap) : DwzMessage.error(message.getMessage(), queryMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("程序异常：【 " + e.getMessage() + "】", queryMap);
        }
    }

    /**
     * @param modelMap
     * @param id
     * @param to
     * @return
     */
    @RequestMapping("to.do")
    public String to(ModelMap modelMap, Long id, String to) {
        if (id != null) {
            WorkOrder workOrder = workOrderService.get(id);
            modelMap.addAttribute("workOrder", workOrder);
        }
        return "workorder/" + to;
    }

    /**
     * @param queryMap
     * @param workOrder
     * @return
     */
    @RequestMapping("up.do")
    @ResponseBody
    public Object up(QueryMap queryMap, WorkOrder workOrder) {
        try {
            ServiceCtrlMessage message = workOrderService.up(workOrder);
            return message.isResult() ? DwzMessage.success("", queryMap) : DwzMessage.error(message.getMessage(), queryMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("程序异常：【 " + e.getMessage() + "】", queryMap);
        }
    }

    /**
     * @param queryMap
     * @param workOrder
     * @return
     */
    @RequestMapping("finish.do")
    @ResponseBody
    public Object finish(QueryMap queryMap, WorkOrder workOrder) {
        try {
            ServiceCtrlMessage message = workOrderService.finish(workOrder);
            return message.isResult() ? DwzMessage.success("", queryMap) : DwzMessage.error(message.getMessage(), queryMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("程序异常：【 " + e.getMessage() + "】", queryMap);
        }
    }

    /**
     * @param queryMap
     * @param workOrder
     * @return
     */
    @RequestMapping("cancel.do")
    @ResponseBody
    public Object cancel(QueryMap queryMap, WorkOrder workOrder) {
        try {
            ServiceCtrlMessage message = workOrderService.cancel(workOrder);
            return message.isResult() ? DwzMessage.success("", queryMap) : DwzMessage.error(message.getMessage(), queryMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("程序异常：【 " + e.getMessage() + "】", queryMap);
        }
    }

    /**
     * @param queryMap
     * @return
     */
    @RequestMapping("todo.do")
    @ResponseBody
    public Object queryToDo(QueryMap queryMap) {
        try {
            UcUser ucUser = getCurrentUser();

            queryMap.put("worker", ucUser.getUserName());
            queryMap.put("uper", ucUser.getUserName());

            int toDoCount = workOrderService.queryToDoCount(queryMap.getMap());
            return new ServiceCtrlMessage<>(true, "", toDoCount);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ServiceCtrlMessage<>(false, "查询出现异常！");
        }
    }

    /**
     * @param queryMap
     * @return
     */
    @RequestMapping(value = "/export.do")
    public ResponseEntity<?> export(QueryMap queryMap) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            List<Map<String, String>> exportList = workOrderService.exportQuery(queryMap.getMap());
            if (CollectionUtils.isEmpty(exportList)) {
                httpHeaders.add("Content-Type", "text/html; charset=utf-8");
                return new ResponseEntity<>("没有要导出的记录！", httpHeaders, HttpStatus.OK);
            }
            String templeteFile = System.getProperty("WEBCONTENT.BASE.PASH") + "export/workorder_template.xls";
            String tempFile = System.getProperty("WEBCONTENT.BASE.PASH") + "export/workorder_" + System.currentTimeMillis() + ".xls";
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
     * @param workOrder
     * @return
     */
    @RequestMapping("update.do")
    @ResponseBody
    public Object update(WorkOrder workOrder) {
        return workOrderService.update(workOrder);
    }

}
