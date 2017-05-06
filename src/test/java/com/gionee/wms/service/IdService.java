package com.gionee.wms.service;

import com.gionee.wms.service.common.IDGenerator;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Pengbin on 2017/5/4.
 */
@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring*.xml"})
public class IdService extends TestCase {
    @Autowired
    private IDGenerator idGenerator;

    @Test
    public void getId() {
        try {
            System.out.println(idGenerator.getId("abc.abc"));
            Thread.sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
