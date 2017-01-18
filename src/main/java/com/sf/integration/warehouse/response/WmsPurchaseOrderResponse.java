package com.sf.integration.warehouse.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 入库单（调货单）接口，响应报文
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "wmsPurchaseOrderResponse")
public class WmsPurchaseOrderResponse extends WmsResponse{
	@XmlElement
	private String orderid;
	

	
	public String getOrderid() {
		return orderid;
	}

	
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}


	@Override
	public String toString() {
		return "WmsPurchaseOrderResponse [orderid=" + orderid + "]";
	}

}
