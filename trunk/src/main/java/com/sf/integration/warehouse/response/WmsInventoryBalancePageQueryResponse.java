package com.sf.integration.warehouse.response;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 库存查询接口(分页查询)<br>
 * 返回报文
 * @author PengBin 00001550<br>
 * @date 2014年8月20日 下午1:45:22
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "wmsInventoryBalancePageQueryResponse")
public class WmsInventoryBalancePageQueryResponse extends WmsResponse {

	/** 总数据量 */
	@XmlElement(name = "total_size")
	protected Integer totalSize;

	/** 总页数 */
	@XmlElement(name = "total_page")
	protected Integer totalPage;

	/** 当前页 */
	@XmlElement(name = "page_index")
	protected Integer pageIndex;

	/** 每页数据量 */
	@XmlElement(name = "page_size")
	protected Integer pageSize;

	/** 当前页数据量 */
	@XmlElement(name = "current_page_size")
	protected int currentPageSize;

	/** Item */
	@XmlElementWrapper(name = "list")
	@XmlElements(value = { @XmlElement(name = "item", type = WmsInventoryBalancePageQueryResponseItem.class) })
	protected List<WmsInventoryBalancePageQueryResponseItem> list;

	/**
	 * @return the totalSize
	 */
	public Integer getTotalSize() {
		return totalSize;
	}

	/**
	 * @param totalSize the totalSize
	 */
	public void setTotalSize(Integer totalSize) {
		this.totalSize = totalSize;
	}

	/**
	 * @return the totalPage
	 */
	public Integer getTotalPage() {
		return totalPage;
	}

	/**
	 * @param totalPage the totalPage
	 */
	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	/**
	 * @return the pageIndex
	 */
	public Integer getPageIndex() {
		return pageIndex;
	}

	/**
	 * @param pageIndex the pageIndex
	 */
	public void setPageIndex(Integer pageIndex) {
		this.pageIndex = pageIndex;
	}

	/**
	 * @return the pageSize
	 */
	public Integer getPageSize() {
		return pageSize;
	}

	/**
	 * @param pageSize the pageSize
	 */
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	/**
	 * @return the currentPageSize
	 */
	public int getCurrentPageSize() {
		return currentPageSize;
	}

	/**
	 * @param currentPageSize the currentPageSize
	 */
	public void setCurrentPageSize(int currentPageSize) {
		this.currentPageSize = currentPageSize;
	}

	/**
	 * @return the list
	 */
	public List<WmsInventoryBalancePageQueryResponseItem> getList() {
		return list;
	}

	/**
	 * @param list the list
	 */
	public void setList(List<WmsInventoryBalancePageQueryResponseItem> list) {
		this.list = list;
	}

	/** {@inheritDoc} */
	@Override
	public String toString() {
		return "WmsInventoryBalancePageQueryResponse [totalSize=" + totalSize + ", totalPage=" + totalPage + ", pageIndex=" + pageIndex + ", pageSize=" + pageSize + ", currentPageSize=" + currentPageSize + ", list=" + list + "]";
	}

}
