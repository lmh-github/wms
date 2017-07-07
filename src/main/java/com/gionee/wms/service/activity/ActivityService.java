package com.gionee.wms.service.activity;

import com.gionee.wms.entity.Activity;
import com.gionee.wms.vo.ServiceCtrlMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by Gionee on 2017/7/5.
 */
public interface ActivityService {

    /**
     * @param activity
     * @return
     */
    ServiceCtrlMessage save(Activity activity);

    /**
     * @param activity
     * @return
     */
    ServiceCtrlMessage update(Activity activity);

    /**
     * @param params
     * @return
     */
    List<Activity> query(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    int queryCount(Map<String, Object> params);

    /**
     * @param id
     * @return
     */
    Activity get(Long id);

}
