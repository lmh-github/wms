/**
 * Project Name:wms
 * File Name:SalesOrderMap.java
 * Package Name:com.gionee.wms.entity
 * Date:2014年8月29日下午6:16:12
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.entity;

import java.util.Date;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月29日 下午6:16:12
 */
public class SalesOrderMap {

	/** 主键ID */
	private Long id;
	/** 销售订单号 */
	private String order_code;
	/** 顺丰ERP订单号 */
	private String sf_erp_order;
	/** 发货时间 */
	private Date actual_ship_date_time;
	/** 承运商名 */
	private String carrier;
	/** 承运商服务类型 */
	private String carrier_service;

	/**
	 * 
	 */
	public SalesOrderMap() {
		super();
	}

	/**
	 * 
	 * @param order_code
	 * @param sf_erp_order
	 */
	public SalesOrderMap(String order_code, String sf_erp_order) {
		super();
		this.order_code = order_code;
		this.sf_erp_order = sf_erp_order;
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
	 * @return the actual_ship_date_time
	 */
	public Date getActual_ship_date_time() {
		return actual_ship_date_time;
	}

	/**
	 * @param actual_ship_date_time the actual_ship_date_time
	 */
	public void setActual_ship_date_time(Date actual_ship_date_time) {
		this.actual_ship_date_time = actual_ship_date_time;
	}

	/**
	 * @return the carrier
	 */
	public String getCarrier() {
		return carrier;
	}

	/**
	 * @param carrier the carrier
	 */
	public void setCarrier(String carrier) {
		this.carrier = carrier;
	}

	/**
	 * @return the carrier_service
	 */
	public String getCarrier_service() {
		return carrier_service;
	}

	/**
	 * @param carrier_service the carrier_service
	 */
	public void setCarrier_service(String carrier_service) {
		this.carrier_service = carrier_service;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "SalesOrderMap [id=" + id + ", order_code=" + order_code + ", sf_erp_order=" + sf_erp_order + ", actual_ship_date_time=" + actual_ship_date_time + ", carrier=" + carrier + ", carrier_service=" + carrier_service + "]";
	}

}
