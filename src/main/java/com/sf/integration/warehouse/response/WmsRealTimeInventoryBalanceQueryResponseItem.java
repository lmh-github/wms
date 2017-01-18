/**
 * Project Name:wms
 * File Name:WmsRealTimeInventoryBalanceQueryResponseItem.java
 * Package Name:com.sf.integration.warehouse.response
 * Date:2014年8月26日下午4:08:03
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.sf.integration.warehouse.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月26日 下午4:08:03
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class WmsRealTimeInventoryBalanceQueryResponseItem {
	@XmlElement
	private String company;
	@XmlElement
	private String warehouse;
	@XmlElement
	private String sku_no;
	@XmlElement
	private Double total_stock;
	@XmlElement
	private Double on_hand_stock;
	@XmlElement
	private Double available_stock;
	@XmlElement
	private Double in_transit_stock;
	@XmlElement
	private String lot;
	@XmlElement
	private String expiration_date;
	@XmlElement
	private String inventory_sts;
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

	@XmlElementWrapper(name = "detaillist")
	@XmlElements(value = { @XmlElement(name = "detail", type = WmsRealTimeInventoryBalanceQueryResponseItemDetail.class) })
	private List<WmsRealTimeInventoryBalanceQueryResponseItemDetail> detaillist;

	/**
	 * @return the company
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * @param company the company
	 */
	public void setCompany(String company) {
		this.company = company;
	}

	/**
	 * @return the warehouse
	 */
	public String getWarehouse() {
		return warehouse;
	}

	/**
	 * @param warehouse the warehouse
	 */
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}

	/**
	 * @return the sku_no
	 */
	public String getSku_no() {
		return sku_no;
	}

	/**
	 * @param sku_no the sku_no
	 */
	public void setSku_no(String sku_no) {
		this.sku_no = sku_no;
	}

	/**
	 * @return the total_stock
	 */
	public Double getTotal_stock() {
		return total_stock;
	}

	/**
	 * @param total_stock the total_stock
	 */
	public void setTotal_stock(Double total_stock) {
		this.total_stock = total_stock;
	}

	/**
	 * @return the on_hand_stock
	 */
	public Double getOn_hand_stock() {
		return on_hand_stock;
	}

	/**
	 * @param on_hand_stock the on_hand_stock
	 */
	public void setOn_hand_stock(Double on_hand_stock) {
		this.on_hand_stock = on_hand_stock;
	}

	/**
	 * @return the available_stock
	 */
	public Double getAvailable_stock() {
		return available_stock;
	}

	/**
	 * @param available_stock the available_stock
	 */
	public void setAvailable_stock(Double available_stock) {
		this.available_stock = available_stock;
	}

	/**
	 * @return the in_transit_stock
	 */
	public Double getIn_transit_stock() {
		return in_transit_stock;
	}

	/**
	 * @param in_transit_stock the in_transit_stock
	 */
	public void setIn_transit_stock(Double in_transit_stock) {
		this.in_transit_stock = in_transit_stock;
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
	 * @return the expiration_date
	 */
	public String getExpiration_date() {
		return expiration_date;
	}

	/**
	 * @param expiration_date the expiration_date
	 */
	public void setExpiration_date(String expiration_date) {
		this.expiration_date = expiration_date;
	}

	/**
	 * @return the inventory_sts
	 */
	public String getInventory_sts() {
		return inventory_sts;
	}

	/**
	 * @param inventory_sts the inventory_sts
	 */
	public void setInventory_sts(String inventory_sts) {
		this.inventory_sts = inventory_sts;
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

	/**
	 * @return the detaillist
	 */
	public List<WmsRealTimeInventoryBalanceQueryResponseItemDetail> getDetaillist() {
		return detaillist;
	}

	/**
	 * @param detaillist the detaillist
	 */
	public void setDetaillist(List<WmsRealTimeInventoryBalanceQueryResponseItemDetail> detaillist) {
		this.detaillist = detaillist;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "WmsRealTimeInventoryBalanceQueryResponseItem [company=" + company + ", warehouse=" + warehouse + ", sku_no=" + sku_no + ", total_stock=" + total_stock + ", on_hand_stock=" + on_hand_stock + ", available_stock=" + available_stock + ", in_transit_stock=" + in_transit_stock + ", lot="
				+ lot + ", expiration_date=" + expiration_date + ", inventory_sts=" + inventory_sts + ", user_def6=" + user_def6 + ", user_def7=" + user_def7 + ", user_def8=" + user_def8 + ", detaillist=" + detaillist + "]";
	}

}
