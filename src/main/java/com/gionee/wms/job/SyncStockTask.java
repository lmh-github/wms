package com.gionee.wms.job;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.common.WmsConstants.WarehouseCodeEnum;
import com.gionee.wms.entity.Stock;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.StockService;
import com.gionee.wms.web.client.OrderCenterClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 
 * 描述: 同步库存任务
 * 作者: milton.zhang
 * 日期: 2013-11-4
 */
public class SyncStockTask {
	private static Logger logger = LoggerFactory.getLogger(SyncStockTask.class);

	@Autowired
	private StockService stockService;
	@Autowired
	private OrderCenterClient orderCenterClient;
	@Autowired
	private WarehouseService warehouseService;

	/**
	 * 同步策略：
	 * 定时任务获取未推送的库存推送至商城
	 * 推送成功后修改库存同步状态为已推送
	 */
	public void execute() {
		try {
			logger.info("向金立商城库存同步服务开始...");
			// Warehouse gioneeHouse =
			// warehouseService.getWarehouseByCode(WmsConstants.OFFICIAL_GIONEE_HOUSE_CODE);
			Map<String, Object> paramMap1 = Maps.newHashMap();
			paramMap1.put("warehouseCode_List", Lists.newArrayList(WarehouseCodeEnum.DONG_GUAN_WAREHOUSE.getCode(), WarehouseCodeEnum.SF_WAREHOUSE.getCode()));
			paramMap1.put("syncStatus", 0);
			List<Stock> stockList1 = stockService.queryStockList(paramMap1);
			orderCenterClient.syncStock(stockList1, null);
			logger.info("向金立商城库存同步服务结束");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("向金立商城库存同步服务异常", e);
		}
	}

	public static void main(String[] args) {

	}
}
