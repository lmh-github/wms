package com.gionee.wms.web.action.stock;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
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
import com.gionee.wms.common.MyCollectionUtils;
import com.gionee.wms.common.PermissionConstants;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.DeliveryBatchStatus;
import com.gionee.wms.common.WmsConstants.OrderStatus;
import com.gionee.wms.common.excel.excelexport.module.ExcelModule;
import com.gionee.wms.common.excel.excelexport.userinterface.ExcelExpUtil;
import com.gionee.wms.dto.DeliverySummary;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Delivery;
import com.gionee.wms.entity.DeliveryBatch;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.Shipping;
import com.gionee.wms.entity.Warehouse;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.account.AccountService;
import com.gionee.wms.service.basis.ShippingService;
import com.gionee.wms.service.basis.WarehouseService;
import com.gionee.wms.service.stock.DeliveryService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.gionee.wms.web.AccessException;
import com.gionee.wms.web.action.AjaxActionSupport;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.opensymphony.xwork2.Preparable;

@Controller("DeliveryBatchAction")
@Scope("prototype")
public class DeliveryBatchAction extends AjaxActionSupport implements Preparable {
	private static final long serialVersionUID = 2728587467025993326L;
	private static final int BATCH_ORDERS_NUM = 40;	// 每次生成出货订单数
	@Autowired
	private WarehouseService warehouseService;
	@Autowired
	private DeliveryService deliveryService;
	@Autowired
	private AccountService accountService;
	@Autowired
	private ShippingService shippingService;
	@Autowired
	private SalesOrderService salesOrderService;

	/** 页面相关属性 **/
	private Long id;
	private String batchCode;
	private Long warehouseId;
	private Integer handlingStatus;//默认只显示待处理记录
	private Date preparedTimeBegin;// 制单起始时间
	private Date preparedTimeEnd;// 制单起始时间
	private DeliveryBatch deliveryBatch;// 拣货批次model
	private List<DeliveryBatch> deliveryBatchList; // 拣货批次列表
	private List<DeliverySummary> deliverySummaryList;// 拣货汇总列表
	private List<Warehouse> warehouseList;// 仓库列表
	private List<Shipping> shippingList;// 配送方式列表
	private List<Delivery> deliveryList;
	private List<Long> orderIds;
	private Long shippingId;
	private Page page = new Page();

	/**
	 * 进入拣货批次列表界面
	 */
	@Override
	public String execute() throws Exception {
		// 初始化页面数据
//		warehouseList = warehouseService.getValidWarehouses();

		if(batchCode != null)
			batchCode = batchCode.trim();
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("batchCode", StringUtils.defaultIfBlank(batchCode, null));
//		criteria.put("warehouseId", warehouseId);
		criteria.put("handlingStatus", handlingStatus);
		criteria.put("preparedTimeBegin", preparedTimeBegin);
		criteria.put("preparedTimeEnd", preparedTimeEnd != null ? new Date(preparedTimeEnd.getTime() + (24 * 3600 - 1)
				* 1000) : null);
		int totalRow = deliveryService.getDeliveryBatchTotal(criteria);
		page.setTotalRow(totalRow);
		page.calculate();
		criteria.put("page", page);
		deliveryBatchList = deliveryService.getDeliveryBatchList(criteria, page);
		return SUCCESS;
	}

