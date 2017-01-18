package com.gionee.wms.dao;

import java.util.List;
import java.util.Map;

import com.gionee.wms.entity.ShippingInfo;

/**
 * @=======================================
 * @Description 物流信息
 * @author jay_liang
 * @date 2013-10-9 下午2:42:13
 * @=======================================
 */
@BatisDao
public interface ShippingInfoDao {
	
	List<ShippingInfo> queryShippingInfoByPage(Map<String, Object> criteria);
	
	int queryShippingInfoTotal(Map<String, Object> criteria);
	
	ShippingInfo getShippingInfo(ShippingInfo shippingInfo);
	
	ShippingInfo getShippingInfoById(Long id);

	List<ShippingInfo> getShippingInfoNeedToSub();
	
	int addShippingInfo(ShippingInfo shippingInfo);
	
	int updateShippingInfo(ShippingInfo shippingInfo);
}
