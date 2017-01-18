/*
 * @(#)SkuMapServiceImpl.java 2014-01-06 10:52:31
 * 
 * Copyright 2014 gionee.com,Inc. All rights reserved.
 */
package com.gionee.wms.service.wares;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gionee.wms.dao.SkuMapDao;
import com.gionee.wms.entity.SkuMap;
import com.gionee.wms.service.ServiceException;

/**
 * @author ZuoChangjun 2014-01-06 10:52:31
 */
@Transactional
@Service("skuMapService")
public class SkuMapServiceImpl implements SkuMapService {
	private static Log log = LogFactory.getLog(SkuMapServiceImpl.class);

	@Autowired
	@Qualifier("skuMapDao")
	public SkuMapDao skuMapDao = null;

	/**
	*  增加SkuMap
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
	*/
	public int insertSkuMap(SkuMap skuMap) throws ServiceException {
		return skuMapDao.insertSkuMap(skuMap);
	}

	/**
	*  删除SkuMap
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
	*/
	public int deleteSkuMap(SkuMap skuMap) throws ServiceException {
		return skuMapDao.deleteSkuMap(skuMap);
	}

	/**
	*  删除SkuMap
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
	*/
	public int deleteSkuMapById(Integer id) throws ServiceException {
		return skuMapDao.deleteSkuMapById(id);
	}

	/**
	*  更新SkuMap
	 *  @return 返回受影响的行数
	*  @author ZuoChangjun
	*/
	public int updateSkuMap(SkuMap skuMap) throws ServiceException {
		return skuMapDao.updateSkuMap(skuMap);
	}

	/**
	*  查询SkuMap
	*  @author ZuoChangjun
	*/
	public List<SkuMap> selectSkuMaps(SkuMap skuMap) throws ServiceException {
		return skuMapDao.selectSkuMaps(skuMap);
	}

	/**
	*  查询SkuMap记录个数
	*  @author ZuoChangjun
	*/
	public int selectSkuMapsCount(Map paramsMap) throws ServiceException {
		return skuMapDao.selectSkuMapsCount(paramsMap);
	}

	/**
	*  分页查询SkuMap
	*  @author ZuoChangjun
	*/
	public List<SkuMap> selectPagedSkuMaps(Map paramsMap) throws ServiceException {
		return skuMapDao.selectPagedSkuMaps(paramsMap);
	}

	/**
	*  加载SkuMap
	*  @author ZuoChangjun
	*/
	public SkuMap findSkuMapById(Integer id) throws ServiceException {
		return skuMapDao.findSkuMapById(id);
	}

	/**
	 * 根据outerSkuCodes查询skuCodes
	 * 
	 * @author ZuoChangjun
	 */
	public List<SkuMap> findSkuMapsByOuterSkuCodes(List<String> outerSkuCodeList) throws ServiceException {
		return skuMapDao.findSkuMapsByOuterSkuCodes(outerSkuCodeList);
	}

	/** {@inheritDoc} */
	@Override
	public SkuMap getSkuMapBySkuCode(String skuCode, String outerCode) {
		if (skuCode == null || outerCode == null) {
			return null;
		}
		return skuMapDao.getSkuMapBySkuCode(skuCode, outerCode);
	}
	
	@Override
	public SkuMap getSkuMapByOutSkuCode(String outerSkuCode, String outerCode) {
		if (outerSkuCode == null || outerSkuCode == null) {
			return null;
		}
		return skuMapDao.getSkuMapByOutSkuCode(outerSkuCode, outerCode);
	}

	/** {@inheritDoc} */
	@Override
	public List<SkuMap> querySkuMapBySkuCodes(List<String> skuCodes, String outerCode) {
		if (CollectionUtils.isEmpty(skuCodes) || outerCode == null) {
			return null;
		}
		return skuMapDao.querySkuMapBySkuCodes(skuCodes, outerCode);
	}
}
