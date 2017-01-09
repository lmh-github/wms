/**
 * @(#) Editor.java Created on 2014年8月26日
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.sf.integration.warehouse.response;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.sf.integration.warehouse.bean.Item;

/**
 * 批量同步商品信息到顺丰侧返回信息
 * The class <code>WmsMerchantCatalogBatchResponse</code>
 * @author dujz
 */
@XmlRootElement
public class WmsMerchantCatalogBatchResponse extends WmsResponse{
	/**
	 * 失败商品信息
	 */
	@XmlElementWrapper(name="itemlist") 
	@XmlElement(name="item") 
	private List<Item> itemlist;
	public List<Item> getItemlist() {
		return itemlist;
	}
	public void setItemlist(List<Item> itemlist) {
		this.itemlist = itemlist;
	}
}
