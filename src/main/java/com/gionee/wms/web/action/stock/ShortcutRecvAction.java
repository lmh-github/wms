package com.gionee.wms.web.action.stock;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.IndivFlowType;
import com.gionee.wms.common.WmsConstants.ReceiveType;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.PurPreRecv;
import com.gionee.wms.entity.Receive;
import com.gionee.wms.entity.ReceiveGoods;
import com.gionee.wms.entity.Supplier;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.SupplierService;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.PurchaseService;
import com.gionee.wms.service.stock.ReceiveService;
import com.gionee.wms.service.wares.IndivService;
import com.gionee.wms.service.wares.WaresService;
import com.gionee.wms.web.action.AjaxActionSupport;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.Preparable;

@Controller("ShortcutRecvAction")
@Scope("prototype")
public class ShortcutRecvAction extends AjaxActionSupport implements Preparable {
	private static final long serialVersionUID = 8945106312777211809L;

	@Autowired
	private PurchaseService purchaseService;
	@Autowired
	private WarehouseService warehouseService;
	@Autowired
	private SupplierService supplierService;
	@Autowired
	private ReceiveService receiveService;
	@Autowired
	private IndivService indivService;
	@Autowired
	private WaresService waresService;

	/** 页面相关属性 **/
	private Long id;
	private Long warehouseId;
	private Long supplierId;
	private String receiveCode;
	private String originalCode;
	private Integer handlingStatus;
	private Date preparedTimeBegin;// 制单起始时间
	private Date preparedTimeEnd;// 制单起始时间
	private Receive receive;
	private ReceiveGoods goods;
	private List<Receive> receiveList;
	private List<ReceiveGoods> goodsList;
	private List<IndivFlow> indivList;
	private List<Warehouse> warehouseList;// 仓库列表
	private List<Supplier> supplierList;// 供应商列表
	private Page page = new Page();

