package com.gionee.wms.service.stock;

import com.gionee.wms.dao.AttachmentDao;
import com.gionee.wms.entity.Attachment;
import com.gionee.wms.vo.ServiceCtrlMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Pengbin on 2017/6/8.
 */
@Service
public class AttachmentServiceImpl implements AttachmentService {
    @Autowired
    private AttachmentDao attachmentDao;

    /** {@inheritDoc} */
    @Override
    @Transactional
    public ServiceCtrlMessage add(Attachment attachment) {
        if (attachment == null) {
            return new ServiceCtrlMessage(false, "没有要保存的信息！");
        }

        attachmentDao.insert(attachment);
        return new ServiceCtrlMessage(true, "");
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public ServiceCtrlMessage invalid(Long id) {
        Attachment attachment = attachmentDao.get(id);
        attachment.setStatus("无效");
        attachmentDao.update(attachment);
        return new ServiceCtrlMessage(true, "");
    }

    /** {@inheritDoc} */
    @Override
    public Attachment get(Long id) {
        return attachmentDao.get(id);
    }
}
