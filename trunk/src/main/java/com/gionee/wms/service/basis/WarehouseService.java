package com.gionee.wms.service.basis;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;

public interface WarehouseService {
	/**
	 * 取仓库信息列表
	 */
	List<Warehouse> getWarehouseList(Map<String, Object> criteria);

	/**
	 * 取所有有效的仓库信息
	 */
	List<Warehouse> getValidWarehouses();

	/**
	 * 取指定的仓库信息
	 */
	Warehouse getWarehouse(Long id);
	
	/**
	 * 根据编号取仓库信息
	 */
	Warehouse getWarehouseByCode(String warehouseCode);
	
	/**
	 * 根据订单来源取仓库信息
	 */
	Warehouse getWarehouseByOrderSource(String orderSource);

	/**
	 * 取默认的仓库信息
	 */
	Warehouse getDefaultWarehouse();

	/**
	 * 添加仓库信息
	 */
	void addWarehouse(Warehouse warehouse) throws ServiceException;

	/**
	 * 更新仓库信息
	 */
	void updateWarehouse(Warehouse warehouse) throws ServiceException;

	/**
	 * 设置指定仓库为默认仓库
	 */
	void updateWarehouseToDefault(Long id) throws ServiceException;

	/**
	 * 停用仓库
	 */
	void disableWarehouse(Long id) throws ServiceException;

	/**
	 * 启用仓库
	 */
	void enableWarehouse(Long id) throws ServiceException;
}
