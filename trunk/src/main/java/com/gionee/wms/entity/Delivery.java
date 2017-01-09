package com.gionee.wms.entity;

import java.util.Date;

public class Delivery {
	private Long id;
	private String deliveryCode;// 发货单流水号
	private Long batchId;// 发货批次ID
	private String batchCode;// 发货批次流水号
	private Long warehouseId;// 发货仓库ID
	private String warehouseName;// 发货仓库名称
	private Long originalId;//源单ID
	private String originalCode;// 源单号
	private Integer paymentType;// 支付类型(1:在线支付 2:货到付款)
	private String consignee;// 收货人姓名
	private String province;// 收货人的省份
	private String city;// 收货人的城市
	private String district;// 收货人的地区
	private String address;// 收货人的详细地址
	private String fullAddress;// 完整收货地址(province+city+district+address)
	private String zipcode;// 邮编
	private String tel;// 收货电话
	private String mobile;// 收货手机
	private String bestTime;// 最佳送货时间
	private String postscript;// 订单用户附言
	private Long shippingId;// 配送方式ID
	private String shippingName;// 配送方式名称
	private String shippingNo;// 配送单号
	private Integer invoiceEnabled;// 是否需要发票
	private Integer invoiceStatus;// 是否已出发票
	private Integer handlingStatus;// 发货状态(0未发货,1已发货,-1已取消)
	private String handledBy;// 发货人
	private Date handledTime;// 发货时间
	private String preparedBy;// 制单人
	private Date preparedTime;// 制单时间
	private String remark;
	private String barCodeImgPath;// 条形码图片相对路径

	/**
	 * 取完整地址
	 */
	public String getFullAddress() {
		return this.province + this.city + this.district + this.address;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
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

	public Long getOriginalId() {
		return originalId;
	}

	public void setOriginalId(Long originalId) {
		this.originalId = originalId;
	}

	public String getOriginalCode() {
		return originalCode;
	}

	public void setOriginalCode(String originalCode) {
		this.originalCode = originalCode;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getBestTime() {
		return bestTime;
	}

	public void setBestTime(String bestTime) {
		this.bestTime = bestTime;
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}

	public Long getShippingId() {
		return shippingId;
	}

	public void setShippingId(Long shippingId) {
		this.shippingId = shippingId;
	}

	public String getShippingName() {
		return shippingName;
	}

	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}

	public String getShippingNo() {
		return shippingNo;
	}

	public void setShippingNo(String shippingNo) {
		this.shippingNo = shippingNo;
	}

	public Integer getHandlingStatus() {
		return handlingStatus;
	}

	public void setHandlingStatus(Integer handlingStatus) {
		this.handlingStatus = handlingStatus;
	}

	public String getHandledBy() {
		return handledBy;
	}

	public void setHandledBy(String handledBy) {
		this.handledBy = handledBy;
	}

	public Date getHandledTime() {
		return handledTime;
	}

	public void setHandledTime(Date handledTime) {
		this.handledTime = handledTime;
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

	public void setFullAddress(String fullAddress) {
		this.fullAddress = fullAddress;
	}

	public Integer getInvoiceEnabled() {
		return invoiceEnabled;
	}

	public void setInvoiceEnabled(Integer invoiceEnabled) {
		this.invoiceEnabled = invoiceEnabled;
	}

	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBarCodeImgPath() {
		return barCodeImgPath;
	}

	public void setBarCodeImgPath(String barCodeImgPath) {
		this.barCodeImgPath = barCodeImgPath;
	}

}
