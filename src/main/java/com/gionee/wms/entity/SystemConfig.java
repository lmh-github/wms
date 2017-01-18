/**
 * Project Name:wms
 * File Name:SystemConfig.java
 * Package Name:com.gionee.wms.entity
 * Date:2014年8月20日下午4:39:07
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.entity;

/**
 * 系统配置表Po
 * @author PengBin 00001550<br>
 * @date 2014年8月20日 下午4:39:07
 */
public class SystemConfig {
	private Long id;
	private String key;
	private String value;
	private String comment;
	private Integer status;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param key the key
	 */
	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @return the value
	 */
	public String getValue() {
		return value;
	}

	/**
	 * @param value the value
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * @return the comment
	 */
	public String getComment() {
		return comment;
	}

	/**
	 * @param comment the comment
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "SystemConfig [id=" + id + ", key=" + key + ", value=" + value + ", comment=" + comment + ", status=" + status + "]";
	}

}
