package com.gionee.wms.service;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.wms.entity.ShippingInfo;
import com.gionee.wms.service.basis.ShippingInfoService;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2013-10-9 下午3:58:32
 * @=======================================
 */
@RunWith(value = SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations={"classpath:config/spring/spring*.xml"})
public class ShippingInfoServiceTest extends TestCase {

	@Autowired
	ShippingInfoService shippingInfoService;
		
	@Test
	public void testCheckShippingInfo() {
		ShippingInfo info=shippingInfoService.getShippingInfo("shunfeng", "113312767647");
		info.setIsCheck("3");
		System.out.println(shippingInfoService.updateShippingAndOrder(info));
	}
}
