/*
 * @(#)LogServiceImpl.java 2014-01-08 16:11:37
 *
 * Copyright 2014 gionee.com,Inc. All rights reserved.
 */
package com.gionee.wms.service.log;

import java.util.List;
import java.util.Map;

import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gionee.wms.dao.LogDao;
import com.gionee.wms.entity.Log;



/**
 * @author ZuoChangjun 2014-01-08 16:11:37
 */
@Transactional
@Service("logService")
public class LogServiceImpl implements LogService {
	private static org.apache.commons.logging.Log log = LogFactory.getLog(LogServiceImpl.class);
	
	@Autowired
	@Qualifier("logDao")
	public LogDao logDao = null;
	
   /**
	*  增加Log
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int insertLog(Log log){
    		return logDao.insertLog(log);
	}
	
   /**
	*  删除Log
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int deleteLog(Log log)throws Exception{
			return logDao.deleteLog(log);
	}
	
   /**
	*  删除Log
	*  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int deleteLogById(Integer id)throws Exception{
			return logDao.deleteLogById(id);
	}
   /**
	*  更新Log
    *  @return 返回受影响的行数
	*  @author ZuoChangjun
 	*/
	public int updateLog(Log log)throws Exception{
			return logDao.updateLog(log);
	}
	
   /**
	*  查询Log
	*  @author ZuoChangjun
 	*/
	public List<Log> selectLogs(Log log)throws Exception{
			return logDao.selectLogs(log);
	}
	
   /**
	*  查询Log记录个数
	*  @author ZuoChangjun
 	*/
	public int selectLogsCount(Map paramsMap)throws Exception{
			return logDao.selectLogsCount(paramsMap);
	}
	
   /**
	*  分页查询Log
	*  @author ZuoChangjun
 	*/
	public List<Log> selectPagedLogs(Map paramsMap)throws Exception{
			return logDao.selectPagedLogs(paramsMap);
	}
	
   /**
	*  加载Log
	*  @author ZuoChangjun
 	*/
	public Log findLogById(Integer id)throws Exception{
			return logDao.findLogById(id);
	}
}
