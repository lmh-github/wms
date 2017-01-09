package com.gionee.top.entity;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

public class OrderGoodsDTO {
	private String skuCode;// 商品SKU编码
	private BigDecimal unitPrice;// 单价
	private Integer quantity;// 数量
	private BigDecimal subtotalPrice;// 价格小计

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
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

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
}
