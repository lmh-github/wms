package com.gionee.wms.web.action.report;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.WmsConstants.DeliveryBatchStatus;
import com.gionee.wms.common.WmsConstants.IndivWaresStatus;
import com.gionee.wms.dto.DeliverySummary;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.DeliveryGoods;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.DeliveryService;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionSupport;

@Controller("DeliveryReportAction")
@Scope("prototype")
public class DeliveryReportAction extends ActionSupport {
	private static final long serialVersionUID = -5550025051973418931L;
	@Autowired
	private WarehouseService warehouseService;
	@Autowired
	private DeliveryService deliveryService;

	/** 页面相关属性 **/
	private List<DeliverySummary> summaryList; // 汇总列表
	private List<DeliveryGoods> goodsList; // 明细列表
	private Long warehouseId;
	private String skuCode;
	private String skuName;
	private List<Warehouse> warehouseList;// 仓库列表
	private Date finishedTimeBegin;// 入库起始时间
	private Date finishedTimeEnd;// 入库结束时间
	private Page page = new Page();

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

	public String listSalesDelyDetail() throws Exception {
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
		int totalRow = deliveryService.getDeliveryGoodsTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		goodsList = deliveryService.getDeliveryGoodsByPage(criteria, page);
		return "detail_salesDely";
	}

	public Page getPage() {
		return page;
	}

	public List<DeliverySummary> getSummaryList() {
		return summaryList;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public void setFinishedTimeBegin(Date finishedTimeBegin) {
		this.finishedTimeBegin = finishedTimeBegin;
	}

	public void setFinishedTimeEnd(Date finishedTimeEnd) {
		this.finishedTimeEnd = finishedTimeEnd;
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

	public Date getFinishedTimeBegin() {
		return finishedTimeBegin;
	}

	public Date getFinishedTimeEnd() {
		return finishedTimeEnd;
	}

	public List<DeliveryGoods> getGoodsList() {
		return goodsList;
	}
}
