package com.gionee.top.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.top.config.SystemConfig;
import com.gionee.top.config.SystemConfig.OrderSource;
import com.gionee.top.dao.RdsDao;
import com.gionee.top.entity.OrderGoodsDTO;
import com.gionee.top.entity.OrderInfoDTO;
import com.gionee.top.entity.SyncOrderRequest;
import com.gionee.top.service.OrderSyncService;
import com.gionee.top.util.DateConvert;
import com.gionee.top.util.HttpUtil;
import com.gionee.top.util.JsonUtils;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Order;
import com.taobao.api.domain.PurchaseOrder;
import com.taobao.api.domain.SubPurchaseOrder;
import com.taobao.api.domain.Trade;
import com.taobao.api.domain.TradeRecord;
import com.taobao.api.internal.util.TaobaoUtils;
import com.taobao.api.request.AlipayUserTradeSearchRequest;
import com.taobao.api.response.AlipayUserTradeSearchResponse;
import com.taobao.api.response.FenxiaoOrdersGetResponse;
import com.taobao.api.response.TradeFullinfoGetResponse;

@Service("orderSyncService")
public class OrderSyncServiceImpl implements OrderSyncService {
	private static final Log log = LogFactory
			.getLog(OrderSyncServiceImpl.class);

	private JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
	private HttpUtil httpUtil = new HttpUtil();

	@Autowired
	private RdsDao rdsDao;

