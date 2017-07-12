package com.gionee.wms.service.wares;

import com.gionee.wms.dao.WaresPutawayDao;
import com.gionee.wms.entity.WaresPutaway;
import com.gionee.wms.vo.ServiceCtrlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by gionee on 2017/7/12.
 */
@Service
public class WaresPutawayServiceImpl implements WaresPutawayService {

    @Autowired
    private WaresPutawayDao waresPutawayDao;

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceCtrlMessage save(WaresPutaway waresPutaway) {
        waresPutawayDao.insert(waresPutaway);
        return new ServiceCtrlMessage(true, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ServiceCtrlMessage update(WaresPutaway waresPutaway) {
        waresPutawayDao.update(waresPutaway);
        return new ServiceCtrlMessage(true, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<WaresPutaway> query(Map<String, Object> params) {
        return waresPutawayDao.query(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int queryCount(Map<String, Object> params) {
        return waresPutawayDao.queryCount(params);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WaresPutaway get(Long id) {
        return waresPutawayDao.get(id);
    }
}
