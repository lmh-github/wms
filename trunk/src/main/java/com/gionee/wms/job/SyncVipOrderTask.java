package com.gionee.wms.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.web.client.SyncVipOrderClient;

/**
 * 
 * 描述: 唯品会订单同步任务
 * 作者: jade
 * 日期: 2014-1-2
 */
public class SyncVipOrderTask {
	private static Logger logger = LoggerFactory
			.getLogger(SyncVipOrderTask.class);

	@Autowired
    private	SyncVipOrderClient syncVipOrderClient;
	/**
	 * 每天凌晨4点同步前一天的订单(包括订单、商品、物流、发票、支付等信息)
	 */
	public void execute() {
		if (WmsConstants.VIP_FLAG) {
			try {
				logger.info("唯品会订单同步服务正在启动...");
				syncVipOrderClient.syncVipOrder();
				logger.info("唯品会订单同步服务启动成功");
			} catch (Exception e) {
				// TODO: handle exception
				logger.error("唯品会订单同步服务启动异常", e);
			}
		}
	}
}
