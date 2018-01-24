package com.gionee.wms.dao;

import com.gionee.wms.entity.Sku;
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

    /**
     * 根据sku编号或去SKU记录
     * record.setSkuNo
     * @param skuNo
     * @return
     */
    List<Sku> getSku(String skuNo);

    /**
     * 根据主键获取Sku
     * @param id
     * @return
     */
    Sku  selectSkuByKey(String id);

    /**
     *  根据sku获取已分配库存
     * @param record
     * skuNo
     * @return
     */
    int getTotalUseStoreBySku(StorePlatform record);

    /**
     * 根据sku和平台编号获取已分配库存
     * @param record
     * skuNo
     * platformNo
     * @return
     */
    StorePlatform getPlatformStoreBySku(StorePlatform record);
}