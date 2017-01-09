package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Warehouse;

@BatisDao
public interface WarehouseDao {
	/**
	 *获取仓库信息列表.
	 */
	List<Warehouse> queryWarehouseList(Map<String, Object> criteria);

	/**
	 * 添加仓库
	 */
	int addWarehouse(Warehouse warehouse);

	/**
	 * 更新仓库
	 */
	int updateWarehouse(Warehouse warehouse);

	/**
	 * 更新所有仓库为非默认状态
	 */
	int updateAllWarehouseToUndefault();
}
