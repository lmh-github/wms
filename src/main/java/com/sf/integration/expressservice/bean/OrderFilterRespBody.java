/**
* @author jay_liang
* @date 2014-3-31 下午6:16:54
*/
package com.sf.integration.expressservice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-3-31 下午6:16:54
 * @=======================================
 */
public class OrderFilterRespBody {

	private List<OrderFilterResponse> orderFilterResponse;

	@XmlElement(name="OrderFilterResponse", type=OrderFilterResponse.class)
	public List<OrderFilterResponse> getOrderFilterResponse() {
		return orderFilterResponse;
	}

	public void setOrderFilterResponse(List<OrderFilterResponse> orderFilterResponse) {
		this.orderFilterResponse = orderFilterResponse;
	}
}
