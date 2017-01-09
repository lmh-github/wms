package com.gionee.wms.web.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ModelDriven;
import com.opensymphony.xwork2.Preparable;

/**
 * 增删改查的抽象基类.
 * 
 * 
 * 
 * @author kevin
 */
public abstract class CrudActionSupport<T> extends AjaxActionSupport implements ModelDriven<T>, Preparable {

	private static final long serialVersionUID = -1653204626115064950L;

	public static final String LIST = "list";

	protected Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 默认的action方法, 默认会调用list方法.
	 */
	@Override
	public String execute() throws Exception {
		return list();
	}

	// -- CRUD 方法 --//
	/**
	 * 显示列表.
	 */
	public abstract String list() throws Exception;

	/**
	 * 显示新增或修改界面.
	 */
	@Override
	public abstract String input() throws Exception;

	/**
	 * 新增或修改.
	 */
	public abstract String add() throws Exception;

	/**
	 * 修改.
	 */
	public abstract String update() throws Exception;

	/**
	 * 删除.
	 */
	public abstract String delete() throws Exception;

	/**
	 * 屏蔽了所有Action方法执行前的预处理.
	 */
	public void prepare() throws Exception {
	}

	/**
	 * execute方法执行前的预处理.
	 */
	public void prepareExecute() throws Exception {
		prepareList();
	}

	/**
	 * list方法执行前的预处理.
	 */
	public void prepareList() throws Exception {

	}

	/**
	 * input方法执行前的预处理.
	 */
	public abstract void prepareInput() throws Exception;

	/**
	 * update方法执行前的预处理.
	 */
	public abstract void prepareUpdate() throws Exception;

	/**
	 * add方法执行前的预处理.
	 */
	public abstract void prepareAdd() throws Exception;
}
