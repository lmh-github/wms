package com.gionee.wms.entity;

public class IndivScanItem {
	private String indivCode;// 个体身份编码，如IMEI码或SKU编码
	private Integer waresStatus; // 商品状态 1:良品 2:次品
	private Integer indivEnabled; // 是否管理个体身份编码
	private String skuCode;
	private String skuName;
	private String warehouseName;
	private String transferTo;
	private String transferId;
	private Integer num;
	public String getIndivCode() {
		return indivCode;
	}
	public void setIndivCode(String indivCode) {
		this.indivCode = indivCode;
	}
	public Integer getWaresStatus() {
		return waresStatus;
	}
	public void setWaresStatus(Integer waresStatus) {
		this.waresStatus = waresStatus;
	}
	public Integer getIndivEnabled() {
		return indivEnabled;
	}
	public void setIndivEnabled(Integer indivEnabled) {
		this.indivEnabled = indivEnabled;
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
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public String getTransferTo() {
		return transferTo;
	}
	public void setTransferTo(String transferTo) {
		this.transferTo = transferTo;
	}
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	public Integer getNum() {
		return num;
	}
	public void setNum(Integer num) {
		this.num = num;
	}
	
}
