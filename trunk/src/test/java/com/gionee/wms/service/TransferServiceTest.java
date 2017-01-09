package com.gionee.wms.service;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.wms.entity.Transfer;
import com.gionee.wms.service.stock.TransferService;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2013-10-9 下午3:58:32
 * @=======================================
 */
@RunWith(value = SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations={"classpath:config/spring/spring*.xml"})
public class TransferServiceTest extends TestCase {

	@Autowired
	TransferService transferService;
	
//	@Test
//	public void testAddShippingInfo() {
//		ShippingInfo shippingInfo = new ShippingInfo();
//		shippingInfo.setOrderCode("1234567890");	// 订单号
//		shippingInfo.setCompany("shunfeng");	// 物流公司编码
//		shippingInfo.setShippingNo("1234567890");	// 物流快递单号
//		shippingInfo.setToAddr("广东深圳");
//		System.out.println(shippingInfoDao.addShippingInfo(shippingInfo));
//	}
	
//	@Test
//	public void testAddShippingInfo() {
//		ShippingInfo shippingInfo=new ShippingInfo();
//		shippingInfo.setId("123456");
//		shippingInfo.setData("");
////		shippingInfoDao.addShippingInfo(shippingInfo);
//		ShippingInfo temp=new ShippingInfo();
//		temp.setCompany("yuantong");
//		temp.setShippingNo("2775291500");
//		ShippingInfo shippingInfo1 = shippingInfoDao.getShippingInfo(temp);
//		System.out.println(shippingInfo1);
////		shippingInfo1.setSubscribeTime(new Date());
////		System.out.println(shippingInfoDao.updateShippingInfo(shippingInfo1));
//	}
//	
//	@Test
//	public void testNeedToSub() {
//		List<ShippingInfo> lists = shippingInfoDao.getShippingInfoNeedToSub();
//		System.out.println(lists.size());
//	}
	
	@Test
	public void testQueryShippingInfo() {
		Transfer transfer=transferService.getTransferById(new Long("1"));
		System.out.println(transfer.getTransferTo());
	}
}
