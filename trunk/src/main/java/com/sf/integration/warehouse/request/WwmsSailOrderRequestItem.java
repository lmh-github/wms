/**
 * Project Name:wms
 * File Name:WwmsSailOrderRequestItem.java
 * Package Name:com.sf.integration.warehouse.request
 * Date:2014年8月20日下午3:35:36
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.sf.integration.warehouse.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author PengBin 00001550br
 * @date 2014年8月20日 下午3:35:36
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class WwmsSailOrderRequestItem {
	@XmlElement
	private String erp_order_line_num;
	@XmlElement
	private String item;
	@XmlElement
	private String item_name;
	@XmlElement
	private String uom;
	@XmlElement
	private String lot;
	@XmlElement
	private String qty;
	@XmlElement
	private String item_price;
	@XmlElement
	private String item_discount;
	@XmlElement
	private String bom_action;
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

	/**
	 * @return the erp_order_line_num
	 */
	public String getErp_order_line_num() {
		return erp_order_line_num;
	}

	/**
	 * @param erp_order_line_num the erp_order_line_num
	 */
	public void setErp_order_line_num(String erp_order_line_num) {
		this.erp_order_line_num = erp_order_line_num;
	}

	/**
	 * @return the item
	 */
	public String getItem() {
		return item;
	}

	/**
	 * @param item the item
	 */
	public void setItem(String item) {
		this.item = item;
	}

	/**
	 * @return the item_name
	 */
	public String getItem_name() {
		return item_name;
	}

	/**
	 * @param item_name the item_name
	 */
	public void setItem_name(String item_name) {
		this.item_name = item_name;
	}

	/**
	 * @return the uom
	 */
	public String getUom() {
		return uom;
	}

	/**
	 * @param uom the uom
	 */
	public void setUom(String uom) {
		this.uom = uom;
	}

	/**
	 * @return the lot
	 */
	public String getLot() {
		return lot;
	}

	/**
	 * @param lot the lot
	 */
	public void setLot(String lot) {
		this.lot = lot;
	}

	/**
	 * @return the qty
	 */
	public String getQty() {
		return qty;
	}

	/**
	 * @param qty the qty
	 */
	public void setQty(String qty) {
		this.qty = qty;
	}

	/**
	 * @return the item_price
	 */
	public String getItem_price() {
		return item_price;
	}

	/**
	 * @param item_price the item_price
	 */
	public void setItem_price(String item_price) {
		this.item_price = item_price;
	}

	/**
	 * @return the item_discount
	 */
	public String getItem_discount() {
		return item_discount;
	}

	/**
	 * @param item_discount the item_discount
	 */
	public void setItem_discount(String item_discount) {
		this.item_discount = item_discount;
	}

	/**
	 * @return the bom_action
	 */
	public String getBom_action() {
		return bom_action;
	}

	/**
	 * @param bom_action the bom_action
	 */
	public void setBom_action(String bom_action) {
		this.bom_action = bom_action;
	}

	/**
	 * @return the user_def1
	 */
	public String getUser_def1() {
		return user_def1;
	}

	/**
	 * @param user_def1 the user_def1
	 */
	public void setUser_def1(String user_def1) {
		this.user_def1 = user_def1;
	}

	/**
	 * @return the user_def2
	 */
	public String getUser_def2() {
		return user_def2;
	}

	/**
	 * @param user_def2 the user_def2
	 */
	public void setUser_def2(String user_def2) {
		this.user_def2 = user_def2;
	}

	/**
	 * @return the user_def3
	 */
	public String getUser_def3() {
		return user_def3;
	}

	/**
	 * @param user_def3 the user_def3
	 */
	public void setUser_def3(String user_def3) {
		this.user_def3 = user_def3;
	}

	/**
	 * @return the user_def4
	 */
	public String getUser_def4() {
		return user_def4;
	}

	/**
	 * @param user_def4 the user_def4
	 */
	public void setUser_def4(String user_def4) {
		this.user_def4 = user_def4;
	}

	/**
	 * @return the user_def5
	 */
	public String getUser_def5() {
		return user_def5;
	}

	/**
	 * @param user_def5 the user_def5
	 */
	public void setUser_def5(String user_def5) {
		this.user_def5 = user_def5;
	}

	/**
	 * @return the user_def6
	 */
	public String getUser_def6() {
		return user_def6;
	}

	/**
	 * @param user_def6 the user_def6
	 */
	public void setUser_def6(String user_def6) {
		this.user_def6 = user_def6;
	}

	/**
	 * @return the user_def7
	 */
	public String getUser_def7() {
		return user_def7;
	}

	/**
	 * @param user_def7 the user_def7
	 */
	public void setUser_def7(String user_def7) {
		this.user_def7 = user_def7;
	}

	/**
	 * @return the user_def8
	 */
	public String getUser_def8() {
		return user_def8;
	}

	/**
	 * @param user_def8 the user_def8
	 */
	public void setUser_def8(String user_def8) {
		this.user_def8 = user_def8;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "WwmsSailOrderRequestItem [erp_order_line_num=" + erp_order_line_num + ", item=" + item + ", item_name=" + item_name + ", uom=" + uom + ", lot=" + lot + ", qty=" + qty + ", item_price=" + item_price + ", item_discount=" + item_discount + ", bom_action=" + bom_action + ", user_def1="
				+ user_def1 + ", user_def2=" + user_def2 + ", user_def3=" + user_def3 + ", user_def4=" + user_def4 + ", user_def5=" + user_def5 + ", user_def6=" + user_def6 + ", user_def7=" + user_def7 + ", user_def8=" + user_def8 + "]";
	}

}
