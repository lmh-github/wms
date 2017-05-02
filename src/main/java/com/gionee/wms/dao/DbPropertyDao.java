package com.gionee.wms.dao;

import com.gionee.wms.entity.DbProperty;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Pengbin on 2017/5/4.
 */
@BatisDao
public interface DbPropertyDao {

    DbProperty getByKey(String string);

    int update(DbProperty property);

    int insert(DbProperty property);
}
