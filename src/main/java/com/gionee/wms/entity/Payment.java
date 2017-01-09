package com.gionee.wms.entity;

public class Payment {
private Long id;
private String paymentCode;//支付方式编号
private String paymentName;//支付方式名称
private String paymentType;//支付方式类型(在线支付 货到付款)
public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
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
public String getPaymentType() {
	return paymentType;
}
public void setPaymentType(String paymentType) {
	this.paymentType = paymentType;
}

}
