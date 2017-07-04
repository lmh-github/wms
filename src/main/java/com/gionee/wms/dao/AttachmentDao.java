package com.gionee.wms.dao;

import com.gionee.wms.entity.Attachment;

import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/6/7.
 */
@BatisDao
public interface AttachmentDao {

    /**
     * @param attachment
     * @return
     */
    int insert(Attachment attachment);

    /**
     * @param attachment
     * @return
     */
    int update(Attachment attachment);

    /**
     * @param id
     * @return
     */
    int delete(Long id);

    /**
     * @param id
     * @return
     */
    Attachment get(Long id);

    /**
     * @param params
     * @return
     */
    List<Attachment> list(Map<String, Object> params);
}
