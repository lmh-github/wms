package com.gionee.top.controller;

import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.junit.Before;
import org.junit.Test;

import com.gionee.top.entity.SendRequest;
import com.gionee.top.util.HttpUtil;
import com.gionee.top.util.JsonUtils;

public class OrderControllerTest {

	private HttpUtil httpUtil;
	JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
	
	@Before
	public void init(){
		httpUtil = new HttpUtil();
		httpUtil.init();
	}
	
	@Test
	public void testToSend() {
		try {
//			String url = "http://127.0.0.1:8080/orderSyncServer/order/toSend";
			String url = "http://121.196.132.61:30001/orderSyncServer/order/toSend";
			SendRequest req = new SendRequest();
			req.setOrderCode("493696031603505");
			req.setShippingCode("sf_express");
			req.setShippingNo("904031655879");
			System.out.println(httpUtil.doPostJson(url, jsonUtils.toJson(req)));
		} catch (Exception e) {
			
		}
	}

}
