package com.gionee.wms.web.client;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.InvoiceType;
import com.gionee.wms.common.WmsConstants.PaymentType;
import com.gionee.wms.dto.DeltaResult;
import com.gionee.wms.dto.SkuStocksDto;
import com.gionee.wms.dto.StockSyncDto;
import com.gionee.wms.entity.StockDelta;
import com.gionee.wms.facade.dto.OrderGoodsDTO;
import com.gionee.wms.facade.dto.OrderInfoDTO;
import com.gionee.wms.facade.request.SyncOrderRequest;
import com.gionee.wms.facade.result.CommonResult.ErrCodeEnum;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.ShippingService;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * IUNI订单系统客户端
 * @author Administrator
 * 
 */
@Component("oCClient")
public class OCClient {
	private static final int SUCCESS = 1;
	private static final int DUPLICATE_NOTIFY_SHIPPED = 106;
	private static final int DUPLICATE_NOTIFY_BACK = 107;
	private static Logger logger = LoggerFactory.getLogger(OCClient.class);
	private ShippingService shippingService;
	private HttpUtil httpUtil;
	private JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
	
	/**
	 * 同步库存
	 * @param stockList
	 * @throws IOException 
	 * @throws Exception
	 */
	public void syncStock(List<StockDelta> deltaList) throws ServiceException {
		logger.info("###### sync stock begin ######");
		long tempTimePoint = System.currentTimeMillis();
		if (deltaList != null && deltaList.size() > 0) {
			SkuStocksDto stockDto = new SkuStocksDto();
			List<StockSyncDto> skuList = Lists.newArrayList();
			for (StockDelta delta : deltaList) {
				if(!StringUtils.isEmpty(delta.getGoodsSid())) {
					// 存在商品的skuId
					StockSyncDto skuDto = new StockSyncDto();
					skuDto.setQuantity(delta.getQuantity());
					System.out.println("===============>" + delta.getGoodsSid());
					skuDto.setSkuId(delta.getGoodsSid());
					skuList.add(skuDto);
				}
			}
			if(skuList.size()==0) {
				logger.info("不存在同步的skuId,同步终止");
				logger.info("###### sync stock end ######");
				return;
			}
			stockDto.setSkuStocks(skuList);
			
			String resultMsg = "";
			try {
				resultMsg = httpUtil.doPostJson(WmsConstants.OC_SYNC_STOCK_API, jsonUtils.toJson(stockDto));
				if (StringUtils.isEmpty(resultMsg)) {
					throw new ServiceException("syncStock return null");
				}
				DeltaResult result = jsonUtils.fromJson(resultMsg,	DeltaResult.class);
				Integer err = result.getErr();
				if (ErrCodeEnum.ERR_SUCCESS.getErr() != err) {
					// 不成功
					throw new ServiceException(err + "");
				}
				JSONObject data = (JSONObject)result.getData();
				JSONArray _skuList =(JSONArray) data.get("skuList");
			} catch(IOException e) {
				throw new ServiceException(e.getMessage());
			} finally {
				logger.info("sync stock return:[{}], elapsed time:{}",
						resultMsg, System.currentTimeMillis() - tempTimePoint);
				logger.info("###### sync stock end ######");
			}
		}
	}

	/**
	 * 订单状态通知
	 * 
	 * @param order
	 */
	public void notifyOrder(String content) throws Exception {
		logger.info("###### iuni notify order status begin ######");
		long tempTimePoint = System.currentTimeMillis();

		String resultMsg = "";
		try {
			resultMsg = httpUtil.doPostJson(WmsConstants.OC_UPDATE_ORDER_API, content);
			Map<String, Object> resultMap = jsonUtils.fromJson(resultMsg, Map.class);
			if (resultMap == null) {
				throw new RuntimeException("notifyOrderStatus return illegal");
			}
			int err = (Integer) resultMap.get("err");
			if (ErrCodeEnum.ERR_SUCCESS.getErr() != err) {
				throw new RuntimeException(err + "");
			}
		} finally {
			logger.info("iuni notify order status return:[{}], elapsed time:{}", resultMsg, System.currentTimeMillis()
					- tempTimePoint);
			logger.info("###### iuni notify order status end ######");
		}
	}

