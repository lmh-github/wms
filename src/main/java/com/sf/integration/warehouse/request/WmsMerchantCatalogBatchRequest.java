/**
 * @(#) Editor.java Created on 2014年8月25日
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.sf.integration.warehouse.request;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.gionee.wms.common.WmsConstants;
import com.sf.integration.warehouse.bean.Item;

/**
 * 顺丰仓储的客户系统上传商品目录，批量传入多个商品信息
 * The class <code>WmsMerchantCatalogBatchRequest</code>
 * @author dujz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement
public class WmsMerchantCatalogBatchRequest extends WmsRequest{
	/**
	 * 货主(公司)
	 */
	@XmlElement
	private String company = WmsConstants.SF_COMPANY;
	/**
	 * 接口动作
	 * 注释:新建为NEW，修改为SAVE  
	 */
	@XmlElement
	private String interface_action_code;
	/**
	 * 商品列表
	 */
	@XmlElementWrapper(name="itemlist") 
    @XmlElement(name="item")
	private List<Item> itemlist;
	
	
	public String getCompany() {
		return WmsConstants.SF_COMPANY;
	}
	
	public void setCompany(String company) {
		this.company = company;
	}
	
	public String getInterface_action_code() {
		return interface_action_code;
	}
	public void setInterface_action_code(String interface_action_code) {
		this.interface_action_code = interface_action_code;
	}
	public List<Item> getItemist() {
		return itemlist;
	}
	public void setItemlist(List<Item> itemlist) {
		this.itemlist = itemlist;
	}
	
}
