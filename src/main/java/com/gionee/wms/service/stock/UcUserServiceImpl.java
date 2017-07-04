package com.gionee.wms.service.stock;

import com.gionee.wms.dao.UcUserDao;
import com.gionee.wms.entity.UcUser;
import com.gionee.wms.vo.ServiceCtrlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/6/7.
 */
@Service
public class UcUserServiceImpl implements UcUserService {
    @Autowired
    private UcUserDao ucUserDao;

    /** {@inheritDoc} */
    @Override
    @Transactional
    public ServiceCtrlMessage save(UcUser ucUser) {
        ucUser.setStatus("enabled");
        ucUser.setCreateTime(new Date());
        ucUserDao.insert(ucUser);
        return new ServiceCtrlMessage(true, "");
    }

    /** {@inheritDoc} */
    @Override
    public ServiceCtrlMessage update(UcUser ucUser) {
        ucUserDao.update(ucUser);
        return new ServiceCtrlMessage(true, "");
    }

    /** {@inheritDoc} */
    @Override
    public UcUser get(String account, String userName) {
        return ucUserDao.get(null, account, userName);
    }

    /** {@inheritDoc} */
    @Override
    public UcUser get(Long id) {
        return ucUserDao.get(id, null, null);
    }

    /** {@inheritDoc} */
    @Override
    public List<UcUser> query(Map<String, Object> params) {
        return ucUserDao.query(params);
    }

    /** {@inheritDoc} */
    @Override
    public int queryCount(Map<String, Object> params) {
        return ucUserDao.queryCount(params);
    }
}
