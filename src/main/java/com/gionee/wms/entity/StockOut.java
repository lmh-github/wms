package com.gionee.wms.entity;

import java.util.Date;

public class StockOut {
	private Long id;
	private String stockOutCode;// 出库编号
	private String stockOutType;// 出库类型
	private String originalCode;// 源单号
	private String handledBy;// 经手人
	private Date handledDate;// 出库日期
	private String preparedBy;// 制单人
	private Date preparedTime;// 制单时间
	private Date finishedTime;// 完结时间
	private String handlingStatus;// 处理状态
	private Integer enabled; // 是否可用
	private String remark;// 备注
	private Long warehouseId;
	private String warehouseCode;// 仓库编号
	private String warehouseName;// 仓库名称

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getStockOutCode() {
		return stockOutCode;
	}

	public void setStockOutCode(String stockOutCode) {
		this.stockOutCode = stockOutCode;
	}

	public String getStockOutType() {
		return stockOutType;
	}

	public void setStockOutType(String stockOutType) {
		this.stockOutType = stockOutType;
	}

	public String getOriginalCode() {
		return originalCode;
	}

	public void setOriginalCode(String originalCode) {
		this.originalCode = originalCode;
	}

	public String getHandledBy() {
		return handledBy;
	}

	public void setHandledBy(String handledBy) {
		this.handledBy = handledBy;
	}

	public Date getHandledDate() {
		return handledDate;
	}

	public void setHandledDate(Date handledDate) {
		this.handledDate = handledDate;
	}

	public String getPreparedBy() {
		return preparedBy;
	}

	public void setPreparedBy(String preparedBy) {
		this.preparedBy = preparedBy;
	}

	public Date getPreparedTime() {
		return preparedTime;
	}

	public void setPreparedTime(Date preparedTime) {
		this.preparedTime = preparedTime;
	}

	public String getHandlingStatus() {
		return handlingStatus;
	}

	public void setHandlingStatus(String handlingStatus) {
		this.handlingStatus = handlingStatus;
	}

	public Date getFinishedTime() {
		return finishedTime;
	}

	public void setFinishedTime(Date finishedTime) {
		this.finishedTime = finishedTime;
	}

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

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

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
}
