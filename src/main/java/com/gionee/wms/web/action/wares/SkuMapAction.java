/*
 * @(#)SkuMapAction.java 2014-01-06 10:52:31
 *
 * Copyright 2014 gionee.com,Inc. All rights reserved.
 */
package com.gionee.wms.web.action.wares;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.SkuMap;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.wares.SkuMapService;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Maps;


/**
 * @author ZuoChangjun 2014-01-06 10:52:31
 */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("SkuMapAction")
public class SkuMapAction extends CrudActionSupport<SkuMap> {
	private static Log log = LogFactory.getLog(SkuMapAction.class);
	
	@Autowired
	@Qualifier("skuMapService")
	private SkuMapService skuMapService;
	
	/** 页面相关属性 **/
	private List<SkuMap> skuMapList;
	private Integer id;
	private Integer selectEnabled;// 是否允许页面select可用
	private Boolean editEnabled;// 是否允许页面数据编辑
	private SkuMap skuMap;
	private Page page = new Page();
	
	/**
	 * 打开创建或编辑页面
	 */
	@Override
	public String input() throws Exception {
		if(id != null){
			skuMap = skuMapService.findSkuMapById(id);
			//已同步数据到顺丰，不能进行修改
			if(skuMap.getSkuPushStatus() == 1) {
				editEnabled = false;
			}
		}
		return INPUT;
	}
   /**
	*  增加SkuMap
	*  @author ZuoChangjun
 	*/
	public String add()throws Exception{
	    try{
			skuMapList=skuMapService.selectSkuMaps(skuMap);
			if(CollectionUtils.isNotEmpty(skuMapList)){
				ajaxError("SkuMap添加失败：记录已存在，请重新输入.");
				return null;
			}
			skuMapService.insertSkuMap(skuMap);
			ajaxSuccess("添加SkuMap成功");
		}
	    catch (Exception e){
            log.error(e.getMessage());
			ajaxError("添加SkuMap失败：" + e.getMessage());
        }
	    return null;
	}
	
   /**
	*  删除SkuMap
	*  @author ZuoChangjun
 	*/
	public String deleteSkuMap()throws Exception{
	    try{
			SkuMap skuMap = new SkuMap();
			// TO DO
			skuMapService.deleteSkuMap(skuMap);
			return SUCCESS;
		}
	    catch (Exception e){
            log.error(e.getMessage());
			return SUCCESS;
        }
	}	
	
   /**
	*  删除SkuMap
	*  @author ZuoChangjun
 	*/
	public String delete()throws Exception{
		try {
			skuMapService.deleteSkuMapById(id);
			ajaxSuccess("SkuMap删除成功");
		} catch (ServiceException e) {
			logger.error("SkuMap删除时出错", e);
			ajaxError("SkuMap删除失败：" + e.getMessage());
		}
		return null;
	}
	
   /**
	*  更新SkuMap
	*  @author ZuoChangjun
 	*/
	public String update()throws Exception{
		try {
			skuMapList=skuMapService.selectSkuMaps(skuMap);
			if(CollectionUtils.isNotEmpty(skuMapList)){
				ajaxError("SkuMap编辑失败：记录已存在，请重新输入.");
				return null;
			}
			SkuMap skuMapTmp = skuMapService.findSkuMapById(skuMap.getId());
			//确认保存时刻，没有做同步操作
			if(skuMapTmp.getSkuPushStatus() != 1) {
				skuMapService.updateSkuMap(skuMap);
			}
			ajaxSuccess("SkuMap编辑成功");
		} catch (ServiceException e) {
			logger.error("SkuMap编辑时出错", e);
			ajaxError("SkuMap编辑失败：" + e.getMessage());
		}
		return null;
	}
	
   /**
	*  查询SkuMap
	*  @author ZuoChangjun
 	*/
	public String selectSkuMaps()throws Exception{
		try{
			SkuMap skuMap = new SkuMap();
			// TO DO
			skuMapList=skuMapService.selectSkuMaps(skuMap);
			return SUCCESS;
		}
	    catch (Exception e){
            log.error(e.getMessage());
			return SUCCESS;
        }
	}
	
   /**
	*  分页查询SkuMap
	*  @author ZuoChangjun
 	*/
	public String list()throws Exception{
		try{
			Map<String, Object> criteria = Maps.newHashMap();
			if(skuMap == null){
				skuMap = new SkuMap();
			}
			criteria.put("skuCode", StringUtils.defaultIfBlank(skuMap.getSkuCode(), null));
			criteria.put("outerSkuCode", StringUtils.defaultIfBlank(skuMap.getOuterSkuCode(), null));
			criteria.put("outerCode", StringUtils.defaultIfBlank(skuMap.getOuterCode(), null));
			int totalRow = skuMapService.selectSkuMapsCount(criteria);
			page.setTotalRow(totalRow);
			page.calculate();
			criteria.put("startRow", page.getStartRow());
			criteria.put("endRow", page.getEndRow());
			skuMapList = skuMapService.selectPagedSkuMaps(criteria);
			return SUCCESS;
		}
	    catch (Exception e){
            log.error(e.getMessage());
			return SUCCESS;
        }
	}
	
   /**
	*  加载SkuMap
	*  @author ZuoChangjun
 	*/
	public String findSkuMapById()throws Exception{
		try{
			skuMap=skuMapService.findSkuMapById(id);
			return SUCCESS;
		}
	    catch (Exception e){
            log.error(e.getMessage());
			return SUCCESS;
        }
	}
	
	/**
	 * input方法执行前的预处理.
	 */
	public  void prepareInput() throws Exception{
		
	}

	/**
	 * update方法执行前的预处理.
	 */
	public  void prepareUpdate() throws Exception{
		
	}

	/**
	 * add方法执行前的预处理.
	 */
	public  void prepareAdd() throws Exception{
		
	}
	// ModelDriven接口方法
	@Override
	public SkuMap getModel() {
		return null;
	}
  //-----------------------------------------getter/setter--------------------------------------
	public List<SkuMap> getSkuMapList() {
		return skuMapList;
	}
	public void setSkuMapList(List<SkuMap> skuMapList) {
		this.skuMapList = skuMapList;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public Integer getSelectEnabled() {
		return selectEnabled;
	}
	public void setSelectEnabled(Integer selectEnabled) {
		this.selectEnabled = selectEnabled;
	}
	public Boolean getEditEnabled() {
		return editEnabled;
	}
	public void setEditEnabled(Boolean editEnabled) {
		this.editEnabled = editEnabled;
	}
	public SkuMap getSkuMap() {
		return skuMap;
	}
	public void setSkuMap(SkuMap skuMap) {
		this.skuMap = skuMap;
	}
	public Page getPage() {
		return page;
	}
	public void setPage(Page page) {
		this.page = page;
	}
 
}