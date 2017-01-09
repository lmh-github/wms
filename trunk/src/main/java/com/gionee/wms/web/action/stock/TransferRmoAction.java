/**
 * Project Name:wms
 * File Name:TransferRmoAction.java
 * Package Name:com.gionee.wms.web.action.stock
 * Date:2015年11月2日下午3:54:58
 * Copyright (c) 2015 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.web.action.stock;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.BackStatus;
import com.gionee.wms.common.WmsConstants.IndivStockStatus;
import com.gionee.wms.common.WmsConstants.LogType;
import com.gionee.wms.common.WmsConstants.OrderSource;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.StockRequest;
import com.gionee.wms.entity.*;
import com.gionee.wms.facade.request.OperateOrderRequest;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.ShippingService;
import com.gionee.wms.service.log.LogService;
import com.gionee.wms.service.stock.BackService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.gionee.wms.service.stock.StockService;
import com.gionee.wms.service.stock.TransferRmoService;
import com.gionee.wms.service.wares.IndivService;
import com.gionee.wms.vo.BackGoodsVo;
import com.gionee.wms.web.action.AjaxActionSupport;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.ServletOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;

/**
 * @author PengBin 00001550<br>
 * @date 2015年11月2日 下午3:54:58
 */
@Controller("TransferRmoAction")
@Scope("prototype")
public class TransferRmoAction extends AjaxActionSupport implements Preparable {

    /**
     * serialVersionUID
     */
    private static final long serialVersionUID = 7676595262526937L;

    @Autowired
    private TransferRmoService transferRmoService;

    @Autowired
    private BackService backService;


    @Autowired
    private SalesOrderService salesOrderService;

    @Autowired
    private ShippingService shippingService;

    @Autowired
    private IndivService indivService;

    @Autowired
    private LogService logService;

    private Page page = new Page();

    /**
     * 退换货单号
     */
    private String transferCode;
    /**
     * 订单号
     */
    private String orderCode;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 物流单号
     */
    private String shippingNo;
    /**
     * 起始时间
     */
    private Date backTimeBegin;
    /**
     * 起始时间
     */
    private Date backTimeEnd;

    /**
     * 串号
     */
    private String indivCode;

    /**
     * 上传的文件
     */
    private File upload;
    /**
     * 上传的文件类型
     */
    private String uploadContentType;
    /**
     * 上传的文件名
     */
    private String uploadFileName;

    private Date backedTimeBegin;

    private Date backedTimeEnd;

    private Date createTimeBegin;

    private Date createTimeEnd;

    private Back orderBack;

    private TransferRemove transferRemove;

    private List<TransferRemoveDetail> transferRemoveDetailList;


    private List<BackGoods> goodsList;
    private List<IndivFlow> indivList; // 接收页面输入的串号

    /**
     * {@inheritDoc}
     */
    @Override
    public String execute() throws Exception {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("transferCode", trimToNull(transferCode));
        criteria.put("shippingNo", trimToNull(shippingNo));
        criteria.put("createTimeBegin", createTimeBegin);
        criteria.put("createTimeEnd", createTimeEnd);
        criteria.put("status", StringUtils.trimToNull(ServletActionContext.getRequest().getParameter("status")));
        criteria.put("quality", StringUtils.trimToNull(ServletActionContext.getRequest().getParameter("quality")));
        criteria.put("backPlatform", StringUtils.trimToNull(ServletActionContext.getRequest().getParameter("backPlatform")));
        if (!"1".equals(request.getParameter("exports"))) {
            int totalRow = transferRmoService.getTransferTotal(criteria);

            page.setTotalRow(totalRow);
            page.calculate();
            criteria.put("page", page);
            List<TransferRemove> backList = transferRmoService.getTransferList(criteria, page);
            if (backList != null) {
                Map<String, Object> goodsQueryMap = new HashMap<String, Object>(1);
                for (TransferRemove back : backList) {
                    goodsQueryMap.put("transferCode", back.getTransferCode());
                    List<TransferRemoveDetail> goodsList2 = transferRmoService.getTransferRemoveDetailList(goodsQueryMap);
                    if (goodsList2 != null) {
                        List<TransferRemoveDetail> goods = new ArrayList<TransferRemoveDetail>(goodsList2.size());
                        back.setBackGoods(goods);
                        for (TransferRemoveDetail v : goodsList2) {
                            TransferRemoveDetail g = new TransferRemoveDetail();
                            g.setSkuCode(v.getSkuCode());
                            g.setSkuName(v.getSkuName());
                            goods.add(g);
                        }
                    }
                }
            }
            ActionContext.getContext().getValueStack().set("backList", backList);
            ActionContext.getContext().getValueStack().push(criteria);
            return SUCCESS;
        } else {
            export(criteria);
            return null;
        }
    }

