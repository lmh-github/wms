package com.gionee.wms.dao;

import com.google.common.collect.Maps;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;
import java.util.Map;

/**
 * Created by Pengbin on 2017/5/4.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:config/spring/spring*.xml"})
public class InvoiceInfoDaoTest extends TestCase {

    @Autowired
    private InvoiceInfoDao invoiceInfoDao;

    @Test
    public void exportQuery() {
        Map<String, Object> params = Maps.newHashMap();
        List<Map<String, String>> mapList = invoiceInfoDao.exprotQuery(params);
        for (Map<String, String> m : mapList) {
            System.out.println(m);
        }
    }
}
