package com.gionee.wms.entity;

import java.util.List;

import com.google.common.collect.Lists;

public class AttrSet {
	private Long id;
	private String attrSetName;
	private String remark;
	private List<SkuAttr> attrList = Lists.newArrayList();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getAttrSetName() {
		return attrSetName;
	}

	public void setAttrSetName(String attrSetName) {
		this.attrSetName = attrSetName;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<SkuAttr> getAttrList() {
		return attrList;
	}

	public void setAttrList(List<SkuAttr> attrList) {
		this.attrList = attrList;
	}

}
