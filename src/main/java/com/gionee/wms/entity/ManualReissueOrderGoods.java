/**
 * Project Name:wms
 * File Name:ManualReissueOrderGoods.java
 * Package Name:com.gionee.wms.entity
 * Date:2016年10月8日下午2:21:06
 * Copyright (c) 2016 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.entity;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2016年10月8日 下午2:21:06
 */
public class ManualReissueOrderGoods {

	private Long id;

	private Long manualReissueOrderId;

	private String skuCode;

	private String skuName;

	private Integer qty;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the manualReissueOrderId
	 */
	public Long getManualReissueOrderId() {
		return manualReissueOrderId;
	}

	/**
	 * @param manualReissueOrderId the manualReissueOrderId
	 */
	public void setManualReissueOrderId(Long manualReissueOrderId) {
		this.manualReissueOrderId = manualReissueOrderId;
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
	 * @return the qty
	 */
	public Integer getQty() {
		return qty;
	}

	/**
	 * @param qty the qty
	 */
	public void setQty(Integer qty) {
		this.qty = qty;
	}

}
