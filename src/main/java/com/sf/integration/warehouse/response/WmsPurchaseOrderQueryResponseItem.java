package com.sf.integration.warehouse.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
public class WmsPurchaseOrderQueryResponseItem {

	@XmlElement
	private String id;
	
	@XmlElement
	private String erp_order_line_num;
	
	@XmlElement
	private String sku_no;
	
	@XmlElement
	private String qty;
	
	@XmlElement
	private String lot;
	
	@XmlElement
	private String expiration_date;
	
	@XmlElement
	private String inventory_sts;
	
	@XmlElement
	private String vendor;
	
	@XmlElementWrapper(name = "serialNumberList")
	@XmlElement
	private List<String> serial_number;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getErp_order_line_num() {
		return erp_order_line_num;
	}

	public void setErp_order_line_num(String erp_order_line_num) {
		this.erp_order_line_num = erp_order_line_num;
	}

	public String getSku_no() {
		return sku_no;
	}

	public void setSku_no(String sku_no) {
		this.sku_no = sku_no;
	}

	public String getQty() {
		return qty;
	}

	public void setQty(String qty) {
		this.qty = qty;
	}

	public String getLot() {
		return lot;
	}

	public void setLot(String lot) {
		this.lot = lot;
	}

	public String getExpiration_date() {
		return expiration_date;
	}

	public void setExpiration_date(String expiration_date) {
		this.expiration_date = expiration_date;
	}

	public String getInventory_sts() {
		return inventory_sts;
	}

	public void setInventory_sts(String inventory_sts) {
		this.inventory_sts = inventory_sts;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public List<String> getSerial_number() {
		return serial_number;
	}

	public void setSerial_number(List<String> serial_number) {
		this.serial_number = serial_number;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "WmsPurchaseOrderQueryResponseItem[id="+id+",erp_order_line_num="+erp_order_line_num+",sku_no="+sku_no+",qty="+qty+",lot="+lot+",expiration_date="+expiration_date+",inventory_sts="+inventory_sts+",vendor="+vendor+",serial_number="+serial_number+"]";
	}
}