    public void export(Map<String, Object> criteria) {
        try {
            List<Map<String,Object>> transferRmoList = transferRmoService.getTransferRemoveForCascade(criteria);



            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(ActionUtils.getProjectPath() + "/export/transferRmo_template.xls")));
            HSSFSheet sheet = workbook.getSheetAt(0);





            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");


            for (int i = 0, j = 0; i < transferRmoList.size(); i++, j = 0) {
                Map<String,Object> map = transferRmoList.get(i);

                 HSSFRow newRow = sheet.createRow(i + 1);
                newRow.createCell(j++).setCellValue(transferRmoList.get(i).get("transferCode")+""); //订单号
                newRow.createCell(j++).setCellValue(transferRmoList.get(i).get("skuName")+""); // skuName
                newRow.createCell(j++).setCellValue(transferRmoList.get(i).get("backPlatform")+""); //平台
                newRow.createCell(j++).setCellValue(transferRmoList.get(i).get("quality").equals("0")?"次品":"良品"); // 品质
                newRow.createCell(j++).setCellValue(transferRmoList.get(i).get("remark")==null?"":transferRmoList.get(i).get("remark")+""); // SKU
                newRow.createCell(j++).setCellValue(transferRmoList.get(i).get("createBy") + ""); // 退换类型
                newRow.createCell(j++).setCellValue(transferRmoList.get(i).get("createTime") + ""); // 退换货备注

                int status= Integer.parseInt(transferRmoList.get(i).get("status")+"") ;
                String statuName="";
              // .equals("1")?"已创建":trans
              // 630ferRmoList.get(i).get("status").equals("0")?"无效":"已收货"
               if(status==1){
                   statuName="已创建";
               }else if(status==2){
                   statuName="已收货";
               }else{
                   statuName="无效";
               }
                newRow.createCell(j++).setCellValue(statuName); // 状态
            }

