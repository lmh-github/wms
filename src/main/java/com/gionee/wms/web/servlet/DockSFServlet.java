/**
 * Project Name:wms
 * File Name:DockSFServlet.java
 * Package Name:com.gionee.wms.web.servlet
 * Date:2014年8月28日上午10:31:48
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.web.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.gionee.wms.entity.*;
import com.gionee.wms.service.log.SalesOrderLogService;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.gionee.wms.common.JaxbUtil;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.IndivStockStatus;
import com.gionee.wms.common.WmsConstants.OrderSource;
import com.gionee.wms.common.WmsConstants.OrderStatus;
import com.gionee.wms.common.WmsConstants.SkuMapOuterCodeEnum;
import com.gionee.wms.dao.IndivDao;
import com.gionee.wms.dao.SalesOrderDao;
import com.gionee.wms.dao.StockDao;
import com.gionee.wms.service.log.LogService;
import com.gionee.wms.service.stock.SalesOrderMapService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.gionee.wms.service.wares.SkuMapService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.sf.integration.warehouse.request.WmsSailOrderPushInfo;
import com.sf.integration.warehouse.request.WmsSailOrderPushInfoContainerItem;
import com.sf.integration.warehouse.request.WmsSailOrderPushInfoHeader;
import com.sf.integration.warehouse.response.DockSFResponse;

/**
 * 对接顺丰Servlet
 * @author PengBin 00001550<br>
 * @date 2014年8月28日 上午10:31:48
 */
public class DockSFServlet extends HttpServlet {

	/** serialVersionUID */
	private static final long serialVersionUID = -9070068505927381677L;

	private final Logger logger = LoggerFactory.getLogger(DockSFServlet.class);




	@SuppressWarnings("serial")
	private static final Map<String, Integer> STATUS_MAP = new HashMap<String, Integer>() {
		{
			put("10001", OrderStatus.FILTERED.getCode()); // 生效 ==> 已筛单
			put("10007", OrderStatus.FILTERED.getCode()); // 待确认 ==> 已筛单
			put("10008", OrderStatus.FILTERED.getCode()); // 已确认 ==> 已筛单
			put("10003", OrderStatus.FILTERED.getCode()); // 已下发 ==> 已筛单
			put("300", OrderStatus.PRINTED.getCode()); // 正在检货 ==> 已打单
			put("400", OrderStatus.PRINTED.getCode()); // 拣货完成 ==> 已打单
			put("700", OrderStatus.PICKING.getCode()); // 包装完成 ==> 配货中
			put("10017", OrderStatus.SHIPPING.getCode()); // 装车完成 ==> 待出库
			put("10018", OrderStatus.SHIPPING.getCode()); // 封车完成 ==> 待出库
			put("900", OrderStatus.SHIPPED.getCode()); // 发货确认 ==> 已出库
			put("10016", OrderStatus.RECEIVED.getCode()); // 已完成 ==> 已签收
			put("10011", OrderStatus.FILTERED.getCode()); // 已冻结 ==>
			put("10012", OrderStatus.FILTERED.getCode()); // 已作废 ==>
			put("10013", OrderStatus.CANCELED.getCode()); // 已取消 ==> 已取消
		}
	};

	private static final List<Integer> STATUS_SEQUECE = new ArrayList<Integer>();
	static {
		STATUS_SEQUECE.add(OrderStatus.FILTERED.getCode()); // 生效 ==> 已筛单
		STATUS_SEQUECE.add(OrderStatus.FILTERED.getCode()); // 待确认 ==> 已筛单
		STATUS_SEQUECE.add(OrderStatus.FILTERED.getCode()); // 已确认 ==> 已筛单
		STATUS_SEQUECE.add(OrderStatus.FILTERED.getCode()); // 已下发 ==> 已筛单
		STATUS_SEQUECE.add(OrderStatus.PRINTED.getCode()); // 正在检货 ==> 已打单
		STATUS_SEQUECE.add(OrderStatus.PRINTED.getCode()); // 拣货完成 ==> 已打单
		STATUS_SEQUECE.add(OrderStatus.PICKING.getCode()); // 包装完成 ==> 配货中
		STATUS_SEQUECE.add(OrderStatus.SHIPPING.getCode()); // 装车完成 ==> 待出库
		STATUS_SEQUECE.add(OrderStatus.SHIPPING.getCode()); // 封车完成 ==> 待出库
		STATUS_SEQUECE.add(OrderStatus.SHIPPED.getCode()); // 发货确认 ==> 已出库
		STATUS_SEQUECE.add(OrderStatus.RECEIVED.getCode()); // 已完成 ==> 已签收
		STATUS_SEQUECE.add(OrderStatus.FILTERED.getCode()); // 已冻结 ==>
		STATUS_SEQUECE.add(OrderStatus.FILTERED.getCode()); // 已作废 ==>
		STATUS_SEQUECE.add(OrderStatus.CANCELED.getCode()); // 已取消 ==> 已取消
	};

