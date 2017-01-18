/**
 * @(#) Editor.java Created on 2014年8月26日
 * Copyright (c) 2014 GIONEE. All Rights Reserved
 */
package com.sf;



import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.wms.common.DateConvert;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.entity.SkuMap;
import com.gionee.wms.entity.Transfer;
import com.gionee.wms.entity.TransferGoods;
import com.gionee.wms.service.stock.SFWebService;
import com.gionee.wms.service.stock.TransferService;
import com.gionee.wms.service.wares.SkuMapService;
import com.sf.integration.warehouse.request.WmsPurchaseOrderRequest;
import com.sf.integration.warehouse.request.WmsPurchaseOrderRequestHeader;
import com.sf.integration.warehouse.request.WmsPurchaseOrderRequestItem;
import com.sf.integration.warehouse.request.WmsRequest;
import com.sf.integration.warehouse.response.WmsPurchaseOrderResponse;

/**
 * The class <code>SkuMapJobTest.java</code>
 *
 * @version 1.0
 */
@RunWith(value = SpringJUnit4ClassRunner.class)  
@ContextConfiguration(locations={"classpath:config/spring/spring*.xml"})
public class TransferJobTest {
	
	@Autowired
	private TransferService transferService;
	
	@Autowired
	private SkuMapService skuMapService;
	
	@Autowired
	private SFWebService sfWebService;
	
	
	@Test
	public void jobExecute() {
		WmsPurchaseOrderRequest request = new WmsPurchaseOrderRequest();
		WmsPurchaseOrderRequestHeader header = new WmsPurchaseOrderRequestHeader();
		List<WmsPurchaseOrderRequestItem> detailList = new ArrayList<WmsPurchaseOrderRequestItem>();
		SkuMap skuMap = new SkuMap();
		List<Transfer> list = transferService.getTransfered();
		for (Transfer transfer : list) {
			header.setCompany(WmsConstants.SF_COMPANY);
			header.setWarehouse(WmsConstants.SF_WAREHOUSE);
			header.setErp_order_num(String.valueOf(transfer.getTransferId()));
			header.setErp_order_type("调拨入库");
			header.setTransfer_warehouse(WmsConstants.SF_WAREHOUSE);
			header.setOrder_date(DateConvert.convertD2String(transfer.getCreateTime()));
			header.setScheduled_receipt_date(DateConvert.convertD2String(transfer.getCreateTime()));
			List<TransferGoods> goods = transferService.getTransferGoodsById(transfer.getTransferId());
			WmsPurchaseOrderRequestItem item = new WmsPurchaseOrderRequestItem();
			for (TransferGoods good : goods) {
				skuMap = skuMapService.getSkuMapBySkuCode(good.getSkuCode(), "sf");
				item.setItem(skuMap.getOuterSkuCode());
				item.setTotal_qty(String.valueOf(good.getQuantity()));
				detailList.add(item);
			}
			request.setHeader(header);
			request.setDetailList(detailList);
			WmsPurchaseOrderResponse response = sfWebService.outsideToLscmService(WmsPurchaseOrderRequest.class, WmsPurchaseOrderResponse.class, request);
			if(response.getResult()){
				System.out.println("入库成功,调拨单号:"+transfer.getTransferId());
				transfer.setOrderPushStatus("1");
				transferService.updateTransferSf(transfer);
			}
		}
	}
		
}