	/**
	 * 创建拣货批次
	 */
	public String add() throws Exception {
		if (!accountService.isPermitted(PermissionConstants.SALES_OUT_ADD)) {
			throw new AccessException();
		}
//		Validate.notNull(warehouseId);
		Validate.notNull(shippingId);
//		Validate.notEmpty(orderIds);
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("shippingId", shippingId);
		criteria.put("orderStatus", OrderStatus.FILTERED.getCode());
		Page page = new Page();
		page.setStartRow(0);
		page.setEndRow(BATCH_ORDERS_NUM);
		List<SalesOrder> orders = salesOrderService.getSalesOrderList(criteria, page);
		logger.debug("shippingId is " + shippingId + " orders size is " + orders.size());
		Warehouse warehouse = warehouseService.getDefaultWarehouse();
		Validate.notNull(warehouse);
		deliveryBatch = new DeliveryBatch();
		deliveryBatch.setWarehouseId(warehouse.getId());
		deliveryBatch.setWarehouseName(warehouse.getWarehouseName());
		deliveryBatch.setPreparedBy(ActionUtils.getLoginName());
		try {
			deliveryService.addDeliveryBatch(deliveryBatch, orders);
			ajaxSuccess("创建拣货批次成功，请到批次列表中继续操作");
		} catch (ServiceException e) {
			logger.error("创建拣货批次时出错", e);
			ajaxError("创建拣货批次失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 进入拣货批次确认界面
	 */
	public String input() throws Exception {
		if (!accountService.isPermitted(PermissionConstants.SALES_OUT_CONFIRM)) {
			throw new AccessException();
		}
		warehouseList = warehouseService.getValidWarehouses();
		return INPUT;
	}

	public void prepareInput() throws Exception {
		Validate.notNull(id);
		// 初始化Model对象
		deliveryBatch = deliveryService.getDeliveryBatch(id);
	}
	
	/**
	 * 进入配送信息批量编辑界面
	 */
	public String inputShippingInfo() throws Exception {
		if (!accountService.isPermitted(PermissionConstants.SALES_OUT_SHIPPING_EDIT)) {
			throw new AccessException();
		}
		shippingList = shippingService.getValidShippings();
		return "input_shipping_info";
	}
	
	public void prepareInputShippingInfo() throws Exception {
		Validate.notNull(id);
		// 初始化Model对象
		deliveryList = deliveryService.getDeliveryListByBatchId(id);
	}
	
	/**
	 * 批量更新拣货批次内的订单配送信息
	 */
	public String updateShippingInfo()throws Exception{
		if (!accountService.isPermitted(PermissionConstants.SALES_OUT_SHIPPING_EDIT)) {
			throw new AccessException();
		}
		Validate.notNull(id);
		try {
			if (CollectionUtils.isNotEmpty(deliveryList)) {// 更新出库订单明细项的商品个体信息
				deliveryService.updateShippingInfoList(id, deliveryList);
				ajaxSuccess("编辑配送信息成功");
			} else {
				ajaxError("编辑配送信息失败：参数错误");
			}
		} catch (ServiceException e) {
			logger.error("编辑配送信息时出错", e);
			ajaxError("编辑配送信息失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 确认拣货批次
	 */
	public String confirm() throws Exception {
		if (!accountService.isPermitted(PermissionConstants.SALES_OUT_CONFIRM)) {
			throw new AccessException();
		}
		Validate.notNull(deliveryBatch);
		try {
			deliveryService.confirmDeliveryBatch(deliveryBatch);
			ajaxSuccess("出库成功");
		} catch (ServiceException e) {
			logger.error("确认出库时出错", e);
			ajaxError("确认出库失败：" + e.getMessage());
		}
		return null;
	}

	public void prepareConfirm() throws Exception {
		Validate.notNull(id);
		// 初始化Model对象
		deliveryBatch = deliveryService.getDeliveryBatch(id);
	}

	/**
	 * 更新拣货批次信息
	 */
	public String update() throws Exception {
		Validate.notNull(deliveryBatch);
		Validate.notNull(deliveryBatch.getId());
		try {
			// stockOutService.updateStockOut(stockOut);
			ajaxSuccess("编辑出库单基本信息成功");
		} catch (ServiceException e) {
			logger.error("编辑出库单基本信息时出错", e);
			ajaxError("编辑出库单基本信息失败：" + e.getMessage());
		}
		return null;
	}

	public void prepareUpdate() throws Exception {
		deliveryBatch = new DeliveryBatch();
	}

	/**
	 * 进入拣货批次商品汇总界面
	 */

	public String inputDeliverySummary() throws Exception {
		// 初始化页面数据
		deliveryBatch = deliveryService.getDeliveryBatch(id);
		deliverySummaryList = deliveryService.getDeliverySummaryListByBatchId(id);
		return "input_summary";
	}

	/**
	 * 取消拣货批次
	 */
	public String cancel() throws Exception {
		if (!accountService.isPermitted(PermissionConstants.SALES_OUT_DELETE)) {
			throw new AccessException();
		}
		Validate.notNull(id);
		try {
			 deliveryService.cancelDeliveryBatch(id);
			ajaxSuccess("取消拣货批次成功");
		} catch (ServiceException e) {
			logger.error("取消拣货批次时出错", e);
			ajaxError("取消拣货批次失败：" + e.getMessage());
		}
		return null;
	}
	
	/**
	 * 取拣货批次内的所有拣货单
	 */
	public String getDeliveryIds() throws Exception {
		Validate.notNull(id);
		try {
			
			List<Delivery> deliveryList = deliveryService.getDeliveryListByBatchId(id);
			List<String> deliveryIds= MyCollectionUtils.collectionElementPropertyToList(deliveryList, "id");
			Map<String, Object> params = Maps.newHashMap();
			params.put("deliveryIds", StringUtils.join(deliveryIds, ","));
			ajaxSuccess("开始打印", params);
		} catch (Exception e) {
			logger.error("取拣货批次内的拣货单ID集合时出错", e);
			ajaxError("打印失败：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 下载配送信息
	 */
	public String downloadDistInfo() {
		Validate.notNull(id);
		DeliveryBatch deliveryBatch = deliveryService.getDeliveryBatch(id);
		// 取拣货批次对应的各订单配送信息
		List<Delivery> deliveryList = deliveryService.getDeliveryListByBatchId(id);
		if (CollectionUtils.isEmpty(deliveryList)) {
			throw new ServiceException("订单数据不存在");
		}
		List<Map<String, String>> sheetData = new ArrayList<Map<String, String>>();
		Map<String, String> onceData = new HashMap<String, String>();
		onceData.put("OUT_CODE", deliveryBatch.getBatchCode());
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

	public void prepare() throws Exception {

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

	public String getBatchCode() {
		return batchCode;
	}

	public void setBatchCode(String batchCode) {
		this.batchCode = batchCode;
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

	public DeliveryBatch getDeliveryBatch() {
		return deliveryBatch;
	}

	public void setDeliveryBatch(DeliveryBatch deliveryBatch) {
		this.deliveryBatch = deliveryBatch;
	}

	public List<DeliveryBatch> getDeliveryBatchList() {
		return deliveryBatchList;
	}

	public void setDeliveryBatchList(List<DeliveryBatch> deliveryBatchList) {
		this.deliveryBatchList = deliveryBatchList;
	}

	public List<Warehouse> getWarehouseList() {
		return warehouseList;
	}

	public void setWarehouseList(List<Warehouse> warehouseList) {
		this.warehouseList = warehouseList;
	}

	public List<Long> getOrderIds() {
		return orderIds;
	}

	public void setOrderIds(List<Long> orderIds) {
		this.orderIds = orderIds;
	}

	public Page getPage() {
		return page;
	}

	public void setPage(Page page) {
		this.page = page;
	}

	public List<DeliverySummary> getDeliverySummaryList() {
		return deliverySummaryList;
	}

	public List<Shipping> getShippingList() {
		return shippingList;
	}

	public void setShippingList(List<Shipping> shippingList) {
		this.shippingList = shippingList;
	}

	public List<Delivery> getDeliveryList() {
		return deliveryList;
	}

	public void setDeliveryList(List<Delivery> deliveryList) {
		this.deliveryList = deliveryList;
	}

	public Long getShippingId() {
		return shippingId;
	}

	public void setShippingId(Long shippingId) {
		this.shippingId = shippingId;
	}
}
