package com.gionee.wms.job;

import java.util.Date;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import com.gionee.wms.entity.ShippingInfo;
import com.gionee.wms.kuaidi.pojo.TaskResponse;
import com.gionee.wms.service.basis.ShippingInfoService;
import com.gionee.wms.web.client.KuaidiClient;

/**
 * 快递订阅定时任务，轮询未进行订阅的快递信息
 * 
 * @author liang
 * 
 */
public class KuaidiSubscribeJob implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(KuaidiSubscribeJob.class);
	
	@Autowired
	private ShippingInfoService shippingInfoService;
	
	@Autowired
	private KuaidiClient kuaidiClient;

	private String cronExpression;

	// 线程池关闭超时时间(20s)
	private int shutdownTimeout = 20;

	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@PostConstruct
	public void start() {
		Validate.notBlank(cronExpression);

		threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setThreadNamePrefix("IndivPushJob__");
		threadPoolTaskScheduler.initialize();

		threadPoolTaskScheduler.schedule(this, new CronTrigger(cronExpression));
	}

	@PreDestroy
	public void stop() {
		ScheduledExecutorService scheduledExecutorService = threadPoolTaskScheduler.getScheduledExecutor();
		try {
			scheduledExecutorService.shutdownNow();
			if (!scheduledExecutorService.awaitTermination(shutdownTimeout, TimeUnit.SECONDS)) {
				System.err.println("ScheduledExecutorService did not terminated");
			}
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * 定时对快递100进行物流推送订阅.
	 */
	@Override
	public void run() {
		logger.info("******轮询快递订阅begin******");
		long tempTimePoint = System.currentTimeMillis();
		try {
			List<ShippingInfo> notifiableInfoList = shippingInfoService.getShippingInfoNeedToSub();
			for (ShippingInfo shippingInfo : notifiableInfoList) {
				try {
					TaskResponse resp = kuaidiClient.subscribeKuaidi(shippingInfo);
					if(null!=resp && resp.getResult()) {
						// 订阅成功，记录订阅返回码
						shippingInfo.setSubscribeResult(resp.getReturnCode());
					} else {
						// 订阅失败
						if(null!=resp && null!=resp.getReturnCode()) {
							shippingInfo.setSubscribeResult(resp.getReturnCode());
						}
					}
				} catch(Exception e) {
					// 运行错误,订阅失败
					logger.error("快递订阅失败", e.getMessage());
				}
				// 更新本次订阅结果
				shippingInfo.setSubscribeTime(new Date());
				Integer subCount = shippingInfo.getSubscribeCount();
				if(null==subCount) {
					subCount = new Integer(0);
				}
				shippingInfo.setSubscribeCount(subCount + 1);
				try {
					shippingInfoService.updateShippingInfo(shippingInfo);
				} catch (DataAccessException ex) {
					// 屏蔽异常
					logger.error("通知订单已退货时出错", ex);
				}
			}
		} catch (RuntimeException e) {
			logger.error("轮询订单通知(已发货或已退货)时出错", e);
		}
		logger.info("******轮询快递订阅end，耗时：" + (System.currentTimeMillis() - tempTimePoint) + "******");
	}


	public static void main(String[] args) {
		KuaidiSubscribeJob tsa=new KuaidiSubscribeJob();
		tsa.start();
		tsa.run() ;

	}
	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	/**
	 * 设置normalShutdown的等待时间,单位秒.
	 */
	public void setShutdownTimeout(int shutdownTimeout) {
		this.shutdownTimeout = shutdownTimeout;
	}
}
