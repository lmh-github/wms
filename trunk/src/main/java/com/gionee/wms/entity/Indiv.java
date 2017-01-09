package com.gionee.wms.entity;

import java.util.Date;

/**
 * 商品个体
 * 
 * @author kevin
 */
public class Indiv {
	private Long id;
	private String indivCode;// 个体身份编码，如IMEI码
	private String productBatchNo; // 批次号
	private Integer waresStatus; // 商品状态 1:良品 2:次品
	private Integer stockStatus;// 库存状态 1:在库 2:出库
	private String remark;// 备注
	private Long skuId;
	private String skuCode;
	private String skuName;
	private Long warehouseId;
	private String warehouseName;
	private Date inTime;
	private Long inId;
	private String inCode;
	private Date outTime;
	private Long outId;
	private String outCode;
	private Long orderId;
	private String orderCode;
	private Date rmaTime;
	private Long rmaId;
	private String rmaCode;
	private StockOut stockOut;
	private String measureUnit;
	private Integer rmaCount;// 退货次数
	private Integer pushStatus;// 推送状态
	private Date pushTime;// 推送时间
	private Integer pushCount;// 推送次数
	private String materialCode;//ERP物料编码
	private SalesOrder order;
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

	public String getProductBatchNo() {
		return productBatchNo;
	}

	public void setProductBatchNo(String productBatchNo) {
		this.productBatchNo = productBatchNo;
	}

	public Integer getWaresStatus() {
		return waresStatus;
	}

	public void setWaresStatus(Integer waresStatus) {
		this.waresStatus = waresStatus;
	}

	public Integer getStockStatus() {
		return stockStatus;
	}

	public void setStockStatus(Integer stockStatus) {
		this.stockStatus = stockStatus;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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

	public Long getInId() {
		return inId;
	}

	public void setInId(Long inId) {
		this.inId = inId;
	}

	public String getInCode() {
		return inCode;
	}

	public void setInCode(String inCode) {
		this.inCode = inCode;
	}

	public Date getInTime() {
		return inTime;
	}

	public void setInTime(Date inTime) {
		this.inTime = inTime;
	}

	public String getOutCode() {
		return outCode;
	}

	public void setOutCode(String outCode) {
		this.outCode = outCode;
	}

	public Date getOutTime() {
		return outTime;
	}

	public void setOutTime(Date outTime) {
		this.outTime = outTime;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getRmaCode() {
		return rmaCode;
	}

	public void setRmaCode(String rmaCode) {
		this.rmaCode = rmaCode;
	}

	public Date getRmaTime() {
		return rmaTime;
	}

	public void setRmaTime(Date rmaTime) {
		this.rmaTime = rmaTime;
	}

	public StockOut getStockOut() {
		return stockOut;
	}

	public void setStockOut(StockOut stockOut) {
		this.stockOut = stockOut;
	}

	public String getMeasureUnit() {
		return measureUnit;
	}

	public void setMeasureUnit(String measureUnit) {
		this.measureUnit = measureUnit;
	}

	public Integer getRmaCount() {
		return rmaCount;
	}

	public void setRmaCount(Integer rmaCount) {
		this.rmaCount = rmaCount;
	}

	public Integer getPushStatus() {
		return pushStatus;
	}

	public void setPushStatus(Integer pushStatus) {
		this.pushStatus = pushStatus;
	}

	public Date getPushTime() {
		return pushTime;
	}

	public void setPushTime(Date pushTime) {
		this.pushTime = pushTime;
	}

	public Integer getPushCount() {
		return pushCount;
	}

	public void setPushCount(Integer pushCount) {
		this.pushCount = pushCount;
	}

	public Long getOutId() {
		return outId;
	}

	public void setOutId(Long outId) {
		this.outId = outId;
	}

	public Long getRmaId() {
		return rmaId;
	}

	public void setRmaId(Long rmaId) {
		this.rmaId = rmaId;
	}

	public String getMaterialCode() {
		return materialCode;
	}

	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}

	public SalesOrder getOrder() {
		return order;
	}

	public void setOrder(SalesOrder order) {
		this.order = order;
	}

	public String getCaseCode() {
		return caseCode;
	}

	public void setCaseCode(String caseCode) {
		this.caseCode = caseCode;
	}
	
	

}
