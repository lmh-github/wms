/**
 * Project Name:wms
 * File Name:ManualReissueOrderDao.java
 * Package Name:com.gionee.wms.dao
 * Date:2016年10月8日下午2:24:31
 * Copyright (c) 2016 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.ManualReissueOrder;
import com.gionee.wms.entity.ManualReissueOrderGoods;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2016年10月8日 下午2:24:31
 */
@BatisDao
public interface ManualReissueOrderDao {

	int add(ManualReissueOrder reissueOrder);

	int addGoods(List<ManualReissueOrderGoods> goods);

	int update(ManualReissueOrder reissueOrder);

	int delete(Long id);

	int deleteGoods(Long manualReissueOrderId);

	List<Map<String, Object>> query(Map<String, Object> criteria);

	List<Map<String, Object>> queryForExport(Map<String, Object> criteria);

	Map<String, Object> get(Long id);

	int queryCount(Map<String, Object> criteria);

	List<ManualReissueOrderGoods> queryGoods(Long manualReissueOrderId);
}
