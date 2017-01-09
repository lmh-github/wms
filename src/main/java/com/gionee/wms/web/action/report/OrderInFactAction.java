package com.gionee.wms.web.action.report;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.excel.excelexport.util.DateUtil;
import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.ReceiveSummary;
import com.gionee.wms.entity.ReceiveGoods;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.ReceiveService;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionSupport;
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

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller("OrderInFactAction")
@Scope("prototype")
public class OrderInFactAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {
	private static final long serialVersionUID = 1665826659435853485L;
	@Autowired
	private ReceiveService receiveService;
	@Autowired
	private WarehouseService warehouseService;



	/** 页面相关属性 **/
	private List<ReceiveSummary> summaryList; // 汇总列表
	private List<ReceiveGoods> goodsList; // 明细列表
	private Long warehouseId;
	private String skuCode;
	private String skuName;
	private String orderCode;
	private String orderStatus;

	private Page page = new Page();


	private Date createTimeBegin;
	private Date createTimeEnd;



	private List<Map<String,Object>> orderInFactList;
	protected Logger logger = LoggerFactory.getLogger(getClass());

	//请求环境变量
	protected HttpServletRequest request;
	//响应环境变量
	protected HttpServletResponse response;

	public String listPurRecvSummary() throws Exception {
		// 初始化属性对象
		//warehouseList = warehouseService.getValidWarehouses();

		Map<String, Object> criteria = Maps.newHashMap();

		criteria.put("createTimeBegin",createTimeBegin);
		criteria.put("createTimeEnd", createTimeEnd);
		criteria.put("skuCode",StringUtils.defaultIfBlank(skuCode, null));
		criteria.put("skuName",StringUtils.defaultIfBlank(skuName, null));
		criteria.put("orderCode",StringUtils.defaultIfBlank(orderCode, null));
		criteria.put("orderStatus",StringUtils.defaultIfBlank(orderStatus, null));



		if (!"1".equals(request.getParameter("exports"))) {
			int totalRow = receiveService.orderInFactTotle(criteria);
			page.setTotalRow(totalRow);
			page.calculate();
			criteria.put("page", page);
			orderInFactList= receiveService.orderInFactList(criteria, page);
			for(int i=0;i<orderInFactList.size();i++){
				Map<String,Object> map=new HashMap<String, Object>();


				Date payfor_time=DateUtil.stringToDate(orderInFactList.get(i).get("PAYFOR_TIME") + "");
				Date shaidanTime=DateUtil.stringToDate(orderInFactList.get(i).get("SHAIDAN_TIME") + "");
				Date dadanTime=DateUtil.stringToDate(orderInFactList.get(i).get("DADAN_TIME")+"");
				Date chukuTime=DateUtil.stringToDate(orderInFactList.get(i).get("CHUKU_TIME")+"");
				Date qianshou_time=DateUtil.stringToDate(orderInFactList.get(i).get("QIANSHOU_TIME")+"");


				String SHAIDAN_DADAN= DateUtil.betweenTime(shaidanTime, dadanTime); //筛单-打单
				String DADAN_CHUKU= DateUtil.betweenTime(dadanTime,chukuTime);//打单-出库
				String HEJI_TIME= DateUtil.betweenTime(dadanTime, chukuTime);//合计时间
				String DINGDAN_TIME= DateUtil.betweenTime(payfor_time, qianshou_time);//订单时长

				orderInFactList.get(i).put("SHAIDAN_DADAN",SHAIDAN_DADAN);
				orderInFactList.get(i).put("DADAN_CHUKU",DADAN_CHUKU);
				orderInFactList.get(i).put("HEJI_TIME",HEJI_TIME);
				orderInFactList.get(i).put("DINGDAN_TIME",DINGDAN_TIME);

			}


			return "summary_purRecv";
		}else{
			export(criteria);
			return null;
		}
	}


	public void export(Map<String, Object> criteria) {
		try {
			List<Map<String,Object>> orderInFactList = receiveService.orderInFactCascade(criteria);

			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(ActionUtils.getProjectPath() + "/export/orderInFact_template.xls")));
			HSSFSheet sheet = workbook.getSheetAt(0);




