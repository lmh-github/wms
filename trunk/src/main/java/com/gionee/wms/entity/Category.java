package com.gionee.wms.entity;

import com.gionee.wms.common.WmsConstants;

public class Category {
	private Long id;
	private String catName;
	private String catDesc;
	private Long catPid;
	private String catPath;
	private Integer enabled;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCatName() {
		return catName;
	}

	public void setCatName(String catName) {
		this.catName = catName;
	}

	public String getCatDesc() {
		return catDesc;
	}

	public void setCatDesc(String catDesc) {
		this.catDesc = catDesc;
	}

	public Long getCatPid() {
		return catPid;
	}

	public void setCatPid(Long catPid) {
		this.catPid = catPid;
	}

	public String getCatPath() {
		return catPath;
	}

	public void setCatPath(String catPath) {
		this.catPath = catPath;
	}

	public Boolean isEnabled() {
		if (enabled != null) {
			return (WmsConstants.ENABLED_TRUE == enabled ? true : false);
		}
		return null;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled ? WmsConstants.ENABLED_TRUE : WmsConstants.ENABLED_FALSE;
	}

}
