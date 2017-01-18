/*
 * @(#)SkuMapDao.java 2014-01-06 10:52:31
 * 
 * Copyright 2014 gionee.com,Inc. All rights reserved.
 */
package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.gionee.wms.entity.SkuMap;

/**
 * @author ZuoChangjun 2014-01-06 10:52:31
 */
@BatisDao
public interface SkuMapDao {

	/**
	*  增加SkuMap
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
	*/
	public int insertSkuMap(SkuMap skuMap);

	/**
	*  删除SkuMap
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
	*/
	public int deleteSkuMap(SkuMap skuMap);

	/**
	*  删除SkuMap
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
	*/
	public int deleteSkuMapById(Integer id);

	/**
	*  更新SkuMap
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
	*/
	public int updateSkuMap(SkuMap skuMap);

	/**
	*  查询SkuMap
	*  @author ZuoChangjun
	*/
	public List<SkuMap> selectSkuMaps(SkuMap skuMap);

	/**
	*  查询SkuMap记录个数
	*  @author ZuoChangjun
	*/
	public int selectSkuMapsCount(Map paramsMap);

	/**
	*  分页查询SkuMap
	*  @author ZuoChangjun
	*/
	public List<SkuMap> selectPagedSkuMaps(Map paramsMap);

	/**
	*  加载SkuMap
	*  @author ZuoChangjun
	*/
	public SkuMap findSkuMapById(Integer id);

	/**
	 * 根据outerSkuCodes查询skuCodes
	 * 
	 * @author ZuoChangjun
	 */
	public List<SkuMap> findSkuMapsByOuterSkuCodes(List<String> outerSkuCodeList);

	/**
	 * 根据SkuCode查询 SkuMap
	 * @param skuCode skuCode 
	 * @param outerCode outerCode 
	 * @return SkuMap
	 */
	public SkuMap getSkuMapBySkuCode(@Param("skuCode") String skuCode, @Param("outerCode") String outerCode);
	
	/**
	 * 根据OuterSkuCode查询 SkuMap
	 * @param outerSkuCode outerSkuCode 
	 * @param outerCode outerCode 
	 * @return SkuMap
	 */
	public SkuMap getSkuMapByOutSkuCode(@Param("outerSkuCode") String skuCode, @Param("outerCode") String outerCode);
	
	

	/**
	 * 根据SkuCode 查询SkuMap
	 * @param skuCodes
	 * @param outerCode outerCode
	 * @return
	 */
	public List<SkuMap> querySkuMapBySkuCodes(@Param("skuCodes") List<String> skuCodes, @Param("outerCode") String outerCode);

}