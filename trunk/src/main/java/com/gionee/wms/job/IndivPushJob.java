package com.gionee.wms.job;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;

import com.gionee.dc.web.service.AuthInfo;
import com.gionee.dc.web.service.ImeidtlSale;
import com.gionee.dc.web.service.SaleInvoice;
import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.RemoteCallStatus;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.service.wares.IndivService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 使用Spring的ThreadPoolTaskScheduler定时调度商品个体推送服务
 * 
 */
public class IndivPushJob implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(IndivPushJob.class);
	private static final int SUCCESS = 0;
	private static final int DUPLICATE_PUSH = -2;

	private String cronExpression;

	// 线程池关闭超时时间(20s)
	private int shutdownTimeout = 20;

	private ThreadPoolTaskScheduler threadPoolTaskScheduler;

	@Autowired
	private IndivService indivService;

	@Autowired
	private SaleInvoice sapServiceClient;
	
	@PostConstruct
	public void start() {
		Validate.notBlank(cronExpression);

		threadPoolTaskScheduler = new ThreadPoolTaskScheduler();
		threadPoolTaskScheduler.setThreadNamePrefix("IndivPushJob");
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
		if (WmsConstants.INDIV_PUSH_FLAG) {
			logger.info("******轮询推送发货商品个体begin******");
			long tempTimePoint = System.currentTimeMillis();
			AuthInfo authInfo = new AuthInfo();
			authInfo.setUserName(WmsConstants.WMS_SAP_WS_USERNAME);
			authInfo.setPassword(WmsConstants.WMS_SAP_WS_PASSWORD);
			
			try {
				List<ImeidtlSale> imeiList = Lists.newArrayList();
				Map<String, Object> criteria = Maps.newHashMap();
				int totalRow = indivService.getUnpushedDeliveredIndivTotal(criteria);
				if (totalRow > 0) {
					// 分页推送数据到SAP
					List<Indiv> allIndivs = Lists.newArrayList();
					int pageSize = 100;
					Page page = new Page();
					page.setTotalRow(totalRow);
					page.setPageSize(pageSize);
					page.calculate();
					int totalPage = page.getTotalPage();
					logger.info("商品个体总数为{}, 分{}批推送", totalRow, totalPage);
					for (int i = 1; i <= totalPage; i++) {
						page = Page.getPage(i, totalRow, pageSize);
						criteria.put("page", page);
						List<Indiv> indivs = indivService.getUnpushedDeliveredIndivList(criteria, page);
						// 组装数据
						for (Indiv indiv : indivs) {
							ImeidtlSale imei = new ImeidtlSale();
							imei.setAddition(indiv.getOrder().getTel());
							imei.setColor(null);
							imei.setConsignee(indiv.getOrder().getConsignee());
							imei.setConsigneeTel(indiv.getOrder().getMobile());
							imei.setCreateDate(null);
							imei.setDeliverAddress(indiv.getOrder().getFullAddress());
							imei.setId(null);
							imei.setImei(indiv.getIndivCode());
							imei.setMatlid(indiv.getMaterialCode());
							imei.setMatlname(null);
							imei.setMeid(null);
							imei.setModel(null);
							imei.setSource(null);
							GregorianCalendar gc = new GregorianCalendar();
							gc.setTime(indiv.getOutTime());
							XMLGregorianCalendar xmlGc = DatatypeFactory.newInstance().newXMLGregorianCalendar(gc);
							imei.setSourceDate(xmlGc);
							imeiList.add(imei);
						}
						
						// 调用SAP WebService进行数据推送
						long tempTimePoint2 = System.currentTimeMillis();
						try {
							logger.info("第{}批推送开始......", i);
							//返回值  0：用户名密码错误    2：数据传输成功   3：参数为空   500：异常
							String result = sapServiceClient.addSaleInvoice(authInfo, imeiList);
							logger.info("第{}批推送结果为:{}，耗时{}", new Object[] { i, result,
									(System.currentTimeMillis() - tempTimePoint2) });
							if(StringUtils.isNotEmpty(result)){
								JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
								Map<String, Object> resultMap = jsonUtils.fromJson(result,	Map.class);
								String resultCode = (String) resultMap.get("result");
								// 处理推送结果
								if (resultCode.equals("2")) {
									for (Indiv indiv : indivs) {
										indiv.setPushStatus(RemoteCallStatus.SUCCESS.getCode());
										indiv.setPushTime(new Date());
										indiv.setPushCount(indiv.getPushCount() + 1);
									}
								} else {
									for (Indiv indiv : indivs) {
										indiv.setPushStatus(RemoteCallStatus.FAIL.getCode());
										indiv.setPushTime(new Date());
										indiv.setPushCount(indiv.getPushCount() + 1);
									}
								}
							}else{
								for (Indiv indiv : indivs) {
									indiv.setPushStatus(RemoteCallStatus.FAIL.getCode());
									indiv.setPushTime(new Date());
									indiv.setPushCount(indiv.getPushCount() + 1);
								}
							}
						} catch (RuntimeException e) {
							logger.error("个体推送异常" + e.getMessage());
							for (Indiv indiv : indivs) {
								indiv.setPushStatus(RemoteCallStatus.FAIL.getCode());
								indiv.setPushTime(new Date());
								indiv.setPushCount(indiv.getPushCount() + 1);
							}
						}
						allIndivs.addAll(indivs);
					}

					// 分页更新商品个体推送结果
					for (int i = 1; i <= totalPage; i++) {
						page = Page.getPage(i, totalRow, pageSize);
						indivService.updateIndivList(allIndivs.subList(page.getStartRow() - 1, page.getEndRow()));
					}
				}
			} catch (Exception e) {
				logger.error("轮询推送发货商品个体时出错", e);
			}

			logger.info("******轮询推送发货商品个体end，总耗时：" + (System.currentTimeMillis() - tempTimePoint) + "******");
		}
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

	public static void main(String[] args) {
		
	}
}
