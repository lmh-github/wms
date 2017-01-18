package com.gionee.wms.web.action.wares;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.AttrSet;
import com.gionee.wms.entity.Category;
import com.gionee.wms.entity.Wares;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.wares.AttrSetService;
import com.gionee.wms.service.wares.CategoryService;
import com.gionee.wms.service.wares.WaresService;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Maps;

@Controller("WaresAction")
@Scope("prototype")
public class WaresAction extends CrudActionSupport<Wares> {

	private static final long serialVersionUID = -8940733721506429911L;

	private AttrSetService attrSetService;
	private WaresService waresService;
	private CategoryService categoryService;

	/** 页面相关属性 **/
	private List<Wares> waresList;
	private Long id;
	private Wares wares;
	private List<AttrSet> attrSetList;
	private List<Category> categoryList;
	private Page page = new Page();

	/**
	 * 查询分类列表
	 */
	@Override
	public String list() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("catPath", StringUtils.defaultIfBlank(wares.getCategory().getCatPath(), null));
		criteria.put("waresCode", StringUtils.defaultIfBlank(wares.getWaresCode(), null));
		criteria.put("waresName", StringUtils.defaultIfBlank(wares.getWaresName(), null));
		int totalRow = waresService.getWaresListTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		waresList = waresService.getWaresList(criteria, page);
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
			waresService.addWares(wares);
			ajaxSuccess("商品创建成功");
		} catch (ServiceException e) {
			logger.error("商品创建出错", e);
			ajaxError("商品创建失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 更新分类
	 */
	@Override
	public String update() throws Exception {
		try {
			waresService.updateWares(wares);
			ajaxSuccess("商品编辑成功");
		} catch (ServiceException e) {
			logger.error("商品编辑时出错", e);
			ajaxError("商品编辑失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 删除分类
	 */
	@Override
	public String delete() throws Exception {
		try {
			waresService.deleteWares(id);
			ajaxSuccess("商品删除成功");
		} catch (ServiceException e) {
			logger.error("商品删除时出错", e);
			ajaxError("商品删除失败：" + e.getMessage());
		}
		return null;
	}

	@Override
	public void prepareList() throws Exception {
		// 初始化属性对象
		wares = new Wares();
		Category category = new Category();
		wares.setCategory(category);

		// 初始化商品类型列表
		attrSetList = attrSetService.getAttrSetList(null);
		// 初始化商品分类列表
		categoryList = categoryService.getCategoryList(null);
	}

	// 为input方法准备数据
	@Override
	public void prepareInput() throws Exception {
		if (id != null) {
			wares = waresService.getWares(id);
		} else {
			wares = new Wares();
		}
		// 初始化商品类型列表
		attrSetList = attrSetService.getAttrSetList(null);
		// 初始化商品分类列表
		categoryList = categoryService.getCategoryList(null);
	}

	// 为add方法准备数据
	@Override
	public void prepareAdd() throws Exception {
		wares = new Wares();
	}

	// 为update方法准备数据
	@Override
	public void prepareUpdate() throws Exception {
		wares = new Wares();
	}

	// ModelDriven接口方法
	@Override
	public Wares getModel() {
		return wares;
	}

	// 供页面取值
	public Page getPage() {
		if (page == null) {
			page = new Page();
		}
		return page;
	}

	// 供页面传值
	public void setId(Long id) {
		this.id = id;
	}

	// 供页面取值
	public List<Wares> getWaresList() {
		return waresList;
	}

	// 供页面取值
	public List<AttrSet> getAttrSetList() {
		return attrSetList;
	}

	// 供页面取值
	public List<Category> getCategoryList() {
		return categoryList;
	}

	@Autowired
	public void setAttrSetService(AttrSetService attrSetService) {
		this.attrSetService = attrSetService;
	}

	@Autowired
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Autowired
	public void setWaresService(WaresService waresService) {
		this.waresService = waresService;
	}

}
