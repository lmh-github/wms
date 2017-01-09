package com.gionee.top.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.top.service.OrderSyncService;

/**
 * 
 * 作者:milton.zhang
 * 时间:2014-1-7
 * 描述:淘宝分销订单同步任务 
 */
public class SyncFxOrderTask {
	private static final Log log = LogFactory.getLog(SyncFxOrderTask.class);
	
	@Autowired
	private OrderSyncService orderSyncService;

	/**
	 * 同步策略：
	 * 定时轮询数据库，获取时间区间内的新增订单数据并推送至WMS
	 */
	public void execute() {
		try {
			log.info("淘宝分销订单同步服务开始");
			orderSyncService.syncFxOrder();
			log.info("淘宝分销订单同步服务完成");
		} catch (Exception e) {
			log.error("淘宝分销订单同步服务异常", e);
		}
	}

}
