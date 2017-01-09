package com.gionee.wms.entity;

import java.util.Date;

public class StockChange {
	private Long id;
	private String stockType;// 库存类型 1:可销售库存 2:占用库存 3:不可销售库存 4：总库存
	private String bizType;// 库存变动业务类型
	private Date createTime;// 发生时间
	private String originalCode;// 源单号
	private String opposite;// 业务对应方
	private Integer openingStock;// 期初结存
	private Integer quantity;// 变动数量
	private Integer closingStock;// 期后结余
	private String measureUnit;// 计量单位
	private Stock stock;// 库存

	// public StockDetail(Stock stock, String bizType, String originalCode,
	// Integer quantity) {
	// this.stock = stock;
	// this.bizType = bizType;
	// this.originalCode = originalCode;
	// this.quantity = quantity;
	// }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStockType() {
		return stockType;
	}

	public void setStockType(String stockType) {
		this.stockType = stockType;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getOriginalCode() {
		return originalCode;
	}

	public void setOriginalCode(String originalCode) {
		this.originalCode = originalCode;
	}

	public String getOpposite() {
		return opposite;
	}

	public void setOpposite(String opposite) {
		this.opposite = opposite;
	}

	public Integer getOpeningStock() {
		return openingStock;
	}

	public void setOpeningStock(Integer openingStock) {
		this.openingStock = openingStock;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Integer getClosingStock() {
		return closingStock;
	}

	public void setClosingStock(Integer closingStock) {
		this.closingStock = closingStock;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Stock getStock() {
		return stock;
	}

	public void setStock(Stock stock) {
		this.stock = stock;
	}

}