	@Override
	public void syncOrder() {
		try {
			// 获取交易订单列表
			Calendar calendar1 = Calendar.getInstance();
			Date end = calendar1.getTime();
			calendar1.add(Calendar.MINUTE, -SystemConfig.SYNC_RATE);
			Date start = calendar1.getTime();
			List<Map<String, Object>> tradeObjList = rdsDao.getTradeInfoList(
					start, end);
			for (int i = 0; i < tradeObjList.size(); i++) {
				Map<String, Object> tradeMap = tradeObjList.get(i);
				String tradeInfoStr = tradeMap.get("jdp_response").toString();
				log.info("sync--order--start--num=" + i + "--tradeInfo="
						+ tradeInfoStr);
				TradeFullinfoGetResponse tradeInfo = TaobaoUtils.parseResponse(
						tradeInfoStr, TradeFullinfoGetResponse.class);
				Trade trade = tradeInfo.getTrade();

				// 根据交易来源屏蔽订单
				boolean isShield = false;
				String[] shieldFroms = SystemConfig.TOP_SHIELD_TRADE_FROM
						.split(",");
				for (String from : shieldFroms) {
					if (trade.getTradeFrom().contains(from)) {
						isShield = true;
						log.info("trade--from--shield--" + from);
					}
				}
				if (isShield) {
					continue;
				}
				// 封装订单请求
				SyncOrderRequest orderSyncReq = new SyncOrderRequest();
				OrderInfoDTO orderInfo = new OrderInfoDTO();
				List<OrderGoodsDTO> goodsList = new ArrayList<OrderGoodsDTO>();

				orderInfo.setOrderSource(OrderSource.TMALL_GIONEE.getCode());// IUNI天猫
				orderInfo.setBestTime(null);
				orderInfo.setCity(trade.getReceiverCity() == null ? "" : trade
						.getReceiverCity());
				orderInfo.setGoodsAmount(new BigDecimal(trade.getTotalFee()));// 商品总金额
				orderInfo.setInvoiceContent(null);
				orderInfo.setInvoiceEnabled(1);
				orderInfo.setInvoiceTitle(trade.getInvoiceName());
				orderInfo.setInvoiceType(null);
				orderInfo.setOrderAmount(new BigDecimal(trade.getPayment()));// 订单总金额
				orderInfo.setOrderCode(String.valueOf(trade.getTid()));
				orderInfo.setOrderTime(trade.getCreated());
				orderInfo.setOrderUser(trade.getBuyerNick());
				orderInfo.setPaidAmount(new BigDecimal(trade
						.getReceivedPayment()));// 已支付金额
				orderInfo.setPayableAmount(new BigDecimal(trade
						.getAvailableConfirmFee()));// 应付金额
				orderInfo.setPayNo(trade.getAlipayNo());// 支付流水号(支付宝交易号)
				String paymentCode = trade.getType();// 交易类型
				if (paymentCode.equals("cod")) {
					orderInfo.setPaymentCode(paymentCode);
					orderInfo.setPaymentName("货到付款");
					orderInfo.setPaymentType(2);
					orderInfo.setInvoiceAmount(new BigDecimal(trade.getPayment()));
				} else {
					orderInfo.setPaymentCode("alipay");
					orderInfo.setPaymentName("支付宝");
					orderInfo.setPaymentType(1);
					
					//调用支付宝接口获取该笔交易实际金额，该金额作为发票金额
					try {
						TaobaoClient client = new DefaultTaobaoClient(
								SystemConfig.AOP_URL, SystemConfig.APPKEY,
								SystemConfig.APPSECRET);
						AlipayUserTradeSearchRequest req=new AlipayUserTradeSearchRequest();
						Date nowTime = new Date();
						req.setEndTime(DateConvert.convertD2String(nowTime, "yyyy-MM-dd")+" 23:59:59");//开始时间，时间必须是今天范围之内。格式为yyyy-MM-dd HH:mm:ss，精确到秒
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(nowTime);
						calendar.add(Calendar.DATE, -6);
						Date startTime = calendar.getTime();
						req.setStartTime(DateConvert.convertD2String(startTime, "yyyy-MM-dd") + " 00:00:00");//	 结束时间。与开始时间间隔在七天之内
						req.setAlipayOrderNo(orderInfo.getPayNo());//支付宝订单号，为空查询所有记录
//						req.setMerchantOrderNo(orderInfo.getOrderCode());//商户订单号，为空查询所有记录
						req.setOrderType(null);//订单类型，为空查询所有类型订单。
						req.setOrderStatus(null);//订单状态，为空查询所有状态订单
						req.setOrderFrom(null);//订单来源，为空查询所有来源。淘宝(TAOBAO)，支付宝(ALIPAY)，其它(OTHER)
						req.setPageNo("1");
						req.setPageSize("1");
						AlipayUserTradeSearchResponse response = client.execute(req , SystemConfig.ACCESS_TOKEN);
						log.info("AlipayUserTradeSearchResponse--" + jsonUtils.toJson(response));
						List<TradeRecord> tradeRecords = response.getTradeRecords();
						if(tradeRecords != null && tradeRecords.size() > 0){
							TradeRecord record = tradeRecords.get(0);
							log.info("TradeRecord--" + jsonUtils.toJson(record));
							orderInfo.setInvoiceAmount(new BigDecimal(record.getTotalAmount()));// 发票金额
						}
					} catch (Exception e) {
						log.error("AlipayUserTradeSearchResponse--error--", e);
					}

					if(orderInfo.getInvoiceAmount() == null){
						orderInfo.setInvoiceAmount(new BigDecimal(trade.getPayment()));
					}
				}
				orderInfo.setPostscript(trade.getBuyerMessage());
				orderInfo.setShippingCode("sf_express");// 物流信息天猫后台可修改默认为顺丰

				// 单笔订单联系信息
				orderInfo.setAddress(trade.getReceiverAddress() == null
						? ""
						: trade.getReceiverAddress());
				orderInfo.setConsignee(trade.getReceiverName());
				orderInfo.setDistrict(trade.getReceiverDistrict() == null
						? ""
						: trade.getReceiverDistrict());
				orderInfo.setMobile(trade.getReceiverMobile());
				orderInfo.setProvince(trade.getReceiverState());
				orderInfo.setTel(trade.getReceiverPhone());
				orderInfo.setZipcode(trade.getReceiverZip());
				
				List<Order> orders = trade.getOrders();
				if (orders != null && orders.size() > 0) {
					for (Order order : orders) {
						OrderGoodsDTO good = new OrderGoodsDTO();
						good.setQuantity(Integer.valueOf(String.valueOf(order
								.getNum())));
						good.setSkuCode(order.getOuterSkuId());// wms的sku就是淘宝的商品外部编码，规格编码
						if(StringUtils.isBlank(good.getSkuCode())) {
							good.setSkuCode(order.getOuterIid());
						}
						good.setSubtotalPrice(new BigDecimal(order
								.getTotalFee()));
						good.setUnitPrice(new BigDecimal(order.getPrice()));
						goodsList.add(good);
					}
				}

				orderInfo.setGoodsList(goodsList);
				orderSyncReq.setOrderInfo(orderInfo);
				orderSyncReq.setAppFlag("TMALL_GIONEE");
				orderSyncReq.setOperFlag(null);// 新增标识，1：更新，其他：新增

				//设置请求签名信息
				String timestamp = String.valueOf(System.currentTimeMillis());
				orderSyncReq.setTimestamp(timestamp);
				StringBuilder plainStr = new StringBuilder();
				plainStr.append(trade.getTid()).append(trade.getReceiverName())
						.append(timestamp).append(SystemConfig.SYNC_ORDER_SALT);
				String signature = DigestUtils.md5Hex(plainStr.toString());
				orderSyncReq.setSignature(signature);

				httpUtil.init();
				String retStr = httpUtil.doPostJson(
						SystemConfig.WMS_ORDER_SYNC_URL,
						jsonUtils.toJson(orderSyncReq));
				log.info("sync--order--end--num=" + i + "--result=" + retStr);
			}
		} catch (Exception e) {
			log.error("sync--order--error--", e);
		}
	}

