/*
 * @(#)SkuAction.java 2013-7-5
 *
 * Copyright 2013 Shenzhen Gionee,Inc. All rights reserved.
 */
package com.gionee.wms.web.action.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.service.wares.WaresService;
import com.gionee.wms.vo.SkuVo;

/**
 * WMS与ECShop对接接口:根据商品ID或商品code，获取SKU列表
 * @author ZuoChangjun 2013-7-5
 */
@Controller("skuAction")
@Scope("prototype")
//@ParentPackage(value = "wms-default")
//@Namespace("/sku")
public class SkuAction {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private WaresService waresService;
	
	/**
	 * 根据商品ID，获取SKU列表
	 * @return
	 * @throws Exception
	 */
	//@Action(value="querySkuListByWaresId",results={@Result(type="json", params={"root","resultMap"})})
	public String querySkuListByWaresId() throws Exception {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		Map<String,Object> resultMap=new HashMap<String,Object>();
		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
		try {
			//接收请求参数并转换为Map对象
			//Map<String,Object>  pMap=JsonUtil.toBean(ServletActionContext.getRequest(),HashMap.class);
			String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream());
			Map<String,Object>  pMap=jsonUtils.fromJson(jsonStr, HashMap.class);
			if (pMap == null || pMap.get("waresId") == null) {
				logger.error("querySkuListByWaresId error：waresId is null.");
				resultMap.put("code", WmsConstants.Result.ERROR.getCode());
				resultMap.put("message", WmsConstants.Result.ERROR.getMessage()+":waresId is null.");
				// 输出json结果
				ActionUtils.outputJson(jsonUtils.toJson(resultMap));
				return null;
			}
			paramMap.put("waresId", pMap.get("waresId"));
			paramMap.put("enabled", WmsConstants.ENABLED_TRUE);
			List<SkuVo> skus=waresService.querySkuListByWaresId(paramMap);
        	resultMap.put("code", WmsConstants.Result.SUCCESS.getCode());
        	resultMap.put("message", WmsConstants.Result.SUCCESS.getMessage());
        	resultMap.put("skus", skus);
		} catch (Exception e) {
			logger.error("querySkuListByWaresId error", e);
        	resultMap.put("code", WmsConstants.Result.ERROR.getCode());
        	resultMap.put("message", WmsConstants.Result.ERROR.getMessage()+":"+e.getMessage());
		}
		//输出json结果
		ActionUtils.outputJson(jsonUtils.toJson(resultMap));
		return null;
	}
	
	/**
	 * 根据商品Code，获取SKU列表
	 * @return
	 * @throws Exception
	 */
	//@Action(value="querySkuListByWaresId",results={@Result(type="json", params={"root","resultMap"})})
	public String querySkuListByWaresCode() throws Exception {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		Map<String,Object> resultMap=new HashMap<String,Object>();
		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
		try {
			//接收请求参数并转换为Map对象
			//Map<String,Object>  pMap=JsonUtil.toBean(ServletActionContext.getRequest(),HashMap.class);
			String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream());
			Map<String,Object>  pMap=jsonUtils.fromJson(jsonStr, HashMap.class);
			if (pMap == null || pMap.get("waresCode") == null) {
				logger.error("querySkuListByWaresCode error：wareCode is null.");
				resultMap.put("code", WmsConstants.Result.ERROR.getCode());
				resultMap.put("message", WmsConstants.Result.ERROR.getMessage()+":wareCode is null.");
				// 输出json结果
				ActionUtils.outputJson(jsonUtils.toJson(resultMap));
				return null;
			}
			paramMap.put("waresCode", pMap.get("waresCode"));
			paramMap.put("enabled", WmsConstants.ENABLED_TRUE);
			List<SkuVo> skus=waresService.querySkuListByWaresCode(paramMap);
        	resultMap.put("code", WmsConstants.Result.SUCCESS.getCode());
        	resultMap.put("message", WmsConstants.Result.SUCCESS.getMessage());
        	resultMap.put("skus", skus);
		} catch (Exception e) {
			logger.error("querySkuListByWaresCode error", e);
        	resultMap.put("code", WmsConstants.Result.ERROR.getCode());
        	resultMap.put("message", WmsConstants.Result.ERROR.getMessage()+":"+e.getMessage());
		}
		//输出json结果
		ActionUtils.outputJson(jsonUtils.toJson(resultMap));
		return null;
	}
}
