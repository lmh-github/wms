package com.gionee.wms.job;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.commons.codec.digest.DigestUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.util.Assert;

import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.ThreadUtils.MyThreadFactory;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.BackStatus;
import com.gionee.wms.common.WmsConstants.IndivFlowType;
import com.gionee.wms.common.WmsConstants.NotifyStatus;
import com.gionee.wms.common.WmsConstants.OrderSource;
import com.gionee.wms.common.WmsConstants.OrderStatus;
import com.gionee.wms.common.WmsConstants.RemoteOrderStatus;
import com.gionee.wms.dao.IndivDao;
import com.gionee.wms.entity.Back;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.Shipping;
import com.gionee.wms.facade.dto.OrderNotifyDTO;
import com.gionee.wms.facade.dto.OrderNotifyGoodsDTO;
import com.gionee.wms.service.basis.ShippingService;
import com.gionee.wms.service.stock.BackService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.gionee.wms.web.client.OCClient;
import com.gionee.wms.web.client.OrderCenterClient;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 订单通知Job类
 * 
 * @author Kevin
 * 
 */
public class OrderNotifyJob implements Runnable {
	private static Logger logger = LoggerFactory.getLogger(OrderNotifyJob.class);
	@Autowired
	private SalesOrderService orderService;
	@Autowired
	private OrderCenterClient orderCenterClient;
	@Autowired
	private OCClient oCClient;
	@Autowired
	private ShippingService shippingService;
	@Autowired
	private BackService backService;
	
	@Autowired
	private IndivDao indivDao;
	
	JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
	// 任务首次启动延时参数
	private int initialDelay = 0;
	// 任务执行间隔时间
	private int period = 0;
	// 线程池关闭超时时间(20s)
	private int shutdownTimeout = 20;
	// 调度器服务对象
	private ScheduledExecutorService scheduledExecutorService;

	/**
	 * 系统启动时完成相关初始化工作
	 */
	@PostConstruct
	public void init() {
		if (!WmsConstants.WMS_ASSISTANT_FLAG) {
			return;
		}
		Assert.isTrue(period > 0);
		// 创建单线程调度器
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(new MyThreadFactory("OrderNotifyJob"));
		// 开始调度,上次任务执行完毕后会重新开始延时触发
		scheduledExecutorService.scheduleWithFixedDelay(this, initialDelay, period, TimeUnit.SECONDS);// 开始调度
	}

	/**
	 * 优雅关闭线程池
	 * 
	 * @Title: stop
	 * @Description: 优雅关闭线程池
	 * @return void 返回类型
	 * @throws
	 */
	@PreDestroy
	public void stop() {
		if (!WmsConstants.WMS_ASSISTANT_FLAG) {
			return;
		}
		try {
			// 正常关闭
			scheduledExecutorService.shutdownNow();
			// 判断关闭过程是否超时
			if (!scheduledExecutorService.awaitTermination(shutdownTimeout, TimeUnit.SECONDS)) {
				logger.error("线程池未能关闭");
			}
		} catch (InterruptedException ie) {
			Thread.currentThread().interrupt();
		}
	}

