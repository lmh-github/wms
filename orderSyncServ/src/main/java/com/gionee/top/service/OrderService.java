package com.gionee.top.service;

import com.gionee.top.entity.CommonResult;
import com.gionee.top.entity.SendRequest;

/**
 * 
 * 作者:milton.zhang
 * 时间:2013-12-20
 * 描述:订单业务类
 */
public interface OrderService {
	public CommonResult toSend(SendRequest sendReq);
}
