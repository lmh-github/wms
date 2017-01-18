package com.gionee.wms.dto;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2013-10-19 上午10:32:51
 * @=======================================
 */
public class CommonAjaxResult {
	private Boolean ok = false;
	private String code;
	private String message;
	private Object result;
	public Boolean getOk() {
		return ok;
	}
	public void setOk(Boolean ok) {
		this.ok = ok;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getResult() {
		return result;
	}
	public void setResult(Object result) {
		this.result = result;
	}
}
