package com.gionee.wms.entity;

import java.util.Date;

public class StockCheckTask {
	private Long id;
	private Check stockCheck; // 盘点单
	private String checkUser;// 盘点用户
	private Date checkDate;// 盘点日期
	private Date beginTime;// 盘点开始时间
	private Date endTime;// 盘点结束时间
	private Date createTime;// 任务创建时间
	private String checkStatus;// 任务状态 1:待盘点 2:盘点中 3:盘点结束

}