	/** {@inheritDoc} */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {

			List<String> readLines = IOUtils.readLines(request.getInputStream(), "UTF-8");
			String requestBodyString = StringUtils.join(readLines, StringUtils.EMPTY);
			logger.info(MessageFormat.format("“出库单状态与明细推送接口”推送的内容为：{0}{1}", SystemUtils.LINE_SEPARATOR, requestBodyString));

			if (StringUtils.isEmpty(requestBodyString)) {
				logger.error("“出库单状态与明细推送接口”获取推送正文为空！");
				print(response, new DockSFResponse(false, "“出库单状态与明细推送接口”获取推送正文为空！"));
				return;
			}

			// 由于未知原因导致 request.getParameter("logistics_interface") 获取不到数据，故使用正则表达式获取请求内容
			String sailOrderPushInfoXML = getSailOrderPushInfoXML(requestBodyString);

			if (sailOrderPushInfoXML == null) {
				logger.error("“出库单状态与明细推送接口”获取logistics_interface 内容为空！");
				print(response, new DockSFResponse(false, "“出库单状态与明细推送接口”获取logistics_interface 内容为空！"));
				return;
			}
			logger.info(MessageFormat.format("“出库单状态与明细推送接口”推送的内容为：{0}{1}", SystemUtils.LINE_SEPARATOR, sailOrderPushInfoXML));

			WmsSailOrderPushInfo sailOrderPushInfo = JaxbUtil.unmarshToObjBinding(WmsSailOrderPushInfo.class, sailOrderPushInfoXML);
			System.out.println("=======================");
			System.out.println(sailOrderPushInfo);
			System.out.println("=======================");
			if (sailOrderPushInfo == null) {
				logger.error("“出库单状态与明细推送接口”推送内容格式错误！");
				print(response, new DockSFResponse(false, "推送的格式内容错误！"));
				return;
			}

			ApplicationContext applicationContext = getApplicationContext(request);
			SalesOrderMapService salesOrderMapService = applicationContext.getBean(SalesOrderMapService.class);
			SalesOrderDao salesOrderDao = applicationContext.getBean(SalesOrderDao.class);

			WmsSailOrderPushInfoHeader header = sailOrderPushInfo.getHeader();
			String erp_order = header.getErp_order();
			SalesOrderMap salesOrderMap = salesOrderMapService.getByErpOrderCode(erp_order);
			if (salesOrderMap == null) {
				logger.error(MessageFormat.format("“出库单状态与明细推送接口”，顺丰ERP订单号：{0}不存在！", erp_order));
				print(response, new DockSFResponse(false, MessageFormat.format("erp_order：{0}在系统中没有对应数据！", erp_order)));
				return;
			}



			SalesOrder salesOrder = salesOrderDao.queryOrderByOrderCode(salesOrderMap.getOrder_code());

			String status_code = header.getStatus_code();
			// 是否确认出库，通知外部系统
			boolean notifyFlag = false;
			if ("900".equals(status_code)) { // 已出库状态
				// 订单重复推送校验
				Map<String, String> queryImeisMap = Maps.newHashMap();
				queryImeisMap.put("order_code", salesOrderMap.getOrder_code());
				List<SalesOrderImei> imeiList = salesOrderMapService.queryImeis(queryImeisMap);
				/*if (OrderStatus.SHIPPED.getCode() == salesOrder.getOrderStatus()) {
					logger.error("“出库单状态与明细推送接口”，订单重复推送！");
					print(response, new DockSFResponse(true, "重复成功！"));
					return;
				}*/
				List<WmsSailOrderPushInfoContainerItem> containerList = sailOrderPushInfo.getContainerList();
				// if (CollectionUtils.isEmpty(containerList)) {
				// logger.error("“出库单状态与明细推送接口”推送的内容没有containerList");
				// print(response, new DockSFResponse(false, "没有containerList标签！"));
				// return;
				// }

				if (CollectionUtils.isNotEmpty(containerList) && CollectionUtils.isEmpty(imeiList)) {
					// 该部分为减库存操作
					StockDao stockDao = applicationContext.getBean(StockDao.class);
					for (WmsSailOrderPushInfoContainerItem item : containerList) {
						String sf_sku = item.getItem();
						if (StringUtils.isBlank(sf_sku)) {
							continue;
						}
						String quantity = item.getQuantity();
						String skuCode = getLocalSkuCode(applicationContext, sf_sku);

						Map<String, Object> criteria = Maps.newHashMap();
						criteria.put("warehouseCode", WmsConstants.WarehouseCodeEnum.SF_WAREHOUSE.getCode());
						criteria.put("skuCode", skuCode);
						Stock stock = stockDao.queryStock(criteria);
						stock.setSalesQuantity(stock.getSalesQuantity() - (int) NumberUtils.toDouble(quantity, 0));

						stockDao.updateStockQuantity(stock);
					}
					// End 减库存操作

					// 该部分为串号操作
					// WmsSailOrderPushInfoContainerItem item = containerList.get(0);
					List<String> serial_number_list = Lists.newArrayList();
					for (WmsSailOrderPushInfoContainerItem item : containerList) {
						if (CollectionUtils.isEmpty(item.getSerial_number())) {
							continue;
						} else {
							serial_number_list.addAll(item.getSerial_number());
						}
					}
					if (CollectionUtils.isNotEmpty(serial_number_list)) {
						List<SalesOrderImei> imeis = Lists.newArrayList();
						List<Indiv> indivs = Lists.newArrayList();
						IndivDao indivDao = applicationContext.getBean(IndivDao.class);
						for (String imei : serial_number_list) {
							imeis.add(new SalesOrderImei(salesOrderMap.getOrder_code(), erp_order, imei));
							// 个体订单标示指向此订单，退货时可根据此关联进行，退货入库状态修改
							Indiv indiv = new Indiv();
							indiv.setIndivCode(imei);
							indivs.add(indiv);
						}
						salesOrderMapService.batchAddImes(imeis);
						// 更新个体订单关联
						Map<String, Object> criteria = Maps.newHashMap();
						criteria.put("orderId", salesOrder.getId());
						criteria.put("orderCode", salesOrder.getOrderCode());
						criteria.put("indivCodes", indivs);
						criteria.put("stockStatus", IndivStockStatus.OUT_WAREHOUSE.getCode()); // 设置已出库状态
						indivDao.batchUpdateIndivsStock(criteria);
					}
					// End 串号操作
				}
				notifyFlag = true;
			} else {
				salesOrderMap.setActual_ship_date_time(header.getActual_ship_date_time());
				salesOrderMap.setCarrier(StringUtils.trimToNull(header.getCarrier()));
				salesOrderMap.setCarrier_service(StringUtils.trimToNull(header.getCarrier_service()));
				salesOrderMapService.update(salesOrderMap);
			}

			int newOrderStatus = STATUS_MAP.get(status_code);
			if (STATUS_SEQUECE.indexOf(newOrderStatus) < STATUS_SEQUECE.indexOf(salesOrder.getOrderStatus())) {
				newOrderStatus = salesOrder.getOrderStatus();
			}

			// 修改订单状态和运单号
			String waybill_no = StringUtils.trimToNull(header.getWaybill_no()); // 运单号
			Map<String, Object> params = Maps.newHashMap();
			params.put("orderStatus", newOrderStatus);
			params.put("orderCode", salesOrderMap.getOrder_code());
			params.put("shippingNo", waybill_no);
			params.put("orderId",salesOrder.getId());
			salesOrderDao.updateOrder(params);
			// 通知外部系统，修改订单状态
			if (notifyFlag) {
				SalesOrderService orderService = applicationContext.getBean(SalesOrderService.class);
				if (salesOrder.getOrderSource().equals(OrderSource.OFFICIAL_GIONEE)) {
					// 官网
					orderService.notifyOrder(Lists.newArrayList(salesOrder));
				} else {
					// 其他电商平台
					orderService.notifyTOP(Lists.newArrayList(salesOrder));
				}
			}
			print(response, new DockSFResponse(true, "成功！"));
			addOpLog(sailOrderPushInfo, applicationContext, params); // 记录操作日志
		} catch (Exception e) {
			logger.error("“出库单状态与明细推送接口”推送接口被调用出现异常！", e);
			e.printStackTrace();
			print(response, new DockSFResponse(false, "请求的内容出现异常！"));
		}

	}

	/**
	 * 顺丰SKU转成本系统中的SKU
	 * @param applicationContext applicationContext
	 * @param sf_sku sf_sku
	 * @return
	 */
	private String getLocalSkuCode(ApplicationContext applicationContext, String sf_sku) {
		SkuMapService skuMapService = applicationContext.getBean(SkuMapService.class);
		SkuMap skuMap = skuMapService.getSkuMapByOutSkuCode(sf_sku, SkuMapOuterCodeEnum.SF.getCode());
		String skuCode = skuMap.getSkuCode();
		return skuCode;
	}

	/**
	 * 记录操作日志
	 * @param sailOrderPushInfo
	 * @param applicationContext
	 * @throws Exception
	 */
	private void addOpLog(WmsSailOrderPushInfo sailOrderPushInfo, ApplicationContext applicationContext,Map<String,Object> param) throws Exception {
		LogService logService = applicationContext.getBean(LogService.class);
		Log log = new Log();
		// log.setContent(MessageFormat.format("“出库单状态与明细推送接口”，推送内容为：{0}", JSONObject.fromObject(sailOrderPushInfo).toString()));
		log.setOpName("出库单状态与明细推送接口");
		log.setOpTime(new Date());
		log.setOpUserName("WMS系统,出库单状态与明细推送接口");
		log.setType(WmsConstants.LogType.BIZ_LOG.getCode());
		// log.setContent(log.getContent().substring(0, 1999));
		log.setContent(StringUtils.EMPTY);
		logService.insertLog(log);
		System.out.println("记录操作日志");


		 SalesOrderLogService salesOrderLogService =  applicationContext.getBean(SalesOrderLogService.class);
		SalesOrderLog lg=new SalesOrderLog();
		lg.setOpTime(new Date());
		lg.setOpUser("WMS业务记录者(sf推送)");
		lg.setOrderStatus(Integer.parseInt(param.get("orderStatus") + ""));
		lg.setOrderId(Long.parseLong(param.get("orderId") + ""));
		lg.setRemark("出库单状态与明细推送接口");
		salesOrderLogService.insertSalesOrderLog(lg);



	}

	/**
	 * 返回调用端消息
	 * @param response
	 * @param msg
	 */
	private void print(HttpServletResponse response, DockSFResponse msg) {
		try {
			response.setCharacterEncoding("utf-8");
			response.setContentType("text/html; charset=utf-8");

			PrintWriter out = response.getWriter();
			out.println(JaxbUtil.safeMarshToXmlBinding(DockSFResponse.class, msg));

			out.flush();
			out.close();
		} catch (Exception e) {
			logger.error("“出库单状态与明细推送接口”向调用端返回数据时异常！", e);
			e.printStackTrace();
		}
	}

	/**
	 * 获取Spring的ApplicationContext
	 * @param request
	 * @return
	 */
	private ApplicationContext getApplicationContext(HttpServletRequest request) {
		ServletContext servletContext = request.getSession().getServletContext();
		WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

		return applicationContext;
	}

	/**
	 * 获取wmsSailOrderPushInfo内容
	 * @param requestBodyString
	 * @return
	 */
	private String getSailOrderPushInfoXML(String requestBodyString) {
		try {
			Pattern pattern = Pattern.compile("logistics_interface=([\\s\\S]*?)&");
			Matcher matcher = pattern.matcher(requestBodyString);
			if (matcher.find()) {
				String g1 = matcher.group(1);
				String xml = URLDecoder.decode(g1, "UTF-8");
				return xml;
			}
			return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static void main2(String[] args) {
		//System.out.println(STATUS_SEQUECE.indexOf(OrderStatus.PRINTED.getCode()));


		String []a ={"2015-02-01","2015-03-01","2015-04-01","2015-05-01","2015-06-01","2015-07-01","2015-08-01","2015-09-01","2015-10-01","2015-11-01","2015-12-01"};
		String []b ={"2015-02-28","2015-03-31","2015-04-30","2015-05-31","2015-06-30","2015-07-31","2015-08-31","2015-09-30","2015-10-31","2015-11-30","2015-12-31"};
		for(int i=0;i<a.length;i++){
			System.out.println("I am" + a[i] + b[i]);
			//spk.readDate(false, DateUtil.parser(a[i], "yyyy-MM-dd"), DateUtil.parser(b[i], "yyyy-MM-dd"));
			try {
				Thread.sleep(1000);//*60*60
			} catch (InterruptedException e) {
				e.printStackTrace();
				break;
			}
		}


	}

	public static void main(String[] args) {
			String url="http://qimenapi.tbsandbox.com/router/qimen/service";//沙箱环境
		String appkey="21689850";
		String secret="9116509adac664b2505c5afdbd653bfc";
		String sessionKey="6100d178488f3c3be70e3e8c813605dde2de82437e511e2720912157";

		System.out.println(WmsConstants.BAR_CODE_PATH);
	}

}
