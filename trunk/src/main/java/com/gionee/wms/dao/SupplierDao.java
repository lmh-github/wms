package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Supplier;

@BatisDao
public interface SupplierDao {
	/**
	 *获取供应商信息列表.
	 */
	List<Supplier> querySupplierList(Map<String, Object> criteria);

	/**
	 * 添加供应商
	 */
	int addSupplier(Supplier supplier);

	/**
	 * 更新供应商
	 */
	int updateSupplier(Supplier supplier);

	/**
	 * 更新所有供应商为非默认状态
	 */
	int updateAllSupplierToUndefault();
}