	/**
	 * 订单通知
	 */
	@Override
	public void run() {
		logger.info("******轮询订单通知begin******");
		long tempTimePoint = System.currentTimeMillis();
		try {
			List<SalesOrder> notifiableOrderList = orderService.getSalesOrdersNeedToBeNotified();
			for (SalesOrder order : notifiableOrderList) {
				if (org.apache.commons.lang.StringUtils.isBlank(order.getOrderSource())
						|| order.getOrderSource().equals(OrderSource.OFFICIAL_GIONEE.getCode())) {
					//金立官网
					Map<String, String> parameters = Maps.newHashMap();
					parameters.put("orderSn", order.getOrderCode());
					if (OrderStatus.BACKED.getCode() == order.getOrderStatus()) {
						// 远程通知订单中心已退货
						parameters.put("orderStatus", RemoteOrderStatus.REFUNDING.getCode());
						parameters.put(
								"validCode",
								DigestUtils.md5Hex(order.getOrderCode() + RemoteOrderStatus.REFUNDING.getCode()
										+ WmsConstants.ECSHOP_SALT));
						//查询该订单下的退货商品

						parameters.put("goods", "");
						parameters.put("remark", "");
					} else if (OrderStatus.SHIPPED.getCode() == order.getOrderStatus()) {
						// 远程通知订单中心已发货
						String shippingCode = shippingService.getShipping(order.getShippingId()).getShippingCode();
						String shippingSn = order.getShippingNo();
						parameters.put("orderStatus", RemoteOrderStatus.SENDING.getCode());
						parameters.put("shippingCode", shippingCode);
						parameters.put("shippingSn", shippingSn);
						parameters.put(
								"validCode",
								DigestUtils.md5Hex(order.getOrderCode() + RemoteOrderStatus.SENDING.getCode()
										+ shippingCode + shippingSn + WmsConstants.ECSHOP_SALT));
					} else if (OrderStatus.PRINTED.getCode() == order.getOrderStatus()) {
						// 远程通知订单中心已打单
						parameters.put("orderStatus", RemoteOrderStatus.PREPARING.getCode());
						parameters.put(
								"validCode",
								DigestUtils.md5Hex(order.getOrderCode() + RemoteOrderStatus.PREPARING.getCode()
										+ WmsConstants.ECSHOP_SALT));
					} else if (OrderStatus.RECEIVED.getCode() == order.getOrderStatus()) {
						// 远程通知订单中心已打单
						parameters.put("orderStatus", RemoteOrderStatus.SHIPPED.getCode());
						parameters.put(
								"validCode",
								DigestUtils.md5Hex(order.getOrderCode() + RemoteOrderStatus.SHIPPED.getCode()
										+ WmsConstants.ECSHOP_SALT));
					} else if (OrderStatus.REFUSED.getCode() == order.getOrderStatus()) {
						// 远程通知订单中心已打单
						parameters.put("orderStatus", RemoteOrderStatus.REFUSED.getCode());
						parameters.put(
								"validCode",
								DigestUtils.md5Hex(order.getOrderCode() + RemoteOrderStatus.REFUSED.getCode()
										+ WmsConstants.ECSHOP_SALT));
					}
					try {
						orderCenterClient.notifyOrder(order, parameters);
						order.setOrderNotifyStatus(NotifyStatus.NOTIFIED_SUCCESS.getCode());
					} catch (Exception e) {
						order.setOrderNotifyStatus(NotifyStatus.NOTIFIED_FAIL.getCode());
					}
					// 更新本次通知结果
					order.setOrderNotifyTime(new Date());
					order.setOrderNotifyCount(order.getOrderNotifyCount() + 1);
					try {
						orderService.updateSalesOrder(order);
					} catch (DataAccessException ex) {
						// 屏蔽异常
						logger.error("通知订单时出错-OFFICIAL_GIONEE", ex);
					}
				}else if(order.getOrderSource().equals(OrderSource.OFFICIAL_IUNI.getCode())){
					//IUNI官网
					String jsonReq = "";
					OrderNotifyDTO orderDTO = new OrderNotifyDTO();
					orderDTO.setOrderSn(order.getOrderCode());
					if (OrderStatus.BACKED.getCode() == order.getOrderStatus()) {
						// 远程通知订单中心已退货
						//根据订单号查询退货单及退货商品
						Map<String, Object> criteria = Maps.newHashMap();
						criteria.put("orderCode", order.getOrderCode());
						criteria.put("backStatus", BackStatus.BACKED.getCode());
						Back back = backService.getBack(criteria);
						if(back == null){
							continue;
						}
						orderDTO.setDelverySn(back.getBackCode());
						orderDTO.setOrderSn(back.getOrderCode());
						orderDTO.setOrderStatus(RemoteOrderStatus.REFUNDING.getCode());
						orderDTO.setRemark(back.getRemarkBacked());
						orderDTO.setShippingCode(back.getShippingCode());
						orderDTO.setShippingSn(back.getShippingNo());
						//从商品流转信息中查询退货商品和数量
						Map<String, Integer> goodsMap = Maps.newHashMap();
						criteria.clear();
						criteria.put("flowId", back.getId());
						criteria.put("flowType", IndivFlowType.IN_RMA.getCode());
						List<IndivFlow> indivList = indivDao.queryIndivFlowList(criteria);
						for(IndivFlow indiv : indivList){
							String skuCode = indiv.getSkuCode();
							if(goodsMap.containsKey(skuCode)){
								goodsMap.put(skuCode, goodsMap.get(indiv.getSkuCode())+1);
							}else{
								goodsMap.put(skuCode, 1);
							}
						}
						
						List<OrderNotifyGoodsDTO> goods = Lists.newArrayList();
						for (Map.Entry<String, Integer> entry : goodsMap.entrySet()) {
							OrderNotifyGoodsDTO good = new OrderNotifyGoodsDTO();
							good.setQuantity(Integer.valueOf(entry.getValue().toString()));
							good.setSkuCode(entry.getKey().toString());
							goods.add(good);
						}
						orderDTO.setGoods(goods);
					} else if (OrderStatus.SHIPPED.getCode() == order.getOrderStatus()) {
						// 远程通知订单中心已发货
						Shipping shipping = shippingService.getShipping(order.getShippingId());
						String shippingSn = order.getShippingNo();
						orderDTO.setOrderStatus(RemoteOrderStatus.SENDING.getCode());
						orderDTO.setShippingCode(shipping.getShippingCode());
						orderDTO.setShippingSn(shippingSn);
						orderDTO.setShippingName(shipping.getShippingName());
					} else if (OrderStatus.PRINTED.getCode() == order.getOrderStatus()) {
						// 远程通知订单中心已打单
						orderDTO.setOrderStatus(RemoteOrderStatus.PREPARING.getCode());
					} else if (OrderStatus.RECEIVED.getCode() == order.getOrderStatus()) {
						// 远程通知订单中心已签收
						orderDTO.setOrderStatus(RemoteOrderStatus.SHIPPED.getCode());
					} else if (OrderStatus.REFUSEING.getCode() == order
							.getOrderStatus()) {
						// 远程通知订单中心拒收中
						orderDTO.setOrderStatus(RemoteOrderStatus.REJECTING
								.getCode());
					} else if (OrderStatus.REFUSED.getCode() == order.getOrderStatus()) {
						// 远程通知订单中心已拒收
						orderDTO.setOrderStatus(RemoteOrderStatus.REFUSED.getCode());
					}
					try {
						jsonReq = jsonUtils.toJson(orderDTO);
						oCClient.notifyOrder(jsonReq);
						order.setOrderNotifyStatus(NotifyStatus.NOTIFIED_SUCCESS.getCode());
					} catch (Exception e) {
						order.setOrderNotifyStatus(NotifyStatus.NOTIFIED_FAIL.getCode());
					}
					// 更新本次通知结果
					order.setOrderNotifyTime(new Date());
					order.setOrderNotifyCount(order.getOrderNotifyCount() + 1);
					try {
						orderService.updateSalesOrder(order);
					} catch (DataAccessException ex) {
						// 屏蔽异常
						logger.error("通知订单时出错-OFFICIAL_IUNI", ex);
					}
				}
			}
		} catch (RuntimeException e) {
			logger.error("轮询订单通知时出错", e);
		}
		logger.info("******轮询订单通知end，耗时：" + (System.currentTimeMillis() - tempTimePoint) + "******");
	}

	public void setInitialDelay(int initialDelay) {
		this.initialDelay = initialDelay;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

}
