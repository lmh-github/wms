package com.gionee.top.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.top.service.OrderSyncService;

/**
 * 
 * 描述: 淘宝订单同步任务 
 * 作者: milton.zhang 
 * 日期: 2013-11-30
 */
public class SyncOrderTask {
	private static final Log log = LogFactory.getLog(SyncOrderTask.class);
	
	@Autowired
	private OrderSyncService orderSyncService;

	/**
	 * 同步策略：
	 * 定时轮询数据库，获取时间区间内的新增订单数据并推送至WMS
	 */
	public void execute() {
		try {
			log.info("淘宝订单同步服务开始");
			orderSyncService.syncOrder();
			log.info("淘宝订单同步服务完成");
		} catch (Exception e) {
			log.error("淘宝订单同步服务异常", e);
		}
	}

	public static void main(String[] args) {
		try {

		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
