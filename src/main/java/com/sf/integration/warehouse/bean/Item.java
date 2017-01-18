/**
 * @(#) Editor.java Created on 2014年8月25日
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.sf.integration.warehouse.bean;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.gionee.wms.common.JaxbUtil;
import com.sf.integration.warehouse.request.WmsMerchantCatalogBatchRequest;

/**
 * 商品
 * The class <code>Item</code>
 * @author dujz
 */
@XmlRootElement(name="item")
public class Item {
	
	/**
	 * 商品编码
	 */
	private String item;
	/**
	 * 商品名称
	 */
	private String description;
	/**
	 * 商品颜色
	 */
	private String item_color;
	/**
	 * 数量单位
	 */
	private String quantity_um_1;
	/**
	 * 存储模板
	 */
	private String storage_template;
	
	/**
	 * 入库imei跟踪 Y/N 
	 */
	private String serial_num_track_inbound;
	/**
	 * 出库imei跟踪 Y/N 
	 */
	private String serial_num_track_outbound;
	
	@XmlElement
	public String getItem() {
		return item;
	}
	public void setItem(String item) {
		this.item = item;
	}
	@XmlElement
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	@XmlElement
	public String getItem_color() {
		return item_color;
	}
	public void setItem_color(String item_color) {
		this.item_color = item_color;
	}
	@XmlElement
	public String getQuantity_um_1() {
		return quantity_um_1;
	}
	public void setQuantity_um_1(String quantity_um_1) {
		this.quantity_um_1 = quantity_um_1;
	}
	@XmlElement
	public String getStorage_template() {
		return storage_template;
	}
	public void setStorage_template(String storage_template) {
		this.storage_template = storage_template;
	}
	@XmlElement
	public String getSerial_num_track_inbound() {
		return serial_num_track_inbound;
	}
	public void setSerial_num_track_inbound(String serial_num_track_inbound) {
		this.serial_num_track_inbound = serial_num_track_inbound;
	}
	@XmlElement
	public String getSerial_num_track_outbound() {
		return serial_num_track_outbound;
	}
	public void setSerial_num_track_outbound(String serial_num_track_outbound) {
		this.serial_num_track_outbound = serial_num_track_outbound;
	}
	public static void main(String[] args) throws JAXBException {
		WmsMerchantCatalogBatchRequest request = new WmsMerchantCatalogBatchRequest();
		Item item = new Item();
		item.setItem("0001");
		item.setDescription("test");
		Item item2 = new Item();
		item2.setItem("0002");
		item2.setDescription("test2");
		System.out.println(JaxbUtil.marshToXmlBinding(Item.class, item));
		
		List<Item> list = new ArrayList<Item>();
		list.add(item);
		list.add(item2);
		request.setItemlist(list);
		
		System.out.println(JaxbUtil.marshToXmlBinding(WmsMerchantCatalogBatchRequest.class, request));
	}
}
