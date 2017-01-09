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
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.Receive;
import com.gionee.wms.entity.ReceiveGoods;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.entity.Supplier;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.SupplierService;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.PurchaseService;
import com.gionee.wms.service.stock.ReceiveService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.gionee.wms.service.wares.IndivService;
import com.gionee.wms.web.action.AjaxActionSupport;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.Preparable;

/**
 * RMA：Return Material Authorization(退货授权)
 * 具体可参考：http://baike.baidu.com/view/355639.htm
 * 
 * @author kevin
 */
@Controller("RmaRecvAction")
@Scope("prototype")
public class RmaRecvAction extends AjaxActionSupport implements Preparable {

	private static final long serialVersionUID = -8940733721506429911L;

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
	private SalesOrderService salesOrderService;

	/** 页面相关属性 **/
	private Long id;
	private Long warehouseId;
	private Long supplierId;
	private String receiveCode;
	private String purchaseCode;
	private Integer handlingStatus;
	private Date preparedTimeBegin;// 制单起始时间
	private Date preparedTimeEnd;// 制单起始时间
	private Long orderId;
	private SalesOrder order;
	private Integer waresStatus;
	private Receive receive;
	private ReceiveGoods goods;
	private IndivFlow indivFlow;
	private List<Receive> receiveList;
	private List<ReceiveGoods> goodsList;
	private List<IndivFlow> indivList;
	private List<Warehouse> warehouseList;// 仓库列表
	private List<Supplier> supplierList;// 供应商列表
	private Page page = new Page();
	private List<SalesOrderGoods> orderGoods; 
	private List<String> goodIds;
	private Integer[] nonDefective;
	private Integer[] defective;

