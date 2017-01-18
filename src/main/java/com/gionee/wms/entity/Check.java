package com.gionee.wms.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.gionee.wms.common.WmsConstants;

public class Check {
	private Long id;
	private String checkCode; // 盘点单编号
	private Long warehouseId;
	private String warehouseName;
	// private String checkResult;// 全盘结果 1:无盈亏 2:有盈亏
	private Date plannedTime;// 计划时间
	private Date firstTime;// 预盘时间
	private Date secondTime;// 复盘时间
	private Date confirmTime;// 盘点结果确认时间
	private Integer enabled; // 是否可用
	private Date preparedTime;// 制单时间
	private String preparedBy;// 制单人
	private Integer handlingStatus;// 盘点单状态 1:待盘点 2:已预盘 3:已复盘 0:已确认
	private Date handledTime;
	private String handledBy;
	private String remark;// 备注
	private Warehouse warehouse;// 盘点仓库
	private List<CheckGoods> checkDetail = new ArrayList<CheckGoods>();// 盘点物品明细
	private String checkType;// 盘点类型 1：配件盘点，2：个体盘点
	private Integer auditStatus;// 审核状态 0:待审核 ，1-998：审核中，999：已审核
	private Integer stockDumpStatus;// 盘点单是否已经做过库存信息备份,1:已备份；0：未备份

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getPlannedTime() {
		return plannedTime;
	}

	public void setPlannedTime(Date plannedTime) {
		this.plannedTime = plannedTime;
	}

	public String getCheckCode() {
		return checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public Warehouse getWarehouse() {
		return warehouse;
	}

	public void setWarehouse(Warehouse warehouse) {
		this.warehouse = warehouse;
	}

	public Integer getHandlingStatus() {
		return handlingStatus;
	}

	public void setHandlingStatus(Integer handlingStatus) {
		this.handlingStatus = handlingStatus;
	}

	public Date getFirstTime() {
		return firstTime;
	}

	public void setFirstTime(Date firstTime) {
		this.firstTime = firstTime;
	}

	public Date getSecondTime() {
		return secondTime;
	}

	public void setSecondTime(Date secondTime) {
		this.secondTime = secondTime;
	}

	public Date getConfirmTime() {
		return confirmTime;
	}

	public void setConfirmTime(Date confirmTime) {
		this.confirmTime = confirmTime;
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

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public List<CheckGoods> getCheckDetail() {
		return checkDetail;
	}

	public void setCheckDetail(List<CheckGoods> checkDetail) {
		this.checkDetail = checkDetail;
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

	public Integer getEnabled() {
		return enabled;
	}

	public void setEnabled(Integer enabled) {
		this.enabled = enabled;
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

	public String getCheckType() {
		return checkType;
	}

	public void setCheckType(String checkType) {
		this.checkType = checkType;
	}

	public Integer getAuditStatus() {
		return auditStatus;
	}

	public void setAuditStatus(Integer auditStatus) {
		this.auditStatus = auditStatus;
	}

	public Integer getStockDumpStatus() {
		return stockDumpStatus;
	}

	public void setStockDumpStatus(Integer stockDumpStatus) {
		this.stockDumpStatus = stockDumpStatus;
	}

}
