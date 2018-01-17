package com.gionee.wms.service.stock;

import com.gionee.wms.dao.StorePlatformDao;
import com.gionee.wms.entity.StorePlatform;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class StorePlatformServiceImpl implements  StorePlatformService{

    @Autowired
    private StorePlatformDao storePlatformDao;

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
}
