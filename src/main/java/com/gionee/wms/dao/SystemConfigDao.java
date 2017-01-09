/**
 * Project Name:wms
 * File Name:SystemConfigDao.java
 * Package Name:com.gionee.wms.dao
 * Date:2014年8月20日下午4:42:59
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.SystemConfig;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月20日 下午4:42:59
 */
@BatisDao
public interface SystemConfigDao {
	/**
	 * 添加
	 * @param systemConfig
	 * @return
	 */
	long add(SystemConfig systemConfig);

	List<SystemConfig> queryList(Map<String, Object> criteria);

	/**
	 * 修改
	 * @param systemConfig
	 * @return
	 */
	Integer update(SystemConfig systemConfig);
}
