package com.gionee.wms.dao;


import com.gionee.wms.entity.Activity;

import java.util.List;
import java.util.Map;

/**
 * Created by gionee on 2017/7/5.
 */
@BatisDao
public interface ActivityDao {
    /**
     * @param activity
     * @return
     */
    int insert(Activity activity);

    /**
     * @param activity
     * @return
     */
    int update(Activity activity);

    /**
     * @param id
     * @return
     */
    Activity get(Long id);

    /**
     * @param params
     * @return
     */
    List<Activity> query(Map<String, Object> params);

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
