package com.gionee.wms.web.action.stock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
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
import com.gionee.wms.common.WmsConstants.StockBizType;
import com.gionee.wms.common.WmsConstants.StockType;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.StockChange;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.StockService;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionSupport;

@Controller("StockChangeAction")
@Scope("prototype")
public class StockChangeAction extends ActionSupport {
	private static final long serialVersionUID = -5550025051973418931L;

	private StockService stockService;
	private WarehouseService warehouseService;

	/** 页面相关属性 **/
	private List<StockChange> changeList; // 库存流水列表
	private Long warehouseId;
	private Long skuId;
	private String skuCode;
	private String skuName;
	private String stockType;
	private String bizType;
	private List<Warehouse> warehouseList;// 仓库列表
	private Date createTimeBegin;// 起始时间
	private Date createTimeEnd;// 结束时间
	private Page page = new Page();
	private String exports;

	@Override
	public String execute() throws Exception {
		// 初始化属性对象
		warehouseList = warehouseService.getValidWarehouses();
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("warehouseId", warehouseId);
		criteria.put("skuId", skuId);
		criteria.put("skuCode", StringUtils.defaultIfBlank(skuCode, null));
		criteria.put("skuName", StringUtils.defaultIfBlank(skuName, null));
		criteria.put("stockType", StringUtils.defaultIfBlank(stockType, null));
		criteria.put("bizType", StringUtils.defaultIfBlank(bizType, null));
		criteria.put("createTimeBegin", createTimeBegin);
		criteria.put("createTimeEnd", createTimeEnd);

		if ("1".equals(exports)) {
			Page exportPage = new Page();
			exportPage.setStartRow(0);
			exportPage.setEndRow(10000);
			changeList = stockService.getStockChangeList(criteria, exportPage);
			return download();
		} else {
			int totalRow = stockService.getStockChangeListTotal(criteria);
			page.setTotalRow(totalRow);
			page.calculate();
			criteria.put("page", page);
			changeList = stockService.getStockChangeList(criteria, page);
			return SUCCESS;
		}
	}

	/**
	 * 导出
	 * @return
	 */
	private String download() {
		if (CollectionUtils.isEmpty(changeList)) {
			throw new ServiceException("没有数据！");
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		List<Map<String, String>> sheetData = new ArrayList<Map<String, String>>();
		int count = 0;
		for (StockChange stockChange : changeList) {
			Map<String, String> repeatData = new HashMap<String, String>();
			repeatData.put("SKUCODE", stockChange.getStock().getSku().getSkuCode());
			repeatData.put("SKUNAME", stockChange.getStock().getSku().getSkuName());
			repeatData.put("WAREHOUSENAME", stockChange.getStock().getWarehouse().getWarehouseName());
			repeatData.put("STOCKTYPE", getStockTypeName(stockChange.getStockType()));
			repeatData.put("CREATETIME", sdf.format(stockChange.getCreateTime()));
			repeatData.put("BIZTYPE", getStockBizTypeName(stockChange.getBizType()));
			repeatData.put("ORIGINALCODE", stockChange.getOriginalCode());
			repeatData.put("OPENINGSTOCK", stockChange.getOpeningStock().toString());
			repeatData.put("QUANTITY", stockChange.getQuantity().toString());
			repeatData.put("CLOSINGSTOCK", stockChange.getClosingStock().toString());

			if (count++ >= 10000) {
				break; // 大于10000条终止，防止内存溢出
			}

			sheetData.add(repeatData);

		}

		ExcelModule excelModule = new ExcelModule(sheetData);
		HttpServletResponse response = ServletActionContext.getResponse();
		// 清空输出流
		response.reset();
		// 设置响应头和下载保存的文件名
		response.setHeader("content-disposition", "attachment;filename=stock_change" + System.currentTimeMillis() + ".xls");
		// 定义输出类型
		response.setContentType("APPLICATION/msexcel");
		OutputStream out = null;
		try {
			String templeteFile = ActionUtils.getProjectPath() + "/export/stock_change_exp_template.xls";
			String tempFile = ActionUtils.getProjectPath() + "/export/stock_change" + System.currentTimeMillis() + ".xls";
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

		return null;
	}

	private String getStockTypeName(String code) {
		if (code == null) {
			return StringUtils.EMPTY;
		}
		for (StockType e : StockType.values()) {
			if (e.getCode().equals(code)) {
				return e.getName();
			}
		}
		return StringUtils.EMPTY;
	}

	private String getStockBizTypeName(String code) {
		if (code == null) {
			return StringUtils.EMPTY;
		}
		for (StockBizType e : StockBizType.values()) {
			if (e.getCode().equals(code)) {
				return e.getName();
			}
		}
		return StringUtils.EMPTY;
	}

	public Page getPage() {
		return page;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public String getSkuName() {
		return skuName;
	}

	public List<Warehouse> getWarehouseList() {
		return warehouseList;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public Date getCreateTimeBegin() {
		return createTimeBegin;
	}

	public void setCreateTimeBegin(Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	public StockService getStockService() {
		return stockService;
	}

	public List<StockChange> getChangeList() {
		return changeList;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getExports() {
		return exports;
	}

	public void setExports(String exports) {
		this.exports = exports;
	}

	@Autowired
	public void setWarehouseService(WarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

	@Autowired
	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

}
