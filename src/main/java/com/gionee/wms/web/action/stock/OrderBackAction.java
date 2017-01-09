/**
 * Project Name:wms
 * File Name:OrderBackAction.java
 * Package Name:com.gionee.wms.web.action.stock
 * Date:2015年11月2日下午3:54:58
 * Copyright (c) 2015 深圳市金立通信设备有限公司 版权所有 .
 */
package com.gionee.wms.web.action.stock;

import static org.apache.commons.lang3.StringUtils.trimToEmpty;
import static org.apache.commons.lang3.StringUtils.trimToNull;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import com.gionee.wms.common.ActionUtils;
import com.gionee.wms.common.WmsConstants.BackStatus;
import com.gionee.wms.common.WmsConstants.IndivStockStatus;
import com.gionee.wms.common.WmsConstants.LogType;
import com.gionee.wms.common.WmsConstants.OrderSource;
import com.gionee.wms.dto.Page;
import com.gionee.wms.entity.Back;
import com.gionee.wms.entity.BackGoods;
import com.gionee.wms.entity.Indiv;
import com.gionee.wms.entity.IndivFlow;
import com.gionee.wms.entity.IndivScanItem;
import com.gionee.wms.entity.Log;
import com.gionee.wms.entity.SalesOrder;
import com.gionee.wms.entity.SalesOrderGoods;
import com.gionee.wms.entity.Shipping;
import com.gionee.wms.facade.request.OperateOrderRequest;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.basis.ShippingService;
import com.gionee.wms.service.log.LogService;
import com.gionee.wms.service.stock.BackService;
import com.gionee.wms.service.stock.SalesOrderService;
import com.gionee.wms.service.wares.IndivService;
import com.gionee.wms.vo.BackGoodsVo;
import com.gionee.wms.web.action.AjaxActionSupport;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.Preparable;

/**
 * 
 * @author PengBin 00001550<br>
 * @date 2015年11月2日 下午3:54:58
 */
@Controller("OrderBackAction")
@Scope("prototype")
public class OrderBackAction extends AjaxActionSupport implements Preparable {

	/** serialVersionUID */
	private static final long serialVersionUID = 7676595262526937L;

	@Autowired
	private BackService backService;

	@Autowired
	private SalesOrderService salesOrderService;

	@Autowired
	private ShippingService shippingService;

	@Autowired
	private IndivService indivService;

	@Autowired
	private LogService logService;

	private Page page = new Page();

	/** 退换货单号 */
	private String backCode;
	/** 订单号 */
	private String orderCode;
	/** 状态 */
	private Integer backStatus;
	/** 物流单号 */
	private String shippingNo;
	/** 起始时间 */
	private Date backTimeBegin;
	/** 起始时间 */
	private Date backTimeEnd;

	/** 串号 */
	private String indivCode;

	/** 上传的文件 */
	private File upload;
	/** 上传的文件类型 */
	private String uploadContentType;
	/** 上传的文件名 */
	private String uploadFileName;

	private Date backedTimeBegin;

	private Date backedTimeEnd;

	private Date createTimeBegin;

	private Date createTimeEnd;

	private Back orderBack;
	private List<BackGoods> goodsList;
	private List<IndivFlow> indivList; // 接收页面输入的串号

	/** {@inheritDoc} */
	@Override
	public String execute() throws Exception {
		Map<String, Object> criteria = Maps.newHashMap();
		criteria.put("orderCode", trimToNull(orderCode));
		criteria.put("backStatus", backStatus);
		criteria.put("shippingNo", trimToNull(shippingNo));
		criteria.put("createTimeBegin", createTimeBegin);
		criteria.put("createTimeEnd", createTimeEnd);
		criteria.put("consignee", StringUtils.trimToNull(ServletActionContext.getRequest().getParameter("consignee")));
		criteria.put("mobile", StringUtils.trimToNull(ServletActionContext.getRequest().getParameter("mobile")));
		criteria.put("backType", StringUtils.trimToNull(ServletActionContext.getRequest().getParameter("backType")));
		criteria.put("mark", StringUtils.trimToNull(ServletActionContext.getRequest().getParameter("mark")));
		if (!"1".equals(request.getParameter("exports"))) {
			int totalRow = backService.getBackTotal(criteria);
			page.setTotalRow(totalRow);
			page.calculate();
			criteria.put("page", page);
			List<Back> backList = backService.getBackList(criteria, page);
			if (backList != null) {
				Map<String, Object> goodsQueryMap = new HashMap<String, Object>(1);
				for (Back back : backList) {
					goodsQueryMap.put("backCode", back.getBackCode());
					List<BackGoodsVo> goodsList2 = backService.getBackGoodsList(goodsQueryMap);
					if (goodsList2 != null) {
						List<BackGoods> goods = new ArrayList<BackGoods>(goodsList2.size());
						back.setBackGoods(goods);
						for (BackGoodsVo v : goodsList2) {
							BackGoods g = new BackGoods();
							g.setSkuCode(v.getSkuCode());
							g.setSkuName(v.getSkuName());

							goods.add(g);
						}
					}
				}
			}
			ActionContext.getContext().getValueStack().set("backList", backList);
			ActionContext.getContext().getValueStack().push(criteria);
			return SUCCESS;
		} else {
			export(criteria);
			return null;
		}
	}

