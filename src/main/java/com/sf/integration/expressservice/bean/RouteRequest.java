/**
* @author jay_liang
* @date 2014-3-25 下午3:52:50
*/
package com.sf.integration.expressservice.bean;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-3-25 下午3:52:50
 * @=======================================
 */
public class RouteRequest {

	private Integer trackingType;
	private String trackingNumber;
	private Integer methodType;
	@XmlAttribute(name="tracking_type")
	public Integer getTrackingType() {
		return trackingType;
	}
	public void setTrackingType(Integer trackingType) {
		this.trackingType = trackingType;
	}
	@XmlAttribute(name="tracking_number")
	public String getTrackingNumber() {
		return trackingNumber;
	}
	public void setTrackingNumber(String trackingNumber) {
		this.trackingNumber = trackingNumber;
	}
	@XmlAttribute(name="method_type")
	public Integer getMethodType() {
		return methodType;
	}
	public void setMethodType(Integer methodType) {
		this.methodType = methodType;
	}
}