	/**
	 * 进入退货入库单列表界面
	 */
	public String execute() throws Exception {
		// 初始化页面数据
		warehouseList = warehouseService.getValidWarehouses();
		supplierList = supplierService.getValidSuppliers();

		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("warehouseId", warehouseId);
		criteria.put("receiveCode", StringUtils.defaultIfBlank(receiveCode, null));
		criteria.put("handlingStatus", handlingStatus);
		criteria.put("receiveType", ReceiveType.RMA.getCode());
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

	/**
	 * 进入退货入库单初始化界面
	 */
	public String inputInit() throws Exception {
		// 初始化页面数据
		Validate.notNull(orderId);
		order = salesOrderService.getSalesOrder(orderId);
		warehouseList = warehouseService.getValidWarehouses();
		
		//查询订单下商品清单
		orderGoods = salesOrderService.getOrderGoodsList(orderId);
		return "input_init";
	}
	
	/**
	 * 进入拒收初始化界面
	 */
	public String inputRefuse() throws Exception {
		// 初始化页面数据
		Validate.notNull(orderId);
		order = salesOrderService.getSalesOrder(orderId);
		warehouseList = warehouseService.getValidWarehouses();
		
		//查询订单下商品清单
//		orderGoods = salesOrderService.getOrderGoodsList(orderId);
		return "input_refuse";
	}

	/**
	 * 进入退货入库单编辑界面
	 */
	public String input() throws Exception {
		Validate.notNull(id);
		receive = receiveService.getReceive(id);
		goodsList = receiveService.getReceiveGoodsList(id);
		return INPUT;
	}

	/**
	 * 进入退货商品个体编辑界面
	 */
	public String inputIndivItem() throws Exception {
		return "input_indiv";
	}

	/**
	 * 进入退货商品SKU编辑界面
	 */
	public String inputSkuItem() throws Exception {
		return "input_sku";
	}

	/**
	 * 根据商品编码查询商品个体信息(for ajax)
	 */
	public String getIndivInfo() throws Exception {
		String indivCode = ActionUtils.getRequest().getParameter("indivCode");
		Validate.notNull(indivCode);
		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
		try {
			Indiv indiv = indivService.getIndivByCode(indivCode);
			System.out.println(jsonUtils.toJson(indiv));
			ActionUtils.outputJson(jsonUtils.toJson(indiv));
		} catch (Exception e) {
			logger.error("AJAX取商品个体信息时出错", e);
			ActionUtils.outputJson(jsonUtils.toJson(null));
		}
		return null;
	}

	/**
	 * 根据商品编码查询订单信息(for ajax)
	 
	public String getOrderInfo() throws Exception {
		String orderCode = ActionUtils.getRequest().getParameter("orderCode");
		Validate.notNull(orderCode);
		JsonUtils jsonUtils = new JsonUtils(Inclusion.ALWAYS);
		try {
			SalesOrder salesOrder = salesOrderService.getSalesOrder(orderCode);
			ActionUtils.outputJson(jsonUtils.toJson(salesOrder));
		} catch (Exception e) {
			logger.error("AJAX取订单信息时出错", e);
			ActionUtils.outputJson(jsonUtils.toJson(null));
		}
		return null;
	}*/

	/**
	 * 添加入库明细项(有编号商品)
	 
	public String addIndivItem() throws Exception {
		Validate.notNull(indivFlow);
		receive = receiveService.getReceive(id);
		try {
			receiveService.addRmaIndiv(receive, indivFlow);
			ajaxSuccess("添加退货商品成功");
		} catch (ServiceException e) {
			logger.error("添加退货商品时出错", e);
			ajaxError("添加退货商品失败：" + e.getMessage());
		}
		return null;
	}*/

	/**
	 * 进入收货商品编辑页面
	 */
	public String inputGoods() throws Exception {
		Validate.notNull(id);
		goods = receiveService.getReceiveGoods(id);
		if (WmsConstants.ENABLED_TRUE == goods.getIndivEnabled()) {
			indivList = indivService.getIndivFlowsByFlowItemId(IndivFlowType.IN_RMA.getCode(), id);
			return "input_indiv";
		}
		return "input_goods";
	}

	public String updateGoods() throws Exception {
		Validate.notNull(goods);
		if (WmsConstants.ENABLED_TRUE == goods.getIndivEnabled()) {
			if (CollectionUtils.isEmpty(indivList)) {
				ajaxError("编辑收货商品失败：商品编码不能为空");
				return null;
			}
			// 过滤商品个体
			Iterator<IndivFlow> itr = indivList.iterator();
			while (itr.hasNext()) {
				IndivFlow indiv = itr.next();
				if (indiv == null || StringUtils.isBlank(indiv.getIndivCode())) {
					itr.remove();
				} else {
					indiv.setIndivCode(indiv.getIndivCode().trim());
				}
			}
			try {
				receiveService.updatePurchaseRecvGoodsWithIndivList(goods, indivList);
				ajaxSuccess("编辑收货商品成功");
			} catch (ServiceException e) {
				logger.error("编辑收货商品时出错", e);
				ajaxError("编辑收货商品失败：" + e.getMessage());
			}

		} else {
			try {
				receiveService.updateReceiveGoods(goods);
				ajaxSuccess("编辑收货商品成功");
			} catch (ServiceException e) {
				logger.error("编辑收货商品时出错", e);
				ajaxError("编辑收货商品失败：" + e.getMessage());
			}

		}
		return null;
	}

	public void prepareUpdateGoods() throws Exception {
		Validate.notNull(id);
		goods = receiveService.getReceiveGoods(id);
	}

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
	 * 创建退货入库单
	 */
	public String add() throws Exception {
		Validate.notNull(receive);
		Validate.notNull(waresStatus);
		receive.setWarehouseName(warehouseService.getWarehouse(receive.getWarehouseId()).getWarehouseName());
		try {
			receive = receiveService.addRmaReceive(receive, waresStatus);
			Map<String, Object> params = Maps.newHashMap();
			params.put("receiveId", receive.getId());
			ajaxSuccess("创建退货入库单成功", params);
		} catch (Exception e) {
			logger.error("创建退货入库单时出错", e);
			ajaxError("创建退货入库单失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 拒收确认
	 */
	public String refuse() throws Exception {
		Validate.notNull(receive);
		Validate.notNull(waresStatus);
		receive.setWarehouseName(warehouseService.getWarehouse(receive.getWarehouseId()).getWarehouseName());
		try {
			receive = receiveService.refuseRmaReceive(receive, waresStatus);
			Map<String, Object> params = Maps.newHashMap();
			params.put("receiveId", receive.getId());
			ajaxSuccess("操作成功", params);
		} catch (Exception e) {
			logger.error("出错", e);
			ajaxError("操作失败：" + e.getMessage());
		}
		return null;
	}

	public String confirm() throws Exception {
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

	public String getPurchaseCode() {
		return purchaseCode;
	}

	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
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

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public Integer getWaresStatus() {
		return waresStatus;
	}

	public void setWaresStatus(Integer waresStatus) {
		this.waresStatus = waresStatus;
	}

	public SalesOrder getOrder() {
		return order;
	}

	public void setOrder(SalesOrder order) {
		this.order = order;
	}

	public List<SalesOrderGoods> getOrderGoods() {
		return orderGoods;
	}

	public void setOrderGoods(List<SalesOrderGoods> orderGoods) {
		this.orderGoods = orderGoods;
	}

	public List<String> getGoodIds() {
		return goodIds;
	}

	public void setGoodIds(List<String> goodIds) {
		this.goodIds = goodIds;
	}

	public Integer[] getNonDefective() {
		return nonDefective;
	}

	public void setNonDefective(Integer[] nonDefective) {
		this.nonDefective = nonDefective;
	}

	public Integer[] getDefective() {
		return defective;
	}

	public void setDefective(Integer[] defective) {
		this.defective = defective;
	}

}
