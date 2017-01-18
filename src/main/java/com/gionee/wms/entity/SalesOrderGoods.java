package com.gionee.wms.entity;

import java.math.BigDecimal;

public class SalesOrderGoods {
	private Long id;
	private Long skuId; // SKU ID
	private String skuCode;// SKU 编码
	private String skuName;// SKU名称
	private BigDecimal unitPrice;// 单价
	private Integer quantity;// 数量
	private String measureUnit;// 计量单位
	private BigDecimal subtotalPrice;// 价格小计
	private Integer indivEnabled; // 是否需绑定个体
	private Integer preparedNum;
	private SalesOrder order;
	private String goodsSid;

	/**
	 * 订单来源
	 */
	private String orderSource;
	
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

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getSubtotalPrice() {
		return subtotalPrice;
	}

	public void setSubtotalPrice(BigDecimal subtotalPrice) {
		this.subtotalPrice = subtotalPrice;
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

	public SalesOrder getOrder() {
		return order;
	}

	public void setOrder(SalesOrder order) {
		this.order = order;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public Integer getPreparedNum() {
		return preparedNum;
	}

	public void setPreparedNum(Integer preparedNum) {
		this.preparedNum = preparedNum;
	}

	public String getGoodsSid() {
		return goodsSid;
	}

	public void setGoodsSid(String goodsSid) {
		this.goodsSid = goodsSid;
	}
	
}
