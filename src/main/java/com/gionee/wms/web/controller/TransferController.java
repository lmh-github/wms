package com.gionee.wms.web.controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.OneBarcodeUtil;
import com.gionee.wms.common.excel.ExcelUtil;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.QueryMap;
import com.gionee.wms.dto.ShiroUser;
import com.gionee.wms.entity.*;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.account.AccountService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

import static com.gionee.wms.common.WmsConstants.TransferStatus.DELIVERYING;
import static com.gionee.wms.common.WmsConstants.TransferStatus.UN_DELIVERYD;

/**
 * 调货|配货|分仓
 * @author XXX
 */
@Controller
@RequestMapping("/trans")
public class TransferController {

    private static Logger logger = LoggerFactory.getLogger(TransferController.class);

    @Autowired
    private TransferService transferService;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private AccountService accountService;

    /**
     * 列表查询
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
    public String toUpload(Integer type, HttpServletRequest request) {
        request.setAttribute("type", type);
        request.setAttribute("rel", request.getParameter("rel"));
        return "transfer/file_transfer";
    }

    @RequestMapping("/upload.json")
    @ResponseBody
    public Object importExcel(MultipartFile multipartFile, Integer type, QueryMap queryMap) throws IOException {
        if (multipartFile == null) {
            return DwzMessage.error("上传出现异常！", null);
        }
        LinkedHashMap<String, String> mapping = new LinkedHashMap<>();
        String jsonStr;
        if (null != type && type == 1) {
            mapping.put("1", "transferTo");
            mapping.put("2", "consignee");
            mapping.put("3", "contact");
            mapping.put("8", "remark");
            mapping.put("4", "array,goodsList,skuCode");
            mapping.put("6", "array,goodsList,quantity");
            mapping.put("7", "array,goodsList,unitPrice");
            jsonStr = ExcelUtil.read(mapping, multipartFile.getInputStream(), multipartFile.getOriginalFilename(), 1, 0, 0);

        } else {
            mapping.put("2", "consignee");
            mapping.put("3", "transferTo");
            mapping.put("4", "contact");
            mapping.put("10", "remark");
            mapping.put("5", "array,goodsList,skuCode");
            mapping.put("9", "array,goodsList,unitPrice");
            mapping.put("8", "array,goodsList,quantity");
            jsonStr = ExcelUtil.read(mapping, multipartFile.getInputStream(), multipartFile.getOriginalFilename(), 2, 2, 0);

        }
        JsonUtils jsonUtils = new JsonUtils();
        try {
            List<Transfer> transferList = transferService.convert(jsonUtils.jsonToList(jsonStr, Transfer.class), type);
            transferService.addBatch(transferList);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return DwzMessage.error(e.getMessage(), queryMap);
        }
        return DwzMessage.success("上传成功！", queryMap);
    }

    /**
     * 取消
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
     * @param modelMap   modelMap
     * @param transferId 调货单ID
     * @param type       SF顺丰|null
     * @return
     */
    @RequestMapping("/trans{type}.do")
    @ResponseBody
    public Object trans(ModelMap modelMap, String transferId, @PathVariable("type") String type) {
        if (StringUtils.isBlank(transferId)) {
            return new ModelAndView("transfer/transPrepare");
        } else {
            if (Pattern.matches("\\d+", transferId)) {
                Transfer transfer = transferService.getTransferById(Long.valueOf(transferId));
                if (transfer == null) {
                    return new ServiceCtrlMessage<>(false, "调拨单：【" + transferId + "】不存在！");
                }
                if ("SF".equals(type)) {
                    if (transfer.getOrderPushStatus() == null) {
                        return new ServiceCtrlMessage<>(false, "调拨单：【" + transferId + "】不属于该模块！");
                    }
                    Warehouse warehouse = warehouseService.getWarehouse(Long.valueOf(transfer.getTransferTo()));
                    modelMap.put("transferTo", warehouse.getWarehouseName());
                } else {
                    // 顺丰调货单
                    if (transfer.getOrderPushStatus() != null) {
                        return new ServiceCtrlMessage<>(false, "调拨单：【" + transferId + "】不属于该模块！");
                    }
                    modelMap.put("transferTo", transfer.getTransferTo());
                }
                if (!Arrays.asList(UN_DELIVERYD.getCode(), DELIVERYING.getCode()).contains(transfer.getStatus())) {
                    return new ServiceCtrlMessage<>(false, "调拨单：" + transferId + " 已经发货或者取消！");
                }
                modelMap.put("transfer", transfer);
                modelMap.put("type", type);
                load(modelMap, Long.valueOf(transferId));

                return new ModelAndView("transfer/trans", modelMap);
            }
            return new ServiceCtrlMessage<>(false, "输入的单号有误！");
        }
    }

    /**
     * 扫描配货
     * @param sv         SKU|IMEI
     * @param num        数量
     * @param transferId 调拨单ID
     * @param isImei     是否是IMEI扫描
     * @return
     */
    @RequestMapping("/scan.do")
    @ResponseBody
    public Object scan(ModelMap modelMap, String sv, Integer num, Long transferId, Boolean isImei) {
        try {
            if (StringUtils.isBlank(sv) || transferId == null) {
                return new ServiceCtrlMessage<>(false, "参数为空！");
            }
            ServiceCtrlMessage message = transferService.picking(sv, num, transferId, isImei);
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
     * @param transferId 调拨单
     * @param logisticNo 运单号
     * @param type       SF顺丰|null
     * @return
     */
    @RequestMapping("/dispatch{type}.do")
    @ResponseBody
    public Object dispatch(Long transferId, String logisticNo, @PathVariable("type") String type) {
        try {
            return transferService.dispatch(transferId, logisticNo, type);
        }catch ( ServiceException e) {
            logger.error(e.getMessage(), e);
            return new ServiceCtrlMessage<>(false, e.getMessage());
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            return new ServiceCtrlMessage<>(false, "操作出现异常");
        }
    }

    /**
     * 加载信息
     * @param modelMap
     * @param transferId
     */
    private void load(ModelMap modelMap, Long transferId) {
        List<TransferGoods> goodsList = transferService.getTransferGoodsForPrep(transferId);
        modelMap.put("goodsList", goodsList);

        List<Indiv> indivList = transferService.getIndivList(transferId);
        modelMap.put("indivList", indivList);
    }

    /**
     * 打印
     * @param modelMap
     * @param transferId
     * @return
     */
    @RequestMapping({"/print.do"})
    public String print(ModelMap modelMap, Long transferId) {
        Transfer transfer = this.transferService.getTransferById(transferId);
        List<TransferGoods> goodsList = this.transferService.getTransferGoodsById(transferId);
        modelMap.put("transfer", transfer);
        modelMap.put("goodsList", goodsList);
        String barCodePath = ActionUtils.getProjectPath() + "/barCodeTransferTemp/";
        String fileName = OneBarcodeUtil.generateBar(String.valueOf(transferId), barCodePath);
        if (fileName != null) {
            String barCodeImgPath = "/barCodeTransferTemp/" + fileName;
            modelMap.put("barCodeImgPath", barCodeImgPath);
        }

        return "transfer/printTransfer";
    }

}
