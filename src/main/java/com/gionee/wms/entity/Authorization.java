/**
* @author jay_liang
* @date 2014-4-4 下午3:43:21
*/
package com.gionee.wms.entity;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-4-4 下午3:43:21
 * @=======================================
 */
public class Authorization {

	private Long id;
	private String ip;
	private String mac;
	private  Long userName;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public Long getUserName() {
		return userName;
	}
	public void setUserName(Long userName) {
		this.userName = userName;
	}
}
