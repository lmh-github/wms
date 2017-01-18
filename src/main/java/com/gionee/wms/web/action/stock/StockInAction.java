package com.gionee.wms.web.action.stock;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.JsonUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.IndivFlowType;
import com.gionee.wms.common.WmsConstants.ReceiveType;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Category;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.Sku;
import com.gionee.wms.entity.StockIn;
import com.gionee.wms.entity.StockInItem;
import com.gionee.wms.entity.Supplier;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.account.AccountService;
import com.gionee.wms.service.basis.SupplierService;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.common.CommonService;
import com.gionee.wms.service.stock.StockInService;
import com.gionee.wms.service.wares.IndivService;
import com.gionee.wms.service.wares.WaresService;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

@Controller("StockInAction")
@Scope("prototype")
public class StockInAction extends CrudActionSupport<StockIn> {

	private static final long serialVersionUID = -8940733721506429911L;

	private WarehouseService warehouseService;
	private SupplierService supplierService;
	private IndivService indivService;
	private StockInService stockInService;
	private AccountService accountService;
	private WaresService waresService;
	private CommonService commonService;

	/** 页面相关属性 **/
	private Long id;
	private List<StockIn> stockInList; // 入库单列表
	private String stockInCode;// 入库单编码
	private StockIn stockIn;// 入库单model
	private StockInItem stockInItem; // 入库明细项
	private List<StockInItem> stockInDetail;// 入库明细
	private List<IndivFlow> indivList;// 商品身份信息
	private List<Category> categoryList;// 商品分类
	private List<Warehouse> warehouseList;// 仓库列表
	private List<Supplier> supplierList;// 供应商列表
	private Date handledDateBegin;// 入库起始时间
	private Date handledDateEnd;// 入库结束时间
	private ReceiveType[] stockInTypes = ReceiveType.values();// 入库类型枚举
	private Page page = new Page();

