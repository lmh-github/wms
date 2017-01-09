/*
 * @(#)OrderStatusVo.java 2014-1-21
 *
 * Copyright 2013 Shenzhen Gionee,Inc. All rights reserved.
 */
package com.gionee.wms.vo;

/**
 *
 * @author ZuoChangjun 2014-1-21
 */
public class OrderStatusVo {
	private Long shippingId;// 物流公司Id
	private String shippingName;// 物流公司名

	private int orderStatus;// 订单状态
	private int orderCount;// 订单数
	
	public Long getShippingId() {
		return shippingId;
	}
	public void setShippingId(Long shippingId) {
		this.shippingId = shippingId;
	}
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	public int getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(int orderStatus) {
		this.orderStatus = orderStatus;
	}
	public int getOrderCount() {
		return orderCount;
	}
	public void setOrderCount(int orderCount) {
		this.orderCount = orderCount;
	}
}
