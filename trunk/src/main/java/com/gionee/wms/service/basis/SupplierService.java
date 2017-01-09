package com.gionee.wms.service.basis;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Supplier;
import com.gionee.wms.service.ServiceException;

public interface SupplierService {
	/**
	 * 取供应商信息列表
	 */
	List<Supplier> getSupplierList(Map<String, Object> criteria);

	/**
	 * 取所有有效的供应商信息
	 */
	List<Supplier> getValidSuppliers();

	/**
	 * 取指定的供应商信息
	 */
	Supplier getSupplier(Long id);

	/**
	 * 取默认的供应商信息
	 */
	Supplier getDefaultSupplier();
	
	/**
	 * 根据编号取供应商信息
	 */
	Supplier getSupplierByCode(String supplierCode);

	/**
	 * 添加供应商信息
	 */
	void addSupplier(Supplier supplier) throws ServiceException;

	/**
	 * 更新供应商信息
	 */
	void updateSupplier(Supplier supplier) throws ServiceException;

	/**
	 * 设置指定供应商为默认供应商
	 */
	void updateSupplierToDefault(Long id) throws ServiceException;

	/**
	 * 停用供应商
	 */
	void disableSupplier(Long id) throws ServiceException;

	/**
	 * 启用供应商
	 */
	void enableSupplier(Long id) throws ServiceException;
}
