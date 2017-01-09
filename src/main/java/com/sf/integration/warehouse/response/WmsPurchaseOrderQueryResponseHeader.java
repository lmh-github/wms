package com.sf.integration.warehouse.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class WmsPurchaseOrderQueryResponseHeader {
	
	@XmlElement
	private String erp_order_num;
	
	@XmlElement
	private String close_date;

	public String getErp_order_num() {
		return erp_order_num;
	}

	public void setErp_order_num(String erp_order_num) {
		this.erp_order_num = erp_order_num;
	}

	public String getClose_date() {
		return close_date;
	}

	public void setClose_date(String close_date) {
		this.close_date = close_date;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "WmsPurchaseOrderQueryHeader[erp_order_num="+erp_order_num+",close_date="+close_date+"]";
	}

}
