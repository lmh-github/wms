package com.sf.integration.warehouse.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class WmsPurchaseOrderRequestItem {
	@XmlElement
	private String erp_order_line_num;
	@XmlElement
	private String item;
	@XmlElement
	private String total_qty;
	@XmlElement
	private String lot;
	@XmlElement
	private String note;
	@XmlElement
	private String user_def1;
	@XmlElement
	private String user_def2;
	@XmlElement
	private String user_def3;
	@XmlElement
	private String user_def4;
	@XmlElement
	private String user_def5;
	@XmlElement
	private String user_def6;
	@XmlElement
	private String user_def7;
	@XmlElement
	private String user_def8;

	
	public String getErp_order_line_num() {
		return erp_order_line_num;
	}

	
	public void setErp_order_line_num(String erp_order_line_num) {
		this.erp_order_line_num = erp_order_line_num;
	}

	
	public String getItem() {
		return item;
	}

	
	public void setItem(String item) {
		this.item = item;
	}


	public String getTotal_qty() {
		return total_qty;
	}

	public void setTotal_qty(String total_qty) {
		this.total_qty = total_qty;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	
	public String getLot() {
		return lot;
	}

	
	public void setLot(String lot) {
		this.lot = lot;
	}

	
	public String getUser_def1() {
		return user_def1;
	}

	
	public void setUser_def1(String user_def1) {
		this.user_def1 = user_def1;
	}

	
	public String getUser_def2() {
		return user_def2;
	}

	
	public void setUser_def2(String user_def2) {
		this.user_def2 = user_def2;
	}

	
	public String getUser_def3() {
		return user_def3;
	}

	public void setUser_def3(String user_def3) {
		this.user_def3 = user_def3;
	}

	
	public String getUser_def4() {
		return user_def4;
	}

	
	public void setUser_def4(String user_def4) {
		this.user_def4 = user_def4;
	}

	
	public String getUser_def5() {
		return user_def5;
	}

	
	public void setUser_def5(String user_def5) {
		this.user_def5 = user_def5;
	}

	
	public String getUser_def6() {
		return user_def6;
	}

	public void setUser_def6(String user_def6) {
		this.user_def6 = user_def6;
	}

	
	public String getUser_def7() {
		return user_def7;
	}

	
	public void setUser_def7(String user_def7) {
		this.user_def7 = user_def7;
	}

	
	public String getUser_def8() {
		return user_def8;
	}

	
	public void setUser_def8(String user_def8) {
		this.user_def8 = user_def8;
	}

	
	@Override
	public String toString() {
		return "WwmsSailOrderRequestItem [erp_order_line_num=" + erp_order_line_num + ", item=" + item + ", total_qty=" + total_qty + ", lot=" + lot + ", note=" + note + ", user_def1="+ user_def1 + ", user_def2=" + user_def2 + ", user_def3=" + user_def3 + ", user_def4=" + user_def4 + ", user_def5=" + user_def5 + ", user_def6=" + user_def6 + ", user_def7=" + user_def7 + ", user_def8=" + user_def8 + "]";
	}

}
