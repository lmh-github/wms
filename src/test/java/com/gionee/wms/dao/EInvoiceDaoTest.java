package com.gionee.wms.dao;

import com.gionee.wms.common.TemplateHelper;
import com.gionee.wms.entity.InvoiceInfo;
import com.gionee.wms.service.common.MailService;
import com.gionee.wms.service.stock.InvoiceInfoService;
import com.google.common.collect.Lists;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;

import static com.gionee.wms.common.WmsConstants.EInvoiceStatus.*;
import static com.gionee.wms.common.WmsConstants.OrderStatus.*;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Created by Pengbin on 2017/3/15.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring*.xml"})
public class EInvoiceDaoTest extends TestCase {

    @Autowired
    private InvoiceInfoService invoiceInfoSerivce;

    @Autowired
    private MailService mailService;

    @Test
    public void queryToMakeOrder() {
        try {
            System.out.println(invoiceInfoSerivce.queryForJob(newArrayList(RECEIVED.getCode()), newArrayList(WAIT_MAKE.toString(), KP_DELAYED.toString())));
            System.out.println(invoiceInfoSerivce.queryForJob(Lists.newArrayList(CANCELED.getCode(), REFUSED.getCode(), BACKED.getCode()), Lists.newArrayList(SUCCESS.toString(), CH_DELAYED.toString())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testP() {
        try {
            MimeMessageHelper messageHelper = mailService.createMimeMessageHelper(false);
            messageHelper.setSubject("金立手机电子发票，请查收，谢谢");
            messageHelper.setText(TemplateHelper.generate(new HashMap<String, Object>(0), "invoice-mail.html"), true);// 以HTML格式发送
            messageHelper.setTo("pengbin@gionee.com");
            mailService.send(messageHelper);
            Thread.sleep(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test1() {
        try {
            InvoiceInfo invoiceInfo = new InvoiceInfo();
            invoiceInfo.setId("8");
            invoiceInfo.setOrderCode("123456");
            invoiceInfo.setFpDm("456");
            invoiceInfo.setFpHm("789");
            invoiceInfo.setKprq(new Date());
            invoiceInfo.setReturnCode("abc");
            invoiceInfo.setOpDate(new Date());
            invoiceInfo.setOpUser("admin");
            invoiceInfo.setStatus("");
            invoiceInfo.setJsonData("{\"a\":123}");
            invoiceInfo.setInvoiceType("E");
            invoiceInfoSerivce.saveOrUpdate(invoiceInfo, true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
