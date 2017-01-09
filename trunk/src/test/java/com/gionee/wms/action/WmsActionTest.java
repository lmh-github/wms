package com.gionee.wms.action;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.junit.Before;
import org.junit.Test;

import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.facade.dto.OrderGoodsDTO;
import com.gionee.wms.facade.dto.OrderInfoDTO;
import com.gionee.wms.facade.request.OperateOrderRequest;
import com.gionee.wms.facade.request.SyncOrderRequest;
import com.gionee.wms.web.client.HttpUtil;
import com.google.common.collect.Lists;

public class WmsActionTest {
	
	private HttpUtil httpUtil;
	JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
	
	@Before
	public void init(){
		httpUtil = new HttpUtil();
		httpUtil.init();
	}
	
	@Test
	public void testSyncOrder() {
		try {
			String url = "http://127.0.0.1:8080/wms/api/syncOrderOc.action";
			SyncOrderRequest req = new SyncOrderRequest();
			req.setAppFlag("OFFICIAL_IUNI");
			req.setOperFlag(null);
			
			OrderInfoDTO orderInfo = new OrderInfoDTO();
			orderInfo.setAddress("milton address");
			orderInfo.setBestTime("周末");
			orderInfo.setCity("西安");
			orderInfo.setConsignee("milton");
			orderInfo.setDistrict("未央区");
			orderInfo.setGoodsAmount(new BigDecimal(1000));
			orderInfo.setInvoiceAmount(new BigDecimal(1000));
			orderInfo.setInvoiceContent("milton invoice content");
			orderInfo.setInvoiceEnabled(1);
			orderInfo.setInvoiceTitle("milton");
			orderInfo.setInvoiceType(1);
			orderInfo.setMobile("88888888");
			orderInfo.setOrderAmount(new BigDecimal(1000));
			orderInfo.setOrderCode("80000000003");
			orderInfo.setOrderSource("");
			orderInfo.setOrderTime(new Date());
			orderInfo.setOrderUser("milton");
			orderInfo.setPaidAmount(new BigDecimal(1000));
			orderInfo.setPayableAmount(new BigDecimal(1000));
			orderInfo.setPaymentCode("alipay");
			orderInfo.setPaymentName("milton");
			orderInfo.setPaymentType(1);
			orderInfo.setPayNo("12345678910");
			orderInfo.setPostscript("尽快发货");
			orderInfo.setProvince("陕西");
			orderInfo.setShippingCode("ems");
			orderInfo.setTel("091388888888");
			orderInfo.setZipcode("715500");
			
			req.setOrderInfo(orderInfo);
			
			List<OrderGoodsDTO> goods = Lists.newArrayList();
			OrderGoodsDTO good = new OrderGoodsDTO();
			good.setSubtotalPrice(new BigDecimal(1000));
			good.setQuantity(1);
			good.setSkuCode("1011");
			good.setUnitPrice(new BigDecimal(1000));
			goods.add(good);
			
			orderInfo.setGoodsList(goods);
			
			System.out.println(httpUtil.doPostJson(url, jsonUtils.toJson(req)));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	@Test
	public void testOperateOrder() {
		try {
			String url = "http://127.0.0.1:8080/wms/api/operateOrder.action";
			OperateOrderRequest req = new OperateOrderRequest();
			req.setBackCode("12345678");
			req.setOperFlag("BACK");
			req.setOrderCode("20130911442205017464");
			req.setRemark("milton test");
			req.setShippingCode("ems");
			req.setShippingNo("1234");
			req.setSignature("888");
			req.setTimestamp("124");
			
			List<OrderGoodsDTO> goods = Lists.newArrayList();
			OrderGoodsDTO good = new OrderGoodsDTO();
			good.setQuantity(2);
			good.setSkuCode("1002");
			goods.add(good);
			req.setGoods(goods);
			
			System.out.println(httpUtil.doPostJson(url, jsonUtils.toJson(req)));
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

}
