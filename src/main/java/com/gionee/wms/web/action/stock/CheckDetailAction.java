package com.gionee.wms.web.action.stock;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Category;
import com.gionee.wms.entity.Check;
import com.gionee.wms.entity.CheckGoods;
import com.gionee.wms.entity.Stock;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.stock.CheckService;
import com.gionee.wms.service.stock.StockService;
import com.gionee.wms.service.wares.CategoryService;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Maps;

@Controller("CheckDetailAction")
@Scope("prototype")
public class CheckDetailAction extends CrudActionSupport<Check> {
	private static final long serialVersionUID = 2566942049805660757L;
	private CheckService stockCheckService;
	private CategoryService categoryService;
	private StockService stockService;

	/** 页面相关属性 **/
	private List<CheckGoods> stockCheckDetail;
	private Long stockCheckId;
	private Check stockCheck;
	private String catPath;
	private String skuCode;
	private String skuName;
	private List<Long> skuIds;
	private Long detailItemId;
	private List<Category> categoryList;// 商品分类
	private List<Stock> stockList;//库存列表
	private Page page = new Page();

	/**
	 * 查询盘点任务列表
	 */
	@Override
	public String list() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("stockCheckId", stockCheckId);
		criteria.put("catPath", StringUtils.defaultIfBlank(catPath, null));
		criteria.put("skuCode", StringUtils.defaultIfBlank(skuCode, null));
		criteria.put("skuName", StringUtils.defaultIfBlank(skuName, null));
		int stockCheckListTotal = stockCheckService.getCheckGoodsTotal(criteria);
		page.setTotalRow(stockCheckListTotal);
		page.calculate();
		criteria.put("page", page);
		stockCheckDetail = stockCheckService.getCheckGoodsList(criteria, page);
		return SUCCESS;
	}
	
	/**
	 * SKU查找并添加到盘点明细
	 */
	@Override
	public String input() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("warehouseCode", StringUtils.defaultIfBlank(stockCheck.getWarehouse().getWarehouseCode(), null));
		criteria.put("catPath", StringUtils.defaultIfBlank(catPath, null));
		criteria.put("skuCode", StringUtils.defaultIfBlank(skuCode, null));
		criteria.put("skuName", StringUtils.defaultIfBlank(skuName, null));
		int totalRow = stockService.getStockListTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		stockList = stockService.getStockList(criteria, page);
		categoryList = categoryService.getCategoryList(null);
		return INPUT;
	}

	/**
	 * 创建盘点单
	 */
	@Override
	public String add() throws Exception {
		//设置制单人
		//		stockCheckItem.setPreparedBy("kevin");
		// stockCheck.setPrepareBy(accountService.getCurrentUser().getLoginName());
		Validate.notNull(stockCheckId);
//		String checkCode = ActionUtils.getRequest().getParameter("checkCode");
		Validate.notNull(skuIds);
		try {
//			stockCheckService.addStockCheckDetail(stockCheckId, skuIds);
			ajaxSuccess("SKU成功添加到盘点明细");
		} catch (ServiceException e) {
			logger.error("SKU添加到盘点明细时出错", e);
			ajaxError("SKU添加到盘点明细失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 删除盘点项
	 */
	@Override
	public String delete() throws Exception {
		Validate.notNull(detailItemId);
		try {
//			stockCheckService.deleteDetailItem(detailItemId);
			ajaxSuccess("删除盘点项成功");
		} catch (ServiceException e) {
			logger.error("删除盘点项时出错", e);
			ajaxError("删除盘点项失败：" + e.getMessage());
		}
		return null;
	}

	@Override
	public void prepareList() throws Exception {
		// 初始化属性对象
		stockCheck = stockCheckService.getCheck(stockCheckId);
		categoryList = categoryService.getCategoryList(null);
	}

	@Override
	public void prepareAdd() throws Exception {
//		if (stockCheck == null) {
//			stockCheck = new StockCheck();
//		}

	}

	// 供页面取值
	public Page getPage() {
		if (page == null) {
			page = new Page();
		}
		return page;
	}

	public String getCatPath() {
		return catPath;
	}

	public String getSkuCode() {
		return skuCode;
	}

	public String getSkuName() {
		return skuName;
	}

	public List<Stock> getStockList() {
		return stockList;
	}

	public Long getStockCheckId() {
		return stockCheckId;
	}
	public List<Long> getSkuIds() {
		return skuIds;
	}
	



	public Long getDetailItemId() {
		return detailItemId;
	}

	public void setDetailItemId(Long detailItemId) {
		this.detailItemId = detailItemId;
	}

	// 供页面传值
	public void setStockCheckId(Long stockCheckId) {
		this.stockCheckId = stockCheckId;
	}

	public void setCatPath(String catPath) {
		this.catPath = catPath;
	}

	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}

	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	
	public void setSkuIds(List<Long> skuIds) {
		this.skuIds = skuIds;
	}
	


	

	public List<Category> getCategoryList() {
		return categoryList;
	}

	@Override
	public Check getModel() {
		return stockCheck;
	}

	public List<CheckGoods> getStockCheckDetail() {
		return stockCheckDetail;
	}

	@Override
	public void prepareInput() throws Exception {
		Validate.notNull(stockCheckId);
		stockCheck = stockCheckService.getCheck(stockCheckId);
	}

	@Override
	public void prepareUpdate() throws Exception {

	}

	@Override
	public String update() throws Exception {
		return null;
	}
	@Autowired
	public void setStockCheckService(CheckService stockCheckService) {
		this.stockCheckService = stockCheckService;
	}

	@Autowired
	public void setCategoryService(CategoryService categoryService) {
		this.categoryService = categoryService;
	}
	@Autowired
	public void setStockService(StockService stockService) {
		this.stockService = stockService;
	}

}
