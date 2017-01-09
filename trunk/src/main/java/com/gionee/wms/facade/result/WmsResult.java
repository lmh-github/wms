package com.gionee.wms.facade.result;

import java.io.Serializable;

/**
 * 
 * @ClassName: WmsResult
 * @Description: WMS服务调用返回的结果基类,并统一定义所有返回编码.
 * @author Kevin
 * @date 2013-7-4 上午09:36:49
 * 
 */
public class WmsResult implements Serializable {
	/**
	 * 序列化ID
	 */
	private static final long serialVersionUID = 1459078718011652457L;
	/*
	 * 返回编码
	 */
	private String code;
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
		return message;
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
		setResult(WmsCodeEnum.SYSTEM_ERROR.getCode(), WmsCodeEnum.SYSTEM_ERROR.getDescribe());
	}

	/**
	 * 
	 * @ClassName: WmsCodeEnum
	 * 
	 * @author Kevin
	 * 
	 */
	public enum WmsCodeEnum {
		SUCCESS("1", ""),
		SYSTEM_ERROR("300", "Unknown runtime exception"),
		CONTENT_TYPE_ILLEGAL("101","Content type of request is illegal"),
		PARAM_ERROR("102", "Parameters is illegal"),
		PARAM_WAREHOUSE_CODE_NULL("103","'warehouseCode' is null"),
		PARAM_SKU_CODE_NULL("104","'skuCode' is null"),
		PARAM_ORDERS_NULL("105","'orders' is null"),
		PARAM_ORDER_INFO_NULL("116","'orderInfo' is null"),
		PARAM_ORDER_CODE_NULL("106","'orderCode' is null"),
		PARAM_CONSIGNEE_NULL("107","'consignee' is null"),
		PARAM_PROVINCE_NULL("108","'province' is null"),
		PARAM_CITY_NULL("109","'city' is null"),
		PARAM_ADDRESS_NULL("110","'address' is null"),
		PARAM_PHONE_NULL("111","'tel' and 'mobile' is null"),
		PARAM_INVOICE_ENABLED_NULL("112","'invoiceEnabled' is null"),
		PARAM_GOODS_AMOUNT_NULL("113","'goodsAmount' is null"),
		PARAM_ORDER_AMOUNT_NULL("114","'orderAmount' is null"),
		PARAM_PAYABLE_AMOUNT_NULL("115","'payableAmount' is null"),
		PARAM_GOODS_LIST_NULL("116","'goodsList' is null"),
		PARAM_TIMESTAMP_ILLEGAL("117", "timestamp is illegal"),
		STOCK_NOT_EXISTS("201","Stock info is not exists"),
		SKU_NOT_EXISTS("202","Sku is not exists"),
		DUPLICATE_ORDER("203","Duplicate order"),
		STOCK_OCCUPY_FAILED("204","Stock occupation fail"),
		ORDER_NOT_EXIST("205","Order is not exists"),
		ORDER_SHIPPED("206","Order is shipped"),
		DUPLICATE_CANCEL_ORDER("207","Duplicate cancel order"),
		NOT_ALLOWED_CANCEL("208","Not allowed to cancel"),
		SIGNATURE_CHECKSUM_FAILURE("209", "Signature verification failed"),
		ACCESS_EXPIRED("210", "Accessing is expired "),
		STOCK_RELEASE_FAILED("211","Stock release fail"),
		SHIPPING_CODE_NOT_EXISTS("212","Shipping code is not exists"),
		PARAM_OPERFLAG_NULL("213","'operFlag' is null"),
		CURRENT_STATUS_FORBIDDEN_UPDATE("214","Current status forbidden operate"),
		TRANSFER_NOT_EXIST("215", "调拨单不存在");

		private String code;
		private String describe;

		WmsCodeEnum(String code, String describe) {
			this.code = code;
			this.describe = describe;
		}

		public String getCode() {
			return this.code;
		}

		public String getDescribe() {
			return this.describe;
		}
	}

	public static String getResultZh(String code) {
		if("201".equals(code)) {
			return WmsCodeEnum.STOCK_NOT_EXISTS.describe;
		} else if("202".equals(code)) {
			return WmsCodeEnum.SKU_NOT_EXISTS.describe;
		} else if("203".equals(code)) {
			return WmsCodeEnum.DUPLICATE_ORDER.describe;
		} else if("204".equals(code)) {
			return WmsCodeEnum.STOCK_OCCUPY_FAILED.describe;
		} else if("205".equals(code)) {
			return WmsCodeEnum.ORDER_NOT_EXIST.describe;
		} else if("206".equals(code)) {
			return WmsCodeEnum.ORDER_SHIPPED.describe;
		} else if("207".equals(code)) {
			return WmsCodeEnum.DUPLICATE_CANCEL_ORDER.describe;
		} else if("208".equals(code)) {
			return WmsCodeEnum.NOT_ALLOWED_CANCEL.describe;
		} else if("209".equals(code)) {
			return WmsCodeEnum.SIGNATURE_CHECKSUM_FAILURE.describe;
		} else if("210".equals(code)) {
			return WmsCodeEnum.ACCESS_EXPIRED.describe;
		} else if("211".equals(code)) {
			return WmsCodeEnum.STOCK_RELEASE_FAILED.describe;
		} else if("212".equals(code)) {
			return WmsCodeEnum.SHIPPING_CODE_NOT_EXISTS.describe;
		} else if("213".equals(code)) {
			return WmsCodeEnum.PARAM_OPERFLAG_NULL.describe;
		} else if("214".equals(code)) {
			return WmsCodeEnum.CURRENT_STATUS_FORBIDDEN_UPDATE.describe;
		}  else if("215".equals(code)) {
			return WmsCodeEnum.TRANSFER_NOT_EXIST.describe;
		} else {
			return code;
		}
	}
}