			for(int i=0;i<orderInFactList.size();i++){
				Map<String,Object> map=new HashMap<String, Object>();


				Date payfor_time=DateUtil.stringToDate(orderInFactList.get(i).get("PAYFOR_TIME") + "");
				Date shaidanTime=DateUtil.stringToDate(orderInFactList.get(i).get("SHAIDAN_TIME") + "");
				Date dadanTime=DateUtil.stringToDate(orderInFactList.get(i).get("DADAN_TIME")+"");
				Date chukuTime=DateUtil.stringToDate(orderInFactList.get(i).get("CHUKU_TIME")+"");
				Date qianshou_time=DateUtil.stringToDate(orderInFactList.get(i).get("QIANSHOU_TIME")+"");


				String SHAIDAN_DADAN= DateUtil.betweenTime(shaidanTime, dadanTime); //筛单-打单
				String DADAN_CHUKU= DateUtil.betweenTime(dadanTime,chukuTime);//打单-出库
				String HEJI_TIME= DateUtil.betweenTime(dadanTime, chukuTime);//合计时间
				String DINGDAN_TIME= DateUtil.betweenTime(payfor_time, qianshou_time);//订单时长

				orderInFactList.get(i).put("SHAIDAN_DADAN",SHAIDAN_DADAN);
				orderInFactList.get(i).put("DADAN_CHUKU",DADAN_CHUKU);
				orderInFactList.get(i).put("HEJI_TIME",HEJI_TIME);
				orderInFactList.get(i).put("DINGDAN_TIME",DINGDAN_TIME);

			}

			Map<String, String> orderSourceMap = Maps.newHashMap();
			for (WmsConstants.OrderSource em : WmsConstants.OrderSource.values()) {
				orderSourceMap.put(em.getCode(), em.getName());
			}
			for (int i = 0, j = 0; i < orderInFactList.size(); i++, j = 0) {

				HSSFRow newRow = sheet.createRow(i + 1);
				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("ORDER_CODE")+""); //订单号

				String orderSource=  orderSourceMap.get( orderInFactList.get(i).get("ORDER_SOURCE")+"");

				newRow.createCell(j++).setCellValue(orderSource); //订单来源(平台)
				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("ORDER_TIME")==null?"":orderInFactList.get(i).get("ORDER_TIME")+""); //订单时间
				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("PAYFOR_TIME")==null?"":orderInFactList.get(i).get("PAYFOR_TIME")+""); //支付时间

				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("SHAIDAN_TIME")==null?"":orderInFactList.get(i).get("SHAIDAN_TIME")+""); //筛单时间
				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("DADAN_TIME")==null?"":orderInFactList.get(i).get("DADAN_TIME")+""); //打单时间
				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("CHUKU_TIME")==null?"":orderInFactList.get(i).get("CHUKU_TIME")+""); //出库时间
				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("QIANSHOU_TIME")==null?"":orderInFactList.get(i).get("QIANSHOU_TIME")+""); //签收时间


				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("SHAIDAN_DADAN")+""); //筛单-打单时长
				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("DADAN_CHUKU")+""); //打单-出库时长
				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("HEJI_TIME")+""); //合计物流时长
				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("DINGDAN_TIME")+""); //订单时长


				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("SKU_NAME")+""); //支付时间
				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("ADDRESS")+""); //订单号

				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("ORDERAMOUNT")+""); //订单号
				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("SHIPPINGNAME")+""); // skuName
				newRow.createCell(j++).setCellValue(orderInFactList.get(i).get("SHIPPINGNO")+""); //平台

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
		String province=null;
		String city=null;

		criteria.put("province", province);
		criteria.put("city", city);
		criteria.put("createTimeBegin",createTimeBegin);
		criteria.put("createTimeEnd",createTimeEnd);

		if (!"1".equals(request.getParameter("exports"))) {

			int totalRow = receiveService.getUserAreaTotle(criteria);
			page.setTotalRow(totalRow);
			page.calculate();
			criteria.put("page", page);
			orderInFactList= receiveService.orderInFactList(criteria, page);
			return "summary_purRecv";
		}else{
			export(criteria);
			return null;
		}
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

	public String getOrderStatus() {
		return orderStatus;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
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



	public void setOrderInFactList(List<Map<String, Object>> orderInFactList) {
		this.orderInFactList = orderInFactList;
	}

	public List<Map<String, Object>> getOrderInFactList() {
		return orderInFactList;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
}
