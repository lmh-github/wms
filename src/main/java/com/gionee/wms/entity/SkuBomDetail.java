/**
 * Project Name:wms
 * File Name:SkuBomDetail.java
 * Package Name:com.gionee.wms.entity
 * Date:2015年6月30日下午5:05:57
 * Copyright (c) 2015 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.entity;

/**
 * 组合SKU
 * @author PengBin 00001550<br>
 * @date 2015年6月30日 下午5:05:57
 */
public class SkuBomDetail {
	private String pSkuCode;
	private String cSkuCode;
	private Integer quantity;
	private String skuCode;
	private String skuName;
	private String measureUnit;
	/** 是否赠品 */
	private Integer isBonus;

	/**
	 * @return the pSkuCode
	 */
	public String getPSkuCode() {
		return pSkuCode;
	}

	/**
	 * @param pSkuCode the pSkuCode
	 */
	public void setPSkuCode(String pSkuCode) {
		this.pSkuCode = pSkuCode;
	}

	/**
	 * @return the cSkuCode
	 */
	public String getCSkuCode() {
		return cSkuCode;
	}

	/**
	 * @param cSkuCode the cSkuCode
	 */
	public void setCSkuCode(String cSkuCode) {
		this.cSkuCode = cSkuCode;
	}

	/**
	 * @return the quantity
	 */
	public Integer getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity the quantity
	 */
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return the skuCode
	 */
	public String getSkuCode() {
		return skuCode;
	}

	/**
	 * @param skuCode the skuCode
	 */
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	/**
	 * @return the skuName
	 */
	public String getSkuName() {
		return skuName;
	}

	/**
	 * @param skuName the skuName
	 */
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	/**
	 * @return the measureUnit
	 */
	public String getMeasureUnit() {
		return measureUnit;
	}

	/**
	 * @param measureUnit the measureUnit
	 */
	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	/**
	 * @return the isBonus
	 */
	public Integer getIsBonus() {
		return isBonus;
	}

	/**
	 * @param isBonus the isBonus
	 */
	public void setIsBonus(Integer isBonus) {
		this.isBonus = isBonus;
	}

}
