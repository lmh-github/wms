/**
 * Project Name:wms
 * File Name:WmsRealTimeInventoryBalanceQueryRequest.java
 * Package Name:com.sf.integration.warehouse.request
 * Date:2014年8月26日下午2:06:45
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.sf.integration.warehouse.request;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月26日 下午2:06:45
 */
@XmlAccessorType(XmlAccessType.FIELD)
// @XmlType(name = "", propOrder = { "checkword", "company", "itemlist", "warehouse", "inventory_sts" })
@XmlRootElement(name = "wmsRealTimeInventoryBalanceQueryRequest")
public class WmsRealTimeInventoryBalanceQueryRequest extends WmsRequest {

	@XmlElementWrapper(name = "itemlist")
	@XmlElements(value = { @XmlElement(name = "item", type = String.class) })
	private List<String> itemList;

	/** 货主 */
	@XmlElement
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String company;

	/** 库存状态 */
	@XmlElement
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String inventory_sts;

	@XmlElement
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String warehouse;

	/**
	 * @return the itemList
	 */
	public List<String> getItemList() {
		return itemList;
	}

	/**
	 * @param itemList the itemList
	 */
	public void setItemList(List<String> itemList) {
		this.itemList = itemList;
	}

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

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "WmsRealTimeInventoryBalanceQueryRequest [itemList=" + itemList + ", inventory_sts=" + inventory_sts + "]";
	}

}
