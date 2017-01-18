package com.gionee.wms.job;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.common.DateConvert;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.entity.SkuMap;
import com.gionee.wms.entity.Transfer;
import com.gionee.wms.entity.TransferGoods;
import com.gionee.wms.service.stock.SFWebService;
import com.gionee.wms.service.stock.TransferService;
import com.gionee.wms.service.wares.SkuMapService;
import com.google.common.collect.Lists;
import com.sf.integration.warehouse.request.WmsPurchaseOrderRequest;
import com.sf.integration.warehouse.request.WmsPurchaseOrderRequestHeader;
import com.sf.integration.warehouse.request.WmsPurchaseOrderRequestItem;
import com.sf.integration.warehouse.response.WmsPurchaseOrderResponse;

public class TransferJobTask {

	private static Logger logger = LoggerFactory
			.getLogger(TransferJobTask.class);
	
	@Autowired
	private TransferService transferService;
	
	@Autowired
	private SkuMapService skuMapService;
	
	@Autowired
	private SFWebService sfWebService;
	
	public void execute(){
		logger.info("*****开始推送调拨单到顺丰start*****");
		WmsPurchaseOrderRequest request = new WmsPurchaseOrderRequest();
		WmsPurchaseOrderRequestHeader header = new WmsPurchaseOrderRequestHeader();
		SkuMap skuMap = new SkuMap();
		List<Transfer> list = transferService.getTransfered();
		if(list.size() > 0){
			for (Transfer transfer : list) {
				header.setCompany(WmsConstants.SF_COMPANY);
				header.setWarehouse(WmsConstants.SF_WAREHOUSE);
				header.setErp_order_num(String.valueOf(transfer.getTransferId()));
				header.setErp_order_type("调拨入库");
				header.setTransfer_warehouse(WmsConstants.SF_WAREHOUSE);
				header.setOrder_date(DateConvert.convertD2String(transfer.getCreateTime()));
				header.setScheduled_receipt_date(DateConvert.convertD2String(new Date()));
				List<TransferGoods> goods = transferService.getTransferGoodsById(transfer.getTransferId());
				List<WmsPurchaseOrderRequestItem> detailList = Lists.newArrayList();
				for (TransferGoods good : goods) {
					WmsPurchaseOrderRequestItem item = new WmsPurchaseOrderRequestItem();
					skuMap = skuMapService.getSkuMapBySkuCode(good.getSkuCode(), "sf");
					if(skuMap == null){
						logger.info("调拨单推送失败,"+good.getSkuCode()+"不是顺丰sku");
						return;
					}
					item.setItem(skuMap.getOuterSkuCode());
					item.setTotal_qty(String.valueOf(good.getQuantity()));
					detailList.add(item);
				}
				request.setHeader(header);
				request.setDetailList(detailList);
				WmsPurchaseOrderResponse response = sfWebService.outsideToLscmService(WmsPurchaseOrderRequest.class, WmsPurchaseOrderResponse.class, request);
				if(response.getResult()){
					logger.info("返回结果:"+response.getResult()+","+response.getRemark()+",调拨单号："+response.getOrderid()+",调拨入库成功");
					transfer.setOrderPushStatus("1");
					transfer.setOrderConfirmStatus("0");
					transferService.updateTransferSf(transfer);
				}else{
					logger.error("返回结果:"+response.getResult()+","+response.getRemark()+",调拨单号："+response.getOrderid()+",调拨入库失败");
					return;
				}
			}
			logger.info("******推送调拨单到顺丰入库任务结束end******,总共"+list.size()+"条记录入库");
		}else{
			logger.info("******推送调拨单到顺丰入库任务结束end******，无需要推送的调拨单信息");
		}
	}
}
