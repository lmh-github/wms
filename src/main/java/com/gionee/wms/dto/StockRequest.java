package com.gionee.wms.dto;

import com.gionee.wms.common.WmsConstants.StockBizType;
import com.gionee.wms.common.WmsConstants.StockType;

public class StockRequest {
	private Long warehouseId;//仓库ID
	private Long skuId;// SKU ID
	private String skuCode; // SKU编码
	private StockType srcStockType; // 源库存类型
	private StockType destStockType;// 目标库存类型
	private Integer quantity;// 数量
	private StockBizType stockBizType;// 业务类型
	private String originalCode;// 业务类型的原单号
	private String goodsSid;// 商品传入的skuId

	public StockRequest(Long warehouseId, Long skuId, StockType destStockType, int quantity, StockBizType stockBizType,
			String originalCode) {
		this.warehouseId = warehouseId;
		this.skuId = skuId;
		this.destStockType = destStockType;
		this.quantity = quantity;
		this.stockBizType = stockBizType;
		this.originalCode = originalCode;
	}

	public StockRequest(Long warehouseId, Long skuId, StockType srcStockType, StockType destStockType,
			Integer quantity, StockBizType stockBizType, String originalCode) {
		this.warehouseId = warehouseId;
		this.skuId = skuId;
		this.srcStockType = srcStockType;
		this.destStockType = destStockType;
		this.quantity = quantity;
		this.stockBizType = stockBizType;
		this.originalCode = originalCode;
	}
	
	public StockRequest(Long warehouseId, String skuCode, StockType destStockType, int quantity, StockBizType stockBizType,
			String originalCode) {
		this.warehouseId = warehouseId;
		this.skuCode = skuCode;
		this.destStockType = destStockType;
		this.quantity = quantity;
		this.stockBizType = stockBizType;
		this.originalCode = originalCode;
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

	public StockType getSrcStockType() {
		return srcStockType;
	}

	public void setSrcStockType(StockType srcStockType) {
		this.srcStockType = srcStockType;
	}

	public StockType getDestStockType() {
		return destStockType;
	}

	public void setDestStockType(StockType destStockType) {
		this.destStockType = destStockType;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public StockBizType getStockBizType() {
		return stockBizType;
	}

	public void setStockBizType(StockBizType stockBizType) {
		this.stockBizType = stockBizType;
	}

	public String getOriginalCode() {
		return originalCode;
	}

	public void setOriginalCode(String originalCode) {
		this.originalCode = originalCode;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getGoodsSid() {
		return goodsSid;
	}

	public void setGoodsSid(String goodsSid) {
		this.goodsSid = goodsSid;
	}

}
