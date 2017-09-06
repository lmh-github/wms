package com.gionee.wms.web.controller;

import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.excel.ExcelUtil;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.entity.*;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.TransferService;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import java.io.File;
import java.util.*;
import java.util.regex.Pattern;

import static com.gionee.wms.common.WmsConstants.TransferStatus.DELIVERYING;
import static com.gionee.wms.common.WmsConstants.TransferStatus.UN_DELIVERYD;

/**
 * 配货
 * Created by Pengbin on 2017/4/24.
 */
@Controller
@RequestMapping("/trans")
public class TransferController {

    private static Logger logger = LoggerFactory.getLogger(TransferController.class);

    @Autowired
    private TransferService transferService;
    @Autowired
    private WarehouseService warehouseService;

    /**
     * 列表查询
     *
     * @param modelMap
     * @param queryMap
     * @param page
     * @return
     */
    @RequestMapping("/list.do")
    public String list(ModelMap modelMap, QueryMap queryMap, Page page) {
        int total = transferService.getTransferListTotal(queryMap.getMap());
        page.setTotalRow(total);
        page.calculate();
        List<Transfer> transferList = transferService.getTransferList(queryMap.getMap(), page);

        modelMap.addAllAttributes(queryMap.getMap());
        modelMap.addAttribute("transferList", transferList);
        modelMap.addAttribute("page", page);

        return "transfer/transferList";
    }