	public void syncOrder(String orderCode) throws IOException {
		OrderInfoDTO orderInfo = new OrderInfoDTO();
		orderInfo.setOrderCode(orderCode);
		orderInfo.setOrderUser("zhangsan");
		orderInfo.setOrderTime(new Date());
		orderInfo.setPaymentType(PaymentType.ONLINE.getCode());
		orderInfo.setPaymentCode("");
		orderInfo.setPaymentName("支付宝");
		orderInfo.setPayNo("12111211211");
		orderInfo.setPaidAmount(new BigDecimal("1500"));
		orderInfo.setConsignee("张三");
		orderInfo.setProvince("广东省");
		orderInfo.setCity("深圳市");
		orderInfo.setDistrict("福田区");
		orderInfo.setAddress("车公庙110号");
		orderInfo.setZipcode("518000");
		orderInfo.setTel("0755-87890987");
		orderInfo.setMobile("13888888888");
		orderInfo.setBestTime("周未");
		orderInfo.setShippingCode("sf_express");
		orderInfo.setInvoiceEnabled(WmsConstants.ENABLED_TRUE);
		orderInfo.setInvoiceType(InvoiceType.PLAIN.getCode());
		orderInfo.setInvoiceAmount(new BigDecimal("1600"));
		orderInfo.setInvoiceTitle("张三");
		orderInfo.setInvoiceContent("test");
		orderInfo.setPostscript("请及时发货");
		orderInfo.setGoodsAmount(new BigDecimal("1656.5"));
		orderInfo.setOrderAmount(new BigDecimal("1666.5"));
		orderInfo.setPayableAmount(new BigDecimal("1600"));
		List<OrderGoodsDTO> goodsList = Lists.newArrayList();
		OrderGoodsDTO goods = new OrderGoodsDTO();
		 goods.setSkuCode("A0002001");
		 goods.setUnitPrice(new BigDecimal("1500"));
		 goods.setQuantity(1);
		 goods.setSubtotalPrice(new BigDecimal("1500"));
		 goodsList.add(goods);
		// goods = new OrderGoodsDTO();
		// goods.setSkuCode("B0001001");
		// goods.setUnitPrice(new BigDecimal("50.5"));
		// goods.setQuantity(3);
		// goods.setSubtotalPrice(new BigDecimal("156.5"));
		// goodsList.add(goods);
		// orderInfo.setGoodsList(goodsList);
		// goods = new OrderGoodsDTO();
		// goods.setSkuCode("A0001002");
		// goods.setUnitPrice(new BigDecimal("2000"));
		// goods.setQuantity(1);
		// goods.setSubtotalPrice(new BigDecimal("2000"));
		// goodsList.add(goods);
		// goods = new OrderGoodsDTO();
		// goods.setSkuCode("C0002001");
		// goods.setUnitPrice(new BigDecimal("300"));
		// goods.setQuantity(10);
		// goods.setSubtotalPrice(new BigDecimal("5000"));
		// goodsList.add(goods);

		goods = new OrderGoodsDTO();
		goods.setSkuCode("C0002001");
		goods.setUnitPrice(new BigDecimal("1010.01"));
		goods.setQuantity(1);
		goods.setSubtotalPrice(new BigDecimal("1010.01"));
		goodsList.add(goods);
		orderInfo.setGoodsList(goodsList);
		SyncOrderRequest request = new SyncOrderRequest();
		request.setOrderInfo(orderInfo);
		String timestamp = System.currentTimeMillis() + "";
		request.setTimestamp(timestamp);

		request.setSignature(DigestUtils.md5Hex(orderInfo.getOrderCode() + orderInfo.getConsignee() + timestamp
				+ WmsConstants.SYNC_ORDER_SALT));

		HttpUtil httpUtil = new HttpUtil();
		httpUtil.init();
		 String jsonStr = jsonUtils.toJson(request);
//		String jsonStr = "{\"signature\":\"2ff1034c40a39b8124fb1dfe65cdf5a7\",\"timestamp\":1374633777000,\"orderInfo\":{\"orderCode\":\"20130723109687050037\",\"orderUser\":\"test1\",\"orderTime\":\"1374569159\",\"paymentType\":2,\"paymentCode\":\"cod\",\"payNo\":\"\",\"paidAmount\":\"0.00\",\"consignee\":\"wssss\",\"province\":\"\u5317\u4eac\",\"city\":\"\u5317\u4eac\",\"district\":\"\u897f\u57ce\u533a\",\"address\":\"\u5730\u5740\u6216\u56fa\u5b9a\u7535\u6216\u56fa\u5b9a\u7535\u6216\u56fa\u5b9a\u753511\",\"zipcode\":\"222222\",\"tel\":\"\",\"mobile\":\"11111111111\",\"bestTime\":\"\",\"shippingCode\":\"sf_express\",\"invoiceEnabled\":1,\"invoiceType\":1,\"invoiceTitle\":\"wssss\",\"invoiceAmount\":\"0.00\",\"invoiceContent\":\"\u6d4b\u8bd5\u624b\u673atest1 \",\"postscript\":\"\",\"goodsAmount\":\"1010.01\",\"orderAmount\":1010.01,\"payableAmount\":\"1010.01\",\"goodsList\":[{\"skuCode\":\"C0002001\",\"unitPrice\":\"1010.01\",\"quantity\":\"1\",\"subtotalPrice\":1010.01}]}}";
		String responseJson = httpUtil.doPostJson("http://127.0.0.1:8080/wms/api/syncOrder.action", jsonStr);
//		String responseJson = httpUtil.doPostJson("http://18.8.5.116:8080/wms/api/syncOrder.action", jsonStr);
		httpUtil.destroy();
		System.out.println(responseJson);
	}

