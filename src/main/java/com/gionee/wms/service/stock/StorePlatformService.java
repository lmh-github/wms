package com.gionee.wms.service.stock;

import com.gionee.wms.entity.Sku;
import com.gionee.wms.entity.StorePlatform;

import java.util.List;
import java.util.Map;

public interface StorePlatformService {
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
     *  根据skuNo获取SKU
     * @param record
     * @return
     */
    Sku getSku(StorePlatform record);

    /**
     *  根据skuNo获取已分配库存
     * @param skuNo
     * @return
     */
    int getTotalUseStoreBySku(String skuNo);

    /**
     * 发货前校验库存是否足够，不足为false
     * @param skuNo
     * @param platformNo
     * @param needStore 需要出库的库存数量
     * @return
     * @throws Exception
     */
    boolean hasEnoughStorePlatform(String skuNo,String platformNo,Integer needStore) throws Exception;

    /**
     * 出库后平台库存数量递减
     * @param skuId
     * @param platformNo
     * @throws Exception
     */
    void afterOutStoreHandle(String skuId,String platformNo,Integer needStore) throws Exception;

    /**
     * 除了分配给平台库存，该SKU剩余的库存是否足够
     * @param skuNo
     * @param needStore
     * @return
     * @throws Exception
     */
    boolean hasEnoughStore(String skuNo,Integer needStore);
}
