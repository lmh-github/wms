package com.gionee.wms.job;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.entity.SalesOrderImei;
import com.gionee.wms.entity.SalesOrderMap;
import com.gionee.wms.entity.SkuMap;
import com.gionee.wms.entity.Transfer;
import com.gionee.wms.service.stock.SFWebService;
import com.gionee.wms.service.stock.SalesOrderMapService;
import com.gionee.wms.service.stock.TransferService;
import com.gionee.wms.service.wares.SkuMapService;
import com.google.common.collect.Maps;
import com.sf.integration.warehouse.request.WmsPurchaseOrderQueryRequest;
import com.sf.integration.warehouse.response.WmsPurchaseOrderQueryResponse;
import com.sf.integration.warehouse.response.WmsPurchaseOrderQueryResponseHeader;
import com.sf.integration.warehouse.response.WmsPurchaseOrderQueryResponseItem;

public class TransferStatusTask {

	private static Logger logger = LoggerFactory
			.getLogger(TransferStatusTask.class);
	
	@Autowired
	private TransferService transferService;
	
	@Autowired
	private SkuMapService skuMapService;
	
	@Autowired
	private SFWebService sfWebService;
	
	@Autowired
	private SalesOrderMapService salesOrderMapService;
	
	public void execute(){
		//调用入库单状态查询接口
		logger.info("&&&&调用调拨单入库状态查询接口start***");
		WmsPurchaseOrderQueryRequest request = new WmsPurchaseOrderQueryRequest();
		request.setCheckword(WmsConstants.SF_CHECKWORD);
		request.setCompany(WmsConstants.SF_COMPANY);
		List<Transfer> list = transferService.getTransferPush();
		for (Transfer transfer : list) {
			request.setOrderid(String.valueOf(transfer.getTransferId()));
			request.setWarehouse(WmsConstants.SF_WAREHOUSE);
			
			WmsPurchaseOrderQueryResponse response = sfWebService.outsideToLscmService(WmsPurchaseOrderQueryRequest.class, WmsPurchaseOrderQueryResponse.class, request);
			if(response.getResult()){
				WmsPurchaseOrderQueryResponseHeader header = response.getHeader();
				
				String sf_erp_order = header.getErp_order_num();
				
				List<WmsPurchaseOrderQueryResponseItem> detailListQuery = response.getDetailList();
				Map<String, Object> params = Maps.newHashMap();
				List<SalesOrderImei> salesOrderImeis = new ArrayList<SalesOrderImei>();
				SkuMap skuMap = new SkuMap();
				
				for (WmsPurchaseOrderQueryResponseItem wmsPurchaseOrderQueryResponseItem : detailListQuery) {
					params.put("transferId", transfer.getTransferId());
					skuMap = skuMapService.getSkuMapByOutSkuCode(wmsPurchaseOrderQueryResponseItem.getSku_no(), "sf");
					if(skuMap == null){
						logger.info("调拨单入库状态查询失败,"+wmsPurchaseOrderQueryResponseItem.getSku_no()+"不是顺丰sku");
						return;
					}
					params.put("skuCode", skuMap.getSkuCode());
					params.put("qty", wmsPurchaseOrderQueryResponseItem.getQty());
					transferService.updateTransferGoods(params);
					List<String> seriNum = wmsPurchaseOrderQueryResponseItem.getSerial_number();
					if(seriNum!=null){
						for (String imei : seriNum) {
							SalesOrderImei sImei = new SalesOrderImei();
							sImei.setImei(imei);
							sImei.setOrder_code(String.valueOf(transfer.getTransferId()));
							sImei.setSf_erp_order(sf_erp_order);
							salesOrderImeis.add(sImei);
						}
					}
					if(salesOrderImeis.size() > 0){
						salesOrderMapService.batchAddImes(salesOrderImeis);
					}
				}
				if(detailListQuery.size() > 0){
					transfer.setOrderConfirmStatus("1");
					transferService.updateTransferSf(transfer);
				}
			}else{
				logger.info("&&&&查询调拨单入库状态失败&&&&&，"+response.getRemark());
			}
			logger.info("**********查询调拨单入库状态成功*********");
		}
		logger.info("&&&&调用调拨单入库状态查询接口end***");
	}
}
