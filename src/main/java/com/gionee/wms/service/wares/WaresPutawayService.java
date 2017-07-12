package com.gionee.wms.service.wares;

import com.gionee.wms.entity.WaresPutaway;
import com.gionee.wms.vo.ServiceCtrlMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by gionee on 2017/7/12.
 */
public interface WaresPutawayService {

    /**
     * @param waresPutaway
     * @return
     */
    ServiceCtrlMessage save(WaresPutaway waresPutaway);

    /**
     * @param waresPutaway
     * @return
     */
    ServiceCtrlMessage update(WaresPutaway waresPutaway);

    /**
     * @param params
     * @return
     */
    List<WaresPutaway> query(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    int queryCount(Map<String, Object> params);

    /**
     * @param id
     * @return
     */
    WaresPutaway get(Long id);
}
