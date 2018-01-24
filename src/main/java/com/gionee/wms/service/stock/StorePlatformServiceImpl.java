package com.gionee.wms.service.stock;

import com.gionee.wms.common.Util;
import com.gionee.wms.dao.StorePlatformDao;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.entity.Stock;
import com.gionee.wms.entity.StorePlatform;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.web.extend.DwzMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StorePlatformServiceImpl implements  StorePlatformService{

    @Autowired
    private StorePlatformDao storePlatformDao;
    @Autowired
    private StockService stockService;



    @Override
    public int deleteByPrimaryKey(String id) {
        return storePlatformDao.deleteByPrimaryKey(id);
    }

    @Override
    public int insert(StorePlatform record) {
        return storePlatformDao.insert(record);
    }

    @Override
    public int insertSelective(StorePlatform record) {
        return storePlatformDao.insertSelective(record);
    }

    @Override
    public StorePlatform selectByPrimaryKey(String id) {
        return storePlatformDao.selectByPrimaryKey(id);
    }

    @Override
    public int updateByPrimaryKeySelective(StorePlatform record) {
        return storePlatformDao.updateByPrimaryKeySelective(record);
    }

    @Override
    public int updateByPrimaryKey(StorePlatform record) {
        return storePlatformDao.updateByPrimaryKey(record);
    }

    @Override
    public List<StorePlatform> getAll(Map<String, Object> criteria) {
        return storePlatformDao.getAll(criteria);
    }

    @Override
    public List<StorePlatform> getFirst(Map<String, Object> criteria) {
        return storePlatformDao.getFirst(criteria);
    }

    @Override
    public Sku getSku(StorePlatform record) {
        List<Sku> skus = storePlatformDao.getSku(record.getSkuNo());
        if(skus!=null&&skus.size()>0){
            return skus.get(0);
        }
        return null;
    }

    @Override
    public int getTotalUseStoreBySku(String skuNo) {
        StorePlatform record = new StorePlatform();
        record.setSkuNo(skuNo);
        return storePlatformDao.getTotalUseStoreBySku(record);
    }

    @Override
    public boolean hasEnoughStorePlatform(String skuNo, String platformNo,Integer needStore) throws Exception {
        StorePlatform record = new StorePlatform();
        record.setSkuNo(skuNo);
        record.setPlatformNo(platformNo);
        StorePlatform store = storePlatformDao.getPlatformStoreBySku(record);
        int totalNum = 0;
        if(store!=null) totalNum = store.getTotalNum();
        needStore = needStore ==null?0:needStore;
        if(totalNum>=needStore){
            return true;
        }
        return false;
    }

    @Override
    public void afterOutStoreHandle(String skuId, String platformNo,Integer needStore) throws Exception {
        StorePlatform record = new StorePlatform();
        Sku sku = storePlatformDao.selectSkuByKey(skuId);
        record.setSkuNo(sku.getSkuCode());
        record.setPlatformNo(platformNo);
        StorePlatform store = storePlatformDao.getPlatformStoreBySku(record);
        int totalNum = 0;
        if(store!=null) totalNum = store.getTotalNum();
        needStore = needStore ==null?0:needStore;
        if(totalNum<needStore) throw new ServiceException(Util.getNameByCode(platformNo)+"平台库存不足");
        store.setTotalNum(totalNum-needStore);
        storePlatformDao.updateByPrimaryKey(store);
    }

    @Override
    public boolean hasEnoughStore(String skuNo, Integer needStore)  {
        String wareHouseCode = "01";//东莞电商仓
        Stock stock = stockService.getStock(wareHouseCode, skuNo);
        StorePlatform storePlatform = new StorePlatform();
        storePlatform.setSkuNo(skuNo);
        int totalUseStore= storePlatformDao.getTotalUseStoreBySku(storePlatform);
        if(stock==null) return false;
        if(needStore==null)needStore=0;
        int temp = stock.getSalesQuantity() - totalUseStore -needStore;
        if(temp<0) return false;
        return true;
    }

}
