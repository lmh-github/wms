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
import com.gionee.wms.common.WmsConstants.OrderSource;
import com.gionee.wms.dao.WarehouseDao;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;
import com.google.common.collect.Maps;

@Service("warehouseService")
public class WarehouseServiceImpl implements WarehouseService {
	private WarehouseDao warehouseDao;

	@Override
	public List<Warehouse> getWarehouseList(Map<String, Object> criteria) {
		try {
			return warehouseDao.queryWarehouseList(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public List<Warehouse> getValidWarehouses() {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("enabled", WmsConstants.ENABLED_TRUE);
		try {
			return warehouseDao.queryWarehouseList(criteria);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Warehouse getWarehouse(Long id) {
		Validate.notNull(id);
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("id", id);
		List<Warehouse> list = getWarehouseList(criteria);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	@Override
	public Warehouse getWarehouseByCode(String warehouseCode) {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("warehouseCode", warehouseCode);
		List<Warehouse> list = getWarehouseList(criteria);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	@Override
	public Warehouse getDefaultWarehouse() {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("defaultStatus", WmsConstants.ENABLED_TRUE);
		List<Warehouse> list = getWarehouseList(criteria);
		if (CollectionUtils.isEmpty(list)) {
			return null;
		} else if (list.size() == 1) {
			return list.get(0);
		} else {
			throw new RuntimeException("数据异常");
		}
	}

	@Override
	public void addWarehouse(Warehouse warehouse) throws ServiceException {
		warehouse.setCreateTime(new Date());
		if (warehouse.getDefaultStatus() == null) {
			warehouse.setDefaultStatus(WmsConstants.ENABLED_FALSE);
		}
		if (warehouse.getEnabled() == null) {
			warehouse.setEnabled(WmsConstants.ENABLED_TRUE);
		}
		// 检测当前是否有设置默认仓库，没有则设置新的仓库为默认仓库
		if (getDefaultWarehouse() == null) {
			warehouse.setDefaultStatus(WmsConstants.ENABLED_TRUE);
		}
		try {
			warehouseDao.addWarehouse(warehouse);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateWarehouse(Warehouse warehouse) throws ServiceException {
		try {
			warehouseDao.updateWarehouse(warehouse);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void updateWarehouseToDefault(Long id) throws ServiceException {
		Warehouse warehouse = getWarehouse(id);
		if (warehouse == null) {
			throw new ServiceException("仓库不存在");
		} else if (WmsConstants.ENABLED_FALSE == warehouse.getEnabled()) {
			throw new ServiceException("仓库已停用");
		} else if (WmsConstants.ENABLED_TRUE == warehouse.getDefaultStatus()) {
			return;
		}

		// 清空现有默认仓库
		try {
			warehouseDao.updateAllWarehouseToUndefault();
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}

		// 设置新的默认仓库
		warehouse.setDefaultStatus(WmsConstants.ENABLED_TRUE);
		try {
			warehouseDao.updateWarehouse(warehouse);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void disableWarehouse(Long id) throws ServiceException {
		Warehouse warehouse = getWarehouse(id);
		if (warehouse == null) {
			throw new ServiceException("仓库不存在");
		} else if (WmsConstants.ENABLED_TRUE == warehouse.getDefaultStatus()) {
			throw new ServiceException("不能停用默认仓库");
		} else if (WmsConstants.ENABLED_FALSE == warehouse.getEnabled()) {
			return;
		}
		warehouse.setEnabled(WmsConstants.ENABLED_FALSE);
		try {
			warehouseDao.updateWarehouse(warehouse);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public void enableWarehouse(Long id) throws ServiceException {
		Warehouse warehouse = getWarehouse(id);
		if (warehouse == null) {
			throw new ServiceException("仓库不存在");
		} else if (WmsConstants.ENABLED_TRUE == warehouse.getEnabled()) {
			return;
		}
		warehouse.setEnabled(WmsConstants.ENABLED_TRUE);
		try {
			warehouseDao.updateWarehouse(warehouse);
		} catch (DataAccessException e) {
			throw new ServiceException(e);
		}
	}

	@Override
	public Warehouse getWarehouseByOrderSource(String orderSource) {
		if(null==orderSource) {
			return getDefaultWarehouse();
		}
		if (orderSource.equals(OrderSource.OFFICIAL_IUNI.getCode())) {
			return getWarehouseByCode(WmsConstants.OFFICIAL_IUNI_HOUSE_CODE);
		} else if (orderSource.equals(OrderSource.TMALL_IUNI.getCode())) {
			return getWarehouseByCode(WmsConstants.TMALL_IUNI_HOUSE_CODE);
		} else if (orderSource.equals(OrderSource.VIP_IUNI.getCode())) {
			return getWarehouseByCode(WmsConstants.VIP_IUNI_HOUSE_CODE);
		} else if (orderSource.equals(OrderSource.OFFICIAL_GIONEE.getCode())) {
			return getWarehouseByCode(WmsConstants.OFFICIAL_GIONEE_HOUSE_CODE);
		} else if (orderSource.equals(OrderSource.TMALL_GIONEE.getCode())) {
			return getWarehouseByCode(WmsConstants.TMALL_GIONEE_HOUSE_CODE);
		} else if (orderSource.equals(OrderSource.VIP_GIONEE.getCode())) {
			return getWarehouseByCode(WmsConstants.VIP_GIONEE_HOUSE_CODE);
		} else if (orderSource.equals(OrderSource.TAOBAO_FX_GIONEE)) {
			return getWarehouseByCode(WmsConstants.TAOBAO_FX_GIONEE_HOUSE_CODE);
		} else {
			return getDefaultWarehouse();
		}
	}

	@Autowired
	public void setWarehouseDao(WarehouseDao warehouseDao) {
		this.warehouseDao = warehouseDao;
	}

}
