package com.gionee.wms.entity;

public class CheckItem {
	private Long id;
	private String skuCode;
	private String skuName;
	private String indivCode;
	private Integer num;
	private Integer indivEnabled; // 是否管理个体身份编码
	private Long checkId;
	private String compareType; // 比较类型，1：盘点多出的数据 2：系统多出的数据
	private Integer waresStatus; // 商品状态 1:良品 2:次品
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getIndivCode() {
		return indivCode;
	}
	public void setIndivCode(String indivCode) {
		this.indivCode = indivCode;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	public Integer getIndivEnabled() {
		return indivEnabled;
	}
	public void setIndivEnabled(Integer indivEnabled) {
		this.indivEnabled = indivEnabled;
	}
	public Long getCheckId() {
		return checkId;
	}
	public void setCheckId(Long checkId) {
		this.checkId = checkId;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCompareType() {
		return compareType;
	}
	public void setCompareType(String compareType) {
		this.compareType = compareType;
	}

	public Integer getWaresStatus() {
		return waresStatus;
	}

	public void setWaresStatus(Integer waresStatus) {
		this.waresStatus = waresStatus;
	}
	
}
