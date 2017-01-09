/**
 * Project Name:wms
 * File Name:SalesOrderMapServiceImpl.java
 * Package Name:com.gionee.wms.service.stock
 * Date:2014年8月29日下午6:45:36
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.service.stock;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gionee.wms.dao.SalesOrderImeiDao;
import com.gionee.wms.dao.SalesOrderMapDao;
import com.gionee.wms.entity.SalesOrderImei;
import com.gionee.wms.entity.SalesOrderMap;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月29日 下午6:45:36
 */
@Service
public class SalesOrderMapServiceImpl implements SalesOrderMapService {

	@Autowired
	private SalesOrderMapDao salesOrderMapDao;

	@Autowired
	private SalesOrderImeiDao salesOrderImeiDao;

	/** {@inheritDoc} */
	@Override
	public int add(SalesOrderMap salesOrderMap) {
		return salesOrderMapDao.add(salesOrderMap);
	}

	/** {@inheritDoc} */
	@Override
	public SalesOrderMap getByOrderCode(String orderCode) {
		return salesOrderMapDao.getByOrderCode(orderCode);
	}

	/** {@inheritDoc} */
	@Override
	public SalesOrderMap getByErpOrderCode(String erpOrderCode) {
		return salesOrderMapDao.getByErpOrderCode(erpOrderCode);
	}

	/** {@inheritDoc} */
	@Override
	public int update(SalesOrderMap salesOrderMap) {
		return salesOrderMapDao.update(salesOrderMap);
	}

	/** {@inheritDoc} */
	@Override
	public int batchAddImes(List<SalesOrderImei> salesOrderImeis) {
		return salesOrderImeiDao.batchAdd(salesOrderImeis);
	}

	/** {@inheritDoc} */
	@Override
	public List<SalesOrderImei> queryAllImeis() {

		return salesOrderImeiDao.queryAllImeis();
	}

	/** {@inheritDoc} */
	@Override
	public List<SalesOrderImei> queryImeis(Map<String, String> criteria) {

		return salesOrderImeiDao.queryImeis(criteria);
	}

}
