package com.gionee.wms.dto;

public class ReceiveSummary {
	private Long warehouseId;
	private String warehouseName;
	private String stockInCode;
	private String skuCode;
	private String skuName;
	private Long skuId;
	private Integer waresStatus;// 1:良品，2：次品,对应WMS_STOCKIN_GOODS和WMS_STOCK_OUT_DETAIL表的WARES_STATUS字段
	private String measureUnit;
	private String goodsSid;
	private Integer quantity;

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getStockInCode() {
		return stockInCode;
	}

	public void setStockInCode(String stockInCode) {
		this.stockInCode = stockInCode;
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

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Integer getWaresStatus() {
		return waresStatus;
	}

	public void setWaresStatus(Integer waresStatus) {
		this.waresStatus = waresStatus;
	}

	public String getGoodsSid() {
		return goodsSid;
	}

	public void setGoodsSid(String goodsSid) {
		this.goodsSid = goodsSid;
	}

}
