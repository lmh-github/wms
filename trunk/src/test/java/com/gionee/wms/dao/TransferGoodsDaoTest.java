package com.gionee.wms.dao;

import java.util.List;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.wms.entity.TransferGoods;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2013-10-21 下午9:31:34
 * @=======================================
 */
@RunWith(value = SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations={"classpath:config/spring/spring*.xml"})
public class TransferGoodsDaoTest extends TestCase {
	
	@Autowired
	TransferDao transferDao;
	
	@Test
	public void indivDaoTest() {
		List<TransferGoods> list = transferDao.getTransferGoodsById(1713122801053l);
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i).getPreparedNum());
		}
		Assert.assertNotNull(list);
	}

}
