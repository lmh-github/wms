/*
 * @(#)SalesOrderLogDao.java 2014-02-12 16:14:00
 *
 * Copyright 2014 gionee.com,Inc. All rights reserved.
 */
package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.SalesOrderLog;

/**
 * @author ZuoChangjun 2014-02-12 16:14:00
 */
@BatisDao
public interface SalesOrderLogDao {

	/**
	 * 增加SalesOrderLog
	 * 
	 * @return 返回受影响的行数
	 * @author ZuoChangjun
	 */
	public int insertSalesOrderLog(SalesOrderLog salesOrderLog);


	public int insert(SalesOrderLog salesOrderLog);
	/**
	 * 批量增加SalesOrderLog
	 * 
	 * @return 返回受影响的行数
	 * @author ZuoChangjun
	 */
	public int batchInsertSalesOrderLog(Map paramsMap);

	/**
	 * 删除SalesOrderLog
	 * 
	 * @return 返回受影响的行数
	 * @author ZuoChangjun
	 */
	public int deleteSalesOrderLog(SalesOrderLog salesOrderLog);

	/**
	 * 删除SalesOrderLog
	 * 
	 * @return 返回受影响的行数
	 * @author ZuoChangjun
	 */
	public int deleteSalesOrderLogById(Integer id);

	/**
	 * 更新SalesOrderLog
	 * 
	 * @return 返回受影响的行数
	 * @author ZuoChangjun
	 */
	public int updateSalesOrderLog(SalesOrderLog salesOrderLog);

	/**
	 * 查询SalesOrderLog
	 * 
	 * @author ZuoChangjun
	 */
	public List<SalesOrderLog> selectSalesOrderLogs(SalesOrderLog salesOrderLog);

	/**
	 * 根据订单ID, 查询SalesOrderLog
	 * 
	 * @author ZuoChangjun
	 */
	public List<SalesOrderLog> selectSalesOrderLogsByOrderId(Long orderId);

	/**
	 * 查询SalesOrderLog记录个数
	 * 
	 * @author ZuoChangjun
	 */
	public int selectSalesOrderLogsCount(SalesOrderLog salesOrderLog);

	/**
	 * 分页查询SalesOrderLog
	 * 
	 * @author ZuoChangjun
	 */
	public List<SalesOrderLog> selectPagedSalesOrderLogs(
			SalesOrderLog salesOrderLog);

	/**
	 * 加载SalesOrderLog
	 * 
	 * @author ZuoChangjun
	 */
	public SalesOrderLog findSalesOrderLogById(Integer id);

}