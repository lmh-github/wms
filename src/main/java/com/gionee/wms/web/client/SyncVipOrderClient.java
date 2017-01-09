/*
 * @(#)VipOrderSyncClient.java 2013-12-31
 *
 * Copyright 2013 Shenzhen Gionee,Inc. All rights reserved.
 */
package com.gionee.wms.web.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gionee.wms.common.DateConvert;
import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.MD5Util;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.InvoiceType;
import com.gionee.wms.common.WmsConstants.PaymentType;
import com.gionee.wms.dto.SendRequest;
import com.gionee.wms.entity.Log;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.facade.OrderManager;
import com.gionee.wms.facade.dto.OrderGoodsDTO;
import com.gionee.wms.facade.dto.OrderInfoDTO;
import com.gionee.wms.facade.result.CommonResult;
import com.gionee.wms.facade.result.CommonResult.ErrCodeEnum;
import com.gionee.wms.service.log.LogService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 唯品会订单同步
 * 
 * @author ZuoChangjun 2013-12-31
 */
@Component("syncVipOrderClient")
public class SyncVipOrderClient {
	private static Logger logger = LoggerFactory
			.getLogger(SyncVipOrderClient.class);
	private JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
	@Autowired
	private OrderManager orderManager;
	@Autowired
	private SalesOrderService orderService;
	@Autowired
	private LogService logService;
	public void syncVipOrder() throws IOException {
		HttpUtil httpUtil = new HttpUtil();
		httpUtil.init();
		Map<String, Object> paramsMap = new HashMap<String, Object>();
		String order_call_time = System.currentTimeMillis() + "";
		String order_token = MD5Util.encode(WmsConstants.VIP_SCM_SOURCE
				+ WmsConstants.VIP_SCM_ORDER_LIST_API_NAME + order_call_time);
		paramsMap.put("token", order_token);
		paramsMap.put("call_time", order_call_time);
		paramsMap.put("sid", WmsConstants.VIP_SCM_SID);
		paramsMap.put("o", "json");
		//Calendar cal = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		//cal.add(Calendar.DATE, -1); // 得到前一天
		//String yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(cal.getTime());
		//paramsMap.put("st_add_time", yestedayDate+" 00:00:00");
		//paramsMap.put("et_add_time", yestedayDate+" 23:59:59");
		//paramsMap.put("st_add_time", "2013-12-26 14:14:00");
		//paramsMap.put("et_add_time", "2013-12-26 14:20:00");
		//paramsMap.put("st_add_time", "2013-01-01 00:00:00");
		//paramsMap.put("et_add_time", "2014-03-01 23:59:59");
		Map<String, Object> paramsMap2 = new HashMap<String, Object>();
		paramsMap2.put("orderSource", WmsConstants.OrderSource.VIP_GIONEE.getCode());
		//String st_add_time = orderService.queryLastSyncOrderTime(paramsMap2);
		//第一次同步,如果为空，则初始化值
		//if(StringUtils.isBlank(st_add_time)){
			//st_add_time = "2014-02-10 00:00:00";
		//}
		//String et_add_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
		Date nowDate = new Date();
		String st_add_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(nowDate.getTime()-12*60*60*1000));//6小时前
		String et_add_time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(nowDate);
		paramsMap.put("st_add_time", st_add_time);
		paramsMap.put("et_add_time", et_add_time);
		
		log(null,"同步唯品会订单开始，同步时间:st_add_time="+st_add_time+",et_add_time="+et_add_time);//记录日志
		
