package com.gionee.wms.dao;

import com.gionee.wms.entity.WaresPutaway;

import java.util.List;
import java.util.Map;

/**
 * Created by gionee on 2017/7/12.
 */
@BatisDao
public interface WaresPutawayDao {
    /**
     * @param waresPutaway
     * @return
     */
    int insert(WaresPutaway waresPutaway);

    /**
     * @param waresPutaway
     * @return
     */
    int update(WaresPutaway waresPutaway);

    /**
     * @param id
     * @return
     */
    WaresPutaway get(Long id);

    /**
     * @param params
     * @return
     */
    List<WaresPutaway> query(Map<String, Object> params);

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
