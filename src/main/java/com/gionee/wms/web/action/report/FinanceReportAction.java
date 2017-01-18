package com.gionee.wms.web.action.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.SalesOutStat;
import com.gionee.wms.service.stat.OrderStatService;
import com.gionee.wms.web.action.AjaxActionSupport;
import com.google.common.collect.Maps;

@Controller("FinanceReportAction")
@Scope("prototype")
public class FinanceReportAction extends AjaxActionSupport {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1637737460875259719L;

	@Autowired
	private OrderStatService orderStatService;
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
	private List<SalesOutStat> statList;
	private Date shippingTimeBegin;// 入库起始时间
	private Date shippingTimeEnd;// 入库结束时间
	private String exports;
	
	private Page page = new Page();
	
	public String list() throws Exception {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0); 
		calendar.set(Calendar.SECOND, 0); 
		calendar.set(Calendar.MINUTE, 0); 
		calendar.set(Calendar.MILLISECOND, 0); 
		if(null==shippingTimeEnd) {
			shippingTimeEnd = calendar.getTime();
		}
		calendar.add(Calendar.DATE, -1);
		if(null==shippingTimeBegin) {
			shippingTimeBegin = calendar.getTime();
		}
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("shippingTimeBegin", shippingTimeBegin);
		criteria.put("shippingTimeEnd", shippingTimeEnd);
		int totalRow = orderStatService.getSalesOutStatTotal(criteria);
		if("1".equals(exports)) {
			page.setTotalRow(totalRow);
			page.setCurrentPage(1);
			page.setPageSize(Integer.MAX_VALUE);
			page.calculate();
			criteria.put("page", page);
			return exportReport(orderStatService.getSalesOutStatList(criteria, page));
		} else {
			page.setTotalRow(totalRow);
			page.calculate();
			criteria.put("page", page);
			statList = orderStatService.getSalesOutStatList(criteria, page);
		}
		return SUCCESS;
	}
	
	/**
	 * 导出订单信息
	 */
	private String exportReport(List<SalesOutStat> statList) {
		if (CollectionUtils.isEmpty(statList)) {
			logger.error("无导出无数据");
			return ERROR;
		}
		List<Map<String, String>> sheetData = new ArrayList<Map<String, String>>();
		for (SalesOutStat stat : statList) {
			Map<String, String> repeatData = new HashMap<String, String>();
			String date = null==stat.getShippingTime()?"":sdf.format(stat.getShippingTime());
			repeatData.put("OUT_TIME", date);
			repeatData.put("ORDER_CODE", stat.getOrderCode());
			repeatData.put("PAY_NO", stat.getPayNo());
			repeatData.put("SKU_NAME", stat.getSkuName());
			repeatData.put("MATERIAL_CODE", stat.getMaterialCode());
			repeatData.put("QUANTITY", stat.getQuantity()+"");
			repeatData.put("UNIT_PRICE", null==stat.getUnitPrice()?"":stat.getUnitPrice()+"");
			repeatData.put("ORDER_AMOUNT", null==stat.getOrderAmount()?"":stat.getOrderAmount()+"");
			if(null!=stat.getInvoiceStatus() && stat.getInvoiceStatus()==1) {
				repeatData.put("INVOICE_STATUS", "已开");
			} else {
				repeatData.put("INVOICE_STATUS", "未开");
			}
			repeatData.put("INVOICE_AMOUNT", null==stat.getInvoiceAmount()?"":stat.getInvoiceAmount()+"");
			repeatData.put("PARTNER_NAME", stat.getPartnerName());
			sheetData.add(repeatData);
		}
		ExcelModule excelModule = new ExcelModule(sheetData);
		HttpServletResponse response = ServletActionContext.getResponse();
		// 清空输出流
		response.reset();
		// 设置响应头和下载保存的文件名
		response.setHeader("content-disposition", "attachment;filename=finance_stat_list.xls");
		// 定义输出类型
		response.setContentType("APPLICATION/msexcel");
		OutputStream out = null;
		try {
			String templeteFile = ActionUtils.getProjectPath() + "/export/finance_exp_template.xls";
//			String tempFile = ActionUtils.getClassPath() + "config/excel/order_info_list.xls";
			String tempFile = ActionUtils.getProjectPath() + "/export/finance_stat_list.xls";
//			System.out.println(templeteFile + " " + tempFile);
			File file = ExcelExpUtil.expExcel(excelModule, templeteFile, tempFile);
			out = response.getOutputStream();
			FileUtils.copyFile(file, out);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		// 返回null,可防止报异常：getOutputStream() has already been called for this
		// response
		return null;
	}

	public List<SalesOutStat> getStatList() {
		return statList;
	}

	public void setStatList(List<SalesOutStat> statList) {
		this.statList = statList;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Date getShippingTimeBegin() {
		return shippingTimeBegin;
	}

	public void setShippingTimeBegin(Date shippingTimeBegin) {
		this.shippingTimeBegin = shippingTimeBegin;
	}

	public Date getShippingTimeEnd() {
		return shippingTimeEnd;
	}

	public void setShippingTimeEnd(Date shippingTimeEnd) {
		this.shippingTimeEnd = shippingTimeEnd;
	}

	public String getExports() {
		return exports;
	}

	public void setExports(String exports) {
		this.exports = exports;
	}
}
