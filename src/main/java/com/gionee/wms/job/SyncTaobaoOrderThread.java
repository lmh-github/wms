package com.gionee.wms.job;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.gionee.wms.common.DateConvert;
import com.gionee.wms.common.MessageDecode;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.OrderSource;
import com.gionee.wms.facade.OrderManager;
import com.gionee.wms.facade.dto.OrderGoodsDTO;
import com.gionee.wms.facade.dto.OrderInfoDTO;
import com.gionee.wms.facade.request.SyncOrderRequest;
import com.gionee.wms.facade.result.WmsResult;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.NotifyTrade;
import com.taobao.api.request.TradeContactGetRequest;
import com.taobao.api.request.TradeFullinfoGetRequest;
import com.taobao.api.response.TradeContactGetResponse;
import com.taobao.api.response.TradeFullinfoGetResponse;

public class SyncTaobaoOrderThread implements Runnable {

	private static Logger logger = LoggerFactory
			.getLogger(SyncTaobaoOrderThread.class);

	@Autowired
	private OrderManager orderManager;

	private String msg;

	public SyncTaobaoOrderThread(String msg) {
		this.msg = msg;
	}

	@Override
	public void run() {
		try {
			Object obj = MessageDecode.decodeMsg(msg);
			if (obj != null && (obj instanceof NotifyTrade)) {
				NotifyTrade trade = (NotifyTrade) obj;
				//封装订单请求
				SyncOrderRequest reqInfo = new SyncOrderRequest();
				OrderInfoDTO orderInfo = new OrderInfoDTO();

				//根据交易编号查找交易详情taobao.trade.fullinfo.get
				TaobaoClient client = new DefaultTaobaoClient(
						WmsConstants.TAOBAO_TOP_URL,
						WmsConstants.TAOBAO_APPKEY, WmsConstants.TAOBAO_SECRET,
						"json");
				TradeFullinfoGetRequest tradeInfoReq = new TradeFullinfoGetRequest();
				tradeInfoReq
						.setFields("receiver_name,receiver_address,receiver_city,receiver_district,total_fee,invoice_name,receiver_mobile,created,payment,type,alipay_no,buyer_message,receiver_state,receiver_phone,receiver_zip,available_confirm_fee,orders.price,orders.num,orders.total_fee,orders.outer_sku_id");
				tradeInfoReq.setTid(trade.getTid());
				TradeFullinfoGetResponse tradeInfores = client.execute(
						tradeInfoReq, WmsConstants.TAOBAO_SESSIONKEY);
				logger.info("taobao.trade.fullinfo.get-"
						+ tradeInfores.getBody());
				JSONObject jsonObj = JSONObject.fromObject(tradeInfores
						.getBody());
				JSONObject tradeJsonObj = jsonObj.getJSONObject(
						"trade_fullinfo_get_response").getJSONObject("trade");
				if (tradeJsonObj != null) {
					//生成时间戳与签名密钥
					String timestamp = String.valueOf(System
							.currentTimeMillis());
					StringBuffer plainStr = new StringBuffer();
					plainStr.append(trade.getTid())
							.append(tradeJsonObj.getString("receiver_name"))
							.append(timestamp)
							.append(WmsConstants.SYNC_ORDER_SALT);
					String signature = DigestUtils.md5Hex(plainStr.toString());
					reqInfo.setTimestamp(timestamp);
					reqInfo.setSignature(signature);

					//设置wms订单参数
					orderInfo.setOrderSource(OrderSource.TMALL_GIONEE.getCode());
					orderInfo.setBestTime(null);
					orderInfo.setCity(tradeJsonObj.getString("receiver_city"));
					orderInfo.setGoodsAmount(new BigDecimal(tradeJsonObj
							.getString("total_fee")));//商品总金额
					orderInfo.setInvoiceAmount(new BigDecimal(tradeJsonObj
							.getString("total_fee")));//发票金额??
					orderInfo.setInvoiceContent(null);
					orderInfo.setInvoiceEnabled(1);
					orderInfo.setInvoiceTitle(tradeJsonObj
							.getString("invoice_name"));
					orderInfo.setInvoiceType(null);
					orderInfo.setOrderAmount(new BigDecimal(tradeJsonObj
							.getString("total_fee")));//订单总金额(需要支付的金额)??
					orderInfo.setOrderCode(String.valueOf(trade.getTid()));
					orderInfo.setOrderTime(DateConvert.convertS2Date(
							tradeJsonObj.getString("created"),
							"yyyy-MM-dd HH:mm:ss"));
					orderInfo.setOrderUser(trade.getBuyerNick());
					orderInfo.setPaidAmount(new BigDecimal(tradeJsonObj
							.getString("payment")));//已支付金额??
					orderInfo.setPayableAmount(new BigDecimal(tradeJsonObj
							.getString("available_confirm_fee")));//应付金额??
					String paymentCode = tradeJsonObj.getString("type");//交易类型
					if (paymentCode.equals("cod")) {
						orderInfo.setPaymentCode(paymentCode);
						orderInfo.setPaymentName("货到付款");
						orderInfo.setPaymentType(2);
					} else {
						orderInfo.setPaymentCode("alipay");
						orderInfo.setPaymentName("支付宝");
						orderInfo.setPaymentType(1);
					}
					orderInfo.setPayNo(tradeJsonObj.getString("alipay_no"));//支付流水号(支付宝交易号)
					orderInfo.setPostscript(tradeJsonObj
							.getString("buyer_message"));
					orderInfo.setShippingCode("sf_express");//物流信息天猫后台可修改默认为顺风??

					//单笔订单联系信息
					TradeContactGetRequest contactReq = new TradeContactGetRequest();
					contactReq.setTid(trade.getTid());
					contactReq
							.setFields("receiver_city,receiver_district,receiver_name,receiver_state,receiver_address,receiver_zip,receiver_mobile,receiver_phone");
					TradeContactGetResponse contactRes = client.execute(
							contactReq, WmsConstants.TAOBAO_SESSIONKEY);
					logger.info("taobao.trade.contact.get-"
							+ contactRes.getBody());
					JSONObject contactJsonBody = JSONObject
							.fromObject(contactRes.getBody());
					JSONObject contactJsonObj = contactJsonBody.getJSONObject(
							"trade_contact_get_response").getJSONObject(
							"contact");
					orderInfo.setAddress(contactJsonObj
							.getString("receiver_address"));
					orderInfo.setConsignee(contactJsonObj
							.getString("receiver_name"));
					orderInfo.setDistrict(contactJsonObj
							.getString("receiver_district"));
					orderInfo.setMobile(contactJsonObj
							.getString("receiver_mobile"));
					orderInfo.setProvince(contactJsonObj
							.getString("receiver_state"));
					orderInfo
							.setTel(contactJsonObj.getString("receiver_phone"));
					orderInfo.setZipcode(contactJsonObj
							.getString("receiver_zip"));

					List<OrderGoodsDTO> goodsList = new ArrayList<OrderGoodsDTO>();
					JSONObject ordersJsonObj = tradeJsonObj
							.getJSONObject("orders");
					JSONArray orders = ordersJsonObj.getJSONArray("order");
					if (orders != null && orders.size() > 0) {
						for (int i = 0; i < orders.size(); i++) {
							OrderGoodsDTO good = new OrderGoodsDTO();
							good.setQuantity(Integer.valueOf(orders
									.getJSONObject(i).getString("num")));
							good.setSkuCode(orders.getJSONObject(i).getString(
									"outer_sku_id"));//wms的sku就是淘宝的商品外部编码，规格编码
							good.setSubtotalPrice(new BigDecimal(orders
									.getJSONObject(i).getString("total_fee")));
							good.setUnitPrice(new BigDecimal(orders
									.getJSONObject(i).getString("price")));
							goodsList.add(good);
						}
					}
					orderInfo.setGoodsList(goodsList);

					reqInfo.setOrderInfo(orderInfo);

					ApplicationContext ctx = new ClassPathXmlApplicationContext(
							"config/spring/spring-*.xml");
					OrderManager orderManager = (OrderManager) ctx
							.getBean("orderManager");
					WmsResult result = orderManager.syncOrder(
							reqInfo.getOrderInfo(), reqInfo.getTimestamp(),
							reqInfo.getSignature(), null);
					logger.info("SyncTaobaoOrderThread-orderCode:"
							+ reqInfo.getOrderInfo().getOrderCode()
							+ "-result-code:" + result.getCode() + "-message:"
							+ result.getMessage());
				}
			}
		} catch (Exception e) {
			logger.error("SyncTaobaoOrderThread", e);
			e.printStackTrace();
		}
	}

	@Autowired
	public void setOrderManager(OrderManager orderManager) {
		this.orderManager = orderManager;
	}
}
