package com.gionee.wms.entity;

import java.util.Date;

/**
 * 商品或者叫SPU(标准化产品单元)
 * 
 * @author kevin
 */
public class Wares {
	private Long id;
	private String waresName; // 商品名称
	private String waresCode; // 商品编码
	private String waresBrand; // 商品品牌
	private String measureUnit;// 计量单位
	private Integer indivEnabled; // 是否管理个体身份编码
	private Category category; // 商品分类
	private AttrSet attrSet; // 商品类型
	private Date createTime; // 创建时间
	private Integer enabled; // 是否启用
	private String waresModel;//商品规格型号
	private String waresRemark;//商品备注信息，用于发票备注

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWaresName() {
		return waresName;
	}

	public void setWaresName(String waresName) {
		this.waresName = waresName;
	}

	public String getWaresCode() {
		return waresCode;
	}

	public void setWaresCode(String waresCode) {
		this.waresCode = waresCode;
	}

	public String getWaresBrand() {
		return waresBrand;
	}

	public void setWaresBrand(String waresBrand) {
		this.waresBrand = waresBrand;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public AttrSet getAttrSet() {
		return attrSet;
	}

	public void setAttrSet(AttrSet attrSet) {
		this.attrSet = attrSet;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

//	public Boolean isEnabled() {
//		if (enabled != null) {
//			return (WmsConstants.ENABLED_TRUE == enabled ? true : false);
//		}
//		return null;
//	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Integer getIndivEnabled() {
		return indivEnabled;
	}

	public void setIndivEnabled(Integer indivEnabled) {
		this.indivEnabled = indivEnabled;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public String getWaresModel() {
		return waresModel;
	}

	public void setWaresModel(String waresModel) {
		this.waresModel = waresModel;
	}

	public String getWaresRemark() {
		return waresRemark;
	}

	public void setWaresRemark(String waresRemark) {
		this.waresRemark = waresRemark;
	}

}
