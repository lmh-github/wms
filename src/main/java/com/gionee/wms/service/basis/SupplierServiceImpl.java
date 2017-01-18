package com.gionee.wms.service.basis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dao.SupplierDao;
import com.gionee.wms.entity.Supplier;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;
import com.google.common.collect.Maps;

@Service("supplierService")
public class SupplierServiceImpl implements SupplierService {
	private SupplierDao supplierDao;

	@Override
	public List<Supplier> getSupplierList(Map<String, Object> criteria) {
		try {
			return supplierDao.querySupplierList(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Supplier> getValidSuppliers() {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("enabled", WmsConstants.ENABLED_TRUE);
		try {
			return supplierDao.querySupplierList(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Supplier getSupplier(Long id) {
		Validate.notNull(id);
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("id", id);
		List<Supplier> list = getSupplierList(criteria);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	@Override
	public Supplier getDefaultSupplier() {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("defaultStatus", WmsConstants.ENABLED_TRUE);
		List<Supplier> list = getSupplierList(criteria);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}
	
	@Override
	public Supplier getSupplierByCode(String supplierCode) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("supplierCode", supplierCode);
		List<Supplier> list = getSupplierList(criteria);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	@Override
	public void addSupplier(Supplier supplier) throws ServiceException {
		supplier.setCreateTime(new Date());
		if (supplier.getDefaultStatus() == null) {
			supplier.setDefaultStatus(WmsConstants.ENABLED_FALSE);
		}
		if (supplier.getEnabled() == null) {
			supplier.setEnabled(WmsConstants.ENABLED_TRUE);
		}
		// 检测当前是否有设置默认供应商，没有则设置新的供应商为默认供应商
		if (getDefaultSupplier() == null) {
			supplier.setDefaultStatus(WmsConstants.ENABLED_TRUE);
		}
		try {
			supplierDao.addSupplier(supplier);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateSupplier(Supplier supplier) throws ServiceException {
		try {
			supplierDao.updateSupplier(supplier);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateSupplierToDefault(Long id) throws ServiceException {
		Supplier supplier = getSupplier(id);
		if (supplier == null) {
			throw new ServiceException("供应商不存在");
		} else if (WmsConstants.ENABLED_FALSE == supplier.getEnabled()) {
			throw new ServiceException("供应商已停用");
		} else if (WmsConstants.ENABLED_TRUE == supplier.getDefaultStatus()) {
			return;
		}

		// 清空现有默认供应商
		try {
			supplierDao.updateAllSupplierToUndefault();
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

		// 设置新的默认供应商
		supplier.setDefaultStatus(WmsConstants.ENABLED_TRUE);
		try {
			supplierDao.updateSupplier(supplier);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void disableSupplier(Long id) throws ServiceException {
		Supplier supplier = getSupplier(id);
		if (supplier == null) {
			throw new ServiceException("供应商不存在");
		} else if (WmsConstants.ENABLED_TRUE == supplier.getDefaultStatus()) {
			throw new ServiceException("不能停用默认供应商");
		} else if (WmsConstants.ENABLED_FALSE == supplier.getEnabled()) {
			return;
		}
		supplier.setEnabled(WmsConstants.ENABLED_FALSE);
		try {
			supplierDao.updateSupplier(supplier);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void enableSupplier(Long id) throws ServiceException {
		Supplier supplier = getSupplier(id);
		if (supplier == null) {
			throw new ServiceException("供应商不存在");
		} else if (WmsConstants.ENABLED_TRUE == supplier.getEnabled()) {
			return;
		}
		supplier.setEnabled(WmsConstants.ENABLED_TRUE);
		try {
			supplierDao.updateSupplier(supplier);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Autowired
	public void setSupplierDao(SupplierDao supplierDao) {
		this.supplierDao = supplierDao;
	}

}
