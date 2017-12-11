package com.gionee.wms.web.action.report;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.DeliveryBatchStatus;
import com.gionee.wms.common.WmsConstants.IndivWaresStatus;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dto.DeliverySummary;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.DeliveryGoods;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.DeliveryService;
import com.gionee.wms.vo.DeliveryDetails;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionSupport;
import lombok.Cleanup;
import lombok.Data;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@Data
@Controller("DeliveryReportAction")
@Scope("prototype")
public class DeliveryReportAction extends ActionSupport {
    private static final long serialVersionUID = -5550025051973418931L;
    @Autowired
    private WarehouseService warehouseService;
    @Autowired
    private DeliveryService deliveryService;

    /** 汇总列表 */
    private List<DeliverySummary> summaryList;
    /** 明细列表 */
    private List<DeliveryGoods> goodsList;
    private Long warehouseId;
    private String skuCode;
    private String skuName;
    /** 仓库列表 */
    private List<Warehouse> warehouseList;
    /** 入库起始时间 */
    private Date finishedTimeBegin = new Date();
    /** 入库结束时间 */
    private Date finishedTimeEnd = new Date();
    private Page page = new Page();
    private List<DeliveryDetails> deliveryDetails;

    /**
     * 列表查询和导出
     * @return
     */
    public String listSalesDelyDetail() {
        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("finishedTimeBegin", finishedTimeBegin);
        criteria.put("finishedTimeEnd", finishedTimeEnd);

        Integer totalRow = deliveryService.queryDeliveryDetailsCount(criteria);

        criteria.put("page", page);
        page.setTotalRow(totalRow);
        page.calculate();

        if (totalRow == 0) {
            deliveryDetails = new ArrayList<>(0);
        }

        // 1 为导出标记
        if ("1".equals(ServletActionContext.getRequest().getParameter("exports"))) {
            // 一次导出4000条
            Page exportQueryPage = new Page();
            exportQueryPage.setStartRow(1);
            exportQueryPage.setEndRow(4000);
            criteria.put("page", exportQueryPage);
            deliveryDetails = deliveryService.queryDeliveryDetailsList(criteria);
            return export();
        } else {
            deliveryDetails = deliveryService.queryDeliveryDetailsList(criteria);
        }

        return "detail_salesDely";
    }

    private String export() {
        Map<String, String> orderSourceMap = Maps.newHashMap();
        for (WmsConstants.OrderSource em : WmsConstants.OrderSource.values()) {
            orderSourceMap.put(em.getCode(), em.getName());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        List<Map<String, String>> sheetData = new ArrayList<>();
        for (DeliveryDetails d : deliveryDetails) {
            Map<String, String> repeatData = new HashMap<>(16);
            repeatData.put("ORDER_SOURCE", orderSourceMap.get(d.getOrderSource()));
            repeatData.put("DELIVERY_DATE", sdf.format(d.getDeliveryDate()));
            repeatData.put("ORDER_CODE", d.getOrderCode());
            repeatData.put("PAYMENT", d.getPayment());
            repeatData.put("PAY_NO", d.getPayNo());
            repeatData.put("SKU_NAME", d.getSkuName());
            repeatData.put("QUANTITY", d.getQuantity().toString());
            repeatData.put("PRICE", d.getPrice().toString());
            repeatData.put("INVOICE_AMOUNT", d.getInvoiceAmount().toString());
            repeatData.put("CONSIGNEE", d.getConsignee());
            repeatData.put("EXPRESS_TYPE", d.getExpressType());
            repeatData.put("EXPRESS_NO", d.getExpressNo());
            repeatData.put("MATERIAL_CODE", d.getMaterialCode());
            repeatData.put("DEPARTURE", d.getDeparture());
            repeatData.put("ORDER_STATUS", WmsConstants.getOrderStatusZh(d.getOrderStatus()));

            sheetData.add(repeatData);
        }

        HttpServletResponse response = ServletActionContext.getResponse();
        // 清空输出流
        response.reset();
        // 设置响应头和下载保存的文件名
        response.setHeader("content-disposition", "attachment;filename=" + new SimpleDateFormat("yyyy-MM-dd_HHmmss").format(new Date()) + " delivery_details.xls");
        // 定义输出类型
        response.setContentType("APPLICATION/msexcel");
        try {
            String templeteFile = ActionUtils.getProjectPath() + "/export/delivey_detail_template.xls";
            String tempFile = ActionUtils.getProjectPath() + "/export/delivey_detail_tmp" + System.currentTimeMillis() + ".xls";
            File file = ExcelExpUtil.expExcel(new ExcelModule(sheetData), templeteFile, tempFile);
            @Cleanup OutputStream out = response.getOutputStream();
            FileUtils.copyFile(file, out);

            // 安静的删除临时文件
            FileUtils.deleteQuietly(new File(tempFile));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public String listSalesDelySummary() throws Exception {
        // 初始化属性对象
        warehouseList = warehouseService.getValidWarehouses();

        Map<String, Object> criteria = Maps.newHashMap();
        criteria.put("warehouseId", warehouseId);
        criteria.put("skuCode", StringUtils.defaultIfBlank(skuCode, null));
        criteria.put("skuName", StringUtils.defaultIfBlank(skuName, null));
        criteria.put("finishedTimeBegin", finishedTimeBegin);
        criteria.put("finishedTimeEnd", finishedTimeEnd);
        criteria.put("handlingStatus", DeliveryBatchStatus.FINISHED.getCode());
        criteria.put("waresStatus", IndivWaresStatus.NON_DEFECTIVE.getCode());
        int totalRow = deliveryService.getDeliverySummaryTotal(criteria);
        page.setTotalRow(totalRow);
        page.calculate();
        criteria.put("page", page);
        summaryList = deliveryService.getDeliverySummaryList(criteria, page);
        return "summary_salesDely";
    }

}
