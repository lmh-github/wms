package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.SaleStat;
import com.gionee.wms.entity.SalesOutStat;
import com.gionee.wms.vo.SalesStatVo;
import com.gionee.wms.vo.TransferStatVo;

/**
 * 
 * 作者:milton.zhang
 * 时间:2014-4-24
 * 描述:统计DAO
 */
@BatisDao
public interface StatDao {

	/**
	 * 批量增加销售统计记录
	 */
	void addSaleStats(List<SaleStat> statList);

	/**
	 * 根据条件查询统计结果
	 * @param criteria
	 * @return
	 */
	List<SaleStat> querySaleStatList(Map<String, Object> criteria);
	
	/**
	 * 分页查询
	 */
	List<SalesOutStat> querySalesOutStatByPage(Map<String, Object> criteria);
	
	/**
	 * 查询总数
	 */
	int querySalesOutStatTotal(Map<String, Object> criteria);
	
	/**
	 * 插入出库统计记录
	 */
	void addSalesOutStat(SalesOutStat salesOutStat);
	
	/**
	 * 查询订单统计数据
	 */
	List<SalesStatVo> queryOrderListAndGoodsForStat(Map<String, Object> criteria);
	
	/**
	 * 查询调拨统计数据
	 */
	List<TransferStatVo> getTransferAndGoodsForStat(Map<String, Object> criteria);
}