	/**
	 * 查询入库单列表
	 */
	@Override
	public String list() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("stockInCode", StringUtils.defaultIfBlank(stockIn.getStockInCode(), null));
		criteria.put("warehouseCode", StringUtils.defaultIfBlank(stockIn.getWarehouse().getWarehouseCode(), null));
		criteria.put("stockInType", StringUtils.defaultIfBlank(stockIn.getStockInType(), null));
		criteria.put("startTime", handledDateBegin);
		criteria.put("endTime", handledDateEnd);
		int totalRow = stockInService.getStockInListTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		stockInList = stockInService.getStockInList(criteria, page);
		return SUCCESS;
	}

	@Override
	public void prepareList() throws Exception {
		// 初始化查询参数对象
		stockIn = new StockIn();
		Warehouse warehouse = new Warehouse();
		stockIn.setWarehouse(warehouse);
		// 初始化页面数据
		warehouseList = warehouseService.getValidWarehouses();
	}

	/**
	 * 根据SKU编码查询SKU信息(for ajax)
	 */
	public String getSkuInfo() throws Exception {
		String skuCode = ActionUtils.getRequest().getParameter("skuCode");
		Validate.notNull(skuCode);
		Sku sku = waresService.getSkuByCode(skuCode);
		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
		ActionUtils.outputJson(jsonUtils.toJson(sku));
		return null;
	}

	@Override
	public String input() throws Exception {
		return null;
	}

	@Override
	public void prepareInput() throws Exception {

	}

	/**
	 * 进入采购入库单新增或编辑界面
	 */
	public String inputPurchaseIn() throws Exception {
		// 如果未生成编号，则重新生成
		if (StringUtils.isBlank(stockIn.getStockInCode())) {
			stockIn.setStockInCode(commonService.getBizCode(CommonService.PURCHASE_IN));
		} else {
			// 传参重复，则只取其中一个
			if (stockIn.getStockInCode().indexOf(",") >= 0) {
				stockIn.setStockInCode(StringUtils.split(stockIn.getStockInCode(), ",")[0]);
			}
		}
		// 初始化页面数据
		warehouseList = warehouseService.getValidWarehouses();
		supplierList = supplierService.getValidSuppliers();
		if (stockIn.getHandledDate() == null) {
			stockIn.setHandledDate(new Date());
		}
		return "input_purchase";
	}

	public void prepareInputPurchaseIn() throws Exception {
		// 初始化Model对象
		if (stockInCode == null) {// 添加
			stockIn = new StockIn();
			stockInDetail = Lists.newArrayList();
		} else {// 编辑
			// 传参重复，则只取其中一个
			if (stockInCode.indexOf(",") >= 0) {
				stockInCode = StringUtils.split(stockInCode, ",")[0];
			}
			stockIn = stockInService.getStockInByCode(stockInCode);
			stockInDetail = stockInService.getStockInDetail(stockIn.getId());
		}
	}

	/**
	 * 进入查看采购入库单界面
	 */
	public String showPurchaseIn() throws Exception {
		return "show_purchase";
	}

	public void prepareShowPurchaseIn() throws Exception {
		Validate.notNull(id);
		// 初始化Model对象
		stockIn = stockInService.getStockIn(id);
		stockInDetail = stockInService.getStockInDetail(id);
	}

	/**
	 * 进入入库明细项编辑页面
	 */
	public String inputInItem() throws Exception {
		if (id != null && WmsConstants.ENABLED_TRUE == stockInItem.getIndivEnabled()) {
			indivList = indivService.getIndivFlowsByFlowItemId(IndivFlowType.IN_PURCHASE.getCode(), id);
		}
		return "input_item";
	}

	public void prepareInputInItem() throws Exception {
		// 初始化Model对象
		if (id != null) {
			stockInItem = stockInService.getStockInItem(id);
		}
	}

	/**
	 * 进入入库明细项查看页面
	 */
	public String showStockInItem() throws Exception {
		return "show_item";
	}

	public void prepareShowStockInItem() throws Exception {
		// 初始化Model对象
		Validate.notNull(id);
		stockInItem = stockInService.getStockInItem(id);
		if (WmsConstants.ENABLED_TRUE == stockInItem.getIndivEnabled()) {
			indivList = indivService.getIndivFlowsByFlowItemId(IndivFlowType.IN_PURCHASE.getCode(), id);
		}
	}

	/**
	 * 更新入库明细项
	 */
	public String updateStockInItem() throws Exception {
		Validate.notNull(id);
		Validate.notNull(stockInItem);
		// 过滤商品个体
		if (CollectionUtils.isNotEmpty(indivList)) {
			Iterator<IndivFlow> itr = indivList.iterator();
			while (itr.hasNext()) {
				IndivFlow indiv = itr.next();
				if (indiv == null || StringUtils.isBlank(indiv.getIndivCode())) {
					itr.remove();
				}
			}
		}
		try {
			if (CollectionUtils.isNotEmpty(indivList)) {// 更新入库明细项及商品身份信息集
				stockInService.updateStockInItemAndIndivs(stockInItem, indivList);
			} else {// 更新入库明细项
				stockInService.updateStockInItem(stockInItem);
			}
			ajaxSuccess("更新入库商品成功");
		} catch (ServiceException e) {
			logger.error("更新入库商品时出错", e);
			ajaxError("更新入库商品失败：" + e.getMessage());
		}
		return null;
	}

	public void prepareUpdateStockInItem() throws Exception {
		Validate.notNull(id);
		stockInItem = stockInService.getStockInItem(id);
	}

	/**
	 * 添加入库明细项
	 */
	public String addStockInItem() throws Exception {
		Validate.notNull(stockInItem);
		// 过滤商品个体
		if (CollectionUtils.isNotEmpty(indivList)) {
			Iterator<IndivFlow> itr = indivList.iterator();
			while (itr.hasNext()) {
				IndivFlow indiv = itr.next();
				if (indiv == null || StringUtils.isBlank(indiv.getIndivCode())) {
					itr.remove();
				}
			}
		}
		try {
			// if (CollectionUtils.isNotEmpty(indivList)) {// 保存入库明细项及商品个体信息
			// stockInService.addStockInItemAndIndivs(stockInItem, indivList);
			// } else {// 保存入库明细项
			// stockInService.addStockInItem(stockInItem);
			// }
			stockInService.addPurchaseInItem(stockInItem, indivList);
			ajaxSuccess("添加入库商品成功");
		} catch (ServiceException e) {
			logger.error("添加入库商品时出错", e);
			ajaxError("添加入库商品失败：" + e.getMessage());
		}
		return null;
	}

	public String deleteStockInItem() throws Exception {
		Validate.notNull(id);
		try {
			stockInService.deleteStockInItem(id);
			ajaxSuccess("删除入库商品成功");
		} catch (ServiceException e) {
			logger.error("删除入库商品时出错", e);
			ajaxError("删除入库商品失败：" + e.getMessage());
		}
		return null;
	}

	@Override
	public String add() throws Exception {
		return null;
	}

	@Override
	public void prepareAdd() throws Exception {
	}

	@Override
	public String update() throws Exception {
		Validate.notNull(stockIn);
		try {
			stockIn.setWarehouse(warehouseService.getWarehouseByCode(stockIn.getWarehouse().getWarehouseCode()));
			stockInService.confirmStockIn(stockIn);
			ajaxSuccess("添加入库单成功，库存已更新");
		} catch (ServiceException e) {
			logger.error("添加入库单时出错", e);
			ajaxError("添加入库单失败：" + e.getMessage());
		}
		return null;
	}

	@Override
	public void prepareUpdate() throws Exception {
		Validate.notNull(stockInCode);
		stockIn = stockInService.getStockInByCode(stockInCode);
	}

	/**
	 * 删除入库单
	 */
	@Override
	public String delete() throws Exception {
		Validate.notNull(id);
		try {
			stockInService.deleteStockIn(id);
			ajaxSuccess("删除入库单成功");
		} catch (ServiceException e) {
			logger.error("删除入库单时出错", e);
			ajaxError("删除入库单失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 取消入库单
	 */
	public String cancel() throws Exception {
		try {
			stockInService.deleteStockIn(stockInCode);
			ajaxSuccess("取消入库单成功");
		} catch (ServiceException e) {
			logger.error("取消入库单时出错", e);
			ajaxError("取消入库单失败：" + e.getMessage());
		}
		return null;
	}

	// ModelDriven接口方法
	@Override
	public StockIn getModel() {
		return stockIn;
	}

	public String getStockInCode() {
		return stockInCode;
	}

	public void setStockInCode(String stockInCode) {
		this.stockInCode = stockInCode;
	}

	public List<IndivFlow> getIndivList() {
		return indivList;
	}

	public void setIndivList(List<IndivFlow> indivList) {
		this.indivList = indivList;
	}

	public List<Supplier> getSupplierList() {
		return supplierList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StockInItem getStockInItem() {
		return stockInItem;
	}

	public void setStockInItem(StockInItem stockInItem) {
		this.stockInItem = stockInItem;
	}

	public Page getPage() {
		if (page == null) {
			page = new Page();
		}
		return page;
	}

	public List<Category> getCategoryList() {
		return categoryList;
	}

	public List<Warehouse> getWarehouseList() {
		return warehouseList;
	}

	public List<StockInItem> getStockInDetail() {
		return stockInDetail;
	}

	public Date getHandledDateBegin() {
		return handledDateBegin;
	}

	public void setHandledDateBegin(Date handledDateBegin) {
		this.handledDateBegin = handledDateBegin;
	}

	public Date getHandledDateEnd() {
		return handledDateEnd;
	}

	public void setHandledDateEnd(Date handledDateEnd) {
		this.handledDateEnd = handledDateEnd;
	}

	public List<StockIn> getStockInList() {
		return stockInList;
	}

	public ReceiveType[] getStockInTypes() {
		return stockInTypes;
	}

	public void setStockInTypes(ReceiveType[] stockInTypes) {
		this.stockInTypes = stockInTypes;
	}

	@Autowired
	public void setIndivService(IndivService indivService) {
		this.indivService = indivService;
	}

	@Autowired
	public void setAccountService(AccountService accountService) {
		this.accountService = accountService;
	}

	@Autowired
	public void setStockInService(StockInService stockInService) {
		this.stockInService = stockInService;
	}

	@Autowired
	public void setWarehouseService(WarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

	@Autowired
	public void setSupplierService(SupplierService supplierService) {
		this.supplierService = supplierService;
	}

	@Autowired
	public void setWaresService(WaresService waresService) {
		this.waresService = waresService;
	}

	@Autowired
	public void setCommonService(CommonService commonService) {
		this.commonService = commonService;
	}

}
