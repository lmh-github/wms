package com.gionee.wms.web.action;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.JsonUtils;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.Preparable;

/**
 * DWZ的ajax基类.
 * 
 * 
 * 
 * @author kevin
 */
public class AjaxActionSupport extends ActionSupport implements ServletRequestAware, ServletResponseAware{
	private static final long serialVersionUID = -60447452940323424L;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	private String navTabId;
	private String rel;
	private String callbackType;
	private String forwardUrl;
	
	//请求环境变量
	protected HttpServletRequest request;
	//响应环境变量
	protected HttpServletResponse response;

	/**
	 * ajax响应成功
	 * 
	 * @param message 提示信息
	 */
	protected void ajaxSuccess(String message) {
		Map<String, Object> result = getBasicMap(StatusCode.SUCESS, message);
		ajaxResponse(result, null);
	}

	protected void ajaxSuccess(String message, Map<String, Object> params) {
		Map<String, Object> result = getBasicMap(StatusCode.SUCESS, message);
		ajaxResponse(result, params);
	}

	/**
	 * ajax响应失败
	 * 
	 * @param message 提示信息
	 */
	protected void ajaxError(String message) {
		Map<String, Object> paramsMap = getBasicMap(StatusCode.ERROR, message);
		ajaxResponse(paramsMap, null);
	}

	protected void ajaxError(String message, Map<String, Object> params) {
		Map<String, Object> paramsMap = getBasicMap(StatusCode.ERROR, message);
		ajaxResponse(paramsMap, params);
	}
	
	protected void ajaxObject(Object obj) {
		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
		ActionUtils.outputJson(jsonUtils.toJson(obj));
	}

	private void ajaxResponse(Map<String,Object> result, Map<String, Object> params) {
		if(params!=null){
			result.putAll(params);	
		}
		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
//		ActionUtils.outputJson(jsonUtils.toJson(result));
		/*JAY注释：Struts2框架中，使用iframe/form方式的ajaxupload，提示下载json的问题:
		如果处理这个upload ajax请求的action配置了json的result type，请注意，在隐藏的表单向隐藏的iframe提交后，返回的json数据会导致弹出一个下载框（提示下载返回的json数据）
		----- 这和纯ajax请求不同，纯ajax请求会自动将json数据分析成object。*/
		ActionUtils.outputText(jsonUtils.toJson(result));
	}
	
	/**
	 * 构建DWZ的基础json串
	 * 
	 * @param statusCode 状态码
	 * @param message 提示信息
	 */
	private Map<String, Object> getBasicMap(StatusCode statusCode, String message) {
		Map<String, Object> result = Maps.newHashMap();
		result.put("statusCode", statusCode.toString());
		result.put("message", message);
		result.put("callbackType", callbackType);
		result.put("navTabId", navTabId);
		result.put("forwardUrl", forwardUrl);
		result.put("rel", rel);
		return result;
	}

	public static enum StatusCode {
		SUCESS(200), ERROR(300), TIMEOUT(301);

		private final int value;

		StatusCode(int value) {
			this.value = value;
		}

		@Override
		public String toString() {
			return String.valueOf(value);
		}
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

	// //////////////////TODO：另一方式////////////////////////////////////
	/**
	 * ajax请求处理后跳转
	 * 
	 * @param message
	 * @return
	 */
	protected String ajaxForwardSuccess(String message) {
		return ajaxForward(200, message);
	}

	protected String ajaxForwardError(String message) {
		return ajaxForward(300, message);
	}

	private int statusCode = 200;
	private String message;

	private String ajaxForward(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
		return "ajaxDone";
	}

	public int getStatusCode() {
		return statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setServletRequest(HttpServletRequest request) {
		this.request = request;
	}

	public void setServletResponse(HttpServletResponse response) {
		this.response = response;
	}
}
