package com.gionee.wms.web.action.stock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.PermissionConstants;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dto.DeliverySummary;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Delivery;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.StockOut;
import com.gionee.wms.entity.StockOutItem;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.account.AccountService;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.DeliveryService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.gionee.wms.service.stock.StockOutService;
import com.gionee.wms.service.wares.IndivService;
import com.gionee.wms.web.AccessException;
import com.gionee.wms.web.action.CrudActionSupport;
import com.google.common.collect.Maps;

@Controller("StockOutAction")
@Scope("prototype")
public class StockOutAction extends CrudActionSupport<StockOut> {
	private static final long serialVersionUID = 2728587467025993326L;

	private WarehouseService warehouseService;
	private IndivService indivService;
	private StockOutService stockOutService;
	private SalesOrderService salesOrderService;
	private DeliveryService deliveryService;
	private AccountService accountService;

	/** 页面相关属性 **/
	private Long id;
	private String stockOutCode;
	private String orderCode;
	private Long warehouseId;
	private String warehouseCode;
	private List<String> orderCodes;
	private List<StockOut> stockOutList; // 入库单列表
	private StockOut stockOut;// 入库单model
	private StockOutItem stockOutItem; // 入库明细项
	private List<SalesOrder> salesOrderList;// 入库明细
	private List<IndivFlow> indivList;// 商品身份信息
	private List<DeliverySummary> detailSummaryList;// 出库明细汇总列表
	private List<Warehouse> warehouseList;// 仓库列表
	private Date handledDateBegin;// 出库起始时间
	private Date handledDateEnd;// 出库结束时间
	private Page page = new Page();

