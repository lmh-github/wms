package com.gionee.wms.service.stock;

import com.gionee.wms.entity.WorkOrder;
import com.gionee.wms.vo.ServiceCtrlMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/6/6.
 */
public interface WorkOrderService {

    /**
     * @param workOrder
     * @return
     */
    ServiceCtrlMessage save(WorkOrder workOrder);

    /**
     * @param workOrder
     * @return
     */
    ServiceCtrlMessage update(WorkOrder workOrder);

    /**
     * @param id
     * @return
     */
    ServiceCtrlMessage accept(Long id);

    /**
     * @param workOrder
     * @return
     */
    ServiceCtrlMessage up(WorkOrder workOrder);

    /**
     * @param workOrder
     * @return
     */
    ServiceCtrlMessage finish(WorkOrder workOrder);

    /**
     * @param workOrder
     * @return
     */
    ServiceCtrlMessage cancel(WorkOrder workOrder);

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
     * @param id
     * @return
     */
    WorkOrder get(Long id);

    /**
     * @param params
     * @return
     */
    List<Map<String, String>> exportQuery(Map<String, Object> params);
}
