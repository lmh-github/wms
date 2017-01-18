package com.gionee.wms.entity;

import java.util.Date;

public class Shipping {
	private Long id;
	private String shippingCode;// 配送方式编号
	private String shippingName;// 配送方式名称
	private String contact;// 配送方式联系人
	private String phone;// 配送方式电话
	private Integer defaultStatus;// 是否默认 (1:是 0:否)
	private Date createTime;// 创建时间
	private String remark;
	private Integer enabled; // 是否启用;
	private String templateName;//配送单模板文件名
	private String companyCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getShippingCode() {
		return shippingCode;
	}

	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Integer getDefaultStatus() {
		return defaultStatus;
	}

	public void setDefaultStatus(Integer defaultStatus) {
		this.defaultStatus = defaultStatus;
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

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	
}
