package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.DeliveryBatch;

@BatisDao
public interface DeliveryBatchDao {
	/**
	 * 分页查询符合条件的发货批次列表.
	 */
	List<DeliveryBatch> queryDeliveryBatchByPage(Map<String, Object> criteria);

	/**
	 * 查询符合条件的发货批次总数
	 */
	int queryDeliveryBatchTotal(Map<String, Object> criteria);

	/**
	 * 根据ID获取发货批次
	 */
	DeliveryBatch queryDeliveryBatch(Long id);

	/**
	 * 添加发货批次
	 */
	int addDeliveryBatch(DeliveryBatch deliveryBatch);

	/**
	 * 更新发货批次
	 */
	int updateDeliveryBatch(DeliveryBatch deliveryBatch);
	
	/**
	 * 删除发货批次
	 */
	int deleteDeliveryBatch(Long id);
}