	/**
	 * 进入收货单列表界面
	 */
	public String execute() throws Exception {
		// 初始化页面数据
		warehouseList = warehouseService.getValidWarehouses();
		supplierList = supplierService.getValidSuppliers();

		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("warehouseId", warehouseId);
		criteria.put("receiveCode", StringUtils.defaultIfBlank(receiveCode, null));
		criteria.put("originalCode", StringUtils.defaultIfBlank(originalCode, null));
		criteria.put("handlingStatus", handlingStatus);
		criteria.put("receiveType", ReceiveType.PURCHASE.getCode());
		criteria.put("preparedTimeBegin", preparedTimeBegin);
		criteria.put("preparedTimeEnd", preparedTimeEnd != null ? new Date(preparedTimeEnd.getTime() + (24 * 3600 - 1)
				* 1000) : null);
		int totalRow = receiveService.getReceiveTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		receiveList = receiveService.getReceiveList(criteria, page);
		return SUCCESS;
	}

//	/**
//	 * 进入收货商品清单界面
//	 */
//	public String listGoods() throws Exception {
//		Validate.notNull(id);
//		receive = receiveService.getReceive(id);
//		goodsList = receiveService.getReceiveGoodsList(id);
//		return "list_goods";
//	}
	
//	/**
//	 * 进入收货单创建界面
//	 */
//	public String input() throws Exception {
//		warehouseList = warehouseService.getValidWarehouses();
//		supplierList = supplierService.getValidSuppliers();
//		return INPUT;
//	}
	
//	/**
//	 * 创建收货单
//	 */
//	public String add() throws Exception {
//		Validate.notNull(receive.getWarehouseId());
//		Validate.notNull(receive.getSupplierId());
//		try {
//			Warehouse warehouse = warehouseService.getWarehouse(receive.getWarehouseId());
//			Validate.notNull(warehouse);
//			receive.setWarehouseName(warehouse.getWarehouseName());
//			Supplier supplier = supplierService.getSupplier(receive.getSupplierId());
//			Validate.notNull(supplier);
//			receive.setSupplierName(supplier.getSupplierName());
//			receiveService.addShortcutReceive(receive);
//			ajaxSuccess("创建收货单成功");
//		} catch (ServiceException e) {
//			logger.error("创建快捷收货单时出错", e);
//			ajaxError("创建收货单失败：" + e.getMessage());
//		}
//		return null;
//	}

//	/**
//	 * 进入收货商品新增页面
//	 */
//	public String inputGoods() throws Exception {
//		Validate.notNull(id);
//		return "input_goods";
//	}
	
//	/**
//	 * 进入收货商品编辑页面
//	 */
//	public String inputGoods2() throws Exception {
//		Validate.notNull(id);
//		goods = receiveService.getReceiveGoods(id);
//		Validate.notNull(goods);
//		if(WmsConstants.ENABLED_TRUE == goods.getIndivEnabled()){
//			indivList = indivService.getIndivFlowsByFlowItemId(IndivFlowType.IN_PURCHASE.getCode(), id);	
//		}
//		return "input_goods2";
//	}
	
//	/**
//	 * 根据SKU编码查询SKU信息(for ajax)
//	 */
//	public String getSkuInfo() throws Exception {
//		String skuCode = ActionUtils.getRequest().getParameter("skuCode");
//		Validate.notEmpty(skuCode);
//		Sku sku = waresService.getSkuByCode(skuCode);
//		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
//		ActionUtils.outputJson(jsonUtils.toJson(sku));
//		return null;
//	}
	
//	/**
//	 * 添加收货商品
//	 */
//	public String addGoods() throws Exception {
//		Validate.notNull(id);
//		Validate.notNull(goods);
//		receive = receiveService.getReceive(id);
//		Validate.notNull(receive);
//		goods.setReceive(receive);
//		
//		if (WmsConstants.ENABLED_TRUE == goods.getIndivEnabled()) {
//			if (CollectionUtils.isEmpty(indivList)) {
//				ajaxError("添加商品失败：商品编码不能为空");
//				return null;
//			}
//			// 过滤商品个体
//			Iterator<IndivFlow> itr = indivList.iterator();
//			while (itr.hasNext()) {
//				IndivFlow indiv = itr.next();
//				if (indiv == null || StringUtils.isBlank(indiv.getIndivCode())) {
//					itr.remove();
//				} else {
//					indiv.setIndivCode(indiv.getIndivCode().trim());
//				}
//			}
//			try {
//				receiveService.addShortcutRecvGoodsWithIndivList(goods, indivList);
//				ajaxSuccess("添加商品成功");
//			} catch (ServiceException e) {
//				logger.error("快捷收货添加商品时出错", e);
//				ajaxError("添加商品失败：" + e.getMessage());
//			}
//
//		} else {
//			try {
//				receiveService.addShortcutRecvGoods(goods);
//				ajaxSuccess("添加商品成功");
//			} catch (ServiceException e) {
//				logger.error("快捷收货添加商品时出错", e);
//				ajaxError("添加商品失败：" + e.getMessage());
//			}
//
//		}
//		return null;
//	}


//	public String updateGoods() throws Exception {
//		Validate.notNull(goods);
//		if (WmsConstants.ENABLED_TRUE == goods.getIndivEnabled()) {
//			if (CollectionUtils.isEmpty(indivList)) {
//				ajaxError("编辑收货商品失败：商品编码不能为空");
//				return null;
//			}
//			// 过滤商品个体
//			Iterator<IndivFlow> itr = indivList.iterator();
//			while (itr.hasNext()) {
//				IndivFlow indiv = itr.next();
//				if (indiv == null || StringUtils.isBlank(indiv.getIndivCode())) {
//					itr.remove();
//				} else {
//					indiv.setIndivCode(indiv.getIndivCode().trim());
//				}
//			}
//			try {
//				receiveService.updatePurchaseRecvGoodsWithIndivList(goods, indivList);
//				ajaxSuccess("编辑收货商品成功");
//			} catch (ServiceException e) {
//				logger.error("编辑收货商品时出错", e);
//				ajaxError("编辑收货商品失败：" + e.getMessage());
//			}
//
//		} else {
//			try {
//				receiveService.updateReceiveGoods(goods);
//				ajaxSuccess("编辑收货商品成功");
//			} catch (ServiceException e) {
//				logger.error("编辑收货商品时出错", e);
//				ajaxError("编辑收货商品失败：" + e.getMessage());
//			}
//
//		}
//		return null;
//	}
	public String updateGoods_bak() throws Exception {
		Validate.notNull(goods);
		try {
			receiveService.updateReceiveGoods(goods);
			ajaxSuccess("编辑收货商品成功");
		} catch (ServiceException e) {
			logger.error("编辑收货商品时出错", e);
			ajaxError("编辑收货商品失败：" + e.getMessage());
		}
		return null;
	}

