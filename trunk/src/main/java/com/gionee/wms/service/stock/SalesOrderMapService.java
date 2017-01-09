/**
 * Project Name:wms
 * File Name:SalesOrderMapService.java
 * Package Name:com.gionee.wms.service.stock
 * Date:2014年8月29日下午6:45:02
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.service.stock;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.SalesOrderImei;
import com.gionee.wms.entity.SalesOrderMap;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2014年8月29日 下午6:45:02
 */
public interface SalesOrderMapService {
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
	 * @param orderCode
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
	 * 批量添加订单串号
	 * @param salesOrderImeis
	 * @return
	 */
	int batchAddImes(List<SalesOrderImei> salesOrderImeis);

	/**
	 * 查詢所有串号
	 * @return
	 */
	List<SalesOrderImei> queryAllImeis();

	/**
	 * 条件查询串号
	 * @param criteria<br>
	 * <code>
	 * <ul>
	 * <li>criteria.put("order_code") ==>订单号</li>
	 * <li>criteria.put("sf_erp_order") ==>顺丰回传订单号</li>
	 * </ul>
	 * </code>
	 * @return
	 */
	List<SalesOrderImei> queryImeis(Map<String, String> criteria);
}
