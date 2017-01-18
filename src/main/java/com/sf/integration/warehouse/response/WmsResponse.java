/**
 * Project Name:wms
 * File Name:WmsResponse.java
 * Package Name:com.sf.integration.warehouse.response
 * Date:2014年8月26日下午3:37:08
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.sf.integration.warehouse.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月26日 下午3:37:08
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class WmsResponse {

	/** 处理结果 */
	@XmlElement
	@XmlJavaTypeAdapter(ResultAdapter.class)
	private Boolean result;
	/** 备注 */
	@XmlElement
	@XmlJavaTypeAdapter(CollapsedStringAdapter.class)
	private String remark;

	/**
	 * @return the result
	 */
	public Boolean getResult() {
		return result;
	}

	/**
	 * @param result the result
	 */
	public void setResult(Boolean result) {
		this.result = result;
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

}