            // 清空输出流
            response.reset();
            // 设置响应头和下载保存的文件名
            response.setHeader("content-disposition", "attachment;filename=back_" + System.currentTimeMillis() + ".xls");
            // 定义输出类型
            response.setContentType("APPLICATION/msexcel");
            ServletOutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);

            outputStream.flush();
            try {
                outputStream.close();
            } catch (Exception e1) {
                e1.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String input() throws Exception {
        List<Shipping> shippings = shippingService.getValidShippings();
        ActionContext.getContext().getValueStack().set("shippings", shippings);
        return INPUT;
    }



    /**
     * 跳转确认收货页面
     *
     * @return
     */
    public String toConfirm() {
        HashMap<String, Object> criteria = Maps.newHashMap();
        criteria.put("transferCode", transferCode);
        Back backOrder = backService.getBack(criteria); // 查询出退货单
        List<BackGoodsVo> backGoodsList = backService.getBackGoodsList(criteria); // 详细单
        SalesOrder salesOrder = salesOrderService.getSalesOrderByCode(backOrder.getOrderCode()); // 订单

        // 查询发货imei
        ArrayList<Object> itemList = Lists.newArrayList();
        criteria.clear();
        criteria.put("orderCode", backOrder.getOrderCode());
        List<Indiv> indivList = indivService.getIndivList(criteria);
        for (Indiv indiv : indivList) {
            IndivScanItem item = new IndivScanItem();
            item.setIndivCode(indiv.getIndivCode());
            item.setSkuCode(indiv.getSkuCode());
            item.setSkuName(indiv.getSkuName());
            item.setNum(1);
            itemList.add(item);
        }

        ActionContext.getContext().getValueStack().set("backOrder", backOrder);
        ActionContext.getContext().getValueStack().set("backGoodsList", backGoodsList);
        ActionContext.getContext().getValueStack().set("salesOrder", salesOrder);
        ActionContext.getContext().getValueStack().set("itemList", itemList);

        return "confirmBack";
    }

    /**
     * 上传
     *
     * @return
     */
    public String toUpload() {
        return "upload";
    }

    /**
     * 退货验收
     */
    public void confirm() {
        try {
            backService.handleConfirmBack(orderBack);

            for (BackStatus s : BackStatus.values()) {
                if (orderBack.getBackStatus() == s.getCode()) {
                    //	addLog("调拨退货：" + orderBack.gettransferCode(), "单据状态变更为：" + s.getName());
                }
            }
            ajaxSuccess("操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("退货验收出现异常！", e);
            ajaxError("操作出现异常！");
        }
    }


    @Autowired
    private StockService stockService;

    /**
     * 修改
     */
    public void update() {
        try {
            backService.handleUpdate(orderBack);

            //addLog("调拨退货：" + orderBack.gettransferCode(), "修改单据！");

            ajaxSuccess("操作成功！");
        } catch (Exception e) {
            e.printStackTrace();
            ajaxError("操作出现异常！");
        }
    }

    /**
     * 新增
     *
     * @return
     */
    public void add() {
        try {
            if (CollectionUtils.isEmpty(transferRemoveDetailList)) {
                ajaxError("请添加商品！");
            }

            if (("null").equals(transferRemoveDetailList)) {
                ajaxError("请添加商品！");
            }
            // 根据SKU去除重复商品
            List<String> skuList = Lists.newArrayList();
            for (Iterator<TransferRemoveDetail> iterator = transferRemoveDetailList.iterator(); iterator.hasNext(); ) {
                TransferRemoveDetail backGoods = iterator.next();
                if (skuList.contains(backGoods.getSkuCode())) {
                    iterator.remove();
                    continue;
                }
                skuList.add(backGoods.getSkuCode());
            }

            transferRemove.setCreateBy(ActionUtils.getLoginName()); // 操作人

            transferRemove.setCreateTime(new Date());
            transferRmoService.addTransfer(transferRemove, transferRemoveDetailList);

            addLog("调拨退货：" + transferRemove.getTransferCode(), "新增退换货单！");

            ajaxSuccess("创建成功！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("新增退换货出现异常！", e);
            ajaxError("操作出现异常！");
        }
    }

    /**
     * 查找订单
     */
    public void lookupSalesOrder() {
        try {
            Map<String, Object> criteria = Maps.newHashMap();
            criteria.put("orderCode", orderCode);
            List<Back> backList = backService.getBackList(criteria, Page.getPage(1, 20, 20)); // 校验订单是否已经申请了退换货
            for (Back b : backList) {
                if (b.getBackStatus().intValue() != BackStatus.CANCELED.getCode()) {
                    ajaxError("该订单已经申请了退换货，请不要重复申请！");
                    return;
                }
            }

            SalesOrder salesOrder = salesOrderService.getSalesOrderByCode(orderCode);
            if (salesOrder == null) {
                ajaxError("订单号：" + orderCode + "，不存在！");
                return;
            }

			/*if (salesOrder.getOrderStatus() != OrderStatus.RECEIVED.getCode() && salesOrder.getOrderStatus() != OrderStatus.SHIPPED.getCode()) {
                ajaxError("未出库或者未签收状态的订单不能申请退换货！");
				return;
			}*/

            List<SalesOrderGoods> goodsList = salesOrderService.getOrderGoodsList(salesOrder.getId());
            if (goodsList != null) {
                for (SalesOrderGoods salesOrderGoods : goodsList) {
                    salesOrderGoods.setOrder(null);
                }
            }

            Map<String, Object> root = Maps.newHashMap();
            root.put("salesOrder", salesOrder);
            root.put("goodsList", goodsList);
            ajaxObject(root);
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("退换货查找订单出现异常！", e);
            ajaxError("程序出现异常！");
        }
    }

    /**
     * 查看
     *
     * @return
     */
    public String toView() {
        HashMap<String, Object> criteria = Maps.newHashMap();
        criteria.put("transferCode", transferCode);

        TransferRemove transferRemoveOrder = transferRmoService.getTransferRemove(criteria);// 查询出退货单 transferRemove

        List<TransferRemoveDetail> TransferRemoveDetailList = transferRmoService.getTransferRemoveDetailList(criteria);


        criteria.clear();
        criteria.put("flowCode", transferCode);

        List<Log> logs = null;
        try {
            Map<String, Object> logParams = Maps.newHashMap();
            logParams.put("opName_hit", "调拨退货：" + transferCode);
            logParams.put("endRow", 100);
            logParams.put("startRow", 1);
            logs = logService.selectPagedLogs(logParams);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ActionContext.getContext().getValueStack().set("transferRemoveOrder", transferRemoveOrder);
        ActionContext.getContext().getValueStack().set("backGoodsList", TransferRemoveDetailList);
        ActionContext.getContext().getValueStack().set("logs", logs == null ? new ArrayList<Log>() : logs);

        return "view";
    }

    /**
     * 查找串号
     */
    public void lookupIndiv() {
        try {
            Indiv indiv = indivService.getIndivByCode(indivCode);
            if (indiv == null) {
                ajaxError("串号不存在!");
                return;
            }

            Integer stockStatus = indiv.getStockStatus();
            if (IndivStockStatus.OUT_WAREHOUSE.getCode() != stockStatus) { // 非出库状态的串号拒绝录入
                ajaxError("串号：" + indivCode + " 已经在库或者出库中，不能操作！");
                return;
            }

            // 提示：没有做串号和退货订单的完全关联
            ajaxObject(indiv);
        } catch (ServiceException e) {
            e.printStackTrace();
            logger.error("退换货查找串号出现异常！", e);
            ajaxError("程序出现异常！");
        }
    }

    /**
     * 取消/确认收货
     * 退货单
     */
    public void cancel() {
        try {
            //OperateOrderRequest request = new OperateOrderRequest();
            //request.setOrderCode(transferCode);
            //backService.cancelBack(request);
            TransferRemove transferRemove = new TransferRemove();
            transferRemove.setTransferCode(transferCode);
            transferRemove.setStatus(status);

            if (status == 0) {
                transferRmoService.updateTransferRemove(transferRemove);
                addLog("调拨退货：" + transferCode, "取消退换货单！");
                ajaxSuccess("取消成功！");
            }
            if (status == 2) {
                Map<String, Object> map = new HashMap<String, Object>();
                map.put("transferCode", transferCode);
                List<TransferRemoveDetail> list = transferRmoService.getTransferRemoveDetailList(map);
                if (list.size() > 0) {
                    for(TransferRemoveDetail e:list){
                        StockRequest stockRequest = new StockRequest((long) 1643,
                                e.getSkuCode(), WmsConstants.StockType.STOCK_SALES,e.getNum(),
                                WmsConstants.StockBizType.IN_BACK_TRANSFER, transferCode);
                        stockService.increaseStock(stockRequest, true);
                    }
                    transferRmoService.updateTransferRemove(transferRemove);
                    addLog("调拨退货：" + transferCode, "确定退换货单！");
                    ajaxSuccess("操作成功！");
                }else{
                    addLog("调拨退货：" + transferCode, "取消退换货单！");
                    ajaxSuccess("SKU编码");
                }



            }

        } catch (Exception e) {
            e.printStackTrace();
            if (status == 0) {
                logger.error("取消退换货单出现异常！", e);

            } else {
                logger.error("确认退换货单出现异常！", e);
            }
            ajaxError("程序出现异常！");
        }
    }

    /**
     * 删除
     */
    public void delete() {
        try {
            backService.handleDelete(transferCode);

            addLog("调拨退货：" + transferCode, "删除退换货单！");
            ajaxSuccess("删除成功！");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("删除退换货单出现异常！", e);
            ajaxError("程序出现异常！");
        }
    }

    @Override
    public void prepare() throws Exception {
    }

    /**
     * 写入操作日志
     *
     * @param opName
     * @param content
     */
    private void addLog(String opName, String content) {
        try {
            Log log = new Log();
            log.setIp(ActionUtils.getRemoteAddr(request));
            log.setOpTime(new Date());
            log.setOpUserName(ActionUtils.getLoginName());
            log.setType(LogType.OP_LOG.getCode());
            log.setOpName(opName);
            log.setContent(content);

            logService.insertLog(log);
        } catch (Exception e) {
            // ignore exception
            e.printStackTrace();
        }
    }

    public String getTransferCode() {
        return transferCode;
    }

    public void setTransferCode(String transferCode) {
        this.transferCode = transferCode;
    }

    /**
     * @return the orderCode
     */
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * @param orderCode the orderCode
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * @return the shippingNo
     */
    public String getShippingNo() {
        return shippingNo;
    }

    /**
     * @param shippingNo the shippingNo
     */
    public void setShippingNo(String shippingNo) {
        this.shippingNo = shippingNo;
    }

    /**
     * @return the backTimeBegin
     */
    public Date getBackTimeBegin() {
        return backTimeBegin;
    }

    /**
     * @param backTimeBegin the backTimeBegin
     */
    public void setBackTimeBegin(Date backTimeBegin) {
        this.backTimeBegin = backTimeBegin;
    }

    /**
     * @return the backTimeEnd
     */
    public Date getBackTimeEnd() {
        return backTimeEnd;
    }

    /**
     * @param backTimeEnd the backTimeEnd
     */
    public void setBackTimeEnd(Date backTimeEnd) {
        this.backTimeEnd = backTimeEnd;
    }

    /**
     * @return the orderBack
     */
    public Back getOrderBack() {
        return orderBack;
    }

    /**
     * @param orderBack the orderBack
     */
    public void setOrderBack(Back orderBack) {
        this.orderBack = orderBack;
    }

    /**
     * @return the goodsList
     */
    public List<BackGoods> getGoodsList() {
        return goodsList;
    }

    /**
     * @param goodsList the goodsList
     */
    public void setGoodsList(List<BackGoods> goodsList) {
        this.goodsList = goodsList;
    }

    /**
     * @return the page
     */
    public Page getPage() {
        return page;
    }

    /**
     * @param page the page
     */
    public void setPage(Page page) {
        this.page = page;
    }

    /**
     * @return the indivCode
     */
    public String getIndivCode() {
        return indivCode;
    }

    /**
     * @param indivCode the indivCode
     */
    public void setIndivCode(String indivCode) {
        this.indivCode = indivCode;
    }

    /**
     * @return the indivList
     */
    public List<IndivFlow> getIndivList() {
        return indivList;
    }

    /**
     * @param indivList the indivList
     */
    public void setIndivList(List<IndivFlow> indivList) {
        this.indivList = indivList;
    }

    /**
     * @return the upload
     */
    public File getUpload() {
        return upload;
    }

    /**
     * @param upload the upload
     */
    public void setUpload(File upload) {
        this.upload = upload;
    }

    /**
     * @return the uploadContentType
     */
    public String getUploadContentType() {
        return uploadContentType;
    }

    /**
     * @param uploadContentType the uploadContentType
     */
    public void setUploadContentType(String uploadContentType) {
        this.uploadContentType = uploadContentType;
    }

    /**
     * @return the uploadFileName
     */
    public String getUploadFileName() {
        return uploadFileName;
    }

    /**
     * @param uploadFileName the uploadFileName
     */
    public void setUploadFileName(String uploadFileName) {
        this.uploadFileName = uploadFileName;
    }

    /**
     * @return the backedTimeBegin
     */
    public Date getBackedTimeBegin() {
        return backedTimeBegin;
    }

    /**
     * @param backedTimeBegin the backedTimeBegin
     */
    public void setBackedTimeBegin(Date backedTimeBegin) {
        this.backedTimeBegin = backedTimeBegin;
    }

    /**
     * @return the backedTimeEnd
     */
    public Date getBackedTimeEnd() {
        return backedTimeEnd;
    }

    /**
     * @param backedTimeEnd the backedTimeEnd
     */
    public void setBackedTimeEnd(Date backedTimeEnd) {
        this.backedTimeEnd = backedTimeEnd;
    }

    /**
     * @return the createTimeBegin
     */
    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    /**
     * @param createTimeBegin the createTimeBegin
     */
    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    /**
     * @return the createTimeEnd
     */
    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    /**
     * @param createTimeEnd the createTimeEnd
     */
    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }


    public TransferRmoService getTransferRmoService() {
        return transferRmoService;
    }

    public TransferRemove getTransferRemove() {
        return transferRemove;
    }

    public List<TransferRemoveDetail> getTransferRemoveDetailList() {
        return transferRemoveDetailList;
    }

    public void setTransferRmoService(TransferRmoService transferRmoService) {
        this.transferRmoService = transferRmoService;
    }

    public void setTransferRemoveDetailList(List<TransferRemoveDetail> transferRemoveDetailList) {
        this.transferRemoveDetailList = transferRemoveDetailList;
    }

    public void setTransferRemove(TransferRemove transferRemove) {
        this.transferRemove = transferRemove;
    }


}
