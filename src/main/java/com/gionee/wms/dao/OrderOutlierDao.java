package com.gionee.wms.dao;

import com.gionee.wms.entity.OrderOutlier;

import java.util.List;
import java.util.Map;

/**
 * Date: 2017/10/30
 * Time: 15:26
 *
 * @author huyunfan
 */
@BatisDao
public interface OrderOutlierDao {
    /**
     * @param orderOutlier
     * @return
     */
    int insert(OrderOutlier orderOutlier);

    /**
     * @param orderOutlier
     * @return
     */
    int update(OrderOutlier orderOutlier);

    /**
     * @param id
     * @return
     */
    OrderOutlier get(Long id);

    /**
     * @param params
     * @return
     */
    List<OrderOutlier> query(Map<String, Object> params);

    /**
     *
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
     *
     * @param id
     * @return
     */
    int delete(String id);
}
