package com.gionee.wms.entity;

import java.util.Date;

public class OperationLog {
	private Long id;
	private String opType;// 操作类型
	private String opKey;// 操作关键标识
	private String handler;// 操作人
	private Date opTime;// 操作时间
	private String opIp;// 操作ip
	private String beforeContenet;// 操作前内容
	private String afterContent;// 操作后内容
	private String remark;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getOpType() {
		return opType;
	}

	public void setOpType(String opType) {
		this.opType = opType;
	}

	public String getOpKey() {
		return opKey;
	}

	public void setOpKey(String opKey) {
		this.opKey = opKey;
	}

	public String getHandler() {
		return handler;
	}

	public void setHandler(String handler) {
		this.handler = handler;
	}

	public Date getOpTime() {
		return opTime;
	}

	public void setOpTime(Date opTime) {
		this.opTime = opTime;
	}

	public String getOpIp() {
		return opIp;
	}

	public void setOpIp(String opIp) {
		this.opIp = opIp;
	}

	public String getBeforeContenet() {
		return beforeContenet;
	}

	public void setBeforeContenet(String beforeContenet) {
		this.beforeContenet = beforeContenet;
	}

	public String getAfterContent() {
		return afterContent;
	}

	public void setAfterContent(String afterContent) {
		this.afterContent = afterContent;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
