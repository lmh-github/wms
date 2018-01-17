package com.gionee.wms.dao;

import com.gionee.wms.entity.StorePlatform;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@BatisDao
public interface StorePlatformDao {
    int deleteByPrimaryKey(String id);

    int insert(StorePlatform record);

    int insertSelective(StorePlatform record);

    StorePlatform selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(StorePlatform record);

    int updateByPrimaryKey(StorePlatform record);

    List<StorePlatform> getAll(Map<String, Object> criteria);

    /**
     *  查询第一条记录
     * @param criteria
     * criteria.put("skuNo" );
     * criteria.put("platformNo" );
     * @return
     */
    List<StorePlatform> getFirst(Map<String, Object> criteria);
}