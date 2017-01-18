package com.gionee.wms.job;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.lang3.Validate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.Shipping;
import com.gionee.wms.service.basis.ShippingService;
import com.gionee.wms.service.stock.KuaidiService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.google.common.collect.Maps;
import com.sf.integration.expressservice.bean.OrderFilterRespBean;
import com.sf.integration.expressservice.bean.OrderFilterResponse;

/**
 * 快递筛单任务
 * 
 * @author liang
 * 
 */
public class KuaidiFilterJob implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(KuaidiFilterJob.class);
	
	private final static int BATCH_ORDERS_NUM = 5;	// 第次查询订单数
	
	private final static long OFFSET_TIME = 30 * 1000; // 差距时间
	
	protected Logger filterLog = LoggerFactory.getLogger("filterLog");
	
	private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
	
	@Autowired
	private SalesOrderService orderService;
	
	@Autowired
	private KuaidiService kuaidiService;
	
	@Autowired
	private ShippingService shippingService;

	private String cronExpression;

	// 线程池关闭超时时间(20s)
	private int shutdownTimeout = 20;

	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@PostConstruct
	public void start() {
		Validate.notBlank(cronExpression);

		threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setThreadNamePrefix("KuaidiFilterJob");
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
	 * 定时推送已发货商品个体到SAP.
	 */
	@Override
	public void run() {
		logger.info("******快递筛单开始******");
		long tempTimePoint = System.currentTimeMillis();
		List<SalesOrder> orderList=null;
		try {
			Map<String, Object> criteria = Maps.newHashMap();
			criteria.put("orderStatus", WmsConstants.OrderStatus.UNFILTER.getCode());
			Page page = new Page();
			page.setStartRow(0);
			page.setEndRow(BATCH_ORDERS_NUM);
			orderList = orderService.getSalesOrderList(criteria, page);
			StringBuffer xml = new StringBuffer();
			if(null!=orderList && orderList.size()>0) {
				Shipping ems=shippingService.getShippingByCode("ems");
				List<SalesOrder> needUpdateOrders = new ArrayList<SalesOrder>();
				for (int i = 0; i < orderList.size(); i++) {
					SalesOrder order=orderList.get(i);
					StringBuffer address=new StringBuffer();
					address.append(null==order.getProvince()?"":order.getProvince()).append(null==order.getCity()?"":order.getCity()).append(null==order.getDistrict()?"":order.getDistrict());
					address.append(order.getAddress());
					xml.append("<OrderFilter filter_type=\"1\" ");
					xml.append("d_address=\"").append(address).append("\"/>");
				}
				if(logger.isDebugEnabled()) {
					logger.debug(xml.toString());
				}
				OrderFilterRespBean orderFilter = kuaidiService.sfOrderFilter(xml.toString());
				if("OK".equals(orderFilter.getHead())) {
					List<OrderFilterResponse> filterList = orderFilter.getBody().getOrderFilterResponse();
					for (int i = 0; i < filterList.size(); i++) {
						OrderFilterResponse orderFilterResponse = filterList.get(i);
						SalesOrder order = orderList.get(i);
						if(3==orderFilterResponse.getFilterResult().intValue()) {
							order.setShippingId(ems.getId());
							needUpdateOrders.add(order);
						}
						recordFilter(order, orderFilterResponse);
					}
					if(null!=needUpdateOrders && needUpdateOrders.size()>0) {
						orderService.updateShippingInfoList(needUpdateOrders);	// 批量更新快递公司
					}
					orderService.updateSalesOrderStatus(orderList, WmsConstants.OrderStatus.FILTERED.getCode(), WmsConstants.OrderStatus.UNFILTER.getCode());	// 批量更新为已筛单状态
				} else {
					logger.error("快递筛单错误: " + orderFilter.getError().getValue());
					Date nowDate = new Date();
					if(nowDate.getTime() - orderList.get(0).getJoinTime().getTime() > OFFSET_TIME) {
						orderService.updateSalesOrderStatus(orderList, WmsConstants.OrderStatus.FILTERED.getCode(), WmsConstants.OrderStatus.UNFILTER.getCode());	// 批量更新为已筛单状态
					}
				}
			}
		} catch (RuntimeException e) {
			logger.error("快递筛单时出错", e);
			Date nowDate = new Date();
			if(nowDate.getTime() - orderList.get(0).getJoinTime().getTime() > OFFSET_TIME) {
				orderService.updateSalesOrderStatus(orderList, WmsConstants.OrderStatus.FILTERED.getCode(), WmsConstants.OrderStatus.UNFILTER.getCode());	// 批量更新为已筛单状态
			}
		}
		logger.info("******快递筛单结束，耗时：" + (System.currentTimeMillis() - tempTimePoint) + "******");
	}
	
	/**
	 * 记录筛选结果日志
	 */
	private void recordFilter(SalesOrder order, OrderFilterResponse orderFilterResponse) {
		StringBuffer sb=new StringBuffer();
		sb.append(sdf.format(new Date())).append("{]").append(order.getOrderCode()).append("{]").append(order.getProvince()).append(order.getCity())
			.append(order.getDistrict()).append("{]")	.append(orderFilterResponse.getDestcode()).append("{]").append(orderFilterResponse.getRemark()==null?"":orderFilterResponse.getRemark())
			.append("{]").append(orderFilterResponse.getFilterResult());
		filterLog.info(sb.toString());
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
