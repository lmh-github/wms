package com.gionee.wms.entity;

import java.util.Date;

/**
 * @=======================================
 * @Description 快递信息
 * @author jay_liang
 * @date 2013-10-9 上午10:51:10
 * @=======================================
 */
public class ShippingInfo {
	private Long id;	// ID
	private String pushStatus;	// 轮询状态
	private String pushMessage;	// 轮询状态相关消息
	private String state;	// 快递单当前签书状态
	private String orderCode;	// 订单ID
	private String data;	// 接收数据
	private String subscribeResult;	// 订阅结果
	private Date subscribeTime;	// 订阅时间
	private Integer subscribeCount;	// 订阅次数
	private String isCheck;	// 签收状态: 0 未签 3 已签
	private String fromAddr;	// 寄件人地址
	private String toAddr;	// 收件人地址
	private String returnCode;	// 订阅的返回码
	private Date lastPushTime;	// 最后一次推送时间
	private String shippingNo;	// 快递单号
	private Long shippingId;	// 物流对象
	private String company;	// 快递公司名(只作为查询条件使用)
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getPushStatus() {
		return pushStatus;
	}
	public void setPushStatus(String pushStatus) {
		this.pushStatus = pushStatus;
	}
	public String getPushMessage() {
		return pushMessage;
	}
	public void setPushMessage(String pushMessage) {
		this.pushMessage = pushMessage;
	}
	public String getSubscribeResult() {
		return subscribeResult;
	}
	public void setSubscribeResult(String subscribeResult) {
		this.subscribeResult = subscribeResult;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public Date getSubscribeTime() {
		return subscribeTime;
	}
	public void setSubscribeTime(Date subscribeTime) {
		this.subscribeTime = subscribeTime;
	}
	public Integer getSubscribeCount() {
		return subscribeCount;
	}
	public void setSubscribeCount(Integer subscribeCount) {
		this.subscribeCount = subscribeCount;
	}
	public String getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(String isCheck) {
		this.isCheck = isCheck;
	}
	public String getFromAddr() {
		return fromAddr;
	}
	public void setFromAddr(String fromAddr) {
		this.fromAddr = fromAddr;
	}
	public String getToAddr() {
		return toAddr;
	}
	public void setToAddr(String toAddr) {
		this.toAddr = toAddr;
	}
	public Date getLastPushTime() {
		return lastPushTime;
	}
	public void setLastPushTime(Date lastPushTime) {
		this.lastPushTime = lastPushTime;
	}
	public String getReturnCode() {
		return returnCode;
	}
	public void setReturnCode(String returnCode) {
		this.returnCode = returnCode;
	}
	public String getShippingNo() {
		return shippingNo;
	}
	public void setShippingNo(String shippingNo) {
		this.shippingNo = shippingNo;
	}
	public Long getShippingId() {
		return shippingId;
	}
	public void setShippingId(Long shippingId) {
		this.shippingId = shippingId;
	}
}
