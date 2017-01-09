/**
 * @(#) Editor.java Created on 2014年8月26日
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.gionee.wms.job;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.common.JaxbUtil;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.entity.SkuMap;
import com.gionee.wms.service.stock.SFWebService;
import com.gionee.wms.service.wares.SkuMapService;
import com.gionee.wms.service.wares.WaresService;
import com.sf.integration.warehouse.bean.Item;
import com.sf.integration.warehouse.request.WmsMerchantCatalogBatchRequest;
import com.sf.integration.warehouse.response.WmsMerchantCatalogBatchResponse;

/**
 * 同步商品信息到顺丰侧
 * The class <code>SyncSfSkuMapTask</code>
 * @author dujz
 * @version 1.0
 */
public class SyncSfSkuMapTask {
	private static Logger logger = LoggerFactory
			.getLogger(SyncSfSkuMapTask.class);
	
	@Autowired
	private WaresService waresService;
	
	@Autowired
	private SkuMapService skuMapService;
	
	@Autowired
	private SFWebService sfWebService;
	
	/**
	 * 每天定时同步商品信息到顺丰侧
	 */
	public void execute() {
		
		logger.info("同步商品信息到顺丰服务正在启动...");
		SkuMap skuMap = new SkuMap();
		skuMap.setOuterCode("sf");
		skuMap.setSkuPushStatus(0);
		//获取未推送顺丰所有sku
		List<SkuMap> skuMaps = skuMapService.selectSkuMaps(skuMap);
		if(skuMaps == null || skuMaps.size() ==0) {
			logger.info("不存在需要同步的商品信息...");
			return;
		}
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
		WmsMerchantCatalogBatchResponse response = sfWebService.outsideToLscmService(WmsMerchantCatalogBatchRequest.class,WmsMerchantCatalogBatchResponse.class,request);
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
