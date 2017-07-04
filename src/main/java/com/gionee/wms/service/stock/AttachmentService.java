package com.gionee.wms.service.stock;

import com.gionee.wms.entity.Attachment;
import com.gionee.wms.vo.ServiceCtrlMessage;

/**
 * Created by Pengbin on 2017/6/8.
 */
public interface AttachmentService {

    /**
     * @param attachment
     * @return
     */
    ServiceCtrlMessage add(Attachment attachment);

    /**
     * @param id
     * @return
     */
    ServiceCtrlMessage invalid(Long id);

    /**
     * @param id
     * @return
     */
    Attachment get(Long id);
}