	@Override
	public void syncFxOrder() {
		try {
			// 获取交易订单列表
			Date end = new Date();
			Date start = new Date(end.getTime() - SystemConfig.SYNC_RATE * 1000);
			List<Map<String, Object>> tradeObjList = rdsDao.getFxTradeInfoList(
					start, end);
			for (int i = 0; i < tradeObjList.size(); i++) {
				Map<String, Object> tradeMap = tradeObjList.get(i);
				String tradeInfoStr = tradeMap.get("jdp_response").toString();
				log.info("sync--fx--order--start--num=" + i + "--tradeInfo="
						+ tradeInfoStr);
				FenxiaoOrdersGetResponse tradeInfo = TaobaoUtils.parseResponse(
						tradeInfoStr, FenxiaoOrdersGetResponse.class);
				List<PurchaseOrder> purchaseOrderList = tradeInfo
						.getPurchaseOrders();

				for (PurchaseOrder purchaseOrder : purchaseOrderList) {
					// 封装订单请求
					SyncOrderRequest orderSyncReq = new SyncOrderRequest();
					OrderInfoDTO orderInfo = new OrderInfoDTO();
					List<OrderGoodsDTO> goodsList = new ArrayList<OrderGoodsDTO>();

					orderInfo.setOrderSource(OrderSource.TAOBAO_FX_GIONEE.getCode());// GIONEE淘宝分销
					orderInfo.setBestTime(null);
					orderInfo
							.setCity(purchaseOrder.getReceiver().getCity() == null
									? ""
									: purchaseOrder.getReceiver().getCity());
					orderInfo.setInvoiceAmount(new BigDecimal(purchaseOrder
							.getTotalFee()));// 发票金额(以分销商给经销商优惠后总金额为准)
					orderInfo.setInvoiceContent(null);
					orderInfo.setInvoiceEnabled(1);
					orderInfo.setInvoiceTitle(purchaseOrder.getReceiver()
							.getName());
					orderInfo.setInvoiceType(null);
					orderInfo.setOrderAmount(new BigDecimal(purchaseOrder
							.getTotalFee()));// 订单总金额(以分销商给经销商优惠后总金额为准)
					orderInfo.setOrderCode(String.valueOf(purchaseOrder
							.getTcOrderId()));
					orderInfo.setOrderTime(purchaseOrder.getCreated());
					orderInfo.setOrderUser(purchaseOrder.getBuyerNick());
					orderInfo.setPaidAmount(new BigDecimal(purchaseOrder
							.getDistributorPayment()));// 已支付金额
					orderInfo.setPayableAmount(new BigDecimal(purchaseOrder
							.getDistributorPayment()));// 应付金额
					String paymentCode = purchaseOrder.getPayType();// 交易类型(默认所以都设置为支付宝)
					orderInfo.setPaymentCode("alipay");
					orderInfo.setPaymentName("支付宝");
					orderInfo.setPaymentType(1);
					orderInfo.setPayNo(purchaseOrder.getAlipayNo());// 支付流水号(支付宝交易号)
					orderInfo.setPostscript(purchaseOrder.getMemo());
					orderInfo.setShippingCode("sf_express");// 物流信息天猫后台可修改默认为顺丰

					// 单笔订单联系信息
					orderInfo.setAddress(purchaseOrder.getReceiver()
							.getAddress() == null ? "" : purchaseOrder
							.getReceiver().getAddress());
					orderInfo.setConsignee(purchaseOrder.getReceiver()
							.getName());
					orderInfo.setDistrict(purchaseOrder.getReceiver()
							.getDistrict() == null ? "" : purchaseOrder
							.getReceiver().getDistrict());
					orderInfo.setMobile(purchaseOrder.getReceiver()
							.getMobilePhone());
					orderInfo.setProvince(purchaseOrder.getReceiver()
							.getState());
					orderInfo.setTel(purchaseOrder.getReceiver().getPhone());
					orderInfo.setZipcode(purchaseOrder.getReceiver().getZip());

					float goodsAmount = 0;//商品总金额(优惠前的商品总价)

					List<SubPurchaseOrder> orders = purchaseOrder
							.getSubPurchaseOrders();
					if (orders != null && orders.size() > 0) {
						for (SubPurchaseOrder order : orders) {
							OrderGoodsDTO good = new OrderGoodsDTO();
							good.setQuantity(Integer.valueOf(String
									.valueOf(order.getNum())));
							good.setSkuCode(order.getSkuOuterId());// wms的sku就是淘宝的商品外部编码，规格编码
							good.setSubtotalPrice(new BigDecimal(order
									.getTotalFee()));
							good.setUnitPrice(new BigDecimal(order.getBillFee()));
							goodsList.add(good);

							goodsAmount += (Integer.valueOf(String
									.valueOf(order.getNum())) * Float
									.valueOf(order.getAuctionPrice()));
						}
					}

					orderInfo.setGoodsAmount(new BigDecimal(String
							.valueOf(goodsAmount)));// 商品总金额(优惠前的商品总价)

					orderInfo.setGoodsList(goodsList);
					orderSyncReq.setOrderInfo(orderInfo);
					orderSyncReq.setAppFlag("TAOBAO_FX_GIONEE");
					orderSyncReq.setOperFlag(null);// 新增标识，1：更新，其他：新增

					//设置请求签名信息
					String timestamp = String.valueOf(System.currentTimeMillis());
					orderSyncReq.setTimestamp(timestamp);
					StringBuilder plainStr = new StringBuilder();
					plainStr.append(purchaseOrder.getTcOrderId()).append(purchaseOrder.getReceiver().getName())
							.append(timestamp).append(SystemConfig.SYNC_ORDER_SALT);
					String signature = DigestUtils.md5Hex(plainStr.toString());
					orderSyncReq.setSignature(signature);
					
					httpUtil.init();
					String retStr = httpUtil.doPostJson(
							SystemConfig.WMS_ORDER_SYNC_URL,
							jsonUtils.toJson(orderSyncReq));
					log.info("sync--fx--order--end--num=" + i + "--result="
							+ retStr);
				}
			}
		} catch (Exception e) {
			log.error("sync--fx--order--error--", e);
		}
	}

}
