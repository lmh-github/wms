package com.gionee.wms.entity;

import java.util.Date;

public class Log {

	private Integer id;
	private Integer type;
	private String opName;
	private String content;
	private String ip;
	private String opUserId;
	private String opUserName;
	private Date opTime;

	private String shortContent;// 截取部分内容

	public Log() {
		super();
	}

	public Log(Integer type, String opName, String content, String opUserName, Date opTime) {
		this.type = type;
		this.opName = opName;
		this.content = content;
		this.opUserName = opUserName;
		this.opTime = opTime;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public String getOpName() {
		return opName;
	}

	public void setOpName(String opName) {
		this.opName = opName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content == null ? null : content.trim();
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip == null ? null : ip.trim();
	}

	public String getOpUserId() {
		return opUserId;
	}

	public void setOpUserId(String opUserId) {
		this.opUserId = opUserId == null ? null : opUserId.trim();
	}

	public String getOpUserName() {
		return opUserName;
	}

	public void setOpUserName(String opUserName) {
		this.opUserName = opUserName == null ? null : opUserName.trim();
	}

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public String getShortContent() {
		return shortContent;
	}

	public void setShortContent(String shortContent) {
		this.shortContent = shortContent;
	}
}