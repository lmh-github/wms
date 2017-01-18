/*
 * @(#)LogDao.java 2014-01-08 16:11:37
 *
 * Copyright 2014 gionee.com,Inc. All rights reserved.
 */
package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Log;

/**
 * @author ZuoChangjun 2014-01-08 16:11:37
 */
@BatisDao
public interface LogDao {

   /**
	*  增加Log
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int insertLog(Log log);
	
   /**
	*  删除Log
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int deleteLog(Log log);
	
   /**
	*  删除Log
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int deleteLogById(Integer id);
	
   /**
	*  更新Log
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int updateLog(Log log);
	
   /**
	*  查询Log
	*  @author ZuoChangjun
 	*/
	public List<Log> selectLogs(Log log);
	
   /**
	*  查询Log记录个数
	*  @author ZuoChangjun
 	*/
	public int selectLogsCount(Map paramsMap);
	
  /**
	*  分页查询Log
	*  @author ZuoChangjun
 	*/
	public List<Log> selectPagedLogs(Map paramsMap);
	
   /**
	*  加载Log
	*  @author ZuoChangjun
 	*/
	public Log findLogById(Integer id);

}