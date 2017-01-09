/**
* @author jay_liang
* @date 2014-3-24 上午11:21:25
*/
package com.sf.integration.expressservice.bean;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-3-24 上午11:21:25
 * @=======================================
 */
public class SFError {

	private String code;
	private String value;
	@XmlAttribute(name="code")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	@XmlValue
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
}
