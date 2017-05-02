package com.gionee.wms.service.common;

import org.springframework.mail.javamail.MimeMessageHelper;

/**
 * 邮件Servcie
 * Created by Pengbin on 2017/4/13.
 */
public interface MailService {
    /**
     * 创建 MimeMessageHelper
     * @param multipart 是否带附件格式
     * @return MimeMessageHelper
     */
    MimeMessageHelper createMimeMessageHelper(boolean multipart);

    /**
     * 用异步方式发送邮件，解决发送慢的问题
     * @param mimeMessageHelper mimeMessageHelper
     */
    void send(MimeMessageHelper mimeMessageHelper);
}
