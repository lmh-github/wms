package com.gionee.wms.web.action.report;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.DailyStock;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.stock.StockService;
import com.opensymphony.xwork2.ActionSupport;

@Controller("DailyStockDetailAction")
@Scope("prototype")
public class DailyStockDetailAction extends ActionSupport {

	@Autowired
	private StockService stockService;
	private List<DailyStock> dailyStockList = new ArrayList<DailyStock>();
	private Date startDate;
	private Date endDate;
	private String skuCode;
	private Page page = new Page();

	public String execute() throws ParseException {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("startDate", startDate);
		criteria.put("endDate", endDate);
		criteria.put("skuCode", StringUtils.defaultIfBlank(skuCode, null));
		int totalCount = stockService.getDailyStockTotalCount(criteria);
		page.setTotalRow(totalCount);
		page.calculate();
		dailyStockList = stockService.getDailyStockList(criteria, page);
		return "success";
	}

	public String exportExcel() {
		dailyStockList = stockService.getDailyStockList();
		if (CollectionUtils.isEmpty(dailyStockList)) {
			throw new ServiceException("数据不存在");
		}
		List<Map<String, String>> sheetData = new ArrayList<Map<String, String>>();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		int count = 0;
		for (DailyStock item : dailyStockList) {
			Map<String, String> repeatData = new HashMap<String, String>();
			repeatData.put("SKU_CODE", item.getSkuCode());
			repeatData.put("REPORT_DATE", sdf.format(item.getReportDate()));
			repeatData.put("STARTSTOCKQTY",
					String.valueOf(item.getStartStockQty()));
			repeatData
					.put("OUTSTOCKQTY", String.valueOf(item.getOutStockQty()));
			repeatData.put("OCCUPYSTOCKQTY",
					String.valueOf(item.getOccupyStockQty()));
			repeatData
					.put("ENDSTOCKQTY", String.valueOf(item.getEndStockQty()));
			sheetData.add(repeatData);
			count++;
			if (count >= 10000) {
				break; // 大于10000条终止，防止内存溢出
			}
		}
		ExcelModule excelModule = new ExcelModule(sheetData);
		HttpServletResponse response = ServletActionContext.getResponse();
		// 清空输出流
		response.reset();
		// 设置响应头和下载保存的文件名
		response.setHeader("content-disposition",
				"attachment;filename=stock_dailyStock_list.xls");
		// 定义输出类型
		response.setContentType("APPLICATION/msexcel");
		OutputStream out = null;
		try {
			String templeteFile = ActionUtils.getProjectPath()
					+ "/export/stock_dailyStock_template.xls";
			String tempFile = ActionUtils.getProjectPath()
					+ "/export/stock_dailyStock_list.xls";
			File file = ExcelExpUtil.expExcel(excelModule, templeteFile,
					tempFile);
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
		return null;
	}
	public List<DailyStock> getDailyStockList() {
		return dailyStockList;
	}

	public void setDailyStockList(List<DailyStock> dailyStockList) {
		this.dailyStockList = dailyStockList;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
}
