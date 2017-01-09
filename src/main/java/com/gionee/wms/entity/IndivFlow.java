package com.gionee.wms.entity;

import java.util.Date;

public class IndivFlow {
	private Long id;
	private String indivCode;// 商品编码
	private Long skuId;
	private String skuCode;
	private String skuName;
	private Long warehouseId;
	private String warehouseName;
	private String flowType;// 流转类型 1:采购入库 2:销售出库 3:退货入库
	private Long flowId;
	private Date flowTime;// 流转时间
	private String flowCode;// 流转单编号
	private Long flowGoodsId;// 流转单商品ID
	private String productBatchNo;// 生产批次号
	private String measureUnit;// 计量单位
	private Integer waresStatus;// 商品状态 1：良品 2：次品
	private String remark;
	private Integer enabled;
	private String caseCode;//箱号

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIndivCode() {
		return indivCode;
	}

	public void setIndivCode(String indivCode) {
		this.indivCode = indivCode;
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

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public String getFlowType() {
		return flowType;
	}

	public void setFlowType(String flowType) {
		this.flowType = flowType;
	}

	public Date getFlowTime() {
		return flowTime;
	}

	public void setFlowTime(Date flowTime) {
		this.flowTime = flowTime;
	}

	public String getFlowCode() {
		return flowCode;
	}

	public void setFlowCode(String flowCode) {
		this.flowCode = flowCode;
	}

	public Long getFlowGoodsId() {
		return flowGoodsId;
	}

	public void setFlowGoodsId(Long flowGoodsId) {
		this.flowGoodsId = flowGoodsId;
	}

	public String getProductBatchNo() {
		return productBatchNo;
	}

	public void setProductBatchNo(String productBatchNo) {
		this.productBatchNo = productBatchNo;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Integer getWaresStatus() {
		return waresStatus;
	}

	public void setWaresStatus(Integer waresStatus) {
		this.waresStatus = waresStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public Long getFlowId() {
		return flowId;
	}

	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	public String getCaseCode() {
		return caseCode;
	}

	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}
	
}
