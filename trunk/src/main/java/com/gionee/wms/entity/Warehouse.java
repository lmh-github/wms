package com.gionee.wms.entity;

import java.util.Date;

/**
 * 仓库实体
 * 
 * @author kevin
 */
public class Warehouse {
	private Long id;
	private String warehouseCode;// 仓库编号
	private String warehouseName;// 仓库名称
	private String warehouseAddress;// 仓库地址
	private String warehousePhone;// 仓库电话
	private String warehouseContact;// 仓库联系人
	private String warehouseType;// 仓库类型 (1:实仓 0:虚仓)
	private Integer defaultStatus;// 是否默认 (1:是 0:否)
	private Date createTime;// 创建时间
	private String remark;
	private Integer enabled; // 是否启用;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
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
	public String getWarehouseAddress() {
		return warehouseAddress;
	}
	public void setWarehouseAddress(String warehouseAddress) {
		this.warehouseAddress = warehouseAddress;
	}
	public String getWarehousePhone() {
		return warehousePhone;
	}
	public void setWarehousePhone(String warehousePhone) {
		this.warehousePhone = warehousePhone;
	}
	public String getWarehouseContact() {
		return warehouseContact;
	}
	public void setWarehouseContact(String warehouseContact) {
		this.warehouseContact = warehouseContact;
	}
	public String getWarehouseType() {
		return warehouseType;
	}
	public void setWarehouseType(String warehouseType) {
		this.warehouseType = warehouseType;
	}
	public Integer getDefaultStatus() {
		return defaultStatus;
	}
	public void setDefaultStatus(Integer defaultStatus) {
		this.defaultStatus = defaultStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
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

	

}