	public void cancelOrder(String orderCode) throws IOException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("orderCode", orderCode);
		String timestamp = System.currentTimeMillis() + "";
		jsonObj.put("timestamp", timestamp);
		jsonObj.put("signature", DigestUtils.md5Hex(orderCode + timestamp + WmsConstants.SYNC_ORDER_SALT));

		HttpUtil httpUtil = new HttpUtil();
		httpUtil.init();
		String responseJson = httpUtil.doPostJson("http://127.0.0.1:8080/wms/api/cancelOrder.action", jsonUtils
				.toJson(jsonObj));
		httpUtil.destroy();
		System.out.println(responseJson);
	}

	public void queryStock(String skuCode) throws IOException {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("warehouseCode", "001");
		jsonObj.put("skuCode", skuCode);

		HttpUtil httpUtil = new HttpUtil();
		httpUtil.init();
		// String responseJson =
		// httpUtil.doPostJson("http://127.0.0.1:8080/wms/api/queryStock.action",
		// jsonUtils
		// .toJson(jsonObj));
		Map param = Maps.newHashMap();
		param.put("warehouseCode", "001");
		param.put("skuCode", "A0001002");
		String responseJson = httpUtil.doPost("http://127.0.0.1:8080/wms/api/queryStock.action", param);
		httpUtil.destroy();
		System.out.println(responseJson);
	}

	// public void syncOrders() throws IOException{
	// SyncOrderRequest request = new SyncOrderRequest();
	// List orders = Lists.newArrayList();
	// // request.setOrders(orders);
	// OrderInfoDTO order = new OrderInfoDTO();
	// order.setOrderCode("11111111");
	// order.setOrderUser("zhangsan");
	// order.setOrderTime(new Date());
	// order.setPaymentCode(PaymentType.ALIPAY.getCode());
	// order.setConsignee("张三");
	// order.setProvince("广东省");
	// order.setCity("深圳市");
	// order.setDistrict("福田区");
	// order.setAddress("车公庙110号");
	// order.setZipcode("518000");
	// order.setTel("0755-87890987");
	// order.setMobile("13888888888");
	// order.setBestTime("周未");
	// order.setShippingCode("001");
	// order.setInvoiceEnabled(WmsConstants.ENABLED_TRUE);
	// order.setInvoiceAmount(new BigDecimal("1656.5"));
	// order.setInvoiceTitle("张三");
	// order.setInvoiceContent("test");
	// order.setPostscript("请及时发货");
	// order.setGoodsAmount(new BigDecimal("1656.5"));
	// order.setShippingFee(new BigDecimal("10"));
	// List<OrderGoodsDTO> goodsList = Lists.newArrayList();
	// order.setGoodsList(goodsList);
	// OrderGoodsDTO goods = new OrderGoodsDTO();
	// goods.setSkuCode("A0001002");
	// goods.setUnitPrice(new BigDecimal("1500"));
	// goods.setQuantity(1);
	// goods.setSubtotalPrice(new BigDecimal("1500"));
	// goodsList.add(goods);
	// goods = new OrderGoodsDTO();
	// goods.setSkuCode("B0001001");
	// goods.setUnitPrice(new BigDecimal("50.5"));
	// goods.setQuantity(3);
	// goods.setSubtotalPrice(new BigDecimal("156.5"));
	// goodsList.add(goods);
	// orders.add(order);
	// order = new OrderInfoDTO();
	// order.setOrderCode("22222222");
	// order.setOrderUser("lisi");
	// order.setOrderTime(new Date());
	// order.setPaymentCode(PaymentType.ALIPAY.getCode());
	// order.setConsignee("李四");
	// order.setProvince("湖南省");
	// order.setCity("长沙市");
	// order.setDistrict("芙蓉区");
	// order.setAddress("解放路五里牌2320号");
	// order.setZipcode("414411");
	// order.setTel("0731-89898767");
	// order.setMobile("13000000000");
	// order.setBestTime("工作日");
	// order.setShippingCode("002");
	// order.setInvoiceEnabled(WmsConstants.ENABLED_TRUE);
	// order.setInvoiceAmount(new BigDecimal("1600"));
	// order.setInvoiceTitle("李四");
	// order.setPostscript("请不要改错尺寸");
	// order.setGoodsAmount(new BigDecimal("1600"));
	// order.setShippingFee(new BigDecimal("10"));
	// goodsList = Lists.newArrayList();
	// order.setGoodsList(goodsList);
	// goods = new OrderGoodsDTO();
	// goods.setSkuCode("A0002001");
	// goods.setUnitPrice(new BigDecimal("1200"));
	// goods.setQuantity(1);
	// goods.setSubtotalPrice(new BigDecimal("1200"));
	// goodsList.add(goods);
	// goods = new OrderGoodsDTO();
	// goods.setSkuCode("C0002001");
	// goods.setUnitPrice(new BigDecimal("200"));
	// goods.setQuantity(2);
	// goods.setSubtotalPrice(new BigDecimal("400"));
	// goodsList.add(goods);
	// orders.add(order);
	// JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
	// String timestamp = System.currentTimeMillis()+"";
	// request.setTimestamp(timestamp);
	// request.setSignature(DigestUtils.md5Hex(orders.size()+timestamp+WmsConstants.SYNC_ORDER_SALT));
	//		
	// HttpUtil httpUtil = new HttpUtil();
	// httpUtil.init();
	// httpUtil.doPostJson("http://127.0.0.1:8080/wms/api/syncOrders.action",
	// jsonUtils.toJson(request));
	// httpUtil.destroy();
	// }

	public static void main(String[] args) throws IOException {
		OCClient ocClient = new OCClient();
		ocClient.syncOrder("39999999");
		ocClient.syncOrder("40000000");
		ocClient.syncOrder("41111111");
//		ocClient.syncOrder("25555555");
//		ocClient.syncOrder("26666666");
//		 ocClient.cancelOrder("21111111");
		// ocClient.queryStock("A0001002");
		// System.out.println(new Date(1374585481000l));

	}

	@Autowired
	public void setHttpUtil(HttpUtil httpUtil) {
		this.httpUtil = httpUtil;
	}

	public ShippingService getShippingService() {
		return shippingService;
	}

	@Autowired
	public void setShippingService(ShippingService shippingService) {
		this.shippingService = shippingService;
	}

}
