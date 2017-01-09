package com.gionee.wms.entity;

import java.io.Serializable;

/**
 * 金立sku与第三方sku映射关系
 * @since 2014-01-06
 */
public class SkuMap implements Serializable {

	private static final long serialVersionUID = 138897583353318491L;

	/**
	 * column WMS_SKU_MAP.ID
	 */
	private Integer id;

	/**
	 * column WMS_SKU_MAP.SKU_CODE
	 */
	private String skuCode;

	/**
	 * column WMS_SKU_MAP.OUTER_SKU_CODE
	 */
	private String outerSkuCode;

	/**
	 * column WMS_SKU_MAP.OUTER_CODE
	 */
	private String outerCode;
    
    /**
     * sf push status flag
     */
    private Integer skuPushStatus;

	public SkuMap() {
		super();
	}

	public SkuMap(Integer id, String skuCode, String outerSkuCode, String outerCode) {
		this.id = id;
		this.skuCode = skuCode;
		this.outerSkuCode = outerSkuCode;
		this.outerCode = outerCode;
	}

	/**
	 * getter for Column WMS_SKU_MAP.ID
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * setter for Column WMS_SKU_MAP.ID
	 * @param id
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * getter for Column WMS_SKU_MAP.SKU_CODE
	 */
	public String getSkuCode() {
		return skuCode;
	}

	/**
	 * setter for Column WMS_SKU_MAP.SKU_CODE
	 * @param skuCode
	 */
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode == null ? "" : skuCode.trim();
	}

	/**
	 * getter for Column WMS_SKU_MAP.OUTER_SKU_CODE
	 */
	public String getOuterSkuCode() {
		return outerSkuCode;
	}

	/**
	 * setter for Column WMS_SKU_MAP.OUTER_SKU_CODE
	 * @param outerSkuCode
	 */
	public void setOuterSkuCode(String outerSkuCode) {
		this.outerSkuCode = outerSkuCode == null ? "" : outerSkuCode.trim();
	}

	/**
	 * getter for Column WMS_SKU_MAP.OUTER_CODE
	 */
	public String getOuterCode() {
		return outerCode;
	}

	/**
	 * setter for Column WMS_SKU_MAP.OUTER_CODE
	 * @param outerCode
	 */
	public void setOuterCode(String outerCode) {
		this.outerCode = outerCode == null ? "" : outerCode.trim();
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "SkuMap [id=" + id + ", skuCode=" + skuCode + ", outerSkuCode=" + outerSkuCode + ", outerCode=" + outerCode + "]";
	}

	public Integer getSkuPushStatus() {
		return skuPushStatus;
	}

	public void setSkuPushStatus(Integer skuPushStatus) {
		this.skuPushStatus = skuPushStatus;
	}

}