/**
* @author jay_liang
* @date 2014-3-25 上午11:00:46
*/
package com.sf.integration.expressservice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-3-25 上午11:00:46
 * @=======================================
 */
public class RouteResponseBody {
	
	private List<RouteResponse> routeResponse;

	@XmlElement(name="RouteResponse", type=RouteResponse.class)
	public List<RouteResponse> getRouteResponse() {
		return routeResponse;
	}

	public void setRouteResponse(List<RouteResponse> routeResponse) {
		this.routeResponse = routeResponse;
	}
}
