package com.gionee.wms.dto;

/**
 * 为适应DWZ框架的AJAX
 * @author Administrator
 *
 */
public class AjaxResult {
	private String statusCode;
	private String message;
	private String navTabId;
	private String rel;
	private String callbackType;
	private String forwardUrl;
	private String params;
	public String getStatusCode() {
		return statusCode;
	}
	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getNavTabId() {
		return navTabId;
	}
	public void setNavTabId(String navTabId) {
		this.navTabId = navTabId;
	}
	public String getRel() {
		return rel;
	}
	public void setRel(String rel) {
		this.rel = rel;
	}
	public String getCallbackType() {
		return callbackType;
	}
	public void setCallbackType(String callbackType) {
		this.callbackType = callbackType;
	}
	public String getForwardUrl() {
		return forwardUrl;
	}
	public void setForwardUrl(String forwardUrl) {
		this.forwardUrl = forwardUrl;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	
}
