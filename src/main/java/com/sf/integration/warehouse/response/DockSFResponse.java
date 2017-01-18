/**
 * Project Name:wms
 * File Name:DockSFResponse.java
 * Package Name:com.sf.integration.warehouse.response
 * Date:2014年8月28日下午1:55:00
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.sf.integration.warehouse.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 对接顺丰接口响应体
 * @see com.gionee.wms.web.servlet.DockSFServlet
 * @author PengBin 00001550<br>
 * @date 2014年8月28日 下午1:55:00
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "Response")
public class DockSFResponse {
	/** 处理结果 */
	@XmlElement
	private boolean success;
	/** 备注 */
	@XmlElement
	private String reason;

	public DockSFResponse() {
		super();
	}

	public DockSFResponse(boolean success, String reason) {
		super();
		this.success = success;
		this.reason = reason;
	}

	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the reason
	 */
	public String getReason() {
		return reason;
	}

	/**
	 * @param reason the reason
	 */
	public void setReason(String reason) {
		this.reason = reason;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "DockSFResponse [success=" + success + ", reason=" + reason + "]";
	}

}
