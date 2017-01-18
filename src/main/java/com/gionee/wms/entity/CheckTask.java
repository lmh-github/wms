package com.gionee.wms.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CheckTask {
	private Long id;
	private Check stockCheck;// 盘点单
	private String taskType;// 盘点任务类型 1:预盘 2:复盘
	private String taskUser;// 盘点任务人
	private Date taskDate;// 盘点任务执行日期
	private String taskStatus;// 盘点任务状态 1: 待处理 2:处理中 3:处理完成
	private Date createTime;// 创建时间
	private List<PhysicalDetailItem> physicalDetail = new ArrayList<PhysicalDetailItem>();// 实盘明细
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Check getStockCheck() {
		return stockCheck;
	}
	public void setStockCheck(Check stockCheck) {
		this.stockCheck = stockCheck;
	}
	public String getTaskType() {
		return taskType;
	}
	public void setTaskType(String taskType) {
		this.taskType = taskType;
	}
	public String getTaskUser() {
		return taskUser;
	}
	public void setTaskUser(String taskUser) {
		this.taskUser = taskUser;
	}
	public Date getTaskDate() {
		return taskDate;
	}
	public void setTaskDate(Date taskDate) {
		this.taskDate = taskDate;
	}
	public String getTaskStatus() {
		return taskStatus;
	}
	public void setTaskStatus(String taskStatus) {
		this.taskStatus = taskStatus;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public List<PhysicalDetailItem> getPhysicalDetail() {
		return physicalDetail;
	}
	public void setPhysicalDetail(List<PhysicalDetailItem> physicalDetail) {
		this.physicalDetail = physicalDetail;
	}

}
