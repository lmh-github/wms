package com.gionee.wms.entity;

public class CheckStatusException {
	private Long id;// 主键
	private Long checkId;// 主键
	private String indivCode;// 个体商品IMEI编码
	private Integer bookStatus;// 账面个体状态
	private Integer checkStatus;// 实盘个体状态

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getIndivCode() {
		return indivCode;
	}

	public void setIndivCode(String indivCode) {
		this.indivCode = indivCode;
	}

	public Integer getBookStatus() {
		return bookStatus;
	}

	public void setBookStatus(Integer bookStatus) {
		this.bookStatus = bookStatus;
	}

	public Integer getCheckStatus() {
		return checkStatus;
	}

	public void setCheckStatus(Integer checkStatus) {
		this.checkStatus = checkStatus;
	}

	public Long getCheckId() {
		return checkId;
	}

	public void setCheckId(Long checkId) {
		this.checkId = checkId;
	}
}
