package com.gionee.wms.web.action.wares;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.entity.Category;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.account.AccountService;
import com.gionee.wms.service.wares.CategoryService;
import com.gionee.wms.web.action.CrudActionSupport;

@Controller("CategoryAction")
@Scope("prototype")
public class CategoryAction extends CrudActionSupport<Category> {

	private static final long serialVersionUID = -8940733721506429911L;

	private CategoryService categoryService;
	private AccountService accountService;

	/** 页面相关属性 **/
	private List<Category> categoryList;
	private Long id;
	private Category category;

	/**
	 * 查询分类列表
	 */
	@Override
	public String list() throws Exception {
		categoryList = categoryService.getCategoryList(null);
		return SUCCESS;
	}

	/**
	 * 打开创建或编辑页面
	 */
	@Override
	public String input() throws Exception {
		categoryList = categoryService.getCategoryList(null);
		return INPUT;
	}

	/**
	 * 创建分类
	 */
	@Override
	public String add() throws Exception {
//		if (!accountService.isPermitted(PermissionConstants.CAT_ADD)) {
//			throw new AccessException();
//		}
		try {
			categoryService.addCategory(category);
			ajaxSuccess("创建分类成功");
		} catch (ServiceException e) {
			logger.error("创建分类时出错", e);
			ajaxError("创建分类失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 更新分类
	 */
	@Override
	public String update() throws Exception {
//		if (!accountService.isPermitted(PermissionConstants.CAT_EDIT)) {
//			throw new AccessException();
//		}
		try {
			categoryService.updateCategory(category);
			ajaxSuccess("编辑分类成功");
		} catch (ServiceException e) {
			logger.error("编辑分类时出错", e);
			ajaxError("编辑分类失败：" + e.getMessage());
		}
		
		return null;
	}

	/**
	 * 删除分类
	 */
	@Override
	public String delete() throws Exception {
//		if (!accountService.isPermitted(PermissionConstants.CAT_DELETE)) {
//			throw new AccessException();
//		}
		try {
			categoryService.deleteCategory(id);
			ajaxSuccess("删除分类成功");
		} catch (ServiceException e) {
			logger.error("删除分类时出错", e);
			ajaxError("删除分类失败：" + e.getMessage());
		}
		return null;
	}

	// 为add方法准备数据
	@Override
	public void prepareAdd() throws Exception {
		if (category == null) {
			category = new Category();
		}
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
			category = categoryService.getCategory(id);
		} else {
			category = new Category();
		}

	}

	// ModelDriven接口方法
	@Override
	public Category getModel() {
		return category;
	}

	// 供页面传值
	public void setId(Long id) {
		this.id = id;
	}

	// 供页面取值
	public List<Category> getCategoryList() {
		return categoryList;
	}

	@Autowired
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

}
