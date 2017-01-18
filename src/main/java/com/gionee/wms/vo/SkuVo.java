/*
 * @(#)SkuVo.java 2013-7-5
 *
 * Copyright 2013 Shenzhen Gionee,Inc. All rights reserved.
 */
package com.gionee.wms.vo;


/**
 *
 * @author ZuoChangjun 2013-7-5
 */
public class SkuVo {
	private Long skuId;//skuID
	private Long waresId;// 商品ID
	private String skuName; // sku名称
	private String skuCode;// sku编码
	private String skuBarcode;// sku条形码（EAN码）
	
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public Long getWaresId() {
		return waresId;
	}
	public void setWaresId(Long waresId) {
		this.waresId = waresId;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getSkuBarcode() {
		return skuBarcode;
	}
	public void setSkuBarcode(String skuBarcode) {
		this.skuBarcode = skuBarcode;
	}
}
