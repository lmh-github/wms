package com.gionee.wms.web.action.wares;

import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Category;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.entity.SkuBomDetail;
import com.gionee.wms.entity.Wares;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.account.AccountService;
import com.gionee.wms.service.stock.StockService;
import com.gionee.wms.service.wares.CategoryService;
import com.gionee.wms.service.wares.WaresService;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;

@Controller("SkuAction")
@Scope("prototype")
public class SkuAction extends CrudActionSupport<Sku> {

	private static final long serialVersionUID = -8940733721506429911L;

	private WaresService waresService;
	private CategoryService categoryService;
	private StockService stockService;
	private AccountService accountService;

	/** 页面相关属性 **/
	private List<Sku> skuList;
	private Long id;
	private Integer selectEnabled;// 是否允许页面select可用
	private Boolean editEnabled;// 是否允许页面数据编辑
	private Sku sku;
	private List<SkuBomDetail> skuBomList;
	private List<Category> categorys;// 商品分类
	private List<Wares> waresList;// 商品列表
	private Wares waresAttrInfo; // 商品属性信息
	private List<Integer> itemIdList;// SKU绑定的属性值
	private Page page = new Page();

	/**
	 * 查询SKU列表
	 */
	@Override
	public String list() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("catPath", StringUtils.defaultIfBlank(sku.getWares().getCategory().getCatPath(), null));
		criteria.put("skuCode", StringUtils.defaultIfBlank(sku.getSkuCode(), null));
		criteria.put("skuName", StringUtils.defaultIfBlank(sku.getSkuName(), null));
		criteria.put("materialCode", StringUtils.defaultIfBlank(sku.getMaterialCode(), null));
		criteria.put("waresId", sku.getWares().getId());

