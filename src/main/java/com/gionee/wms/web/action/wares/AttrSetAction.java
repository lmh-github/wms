package com.gionee.wms.web.action.wares;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.entity.AttrSet;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.account.AccountService;
import com.gionee.wms.service.wares.AttrSetService;
import com.gionee.wms.web.action.CrudActionSupport;

@Controller("AttrSetAction")
@Scope("prototype")
public class AttrSetAction extends CrudActionSupport<AttrSet> {

	private static final long serialVersionUID = -8940733721506429911L;

	private AttrSetService attrSetService;
	private AccountService accountService;

	/** 页面相关属性 **/
	private List<AttrSet> attrSetList;
	private Long id;
	private AttrSet attrSet;

	/**
	 * 查询分类列表
	 */
	@Override
	public String list() throws Exception {
		attrSetList = attrSetService.getAttrSetList(null);
		return SUCCESS;
	}

	/**
	 * 打开创建或编辑页面
	 */
	@Override
	public String input() throws Exception {
		return INPUT;
	}

	/**
	 * 创建分类
	 */
	@Override
	public String add() throws Exception {
		try {
			attrSetService.addAttrSet(attrSet);
			ajaxSuccess("创建商品类型成功");
		} catch (ServiceException e) {
			logger.error("创建商品类型出错", e);
			ajaxError("创建商品类型失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 更新分类
	 */
	@Override
	public String update() throws Exception {
		try {
			attrSetService.updateAttrSet(attrSet);
			ajaxSuccess("编辑商品类型成功");
		} catch (ServiceException e) {
			logger.error("编辑商品类型时出错", e);
			ajaxError("编辑商品类型失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 删除分类
	 */
	@Override
	public String delete() throws Exception {
		try {
			attrSetService.deleteAttrSet(id);
			ajaxSuccess("商品类型删除成功");
		} catch (ServiceException e) {
			logger.error("商品类型删除时出错", e);
			ajaxError("商品类型删除失败：" + e.getMessage());
		}
		return null;
	}

	// 为add方法准备数据
	@Override
	public void prepareAdd() throws Exception {
		attrSet = new AttrSet();
	}

	// 为input方法准备数据
	@Override
	public void prepareInput() throws Exception {
		prepareModel();
	}

	// 为update方法准备数据
	@Override
	public void prepareUpdate() throws Exception {
		prepareModel();
	}

	private void prepareModel() throws Exception {
		if (id != null) {
			attrSet = attrSetService.getAttrSet(id);
		} else {
			attrSet = new AttrSet();
		}

	}

	// ModelDriven接口方法
	@Override
	public AttrSet getModel() {
		return attrSet;
	}

	// 供页面传值
	public void setId(Long id) {
		this.id = id;
	}

	// 供页面取值
	public List<AttrSet> getAttrSetList() {
		return attrSetList;
	}

	@Autowired
	public void setAttrSetService(AttrSetService attrSetService) {
		this.attrSetService = attrSetService;
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

}
