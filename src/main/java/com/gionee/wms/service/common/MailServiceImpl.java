package com.gionee.wms.service.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.task.TaskExecutor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.MimeMessage;

/**
 * Created by Pengbin on 2017/4/13.
 */
@Component
public class MailServiceImpl implements MailService {

    private final Logger logger = LoggerFactory.getLogger(MailServiceImpl.class);

    @Autowired
    @Qualifier("taskExecutor")
    private TaskExecutor taskExecutor;
    @Autowired
    private JavaMailSender javaMailSender;
    /** 邮件发送人 */
    @Value("${mail.from}")
    private String from;

    /**
     * {@inheritDoc}
     */
    @Override
    public MimeMessageHelper createMimeMessageHelper(boolean multipart) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, multipart, "UTF-8");
            mimeMessageHelper.setFrom(from);
            return mimeMessageHelper;
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    /** {@inheritDoc} */
    @Override
    public void send(final MimeMessageHelper mimeMessageHelper) {
        taskExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    javaMailSender.send(mimeMessageHelper.getMimeMessage());
                } catch (Exception e) {
                    logger.error(e.getMessage(), e);
                }
            }
        });
    }


}
