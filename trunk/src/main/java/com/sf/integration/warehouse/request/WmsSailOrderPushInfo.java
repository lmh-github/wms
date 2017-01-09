/**
 * Project Name:wms
 * File Name:WmsSailOrderPushInfo.java
 * Package Name:com.sf.integration.warehouse.request
 * Date:2014年8月28日上午11:05:17
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
 * 出库单状态与明细推送接口（请求报文）
 * @author PengBin 00001550<br>
 * @date 2014年8月28日 上午11:05:17
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "wmsSailOrderPushInfo")
public class WmsSailOrderPushInfo {

	@XmlElement(name = "header", type = WmsSailOrderPushInfoHeader.class)
	private WmsSailOrderPushInfoHeader header;

	@XmlElementWrapper(name = "detailList")
	@XmlElements(value = { @XmlElement(name = "item", type = WmsSailOrderPushInfoDetailItem.class) })
	private List<WmsSailOrderPushInfoDetailItem> detailList;

	@XmlElementWrapper(name = "containerList")
	@XmlElements(value = { @XmlElement(name = "item", type = WmsSailOrderPushInfoContainerItem.class) })
	private List<WmsSailOrderPushInfoContainerItem> containerList;

	/**
	 * @return the header
	 */
	public WmsSailOrderPushInfoHeader getHeader() {
		return header;
	}

	/**
	 * @param header the header
	 */
	public void setHeader(WmsSailOrderPushInfoHeader header) {
		this.header = header;
	}

	/**
	 * @return the detailList
	 */
	public List<WmsSailOrderPushInfoDetailItem> getDetailList() {
		return detailList;
	}

	/**
	 * @param detailList the detailList
	 */
	public void setDetailList(List<WmsSailOrderPushInfoDetailItem> detailList) {
		this.detailList = detailList;
	}

	/**
	 * @return the containerList
	 */
	public List<WmsSailOrderPushInfoContainerItem> getContainerList() {
		return containerList;
	}

	/**
	 * @param containerList the containerList
	 */
	public void setContainerList(List<WmsSailOrderPushInfoContainerItem> containerList) {
		this.containerList = containerList;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "WmsSailOrderPushInfo [header=" + header + ", detailList=" + detailList + ", containerList=" + containerList + "]";
	}

}
