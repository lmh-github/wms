package com.gionee.wms.entity;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.NotBlank;

public class PurPreRecv {
	private Long id;
	private String preRecvCode;// 预收编号
	private String postingNo;// 过账凭证号
	private String purchaseCode;// 采购编号
	private Date purPreparedTime;// 采购制单时间
	private String purPreparedBy;// 采购制单人
	private Long warehouseId;// 仓库ID
	private String warehouseCode;// 仓库编号
	private String warehouseName;// 仓库名称
	private Long supplierId;// 供应商ID
	private String supplierCode;// 供应商编号
	private String supplierName;// 供应商名称
	private Date preparedTime;// 制单时间
	private String preparedBy;// 制单人
	private Integer handlingStatus;// 处理状态(0未收货,2收货中,1已收货,-1已取消)
	private Date handledTime;// 经手时间
	private String handledBy;// 经手人
	private Long receiveId;// 收货单ID
	private String receiveCode;// 收货编号
	private String remark;// 备注

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPreRecvCode() {
		return preRecvCode;
	}

	public void setPreRecvCode(String preRecvCode) {
		this.preRecvCode = preRecvCode;
	}

	@NotBlank
	public String getPostingNo() {
		return postingNo;
	}

	public void setPostingNo(String postingNo) {
		this.postingNo = postingNo;
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

	public Date getPreparedTime() {
		return preparedTime;
	}

	public void setPreparedTime(Date preparedTime) {
		this.preparedTime = preparedTime;
	}

	public String getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}

	@NotNull
	public Date getPurPreparedTime() {
		return purPreparedTime;
	}

	public void setPurPreparedTime(Date purPreparedTime) {
		this.purPreparedTime = purPreparedTime;
	}

	@NotBlank
	public String getPurPreparedBy() {
		return purPreparedBy;
	}

	public void setPurPreparedBy(String purPreparedBy) {
		this.purPreparedBy = purPreparedBy;
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

	public Long getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(Long receiveId) {
		this.receiveId = receiveId;
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}

}
