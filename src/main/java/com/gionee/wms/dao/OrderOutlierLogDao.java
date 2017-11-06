package com.gionee.wms.dao;

import com.gionee.wms.entity.OrderOutlierLog;

import java.util.List;
import java.util.Map;

/**
 * Date: 2017/10/30
 * Time: 15:26
 *
 * @author huyunfan
 */
@BatisDao
public interface OrderOutlierLogDao {

    /**
     * @param params
     * @return
     */
    List<OrderOutlierLog> query(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    int queryCount(Map<String, Object> params);

    /**
     * @param logList
     * @return
     */
    int insertBatch(List<OrderOutlierLog> logList);
}
