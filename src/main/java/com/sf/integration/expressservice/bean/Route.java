/**
* @author jay_liang
* @date 2014-3-25 上午11:20:13
*/
package com.sf.integration.expressservice.bean;

import javax.xml.bind.annotation.XmlAttribute;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-3-25 上午11:20:13
 * @=======================================
 */
public class Route {
	
	private String acceptTime;
	private String acceptAddress;
	private String remark;
	private String opcode;
	
	@XmlAttribute(name="accept_time")
	public String getAcceptTime() {
		return acceptTime;
	}
	public void setAcceptTime(String acceptTime) {
		this.acceptTime = acceptTime;
	}
	@XmlAttribute(name="accept_address")
	public String getAcceptAddress() {
		return acceptAddress;
	}
	public void setAcceptAddress(String acceptAddress) {
		this.acceptAddress = acceptAddress;
	}
	@XmlAttribute(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	@XmlAttribute(name="opcode")
	public String getOpcode() {
		return opcode;
	}
	public void setOpcode(String opcode) {
		this.opcode = opcode;
	}
}
