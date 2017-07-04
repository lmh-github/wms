package com.gionee.wms.dao;

import com.gionee.wms.entity.UcUser;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/6/7.
 */
@BatisDao
public interface UcUserDao {

    /**
     * @param ucUser
     * @return
     */
    int insert(UcUser ucUser);

    /**
     * @param ucUser
     * @return
     */
    int update(UcUser ucUser);

    /**
     * @param id
     * @param account
     * @return
     */
    UcUser get(@Param("id") Long id, @Param("account") String account, @Param("userName") String userName);

    /**
     * @param params
     * @return
     */
    List<UcUser> query(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    int queryCount(Map<String, Object> params);
}
