/**
 * Project Name:wms
 * File Name:SystemConfigService.java
 * Package Name:com.gionee.wms.service.stock
 * Date:2014年8月20日下午4:58:00
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.service.stock;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.SystemConfig;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月20日 下午4:58:00
 */
public interface SystemConfigService {

	/**
	 * 添加系统配置
	 * @param systemConfig systemConfig
	 * @return ID
	 */
	long add(SystemConfig systemConfig);

	/**
	 * 查询系统配置，不分页
	 * @param paramsMap<br>
	 * "<code>key</code>":String
	 * @return List<SystemConfig>
	 */
	List<SystemConfig> queryList(Map<String, Object> paramsMap);

	/**
	 * 根据key查询一个配置，只返回第一个
	 * @param key key
	 * @return SystemConfig
	 */
	SystemConfig getByKey(String key);

	/**
	 * 修改系统配置
	 * @param systemConfig systemConfig
	 * @return long
	 */
	long update(SystemConfig systemConfig);
}
