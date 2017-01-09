/**
* @author jay_liang
* @date 2014-3-31 下午6:16:08
*/
package com.sf.integration.expressservice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-3-31 下午6:16:08
 * @=======================================
 */
public class OrderFilterResponse {

	private String orderid;
	private Integer filterResult;
	private String origincode;
	private String destcode;
	private String remark;
	@XmlAttribute(name="orderid")
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	@XmlAttribute(name="filter_result")
	public Integer getFilterResult() {
		return filterResult;
	}
	public void setFilterResult(Integer filterResult) {
		this.filterResult = filterResult;
	}
	@XmlAttribute(name="origincode")
	public String getOrigincode() {
		return origincode;
	}
	public void setOrigincode(String origincode) {
		this.origincode = origincode;
	}
	@XmlAttribute(name="destcode")
	public String getDestcode() {
		return destcode;
	}
	public void setDestcode(String destcode) {
		this.destcode = destcode;
	}
	@XmlAttribute(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
