package com.gionee.wms.dto;

import java.io.Serializable;

import net.sf.json.JSONObject;

/**
 * 
 * 描述: 
 * 作者: jay.liang
 * 日期: 2014-5-22
 */
public class DeltaResult implements Serializable {
	private static final long serialVersionUID = 1459078718011652457L;
	
	private Integer ret;
	private Integer err;
	private String msg;
	private JSONObject data;

	public JSONObject getData() {
		return data;
	}

	public void setData(JSONObject data) {
		this.data = data;
	}

	public Integer getRet() {
		return ret;
	}

	public void setRet(Integer ret) {
		this.ret = ret;
	}

	public Integer getErr() {
		return err;
	}

	public void setErr(Integer err) {
		this.err = err;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}
