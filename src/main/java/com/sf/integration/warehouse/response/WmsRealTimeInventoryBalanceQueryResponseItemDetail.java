/**
 * Project Name:wms
 * File Name:WmsRealTimeInventoryBalanceQueryResponseItemDetail.java
 * Package Name:com.sf.integration.warehouse.response
 * Date:2014年8月26日下午4:11:19
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.sf.integration.warehouse.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月26日 下午4:11:19
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class WmsRealTimeInventoryBalanceQueryResponseItemDetail {

	@XmlElement
	private String sub_warehouse;
	@XmlElement
	private Double total_stock;
	@XmlElement
	private Double on_hand_stock;
	@XmlElement
	private Double available_stock;
	@XmlElement
	private Double in_transit_stock;
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
	 * @return the sub_warehouse
	 */
	public String getSub_warehouse() {
		return sub_warehouse;
	}

	/**
	 * @param sub_warehouse the sub_warehouse
	 */
	public void setSub_warehouse(String sub_warehouse) {
		this.sub_warehouse = sub_warehouse;
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
		return "WmsRealTimeInventoryBalanceQueryResponseItemDetail [sub_warehouse=" + sub_warehouse + ", total_stock=" + total_stock + ", on_hand_stock=" + on_hand_stock + ", available_stock=" + available_stock + ", in_transit_stock=" + in_transit_stock + "]";
	}

}
