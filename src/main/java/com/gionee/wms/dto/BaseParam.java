package com.gionee.wms.dto;

import java.util.Date;

/**
 * @ClassName: BaseParam 
 * @Description: 此类用作查询条件的基类 为Param目录下的类的父类 
 */
public class BaseParam {
	
	/**
	 * 排序字段(例taskInfoSeqid).
	 */
	private String sort;
	
	/**
	 * 正序|反序(例DESC|ASC)
	 */
	private String order;
	
	/**
	 * 用于排序起始行,缺省为0
	 */
    private int firstRow = 0;
    
    /**
     * 用于排序结束行，缺省为9999
     */
    private int lastRow = 9999;
    
    /**
     * 当前页
     */
    private int currentPage = 1;
    
    /**
     * 每页显示的条数
     */
	private int pageSize = 10;
	
	/**
	 * 总记录数
	 */
    private int totalRecord = 0;
    
    /**
     * 创建时间
     */
	private Date createTime ;
	
	/**
	 * 修改时间
	 */
    private Date updateTime;

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public int getFirstRow() {
		return firstRow;
	}

	public void setFirstRow(int firstRow) {
		this.firstRow = firstRow;
	}

	public int getLastRow() {
		return lastRow;
	}

	public void setLastRow(int lastRow) {
		this.lastRow = lastRow;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public int getTotalRecord() {
		return totalRecord;
	}

	public void setTotalRecord(int totalRecord) {
		this.totalRecord = totalRecord;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
