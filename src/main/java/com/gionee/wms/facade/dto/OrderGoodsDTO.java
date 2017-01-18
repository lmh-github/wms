package com.gionee.wms.facade.dto;

import java.math.BigDecimal;

import org.apache.commons.lang.builder.ToStringBuilder;

public class OrderGoodsDTO {
	private String skuCode;// 商品SKU编码
	private BigDecimal unitPrice;// 单价
	private Integer quantity;// 数量
	private BigDecimal subtotalPrice;// 价格小计
	private String goodsSid;	// 具体到商品管理里的skuid
	/**
	 * 订单编号
	 */
	private String orderCode;
	
	/**
	 * 订单来源
	 */
	private String orderSource;
	
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

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public String getGoodsSid() {
		return goodsSid;
	}

	public void setGoodsSid(String goodsSid) {
		this.goodsSid = goodsSid;
	}
	
}
