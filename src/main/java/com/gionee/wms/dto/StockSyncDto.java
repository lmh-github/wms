package com.gionee.wms.dto;

public class StockSyncDto {
	private Integer quantity;// 数量
	private String skuId;	// 商品的skuId
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public String getSkuId() {
		return skuId;
	}
	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
	
}
