package com.gionee.wms.dao;

import com.gionee.wms.entity.SalesOrderNodeInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/5/24.
 */
@BatisDao
public interface SalesOrderNodeInfoDao {

    /**
     * @param salesOrderNodeInfo
     * @return
     */
    int insert(SalesOrderNodeInfo salesOrderNodeInfo);

    /**
     * @param salesOrderNodeInfo
     * @return
     */
    int update(SalesOrderNodeInfo salesOrderNodeInfo);

    /**
     * @param orderCode
     * @return
     */
    SalesOrderNodeInfo get(String orderCode);

    /**
     * @param params
     * @return
     */
    List<Map<String, Object>> query(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    int queryCount(Map<String, Object> params);
}
