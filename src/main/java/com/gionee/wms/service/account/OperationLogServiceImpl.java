package com.gionee.wms.service.account;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.gionee.wms.dao.OperationDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.OperationLog;
import com.gionee.wms.service.ServiceException;
import com.google.common.collect.Maps;
@Service("opLogService")
public class OperationLogServiceImpl implements OperationLogService {
	private OperationDao operationDao;

	@Override
	public void addOpLog(OperationLog opLog) throws ServiceException {
		try {
			operationDao.addOpLog(opLog);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

	}

	@Override
	public List<OperationLog> getOpLogList(Map<String, Object> criteria, Page page) {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		try {
			return operationDao.queryOpLogList(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Integer getOpLogListTotal(Map<String, Object> criteria) {
		return operationDao.queryOpLogListTotal(criteria);
	}

	public void setOperationDao(OperationDao operationDao) {
		this.operationDao = operationDao;
	}

}
