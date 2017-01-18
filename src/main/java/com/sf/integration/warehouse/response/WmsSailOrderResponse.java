/**
 * Project Name:wms
 * File Name:WmsSailOrderResponse.java
 * Package Name:com.sf.integration.warehouse.response
 * Date:2014年8月20日下午3:42:55
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.sf.integration.warehouse.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 出库单（销售订单）接口，响应报文
 * @author PengBin 00001550<br>
 * @date 2014年8月20日 下午3:42:55
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "wmsSailOrderResponse")
public class WmsSailOrderResponse extends WmsResponse {
	@XmlElement
	private String orderid;

	/**
	 * @return the orderid
	 */
	public String getOrderid() {
		return orderid;
	}

	/**
	 * @param orderid the orderid
	 */
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "WmsSailOrderResponse [orderid=" + orderid + "]";
	}

}
