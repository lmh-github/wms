package com.gionee.wms.web.action.report;

import java.io.File;
import java.io.FileInputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gionee.wms.common.ActionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.WmsConstants.IndivWaresStatus;
import com.gionee.wms.common.WmsConstants.ReceiveType;
import com.gionee.wms.common.WmsConstants.StockInStatus;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.ReceiveSummary;
import com.gionee.wms.entity.ReceiveGoods;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.ReceiveService;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionSupport;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller("UserAreaAction")
@Scope("prototype")
public class UserAreaAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
    private static final long serialVersionUID = 1665826659435853485L;
    @Autowired
    private ReceiveService receiveService;
    @Autowired
    private WarehouseService warehouseService;

    /**
     * 页面相关属性 *
     */
    private List<ReceiveSummary> summaryList; // 汇总列表
    private List<ReceiveGoods> goodsList; // 明细列表
    private Long warehouseId;
    private String skuCode;
    private String skuName;
    private List<Warehouse> warehouseList;// 仓库列表
    private Date finishedTimeBegin;// 入库起始时间
    private Date finishedTimeEnd;// 入库结束时间

    private Page page = new Page();


    private Date createTimeBegin;

    private Date createTimeEnd;

    private String s_province;
    private String s_city;
    private List<Map<String, Object>> userAreaList;
    protected Logger logger = LoggerFactory.getLogger(getClass());

    //请求环境变量
    protected HttpServletRequest request;
    //响应环境变量
    protected HttpServletResponse response;

    public String listPurRecvSummary() throws Exception {
        // 初始化属性对象
        //warehouseList = warehouseService.getValidWarehouses();

        Map<String, Object> criteria = Maps.newHashMap();
        String province = null;
        String city = null;
        s_province= StringUtils.defaultIfBlank(s_province, null);
        s_city= StringUtils.defaultIfBlank(s_city, null);
        if (s_province != null) {
            if (s_province.equals("省份")) {
                province = s_province = null;
            } else {
                province = s_province.replace("省", "");
                province = province.replace("市", "");
            }
            if (s_city.equals("地级市")) {
                city = s_city = null;
            } else {
                city = s_city.replace("市", "");
            }
        }
        criteria.put("province", province);
        criteria.put("city", city);
        criteria.put("createTimeBegin", createTimeBegin);
        criteria.put("createTimeEnd", createTimeEnd);

        if (!"1".equals(request.getParameter("exports"))) {

            int totalRow = receiveService.getUserAreaTotle(criteria);
            page.setTotalRow(totalRow);
            page.calculate();
            criteria.put("page", page);
            userAreaList = receiveService.getUserAreaList(criteria, page);
            return "summary_purRecv";
        } else {
            export(criteria);
            return null;
        }
    }


    public void export(Map<String, Object> criteria) {
        try {
            List<Map<String, Object>> userAreaList = receiveService.getUserAreaForCascade(criteria);

            HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(ActionUtils.getProjectPath() + "/export/userarea_template.xls")));
            HSSFSheet sheet = workbook.getSheetAt(0);

            for (int i = 0, j = 0; i < userAreaList.size(); i++, j = 0) {

                HSSFRow newRow = sheet.createRow(i + 1);
                newRow.createCell(j++).setCellValue(userAreaList.get(i).get("PROVINCE") + ""); //订单号
                newRow.createCell(j++).setCellValue(userAreaList.get(i).get("CITY") + ""); // skuName
                newRow.createCell(j++).setCellValue(userAreaList.get(i).get("DISTRICT") + ""); //平台
                newRow.createCell(j++).setCellValue(userAreaList.get(i).get("SKU_NAME") + ""); // 品质
                newRow.createCell(j++).setCellValue(userAreaList.get(i).get("NUM") + ""); // SKU


                //String statuName="";
                // .equals("1")?"已创建":trans
                // 630ferRmoList.get(i).get("status").equals("0")?"无效":"已收货"

                //newRow.createCell(j++).setCellValue(statuName); // 状态
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

    public String listOrderInFact() throws Exception {
        // 初始化属性对象
        //warehouseList = warehouseService.getValidWarehouses();

        Map<String, Object> criteria = Maps.newHashMap();
        String province = null;
        String city = null;

        if (s_province != null) {
            if (s_province.equals("省份")) {
                province = s_province = null;
            } else {
                province = s_province.replace("省", "");
                province = province.replace("市", "");
            }
            if (s_city.equals("地级市")) {
                city = s_city = null;
            } else {
                city = s_city.replace("市", "");
            }
        }
        criteria.put("province", province);
        criteria.put("city", city);
        criteria.put("createTimeBegin", createTimeBegin);
        criteria.put("createTimeEnd", createTimeEnd);

        if (!"1".equals(request.getParameter("exports"))) {

            int totalRow = receiveService.getUserAreaTotle(criteria);
            page.setTotalRow(totalRow);
            page.calculate();
            criteria.put("page", page);
            userAreaList = receiveService.getUserAreaList(criteria, page);
            return "summary_purRecv";
        } else {
            export(criteria);
            return null;
        }
    }

    public static void main(String[] args) {


        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-mm-dd");


        Date a = null;
        Date b = null;
        try {
            a = fmt.parse("2016-01-01");
            b = fmt.parse("2016-10-10");
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Calendar timea = Calendar.getInstance();
        Calendar timeb = Calendar.getInstance();

        timea.setTime(a);
        timea.setTime(a);


        System.out.println(a);
        System.out.println(b);
    }

    public List<ReceiveSummary> getSummaryList() {
        return summaryList;
    }

    public void setSummaryList(List<ReceiveSummary> summaryList) {
        this.summaryList = summaryList;
    }

    public List<ReceiveGoods> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<ReceiveGoods> goodsList) {
        this.goodsList = goodsList;
    }

    public Long getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Long warehouseId) {
        this.warehouseId = warehouseId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public List<Warehouse> getWarehouseList() {
        return warehouseList;
    }

    public void setWarehouseList(List<Warehouse> warehouseList) {
        this.warehouseList = warehouseList;
    }

    public Date getFinishedTimeBegin() {
        return finishedTimeBegin;
    }

    public void setFinishedTimeBegin(Date finishedTimeBegin) {
        this.finishedTimeBegin = finishedTimeBegin;
    }

    public Date getFinishedTimeEnd() {
        return finishedTimeEnd;
    }

    public void setFinishedTimeEnd(Date finishedTimeEnd) {
        this.finishedTimeEnd = finishedTimeEnd;
    }

    public Page getPage() {
        return page;
    }

    public void setPage(Page page) {
        this.page = page;
    }

    public void setCreateTimeBegin(Date createTimeBegin) {
        this.createTimeBegin = createTimeBegin;
    }

    public void setCreateTimeEnd(Date createTimeEnd) {
        this.createTimeEnd = createTimeEnd;
    }

    public Date getCreateTimeBegin() {
        return createTimeBegin;
    }

    public Date getCreateTimeEnd() {
        return createTimeEnd;
    }

    public void setS_province(String s_province) {
        this.s_province = s_province;
    }

    public void setS_city(String s_city) {
        this.s_city = s_city;
    }

    public String getS_province() {
        return s_province;
    }

    public String getS_city() {
        return s_city;
    }

    public void setUserAreaList(List<Map<String, Object>> userAreaList) {
        this.userAreaList = userAreaList;
    }

    public List<Map<String, Object>> getUserAreaList() {
        return userAreaList;
    }


    public void setServletRequest(HttpServletRequest request) {
        this.request = request;
    }

    public void setServletResponse(HttpServletResponse response) {
        this.response = response;
    }
}
