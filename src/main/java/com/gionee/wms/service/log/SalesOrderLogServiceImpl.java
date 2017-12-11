/*
 * @(#)SalesOrderLogServiceImpl.java 2014-02-12 16:14:00
 *
 * Copyright 2014 gionee.com,Inc. All rights reserved.
 */
package com.gionee.wms.service.log;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gionee.wms.dao.SalesOrderLogDao;
import com.gionee.wms.entity.SalesOrderLog;
import com.gionee.wms.service.ServiceException;


/**
 * @author ZuoChangjun 2014-02-12 16:14:00
 */
@Transactional(rollbackFor = Exception.class)
@Service("salesOrderLogService")
public class SalesOrderLogServiceImpl implements SalesOrderLogService {
	private static Log log = LogFactory.getLog(SalesOrderLogServiceImpl.class);
	
	@Autowired
	public SalesOrderLogDao salesOrderLogDao = null;
	
   /**
	*  增加SalesOrderLog
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int insertSalesOrderLog(SalesOrderLog salesOrderLog)throws ServiceException{
    		return salesOrderLogDao.insertSalesOrderLog(salesOrderLog);
	}

	public int insert(SalesOrderLog salesOrderLog)throws ServiceException{
		return salesOrderLogDao.insert(salesOrderLog);
	}

	
	/**
	 * 批量增加SalesOrderLog
	 * 
	 * @return 返回受影响的行数
	 * @author ZuoChangjun
	 */
	public int batchInsertSalesOrderLog(Map paramsMap)throws ServiceException{
		return salesOrderLogDao.batchInsertSalesOrderLog(paramsMap);
	}
	
   /**
	*  删除SalesOrderLog
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int deleteSalesOrderLog(SalesOrderLog salesOrderLog)throws ServiceException{
			return salesOrderLogDao.deleteSalesOrderLog(salesOrderLog);
	}
	
   /**
	*  删除SalesOrderLog
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int deleteSalesOrderLogById(Integer id)throws ServiceException{
			return salesOrderLogDao.deleteSalesOrderLogById(id);
	}
   /**
	*  更新SalesOrderLog
    *  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int updateSalesOrderLog(SalesOrderLog salesOrderLog)throws ServiceException{
			return salesOrderLogDao.updateSalesOrderLog(salesOrderLog);
	}
	
   /**
	*  查询SalesOrderLog
	*  @author ZuoChangjun
 	*/
	public List<SalesOrderLog> selectSalesOrderLogs(SalesOrderLog salesOrderLog)throws ServiceException{
			return salesOrderLogDao.selectSalesOrderLogs(salesOrderLog);
	}
	
	/**
	 * 根据订单ID, 查询SalesOrderLog
	 * 
	 * @author ZuoChangjun
	 */
	public List<SalesOrderLog> selectSalesOrderLogsByOrderId(Long orderId)throws ServiceException{
		return salesOrderLogDao.selectSalesOrderLogsByOrderId(orderId);
	}
	
   /**
	*  查询SalesOrderLog记录个数
	*  @author ZuoChangjun
 	*/
	public int selectSalesOrderLogsCount(SalesOrderLog salesOrderLog)throws ServiceException{
			return salesOrderLogDao.selectSalesOrderLogsCount(salesOrderLog);
	}
	
   /**
	*  分页查询SalesOrderLog
	*  @author ZuoChangjun
 	*/
	public List<SalesOrderLog> selectPagedSalesOrderLogs(SalesOrderLog salesOrderLog)throws ServiceException{
			return salesOrderLogDao.selectPagedSalesOrderLogs(salesOrderLog);
	}
	
   /**
	*  加载SalesOrderLog
	*  @author ZuoChangjun
 	*/
	public SalesOrderLog findSalesOrderLogById(Integer id)throws ServiceException{
			return salesOrderLogDao.findSalesOrderLogById(id);
	}
}