		//获取订单信息
		int status = 0;
		JSONArray orderList = null;
		//订单总数
		int orderTotalCount = 0;
		int pageSize=50;//最大为100
		int totalPageNum=0;
		try {
			//查询订单总数
			paramsMap.put("p", 1);//页号
			paramsMap.put("l", 1);//页大小
			paramsMap.put("stat", 10);//只查询已审核状态订单
			String totalOrderResult = httpUtil.doPost2(WmsConstants.VIP_SCM_URL + WmsConstants.VIP_SCM_ORDER_LIST, paramsMap);
			//System.out.println("订单列表(总数)接口参数="+jsonUtils.toJson(paramsMap));
			//System.out.println("订单列表(总数)接口结果="+totalOrderResult);

			JSONObject orderObj = JSONObject.fromObject(totalOrderResult);
			status = Integer.valueOf(orderObj.getString("status"));
			if (status != 1) {
				logger.error("获取唯品会订单总数发生错误："+orderObj.toString());
				return;
			}
			JSONObject data = orderObj.getJSONObject("data");
			orderList = data.getJSONArray("list");
			orderTotalCount = Integer.valueOf(data.getString("total"));
		}
		catch(Exception e){
			logger.error("获取唯品会订单列表发生错误："+e.getMessage());
			return;
		}
		if(orderTotalCount == 0){
			log(null,"同步唯品会订单结束，暂无唯品会订单。同步时间:st_add_time="+st_add_time+",et_add_time="+et_add_time);//记录日志
			logger.info("同步唯品会订单结束，暂无唯品会订单。同步时间:st_add_time="+st_add_time+",et_add_time="+et_add_time);
			return;
		}
		totalPageNum = orderTotalCount % pageSize == 0 ? orderTotalCount/ pageSize : orderTotalCount / pageSize + 1;
		//同步成功数
		int syncSuccCount = 0;
		//同步失败数
		int syncErrorCount = 0;
		for (int i = 1; i <= totalPageNum; i++) {
			paramsMap.put("p", i);
			paramsMap.put("l", pageSize);
			paramsMap.put("token", order_token);
			paramsMap.put("call_time", order_call_time);
			paramsMap.remove("order_sn");
			paramsMap.put("stat", 10);//只查询已审核状态订单
			//分页查询订单数据
			String orderResult = httpUtil.doPost2(WmsConstants.VIP_SCM_URL + WmsConstants.VIP_SCM_ORDER_LIST, paramsMap);
			//System.out.println("订单列表接口参数="+jsonUtils.toJson(paramsMap));
			//System.out.println("订单列表接口结果="+orderResult);

			JSONObject orderObj = JSONObject.fromObject(orderResult);
			status = Integer.valueOf(orderObj.getString("status"));
			if (status != 1) {
				logger.error("获取唯品会订单列表发生错误："+orderObj.toString()+",页号："+i);
				return;
			}
			JSONObject data = orderObj.getJSONObject("data");
			orderList = data.getJSONArray("list");
			List<OrderInfoDTO> orderInfoList = new ArrayList<OrderInfoDTO>();
			String order_sns = "";
			for (int j = 0; j < orderList.size(); j++) {
				JSONObject jsonObject = orderList.getJSONObject(j);
				if(StringUtils.isNotBlank(jsonObject.getString("order_sn"))){
					if(j==0){
						order_sns = order_sns + jsonObject.getString("order_sn");
					}else{
						order_sns = order_sns + ","+jsonObject.getString("order_sn");
					}
				}
				try {
					//订单对象转换
					OrderInfoDTO orderInfo = toOrderInfoDTO(jsonObject);
					orderInfoList.add(orderInfo);
				} catch (Exception e) {
					logger.error("获取唯品会订单JSONObject转换为OrderInfoDTO失败：" + e.getMessage());
				}
			}
			//String order_sn = "13122600092722,13122600092622,13122600092522,13122600092422";
			//分页查询商品数据
			List<OrderGoodsDTO> goodsList = Lists.newArrayList();
			if(StringUtils.isBlank(order_sns)){
				logger.info("order_sns is blank.");
				continue;
			}
			
			// 获取订单商品信息
			paramsMap.put("p", 1);
			paramsMap.put("l", pageSize*2);//由于一个订单可能包含多个商品，如果作分页就更好更严谨了，不过比较复杂，所以此处把页面大小调整到100，不作分页简化复杂度
			paramsMap.put("order_sn", order_sns);
			paramsMap.remove("stat");
			String goods_call_time = System.currentTimeMillis() + "";
			String goods_token = MD5Util.encode(WmsConstants.VIP_SCM_SOURCE
					+ WmsConstants.VIP_SCM_ORDER_GOODS_LIST_API_NAME + goods_call_time);
			paramsMap.put("token", goods_token);
			paramsMap.put("call_time", goods_call_time);
			JSONArray orderGoodsList = null;
			try {
				// 获取订单商品信息
				String goodsResult = httpUtil.doPost2(WmsConstants.VIP_SCM_URL
						+ WmsConstants.VIP_SCM_ORDER_GOODS_LIST, paramsMap);
				//System.out.println("商品列表接口参数="+jsonUtils.toJson(paramsMap));
				//System.out.println("商品列表接口结果="+goodsResult);
				JSONObject goodsObj = JSONObject.fromObject(goodsResult);
				status = Integer.valueOf(goodsObj.getString("status"));
				if (status != 1) {
					logger.error("获取唯品会订单之商品列表发生错误："+goodsObj.toString()+",订单页号："+i);
					continue;
				}
				JSONObject data2 = goodsObj.getJSONObject("data");
				orderGoodsList = data2.getJSONArray("list");
			} catch (Exception e) {
				logger.error("获取唯品会订单商品信息发生错误：" + e.getMessage());
				return;
			}
			if(CollectionUtils.isEmpty(orderGoodsList)){
				logger.info("暂无唯品会订单商品信息.");
				continue;
			}
			for (int k = 0; k < orderGoodsList.size(); k++) {
				JSONObject jsonObject = orderGoodsList.getJSONObject(k);
				try {
					//商品对象转换
					OrderGoodsDTO goods = toOrderGoodsDTO(jsonObject);
					goodsList.add(goods);
				} catch (Exception e) {
					logger.error("获取唯品会订单JSONObject转换为OrderGoodsDTO失败：" + e.getMessage());
				}
			}
		
			//关联订单和商品数据
			if(CollectionUtils.isEmpty(orderInfoList)){
				logger.info("orderInfoList is empty.");
				continue;
			}
			if(CollectionUtils.isEmpty(goodsList)){
				logger.info("goodsList is empty.");
				continue;
			}
			for(OrderInfoDTO order : orderInfoList){			
				for(OrderGoodsDTO goods : goodsList){
					if(StringUtils.isNotBlank(order.getOrderCode()) && order.getOrderCode().equals(goods.getOrderCode())){
						if(CollectionUtils.isEmpty(order.getGoodsList())){
							order.setGoodsList(new ArrayList<OrderGoodsDTO>());
							order.getGoodsList().add(goods);
						}else{
							order.getGoodsList().add(goods);
						}
					}
					
					
				}
				
			}
			//同步订单和商品数据，且日志记录
			for (OrderInfoDTO order : orderInfoList) {
				//String msg = "";
				try {
					SalesOrder  sOrder = orderService.getSalesOrderByCode(order.getOrderCode());
					//判断是否重复
					if(sOrder != null){
						continue;
					}
					CommonResult result = orderManager.syncOrderNew(order, null);
					if (result.getErr().intValue() == ErrCodeEnum.ERR_SUCCESS.getErr().intValue()) {
						syncSuccCount++;
						//msg = "同步唯品会订单成功";
						log(order,"同步唯品会订单成功,orderCode="+order.getOrderCode());//记录日志
					} else {
						syncErrorCount++;
						//msg = "同步唯品会订单失败:errorCode="+result.getMsg();
						//同步失败的，不修改导出状态
						if(order_sns.endsWith(order.getOrderCode())){
							order_sns=order_sns.replace(","+order.getOrderCode(), "");
						}else{
							order_sns=order_sns.replace(order.getOrderCode()+",", "");
						}
						log(order,"同步唯品会订单失败:errorCode="+result.getMsg()+",orderCode="+order.getOrderCode());//记录日志
					}
					//Thread.sleep(1000);
				} catch (Exception e) {
					logger.error("syncVipOrder error:" + e.getMessage());
					syncErrorCount++;
					//msg = "同步唯品会订单失败:exceptionMsg="+e.getMessage();
					//同步失败的，不修改导出状态
					if(order_sns.endsWith(order.getOrderCode())){
						order_sns=order_sns.replace(","+order.getOrderCode(), "");
					}else{
						order_sns=order_sns.replace(order.getOrderCode()+",", "");
					}
					log(order,"同步唯品会订单失败:exceptionMsg="+e.getMessage()+",orderCode="+order.getOrderCode());//记录日志
				}
			}//end innter for
			
			//供应商根据订单号码修改订单导出状态，支持批量导出
			if(StringUtils.isBlank(order_sns)){
				continue;
			}
			paramsMap.put("p", 1);
			paramsMap.put("l", pageSize);
			paramsMap.put("order_sn", order_sns);
			String export_call_time = System.currentTimeMillis() + "";
			String export_token = MD5Util.encode(WmsConstants.VIP_SCM_SOURCE
					+ WmsConstants.VIP_SCM_ORDER_EXPORT_API_NAME + export_call_time);
			paramsMap.put("token", export_token);
			paramsMap.put("call_time", export_call_time);
			try {
				String exportResult = httpUtil.doPost2(WmsConstants.VIP_SCM_URL
						+ WmsConstants.VIP_SCM_ORDER_EXPORT, paramsMap);
				//System.out.println("修改订单导出状态接口参数="+jsonUtils.toJson(paramsMap));
				//System.out.println("修改订单导出状态接口结果="+exportResult);
				JSONObject exportObj = JSONObject.fromObject(exportResult);
				status = Integer.valueOf(exportObj.getString("status"));
				if (status != 1) {
					logger.error("修改订单导出状态发生错误：status="+status);
				}
				JSONObject data3 = exportObj.getJSONObject("data");
				int failNum = data3.getInt("fail_num");
				logger.info("修改订单导出状态失败数：failNum="+failNum);
			} catch (Exception e) {
				logger.error("修改订单导出状态发生错误：" + e.getMessage());
				//return;
			}
		}//end outer for
		String msg="同步唯品会订单结束，成功数="+syncSuccCount+",失败数="+syncErrorCount+",同步时间:st_add_time="+st_add_time+",et_add_time="+et_add_time;
		log(null,msg);//记录日志
		logger.error("syncSuccCount="+syncSuccCount+",syncErrorCount=" + syncErrorCount);
		//orderInfo.setGoodsList(goodsList);
		httpUtil.destroy();
	}

	/**
	 * JSONObject转换成OrderInfoDTO
	 * @param jsonObject
	 * @return
	 */
	public OrderInfoDTO toOrderInfoDTO(JSONObject jsonObject){
		OrderInfoDTO orderInfo = new OrderInfoDTO();
		//计算发票金额 = 整张出库单商品金额总和 + 快递费用 - 优惠金额 - 促销优惠金额)
		//计算订单金额 = 整张出库单商品金额总和 + 快递费用
		//整张出库单商品金额总和
		BigDecimal goods_money = StringUtils.isBlank(jsonObject.getString("goods_money"))?new BigDecimal(0):new BigDecimal(jsonObject.getString("goods_money"));
		//快递费用
		BigDecimal carriage = StringUtils.isBlank(jsonObject.getString("carriage"))?new BigDecimal(0):new BigDecimal(jsonObject.getString("carriage"));
		//优惠金额
		BigDecimal favourable_money = StringUtils.isBlank(jsonObject.getString("favourable_money"))?new BigDecimal(0):new BigDecimal(jsonObject.getString("favourable_money"));
		//促销优惠金额
		BigDecimal ex_fav_money = StringUtils.isBlank(jsonObject.getString("ex_fav_money"))?new BigDecimal(0):new BigDecimal(jsonObject.getString("ex_fav_money"));
		
		BigDecimal paidAmount = goods_money.add(carriage).subtract(favourable_money).subtract(ex_fav_money);
		BigDecimal orderAmount = goods_money.add(carriage);
		
		orderInfo.setOrderCode(jsonObject.getString("order_sn"));
		orderInfo.setOrderUser(jsonObject.getString("buyer"));
		orderInfo.setOrderTime(DateConvert.convertS2Date(jsonObject.getString("add_time")));
		orderInfo.setPaymentType(PaymentType.ONLINE.getCode());
		orderInfo.setPaymentCode("alipay");
		orderInfo.setPaymentName("在线支付");
		//orderInfo.setPayNo("12111211211");
		orderInfo.setPaidAmount(paidAmount);
		orderInfo.setConsignee(jsonObject.getString("buyer"));
		orderInfo.setProvince(jsonObject.getString("state"));
		orderInfo.setCity(jsonObject.getString("city"));
		//orderInfo.setDistrict("福田区");
		orderInfo.setAddress(jsonObject.getString("address"));
		orderInfo.setZipcode(jsonObject.getString("postcode"));
		orderInfo.setTel(jsonObject.getString("tel"));
		orderInfo.setMobile(jsonObject.getString("mobile"));
		orderInfo.setBestTime(jsonObject.getString("transport_day"));
		orderInfo.setShippingCode("sf_express");//暂时默认为顺丰
		orderInfo.setInvoiceEnabled(WmsConstants.ENABLED_TRUE);
		orderInfo.setInvoiceType(InvoiceType.PLAIN.getCode());
		orderInfo.setInvoiceAmount(paidAmount);
		orderInfo.setInvoiceTitle(jsonObject.getString("invoice"));
		//orderInfo.setInvoiceContent("发票内容");
		orderInfo.setPostscript(jsonObject.getString("remark"));
		orderInfo.setGoodsAmount(goods_money);
		orderInfo.setOrderAmount(orderAmount);
		orderInfo.setPayableAmount(paidAmount);
		
		orderInfo.setOrderSource(WmsConstants.OrderSource.VIP_GIONEE.getCode());
		return orderInfo;
	}
	
	/**
	 * JSONObject转换成OrderGoodsDTO
	 * @param jsonObject
	 * @return
	 */
	public OrderGoodsDTO toOrderGoodsDTO(JSONObject jsonObject){
		OrderGoodsDTO goods = new OrderGoodsDTO();
		goods.setOrderCode(jsonObject.getString("order_sn"));
		goods.setSkuCode(jsonObject.getString("good_sn"));
		goods.setUnitPrice(StringUtils.isBlank(jsonObject.getString("price"))?new BigDecimal(0):new BigDecimal(jsonObject.getString("price")));
		goods.setQuantity(jsonObject.getInt("amount"));
		goods.setSubtotalPrice(new BigDecimal(goods.getUnitPrice().floatValue() * goods.getQuantity().intValue()).setScale(2, BigDecimal.ROUND_HALF_UP));
		goods.setOrderSource(WmsConstants.OrderSource.VIP_GIONEE.getCode());
		return goods;
	}
	
	/**
	 * 同步日志记录
	 * @param order
	 */
	public synchronized void  log(OrderInfoDTO order,String msg){
		Log log = new Log();
		log.setContent(order == null?"":jsonUtils.toJson(order));
		log.setOpName(msg);
		log.setOpTime(new Date());
		log.setOpUserName("WMS系统");
		log.setType(WmsConstants.LogType.BIZ_LOG.getCode());
		try {
			logService.insertLog(log);
		} catch (Exception e) {
            logger.error("同步唯品会订单日志记录失败:"+e.getMessage());
		}
	}
	
	public static void main(String[] args) throws IOException {
		new SyncVipOrderClient().syncVipOrder();
	}

	public void notifyToSend(SendRequest req) {
		try {
			HttpUtil httpUtil = new HttpUtil();
			httpUtil.init();
			Map<String, Object> paramsMap = new HashMap<String, Object>();
			String order_call_time = System.currentTimeMillis() + "";
			String order_token = MD5Util.encode(WmsConstants.VIP_SCM_SOURCE
					+ WmsConstants.VIP_SCM_SHIP_API_NAME + order_call_time);
			paramsMap.put("token", order_token);
			paramsMap.put("call_time", order_call_time);
			paramsMap.put("sid", WmsConstants.VIP_SCM_SID);
			paramsMap.put("o", "json");

			List<Map<String, String>> shipList = Lists.newArrayList();
			Map<String, String> shipMap = Maps.newHashMap();
			shipMap.put("order_sn", req.getOrderCode());
			if(req.getShippingCode().equals("sf_express")){
				shipMap.put("carriers_code", "1000000458");
				shipMap.put("carrier", "顺丰速运(直发)");
			}else if(req.getShippingCode().equals("ems")){
				shipMap.put("carriers_code", "1000000454");
				shipMap.put("carrier", "中国邮政速递物流(直发)");
			}else if(req.getShippingCode().equals("yto")){
				shipMap.put("carriers_code", "1000000455");
				shipMap.put("carrier", "上海圆通速递(直发)");
			}
			shipMap.put("transport_no", req.getShippingNo());
			shipList.add(shipMap);
			paramsMap.put("order_list", jsonUtils.toJson(shipList));

			String result = httpUtil.doPost2(WmsConstants.VIP_SCM_URL
					+ WmsConstants.VIP_SCM_SHIP, paramsMap);
			System.out.println("批量发货接口参数=" + jsonUtils.toJson(paramsMap));
			System.out.println("批量发货接口结果=" + result);

			JSONObject orderObj = JSONObject.fromObject(result);
			int status = 0;
			status = Integer.valueOf(orderObj.getString("status"));
			if (status != 1) {
				logger.error("唯品会发货发生错误：" + orderObj.toString());
				return;
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}
}
