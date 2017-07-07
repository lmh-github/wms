package com.gionee.wms.service.activity.impl;

import com.gionee.wms.dao.ActivityDao;
import com.gionee.wms.entity.Activity;
import com.gionee.wms.service.activity.ActivityService;
import com.gionee.wms.vo.ServiceCtrlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * Created by Gionee on 2017/7/5.
 */
@Service
public class ActivityServiceImpl implements ActivityService {

    @Autowired
    private ActivityDao activityDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceCtrlMessage save(Activity activity) {
        activityDao.insert(activity);
        return new ServiceCtrlMessage(true, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceCtrlMessage update(Activity activity) {
        activityDao.update(activity);
        return new ServiceCtrlMessage(true, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Activity> query(Map<String, Object> params) {
        return activityDao.query(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int queryCount(Map<String, Object> params) {
        return activityDao.queryCount(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Activity get(Long id) {
        return activityDao.get(id);
    }

}
