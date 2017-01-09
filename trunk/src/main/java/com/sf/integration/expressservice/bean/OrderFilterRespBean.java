/**
* @author jay_liang
* @date 2014-3-24 上午10:39:10
*/
package com.sf.integration.expressservice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-3-24 上午10:39:10
 * @=======================================
 */
@XmlRootElement(name = "Response")
public class OrderFilterRespBean {

	private String service;
	private String head;
	private OrderFilterRespBody body;
	private SFError error;
	
	@XmlAttribute(name="service")
	public String getService() {
		return service;
	}
	public void setService(String service) {
		this.service = service;
	}
	@XmlElement(name="Head")
	public String getHead() {
		return head;
	}
	public void setHead(String head) {
		this.head = head;
	}
	@XmlElement(name="ERROR")
	public SFError getError() {
		return error;
	}
	public void setError(SFError error) {
		this.error = error;
	}
	@XmlElement(name="Body")
	public OrderFilterRespBody getBody() {
		return body;
	}
	public void setBody(OrderFilterRespBody body) {
		this.body = body;
	}
}
