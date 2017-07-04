package com.gionee.wms.dao;

import com.gionee.wms.entity.WorkOrder;

import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/6/6.
 */
@BatisDao
public interface WorkOrderDao {

    /**
     * @param workOrder
     * @return
     */
    int insert(WorkOrder workOrder);

    /**
     * @param workOrder
     * @return
     */
    int update(WorkOrder workOrder);

    /**
     * @param id
     * @return
     */
    WorkOrder get(Long id);

    /**
     * @param params
     * @return
     */
    List<WorkOrder> query(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    int queryCount(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    int queryToDoCount(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    List<Map<String, String>> exportQuery(Map<String, Object> params);
}
