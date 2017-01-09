package com.gionee.wms.entity;

import java.util.ArrayList;
import java.util.List;

public class SkuAttr {
	private Long id;
	private String attrName;
	private AttrSet attrSet;
	private List<SkuItem> itemList = new ArrayList<SkuItem>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAttrName() {
		return attrName;
	}

	public void setAttrName(String attrName) {
		this.attrName = attrName;
	}

	public AttrSet getAttrSet() {
		return attrSet;
	}

	public void setAttrSet(AttrSet attrSet) {
		this.attrSet = attrSet;
	}

	public List<SkuItem> getItemList() {
		return itemList;
	}

	public void setItemList(List<SkuItem> itemList) {
		this.itemList = itemList;
	}

}
