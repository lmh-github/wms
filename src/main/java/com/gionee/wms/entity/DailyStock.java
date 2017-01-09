package com.gionee.wms.entity;

import java.util.Date;

public class DailyStock {
	Long id;// 主键
	String skuCode;// sku编码
	Date reportDate;// 记录时间
	Integer startStockQty;// 期初总库存
	Integer outStockQty;// 本期出库量
	Integer occupyStockQty;// 占用库存量
	Integer endStockQty;// 期末库存量
	Date createDate;// 创建时间
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public Date getReportDate() {
		return reportDate;
	}

	public void setReportDate(Date reportDate) {
		this.reportDate = reportDate;
	}

	public Integer getStartStockQty() {
		return startStockQty;
	}

	public void setStartStockQty(Integer startStockQty) {
		this.startStockQty = startStockQty;
	}

	public Integer getOutStockQty() {
		return outStockQty;
	}

	public void setOutStockQty(Integer outStockQty) {
		this.outStockQty = outStockQty;
	}

	public Integer getOccupyStockQty() {
		return occupyStockQty;
	}

	public void setOccupyStockQty(Integer occupyStockQty) {
		this.occupyStockQty = occupyStockQty;
	}

	public Integer getEndStockQty() {
		return endStockQty;
	}

	public void setEndStockQty(Integer endStockQty) {
		this.endStockQty = endStockQty;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
}
