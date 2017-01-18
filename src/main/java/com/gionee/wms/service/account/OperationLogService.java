package com.gionee.wms.service.account;

import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.OperationLog;
import com.gionee.wms.service.ServiceException;

public interface OperationLogService {
	/**
	 * 分页取操作日志列表.
	 */
	List<OperationLog> getOpLogList(Map<String, Object> criteria, Page page);

	/**
	 * 取操作日志列表总数.
	 */
	Integer getOpLogListTotal(Map<String, Object> criteria);

	/**
	 * 添加操作日志.
	 */
	void addOpLog(OperationLog opLog) throws ServiceException;
}
