package com.gionee.wms.facade;

import com.gionee.wms.facade.dto.OrderInfoDTO;
import com.gionee.wms.facade.request.OperateOrderRequest;
import com.gionee.wms.facade.result.CommonResult;
import com.gionee.wms.facade.result.OperateOrderResult;
import com.gionee.wms.facade.result.QueryOrderResult;
import com.gionee.wms.facade.result.WmsResult;

public interface OrderManager {

	/**
	 * 同步订单
	 * 
	 * @param orderInfo 订单信息
	 * @param timestamp 时间戳
	 * @param signature 签名
	 * @return
	 */
	WmsResult syncOrder(OrderInfoDTO orderInfo, String timestamp, String signature, Integer operFlag);
	
	/**
	 * 同步订单
	 * 
	 * @param orderInfo 订单信息
	 * @param timestamp 时间戳
	 * @param signature 签名
	 * @return
	 */
	CommonResult syncOrderNew(OrderInfoDTO orderInfo, Integer operFlag);

	/**
	 * 取消订单
	 * @param orderCode 订单号
	 * @param timestamp 时间戳
	 * @param signature 签名
	 * @return
	 */
	WmsResult cancelOrder(String orderCode, String timestamp, String signature);
	
	/**
	 * 取消订单
	 * @param orderCode 订单号
	 * @param timestamp 时间戳
	 * @param signature 签名
	 * @return
	 */
	CommonResult cancelOrderNew(String orderCode);

	/**
	 * 查询订单
	 * @param orderCode
	 * @return
	 */
	QueryOrderResult queryOrder(String orderCode);

	/**
	 * 操作订单
	 * @param paramMap
	 * @return
	 */
	OperateOrderResult operateOrder(OperateOrderRequest req);
}
