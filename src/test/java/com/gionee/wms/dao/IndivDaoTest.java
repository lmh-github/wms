package com.gionee.wms.dao;

import junit.framework.TestCase;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.wms.entity.Indiv;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2013-10-21 下午9:31:34
 * @=======================================
 */
@RunWith(value = SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations={"classpath:config/spring/spring*.xml"})
public class IndivDaoTest extends TestCase {
	
	@Autowired
	IndivDao indivDao;
	
	@Test
	public void indivDaoTest() {
		Indiv indiv = indivDao.queryIndivByCode("356960020008145");
		System.out.println(indiv.getId());
		Assert.assertNotNull(indiv);
	}

}