	public void prepareUpdateGoods() throws Exception {
		Validate.notNull(id);
		goods = receiveService.getReceiveGoods(id);
	}
	
//	/**
//	 * 删除商品
//	 */
//	public String deleteGoods() throws Exception {
//		Validate.notNull(id);
//		try {
//			receiveService.deleteReceiveGoods(id);
//			ajaxSuccess("删除商品成功");
//		} catch (ServiceException e) {
//			logger.error("删除快捷收货商品时出错", e);
//			ajaxError("删除商品失败：" + e.getMessage());
//		}
//		return null;
//	}

	public void prepare() throws Exception {

	}
	
	/**
	 * 进入收货商品查看页面
	 */
	public String showGoods() throws Exception {
		Validate.notNull(id);
		goods = receiveService.getReceiveGoods(id);
		if (WmsConstants.ENABLED_TRUE == goods.getIndivEnabled()) {
			indivList = indivService.getIndivFlowsByFlowItemId(IndivFlowType.IN_PURCHASE.getCode(), id);
			return "show_indiv";
		}
		return "show_goods";
	}

	/**
	 * 根据采购预收单创建收货单
	 */
	public String createReceive() throws Exception {
		Validate.notNull(id);
		PurPreRecv purPreRecv = purchaseService.getPurPreRecv(id);
		try {
			receiveService.addPurchaseReceiveByPreRecv(purPreRecv);
			ajaxSuccess("创建收货单成功");
		} catch (Exception e) {
			logger.error("创建收货单时出错", e);
			ajaxError("创建收货单失败：" + e.getMessage());
		}
		return null;
	}
	
	public String confirmReceive() throws Exception {
		Validate.notNull(receive);
		try {
			receiveService.confirmPurchaseRecv(receive);
			ajaxSuccess("保存收货单成功，库存已更新");
		} catch (ServiceException e) {
			logger.error("保存收货单时出错", e);
			ajaxError("保存收货单失败：" + e.getMessage());
		}
		return null;
	}

	public void prepareConfirmReceive() throws Exception {
		Validate.notNull(id);
		receive = receiveService.getReceive(id);
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

	public Long getSupplierId() {
		return supplierId;
	}

	public void setSupplierId(Long supplierId) {
		this.supplierId = supplierId;
	}

	public String getOriginalCode() {
		return originalCode;
	}

	public void setOriginalCode(String originalCode) {
		this.originalCode = originalCode;
	}

	public Integer getHandlingStatus() {
		return handlingStatus;
	}

	public void setHandlingStatus(Integer handlingStatus) {
		this.handlingStatus = handlingStatus;
	}

	public Date getPreparedTimeBegin() {
		return preparedTimeBegin;
	}

	public void setPreparedTimeBegin(Date preparedTimeBegin) {
		this.preparedTimeBegin = preparedTimeBegin;
	}

	public Date getPreparedTimeEnd() {
		return preparedTimeEnd;
	}

	public void setPreparedTimeEnd(Date preparedTimeEnd) {
		this.preparedTimeEnd = preparedTimeEnd;
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}

	public List<Receive> getReceiveList() {
		return receiveList;
	}

	public void setGoodsList(List<ReceiveGoods> goodsList) {
		this.goodsList = goodsList;
	}

	public List<Warehouse> getWarehouseList() {
		return warehouseList;
	}

	public void setWarehouseList(List<Warehouse> warehouseList) {
		this.warehouseList = warehouseList;
	}

	public List<Supplier> getSupplierList() {
		return supplierList;
	}

	public void setSupplierList(List<Supplier> supplierList) {
		this.supplierList = supplierList;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public Receive getReceive() {
		return receive;
	}

	public void setReceive(Receive receive) {
		this.receive = receive;
	}

	public List<ReceiveGoods> getGoodsList() {
		return goodsList;
	}

	public ReceiveGoods getGoods() {
		return goods;
	}

	public void setGoods(ReceiveGoods goods) {
		this.goods = goods;
	}

	public List<IndivFlow> getIndivList() {
		return indivList;
	}

	public void setIndivList(List<IndivFlow> indivList) {
		this.indivList = indivList;
	}

}
