package com.gionee.wms.service.common;

import com.gionee.wms.dao.DbPropertyDao;
import com.gionee.wms.entity.DbProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Pengbin on 2017/5/4.
 */
public class DatabaseIDGenerator implements IDGenerator {
    private static Logger log = LoggerFactory.getLogger(DatabaseIDGenerator.class);

    @Autowired
    private DbPropertyDao dbPropertyDao;

    private int idBlockSize = 1;

    private int tryTime = 10;

    private Map<String, IdState> idContainer = new HashMap<>();

    public void setIdBlockSize(int idBlockSize) {
        this.idBlockSize = idBlockSize;
    }

    public void setTryTime(int tryTime) {
        this.tryTime = tryTime;
    }

    private void setNextGroupIdState(IdState idState, String key, int tryTime) throws Exception {
        if (tryTime < 0) {
            log.error("get id error, have try " + tryTime + " times for get id, but can't get id");
            throw new Exception("get id error.");
        }
        DbProperty dbProperty = dbPropertyDao.getByKey(key + ".id");
        if (dbProperty == null) {
            dbProperty = new DbProperty();
            dbProperty.setKey(key + ".id");
            dbProperty.setValue("" + (1 + idBlockSize));
            try {
                if (dbPropertyDao.insert(dbProperty) == 1) {
                    idState.curValue = 1;
                    idState.maxValue = 1 + idBlockSize;
                    return;
                }
            } catch (Exception e1) {
                e1.printStackTrace();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setNextGroupIdState(idState, key, tryTime - 1);
            }
        }
        Integer curValue = Integer.parseInt(dbProperty.getValue());
        Integer maxValue = curValue + idBlockSize;
        dbProperty.setValue(maxValue.toString());
        if (dbPropertyDao.update(dbProperty) == 1) {
            idState.curValue = curValue;
            idState.maxValue = maxValue;
            return;
        } else {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            setNextGroupIdState(idState, key, tryTime - 1);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class,propagation = Propagation.REQUIRES_NEW)
    synchronized public Integer getId(String key) {
        IdState idState;
        if (idContainer.containsKey(key)) {
            idState = idContainer.get(key);
            if (idState.curValue + 1 < idState.maxValue) {
                idState.curValue = idState.curValue + 1;
            } else {
                try {
                    setNextGroupIdState(idState, key, tryTime);
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }
        } else {
            idState = new IdState();
            try {
                setNextGroupIdState(idState, key, tryTime);
                idContainer.put(key, idState);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return idState.curValue;
    }

    @Override
    public Integer getId() {
        return getId("nullkey");
    }

    private static class IdState {
        private int curValue;
        private int maxValue;
    }
}
