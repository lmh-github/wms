/**
 * Project Name:wms
 * File Name:WwmsSailOrderRequest.java
 * Package Name:com.sf.integration.warehouse.request
 * Date:2014年8月20日下午3:23:17
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.sf.integration.warehouse.request;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 出库单（销售订单）接口，请求报文
 * @author PengBin 00001550<br>
 * @date 2014年8月20日 下午3:23:17
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "wmsSailOrderRequest")
public class WmsSailOrderRequest extends WmsRequest {

	@XmlElement(name = "header", type = WmsSailOrderRequestHeader.class)
	private WmsSailOrderRequestHeader header;

	@XmlElementWrapper(name = "detailList")
	@XmlElements(value = { @XmlElement(name = "item", type = WwmsSailOrderRequestItem.class) })
	private List<WwmsSailOrderRequestItem> detailList;

	/**
	 * @return the header
	 */
	public WmsSailOrderRequestHeader getHeader() {
		return header;
	}

	/**
	 * @param header the header
	 */
	public void setHeader(WmsSailOrderRequestHeader header) {
		this.header = header;
	}

	/**
	 * @return the detailList
	 */
	public List<WwmsSailOrderRequestItem> getDetailList() {
		return detailList;
	}

	/**
	 * @param detailList the detailList
	 */
	public void setDetailList(List<WwmsSailOrderRequestItem> detailList) {
		this.detailList = detailList;
	}

}
