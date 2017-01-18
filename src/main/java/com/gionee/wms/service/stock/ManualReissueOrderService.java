/**
 * Project Name:wms
 * File Name:ManualReissueOrderService.java
 * Package Name:com.gionee.wms.service.stock
 * Date:2016年10月8日下午5:53:44
 * Copyright (c) 2016 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.service.stock;

import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.ManualReissueOrder;
import com.gionee.wms.entity.ManualReissueOrderGoods;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2016年10月8日 下午5:53:44
 */
public interface ManualReissueOrderService {

	int handleAdd(ManualReissueOrder reissueOrder, List<ManualReissueOrderGoods> goods);

	int handleUpdate(ManualReissueOrder reissueOrder);

	int handleDelete(Long id);

	List<Map<String, Object>> query(Map<String, Object> criteria, Page page);

	List<Map<String, Object>> queryForExport(Map<String, Object> criteria);

	int queryCount(Map<String, Object> criteria);

	List<ManualReissueOrderGoods> queryGoods(Long manualReissueOrderId);

	Map<String, Object> get(Long id);
}
