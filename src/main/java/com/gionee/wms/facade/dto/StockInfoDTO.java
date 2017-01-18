package com.gionee.wms.facade.dto;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @ClassName: StockInfoDTO
 * @Description: 库存信息DTO
 * @author Kevin
 * @date 2013-7-4 下午06:01:59
 * 
 */

public class StockInfoDTO implements Serializable {
	private static final long serialVersionUID = 8649496712828426938L;

	/**
	 * 库存ID
	 */
	private Long stockId;
	/**
	 * SKU编码
	 */
	private String skuCode;

	/**
	 * 库存编号
	 */
	private String warehouseCode;

	/**
	 * 可销售库存
	 */
	private Integer salesQuantity;

	/**
	 * 不可销售库存
	 */
	private Integer unsalesQuantity;

	/**
	 * 计量单位
	 */
	private String measureUnit;

	public Long getStockId() {
		return stockId;
	}

	public void setStockId(Long stockId) {
		this.stockId = stockId;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public Integer getSalesQuantity() {
		return salesQuantity;
	}

	public void setSalesQuantity(Integer salesQuantity) {
		this.salesQuantity = salesQuantity;
	}

	public Integer getUnsalesQuantity() {
		return unsalesQuantity;
	}

	public void setUnsalesQuantity(Integer unsalesQuantity) {
		this.unsalesQuantity = unsalesQuantity;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
