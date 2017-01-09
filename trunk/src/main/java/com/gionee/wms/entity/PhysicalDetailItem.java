package com.gionee.wms.entity;

public class PhysicalDetailItem {
	private Long id;
	private CheckTask checkTask;// 盘点任务
	private Stock stock;// 库存对象
	private Integer physicalNondefective;// 实盘良品数
	private Integer physicalDefective;// 实盘次品数
	private String remark;// 备注

}
