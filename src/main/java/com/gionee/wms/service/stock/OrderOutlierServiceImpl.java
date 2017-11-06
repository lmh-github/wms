package com.gionee.wms.service.stock;

import com.gionee.wms.dao.OrderOutlierDao;
import com.gionee.wms.entity.OrderOutlier;
import com.gionee.wms.vo.ServiceCtrlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Date: 2017/10/30
 * Time: 15:51
 *
 * @author huyunfan
 */
@Service
public class OrderOutlierServiceImpl implements OrderOutlierService {

    @Autowired
    private OrderOutlierDao orderOutlierDao;

    @Override
    public ServiceCtrlMessage save(OrderOutlier orderOutlier) {
        orderOutlierDao.insert(orderOutlier);
        return new ServiceCtrlMessage(true, "");
    }

    @Override
    public ServiceCtrlMessage update(OrderOutlier orderOutlier) {
        orderOutlierDao.update(orderOutlier);
        return new ServiceCtrlMessage(true, "");
    }

    @Override
    public List<OrderOutlier> query(Map<String, Object> params) {
        return orderOutlierDao.query(params);
    }

    @Override
    public List<OrderOutlier> queryAll(Map<String, Object> params) {
        return orderOutlierDao.queryAll(params);
    }

    @Override
    public int queryCount(Map<String, Object> params) {
        return orderOutlierDao.queryCount(params);
    }

    @Override
    public OrderOutlier get(Long id) {
        return orderOutlierDao.get(id);
    }

    @Override
    public int remove(String id) {
        return orderOutlierDao.delete(id);
    }
}
