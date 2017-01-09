package com.gionee.wms.service.basis;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.Shipping;
import com.gionee.wms.service.ServiceException;

public interface ShippingService {
	/**
	 * 取配送方式列表
	 */
	List<Shipping> getShippingList(Map<String, Object> criteria);

	/**
	 * 取所有有效的配送方式
	 */
	List<Shipping> getValidShippings();

	/**
	 * 取指定的配送方式
	 */
	Shipping getShipping(Long id);
	
	/**
	 * 根据编号取配送方式
	 */
	Shipping getShippingByCode(String shippingCode);

	/**
	 * 取默认的配送方式
	 */
	Shipping getDefaultShipping();

	/**
	 * 添加配送方式
	 */
	void addShipping(Shipping shipping) throws ServiceException;

	/**
	 * 更新配送方式
	 */
	void updateShipping(Shipping shipping) throws ServiceException;

	/**
	 * 设置指定配送方式为默认配送方式
	 */
	void updateShippingToDefault(Long id) throws ServiceException;

	/**
	 * 停用配送方式
	 */
	void disableShipping(Long id) throws ServiceException;

	/**
	 * 启用配送方式
	 */
	void enableShipping(Long id) throws ServiceException;
}
