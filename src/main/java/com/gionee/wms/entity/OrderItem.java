package com.gionee.wms.entity;

import java.math.BigDecimal;

public class OrderItem {
	private Long id;//wms_stock_out_detail表的主键
	private String orderCode;
	private BigDecimal unitPrice;// 单价
	private Integer quantity;// 数量
	private String measureUnit;// 计量单位
	private BigDecimal subtotalPrice;// 价格小计
	private SalesOrder salesOrder; // 销售订单
	private Sku sku;// 商品SKU
	private StockOut stockOut;
	private String remark;
	private Integer indivEnabled; // 是否需绑定个体
	private Integer indivFinished;//是否已绑定个体

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public BigDecimal getUnitPrice() {
		return unitPrice;
	}

	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
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

	public SalesOrder getSalesOrder() {
		return salesOrder;
	}

	public void setSalesOrder(SalesOrder salesOrder) {
		this.salesOrder = salesOrder;
	}

	public Sku getSku() {
		return sku;
	}

	public void setSku(Sku sku) {
		this.sku = sku;
	}

	public StockOut getStockOut() {
		return stockOut;
	}

	public void setStockOut(StockOut stockOut) {
		this.stockOut = stockOut;
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

}
