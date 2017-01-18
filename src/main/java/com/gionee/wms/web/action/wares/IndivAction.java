package com.gionee.wms.web.action.wares;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.WmsConstants.IndivStockStatus;
import com.gionee.wms.common.WmsConstants.IndivWaresStatus;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Category;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.wares.CategoryService;
import com.gionee.wms.service.wares.IndivService;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Maps;

/**
 * @ClassName: IndivAction
 * @Description: 商品个体身份信息
 * @author Kevin
 * @date 2013-6-2 下午10:48:23
 * 
 */
@Controller("IndivAction")
@Scope("prototype")
public class IndivAction extends CrudActionSupport<Indiv> {
	private static final long serialVersionUID = -5550025051973418931L;

	private IndivService indivService;
	private WarehouseService warehouseService;
	private CategoryService categoryService;

	/** 页面相关属性 **/
	private List<Indiv> indivList; // 库存列表
	private Indiv indiv;// model
	private Long id;
	private Long warehouseId;
	private Long skuId;
	private String catPath;// 商品分类
	private String skuCode;
	private String skuName;
	private List<Warehouse> warehouseList;// 仓库列表
	private List<Category> categoryList;// 商品分类
	private Date stockInTimeBegin;// 入库起始时间
	private Date stockInTimeEnd;// 入库结束时间
	private IndivWaresStatus[] indivWaresStatus = IndivWaresStatus.values();// 个体商品状态枚举
	private IndivStockStatus[] indivStockStatus = IndivStockStatus.values();// 个体库存状态枚举
	private Page page = new Page();

	@Override
	public String list() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("indivCode", StringUtils.defaultIfBlank(indiv.getIndivCode(), null));
		criteria.put("warehouseId", warehouseId);
		criteria.put("skuId", skuId);
		criteria.put("skuCode", StringUtils.defaultIfBlank(skuCode, null));
		criteria.put("skuName", StringUtils.defaultIfBlank(skuName, null));
		criteria.put("waresStatus", indiv.getWaresStatus());
		criteria.put("stockStatus", indiv.getStockStatus());
		criteria.put("stockInTimeBegin", stockInTimeBegin);
		criteria.put("stockInTimeEnd", stockInTimeEnd);
		int totalRow = indivService.getIndivListTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		indivList = indivService.getIndivList(criteria, page);
		return SUCCESS;
	}

	@Override
	public void prepareList() throws Exception {
		// 初始化属性对象
		indiv = new Indiv();
		categoryList = categoryService.getCategoryList(null);
		warehouseList = warehouseService.getValidWarehouses();
	}

	@Override
	public String input() throws Exception {
		return INPUT;
	}

	@Override
	public void prepareInput() throws Exception {
		if (id != null) {
			indiv = indivService.getIndiv(id);
		} else {
			indiv = new Indiv();
		}
	}

	@Override
	public String update() throws Exception {
		try {
			indivService.updateIndiv(indiv);
			ajaxSuccess("修改商品状态成功");
		} catch (ServiceException e) {
			logger.error("修改商品状态出错", e);
			ajaxError("修改商品状态失败：" + e.getMessage());
		}
		return null;
	}
	
	public String updateStockStatus() throws Exception {
		try {
			System.out.println(indiv.getId()+"\t"+indiv.getStockStatus());
			indivService.updateIndivStockStatus(indiv);
			ajaxSuccess("修改库存状态成功");
		} catch (ServiceException e) {
			logger.error("修改库存状态出错", e);
			ajaxError("修改库存状态失败：" + e.getMessage());
		}
		return null;
	}

	@Override
	public void prepareUpdate() throws Exception {
		indiv = new Indiv();

	}

	@Override
	public String add() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String delete() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void prepareAdd() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public Indiv getModel() {
		return indiv;
	}

	public Indiv getIndiv() {
		return indiv;
	}

	public void setIndiv(Indiv indiv) {
		this.indiv = indiv;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Long getSkuId() {
		return skuId;
	}

	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}

	public String getCatPath() {
		return catPath;
	}

	public void setCatPath(String catPath) {
		this.catPath = catPath;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public String getSkuName() {
		return skuName;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}

	public Date getStockInTimeBegin() {
		return stockInTimeBegin;
	}

	public void setStockInTimeBegin(Date stockInTimeBegin) {
		this.stockInTimeBegin = stockInTimeBegin;
	}

	public Date getStockInTimeEnd() {
		return stockInTimeEnd;
	}

	public void setStockInTimeEnd(Date stockInTimeEnd) {
		this.stockInTimeEnd = stockInTimeEnd;
	}

	public List<Indiv> getIndivList() {
		return indivList;
	}

	public List<Warehouse> getWarehouseList() {
		return warehouseList;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public Page getPage() {
		return page;
	}

	@Autowired
	public void setIndivService(IndivService indivService) {
		this.indivService = indivService;
	}

	@Autowired
	public void setWarehouseService(WarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

	@Autowired
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}

}
