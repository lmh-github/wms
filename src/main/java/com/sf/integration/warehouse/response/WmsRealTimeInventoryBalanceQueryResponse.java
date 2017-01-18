/**
 * Project Name:wms
 * File Name:WmsRealTimeInventoryBalanceQueryResponse.java
 * Package Name:com.sf.integration.warehouse.response
 * Date:2014年8月26日下午3:35:56
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.sf.integration.warehouse.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 实时库存查询接口，响应
 * @author PengBin 00001550br
 * @date 2014年8月26日 下午3:35:56
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "wmsRealTimeInventoryBalanceQueryResponse")
public class WmsRealTimeInventoryBalanceQueryResponse extends WmsResponse {

	@XmlElementWrapper(name = "itemlist")
	@XmlElements(value = { @XmlElement(name = "item", type = WmsRealTimeInventoryBalanceQueryResponseItem.class) })
	private List<WmsRealTimeInventoryBalanceQueryResponseItem> list;

	/**
	 * @return the list
	 */
	public List<WmsRealTimeInventoryBalanceQueryResponseItem> getList() {
		return list;
	}

	/**
	 * @param list the list
	 */
	public void setList(List<WmsRealTimeInventoryBalanceQueryResponseItem> list) {
		this.list = list;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "WmsRealTimeInventoryBalanceQueryResponse [list=" + list + "]";
	}

}
