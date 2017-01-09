package com.gionee.wms.facade.request;

import java.io.Serializable;

import com.gionee.wms.facade.dto.OrderInfoDTO;

public class SyncOrderRequest implements Serializable {
	private static final long serialVersionUID = 6459294146717042264L;

	/**
	 * 时间戳
	 */
	private String timestamp;

	/**
	 * 签名
	 */
	private String signature;
	
	/**
	 * 应用标识
	 */
	private String appFlag;
	
	/**
	 * 操作标志
	 * 1:更新
	 * 其他:新增
	 */
	private Integer operFlag;

	/**
	 * 订单信息
	 */
	private OrderInfoDTO orderInfo;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

	public OrderInfoDTO getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfoDTO orderInfo) {
		this.orderInfo = orderInfo;
	}

	public Integer getOperFlag() {
		return operFlag;
	}

	public void setOperFlag(Integer operFlag) {
		this.operFlag = operFlag;
	}

	public String getAppFlag() {
		return appFlag;
	}

	public void setAppFlag(String appFlag) {
		this.appFlag = appFlag;
	}

}
