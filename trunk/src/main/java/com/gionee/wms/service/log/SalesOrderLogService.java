/*
 * @(#)SalesOrderLogService.java 2014-02-12 16:14:00
 *
 * Copyright 2014 gionee.com,Inc. All rights reserved.
 */
package com.gionee.wms.service.log;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.SalesOrderLog;
import com.gionee.wms.service.ServiceException;


/**
 *
 * @author ZuoChangjun 2014-02-12 16:14:00
 */
public interface SalesOrderLogService{

   /**
	*  增加SalesOrderLog
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int insertSalesOrderLog(SalesOrderLog salesOrderLog)throws ServiceException;


	public int insert(SalesOrderLog salesOrderLog)throws ServiceException;
	/**
	 * 批量增加SalesOrderLog
	 * 
	 * @return 返回受影响的行数
	 * @author ZuoChangjun
	 */
	public int batchInsertSalesOrderLog(Map paramsMap)throws ServiceException;
	
   /**
	*  删除SalesOrderLog
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int deleteSalesOrderLog(SalesOrderLog salesOrderLog)throws ServiceException;
	
   /**
	*  删除SalesOrderLog
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int deleteSalesOrderLogById(Integer id)throws ServiceException;
	
   /**
	*  更新SalesOrderLog
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int updateSalesOrderLog(SalesOrderLog salesOrderLog)throws ServiceException;
	
   /**
	*  查询SalesOrderLog
	*  @author ZuoChangjun
 	*/
	public List<SalesOrderLog> selectSalesOrderLogs(SalesOrderLog salesOrderLog)throws ServiceException;
	
	/**
	 * 根据订单ID, 查询SalesOrderLog
	 * 
	 * @author ZuoChangjun
	 */
	public List<SalesOrderLog> selectSalesOrderLogsByOrderId(Long orderId)throws ServiceException;
	
   /**
	*  查询SalesOrderLog记录个数
	*  @author ZuoChangjun
 	*/
	public int selectSalesOrderLogsCount(SalesOrderLog salesOrderLog)throws ServiceException;
	
   /**
	*  分页查询SalesOrderLog
	*  @author ZuoChangjun
 	*/
	public List<SalesOrderLog> selectPagedSalesOrderLogs(SalesOrderLog salesOrderLog)throws ServiceException;
	
   /**
	*  加载SalesOrderLog
	*  @author ZuoChangjun
 	*/
	public SalesOrderLog findSalesOrderLogById(Integer id)throws ServiceException;
}