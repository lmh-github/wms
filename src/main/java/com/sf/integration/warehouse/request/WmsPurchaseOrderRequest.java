
package com.sf.integration.warehouse.request;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 入库单（调货单）接口，请求报文
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "wmsPurchaseOrderRequest")
public class WmsPurchaseOrderRequest extends WmsRequest{
	@XmlElement
	private String checkword;

	@XmlElement(name = "header", type = WmsPurchaseOrderRequestHeader.class)
	private WmsPurchaseOrderRequestHeader header;

	@XmlElementWrapper(name = "detailList")
	@XmlElements(value = { @XmlElement(name = "item", type = WmsPurchaseOrderRequestItem.class) })
	private List<WmsPurchaseOrderRequestItem> detailList;

	
	public String getCheckword() {
		return checkword;
	}

	
	public void setCheckword(String checkword) {
		this.checkword = checkword;
	}

	
	public WmsPurchaseOrderRequestHeader getHeader() {
		return header;
	}

	
	public void setHeader(WmsPurchaseOrderRequestHeader header) {
		this.header = header;
	}

	
	public List<WmsPurchaseOrderRequestItem> getDetailList() {
		return detailList;
	}

	
	public void setDetailList(List<WmsPurchaseOrderRequestItem> detailList) {
		this.detailList = detailList;
	}

	@Override
	public String toString() {
		return "WmsPurchaseOrderRequest [checkword=" + checkword + ", header=" + header + ", detailList=" + detailList + "]";
	}

}
