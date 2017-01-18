/**
 * Project Name:wms
 * File Name:ManualReissueOrderServiceImpl.java
 * Package Name:com.gionee.wms.service.stock
 * Date:2016年10月8日下午5:57:32
 * Copyright (c) 2016 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.service.stock;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.wms.dao.ManualReissueOrderDao;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.ManualReissueOrder;
import com.gionee.wms.entity.ManualReissueOrderGoods;
import com.google.common.collect.Maps;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2016年10月8日 下午5:57:32
 */
@Service
public class ManualReissueOrderServiceImpl implements ManualReissueOrderService {

	// private static Logger logger = LoggerFactory.getLogger(ManualReissueOrderServiceImpl.class);

	@Autowired
	private ManualReissueOrderDao manualReissueOrderDao;

	/** {@inheritDoc} */
	@Override
	public int handleAdd(ManualReissueOrder reissueOrder, List<ManualReissueOrderGoods> goods) {
		reissueOrder.setStatus(0);
		manualReissueOrderDao.add(reissueOrder);
		for (ManualReissueOrderGoods g : goods) {
			g.setManualReissueOrderId(reissueOrder.getId());
		}
		manualReissueOrderDao.addGoods(goods);
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public int handleUpdate(ManualReissueOrder reissueOrder) {
		manualReissueOrderDao.update(reissueOrder);
		if (reissueOrder.getGoods() != null && reissueOrder.getGoods().size() > 0) {
			manualReissueOrderDao.deleteGoods(reissueOrder.getId());
			manualReissueOrderDao.addGoods(reissueOrder.getGoods());
		}
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public int handleDelete(Long id) {
		manualReissueOrderDao.delete(id);
		manualReissueOrderDao.deleteGoods(id);
		return 0;
	}

	/** {@inheritDoc} */
	@Override
	public List<Map<String, Object>> query(Map<String, Object> criteria, Page page) {
		if (criteria == null) {
			criteria = Maps.newHashMap();
		}
		criteria.put("page", page);
		return manualReissueOrderDao.query(criteria);
	}

	/** {@inheritDoc} */
	@Override
	public List<Map<String, Object>> queryForExport(Map<String, Object> criteria) {
		return manualReissueOrderDao.queryForExport(criteria);
	}

	/** {@inheritDoc} */
	@Override
	public int queryCount(Map<String, Object> criteria) {
		return manualReissueOrderDao.queryCount(criteria);
	}

	/** {@inheritDoc} */
	@Override
	public List<ManualReissueOrderGoods> queryGoods(Long manualReissueOrderId) {
		return manualReissueOrderDao.queryGoods(manualReissueOrderId);
	}

	/** {@inheritDoc} */
	@Override
	public Map<String, Object> get(Long id) {
		return manualReissueOrderDao.get(id);
	}

}