	public void export(Map<String, Object> criteria) {
		try {
			List<Back> backList = backService.getBackListForCascade(criteria);

			HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(new File(ActionUtils.getProjectPath() + "/export/back_template.xls")));
			HSSFSheet sheet = workbook.getSheetAt(0);

			Map<String, String> backTypeMap = Maps.newHashMap();
			backTypeMap.put("back", "退货");
			backTypeMap.put("exchange", "换货");
			backTypeMap.put("rejected", "拒签");

			Map<Integer, String> backStatusMap = Maps.newHashMap();
			for (BackStatus e : BackStatus.values()) {
				backStatusMap.put(e.getCode(), e.getName());
			}

			Map<String, String> orderSourceMap = Maps.newHashMap();
			for (OrderSource e : OrderSource.values()) {
				orderSourceMap.put(e.getCode(), e.getName());
			}

			SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			for (int i = 0, j = 0; i < backList.size(); i++, j = 0) {
				Back back = backList.get(i);
				List<BackGoods> goods = back.getBackGoods();
				StringBuilder skuNameBuilder = new StringBuilder();
				for (BackGoods g : goods) {
					skuNameBuilder.append(g.getSkuName()).append("、");
				}

				SalesOrder so = back.getSalesOrder();
				String c = trimToEmpty(so.getConsignee()) + " " + trimToEmpty(so.getMobile()) + SystemUtils.LINE_SEPARATOR + trimToEmpty(so.getProvince()) + trimToEmpty(so.getCity()) + trimToEmpty(so.getDistrict()) + trimToEmpty(so.getAddress());

				HSSFRow newRow = sheet.createRow(i + 1);
				newRow.createCell(j++).setCellValue(trimToEmpty(orderSourceMap.get(back.getOrderSource()))); // 订单来源
				newRow.createCell(j++).setCellValue(trimToEmpty(back.getOrderCode())); // 订单号
				newRow.createCell(j++).setCellValue(trimToEmpty(so.getConsignee())); // 联系人
				newRow.createCell(j++).setCellValue(c); // 收货地址
				newRow.createCell(j++).setCellValue(trimToEmpty(skuNameBuilder.toString().replaceAll("、$", ""))); // SKU
				newRow.createCell(j++).setCellValue(trimToEmpty(backTypeMap.get(back.getBackType()))); // 退换类型
				newRow.createCell(j++).setCellValue(trimToEmpty(back.getRemarkBacking())); // 退换货备注
				newRow.createCell(j++).setCellValue(trimToEmpty(back.getCreateTime() == null ? null : format.format(back.getCreateTime()))); // 发起时间
				newRow.createCell(j++).setCellValue(trimToEmpty(back.getShippingCode())); // 快递类型
				newRow.createCell(j++).setCellValue(trimToEmpty(back.getShippingNo())); // 运单号
				newRow.createCell(j++).setCellValue(trimToEmpty(back.getRemarkBacked())); // 已退货备注
				newRow.createCell(j++).setCellValue(trimToEmpty(back.getBackedTime() == null ? null : format.format(back.getBackedTime()))); // 退货时间
				newRow.createCell(j++).setCellValue(trimToEmpty(backStatusMap.get(back.getBackStatus()))); // 退货状态
				newRow.createCell(j++).setCellValue((back.getHasTestBill() == null || back.getHasTestBill() == 0) ? "否" : "是"); // 是否有检测单
				newRow.createCell(j++).setCellValue(trimToEmpty(back.getBearParty())); // 运费承担方
				newRow.createCell(j++).setCellValue(back.getBackMoney() == null ? "" : back.getBackMoney().toString()); // 退款金额
			}

			// 清空输出流
			response.reset();
			// 设置响应头和下载保存的文件名
			response.setHeader("content-disposition", "attachment;filename=back_" + System.currentTimeMillis() + ".xls");
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

	/** {@inheritDoc} */
	@Override
	public String input() throws Exception {
		List<Shipping> shippings = shippingService.getValidShippings();
		ActionContext.getContext().getValueStack().set("shippings", shippings);
		return INPUT;
	}

	/**
	 * 跳转确认收货页面
	 * @return
	 */
	public String toConfirm() {
		HashMap<String, Object> criteria = Maps.newHashMap();
		criteria.put("backCode", backCode);
		Back backOrder = backService.getBack(criteria); // 查询出退货单
		List<BackGoodsVo> backGoodsList = backService.getBackGoodsList(criteria); // 详细单
		SalesOrder salesOrder = salesOrderService.getSalesOrderByCode(backOrder.getOrderCode()); // 订单

		// 查询发货imei
		ArrayList<Object> itemList = Lists.newArrayList();
		criteria.clear();
		criteria.put("orderCode", backOrder.getOrderCode());
		List<Indiv> indivList = indivService.getIndivList(criteria);
		for (Indiv indiv : indivList) {
			IndivScanItem item = new IndivScanItem();
			item.setIndivCode(indiv.getIndivCode());
			item.setSkuCode(indiv.getSkuCode());
			item.setSkuName(indiv.getSkuName());
			item.setNum(1);
			itemList.add(item);
		}

		ActionContext.getContext().getValueStack().set("backOrder", backOrder);
		ActionContext.getContext().getValueStack().set("backGoodsList", backGoodsList);
		ActionContext.getContext().getValueStack().set("salesOrder", salesOrder);
		ActionContext.getContext().getValueStack().set("itemList", itemList);

		return "confirmBack";
	}

	/**
	 * 上传
	 * @return
	 */
	public String toUpload() {
		return "upload";
	}

	/**
	 * 退货验收
	 */
	public void confirm() {
		try {
			backService.handleConfirmBack(orderBack);

			for (BackStatus s : BackStatus.values()) {
				if (orderBack.getBackStatus() == s.getCode()) {
					addLog("手工退换货：" + orderBack.getBackCode(), "单据状态变更为：" + s.getName());
				}
			}
			ajaxSuccess("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("退货验收出现异常！", e);
			ajaxError("操作出现异常！");
		}
	}

	/**
	 * 标记
	 */
	public void mark() {
		try {
			String mark = request.getParameter("mark");
			Back toUpBack = new Back();
			toUpBack.setId(Long.parseLong(request.getParameter("id")));
			toUpBack.setMark(Integer.parseInt(mark));

			backService.handleUpdate(toUpBack);

			addLog("手工退换货：" + backCode, "标记状态为(1：已标记，0：未标记)：" + mark);

			ajaxSuccess("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxError("操作出现异常！");
		}
	}

	/**
	 * 修改
	 */
	public void update() {
		try {
			backService.handleUpdate(orderBack);

			addLog("手工退换货：" + orderBack.getBackCode(), "修改单据！");

			ajaxSuccess("操作成功！");
		} catch (Exception e) {
			e.printStackTrace();
			ajaxError("操作出现异常！");
		}
	}

	/**
	 * 新增
	 * @return
	 */
	public void add() {
		try {
			if (CollectionUtils.isEmpty(goodsList)) {
				ajaxError("请添加商品！");
			}

			// 根据SKU去除重复商品
			List<String> skuList = Lists.newArrayList();
			for (Iterator<BackGoods> iterator = goodsList.iterator(); iterator.hasNext();) {
				BackGoods backGoods = iterator.next();
				if (skuList.contains(backGoods.getSkuCode())) {
					iterator.remove();
					continue;
				}
				skuList.add(backGoods.getSkuCode());
			}

			orderBack.setHandledBy(ActionUtils.getLoginName()); // 操作人
			orderBack.setHandledTime(new Date()); // 操作时间
			backService.handleAddBack(orderBack, goodsList);

			addLog("手工退换货：" + orderBack.getBackCode(), "新增退换货单！");

			ajaxSuccess("创建成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("新增退换货出现异常！", e);
			ajaxError("操作出现异常！");
		}
	}

	/**
	 * 查找订单
	 */
	public void lookupSalesOrder() {
		try {
			Map<String, Object> criteria = Maps.newHashMap();
			criteria.put("orderCode", orderCode);
			List<Back> backList = backService.getBackList(criteria, Page.getPage(1, 20, 20)); // 校验订单是否已经申请了退换货
			for (Back b : backList) {
				if (b.getBackStatus().intValue() != BackStatus.CANCELED.getCode()) {
					ajaxError("该订单已经申请了退换货，请不要重复申请！");
					return;
				}
			}

			SalesOrder salesOrder = salesOrderService.getSalesOrderByCode(orderCode);
			if (salesOrder == null) {
				ajaxError("订单号：" + orderCode + "，不存在！");
				return;
			}

			/*if (salesOrder.getOrderStatus() != OrderStatus.RECEIVED.getCode() && salesOrder.getOrderStatus() != OrderStatus.SHIPPED.getCode()) {
				ajaxError("未出库或者未签收状态的订单不能申请退换货！");
				return;
			}*/

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
			logger.error("退换货查找订单出现异常！", e);
			ajaxError("程序出现异常！");
		}
	}

	/**
	 * 查看
	 * @return
	 */
	public String toView() {
		HashMap<String, Object> criteria = Maps.newHashMap();
		criteria.put("backCode", backCode);
		Back backOrder = backService.getBack(criteria); // 查询出退货单
		List<BackGoodsVo> backGoodsList = backService.getBackGoodsList(criteria); // 详细单
		SalesOrder salesOrder = salesOrderService.getSalesOrderByCode(backOrder.getOrderCode()); // 订单

		// 查询发货imei
		ArrayList<Object> itemList = Lists.newArrayList();
		criteria.clear();
		criteria.put("orderCode", backOrder.getOrderCode());
		List<Indiv> indivList = indivService.getIndivList(criteria);
		for (Indiv indiv : indivList) {
			IndivScanItem item = new IndivScanItem();
			item.setIndivCode(indiv.getIndivCode());
			item.setSkuCode(indiv.getSkuCode());
			item.setSkuName(indiv.getSkuName());
			item.setNum(1);
			itemList.add(item);
		}

		criteria.clear();
		criteria.put("flowCode", backCode);
		List<IndivFlow> indivFlowList = indivService.getIndivFlowList(criteria, Page.getPage(1, 400, 400)); // 退货串号明细

		List<Log> logs = null;
		try {
			Map<String, Object> logParams = Maps.newHashMap();
			logParams.put("opName_hit", "手工退换货：" + backCode);
			logParams.put("endRow", 100);
			logParams.put("startRow", 1);
			logs = logService.selectPagedLogs(logParams);
		} catch (Exception e) {
			e.printStackTrace();
		}

		ActionContext.getContext().getValueStack().set("backOrder", backOrder);
		ActionContext.getContext().getValueStack().set("backGoodsList", backGoodsList);
		ActionContext.getContext().getValueStack().set("salesOrder", salesOrder);
		ActionContext.getContext().getValueStack().set("itemList", itemList);
		ActionContext.getContext().getValueStack().set("indivFlowList", indivFlowList);
		ActionContext.getContext().getValueStack().set("logs", logs == null ? new ArrayList<Log>() : logs);

		return "view";
	}

	/**
	 * 查找串号
	 */
	public void lookupIndiv() {
		try {
			Indiv indiv = indivService.getIndivByCode(indivCode);
			if (indiv == null) {
				ajaxError("串号不存在!");
				return;
			}

			Integer stockStatus = indiv.getStockStatus();
			if (IndivStockStatus.OUT_WAREHOUSE.getCode() != stockStatus) { // 非出库状态的串号拒绝录入
				ajaxError("串号：" + indivCode + " 已经在库或者出库中，不能操作！");
				return;
			}

			// 提示：没有做串号和退货订单的完全关联
			ajaxObject(indiv);
		} catch (ServiceException e) {
			e.printStackTrace();
			logger.error("退换货查找串号出现异常！", e);
			ajaxError("程序出现异常！");
		}
	}

	/**
	 * 取消退货单
	 */
	public void cancel() {
		try {
			OperateOrderRequest request = new OperateOrderRequest();
			request.setBackCode(backCode);
			backService.cancelBack(request);

			addLog("手工退换货：" + backCode, "取消退换货单！");

			ajaxSuccess("取消成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("取消退换货单出现异常！", e);
			ajaxError("程序出现异常！");
		}
	}

	/**
	 * 删除
	 */
	public void delete() {
		try {
			backService.handleDelete(backCode);

			addLog("手工退换货：" + backCode, "删除退换货单！");
			ajaxSuccess("删除成功！");
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("删除退换货单出现异常！", e);
			ajaxError("程序出现异常！");
		}
	}

	@Override
	public void prepare() throws Exception {
	}

	/**
	 * 写入操作日志
	 * @param opName
	 * @param content
	 */
	private void addLog(String opName, String content) {
		try {
			Log log = new Log();
			log.setIp(ActionUtils.getRemoteAddr(request));
			log.setOpTime(new Date());
			log.setOpUserName(ActionUtils.getLoginName());
			log.setType(LogType.OP_LOG.getCode());
			log.setOpName(opName);
			log.setContent(content);

			logService.insertLog(log);
		} catch (Exception e) {
			// ignore exception
			e.printStackTrace();
		}
	}

	/***** get set *****/

	/**
	 * @return the backCode
	 */
	public String getBackCode() {
		return backCode;
	}

	/**
	 * @param backCode the backCode
	 */
	public void setBackCode(String backCode) {
		this.backCode = backCode;
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

	/**
	 * @return the backStatus
	 */
	public Integer getBackStatus() {
		return backStatus;
	}

	/**
	 * @param backStatus the backStatus
	 */
	public void setBackStatus(Integer backStatus) {
		this.backStatus = backStatus;
	}

	/**
	 * @return the shippingNo
	 */
	public String getShippingNo() {
		return shippingNo;
	}

	/**
	 * @param shippingNo the shippingNo
	 */
	public void setShippingNo(String shippingNo) {
		this.shippingNo = shippingNo;
	}

	/**
	 * @return the backTimeBegin
	 */
	public Date getBackTimeBegin() {
		return backTimeBegin;
	}

	/**
	 * @param backTimeBegin the backTimeBegin
	 */
	public void setBackTimeBegin(Date backTimeBegin) {
		this.backTimeBegin = backTimeBegin;
	}

	/**
	 * @return the backTimeEnd
	 */
	public Date getBackTimeEnd() {
		return backTimeEnd;
	}

	/**
	 * @param backTimeEnd the backTimeEnd
	 */
	public void setBackTimeEnd(Date backTimeEnd) {
		this.backTimeEnd = backTimeEnd;
	}

	/**
	 * @return the orderBack
	 */
	public Back getOrderBack() {
		return orderBack;
	}

	/**
	 * @param orderBack the orderBack
	 */
	public void setOrderBack(Back orderBack) {
		this.orderBack = orderBack;
	}

	/**
	 * @return the goodsList
	 */
	public List<BackGoods> getGoodsList() {
		return goodsList;
	}

	/**
	 * @param goodsList the goodsList
	 */
	public void setGoodsList(List<BackGoods> goodsList) {
		this.goodsList = goodsList;
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
	 * @return the indivCode
	 */
	public String getIndivCode() {
		return indivCode;
	}

	/**
	 * @param indivCode the indivCode
	 */
	public void setIndivCode(String indivCode) {
		this.indivCode = indivCode;
	}

	/**
	 * @return the indivList
	 */
	public List<IndivFlow> getIndivList() {
		return indivList;
	}

	/**
	 * @param indivList the indivList
	 */
	public void setIndivList(List<IndivFlow> indivList) {
		this.indivList = indivList;
	}

	/**
	 * @return the upload
	 */
	public File getUpload() {
		return upload;
	}

	/**
	 * @param upload the upload
	 */
	public void setUpload(File upload) {
		this.upload = upload;
	}

	/**
	 * @return the uploadContentType
	 */
	public String getUploadContentType() {
		return uploadContentType;
	}

	/**
	 * @param uploadContentType the uploadContentType
	 */
	public void setUploadContentType(String uploadContentType) {
		this.uploadContentType = uploadContentType;
	}

	/**
	 * @return the uploadFileName
	 */
	public String getUploadFileName() {
		return uploadFileName;
	}

	/**
	 * @param uploadFileName the uploadFileName
	 */
	public void setUploadFileName(String uploadFileName) {
		this.uploadFileName = uploadFileName;
	}

	/**
	 * @return the backedTimeBegin
	 */
	public Date getBackedTimeBegin() {
		return backedTimeBegin;
	}

	/**
	 * @param backedTimeBegin the backedTimeBegin
	 */
	public void setBackedTimeBegin(Date backedTimeBegin) {
		this.backedTimeBegin = backedTimeBegin;
	}

	/**
	 * @return the backedTimeEnd
	 */
	public Date getBackedTimeEnd() {
		return backedTimeEnd;
	}

	/**
	 * @param backedTimeEnd the backedTimeEnd
	 */
	public void setBackedTimeEnd(Date backedTimeEnd) {
		this.backedTimeEnd = backedTimeEnd;
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

}
