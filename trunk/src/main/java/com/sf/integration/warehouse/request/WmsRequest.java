/**
 * Project Name:wms
 * File Name:WmsRequest.java
 * Package Name:com.sf.integration.warehouse.request
 * Date:2014年8月26日下午2:07:52
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.sf.integration.warehouse.request;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月26日 下午2:07:52
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class WmsRequest {

	/** 校验字段 */
	@XmlElement
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String checkword;

	/**
	 * @return the checkword
	 */
	public String getCheckword() {
		return checkword;
	}

	/**
	 * @param checkword the checkword
	 */
	public void setCheckword(String checkword) {
		this.checkword = checkword;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "WmsRequest [checkword=" + checkword + "]";
	}

}
