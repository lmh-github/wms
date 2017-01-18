package com.gionee.wms.entity;

import java.math.BigDecimal;
import java.util.Date;

import com.gionee.wms.common.StringUtils;

public class SalesOrder {
	private Long id;
	private String orderCode;// 订单编号
	private String orderSource;//订单来源
	private String orderUser;// 下单人
	private Date orderTime; // 下单时间
	private Integer paymentType;// 支付类型(1:在线支付 2:货到付款)
	private String paymentCode;// 支付方式编号
	private String paymentName;// 支付方式名称
	private Date paymentTime;//支付时间
	private String payNo;// 支付流水
	private BigDecimal paidAmount;// 支付金额
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
	private Long shippingId;// 配送方式ID
	private String shippingName;// 配送方式名称
	private String shippingNo;// 配送单号
	private Integer invoiceEnabled;// 是否需要发票
	private Integer invoiceType;// 发票类型
	private String invoiceTitle;// 发票抬头
	private BigDecimal invoiceAmount;// 发票金额
	private String invoiceContent;// 发票内容
	private Integer invoiceStatus;// 是否已出发票
	private String postscript;// 订单用户附言
	private BigDecimal goodsAmount;// 商品总金额
	private BigDecimal orderAmount;// 订单总金额
	private BigDecimal payableAmount;// 应付金额
	private Date joinTime;// 订单接收时间
	private Integer orderStatus;
	// private String payStatus;// 支付状态(0未付款,1付款中,2已付款,3退款中,4已退款)
//	private Integer shippingStatus;// 配送状态(0未发货,4,配货中,1已发货,-1已取消)
	private Date shippingTime;//发货时间
	private String stockOutCode;// 出库编号
	private Integer orderNotifyStatus;// 订单状态通知情况(0未通知,1已通知)
	private Date orderNotifyTime;// 订单状态通知时间
	private Integer orderNotifyCount;// 订单状态通知次数
//	private Integer shippingNotifyStatus;// 配送状态通知情况(0未通知,1已通知)
//	private Date shippingNotifyTime;// 配送状态通知时间
//	private Integer shippingNotifyCount;// 配送状态通知次数
	private String remark;
	private String deliveryCode;//发货单流水号
	private String barCodeImgPath;//条码图片路径
	private String barCodeImgBase64;//条码图片base64
	private Long batchId;//发货批次ID
	private String batchCode;//发货批次号
	private String weight;
	private String handledBy;	// 处理人
	private Date handledTime;	// 处理时间
	private String preparedBy;
	private Date preparedTime;
	private String remarkBacked;
	private String remarkBacking;
	private Integer orderPushStatus; // 订单推送状态@see com.gionee.wms.common.WmsConstants.OrderPushStatusEnum
	private Date orderPushTime;// 订单推送时间

	/**
	 * 取完整地址
	 */
	public String getFullAddress() {
		return StringUtils.handleNullString(this.province)
				+ StringUtils.handleNullString(this.city)
				+ StringUtils.handleNullString(this.district)
				+ StringUtils.handleNullString(this.address);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderUser() {
		return orderUser;
	}

	public void setOrderUser(String orderUser) {
		this.orderUser = orderUser;
	}

	public Date getOrderTime() {
		return orderTime;
	}

	public void setOrderTime(Date orderTime) {
		this.orderTime = orderTime;
	}

	public String getOrderSource() {
		return orderSource;
	}

	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	public Integer getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(Integer paymentType) {
		this.paymentType = paymentType;
	}

	public String getPaymentCode() {
		return paymentCode;
	}

	public void setPaymentCode(String paymentCode) {
		this.paymentCode = paymentCode;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public BigDecimal getPaidAmount() {
		return paidAmount;
	}

	public void setPaidAmount(BigDecimal paidAmount) {
		this.paidAmount = paidAmount;
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

	public Integer getInvoiceEnabled() {
		return invoiceEnabled;
	}

	public void setInvoiceEnabled(Integer invoiceEnabled) {
		this.invoiceEnabled = invoiceEnabled;
	}

	public Integer getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(Integer invoiceType) {
		this.invoiceType = invoiceType;
	}

	public String getInvoiceTitle() {
		return invoiceTitle;
	}

	public void setInvoiceTitle(String invoiceTitle) {
		this.invoiceTitle = invoiceTitle;
	}

	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}

	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}

	public String getInvoiceContent() {
		return invoiceContent;
	}

	public void setInvoiceContent(String invoiceContent) {
		this.invoiceContent = invoiceContent;
	}

	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}

	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}

	public String getPostscript() {
		return postscript;
	}

	public void setPostscript(String postscript) {
		this.postscript = postscript;
	}

	public BigDecimal getGoodsAmount() {
		return goodsAmount;
	}

	public void setGoodsAmount(BigDecimal goodsAmount) {
		this.goodsAmount = goodsAmount;
	}

	public BigDecimal getOrderAmount() {
		return orderAmount;
	}

	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}

	public BigDecimal getPayableAmount() {
		return payableAmount;
	}

	public void setPayableAmount(BigDecimal payableAmount) {
		this.payableAmount = payableAmount;
	}

	public Date getJoinTime() {
		return joinTime;
	}

	public void setJoinTime(Date joinTime) {
		this.joinTime = joinTime;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}

