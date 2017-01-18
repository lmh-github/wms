package com.gionee.wms.entity;

public class SkuItem {
	private Long id;
	private String itemName;
	private Long attrId;
	private SkuAttr skuAttr;
	

	public SkuAttr getSkuAttr() {
		return skuAttr;
	}

	public void setSkuAttr(SkuAttr skuAttr) {
		this.skuAttr = skuAttr;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getAttrId() {
		return attrId;
	}

	public void setAttrId(Long attrId) {
		this.attrId = attrId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

}
