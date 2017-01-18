package com.sf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.entity.SalesOrderImei;
import com.gionee.wms.entity.SkuMap;
import com.gionee.wms.job.TransferJobTask;
import com.gionee.wms.service.stock.SFWebService;
import com.gionee.wms.service.stock.SalesOrderMapService;
import com.gionee.wms.service.stock.TransferService;
import com.gionee.wms.service.wares.SkuMapService;
import com.google.common.collect.Maps;
import com.sf.integration.warehouse.request.WmsPurchaseOrderQueryRequest;
import com.sf.integration.warehouse.response.WmsPurchaseOrderQueryResponse;
import com.sf.integration.warehouse.response.WmsPurchaseOrderQueryResponseHeader;
import com.sf.integration.warehouse.response.WmsPurchaseOrderQueryResponseItem;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:config/spring/spring*.xml" })
public class TransferStatusTest {
	private static Logger logger = LoggerFactory
			.getLogger(TransferStatusTest.class);
	@Autowired
	private SFWebService sfWebService;
	
	@Autowired
	private TransferService transferService;
	
	@Autowired
	private SalesOrderMapService salesOrderMapService;
	
	@Autowired
	private SkuMapService skuMapService;
	
	@Test
	public void jobExecute(){
		WmsPurchaseOrderQueryRequest request = new WmsPurchaseOrderQueryRequest();
		request.setCheckword(WmsConstants.SF_CHECKWORD);
		request.setCompany(WmsConstants.SF_COMPANY);
		String transferId = "1714082718457";
		request.setOrderid(transferId);
		request.setWarehouse(WmsConstants.SF_WAREHOUSE);
		
		WmsPurchaseOrderQueryResponse response = sfWebService.outsideToLscmService(WmsPurchaseOrderQueryRequest.class, WmsPurchaseOrderQueryResponse.class, request);
		if(response.getResult()){
			WmsPurchaseOrderQueryResponseHeader header = response.getHeader();
			
			String sf_erp_order = header.getErp_order_num();
			
			List<WmsPurchaseOrderQueryResponseItem> detailList = response.getDetailList();
			Map<String, Object> params = Maps.newHashMap();
			List<SalesOrderImei> salesOrderImeis = new ArrayList<SalesOrderImei>();
			SkuMap skuMap = new SkuMap();
			for (WmsPurchaseOrderQueryResponseItem wmsPurchaseOrderQueryResponseItem : detailList) {
				params.put("transferId", transferId);
				skuMap = skuMapService.getSkuMapByOutSkuCode(wmsPurchaseOrderQueryResponseItem.getSku_no(), "sf");
				if(skuMap == null){
					logger.info("调拨单入库状态查询失败,"+wmsPurchaseOrderQueryResponseItem.getSku_no()+"不是顺丰sku");
					return;
				}
				params.put("skuCode", skuMap.getSkuCode());
				params.put("qty", wmsPurchaseOrderQueryResponseItem.getQty());
				transferService.updateTransferGoods(params);
				List<String> seriNum = wmsPurchaseOrderQueryResponseItem.getSerial_number();
				if(seriNum.size() > 0){
					for (String imei : seriNum) {
						SalesOrderImei sImei = new SalesOrderImei();
						sImei.setImei(imei);
						sImei.setOrder_code(transferId);
						sImei.setSf_erp_order(sf_erp_order);
						salesOrderImeis.add(sImei);
					}
				}
				if(salesOrderImeis.size() > 0){
					salesOrderMapService.batchAddImes(salesOrderImeis);
				}
			}
			
		}
	}
	
}
