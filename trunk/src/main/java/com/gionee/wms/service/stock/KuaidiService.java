/**
* @author jay_liang
* @date 2014-4-20 上午11:41:31
*/
package com.gionee.wms.service.stock;

import com.sf.integration.expressservice.bean.OrderFilterRespBean;
import com.sf.integration.expressservice.bean.RouteRespBean;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-4-20 上午11:41:31
 * @=======================================
 */
public interface KuaidiService {

	/**
	 * 查询顺丰路由接口(xml请参见顺丰说明)
	 */
	public RouteRespBean sfRoute(String xml);
	
	/**
	 * 顺丰筛单接口(xml请参见顺丰说明)
	 */
	public OrderFilterRespBean sfOrderFilter(String xml);
}
