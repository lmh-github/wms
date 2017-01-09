/*
 * @(#)WaresCategoryAction.java 2013-7-5
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
import com.gionee.wms.service.wares.CategoryService;
import com.gionee.wms.vo.CategoryVo;

/**
 * WMS与ECShop对接接口:根据商品分类ID，获取子分类列表(包括商品)
 * @author ZuoChangjun 2013-7-5
 */
@Controller("waresCategoryAction")
@Scope("prototype")
public class WaresCategoryAction {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private CategoryService categoryService;
	/**
	 * 根据商品分类ID，获取子分类列表(包括商品)
	 * @return
	 * @throws Exception
	 */
	//@Action(value="queryCategoryVoList",results={@Result(type="json", params={"root","resultMap"})})
	public String queryCategoryVoList() throws Exception {
		Map<String,Object> paramMap=new HashMap<String,Object>();
		Map<String,Object> resultMap=new HashMap<String,Object>();
		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
		try {
			Integer catId = null;
			//接收请求参数并转换为Map对象
			//Map<String,Object>  pMap=JsonUtil.toBean(ServletActionContext.getRequest(),HashMap.class);
			String jsonStr = IOUtils.toString(ActionUtils.getRequest().getInputStream());
			Map<String,Object>  pMap=jsonUtils.fromJson(jsonStr, HashMap.class);
			if (pMap == null || pMap.get("catId") == null) {
				catId = WmsConstants.WARES_CAT_ROOT_ID;
			} else {
				catId = (Integer) pMap.get("catId");
			}
			paramMap.put("catId", catId);
			List<CategoryVo> waresCats=categoryService.queryCategoryVoList(paramMap);
        	resultMap.put("code", WmsConstants.Result.SUCCESS.getCode());
        	resultMap.put("message", WmsConstants.Result.SUCCESS.getMessage());
        	resultMap.put("waresCats", waresCats);
		} catch (Exception e) {
			logger.error("querySkuListByWaresId error", e);
        	resultMap.put("code", WmsConstants.Result.ERROR.getCode());
        	resultMap.put("message", WmsConstants.Result.ERROR.getMessage()+":"+e.getMessage());
		}
		//输出json结果
		ActionUtils.outputJson(jsonUtils.toJson(resultMap));
		return null;
	}
	
}
