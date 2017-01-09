package com.gionee.wms.service.basis;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dao.ShippingDao;
import com.gionee.wms.entity.Shipping;
import com.gionee.wms.service.ServiceException;
import com.google.common.collect.Maps;

@Service("shippingService")
public class ShippingServiceImpl implements ShippingService {
	private ShippingDao shippingDao;

	@Override
	public List<Shipping> getShippingList(Map<String, Object> criteria) {
		try {
			return shippingDao.queryShippingList(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Shipping> getValidShippings() {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("enabled", WmsConstants.ENABLED_TRUE);
		try {
			return shippingDao.queryShippingList(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Shipping getShipping(Long id) {
		if (id == null) {
			throw new ServiceException("参数错误");
		}
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("id", id);
		List<Shipping> list = getShippingList(criteria);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	@Override
	public Shipping getShippingByCode(String shippingCode) {
		if (StringUtils.isBlank(shippingCode)) {
			throw new ServiceException("参数错误");
		}
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("shippingCode", shippingCode);
		List<Shipping> list = getShippingList(criteria);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	@Override
	public Shipping getDefaultShipping() {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("defaultStatus", WmsConstants.ENABLED_TRUE);
		List<Shipping> list = getShippingList(criteria);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	@Override
	public void addShipping(Shipping shipping) throws ServiceException {
		shipping.setCreateTime(new Date());
		if (shipping.getDefaultStatus() == null) {
			shipping.setDefaultStatus(WmsConstants.ENABLED_FALSE);
		}
		if (shipping.getEnabled() == null) {
			shipping.setEnabled(WmsConstants.ENABLED_TRUE);
		}
		// 检测当前是否有设置默认配送方式，没有则设置新的配送方式为默认配送方式
		if (getDefaultShipping() == null) {
			shipping.setDefaultStatus(WmsConstants.ENABLED_TRUE);
		}
		try {
			shippingDao.addShipping(shipping);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateShipping(Shipping shipping) throws ServiceException {
		try {
			shippingDao.updateShipping(shipping);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateShippingToDefault(Long id) throws ServiceException {
		Shipping shipping = getShipping(id);
		if (shipping == null) {
			throw new ServiceException("配送方式不存在");
		} else if (WmsConstants.ENABLED_FALSE == shipping.getEnabled()) {
			throw new ServiceException("配送方式已停用");
		} else if (WmsConstants.ENABLED_TRUE == shipping.getDefaultStatus()) {
			return;
		}

		// 清空现有默认配送方式
		try {
			shippingDao.updateAllShippingToUndefault();
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

		// 设置新的默认配送方式
		shipping.setDefaultStatus(WmsConstants.ENABLED_TRUE);
		try {
			shippingDao.updateShipping(shipping);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void disableShipping(Long id) throws ServiceException {
		Shipping shipping = getShipping(id);
		if (shipping == null) {
			throw new ServiceException("配送方式不存在");
		} else if (WmsConstants.ENABLED_TRUE == shipping.getDefaultStatus()) {
			throw new ServiceException("不能停用默认配送方式");
		} else if (WmsConstants.ENABLED_FALSE == shipping.getEnabled()) {
			return;
		}
		shipping.setEnabled(WmsConstants.ENABLED_FALSE);
		try {
			shippingDao.updateShipping(shipping);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void enableShipping(Long id) throws ServiceException {
		Shipping shipping = getShipping(id);
		if (shipping == null) {
			throw new ServiceException("配送方式不存在");
		} else if (WmsConstants.ENABLED_TRUE == shipping.getEnabled()) {
			return;
		}
		shipping.setEnabled(WmsConstants.ENABLED_TRUE);
		try {
			shippingDao.updateShipping(shipping);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Autowired
	public void setShippingDao(ShippingDao shippingDao) {
		this.shippingDao = shippingDao;
	}

}
