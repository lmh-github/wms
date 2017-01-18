/*
 * @(#)ShippingParamVo.java 2013-7-30
 *
 * Copyright 2013 Shenzhen Gionee,Inc. All rights reserved.
 */
package com.gionee.wms.vo;

import com.gionee.wms.common.WmsConstants;

/**
 * 快递单查询参数
 * @author ZuoChangjun 2013-7-30
 */
public class ShippingParamVo {
private String key;//身份授权key,向快递100申请的key
private String comCode;//要查询的快递公司代码，不支持中文
private String billCode;//发货单编号,要查询的快递单号
private String validCode;//查询快递的电话号码，目前只有佳吉物流需要这个参数，其他公司请忽略 
private String resultType;//返回类型     0：返回json字符串，1：返回xml对象，2：返回html对象，3：返回text文本。如果不填，默认返回json字符串。 
private String isMultiLine;//返回行数   1:返回多行完整的信息，0:只返回一行信息。
private String order;//排序 desc：按时间由新到旧排列，asc：按时间由旧到新排列

public String getKey() {
	return key;
}
public void setKey(String key) {
	this.key = key;
}

public String getComCode() {
	return comCode;
}
public void setComCode(String comCode) {
	this.comCode = (WmsConstants.shippingComCodeMap.get(comCode)==null?comCode:WmsConstants.shippingComCodeMap.get(comCode));
}
public String getBillCode() {
	return billCode;
}
public void setBillCode(String billCode) {
	this.billCode = billCode;
}

public String getValidCode() {
	return validCode;
}
public void setValidCode(String validCode) {
	this.validCode = validCode;
}
public String getResultType() {
	return resultType;
}
public void setResultType(String resultType) {
	this.resultType = resultType;
}
public String getIsMultiLine() {
	return isMultiLine;
}
public void setIsMultiLine(String isMultiLine) {
	this.isMultiLine = isMultiLine;
}
public String getOrder() {
	return order;
}
public void setOrder(String order) {
	this.order = order;
}
}
