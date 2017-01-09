package com.gionee.wms.facade.dto;

import java.io.Serializable;

public class SalesOrderDTO implements Serializable {
	private static final long serialVersionUID = 2063145837574391764L;
	
	private String orderCode;
	private Integer orderStatus;
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	
}
