package com.gionee.wms.entity;

public class ReceiveGoods {
	private Long id;
	private Long skuId;
	private String skuCode;
	private String skuName;
	private Integer quantity;
	private String measureUnit;
	private String productBatchNo;
	private Integer indivEnabled; // 是否需绑定个体
	private Integer indivFinished;// 是否已绑定个体
	private Integer waresStatus; // 商品状态 1:良品 2:次品
	private Integer enabled;// 是否可用
	private String goodsSid;	// 商品管理skuId
	private Receive receive;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Receive getReceive() {
		return receive;
	}

	public void setReceive(Receive receive) {
		this.receive = receive;
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

	public String getProductBatchNo() {
		return productBatchNo;
	}

	public void setProductBatchNo(String productBatchNo) {
		this.productBatchNo = productBatchNo;
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

	public Integer getWaresStatus() {
		return waresStatus;
	}

	public void setWaresStatus(Integer waresStatus) {
		this.waresStatus = waresStatus;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public String getGoodsSid() {
		return goodsSid;
	}

	public void setGoodsSid(String goodsSid) {
		this.goodsSid = goodsSid;
	}

}
