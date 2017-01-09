package com.gionee.wms.vo;

public class BackGoodsVo {
	private Long id;
	private String backCode;
	private String skuCode;
	private String skuName;
	private Integer quantity;
	private Integer nonDefectiveQuantity;
	private Integer defectiveQuantity;
	private String measureUnit;
	private Integer indivEnabled;
	private String goodsSid;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getBackCode() {
		return backCode;
	}
	public void setBackCode(String backCode) {
		this.backCode = backCode;
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
	public Integer getNonDefectiveQuantity() {
		return nonDefectiveQuantity;
	}
	public void setNonDefectiveQuantity(Integer nonDefectiveQuantity) {
		this.nonDefectiveQuantity = nonDefectiveQuantity;
	}
	public Integer getDefectiveQuantity() {
		return defectiveQuantity;
	}
	public void setDefectiveQuantity(Integer defectiveQuantity) {
		this.defectiveQuantity = defectiveQuantity;
	}
	public String getMeasureUnit() {
		return measureUnit;
	}
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}
	public Integer getIndivEnabled() {
		return indivEnabled;
	}
	public void setIndivEnabled(Integer indivEnabled) {
		this.indivEnabled = indivEnabled;
	}
	public String getGoodsSid() {
		return goodsSid;
	}
	public void setGoodsSid(String goodsSid) {
		this.goodsSid = goodsSid;
	}
	
}
