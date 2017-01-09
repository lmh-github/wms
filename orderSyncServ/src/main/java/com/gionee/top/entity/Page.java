/**
 * 
 */
package com.gionee.top.entity;

import java.util.List;


/**
 * @author navy zhang
 *
 */
public class Page<T> {
	//当前页
	private int curpage;
	//总页
	private int totalpage;
	//总记录数
	private int count;
	private String retcode;
	private String retdesc;
	private List<T> items;
	
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public List<T> getItems() {
		return items;
	}
	public void setItems(List<T> items) {
		this.items = items;
	}
	public int getCurpage() {
		return curpage;
	}
	public void setCurpage(int curpage) {
		this.curpage = curpage;
	}
	public int getTotalpage() {
		return totalpage;
	}
	public void setTotalpage(int totalpage) {
		this.totalpage = totalpage;
	}
	public String getRetcode()
	{
		return retcode;
	}
	public void setRetcode(String retcode)
	{
		this.retcode = retcode;
	}
	public String getRetdesc()
	{
		return retdesc;
	}
	public void setRetdesc(String retdesc)
	{
		this.retdesc = retdesc;
	}
	
}