    /**
     * 导出
     *
     * @param queryMap
     * @return
     */
    @RequestMapping(value = "/export.do")
    public ResponseEntity<?> export(QueryMap queryMap) {
        HttpHeaders httpHeaders = new HttpHeaders();
        try {
            Page page = new Page();
            page.setStartRow(0);
            page.setEndRow(10000);
            List<Map<String, String>> exportList = transferService.exportTransferList(queryMap.getMap(), page);
            if (CollectionUtils.isEmpty(exportList)) {
                httpHeaders.add("Content-Type", "text/html; charset=utf-8");
                return new ResponseEntity<>("没有要导出的记录！", httpHeaders, HttpStatus.OK);
            }
            String templeteFile = System.getProperty("WEBCONTENT.BASE.PASH") + "export/transfer_template.xls";
            String tempFile = System.getProperty("WEBCONTENT.BASE.PASH") + "export/transfer_" + System.currentTimeMillis() + ".xls";
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

    @RequestMapping("/toUp.do")
    public String toUpload() {
        return "transfer/file_transfer";
    }

    @RequestMapping("/upload.json")
    @ResponseBody
    public Object importExcel(MultipartFile multipartFile, QueryMap queryMap) {
        if (multipartFile == null) {
            return DwzMessage.error("上传出现异常！", null);
        }
        LinkedHashMap<String, String> mapping = new LinkedHashMap<>();
        mapping.put("2", "consignee");
        mapping.put("3", "transferTo");
        mapping.put("4", "contact");
        mapping.put("10", "remark");
        mapping.put("5", "array,goodsList,skuCode");
        mapping.put("9", "array,goodsList,unitPrice");
        mapping.put("8", "array,goodsList,quantity");
        String jsonStr = ExcelUtil.read(mapping, multipartFile, 2, 2, 0);
        JsonUtils jsonUtils = new JsonUtils();
        try {
            List<Transfer> transferList = transferService.convert(jsonUtils.jsonToList(jsonStr, Transfer.class));
            transferService.addBatch(transferList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error(e.getMessage(), queryMap);
        }
        return DwzMessage.success("上传成功！", queryMap);
    }

    /**
     * 取消
     *
     * @param queryMap
     * @param transferId
     * @return
     */
    @RequestMapping("/cancel.do")
    @ResponseBody
    public Object cancel(QueryMap queryMap, Long transferId) {
        try {
            transferService.cancelTransfer(transferId);
            return DwzMessage.success("操作成功！", queryMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("操作出现异常！", queryMap);
        }
    }

    @RequestMapping("/complete.do")
    @ResponseBody
    public Object complete(QueryMap queryMap, Long transferId, int flowType) {
        try {
            Transfer transfer = new Transfer();
            transfer.setTransferId(transferId);
            transfer.setFlowType(flowType == 0 ? "通过" : "不通过");
            transferService.updateTransfer(transfer);
            return DwzMessage.success("审核成功！", queryMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("审核成功出现异常！", queryMap);
        }
    }

    /**
     * 添加跳转
     *
     * @param modelMap   modelMap
     * @param transferId
     * @return
     */
    @RequestMapping("/toAddAndEdit.do")
    public String toAdd(ModelMap modelMap, Long transferId) {
        List<Warehouse> warehouseList = warehouseService.getValidWarehouses();
        List<TransferPartner> transferPartnerList = transferService.getTransferPartnerList(new HashMap<String, Object>());
        modelMap.addAttribute("warehouseList", warehouseList);
        modelMap.addAttribute("transferPartnerList", transferPartnerList);

        if (transferId != null) {
            Transfer transfer = transferService.getTransferById(transferId);
            modelMap.addAttribute("transfer", transfer);

            List<TransferGoods> goodsList = transferService.getTransferGoodsById(transferId);
            modelMap.addAttribute("goodsList", goodsList);
        }
        return "transfer/transferEdit";
    }

    /**
     * 添加或者修改
     *
     * @param queryMap
     * @param transfer
     * @return
     */
    @RequestMapping("/addAndEdit.do")
    @ResponseBody
    public Object addAndEdit(QueryMap queryMap, Transfer transfer) {
        try {
            if (transfer == null) {
                return DwzMessage.error("添加的信息为空！", queryMap);
            }
            if (transfer.getTransferId() == null) {
                transferService.addTransfer(transfer);
                return DwzMessage.success("添加成功！", queryMap);
            } else {
                // 状态校验
                if (!"已审核".equals(transferService.getTransferById(transfer.getTransferId()).getFlowType())) {
                    return DwzMessage.error("财务审核后方可正常流转！", queryMap);
                }
                transferService.updateTransfer(transfer);
                return DwzMessage.success("修改成功！", queryMap);
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("操作出现异常！", queryMap);
        }
    }

    /**
     * 选择商品跳转
     *
     * @param transferId
     * @return
     */
    @RequestMapping("/toTransferGoods.do")
    public Object toTransferGoods(ModelMap modelMap, Long transferId) {
        modelMap.addAttribute("transferId", transferId);
        return "transfer/transferGoods";
    }

    /**
     * 添加调拨商品
     *
     * @param queryMap
     * @param transferId
     * @param goods
     * @return
     */
    @RequestMapping("/addGoods.do")
    @ResponseBody
    public Object addGoods(QueryMap queryMap, Long transferId, TransferGoods goods) {
        try {

            transferService.addTransferGoods(transferService.getTransferById(transferId), goods);
            return DwzMessage.success("添加成功！", queryMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("操作出现异常！", queryMap);
        }
    }

    /**
     * 删除调拨商品
     *
     * @param queryMap
     * @param goodsId
     * @return
     */
    @RequestMapping("/delGoods.do")
    @ResponseBody
    public Object delGoods(QueryMap queryMap, Long goodsId) {
        try {
            transferService.deleteGoodsById(null, goodsId);
            return DwzMessage.success("删除成功！", queryMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("操作出现异常！", queryMap);
        }
    }

    /**
     * 删除调拨单
     *
     * @param queryMap
     * @param transferId
     * @return
     */
    @RequestMapping("/del.do")
    @ResponseBody
    public Object delete(QueryMap queryMap, Long transferId) {
        try {
            List<Indiv> indivList = transferService.getIndivList(transferId);
            if (CollectionUtils.isNotEmpty(indivList)) {
                return DwzMessage.error("调拨单在配货中，不能删除！", queryMap);
            }
            transferService.deleteTransfer(transferId);
            return DwzMessage.success("删除成功！", queryMap);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error("操作出现异常！", queryMap);
        }
    }

    /**
     * 配货跳转
     *
     * @param modelMap   modelMap
     * @param transferId 调货单ID
     * @return
     */
    @RequestMapping("/trans.do")
    @ResponseBody
    public Object trans(ModelMap modelMap, String transferId) {
        if (StringUtils.isBlank(transferId)) {
            return new ModelAndView("transfer/transPrepare");
        } else {
            if (Pattern.matches("\\d+", transferId)) {
                Transfer transfer = transferService.getTransferById(Long.valueOf(transferId));
                if (transfer == null) {
                    return new ServiceCtrlMessage<>(false, "调拨单：" + transferId + " 不存在！");
                }
                if (transfer.getOrderPushStatus() != null) {
                    return new ServiceCtrlMessage<>(false, "调拨单：" + transferId + " 不属于该模块！");
                }
                if (!Arrays.asList(UN_DELIVERYD.getCode(), DELIVERYING.getCode()).contains(transfer.getStatus())) {
                    return new ServiceCtrlMessage<>(false, "调拨单：" + transferId + " 已经发货或者取消！");
                }
                modelMap.put("transfer", transfer);
                load(modelMap, Long.valueOf(transferId));

                return new ModelAndView("transfer/trans", modelMap);
            }
            return new ServiceCtrlMessage<>(false, "输入的单号有误！");
        }
    }

    /**
     * 扫描配货
     *
     * @param sv         SKU|IMEI
     * @param num        数量
     * @param transferId 调拨单ID
     * @return
     */
    @RequestMapping("/scan.do")
    @ResponseBody
    public Object scan(ModelMap modelMap, String sv, Integer num, Long transferId) {
        try {
            if (StringUtils.isBlank(sv) || transferId == null) {
                return new ServiceCtrlMessage<>(false, "参数为空");
            }
            ServiceCtrlMessage message = transferService.picking(sv, num, transferId);
            if (message.isResult()) {
                Transfer transfer = transferService.getTransferById(transferId);
                modelMap.put("transfer", transfer);
                load(modelMap, Long.valueOf(transferId));
                return new ModelAndView("transfer/transGoodsFragment", modelMap);
            } else {
                return message;
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ServiceCtrlMessage<>(false, "操作出现异常：" + e.getMessage());
        }
    }

    /**
     * 完成配货，提交运单号，准备出库
     *
     * @param transferId 调拨单
     * @param logisticNo 运单号
     * @return
     */
    @RequestMapping("/dispatch.do")
    @ResponseBody
    public Object dispatch(Long transferId, String logisticNo) {
        try {
            return transferService.dispatch(transferId, logisticNo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ServiceCtrlMessage<>(false, "操作出现异常！");
        }
    }

    /**
     * 加载信息
     *
     * @param modelMap
     * @param transferId
     */
    private void load(ModelMap modelMap, Long transferId) {
        List<TransferGoods> goodsList = transferService.getTransferGoodsForPrep(transferId);
        modelMap.put("goodsList", goodsList);

        List<Indiv> indivList = transferService.getIndivList(transferId);
        modelMap.put("indivList", indivList);
    }

}
