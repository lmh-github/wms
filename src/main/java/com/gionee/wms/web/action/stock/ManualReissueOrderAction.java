/**
 * Project Name:wms
 * File Name:ManualReissueOrderAction.java
 * Package Name:com.gionee.wms.web.action.stock
 * Date:2016年10月8日下午6:29:46
 * Copyright (c) 2016 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.web.action.stock;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.WmsConstants.OrderSource;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.ManualReissueOrder;
import com.gionee.wms.entity.ManualReissueOrderGoods;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.service.stock.ManualReissueOrderService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.gionee.wms.web.action.AjaxActionSupport;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionContext;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2016年10月8日 下午6:29:46
 */
@Controller("ManualReissueOrderAction")
@Scope("prototype")
public class ManualReissueOrderAction extends AjaxActionSupport {

	/** serialVersionUID */
	private static final long serialVersionUID = -2114733892099789013L;

	@Autowired
	private ManualReissueOrderService manualReissueOrderService;

	@Autowired
	private SalesOrderService salesOrderService;

	private Page page = new Page();

	private ManualReissueOrder order = new ManualReissueOrder();

	private Date createTimeBegin;

	private Date createTimeEnd;

	private String consignee;

	private String mobile;

	private String orderCode;

	@Override
	public String execute() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("platform", order.getPlatform());
		criteria.put("orderCode", StringUtils.trimToNull(order.getOrderCode()));
		criteria.put("invoice", order.getInvoice());
		criteria.put("billType", StringUtils.trimToNull(order.getBillType()));
		criteria.put("mobile", StringUtils.trimToNull(mobile));
		criteria.put("status", order.getStatus());
		// criteria.put("paymentName", "");
		criteria.put("createTimeBegin", createTimeBegin);
		criteria.put("createTimeEnd", createTimeEnd);

