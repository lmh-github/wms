package com.gionee.top.controller;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.top.config.SystemConfig;
import com.taobao.api.internal.util.WebUtils;

/**
 * 
 * 作者:milton.zhang 
 * 时间:2013-11-30 
 * 描述:auth2.0授权
 */
@Controller
@RequestMapping("/auth")
public class AuthController {
	private static final Log log = LogFactory.getLog(AuthController.class);
	
	/**
	 * 去授权
	 * @return
	 */
	@RequestMapping(value = "/toAuth", method = RequestMethod.GET)
	public String toAuth() {
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("client_id", SystemConfig.APPKEY);
			param.put("response_type", "code");
			param.put("redirect_uri", SystemConfig.TOP_AUTH_CALLBACK_URL);

			return "redirect:" + SystemConfig.TOP_AUTH_URL
					+ "?response_type=code" + "&client_id="
					+ SystemConfig.APPKEY + "&redirect_uri="
					+ SystemConfig.TOP_AUTH_CALLBACK_URL;
		} catch (Exception e) {
			log.error("AuthController--toAuth--error", e);
		}
		return null;
	}
	
	/**
	 * 根据授权码获取访问令牌
	 * @param error
	 * @param error_description
	 * @param state
	 * @param code
	 * @return
	 */
	@RequestMapping(value = "/getToken", method = RequestMethod.GET)
	public @ResponseBody String getToken(String error, String error_description, String state, String code) {
		try {
			//获取回调地址参数中的授权码信息
			if(!StringUtils.isEmpty(error)){
				return "error:"+error+"</br>error_description:"+error_description;
			}else{
				//获取访问令牌
				Map<String, String> param = new HashMap<String, String>();
				param.put("grant_type", "authorization_code");
				param.put("code", code);
				param.put("client_id", SystemConfig.APPKEY);
				param.put("client_secret", SystemConfig.APPSECRET);
				param.put("redirect_uri", SystemConfig.TOP_AUTH_CALLBACK_URL);
				String retJson = WebUtils.doPost(SystemConfig.TOP_AUTH_TOKEN_URL, param, 3000, 3000);
				if(!StringUtils.isEmpty(retJson)){
					//设置全局访问令牌
					JSONObject jsonObj = JSONObject.fromObject(retJson);
					String token = jsonObj.get("access_token").toString();
					SystemConfig.ACCESS_TOKEN = token;
					return retJson;
				}
			}
		} catch (Exception e) {
			log.error("AuthController--getToken--error", e);
			return e.getMessage();
		}
		return null;
	}
}
