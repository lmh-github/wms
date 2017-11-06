package com.gionee.wms.service.stock;

import com.gionee.wms.entity.OrderOutlier;
import com.gionee.wms.vo.ServiceCtrlMessage;

import java.util.List;
import java.util.Map;

/**
 * Date: 2017/10/30
 * Time: 15:51
 *
 * @author huyunfan
 */
public interface OrderOutlierService {

    /**
     * @param orderOutlier
     * @return
     */
    ServiceCtrlMessage save(OrderOutlier orderOutlier);

    /**
     * @param orderOutlier
     * @return
     */
    ServiceCtrlMessage update(OrderOutlier orderOutlier);


    /**
     * @param params
     * @return
     */
    List<OrderOutlier> query(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    List<OrderOutlier> queryAll(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    int queryCount(Map<String, Object> params);

    /**
     * @param id
     * @return
     */
    OrderOutlier get(Long id);

    /**
     *
     * @param id
     * @return
     */
    int remove(String id);
}
