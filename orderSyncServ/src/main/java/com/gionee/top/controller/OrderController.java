package com.gionee.top.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gionee.top.entity.CommonResult;
import com.gionee.top.entity.SendRequest;
import com.gionee.top.service.OrderService;

/**
 * 
 * 作者:milton.zhang
 * 时间:2013-12-20
 * 描述:订单业务处理
 */
@Controller
@RequestMapping("/order")
public class OrderController {
	private static final Log log = LogFactory.getLog(OrderController.class);

	@Autowired
	private OrderService orderService;

	/**
	 * 发货处理
	 * @return
	 */
	@RequestMapping(value = "/toSend", method = RequestMethod.POST)
	public @ResponseBody
	CommonResult toSend(@RequestBody SendRequest sendReq) {
		return orderService.toSend(sendReq);
	}

	/**
	 * 发货处理，支持浏览器GET方式
	 * @return
	 */
	@RequestMapping(value = "/toSendFast", method = RequestMethod.GET)
	public @ResponseBody
	CommonResult toTest(String orderCode, String shippingCode, String shippingNo) {
		SendRequest sendReq = new SendRequest();
		sendReq.setOrderCode(orderCode);
		sendReq.setShippingCode(shippingCode);
		sendReq.setShippingNo(shippingNo);
		return orderService.toSend(sendReq);
	}

}
