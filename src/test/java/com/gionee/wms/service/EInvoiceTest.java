package com.gionee.wms.service;

import com.gionee.wms.dto.Page;
import com.gionee.wms.dto.PageResult;
import com.gionee.wms.entity.InvoiceInfo;
import com.gionee.wms.service.stock.EInvoiceService;
import com.gionee.wms.service.stock.InvoiceInfoService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;

/**
 * Created by Pengbin on 2017/3/14.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring*.xml"})
public class EInvoiceTest extends TestCase {

    @Autowired
    private InvoiceInfoService invoiceInfoSerivce;

    @Autowired
    private EInvoiceService eInvoiceService;


    @Test
    public void test() {
        try {

            Page page = new Page(10);
            page.calculate();
            PageResult<InvoiceInfo> invoiceInfoPageResult = invoiceInfoSerivce.query(new HashMap<String, Object>(), page);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void mk() {
        eInvoiceService.makeEInvoice("652371629803845");
    }
}
