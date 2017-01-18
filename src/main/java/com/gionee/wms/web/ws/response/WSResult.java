package com.gionee.wms.web.ws.response;

import javax.xml.bind.annotation.XmlType;

import com.gionee.wms.common.WmsConstants;

/**
 * 
 * @ClassName: WSResult
 * @Description: WebService服务调用返回的结果基类,并统一定义所有返回编码.
 * @author Kevin
 * @date 2013-7-28 上午09:36:49
 * 
 */
@XmlType(name = "WSResult", namespace = WmsConstants.TARGET_NS)
public class WSResult {
	// -- 结果编码定义 --//
	public static final String SUCCESS = "1";
	public static final String PARAMETER_ERROR = "400";
	public static final String WAREHOUSE_CODE_NOT_EXISTS = "401";
	public static final String MATERAIL_CODE_NOT_EXISTS = "402";
	public static final String GOODS_LIST_IS_NULL = "403";
	public static final String INDIV_CODES_IS_NULL = "404";
	public static final String INDIV_CODES_QUANTITY_MISMATCHING = "405";
	public static final String WAREHOUSE_IS_DISABLED = "406";

	public static final String SYSTEM_ERROR = "500";
	public static final String SYSTEM_ERROR_MESSAGE = "Runtime unknown error.";
	public static final String DUPLICATE_PUR_RERECEIVE = "501";

	/**
	 * 返回编码说明
	 * 1:成功,400:参数错误,401:仓库编号不存在,402:物料编号不存在,403:商品为空,404:商品个体编码为空,405:商品个体编码数量与对应SKU数量不一致,500:系统错误,501:数据重复
	 */
	private String code = SUCCESS;

	/*
	 * 编码对应信息
	 */
	private String message;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message==null?"":message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * 设置返回结果.
	 * 
	 * @Title: setResult
	 * @Description: 设置返回结果.
	 * @return void 返回类型
	 * @throws
	 */
	public void setResult(String resultCode, String resultMessage) {
		this.code = resultCode;
		this.message = resultMessage;
	}

	public void setResult(String resultCode) {
		this.code = resultCode;
		this.message = "";
	}

	/**
	 * 设置系统未知错误.
	 * 
	 * @Title: setSystemError
	 * @Description: 设置系统未知错误.
	 * @return void 返回类型
	 * @throws
	 */
	public void setSystemError() {
		setResult(SYSTEM_ERROR, SYSTEM_ERROR_MESSAGE);
	}

}
