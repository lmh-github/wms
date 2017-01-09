/**
 * Project Name:wms
 * File Name:OrderPushSFJob.java
 * Package Name:com.gionee.wms.job
 * Date:2014年8月19日上午10:53:29
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.job;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.BooleanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.OrderPushStatusEnum;
import com.gionee.wms.common.WmsConstants.OrderStatus;
import com.gionee.wms.common.WmsConstants.ShippingEnum;
import com.gionee.wms.dao.SalesOrderDao;
import com.gionee.wms.dao.ShippingDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.Shipping;
import com.gionee.wms.entity.SystemConfig;
import com.gionee.wms.service.stock.SFWebService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.gionee.wms.service.stock.SystemConfigService;
import com.google.common.collect.Maps;

/**
 * 订单(订单状态：已筛单)定时推送到顺丰任务
 * @author PengBin 00001550<br>
 * @date 2014年8月19日 上午10:53:29
 */
public class OrderPushSFJob {

	private final Logger logger = LoggerFactory.getLogger(OrderPushSFJob.class);

	@Autowired
	private SalesOrderDao orderDao;

	@Autowired
	private SystemConfigService configService; // 系统配置

	@Autowired
	private ShippingDao shippingDao; // 物流公司

	@Autowired
	private SalesOrderService salesOrderService;

	@Autowired
	private SFWebService sfWebService; // 顺丰WebService

	/**
	 * 定时任务执行方法
	 */
	public void execute() {
		if (!whetherOpenAutoPush()) {
			logger.debug("没有开启订单自动推送！");
			return;
		}
		List<SalesOrder> orderList = queryAllSaiDanOrder();
		if (CollectionUtils.isEmpty(orderList)) {
			logger.debug("没有要推送到顺丰的订单！");
			return;
		}

		for (SalesOrder order : orderList) {
			salesOrderService.pushOrderToSF(order);
		}
	}

	/**
	 * 查询所有的销售订单(订单状态：已筛单，物流公司为：顺丰)
	 * @return List<SalesOrder>
	 */
	private List<SalesOrder> queryAllSaiDanOrder() {
		Page page = new Page();
		page.setStartRow(0);
		page.setEndRow(Integer.MAX_VALUE);

		Long sfid = getSFID();
		if (sfid == null) {
			logger.error("没有顺丰快递公司！");
			return null;
		}

		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("page", page);
		criteria.put("orderStatus", OrderStatus.FILTERED.getCode());
		criteria.put("orderPushStatus", OrderPushStatusEnum.UN_PUSHED.getCode());
		criteria.put("include_orderPushStatus_null", true); // 包含"null"值
		criteria.put("shippingId", sfid); // 顺丰的shippingId

		return orderDao.queryOrderByPage(criteria);
	}

	/**
	 * 查询顺丰物流公司的ID，用于条件过滤
	 * @return Long
	 */
	private Long getSFID() {

		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("shippingCode", ShippingEnum.SF.getCode());
		List<Shipping> shippingList = shippingDao.queryShippingList(criteria);

		if (CollectionUtils.isNotEmpty(shippingList)) {
			return shippingList.get(0).getId();
		}
		return null;
	}

	/**
	 * 是否开启自动推送
	 * @return
	 */
	private boolean whetherOpenAutoPush() {
		SystemConfig config = configService.getByKey(WmsConstants.ORDER_AUTO_PUSH_SF);
		return config != null && BooleanUtils.toBoolean(config.getValue());
	}

	public static void main(String[] args) {
		OrderPushSFJob tsa=new OrderPushSFJob();
		tsa.execute();

	}

}
