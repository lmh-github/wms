/*
 * @(#)LogAction.java 2014-01-08 16:11:37
 *
 * Copyright 2014 gionee.com,Inc. All rights reserved.
 */
package com.gionee.wms.web.action.log;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Log;
import com.gionee.wms.service.log.LogService;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Maps;


/**
 * @author ZuoChangjun 2014-01-08 16:11:37
 */
@SuppressWarnings("serial")
@Scope("prototype")
@Controller("LogAction")
public class LogAction  extends CrudActionSupport<Log> {
	private static org.apache.commons.logging.Log logger = LogFactory.getLog(LogAction.class);
	
	@Autowired
	@Qualifier("logService")
	private LogService logService;
	//输入
	private Log log;
	private Integer id;
	private String startDate;
	private String endDate;
	//输出
	private List<Log> logList=null;
	private Page page = new Page();
	
	/**
	 * 打开创建或编辑页面
	 */
	@Override
	public String input() throws Exception {
		return INPUT;
	}
   /**
	*  增加Log
	*  @author ZuoChangjun
 	*/
	public String add()throws Exception{
	    try{
			Log log = new Log();
			// TO DO
			logService.insertLog(log);
			return SUCCESS;
		}
	    catch (Exception e){
            logger.error(e.getMessage());
			return SUCCESS;
        }
	}
	
   /**
	*  删除Log
	*  @author ZuoChangjun
 	*/
	public String delete()throws Exception{
	    try{
			Log log = new Log();
			// TO DO
			logService.deleteLog(log);
			return SUCCESS;
		}
	    catch (Exception e){
            logger.error(e.getMessage());
			return SUCCESS;
        }
	}	
	
   /**
	*  删除Log
	*  @author ZuoChangjun
 	*/
	public String deleteLogById()throws Exception{
	    try{
			logService.deleteLogById(id);
			return SUCCESS;
		}
	    catch (Exception e){
            logger.error(e.getMessage());
			return SUCCESS;
        }
	}
	
   /**
	*  更新Log
	*  @author ZuoChangjun
 	*/
	public String update()throws Exception{
		try{
			Log log = new Log();
			// TO DO
			logService.updateLog(log);
			return SUCCESS;
		}
	    catch (Exception e){
            logger.error(e.getMessage());
			return SUCCESS;
        }
	}
	
   /**
	*  查询Log
	*  @author ZuoChangjun
 	*/
	public String selectLogs()throws Exception{
		try{
			Log log = new Log();
			// TO DO
			logList=logService.selectLogs(log);
			return SUCCESS;
		}
	    catch (Exception e){
            logger.error(e.getMessage());
			return SUCCESS;
        }
	}
	
   /**
	*  分页查询Log
	*  @author ZuoChangjun
 	*/
	public String list()throws Exception{
		try{
			Map<String, Object> criteria = Maps.newHashMap();
			if(log == null){
				log = new Log();
			}
			//0:全部
			if (log.getType() != null && log.getType().intValue() == 0) {
				log.setType(null);
			}
			criteria.put("type", log.getType());
			criteria.put("content", StringUtils.defaultIfBlank(log.getContent(), null));
			criteria.put("opName", StringUtils.defaultIfBlank(log.getOpName(), null));
			criteria.put("opUserName", StringUtils.defaultIfBlank(log.getOpUserName(), null));
			criteria.put("startDate", StringUtils.defaultIfBlank(startDate, null));
			String oldEndDate = endDate;
			if(StringUtils.isNotBlank(endDate)){
				endDate = endDate + " 23:59:59";
			}else{
				endDate = null;
			}
			criteria.put("endDate", endDate);
			int totalRow = logService.selectLogsCount(criteria);
			page.setTotalRow(totalRow);
			page.calculate();
			criteria.put("startRow", page.getStartRow());
			criteria.put("endRow", page.getEndRow());
			logList=logService.selectPagedLogs(criteria);
			endDate = oldEndDate;
			if(!CollectionUtils.isEmpty(logList)){
				for(Log log:logList){
					log.setContent(log.getContent() == null?"":log.getContent().replace("\"", "'"));
					if(log.getContent().length()>60){
						log.setShortContent(log.getContent().substring(0, 60)+"…");
					}else{
						log.setShortContent(log.getContent());
					}
				}
			}
			return SUCCESS;
		}
	    catch (Exception e){
            logger.error(e.getMessage());
			return SUCCESS;
        }
	}
	
   /**
	*  加载Log
	*  @author ZuoChangjun
 	*/
	public String findLogById()throws Exception{
		try{
			log=logService.findLogById(id);
			return SUCCESS;
		}
	    catch (Exception e){
            logger.error(e.getMessage());
			return SUCCESS;
        }
	}
	
	/**
	 * input方法执行前的预处理.
	 */
	public  void prepareInput() throws Exception{
		
	}

	/**
	 * update方法执行前的预处理.
	 */
	public  void prepareUpdate() throws Exception{
		
	}

	/**
	 * add方法执行前的预处理.
	 */
	public  void prepareAdd() throws Exception{
		
	}
	// ModelDriven接口方法
	@Override
	public Log getModel() {
		return null;
	}
  //-----------------------------------------getter/setter--------------------------------------

	public Log getLog() {
		return log;
	}

	public void setLog(Log log) {
		this.log = log;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<Log> getLogList() {
		return logList;
	}

	public void setLogList(List<Log> logList) {
		this.logList = logList;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}
	public String getStartDate() {
		return startDate;
	}
	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}
	public String getEndDate() {
		return endDate;
	}
	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

}