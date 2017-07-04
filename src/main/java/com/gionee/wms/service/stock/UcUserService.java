package com.gionee.wms.service.stock;

import com.gionee.wms.entity.UcUser;
import com.gionee.wms.vo.ServiceCtrlMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/6/7.
 */
public interface UcUserService {

    /**
     * @param ucUser
     * @return
     */
    ServiceCtrlMessage save(UcUser ucUser);

    /**
     * @param ucUser
     * @return
     */
    ServiceCtrlMessage update(UcUser ucUser);

    /**
     * @param account
     * @param userName
     * @return
     */
    UcUser get(String account, String userName);

    /**
     * @param id
     * @return
     */
    UcUser get(Long id);

    /**
     * @param params
     * @return
     */
    List<UcUser> query(Map<String, Object> params);

    /**
     * @param params
     * @return
     */
    int queryCount(Map<String, Object> params);
}
