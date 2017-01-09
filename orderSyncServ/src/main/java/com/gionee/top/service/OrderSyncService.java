package com.gionee.top.service;

/**
 * 
 * 作者:milton.zhang
 * 时间:2013-11-30
 * 描述:订单同步业务类
 */
public interface OrderSyncService {
	
	/**
	 * 同步淘宝订单业务
	 */
	public void syncOrder();
	
	/**
	 * 同步淘宝分销订单业务
	 */
	public void syncFxOrder();
}
