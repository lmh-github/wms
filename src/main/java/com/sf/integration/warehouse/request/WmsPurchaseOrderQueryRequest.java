package com.sf.integration.warehouse.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 入库单(调货单)查询接口
 * @author Asher
 *
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "wmsPurchaseOrderQueryRequest")
public class WmsPurchaseOrderQueryRequest extends WmsRequest{
	@XmlElement
	private String checkword;
	
	@XmlElement
	private String company;
	
	@XmlElement
	private String orderid;
	
	@XmlElement
	private String warehouse;

	public String getCheckword() {
		return checkword;
	}

	public void setCheckword(String checkword) {
		this.checkword = checkword;
	}

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getOrderid() {
		return orderid;
	}

	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}

	public String getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "WmsPurchaseOrderQueryRequest [checkword=" + checkword + ", company=" + company + ", orderid=" + orderid + ",warehouse="+warehouse+"]";
	}
}
