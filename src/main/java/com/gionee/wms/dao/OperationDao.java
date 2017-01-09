package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.OperationLog;

@BatisDao
public interface OperationDao {
	/**
	 * 分页查询操作日志列表
	 */
	List<OperationLog> queryOpLogList(Map<String, Object> criteria);

	/**
	 * 查询操作日志列表总数
	 */
	int queryOpLogListTotal(Map<String, Object> criteria);

	/**
	 * 添加操作日志
	 */
	int addOpLog(OperationLog opLog);
}
