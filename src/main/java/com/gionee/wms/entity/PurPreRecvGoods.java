package com.gionee.wms.entity;

import java.util.List;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class PurPreRecvGoods {
	private Long id;
	private Long skuId;
	private String skuCode;
	private String skuName;
	private String materialCode;
	private Integer quantity;
	private String measureUnit;
	private String productBatchNo;
	private Integer indivEnabled; // 是否需绑定个体
	private List<String> indivCodeList;
	private PurPreRecv purPreRecv;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	@NotBlank
	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	@NotNull
	@Min(1)
	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public String getProductBatchNo() {
		return productBatchNo;
	}

	public void setProductBatchNo(String productBatchNo) {
		this.productBatchNo = productBatchNo;
	}

	public Integer getIndivEnabled() {
		return indivEnabled;
	}

	public void setIndivEnabled(Integer indivEnabled) {
		this.indivEnabled = indivEnabled;
	}

	public List<String> getIndivCodeList() {
		return indivCodeList;
	}

	public void setIndivCodeList(List<String> indivCodeList) {
		this.indivCodeList = indivCodeList;
	}

	public PurPreRecv getPurPreRecv() {
		return purPreRecv;
	}

	public void setPurPreRecv(PurPreRecv purPreRecv) {
		this.purPreRecv = purPreRecv;
	}

}
