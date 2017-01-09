/*
 * @(#)SkuMapService.java 2014-01-06 10:52:31
 * 
 * Copyright 2014 gionee.com,Inc. All rights reserved.
 */
package com.gionee.wms.service.wares;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.SkuMap;
import com.gionee.wms.service.ServiceException;

/**
 *
 * @author ZuoChangjun 2014-01-06 10:52:31
 */
public interface SkuMapService {

	/**
	*  增加SkuMap
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
	*/
	public int insertSkuMap(SkuMap skuMap) throws ServiceException;

	/**
	*  删除SkuMap
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
	*/
	public int deleteSkuMap(SkuMap skuMap) throws ServiceException;

	/**
	*  删除SkuMap
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
	*/
	public int deleteSkuMapById(Integer id) throws ServiceException;

	/**
	*  更新SkuMap
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
	*/
	public int updateSkuMap(SkuMap skuMap) throws ServiceException;

	/**
	*  查询SkuMap
	*  @author ZuoChangjun
	*/
	public List<SkuMap> selectSkuMaps(SkuMap skuMap) throws ServiceException;

	/**
	*  查询SkuMap记录个数
	*  @author ZuoChangjun
	*/
	public int selectSkuMapsCount(Map paramsMap) throws ServiceException;

	/**
	*  分页查询SkuMap
	*  @author ZuoChangjun
	*/
	public List<SkuMap> selectPagedSkuMaps(Map paramsMap) throws ServiceException;

	/**
	*  加载SkuMap
	*  @author ZuoChangjun
	*/
	public SkuMap findSkuMapById(Integer id) throws ServiceException;

	/**
	 * 根据outerSkuCodes查询skuCodes
	 * 
	 * @author ZuoChangjun
	 */
	public List<SkuMap> findSkuMapsByOuterSkuCodes(List<String> outerSkuCodeList) throws ServiceException;

	/**
	 * 根据SkuCode查询 SkuMap
	 * @param skuCode skuCode 
	 * @param outerCode outerCode 
	 * @return SkuMap
	 */
	public SkuMap getSkuMapBySkuCode(String skuCode, String outerCode);
	
	/**
	 * 根据SkuCode查询 SkuMap
	 * @param outSkuCode outSkuCode 
	 * @param outerCode outerCode 
	 * @return SkuMap
	 */
	public SkuMap getSkuMapByOutSkuCode(String outerSkuCode, String outerCode);

	/**
	 * 根据SkuCode 查询SkuMap
	 * @param skuCodes
	 * @param outerCode outerCode
	 * @return
	 */
	public List<SkuMap> querySkuMapBySkuCodes(List<String> skuCodes, String outerCode);
}