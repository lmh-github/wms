/**
 * Project Name:wms
 * File Name:ManualReissueOrder.java
 * Package Name:com.gionee.wms.entity
 * Date:2016年10月8日上午10:32:37
 * Copyright (c) 2016 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.entity;

import java.util.Date;
import java.util.List;

/**
 * 手工补发订单
 * @author PengBin 00001550<br>
 * @date 2016年10月8日 上午10:32:37
 */
public class ManualReissueOrder {

	private Long id;

	private Integer platform;

	private String orderCode;

	private String newOrderCode;

	private Integer invoice;

	private String billType;

	private String remark;

	private String extension;

	private Integer status;

	private Date createTime;

	private String createBy;

	private Date updateTime;

	private String updateBy;

	private List<ManualReissueOrderGoods> goods;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the platform
	 */
	public Integer getPlatform() {
		return platform;
	}

	/**
	 * @param platform the platform
	 */
	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	/**
	 * @return the orderCode
	 */
	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * @param orderCode the orderCode
	 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	/**
	 * @return the newOrderCode
	 */
	public String getNewOrderCode() {
		return newOrderCode;
	}

	/**
	 * @param newOrderCode the newOrderCode
	 */
	public void setNewOrderCode(String newOrderCode) {
		this.newOrderCode = newOrderCode;
	}

	/**
	 * @return the invoice
	 */
	public Integer getInvoice() {
		return invoice;
	}

	/**
	 * @param invoice the invoice
	 */
	public void setInvoice(Integer invoice) {
		this.invoice = invoice;
	}

	/**
	 * @return the billType
	 */
	public String getBillType() {
		return billType;
	}

	/**
	 * @param billType the billType
	 */
	public void setBillType(String billType) {
		this.billType = billType;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the createTime
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * @param createTime the createTime
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * @return the createBy
	 */
	public String getCreateBy() {
		return createBy;
	}

	/**
	 * @param createBy the createBy
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * @return the updateTime
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * @param updateTime the updateTime
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	/**
	 * @return the updateBy
	 */
	public String getUpdateBy() {
		return updateBy;
	}

	/**
	 * @param updateBy the updateBy
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * @return the goods
	 */
	public List<ManualReissueOrderGoods> getGoods() {
		return goods;
	}

	/**
	 * @param goods the goods
	 */
	public void setGoods(List<ManualReissueOrderGoods> goods) {
		this.goods = goods;
	}

}
