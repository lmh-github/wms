package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Shipping;

@BatisDao
public interface ShippingDao {
	/**
	 * 取配送方式列表 
	 */
	List<Shipping> queryShippingList(Map<String, Object> criteria);
	/**
	 * 添加配送方式
	 */
	int addShipping(Shipping shipping);

	/**
	 * 更新配送方式
	 */
	int updateShipping(Shipping shipping);

	/**
	 * 更新所有配送方式为非默认状态
	 */
	int updateAllShippingToUndefault();
}