	/**
	 * 查询出库单列表
	 */
	@Override
	public String list() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("stockOutCode", StringUtils.defaultIfBlank(stockOut.getStockOutCode(), null));
		criteria.put("warehouseId", warehouseId);
		criteria.put("handledDateBegin", handledDateBegin);
		criteria.put("handledDateEnd", handledDateEnd);
//		int totalRow = stockOutService.getStockOutListTotal(criteria);
//		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
//		stockOutList = stockOutService.getStockOutList(criteria, page);
		return SUCCESS;
	}

	@Override
	public void prepareList() throws Exception {
		// 初始化查询参数对象
		stockOut = new StockOut();
		// 初始化页面数据
		warehouseList = warehouseService.getValidWarehouses();
	}

	/**
	 * 进入出库确认页面
	 */
	@Override
	public String input() throws Exception {
		if (!accountService.isPermitted(PermissionConstants.SALES_OUT_CONFIRM)) {
			throw new AccessException();
		}
		warehouseList = warehouseService.getValidWarehouses();
		return INPUT;
	}

	@Override
	public void prepareInput() throws Exception {
		Validate.notNull(id);
		// 初始化Model对象
//		stockOut = stockOutService.getStockOut(id);
	}

	/**
	 * 确认出库
	 */
	public String confirm() throws Exception {
		if (!accountService.isPermitted(PermissionConstants.SALES_OUT_CONFIRM)) {
			throw new AccessException();
		}
		Validate.notNull(stockOut);
		try {
//			stockOutService.confirmStockOut(stockOut);
			ajaxSuccess("出库成功");
		} catch (ServiceException e) {
			logger.error("确认出库时出错", e);
			ajaxError("确认出库失败：" + e.getMessage());
		}
		return null;
	}

	public void prepareConfirm() throws Exception {
		Validate.notNull(stockOutCode);
		// 初始化Model对象
//		stockOut = stockOutService.getStockOutByCode(stockOutCode);
	}

	/**
	 * 更新出库单基本信息
	 */
	@Override
	public String update() throws Exception {
		Validate.notNull(stockOut);
		Validate.notNull(stockOut.getId());
		try {
//			stockOutService.updateStockOut(stockOut);
			ajaxSuccess("编辑出库单基本信息成功");
		} catch (ServiceException e) {
			logger.error("编辑出库单基本信息时出错", e);
			ajaxError("编辑出库单基本信息失败：" + e.getMessage());
		}
		return null;
	}

	@Override
	public void prepareUpdate() throws Exception {
		stockOut = new StockOut();
	}


	/**
	 * 进入出库明细汇总界面
	 */

	public String inputDetailSummary() throws Exception {
		// 初始化页面数据
		Map<String, Object> criteria = Maps.newHashMap();
//		detailSummaryList = deliveryService.getStockOutSummaryList(stockOutCode);
		return "input_summary";
	}

	public void prepareInputDetailSummary() throws Exception {
		Validate.notBlank(stockOutCode);
		// 初始化Model对象
//		stockOut = stockOutService.getStockOutByCode(stockOutCode);
		// salesOrderList = stockOutService.getSalesOrderList(stockOutCode);
	}

	/**
	 * 批量订单生成出库单
	 */
	public String addStockOutWithOrders() throws Exception {
		if (!accountService.isPermitted(PermissionConstants.SALES_OUT_ADD)) {
			throw new AccessException();
		}
		// 设置制单人
		// stockCheckItem.setPreparedBy("kevin");
		// stockCheck.setPrepareBy(accountService.getCurrentUser().getLoginName());
		Validate.notEmpty(orderCodes);
		Validate.notNull(warehouseId);
		Warehouse warehouse = warehouseService.getWarehouse(warehouseId);
		Validate.notNull(warehouse);
		StockOut stockOut = new StockOut();
//		stockOut.setWarehouseCode(warehouseCode);
		stockOut.setWarehouseId(warehouseId);
		stockOut.setWarehouseName(warehouse.getWarehouseName());
//		stockOut.setWarehouseName(StringUtils.defaultIfBlank(warehouse.getWarehouseName(),""));
		stockOut.setPreparedBy(ActionUtils.getLoginName());
		try {
//			stockOutService.addStockOut(stockOut, orderCodes);
			ajaxSuccess("生成出库单成功，请到出库单列表中继续操作");
		} catch (ServiceException e) {
			logger.error("生成出库单时出错", e);
			ajaxError("生成出库单失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 批量添加订单到出库单
	
	public String addOrdersToStockOut() throws Exception {
		// 设置制单人
		// stockCheckItem.setPreparedBy("kevin");
		// stockCheck.setPrepareBy(accountService.getCurrentUser().getLoginName());
		Validate.notEmpty(orderCodes);
		Validate.notEmpty(stockOutCode);

		try {
			stockOutService.createDeliverys(orderCodes, stockOutCode);
			ajaxSuccess("添加订单到出库单成功");
		} catch (ServiceException e) {
			logger.error("添加订单到出库单时出错", e);
			ajaxError("添加订单到出库单失败：" + e.getMessage());
		}
		return null;
	} */

	/**
	 * 从出库单中移除指定的销售订单
	 */
	public String removeOrder() throws Exception {
		Validate.notNull(orderCode);
		try {
//			stockOutService.deleteDeliveryOrder(orderCode);
			ajaxSuccess("移除订单成功");
		} catch (ServiceException e) {
			logger.error("移除订单时出错", e);
			ajaxError("移除订单失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 添加出库明细项
	 */
	public String addStockOutItem() throws Exception {
		Validate.notNull(stockOutItem);
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
			if (CollectionUtils.isNotEmpty(indivList)) {// 保存出库明细项及更新商品个体出库信息
				// stockOutService.addStockOutItemAndIndivs(stockOutItem,
				// indivList);
			} else {// 保存入库明细项
//				stockOutService.addStockOutItem(stockOutItem);
			}
			ajaxSuccess("添加入库商品成功");
		} catch (ServiceException e) {
			logger.error("添加入库商品时出错", e);
			ajaxError("添加入库商品失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 更新出库明细项
	 
	public String updateStockOutItem() throws Exception {
		if (!accountService.isPermitted(PermissionConstants.SALES_OUT_INDIV_EDIT)) {
			throw new AccessException();
		}
		Validate.notNull(stockOutItem);
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
			if (CollectionUtils.isNotEmpty(indivList)) {// 更新出库订单明细项的商品个体信息
				stockOutService.updateStockOutIndivs(stockOutItem, indivList);
				ajaxSuccess("编辑商品身份编码成功");
			} else {
				ajaxError("编辑商品身份编码失败：商品身份编码为空");
			}
		} catch (ServiceException e) {
			logger.error("编辑商品身份编码时出错", e);
			ajaxError("编辑商品身份编码失败：" + e.getMessage());
		}
		return null;
	}
*/
	public void prepareUpdateStockOutItem() throws Exception {
		Validate.notNull(id);
//		stockOutItem = stockOutService.getStockOutItem(id);
	}

	/**
	 * 从出库单中移除指定的SKU
	 */
	public String removeSku() throws Exception {
		Validate.notNull(orderCode);
		Validate.notNull(stockOutCode);
		try {
			// stockOutService.deleteOrderSkuFromOutDetail(id);
			ajaxSuccess("移除订单成功");
		} catch (ServiceException e) {
			logger.error("移除订单时出错", e);
			ajaxError("移除订单失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 取消出库单
	 */
	public String cancel() throws Exception {
		if (!accountService.isPermitted(PermissionConstants.SALES_OUT_DELETE)) {
			throw new AccessException();
		}
		Validate.notNull(stockOutCode);
		try {
//			stockOutService.cancelStockOutByCode(stockOutCode);
			ajaxSuccess("取消出库单成功");
		} catch (ServiceException e) {
			logger.error("取消出库单时出错", e);
			ajaxError("取消出库单失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 下载配送信息
	 */
	public String downloadDistInfo() {
		Validate.notBlank(stockOutCode);
		// 取出库编号对应的各订单配送信息
		List<Delivery> deliveryList = deliveryService.getDeliveryListByBatchCode(stockOutCode);
		if (CollectionUtils.isEmpty(deliveryList)) {
			throw new ServiceException("订单数据不存在");
		}
		List<Map<String, String>> sheetData = new ArrayList<Map<String, String>>();
		Map<String, String> onceData = new HashMap<String, String>();
		onceData.put("OUT_CODE", stockOutCode);
		sheetData.add(onceData);
		for (Delivery item : deliveryList) {
			Map<String, String> repeatData = new HashMap<String, String>();
			repeatData.put("CONSIGNEE", item.getConsignee());
			repeatData.put("PROVINCE", item.getProvince());
			repeatData.put("CITY", item.getCity());
			repeatData.put("DISTRICT", item.getDistrict());
			repeatData.put("ADDRESS", item.getAddress());
			repeatData.put("ZIPCODE", item.getZipcode());
			repeatData.put("TEL", item.getTel());
			repeatData.put("MOBILE", item.getMobile());
			sheetData.add(repeatData);
		}
		ExcelModule excelModule = new ExcelModule(sheetData);
		HttpServletResponse response = ServletActionContext.getResponse();
		// 清空输出流
		response.reset();
		// 设置响应头和下载保存的文件名
		response.setHeader("content-disposition", "attachment;filename=" + WmsConstants.DISTINFO_EXCEL_DOWNLOAD_NAME);
		// 定义输出类型
		response.setContentType("APPLICATION/msexcel");
		OutputStream out = null;
		try {
			String templeteFile = ActionUtils.getClassPath() + WmsConstants.DISTINFO_EXP_TEMPLETE;
			String tempFile = ActionUtils.getClassPath() + WmsConstants.EXCEL_TEMP_PATH
					+ WmsConstants.DISTINFO_EXCEL_DOWNLOAD_NAME;
			File file = ExcelExpUtil.expExcel(excelModule, templeteFile, tempFile);
			out = response.getOutputStream();
			FileUtils.copyFile(file, out);
			out.flush();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		// 返回null,可防止报异常：getOutputStream() has already been called for this
		// response
		return null;
	}

	@Override
	public String add() throws Exception {
		return null;
	}

	@Override
	public void prepareAdd() throws Exception {
		stockOut = new StockOut();
	}

	@Override
	public String delete() throws Exception {
		return null;
	}

	// ModelDriven接口方法
	@Override
	public StockOut getModel() {
		return stockOut;
	}

	public List<IndivFlow> getIndivList() {
		return indivList;
	}

	public void setIndivList(List<IndivFlow> indivList) {
		this.indivList = indivList;
	}

	public List<StockOut> getStockOutList() {
		return stockOutList;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public List<String> getOrderCodes() {
		return orderCodes;
	}

	public void setOrderCodes(List<String> orderCodes) {
		this.orderCodes = orderCodes;
	}

	public Page getPage() {
		if (page == null) {
			page = new Page();
		}
		return page;
	}

	public List<Warehouse> getWarehouseList() {
		return warehouseList;
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

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	public List<SalesOrder> getSalesOrderList() {
		return salesOrderList;
	}

	public String getStockOutCode() {
		return stockOutCode;
	}

	public void setStockOutCode(String stockOutCode) {
		this.stockOutCode = stockOutCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public StockOutItem getStockOutItem() {
		return stockOutItem;
	}

	public void setStockOutItem(StockOutItem stockOutItem) {
		this.stockOutItem = stockOutItem;
	}

	public List<DeliverySummary> getDetailSummaryList() {
		return detailSummaryList;
	}
	

	public Long getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
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
	public void setWarehouseService(WarehouseService warehouseService) {
		this.warehouseService = warehouseService;
	}

	@Autowired
	public void setStockOutService(StockOutService stockOutService) {
		this.stockOutService = stockOutService;
	}

	@Autowired
	public void setSalesOrderService(SalesOrderService salesOrderService) {
		this.salesOrderService = salesOrderService;
	}
	
	@Autowired
	public void setDeliveryService(DeliveryService deliveryService) {
		this.deliveryService = deliveryService;
	}

}
