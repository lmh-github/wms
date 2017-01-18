/*
 * @(#)CategoryVo.java 2013-7-5
 *
 * Copyright 2013 Shenzhen Gionee,Inc. All rights reserved.
 */
package com.gionee.wms.vo;

/**
 * 商品分类和商品树形结构
 * WMS与ECSop对接用
 * @author ZuoChangjun 2013-7-5
 */
public class CategoryVo {
	private Long id;// 主键 商品分类ID或商品ID
	private Long catPid; // 父ID
	private String catName;// 商品分类名或商品名
	private String waresCode;// 商品编码,此字段只针对isLeaf=1有效
	private int isLeaf;// 0:商品分类，1：商品
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getCatPid() {
		return catPid;
	}
	public void setCatPid(Long catPid) {
		this.catPid = catPid;
	}
	public String getCatName() {
		return catName;
	}
	public void setCatName(String catName) {
		this.catName = catName;
	}
	public String getWaresCode() {
		return waresCode;
	}
	public void setWaresCode(String waresCode) {
		this.waresCode = waresCode;
	}
	public int getIsLeaf() {
		return isLeaf;
	}
	public void setIsLeaf(int isLeaf) {
		this.isLeaf = isLeaf;
	}

}