		int totalRow = waresService.getSkuWithAttrListTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		skuList = waresService.getSkuWithAttrList(criteria, page);
		categorys = categoryService.getCategoryList(null);
		return SUCCESS;
	}

	/**
	 * SKU查找带回
	 */
	public String lookup() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("catPath", StringUtils.defaultIfBlank(sku.getWares().getCategory().getCatPath(), null));
		criteria.put("skuCode", StringUtils.defaultIfBlank(sku.getSkuCode(), null));
		criteria.put("skuName", StringUtils.defaultIfBlank(sku.getSkuName(), null));
		criteria.put("waresId", sku.getWares().getId());
		criteria.put("indivEnabled", sku.getWares().getIndivEnabled());
		// int totalRow = waresService.getSkuWithAttrListTotal(criteria);
		int totalRow = waresService.getSkuListTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		// skuList = waresService.getSkuWithAttrList(criteria, page);
		skuList = waresService.getSkuList(criteria, page);
		// totalRow = waresService.getSkuListTotal(criteria);
		// page.setTotalRow(totalRow);
		categorys = categoryService.getCategoryList(null);
		return "lookup";
	}

	/**
	 * 打开创建或编辑页面
	 */
	@Override
	public String input() throws Exception {
		// 初始化商品属性信息
		if (sku.getWares().getId() != null) {
			waresAttrInfo = waresService.getWaresWithAttrInfo(sku.getWares().getId());
		}
		// 如果为编辑操作，需要判断数据是否允许编辑
		if (id != null) {
			editEnabled = CollectionUtils.isEmpty(stockService.getStockList(id));
		}
		return INPUT;
	}

	/**
	 * 添加SKU
	 */
	@Override
	public String add() throws Exception {
		if (CollectionUtils.isNotEmpty(itemIdList)) {
			for (Integer itemId : itemIdList) {
				if (itemId == null) {
					logger.error("添加SKU时出错：属性值不能为空");
					ajaxError("添加SKU失败：属性值不能为空");
					return null;
				}
			}
			// sku.setItemIds(StringUtils.join(itemIdList, ","));
			sku.setItemIds(Joiner.on(",").skipNulls().join(itemIdList));
		} else {
			sku.setItemIds("");
		}
		try {
			sku.setSkuCode(StringUtils.trimToNull(sku.getSkuCode()));
			sku.setMaterialCode(StringUtils.trimToNull(sku.getMaterialCode()));
			waresService.addSku(sku);
			ajaxSuccess("添加SKU成功");
		} catch (ServiceException e) {
			logger.error(e.getMessage(), e);
			ajaxError("添加SKU失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 编辑SKU
	 */
	@Override
	public String update() throws Exception {
		// if (!accountService.isPermitted(PermissionConstants.CAT_EDIT)) {
		// throw new AccessException();
		// }
		try {
			sku.setSkuCode(StringUtils.trimToNull(sku.getSkuCode()));
			sku.setMaterialCode(StringUtils.trimToNull(sku.getMaterialCode()));
			sku.setSkuBomList(skuBomList);
			waresService.updateSku(sku);
			ajaxSuccess("SKU编辑成功");
		} catch (ServiceException e) {
			logger.error("SKU编辑时出错", e);
			ajaxError("SKU编辑失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 删除SKU
	 */
	@Override
	public String delete() throws Exception {
		// if (!accountService.isPermitted(PermissionConstants.CAT_DELETE)) {
		// throw new AccessException();
		// }
		try {
			waresService.deleteSku(id);
			ajaxSuccess("SKU删除成功");
		} catch (ServiceException e) {
			logger.error("SKU删除时出错", e);
			ajaxError("SKU删除失败：" + e.getMessage());
		}
		return null;
	}

	// 为add方法准备数据
	@Override
	public void prepareAdd() throws Exception {
		if (sku == null) {
			sku = new Sku();
		}
	}

	// 为input方法准备数据
	@Override
	public void prepareInput() throws Exception {
		// 初始化Model对象
		if (id != null) {
			sku = waresService.getSku(id);
		} else {
			sku = new Sku();
			Wares wares = new Wares();
			sku.setWares(wares);
		}
		// 初始化商品列表
		waresList = waresService.getWaresList(null);

		editEnabled = true;
	}

	// 为update方法准备数据
	@Override
	public void prepareUpdate() throws Exception {
		prepareModel();
	}

	private void prepareModel() throws Exception {
		if (id != null) {
			sku = waresService.getSku(id);
		} else {
			sku = new Sku();
		}

	}

	// ModelDriven接口方法
	@Override
	public Sku getModel() {
		if (sku == null) {
			sku = new Sku();
			Wares wares = new Wares();
			Category cat = new Category();
			wares.setCategory(cat);
			sku.setWares(wares);
		}
		return sku;
	}

	// -- 供页面传值 --
	public void setId(Long id) {
		this.id = id;
	}

	public void setSelectEnabled(Integer selectEnabled) {
		this.selectEnabled = selectEnabled;
	}

	public void setEditEnabled(Boolean editEnabled) {
		this.editEnabled = editEnabled;
	}

	// --供页面取值--
	public List<Sku> getSkuList() {
		return skuList;
	}

	public List<Category> getCategorys() {
		return categorys;
	}

	public List<Wares> getWaresList() {
		return waresList;
	}

	public Wares getWaresAttrInfo() {
		return waresAttrInfo;
	}

	public Page getPage() {
		if (page == null) {
			page = new Page();
		}
		return page;
	}

	public Integer getSelectEnabled() {
		return selectEnabled;
	}

	public Boolean getEditEnabled() {
		return editEnabled;
	}

	public List<Integer> getItemIdList() {
		return itemIdList;
	}

	public void setItemIdList(List<Integer> itemIdList) {
		this.itemIdList = itemIdList;
	}

	@Autowired
	public void setWaresService(WaresService waresService) {
		this.waresService = waresService;
	}

	@Autowired
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Autowired
	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

	/**
	 * @return the skuBomList
	 */
	public List<SkuBomDetail> getSkuBomList() {
		return skuBomList;
	}

	/**
	 * @param skuBomList the skuBomList
	 */
	public void setSkuBomList(List<SkuBomDetail> skuBomList) {
		this.skuBomList = skuBomList;
	}

}
