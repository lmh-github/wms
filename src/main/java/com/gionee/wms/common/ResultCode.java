/*
 * @(#)ResultCode.java 2013-6-24
 *
 * Copyright 2013 SH-Menue,Inc. All rights reserved.
 */
package com.gionee.wms.common;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * IUC接口ResultCode常量
 * 
 * @author ZuoChangjun 2013-6-24
 */
public class ResultCode {

	private static Logger logger = LoggerFactory.getLogger(ResultCode.class);

	public static final String resultKey = "resultCode";

	/**
	 * 系统内部错误
	 */
	public static final int SYSTEM_ERROR = -1;
	/**
	 * 操作成功
	 */
	public static final int SUCCESS = 0;
	/**
	 * 应用ID无效
	 */
	public static final int APPID_INVALID = 1;
	/**
	 * 验证码验证失败
	 */
	public static final int CHECKCODE_INVALID = 2;
	/**
	 * TICKET验证失败
	 */
	public static final int TICKET_INVALID = 3;
	/**
	 * 无操作权限
	 */
	public static final int PERMISSIONS_FORBIDDEN = 4;
	/**
	 * 请求头不正确
	 */
	public static final int REQUEST_HEADER_ERROR = 5;
	/**
	 * ip白名单验证失败
	 */
	public static final int IP_FORBIDDEN = 6;
	/**
	 * 无效的ttl
	 */
	public static final int TTL_INVALID = 7;

	/**
	 * 解析ResultCode
	 * 
	 * @param code
	 */
	public static String parseResultCode(int code) {
		String msg = "";
		switch (code) {
		case SYSTEM_ERROR:
			msg = "SYSTEM_ERROR";
			break;
		case SUCCESS:
			msg = "SUCCESS";
			break;
		case APPID_INVALID:
			msg = "APPID_INVALID";
			break;
		case CHECKCODE_INVALID:
			msg = "CHECKCODE_INVALID";
			break;
		case TICKET_INVALID:
			msg = "TICKET_INVALID";
			break;
		case PERMISSIONS_FORBIDDEN:
			msg = "PERMISSIONS_FORBIDDEN";
			break;
		case REQUEST_HEADER_ERROR:
			msg = "REQUEST_HEADER_ERROR";
			break;
		case IP_FORBIDDEN:
			msg = "IP_FORBIDDEN";
			break;
		}
		return msg;
	}

	/**
	 * 解析ResultCode
	 * 
	 * @param code
	 */
	public static String parseResultCode(String code) {
		if (StringUtils.isBlank(code)) {
			logger.error("ResultCode is Blank.");
			return null;
		}
		int resultCode = Integer.parseInt(code);
		return parseResultCode(resultCode);
	}
}
