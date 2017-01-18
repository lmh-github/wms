package com.sf.integration.warehouse.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.sf.integration.warehouse.request.WmsPurchaseOrderRequestHeader;

/**
 * 入库单状态查询接口
 * @author Asher
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "wmsPurchaseOrderQueryResponse")
public class WmsPurchaseOrderQueryResponse extends WmsResponse {
	
	
	@XmlElement(name = "header", type = WmsPurchaseOrderQueryResponseHeader.class)
	private WmsPurchaseOrderQueryResponseHeader header;
	
	@XmlElementWrapper(name = "detailList")
	@XmlElements(value = { @XmlElement(name = "item", type = WmsPurchaseOrderQueryResponseItem.class) })
	private List<WmsPurchaseOrderQueryResponseItem> detailList;

	public WmsPurchaseOrderQueryResponseHeader getHeader() {
		return header;
	}

	public void setHeader(WmsPurchaseOrderQueryResponseHeader header) {
		this.header = header;
	}

	public List<WmsPurchaseOrderQueryResponseItem> getDetailList() {
		return detailList;
	}

	public void setDetailList(List<WmsPurchaseOrderQueryResponseItem> detailList) {
		this.detailList = detailList;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "WmsPurchaseOrderQueryResponse[header=" + header + ", detailList=" + detailList + "]";
	}
	
}
