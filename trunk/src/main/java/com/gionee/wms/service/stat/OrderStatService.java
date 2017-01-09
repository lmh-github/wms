package com.gionee.wms.service.stat;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.SaleStat;
import com.gionee.wms.entity.SalesOutStat;

public interface OrderStatService {
	/**
	 * 根据起止时间统计销售数据
	 * @param startDate
	 * @param endDate
	 */
	void statSaleData(Date statDate, Date startDate, Date endDate);

	/**
	 * 根据条件查询统计结果
	 * @param criteria
	 * @return
	 */
	List<SaleStat> querySaleStatList(Map<String, Object> criteria);
	
	public List<SalesOutStat> getSalesOutStatList(Map<String, Object> criteria, Page page);

	public int getSalesOutStatTotal(Map<String, Object> criteria);
	
	public void addSalesOutStat(List<SalesOutStat> statList);
}
