package com.gionee.wms.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class Purchase {
	private Long id;
	private String purchaseCode;// 采购编号
	private Long warehouseId;// 仓库ID
	private String warehouseCode;// 仓库编号
	private String warehouseName;// 仓库名称
	private Long supplierId;// 供应商ID
	private String supplierCode;// 供应商编号
	private String supplierName;// 供应商名称
	private Date preparedTime;// 制单时间
	private String preparedBy;// 制单人
	private Date joinTime;// 加入时间
	private Integer handlingStatus;// 处理状态(0未收货,2收货中,1已收货,-1已取消)
	private Date handledTime;// 经手时间
	private String handledBy;// 经手人
	private String remark;// 备注

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@NotBlank
	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	@NotBlank
	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public String getWarehouseName() {
		return warehouseName;
	}

	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getSupplierCode() {
		return supplierCode;
	}

	public void setSupplierCode(String supplierCode) {
		this.supplierCode = supplierCode;
	}

	public String getSupplierName() {
		return supplierName;
	}

	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}

	@NotNull
	public Date getPreparedTime() {
		return preparedTime;
	}

	public void setPreparedTime(Date preparedTime) {
		this.preparedTime = preparedTime;
	}

	@NotBlank
	public String getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}

	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	public Integer getHandlingStatus() {
		return handlingStatus;
	}

	public void setHandlingStatus(Integer handlingStatus) {
		this.handlingStatus = handlingStatus;
	}

	public Date getHandledTime() {
		return handledTime;
	}

	public void setHandledTime(Date handledTime) {
		this.handledTime = handledTime;
	}

	public String getHandledBy() {
		return handledBy;
	}

	public void setHandledBy(String handledBy) {
		this.handledBy = handledBy;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
