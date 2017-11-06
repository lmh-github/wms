package com.gionee.wms.service.log;

import com.gionee.wms.dao.OrderOutlierLogDao;
import com.gionee.wms.entity.OrderOutlierLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Date: 2017/10/31
 * Time: 15:32
 *
 * @author huyunfan
 */
@Service
public class OrderOutlierLogServiceImpl implements OrderOutlierLogService {

    @Autowired
    private OrderOutlierLogDao orderOutlierLogDao;

    @Override
    public List<OrderOutlierLog> query(Map<String, Object> params) {
        return orderOutlierLogDao.query(params);
    }

    @Override
    public int queryCount(Map<String, Object> params) {
        return orderOutlierLogDao.queryCount(params);
    }

    @Override
    public int insertBatch(List<OrderOutlierLog> logList) {
        return orderOutlierLogDao.insertBatch(logList);
    }
}