		if (!"1".equals(request.getParameter("exports"))) {

			int totalRow = manualReissueOrderService.queryCount(criteria);
			page.setTotalRow(totalRow);
			page.calculate();
			criteria.put("page", page);

			if (totalRow == 0) {
				ActionContext.getContext().put("dataList", new ArrayList<Map<String, Object>>(0));
			} else {
				List<Map<String, Object>> listData = manualReissueOrderService.query(criteria, page);
				for (Map<String, Object> map : listData) {
					List<ManualReissueOrderGoods> goods = manualReissueOrderService.queryGoods(Long.valueOf(map.get("id").toString()));
					map.put("goods", goods);
				}
				ActionContext.getContext().put("dataList", listData);
			}
		} else {
			export(criteria);
			return null;
		}
		return SUCCESS;
	}

	public void export(Map<String, Object> criteria) {
		try {
			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(ActionUtils.getProjectPath() + "/export/morder_template.xls")));
			HSSFSheet sheet = workbook.getSheetAt(0);

			List<Map<String, Object>> listData = manualReissueOrderService.queryForExport(criteria);

			Map<Object, String> statusMap = Maps.newHashMap();
			statusMap.put(0L, "未处理");
			statusMap.put(1L, "已处理");

			Map<Object, String> orderSourceMap = Maps.newHashMap();
			for (OrderSource o : WmsConstants.OrderSource.values()) {
				orderSourceMap.put(o.getCode(), o.getName());
			}

			for (int i = 0, j = 0; i < listData.size(); i++, j = 0) {
				Map<String, Object> map = listData.get(i);

				HSSFRow newRow = sheet.createRow(i + 1);
				newRow.createCell(j++).setCellValue((Date) map.get("createTime"));
				newRow.createCell(j++).setCellValue(orderSourceMap.get(getStringValue(map.get("platform")))); // 订单来源
				newRow.createCell(j++).setCellValue(getStringValue(map.get("orderCode"))); // 订单号
				newRow.createCell(j++).setCellValue(getStringValue(map.get("orderAmount"))); // 订单金额
				newRow.createCell(j++).setCellValue(getStringValue(map.get("skuCode")));
				newRow.createCell(j++).setCellValue(getStringValue(map.get("skuName")));
				newRow.createCell(j++).setCellValue(getStringValue(map.get("qty")));
				newRow.createCell(j++).setCellValue(getStringValue(map.get("paymentName")));
				newRow.createCell(j++).setCellValue(getStringValue(map.get("consignee")));
				newRow.createCell(j++).setCellValue(getStringValue(map.get("mobile")));
				newRow.createCell(j++).setCellValue(getStringValue(map.get("newOrderCode")));
				newRow.createCell(j++).setCellValue("1".equals(getStringValue(map.get("invoice"))) ? "是" : "否");
				newRow.createCell(j++).setCellValue(getStringValue(map.get("billType")));
				newRow.createCell(j++).setCellValue(getStringValue(map.get("remark")));
				newRow.createCell(j++).setCellValue(getStringValue(map.get("extension")));
				newRow.createCell(j++).setCellValue(statusMap.get(map.get("status")));
				newRow.createCell(j++).setCellValue(getStringValue(map.get("createBy")));
				newRow.createCell(j++).setCellValue((Date) map.get("createTime"));
				newRow.createCell(j++).setCellValue(getStringValue(map.get("updateBy")));

				Date updateTime = map.get("updateTime") == null ? null : (Date) map.get("updateTime");
				if (updateTime == null) {
					newRow.createCell(j++).setCellValue("");
				} else {
					newRow.createCell(j++).setCellValue(updateTime);
				}
			}

			// 清空输出流
			response.reset();
			// 设置响应头和下载保存的文件名
			response.setHeader("content-disposition", "attachment;filename=morder_" + System.currentTimeMillis() + ".xls");
			// 定义输出类型
			response.setContentType("APPLICATION/msexcel");
			ServletOutputStream outputStream = response.getOutputStream();
			workbook.write(outputStream);

			outputStream.flush();
			try {
				outputStream.close();
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private String getStringValue(Object obj) {
		return obj == null ? "" : obj.toString();
	}

	public String prepareInput() {
		return "toInput";
	}

	/**
	 * 查找订单
	 */
	public void lookupSalesOrder() {
		try {
			SalesOrder salesOrder = salesOrderService.getSalesOrderByCode(orderCode);
			if (salesOrder == null) {
				ajaxError("订单号：" + orderCode + "，不存在！");
				return;
			}

			List<SalesOrderGoods> goodsList = salesOrderService.getOrderGoodsList(salesOrder.getId());
			if (goodsList != null) {
				for (SalesOrderGoods salesOrderGoods : goodsList) {
					salesOrderGoods.setOrder(null);
				}
			}

			Map<String, Object> root = Maps.newHashMap();
			root.put("salesOrder", salesOrder);
			root.put("goodsList", goodsList);
			ajaxObject(root);
		} catch (Exception e) {
			e.printStackTrace();
			ajaxError("程序出现异常！");
		}
	}

	public void add() {
		try {
			if (order.getGoods() == null || order.getGoods().size() == 0) {
				ajaxError("请添加商品！");
				return;
			}

			order.setCreateBy(ActionUtils.getLoginName()); // 设置创建人
			manualReissueOrderService.handleAdd(order, order.getGoods());
			ajaxSuccess("创建成功！");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String toFinish() {
		String strId = ServletActionContext.getRequest().getParameter("id");
		Map<String, Object> map = manualReissueOrderService.get(Long.valueOf(strId));
		ActionContext.getContext().getContextMap().putAll(map);
		return "toFinish";
	}

	public void finish() {
		try {
			if (order.getId() == null) {
				ajaxError("没有要操作的单据！");
				return;
			}

			if (StringUtils.isBlank(order.getNewOrderCode())) {
				ajaxError("没有填写补发订单号！");
				return;
			}

			ManualReissueOrder toUpOrder = new ManualReissueOrder();
			toUpOrder.setId(order.getId());
			toUpOrder.setStatus(1);
			toUpOrder.setNewOrderCode(order.getNewOrderCode());
			toUpOrder.setUpdateBy(ActionUtils.getLoginName());

			manualReissueOrderService.handleUpdate(toUpOrder);

			ajaxSuccess("操作成功！");

		} catch (Exception e) {
			e.printStackTrace();
			ajaxError("操作异常！");
		}
	}

	public void delete() {
		try {
			String strId = ServletActionContext.getRequest().getParameter("id");
			if (StringUtils.isBlank(strId)) {
				ajaxError("没有要操作的单据！");
				return;
			}

			Long id = Long.valueOf(strId);
			manualReissueOrderService.handleDelete(id);

			ajaxSuccess("操作成功！");

		} catch (Exception e) {
			e.printStackTrace();
			ajaxError("操作异常！");
		}
	}

	public String toView() {
		Map<String, Object> map = manualReissueOrderService.get(Long.valueOf(order.getId()));
		List<ManualReissueOrderGoods> goods = manualReissueOrderService.queryGoods(order.getId());
		String orderCode = map.get("orderCode").toString();
		SalesOrder salesOrder = salesOrderService.getSalesOrderByCode(orderCode);

		ActionContext.getContext().getContextMap().putAll(map);
		ActionContext.getContext().put("salesOrder", salesOrder);
		ActionContext.getContext().put("goods", goods);

		return "toView";
	}

	/**
	 * @return the page
	 */
	public Page getPage() {
		return page;
	}

	/**
	 * @param page the page
	 */
	public void setPage(Page page) {
		this.page = page;
	}

	/**
	 * @return the order
	 */
	public ManualReissueOrder getOrder() {
		return order;
	}

	/**
	 * @param order the order
	 */
	public void setOrder(ManualReissueOrder order) {
		this.order = order;
	}

	/**
	 * @return the createTimeBegin
	 */
	public Date getCreateTimeBegin() {
		return createTimeBegin;
	}

	/**
	 * @param createTimeBegin the createTimeBegin
	 */
	public void setCreateTimeBegin(Date createTimeBegin) {
		this.createTimeBegin = createTimeBegin;
	}

	/**
	 * @return the createTimeEnd
	 */
	public Date getCreateTimeEnd() {
		return createTimeEnd;
	}

	/**
	 * @param createTimeEnd the createTimeEnd
	 */
	public void setCreateTimeEnd(Date createTimeEnd) {
		this.createTimeEnd = createTimeEnd;
	}

	/**
	 * @return the consignee
	 */
	public String getConsignee() {
		return consignee;
	}

	/**
	 * @param consignee the consignee
	 */
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile the mobile
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the orderCode
	 */
	public String getOrderCode() {
		return orderCode;
	}

	/**
	 * @param orderCode the orderCode
	 */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

}
