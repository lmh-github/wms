/**
 * Project Name:wms
 * File Name:SystemConfigServiceImpl.java
 * Package Name:com.gionee.wms.service.stock
 * Date:2014年8月20日下午4:58:39
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.service.stock;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.wms.dao.SystemConfigDao;
import com.gionee.wms.entity.SystemConfig;
import com.google.common.collect.Maps;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月20日 下午4:58:39
 */
@Service
public class SystemConfigServiceImpl implements SystemConfigService {
	@Autowired
	private SystemConfigDao systemConfigDao;

	/** {@inheritDoc} */
	public long add(SystemConfig systemConfig) {
		if (systemConfig != null) {
			return systemConfigDao.add(systemConfig);
		}
		return -1;
	}

	/** {@inheritDoc} */
	public List<SystemConfig> queryList(Map<String, Object> paramsMap) {
		if (paramsMap == null) {
			paramsMap = Maps.newHashMap();
		}
		return systemConfigDao.queryList(paramsMap);
	}

	/** {@inheritDoc} */
	public SystemConfig getByKey(String key) {
		if (StringUtils.isBlank(key)) {
			return null;
		}
		Map<String, Object> paramsMap = Maps.newHashMap();
		paramsMap.put("key", StringUtils.trim(key));

		List<SystemConfig> configs = queryList(paramsMap);
		if (CollectionUtils.isEmpty(configs)) {
			return null;
		}

		return configs.get(0);
	}

	/** {@inheritDoc} */
	@Override
	public long update(SystemConfig systemConfig) {
		return systemConfigDao.update(systemConfig);
	}
}
