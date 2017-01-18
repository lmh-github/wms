package com.gionee.wms.dao;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.vo.SalesOrderVo;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2013-10-21 下午9:31:34
 * @=======================================
 */
@RunWith(value = SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations={"classpath:config/spring/spring*.xml"})
public class SalesOrderDaoTest extends TestCase {
	
	@Autowired
	SalesOrderDao salesOrderDao;
	
	@Test
	public void goodsListDaoTest() {
		List<SalesOrderGoods> goodsList = salesOrderDao.queryGoodsListByOrderId(2664l);
		System.out.println(goodsList.size());
		Assert.assertNotNull(goodsList);
	}
	
	@Test
	public void salesOrderVoTest() {
		List<SalesOrderVo> orderVoList=salesOrderDao.queryOrderListByOrderIds(new String[] {"2664"});
		for (SalesOrderVo salesOrderVo : orderVoList) {
			System.out.println(salesOrderVo.getId() + " " + salesOrderVo.getFullAddress());
			List<SalesOrderGoods> goods=salesOrderVo.getGoodsList();
			for (SalesOrderGoods salesOrderGoods : goods) {
				System.out.println(salesOrderGoods.getSkuId() + " " + salesOrderGoods.getSkuName());
			}
		}
	}

}
