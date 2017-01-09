package com.gionee.wms.service.basis;

import java.util.List;
import java.util.Map;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.ShippingInfo;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2013-10-10 上午10:43:41
 * @=======================================
 */
public interface ShippingInfoService {
	
	/**
	 * 分页取快递信息
	 */
	List<ShippingInfo> queryShippingInfoList(Map<String, Object> criteria, Page page);
	
	/**
	 * 快递信息总数
	 */
	Integer queryShippingInfoTotal(Map<String, Object> criteria);
	
	List<ShippingInfo> getShippingInfoNeedToSub();
	
	int updateShippingInfo(ShippingInfo shippingInfo);
	
	int addShippingInfo(ShippingInfo shippingInfo);
	
	ShippingInfo getShippingInfo(String company, String shippingNo);
	
	ShippingInfo getShippingInfoById(Long id);
	
	int updateShippingAndOrder(ShippingInfo shippingInfo);

}
