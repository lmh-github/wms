package com.sf.integration.warehouse.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "header")
public class WmsPurchaseOrderRequestHeader {
	@XmlElement
	private String company;
	@XmlElement
	private String warehouse;
	@XmlElement
	private String erp_order_num;
	@XmlElement
	private String erp_order_type;
	@XmlElement
	private String order_date;
	@XmlElement
	private String buyer;
	@XmlElement
	private String buyer_phone;
	@XmlElement
	private String scheduled_receipt_date;
	@XmlElement
	private String source_id;
	@XmlElement
	private String transfer_warehouse;
	@XmlElement
	private String original_order_no;
	@XmlElement
	private String other_inbound_note;
	@XmlElement
	private String note_to_receiver;
	@XmlElement
	private String is_easy;
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
	
	public String getErp_order_num() {
		return erp_order_num;
	}

	public void setErp_order_num(String erp_order_num) {
		this.erp_order_num = erp_order_num;
	}

	public String getErp_order_type() {
		return erp_order_type;
	}

	public void setErp_order_type(String erp_order_type) {
		this.erp_order_type = erp_order_type;
	}

	public String getBuyer() {
		return buyer;
	}

	public void setBuyer(String buyer) {
		this.buyer = buyer;
	}

	public String getBuyer_phone() {
		return buyer_phone;
	}

	public void setBuyer_phone(String buyer_phone) {
		this.buyer_phone = buyer_phone;
	}

	public String getScheduled_receipt_date() {
		return scheduled_receipt_date;
	}

	public void setScheduled_receipt_date(String scheduled_receipt_date) {
		this.scheduled_receipt_date = scheduled_receipt_date;
	}

	public String getSource_id() {
		return source_id;
	}

	public void setSource_id(String source_id) {
		this.source_id = source_id;
	}

	public String getTransfer_warehouse() {
		return transfer_warehouse;
	}

	public void setTransfer_warehouse(String transfer_warehouse) {
		this.transfer_warehouse = transfer_warehouse;
	}

	public String getOriginal_order_no() {
		return original_order_no;
	}

	public void setOriginal_order_no(String original_order_no) {
		this.original_order_no = original_order_no;
	}

	public String getOther_inbound_note() {
		return other_inbound_note;
	}

	public void setOther_inbound_note(String other_inbound_note) {
		this.other_inbound_note = other_inbound_note;
	}

	public String getNote_to_receiver() {
		return note_to_receiver;
	}

	public void setNote_to_receiver(String note_to_receiver) {
		this.note_to_receiver = note_to_receiver;
	}

	public String getIs_easy() {
		return is_easy;
	}

	public void setIs_easy(String is_easy) {
		this.is_easy = is_easy;
	}

	
	public String getCompany() {
		return company;
	}

	
	public void setCompany(String company) {
		this.company = company;
	}

	
	public String getWarehouse() {
		return warehouse;
	}

	
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	
	public String getOrder_date() {
		return order_date;
	}

	
	public void setOrder_date(String order_date) {
		this.order_date = order_date;
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


	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "WmsSailOrderRequestHeader [company=" + company + ", warehouse=" + warehouse + ", erp_order_num=" + erp_order_num + ", erp_order_type=" + erp_order_type + ", order_date=" + order_date + ", buyer=" + buyer + ", buyer_phone=" + buyer_phone + ", scheduled_receipt_date="
				+ scheduled_receipt_date + ", source_id=" + source_id + ", transfer_warehouse=" + transfer_warehouse + ", original_order_no=" + original_order_no + ", other_inbound_note=" + other_inbound_note + ", note_to_receiver=" + note_to_receiver + ", is_easy="
				+ is_easy + ", user_def1=" + user_def1 + ", user_def2=" + user_def2
				+ ", user_def3=" + user_def3 + ", user_def4=" + user_def4 + ", user_def5=" + user_def5 + ", user_def6=" + user_def6 + ", user_def7=" + user_def7 + ", user_def8=" + user_def8 + "]";
	}
}
