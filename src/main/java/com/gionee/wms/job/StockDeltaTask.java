package com.gionee.wms.job;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.entity.StockDelta;
import com.gionee.wms.service.stock.StockService;

/**
 * 
 * 描述: 同步库存任务 作者: milton.zhang 日期: 2013-11-4
 */
public class StockDeltaTask {
	private static Logger logger = LoggerFactory.getLogger(StockDeltaTask.class);
	
	@Autowired
	private StockService stockService;
	
	/**
	 * 同步策略： 定时任务获取未推送的增量数据至商品管理，同步完成后该记录会删除
	 */
	public void execute() {
		if("2".equals(WmsConstants.WMS_COMPANY)) {
			try {
				List<StockDelta> deltaList = stockService.queryStockDelta();
				if(null!=deltaList && deltaList.size()>0) {
					logger.info("向IUNI商城库存同步服务开始...");
					stockService.syncStockDelta(deltaList);
					logger.info("向IUNI商城库存同步服务结束");
				}
			} catch (Exception e) {
				logger.error("向IUNI商城库存同步服务异常", e);
			}
		}
	}
	//
	// public static void main(String[] args) {
	//
	// }
}
