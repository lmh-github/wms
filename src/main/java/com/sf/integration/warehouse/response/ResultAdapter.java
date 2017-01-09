/**
 * Project Name:wms
 * File Name:ResultAdapter.java
 * Package Name:com.sf.integration.warehouse.response
 * Date:2014年8月26日下午3:53:19
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.sf.integration.warehouse.response;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import org.apache.commons.lang.BooleanUtils;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月26日 下午3:53:19
 */
public class ResultAdapter extends XmlAdapter<String, Boolean> {

	@Override
	public Boolean unmarshal(String v) throws Exception {
		return BooleanUtils.toBoolean(v, "1", "2");
	}

	@Override
	public String marshal(Boolean v) throws Exception {
		return v != null && v ? "1" : "2";
	}

}
