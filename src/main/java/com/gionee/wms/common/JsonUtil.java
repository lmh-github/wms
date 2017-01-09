/*
 * @(#)JsonUtil.java 2012-4-20
 *
 * Copyright 2012 SH-BBMF,Inc. All rights reserved.
 */
package com.gionee.wms.common;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.InputStreamReader;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author ZuoChangjun 2012-4-20
 */
public class JsonUtil {
	private static Log logger = LogFactory.getLog(JsonUtil.class);

	/**
	 * Map对象转化为JSON字符串
	 * 
	 * @param map
	 * @param resultCode
	 * @return
	 */
	public static String toJsonStr(Map<String,Object> resultMap) throws Exception{
		if (resultMap == null) {
			return null;
		}
		return JSONObject.fromObject(resultMap).toString();
	}

	/**
	 * jsonStr转换为Bean类
	 * 
	 * @param jsonString
	 * @param beanCalss
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T toBean(String jsonStr, Class<T> beanCalss) throws Exception{
		if (StringUtils.isBlank(jsonStr)) {
			logger.error("jsonStr is null.");
			return null;
		}
		JSONObject jsonObject = JSONObject.fromObject(jsonStr);
		return (T) JSONObject.toBean(jsonObject, beanCalss);
	}

	/**
	 * HttpServletRequest转换为Bean类
	 * 
	 * @param request
	 * @param beanCalss
	 * @return
	 */
	public static <T> T toBean(HttpServletRequest request, Class<T> beanCalss) throws Exception{

		String jsonStr = getJsonStrFromRequest(request);
		if (StringUtils.isBlank(jsonStr)) {
			logger.error("jsonStr is null.");
			return null;
		}
		return toBean(jsonStr, beanCalss);
	/*	
		if (isJsonRequest(request)) {
			String jsonStr = getJsonStrFromRequest(request);
			if (StringUtils.isBlank(jsonStr)) {
				logger.error("jsonStr is null.");
				return null;
			}
			return toBean(jsonStr, beanCalss);
		} else {
			T obj = beanCalss.newInstance();
			BeanUtils.populate(obj, request.getParameterMap());
			return obj;
		}
	*/}

	/**
	 * resquest对象转换为jsonStr
	 * 
	 * @param request
	 * @return
	 */
	public static String getJsonStrFromRequest(HttpServletRequest request) throws Exception{

		DataInputStream in = null;
		request.setCharacterEncoding("utf-8");
		System.setProperty("sun.net.client.defaultConnectTimeout",String.valueOf(10000));
		BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
		// String jsonStr = new String(br.readLine());
		String inputLine = null;
		StringBuffer sb = new StringBuffer();
		while ((inputLine = br.readLine()) != null) {
			sb.append(inputLine).append("\n");
		}
		IOUtils.closeQuietly(in);
		logger.info("jsonStr="+sb.toString());
		logger.info("request.getContentType()="+request.getContentType());
		
		return sb.toString();
	/*
		if (isJsonRequest(request)) {
			DataInputStream in = null;
			request.setCharacterEncoding("utf-8");
			System.setProperty("sun.net.client.defaultConnectTimeout",String.valueOf(10000));
			BufferedReader br = new BufferedReader(new InputStreamReader(request.getInputStream(), "UTF-8"));
			// String jsonStr = new String(br.readLine());
			String inputLine = null;
			StringBuffer sb = new StringBuffer();
			while ((inputLine = br.readLine()) != null) {
				sb.append(inputLine).append("\n");
			}
			IOUtils.closeQuietly(in);
			logger.info("jsonStr="+sb.toString());
			logger.info("request.getContentType()="+request.getContentType());
			
			return sb.toString();
		} else {
			Map<String, Object> map = new HashMap<String, Object>();
			BeanUtils.populate(map, request.getParameterMap());
			String str = toJsonStr(map);
			
			logger.info("jsonStr="+str);
			logger.info("request.getContentType()="+request.getContentType());
			return str;
		}
	*/}
	
	public static boolean isJsonRequest(HttpServletRequest request) {
		boolean isJsonRequest = false; 
		if(StringUtils.isBlank(request.getContentType())){
			isJsonRequest= false;
		}
		if (request.getContentType().startsWith("text/plain")|| request.getContentType().startsWith("text/html") || request.getContentType().startsWith("application/json")) {
			isJsonRequest = true;
		}
		return isJsonRequest;
	}
}
