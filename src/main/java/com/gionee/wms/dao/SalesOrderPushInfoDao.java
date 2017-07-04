package com.gionee.wms.dao;

import com.gionee.wms.entity.SalesOrderPushInfo;

/**
 * Created by Pengbin on 2017/5/28.
 */
@BatisDao
public interface SalesOrderPushInfoDao {

    /**
     * @param salesOrderPushInfo
     * @return
     */
    int insert(SalesOrderPushInfo salesOrderPushInfo);
}
