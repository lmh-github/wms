/**
 * Project Name:wms
 * File Name:SalesOrderImeiDao.java
 * Package Name:com.gionee.wms.dao
 * Date:2014年8月29日下午6:56:56
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.gionee.wms.entity.SalesOrderImei;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月29日 下午6:56:56
 */
@BatisDao
public interface SalesOrderImeiDao {
	/**
	 * 新增
	 * @param salesOrderImei
	 * @return
	 */
	int add(SalesOrderImei salesOrderImei);

	/**
	 * 批量添加
	 * @param salesOrderImeis
	 * @return
	 */
	int batchAdd(@Param("salesOrderImeis") List<SalesOrderImei> salesOrderImeis);

	/**
	 * 查詢所有串号
	 * @return
	 */
	List<SalesOrderImei> queryAllImeis();

	/**
	 * 条件查询串号
	 * @param criteria<br>
	 * <code>
	 * <ul>
	 * <li>criteria.put("order_code") ==>订单号</li>
	 * <li>criteria.put("sf_erp_order") ==>顺丰回传订单号</li>
	 * </ul>
	 * </code>
	 * @return
	 */
	List<SalesOrderImei> queryImeis(Map<String, String> criteria);
}
