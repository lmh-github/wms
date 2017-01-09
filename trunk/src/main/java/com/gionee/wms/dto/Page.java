package com.gionee.wms.dto;

import java.util.Locale;

/**
 * @ClassName: Page
 * @Description: 页面对象 用于列表分页
 * @author kevin
 * @date Jul 22, 2011 4:29:35 PM
 */
public class Page {

	public Page() {
	}

	public Page(int pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * Enumeration for sort directions.
	 * 
	 * @author Oliver Gierke
	 */
	public static enum Direction {

		ASC, DESC;

		/**
		 * Returns the {@link Direction} enum for the given {@link String}
		 * value.
		 * 
		 * @param value
		 * @return
		 */
		public static Direction fromString(String value) {

			try {
				return Direction.valueOf(value.toUpperCase(Locale.US));
			} catch (Exception e) {
				throw new IllegalArgumentException(String.format(
						"Invalid value '%s' for orders given! Has to be either 'desc' or 'asc' (case insensitive).",
						value), e);
			}
		}
	}

	/**
	 * 当前页，默认为1
	 */
	private int currentPage = 1;

	/**
	 * 每页显示的条数,默认为20
	 */
	private int pageSize=20;

	/**
	 * 总记录数
	 */
	private int totalRow;

	/**
	 * 总页数
	 */
	private int totalPage;

	/**
	 * 起始行
	 */
	private int startRow;

	/**
	 * 结束行
	 */
	private int endRow;

	public void setCurrentPage(int currentPage) {
		if (currentPage > totalPage && totalPage != 0) {
			this.currentPage = totalPage;
		} else if (currentPage < 1) {
			this.currentPage = 1;
		} else {
			this.currentPage = currentPage;
		}
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}

	public void setStartRow(int startRow) {
		this.startRow = startRow;
	}

	public void setEndRow(int endRow) {
		this.endRow = endRow;
	}

	public int getTotalRow() {
		return totalRow;
	}

	public void setTotalRow(int totalRow) {
		this.totalRow = totalRow;
	}

	public void setTotalPage() {
		if (totalRow % pageSize != 0) {
			totalPage = totalRow / pageSize + 1;
		} else {
			totalPage = totalRow / pageSize;
		}
	}

	public int getTotalPage() {
		return totalPage;
	}

	public void setStartRow() {
		if (totalPage == 0) {
			startRow = 0;
		} else {
			startRow = (currentPage - 1) * pageSize + 1;
		}
	}

	public int getStartRow() {
		return startRow;
	}

	public void setEndRow() {
		if (totalPage <= 1) {
			endRow = totalRow;
		} else if (currentPage == totalPage) {
			endRow = totalRow;
		} else {
			endRow = (startRow + pageSize) - 1;
		}
	}

	public int getEndRow() {
		return endRow;
	}

	/**
	 * 传入当前页，总记录数，每页显示的条数，计算出起始行和结束行，存放在page对象中
	 * 
	 * @param currentPage 当前页
	 * @param totalRow 总记录数
	 * @param pageSize 每页显示的记录数
	 * @return
	 */
	public static Page getPage(int currentPage, int totalRow, int pageSize) {
		Page page = new Page();
		// 填充当前页
		page.setCurrentPage(currentPage);

		// 填充总记录数
		page.setTotalRow(totalRow);

		// 填充每页显示的条数
		page.setPageSize(pageSize);

		// 计算总页数
		page.setTotalPage();

		// 计算起始行
		page.setStartRow();

		// 计算结束行
		page.setEndRow();

		return page;
	}

	/**
	 * 计算分布数据
	 */
	public void calculate() {
		// 计算总页数
		setTotalPage();
		// 计算起始行
		setStartRow();
		// 计算结束行
		setEndRow();
	}
}
