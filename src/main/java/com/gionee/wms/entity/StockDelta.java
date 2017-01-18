package com.gionee.wms.entity;

import java.util.Date;

public class StockDelta {
	private String id;
	private String stockType;// 库存类型
	private String bizType;// 库存变动业务类型
	private String skuCode;// sku编码
	private Long warehouseId;// 仓库id
	private Integer quantity;	// 变动数量
	private Date createTime;// 发生时间
	private String originalCode;
	private String goodsSid;
	public String getId() {
		return id;
	}
	public void setId(String id) {
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
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public Long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
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
	public String getGoodsSid() {
		return goodsSid;
	}
	public void setGoodsSid(String goodsSid) {
		this.goodsSid = goodsSid;
	}
}
