/*
  * @(#)LogService.java 2014-01-08 16:11:37
 *
 * Copyright 2014 gionee.com,Inc. All rights reserved.
 */
package com.gionee.wms.service.log;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Log;


/**
 *
 * @author ZuoChangjun 2014-01-08 16:11:37
 */
public interface LogService{

   /**
	*  增加Log
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	int insertLog(Log log);
	
   /**
	*  删除Log
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int deleteLog(Log log)throws Exception;
	
   /**
	*  删除Log
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int deleteLogById(Integer id)throws Exception;
	
   /**
	*  更新Log
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int updateLog(Log log)throws Exception;
	
   /**
	*  查询Log
	*  @author ZuoChangjun
 	*/
	public List<Log> selectLogs(Log log)throws Exception;
	
   /**
	*  查询Log记录个数
	*  @author ZuoChangjun
 	*/
	public int selectLogsCount(Map paramsMap)throws Exception;
	
   /**
	*  分页查询Log
	*  @author ZuoChangjun
 	*/
	public List<Log> selectPagedLogs(Map paramsMap)throws Exception;
	
   /**
	*  加载Log
	*  @author ZuoChangjun
 	*/
	public Log findLogById(Integer id)throws Exception;
}