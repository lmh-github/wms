package com.sf.integration.warehouse.response;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "item")
public class WmsInventoryBalancePageQueryResponseItem {

	/** 货主 */
	@XmlElement(name = "company")
	private String company;

	/** 仓库 */
	@XmlElement(name = "warehouse")
	private String warehouse;

	/** 商品编号 */
	@XmlElement(name = "item")
	private String item;

	/** 在库库存数量 */
	@XmlElement(name = "on_hand_qty")
	private Double on_hand_qty;

	/** 批号 */
	@XmlElement(name = "lot")
	private String lot;

	/** 有效期 */
	@XmlElement(name = "expiration_date")
	private Date expiration_date;

	/** 库存状态 */
	@XmlElement(name = "inventory_sts")
	private Integer inventory_sts;

	/** 预留字段 */
	@XmlElement(name = "user_def1")
	private String user_def1;

	/** 预留字段 */
	@XmlElement(name = "user_def2")
	private String user_def2;

	/** 预留字段 */
	@XmlElement(name = "user_def3")
	private String user_def3;

	/** 预留字段 */
	@XmlElement(name = "user_def4")
	private String user_def4;

	/** 预留字段 */
	@XmlElement(name = "user_def5")
	private String user_def5;

	/** 预留字段 */
	@XmlElement(name = "user_def6")
	private String user_def6;

	/** 预留字段 */
	@XmlElement(name = "user_def7")
	private String user_def7;

	/** 预留字段 */
	@XmlElement(name = "user_def8")
	private String user_def8;

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
	 * @return the on_hand_qty
	 */
	public Double getOn_hand_qty() {
		return on_hand_qty;
	}

	/**
	 * @param on_hand_qty the on_hand_qty
	 */
	public void setOn_hand_qty(Double on_hand_qty) {
		this.on_hand_qty = on_hand_qty;
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
	public Date getExpiration_date() {
		return expiration_date;
	}

	/**
	 * @param expiration_date the expiration_date
	 */
	public void setExpiration_date(Date expiration_date) {
		this.expiration_date = expiration_date;
	}

	/**
	 * @return the inventory_sts
	 */
	public Integer getInventory_sts() {
		return inventory_sts;
	}

	/**
	 * @param inventory_sts the inventory_sts
	 */
	public void setInventory_sts(Integer inventory_sts) {
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

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "Item [company=" + company + ", warehouse=" + warehouse + ", item=" + item + ", on_hand_qty=" + on_hand_qty + ", lot=" + lot + ", expiration_date=" + expiration_date + ", inventory_sts=" + inventory_sts + ", user_def1=" + user_def1 + ", user_def2=" + user_def2 + ", user_def3="
				+ user_def3 + ", user_def4=" + user_def4 + ", user_def5=" + user_def5 + ", user_def6=" + user_def6 + ", user_def7=" + user_def7 + ", user_def8=" + user_def8 + "]";
	}

}
