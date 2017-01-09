/**
 * Project Name:wms
 * File Name:SalesOrderImei.java
 * Package Name:com.gionee.wms.entity
 * Date:2014年8月29日下午6:57:34
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.entity;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月29日 下午6:57:34
 */
public class SalesOrderImei {
	/** 主键ID */
	private Long id;
	/** 销售订单号 */
	private String order_code;
	/** 顺丰ERP订单号 */
	private String sf_erp_order;
	/** 串号 */
	private String imei;

	/**
	 * 
	 */
	public SalesOrderImei() {
	}

	/**
	 * @param order_code
	 * @param sf_erp_order
	 * @param imei
	 */
	public SalesOrderImei(String order_code, String sf_erp_order, String imei) {
		this.order_code = order_code;
		this.sf_erp_order = sf_erp_order;
		this.imei = imei;
	}

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
	 * @return the order_code
	 */
	public String getOrder_code() {
		return order_code;
	}

	/**
	 * @param order_code the order_code
	 */
	public void setOrder_code(String order_code) {
		this.order_code = order_code;
	}

	/**
	 * @return the sf_erp_order
	 */
	public String getSf_erp_order() {
		return sf_erp_order;
	}

	/**
	 * @param sf_erp_order the sf_erp_order
	 */
	public void setSf_erp_order(String sf_erp_order) {
		this.sf_erp_order = sf_erp_order;
	}

	/**
	 * @return the imei
	 */
	public String getImei() {
		return imei;
	}

	/**
	 * @param imei the imei
	 */
	public void setImei(String imei) {
		this.imei = imei;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "SalesOrderImei [id=" + id + ", order_code=" + order_code + ", sf_erp_order=" + sf_erp_order + ", imei=" + imei + "]";
	}

}
