/**
 * @(#) Editor.java Created on 2014年8月26日
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.sf;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.wms.common.JaxbUtil;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.entity.SkuMap;
import com.gionee.wms.job.SyncSfSkuMapTask;
import com.gionee.wms.service.stock.SFWebService;
import com.gionee.wms.service.wares.SkuMapService;
import com.gionee.wms.service.wares.WaresService;
import com.sf.integration.warehouse.bean.Item;
import com.sf.integration.warehouse.request.WmsMerchantCatalogBatchRequest;
import com.sf.integration.warehouse.response.WmsMerchantCatalogBatchResponse;
import com.sf.integration.warehouse.service.IOutsideToLscmService;

/**
 * The class <code>SkuMapJobTest.java</code>
 *
 * @author dujz
 * @version 1.0
 */
@RunWith(value = SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations={"classpath:config/spring/spring*.xml"})
public class SkuMapJobTest {
	private static Logger logger = LoggerFactory
			.getLogger(SkuMapJobTest.class);
	@Autowired
	private WaresService waresService;
	
	@Autowired
	private SkuMapService skuMapService;
	
	@Autowired
	private SFWebService sfWebService;
	
	@Test
	public void jobExecute() {
		SkuMap skuMap = new SkuMap();
		skuMap.setOuterCode("sf");
		skuMap.setSkuPushStatus(0);
		//获取顺丰所有未推送sku
		List<SkuMap> skuMaps = skuMapService.selectSkuMaps(skuMap);
		//顺丰商品业务实体集合
		List<Item> items = new ArrayList<Item>();
		for(SkuMap sm : skuMaps) {
			Item item = new Item();
			Sku sku = waresService.getSkuByCode(sm.getSkuCode());
			item.setItem(sm.getOuterSkuCode());
			item.setDescription(sku.getSkuName());
			item.setQuantity_um_1(sku.getWares().getMeasureUnit());
			item.setStorage_template(sku.getWares().getMeasureUnit());
			if(sku.getWares().getIndivEnabled() == 1) {
				item.setSerial_num_track_inbound("Y");
				item.setSerial_num_track_outbound("Y");
			} else {
				item.setSerial_num_track_inbound("N");
				item.setSerial_num_track_outbound("N");
			}
			items.add(item);
		}
		WmsMerchantCatalogBatchRequest request = new WmsMerchantCatalogBatchRequest();
		request.setInterface_action_code("NEW");
		request.setItemlist(items);
		
		WmsMerchantCatalogBatchResponse response = 
				sfWebService.outsideToLscmService(WmsMerchantCatalogBatchRequest.class,WmsMerchantCatalogBatchResponse.class,request);
		if(response.getResult()) {
			/**
			 * 更改推送状态
			 */
			for(SkuMap sm : skuMaps) {
				sm.setSkuPushStatus(1);
				skuMapService.updateSkuMap(sm);
			}
			logger.info("同步商品信息到顺丰成功完成...");
		} else {
			logger.info("同步商品信息到顺丰未成功");
		}
		logger.info("同步商品信息到顺丰服务运行完成...");
	}
}
