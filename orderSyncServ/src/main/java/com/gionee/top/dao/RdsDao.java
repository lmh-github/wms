package com.gionee.top.dao;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 
 * 作者:milton.zhang
 * 时间:2013-11-30
 * 描述:聚石塔云DB操作
 */
public interface RdsDao {
	/**
	 * 根据数据推送修改时间获取交易数据，淘宝
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 交易数据
	 * @throws Exception 
	 */
	public List<Map<String,Object>> getTradeInfoList(Date start, Date end);
	
	/**
	 * 根据数据推送修改时间获取交易数据，淘宝分销
	 * @param start 开始时间
	 * @param end 结束时间
	 * @return 交易数据
	 * @throws Exception 
	 */
	public List<Map<String,Object>> getFxTradeInfoList(Date start, Date end);
}
