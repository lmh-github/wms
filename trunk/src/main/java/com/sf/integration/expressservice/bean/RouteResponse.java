/**
* @author jay_liang
* @date 2014-3-25 上午11:14:09
*/
package com.sf.integration.expressservice.bean;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-3-25 上午11:14:09
 * @=======================================
 */
public class RouteResponse {

	private String mailno;
	private String orderid;
	private List<Route> route;
	@XmlAttribute(name="mailno")
	public String getMailno() {
		return mailno;
	}
	public void setMailno(String mailno) {
		this.mailno = mailno;
	}
	@XmlAttribute(name="orderid")
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	@XmlElement(name="Route", type=Route.class)
	public List<Route> getRoute() {
		return route;
	}
	public void setRoute(List<Route> route) {
		this.route = route;
	}
}
