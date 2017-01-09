/*
 * @(#)ShippingResultVo.java 2013-7-30
 *
 * Copyright 2013 Shenzhen Gionee,Inc. All rights reserved.
 */
package com.gionee.wms.vo;

import java.util.List;

/**
 * 快递单查询结果
 * 
 * @author ZuoChangjun 2013-7-30
 */
public class ShippingResultVo {
	private String com;// 要查询的快递公司代码，不支持中文
	private String nu;// 发货单编号,要查询的快递单号
	private String state;// 快递单当前的状态 ：0：在途中,1：已发货，2：疑难件，3：已签收，4：已退货。
    private List<Event> data;
	private String status; // 查询结果状态：0：物流单暂无结果,1：查询成功,2：接口出现异常,3,验证参数出错，408：验证码出错（仅适用于APICode url，可忽略) 
	private String message;// 无意义，请忽略
	private String condition;// 无意义，请忽略
	private String ischeck;// 无意义，请忽略

	private class Event {
		private String time;
		private String context;
		public String getTime() {
			return time;
		}
		public void setTime(String time) {
			this.time = time;
		}
		public String getContext() {
			return context;
		}
		public void setContext(String context) {
			this.context = context;
		}
	}

	public String getCom() {
		return com;
	}

	public void setCom(String com) {
		this.com = com;
	}

	public String getNu() {
		return nu;
	}

	public void setNu(String nu) {
		this.nu = nu;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public List<Event> getData() {
		return data;
	}

	public void setData(List<Event> data) {
		this.data = data;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public String getIscheck() {
		return ischeck;
	}

	public void setIscheck(String ischeck) {
		this.ischeck = ischeck;
	}
	
}