//	public Integer getShippingStatus() {
//		return shippingStatus;
//	}
//
//	public void setShippingStatus(Integer shippingStatus) {
//		this.shippingStatus = shippingStatus;
//	}

	public String getStockOutCode() {
		return stockOutCode;
	}

	public void setStockOutCode(String stockOutCode) {
		this.stockOutCode = stockOutCode;
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

	public Date getShippingTime() {
		return shippingTime;
	}

	public void setShippingTime(Date shippingTime) {
		this.shippingTime = shippingTime;
	}

	public Integer getOrderNotifyStatus() {
		return orderNotifyStatus;
	}

	public void setOrderNotifyStatus(Integer orderNotifyStatus) {
		this.orderNotifyStatus = orderNotifyStatus;
	}

	public Date getOrderNotifyTime() {
		return orderNotifyTime;
	}

	public void setOrderNotifyTime(Date orderNotifyTime) {
		this.orderNotifyTime = orderNotifyTime;
	}

	public Integer getOrderNotifyCount() {
		return orderNotifyCount;
	}

	public void setOrderNotifyCount(Integer orderNotifyCount) {
		this.orderNotifyCount = orderNotifyCount;
	}

	public String getDeliveryCode() {
		return deliveryCode;
	}

	public void setDeliveryCode(String deliveryCode) {
		this.deliveryCode = deliveryCode;
	}

	public String getBarCodeImgPath() {
		return barCodeImgPath;
	}

	public void setBarCodeImgPath(String barCodeImgPath) {
		this.barCodeImgPath = barCodeImgPath;
	}

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
	}

	public Long getBatchId() {
		return batchId;
	}

	public void setBatchId(Long batchId) {
		this.batchId = batchId;
	}

	public String getBarCodeImgBase64() {
		return barCodeImgBase64;
	}

	public void setBarCodeImgBase64(String barCodeImgBase64) {
		this.barCodeImgBase64 = barCodeImgBase64;
	}

	public String getWeight() {
		return weight;
	}

	public void setWeight(String weight) {
		this.weight = weight;
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

	public String getRemarkBacked() {
		return remarkBacked;
	}

	public void setRemarkBacked(String remarkBacked) {
		this.remarkBacked = remarkBacked;
	}

	public String getRemarkBacking() {
		return remarkBacking;
	}

	public void setRemarkBacking(String remarkBacking) {
		this.remarkBacking = remarkBacking;
	}

	public Date getPaymentTime() {
		return paymentTime;
	}

	public void setPaymentTime(Date paymentTime) {
		this.paymentTime = paymentTime;
	}

	/**
	 * @return the orderPushStatus
	 */
	public Integer getOrderPushStatus() {
		return orderPushStatus;
	}

	/**
	 * @param orderPushStatus the orderPushStatus
	 */
	public void setOrderPushStatus(Integer orderPushStatus) {
		this.orderPushStatus = orderPushStatus;
	}

	public Date getOrderPushTime() {
		return orderPushTime;
	}

	public void setOrderPushTime(Date orderPushTime) {
		this.orderPushTime = orderPushTime;
	}

}
