package com.gionee.wms.web.action.report;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
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

@Controller("ReceiveReportAction")
@Scope("prototype")
public class ReceiveReportAction extends ActionSupport {
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
	private List<Warehouse> warehouseList;// 仓库列表
	private Date finishedTimeBegin;// 入库起始时间
	private Date finishedTimeEnd;// 入库结束时间
	private Page page = new Page();

	public String listPurRecvSummary() throws Exception {
		// 初始化属性对象
		warehouseList = warehouseService.getValidWarehouses();

		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("warehouseId", warehouseId);
		criteria.put("skuCode", StringUtils.defaultIfBlank(skuCode, null));
		criteria.put("skuName", StringUtils.defaultIfBlank(skuName, null));
		criteria.put("finishedTimeBegin", finishedTimeBegin);
		criteria.put("finishedTimeEnd", finishedTimeEnd);
		criteria.put("handlingStatus", StockInStatus.FINISHED.getCode());
		criteria.put("waresStatus", IndivWaresStatus.NON_DEFECTIVE.getCode());
		criteria.put("receiveType", ReceiveType.PURCHASE.getCode());
		int totalRow = receiveService.getReceiveSummaryTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		summaryList = receiveService.getReceiveSummaryList(criteria, page);
		return "summary_purRecv";
	}

	public String listOrderInFact() throws Exception {
		// 初始化属性对象
		warehouseList = warehouseService.getValidWarehouses();

		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("warehouseId", warehouseId);
		criteria.put("skuCode", StringUtils.defaultIfBlank(skuCode, null));
		criteria.put("skuName", StringUtils.defaultIfBlank(skuName, null));
		criteria.put("finishedTimeBegin", finishedTimeBegin);
		criteria.put("finishedTimeEnd", finishedTimeEnd);
		criteria.put("handlingStatus", StockInStatus.FINISHED.getCode());
		criteria.put("waresStatus", IndivWaresStatus.NON_DEFECTIVE.getCode());
		criteria.put("receiveType", ReceiveType.PURCHASE.getCode());
		int totalRow = receiveService.getReceiveGoodsTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		goodsList = receiveService.getReceiveGoodsByPage(criteria, page);
		return "detail_purRecv";
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

}
