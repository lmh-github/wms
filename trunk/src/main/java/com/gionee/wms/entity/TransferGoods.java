package com.gionee.wms.entity;

import java.math.BigDecimal;

public class TransferGoods {
	private Long id;
	private Long skuId;
	private String skuCode;
	private String skuName;
	private Integer quantity;
	
	//顺丰实收数量
	private Integer qty;
	
	private String measureUnit;
	private String transferId;
	private Integer indivEnabled;
	private Integer preparedNum;
	private Integer rmaNum;
	private BigDecimal unitPrice;// 单价
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getTransferId() {
		return transferId;
	}
	public void setTransferId(String transferId) {
		this.transferId = transferId;
	}
	public Integer getIndivEnabled() {
		return indivEnabled;
	}
	public void setIndivEnabled(Integer indivEnabled) {
		this.indivEnabled = indivEnabled;
	}
	public Integer getPreparedNum() {
		return preparedNum;
	}
	public void setPreparedNum(Integer preparedNum) {
		this.preparedNum = preparedNum;
	}
	public Integer getRmaNum() {
		return rmaNum;
	}
	public void setRmaNum(Integer rmaNum) {
		this.rmaNum = rmaNum;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Integer getQty() {
		return qty;
	}
	public void setQty(Integer qty) {
		this.qty = qty;
	}
	
}
