package com.gionee.wms.entity;

import java.util.Date;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * 商品SKU
 * 
 * @author kevin
 */
public class Sku {
	private Long id;
	private String skuName; // sku名称
	private String skuCode;// sku编码
	private String skuBarcode;// sku条形码（EAN码）
	private String itemIds;// sku属性可选项的id串
	private String materialCode;//ERP物料编号
	private String remark;
	private Date createTime; // 创建时间
	private Integer enabled; // 是否启用
	private Wares wares;// 商品
	// private Supplier supplier;// 供应商
	private List<SkuItem> itemList = Lists.newArrayList();
	private String attrInfo;// sku属性信息
	/** 组合SKU */
	private List<SkuBomDetail> skuBomList = Lists.newArrayList();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuBarcode() {
		return skuBarcode;
	}

	public void setSkuBarcode(String skuBarcode) {
		this.skuBarcode = skuBarcode;
	}

	public String getItemIds() {
		return itemIds;
	}

	public void setItemIds(String itemIds) {
		this.itemIds = itemIds;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Wares getWares() {
		return wares;
	}

	public void setWares(Wares wares) {
		this.wares = wares;
	}

	public List<SkuItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<SkuItem> itemList) {
		this.itemList = itemList;
	}

	public String getAttrInfo() {
		return attrInfo;
	}

	public void setAttrInfo(String attrInfo) {
		this.attrInfo = attrInfo;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	/**
	 * @return the skuBomList
	 */
	public List<SkuBomDetail> getSkuBomList() {
		return skuBomList;
	}

	/**
	 * @param skuBomList the skuBomList
	 */
	public void setSkuBomList(List<SkuBomDetail> skuBomList) {
		this.skuBomList = skuBomList;
	}
	
	

}
