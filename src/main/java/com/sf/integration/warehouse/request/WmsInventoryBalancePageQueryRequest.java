package com.sf.integration.warehouse.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 库存查询接口(分页查询)
 * 请求报文
 * @author PengBin 00001550<br>
 * @date 2014年8月20日 下午2:10:13
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "wmsInventoryBalancePageQueryRequest")
public class WmsInventoryBalancePageQueryRequest extends WmsRequest {

	/** 货主 */
	@XmlElement
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String company;

	/** SKU 代码 */
	@XmlElement
	protected String item;

	/** 仓库 */
	@XmlElement
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String warehouse;

	/** 库存状态 */
	@XmlElement(name = "inventory_sts")
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	protected String inventorySts;

	/** 指定页 */
	@XmlElement(name = "page_index")
	protected Integer pageIndex;

	/**
	 * Gets the value of the company property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getCompany() {
		return company;
	}

	/**
	 * Sets the value of the company property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setCompany(String value) {
		this.company = value;
	}

	/**
	 * Gets the value of the item property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getItem() {
		return item;
	}

	/**
	 * Sets the value of the item property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setItem(String value) {
		this.item = value;
	}

	/**
	 * Gets the value of the warehouse property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getWarehouse() {
		return warehouse;
	}

	/**
	 * Sets the value of the warehouse property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setWarehouse(String value) {
		this.warehouse = value;
	}

	/**
	 * Gets the value of the inventorySts property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link String }
	 *     
	 */
	public String getInventorySts() {
		return inventorySts;
	}

	/**
	 * Sets the value of the inventorySts property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setInventorySts(String value) {
		this.inventorySts = value;
	}

	/**
	 * Gets the value of the pageIndex property.
	 * 
	 * @return
	 *     possible object is
	 *     {@link Integer }
	 *     
	 */
	public Integer getPageIndex() {
		return pageIndex;
	}

	/**
	 * Sets the value of the pageIndex property.
	 * 
	 * @param value
	 *     allowed object is
	 *     {@link String }
	 *     
	 */
	public void setPageIndex(Integer value) {
		this.pageIndex = value;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "WmsInventoryBalancePageQueryRequest [company=" + company + ", item=" + item + ", warehouse=" + warehouse + ", inventorySts=" + inventorySts + ", pageIndex=" + pageIndex + "]";
	}

}
