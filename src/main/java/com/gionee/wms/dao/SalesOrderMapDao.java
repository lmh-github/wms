/**
 * Project Name:wms
 * File Name:SalesOrderMapDao.java
 * Package Name:com.gionee.wms.dao
 * Date:2014年8月29日下午6:25:31
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.dao;

import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderMap;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月29日 下午6:25:31
 */
@BatisDao
public interface SalesOrderMapDao {
	/**
	 * 新增
	 * @param salesOrderMap
	 */
	int add(SalesOrderMap salesOrderMap);

	/**
	 * 根据订单查询
	 * @param orderCode
	 * @return
	 */
	SalesOrderMap getByOrderCode(String orderCode);

	/**
	 * 根据顺丰订单号查询
	 * @param erpOrderCode
	 * @return
	 */
	SalesOrderMap getByErpOrderCode(String erpOrderCode);

	/**
	 * 修改
	 * @param salesOrderMap
	 * @return
	 */
	int update(SalesOrderMap salesOrderMap);

	/**
	 * 根据顺丰ERP Order查询销售订单号
	 * @param erpOrderCode
	 * @return
	 */
	SalesOrder getSalesOrderByErpOrder(String erpOrderCode);
}
