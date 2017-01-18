package com.gionee.wms.entity;

import java.util.List;

public class StockInItem {
	private Long id;
	private StockIn stockIn; // 入库单
	private Long skuId;
	private String skuCode;
	private String skuName;
	private Sku sku;// SKU
	private Integer quantity; // 入库数量
	private String measureUnit;// 计量单位
	private List<String> indivCodes;// 个体身份编码
	private String batchNumber; // 批次号
	private String remark;
	private Integer indivEnabled; // 是否需绑定个体
	private Integer indivFinished;//是否已绑定个体
	private String originalCode;//源单号
	private String waresStatus; // 商品状态 1:良品 2:次品

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StockIn getStockIn() {
		return stockIn;
	}

	public void setStockIn(StockIn stockIn) {
		this.stockIn = stockIn;
	}

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public List<String> getIndivCodes() {
		return indivCodes;
	}

	public void setIndivCodes(List<String> indivCodes) {
		this.indivCodes = indivCodes;
	}

	public String getBatchNumber() {
		return batchNumber;
	}

	public void setBatchNumber(String batchNumber) {
		this.batchNumber = batchNumber;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getIndivEnabled() {
		return indivEnabled;
	}

	public void setIndivEnabled(Integer indivEnabled) {
		this.indivEnabled = indivEnabled;
	}

	public Integer getIndivFinished() {
		return indivFinished;
	}

	public void setIndivFinished(Integer indivFinished) {
		this.indivFinished = indivFinished;
	}
	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public String getOriginalCode() {
		return originalCode;
	}

	public void setOriginalCode(String originalCode) {
		this.originalCode = originalCode;
	}
	public String getWaresStatus() {
		return waresStatus;
	}

	public void setWaresStatus(String waresStatus) {
		this.waresStatus = waresStatus;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
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
	
	
}
