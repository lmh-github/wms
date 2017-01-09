/* @(#)LogInterceptor.java 2014-1-8
 *
 * Copyright 2013 Shenzhen Gionee,Inc. All rights reserved.
 */

package com.gionee.wms.web.filter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.gionee.wms.common.JsonUtil;
import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.dto.ShiroUser;
import com.gionee.wms.entity.Log;
import com.gionee.wms.service.ServiceException;
import com.gionee.wms.service.log.LogService;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.AbstractInterceptor;

/**
 * 日志拦截器
 * @author ZuoChangjun 2014-1-8
 */
public class LogInterceptor extends AbstractInterceptor {

	@Autowired
	private LogService logService;
	private static final long serialVersionUID = 1358600090729208361L;

	public String intercept(ActionInvocation invocation) {
		//System.out.println("日志拦截器已经开始启动...");
		String fullClassName = invocation.getAction().getClass().toString();
		String shortClassName = fullClassName.substring(fullClassName
				.lastIndexOf(".") + 1);
		String methodName = invocation.getProxy().getMethod();
		Map parameters = invocation.getInvocationContext().getParameters();
		boolean result = invocation.getProxy().getExecuteResult();

		HttpServletRequest request = ServletActionContext.getRequest();
		String ip = request.getRemoteAddr();

		ActionContext ctx = invocation.getInvocationContext();
		Map session = ctx.getSession();

		ShiroUser user = (ShiroUser) session.get("user");
		if (user != null && isRequiredLog(methodName) && StringUtils.isNotBlank(getOpName(shortClassName, methodName))) {
			try {
				Log opLog = new Log();
				opLog.setType(WmsConstants.LogType.OP_LOG.getCode());
				opLog.setOpName(getOpName(shortClassName, methodName));
				String paramsJSON = JsonUtil.toJsonStr(parameters);
				if (StringUtils.isNotBlank(paramsJSON)) {
					paramsJSON = paramsJSON.replace("[", "").replace("]", "");
				}
				opLog.setContent(shortClassName + "|" + methodName + "|"
						+ paramsJSON + "|" + result);
				opLog.setIp(ip);
				opLog.setOpUserName(user.getLoginName());
				opLog.setOpUserId(user.getId() + "");
				opLog.setOpTime(new Date(System.currentTimeMillis()));
				this.logService.insertLog(opLog);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		try {
			invocation.invoke();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "success";
	}

	/**
	 * 判断是否是添加操作
	 * 
	 * @param methodName
	 * @return
	 */
	public boolean isAdd(String methodName) {
		if (StringUtils.isBlank(methodName)) {
			throw new ServiceException("非法方法");
		}
		if (methodName.startsWith("add") || methodName.startsWith("insert")
				|| methodName.startsWith("create")
				|| methodName.startsWith("confirm")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断是否是编辑操作
	 * 
	 * @param methodName
	 * @return
	 */
	public boolean isEdit(String methodName) {
		if (StringUtils.isBlank(methodName)) {
			throw new ServiceException("非法方法");
		}
		if (methodName.startsWith("edit") || methodName.startsWith("update")
				|| methodName.startsWith("set")
				|| methodName.startsWith("enable")
				|| methodName.startsWith("disable")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断是否是删除操作
	 * 
	 * @param methodName
	 * @return
	 */
	public boolean isDelete(String methodName) {
		if (StringUtils.isBlank(methodName)) {
			throw new ServiceException("非法方法");
		}
		if (methodName.startsWith("remove") || methodName.startsWith("delete")
				|| methodName.startsWith("del")
				|| methodName.startsWith("cancel")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断是否是打印操作
	 * 
	 * @param methodName
	 * @return
	 */
	public boolean isPrint(String methodName) {
		if (StringUtils.isBlank(methodName)) {
			throw new ServiceException("非法方法");
		}
		if (methodName.contains("print") || methodName.contains("preview")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 其它非查询操作
	 * 
	 * @param methodName
	 * @return
	 */
	public boolean isOther(String methodName) {
		if (StringUtils.isBlank(methodName)) {
			throw new ServiceException("非法方法");
		}
		if (methodName.startsWith("check") || methodName.startsWith("reDescribe")
				|| methodName.startsWith("handle")
				|| methodName.startsWith("ready")
				|| methodName.startsWith("toScan")) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * 判断是否是登录和注销操作
	 * 
	 * @param methodName
	 * @return
	 */
	public boolean isLoginAndLogout(String methodName) {
		if (StringUtils.isBlank(methodName)) {
			throw new ServiceException("非法方法");
		}
		if ("index".equals(methodName) || "logout".equals(methodName)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断是否是查询操作
	 * 
	 * @param methodName
	 * @return
	 */
	public boolean isQuery(String methodName) {
		if (StringUtils.isBlank(methodName)) {
			throw new ServiceException("非法方法");
		}
		if ("execute".equals(methodName) || methodName.startsWith("get")
				|| methodName.startsWith("find")
				|| methodName.startsWith("query")
				|| methodName.startsWith("select")
				|| methodName.startsWith("search")
				|| methodName.startsWith("list")
				|| methodName.startsWith("input")
				|| methodName.startsWith("lookup")) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 判断是否需要记录日志
	 * 
	 * @param methodName
	 * @return
	 */
	public boolean isRequiredLog(String methodName) {
		if (StringUtils.isBlank(methodName)) {
			throw new ServiceException("非法方法");
		}
		if (isLoginAndLogout(methodName) || isAdd(methodName)
				|| isEdit(methodName) || isDelete(methodName)
				/*|| isPrint(methodName)*/ || isOther(methodName)) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 获取操作名
	 * 
	 * @param className
	 * @param methodName
	 * @return
	 */
	public String getOpName(String className, String methodName) {
		return opMap.get(className + "." + methodName);
	}

	
	private static Map<String, String> opMap = new HashMap<String, String>();
	static {
		opMap.put("LoginAction.index", "登录");
		opMap.put("LoginAction.logout", "注销");	
	    //basis
		opMap.put("PrintableTemplateAction.updateShoppingListTemplate", "更新购物清单模板");
		opMap.put("PrintableTemplateAction.updateShippingTemplate", "更新运单模板");
		
		opMap.put("ShippingAction.add", "添加配送方式");
		opMap.put("ShippingAction.update", "更新配送方式");
		opMap.put("ShippingAction.delete", "删除配送方式");
		opMap.put("ShippingAction.setDefault", "设置默认配送方式");
		opMap.put("ShippingAction.enable", "启用配送方式");
		opMap.put("ShippingAction.disable", "停用配送方式");
		
		opMap.put("SupplierAction.add", "添加供应商");
		opMap.put("SupplierAction.update", "更新供应商");
		opMap.put("SupplierAction.delete", "删除供应商");
		opMap.put("SupplierAction.setDefault", "设置默认供应商");
		opMap.put("SupplierAction.enable", "启用供应商");
		opMap.put("SupplierAction.disable", "停用供应商");
		
		opMap.put("WarehouseAction.add", "添加仓库");
		opMap.put("WarehouseAction.update", "更新仓库");
		opMap.put("WarehouseAction.delete", "删除仓库");
		opMap.put("WarehouseAction.setDefault", "设置默认仓库");
		opMap.put("WarehouseAction.enable", "启用仓库");
		opMap.put("WarehouseAction.disable", "停用仓库");
		
		//report
		opMap.put("KuaidiReportAction.reDescribe", "重新订阅快递信息");
		//stock
		opMap.put("BackAction.handledBack", "处理已退货信息");
		
		opMap.put("DeliveryAction.updateShippingInfo", "添加发货配送信息");
		opMap.put("DeliveryAction.addInvoiceInfo", "添加发货发票信息");
		opMap.put("DeliveryAction.toScan", "PDA扫描");
		opMap.put("DeliveryAction.checkKuaidi", "PDA操作新增发货批次信息或更新订单状态");
		opMap.put("DeliveryAction.confirmDelivery", "确认发货");
		opMap.put("DeliveryAction.confirmDeliveryWap", "PDA确认发货");
		
		opMap.put("DeliveryBatchAction.add", "创建拣货批次");
		opMap.put("DeliveryBatchAction.updateShippingInfo", "批量更新拣货批次内的订单配送信息");
		opMap.put("DeliveryBatchAction.confirm", "确认拣货批次");
		opMap.put("DeliveryBatchAction.update", "更新拣货批次信息");
		opMap.put("DeliveryBatchAction.cancel", "取消拣货批次");
		
		opMap.put("PurchaseRecvAction.addGoods", "添加收货商品");
		opMap.put("PurchaseRecvAction.updateGoods", "更新自动收货商品");
		opMap.put("PurchaseRecvAction.updateGoodsManual", "更新手动收货商品");
		opMap.put("PurchaseRecvAction.deleteGoods", "删除收货商品");
		opMap.put("PurchaseRecvAction.createReceive", "根据采购预收单自动创建收货单");
		opMap.put("PurchaseRecvAction.createReceiveManual", "手动创建收货单");
		opMap.put("PurchaseRecvAction.confirmReceive", "确认收货");
		
		opMap.put("PurPreRecvAction.createReceive", "根据采购预收单创建收货单");
		
		opMap.put("RmaInAction.addIndivItem", "添加入库明细项(有编号商品)");
		opMap.put("RmaInAction.updateInItem", "更新入库明细项");
		opMap.put("RmaInAction.addInItem", "添加入库明细项(无编号商品)");	
		opMap.put("RmaInAction.deleteStockInItem", "删除入库明细项");
		opMap.put("RmaInAction.add", "添加入库单");
		opMap.put("RmaInAction.update", "添加退货入库单成功");
		opMap.put("RmaInAction.delete", "删除入库单");
		opMap.put("RmaInAction.cancel", "取消入库单");
		
		opMap.put("RmaRecvAction.updateGoods", "编辑收货商品");
		opMap.put("RmaRecvAction.add", "创建退货入库单");
		opMap.put("RmaRecvAction.confirm", "保存收货单");
		
		opMap.put("SalesOrderAction.add", "新增订单");
		opMap.put("SalesOrderAction.update", "更新订单");
		opMap.put("SalesOrderAction.updateShippingInfo", "批量更新发货批次内的订单配送信息");
		opMap.put("SalesOrderAction.setInvoiceStatus", "批量更新发货批次内的订单配送信息");
		opMap.put("SalesOrderAction.previewShipping", "打印预览运单");
		opMap.put("SalesOrderAction.batchPreviewShipping", "批量打印预览运单");
		opMap.put("SalesOrderAction.previewPicking", "打印拣货清单");
		opMap.put("SalesOrderAction.printInvoice", "打印发票");
		opMap.put("SalesOrderAction.orderPrint", "打印订单");
		opMap.put("SalesOrderAction.addDeliveryBatch", "创建发货批次");
		opMap.put("SalesOrderAction.cancelOrder", "取消订单");
		
		opMap.put("SalesPrepareAction.readyPrepare", "扫描发货单，准备进行配货");
		opMap.put("SalesPrepareAction.confirm", "配货完成，确认提交");
		
		opMap.put("ShortcutRecvAction.createReceive", "根据采购预收单创建收货单");	
		opMap.put("ShortcutRecvAction.confirmReceive", "保存收货单");
		
		opMap.put("StockAction.updateLimit", "设置安全库存");
		
		opMap.put("StockCheckAction.add", "创建盘点单");
		opMap.put("StockCheckAction.addGoods", "批量添加盘点商品");
		opMap.put("StockCheckAction.deleteGoods", "删除盘点商品");
		opMap.put("StockCheckAction.delete", "删除盘点单");
		opMap.put("StockCheckAction.confirm", "确认盘点单");
		
		opMap.put("StockInAction.updateStockInItem", "更新入库明细项");
		opMap.put("StockInAction.addStockInItem", "添加入库明细项");
		opMap.put("StockInAction.deleteStockInItem", "删除入库明细项");
		opMap.put("StockInAction.update", "更新入库单");
		opMap.put("StockInAction.delete", "删除入库单");
		opMap.put("StockInAction.cancel", "取消入库单");

		opMap.put("StockOutAction.confirm", "确认出库");
		opMap.put("StockOutAction.update", "更新出库单");
		opMap.put("StockOutAction.addStockOutWithOrders", "批量订单生成出库单");
		opMap.put("StockOutAction.removeOrder", "从出库单中移除指定的销售订单");
		opMap.put("StockOutAction.addStockOutItem", "添加出库明细项");
		opMap.put("StockOutAction.removeSku", "从出库单中移除指定的SKU");
		opMap.put("StockOutAction.cancel", "取消出库单");
	
		opMap.put("TransferAction.add", "添加调拨单");
		opMap.put("TransferAction.update", "编辑调拨单");
		opMap.put("TransferAction.delete", "删除调拨单");
		opMap.put("TransferAction.addGoods", "添加调拨商品");
		opMap.put("TransferAction.deleteGoods", "删除调拨商品");
		opMap.put("TransferAction.printTransfer", "批量打印预览购物清单");
		
		opMap.put("TransPrepareAction.addIndiv", "调拨商品扫描");
		opMap.put("TransPrepareAction.confirm", "确认调拨配货");
		
		//wares
		opMap.put("AttrSetAction.add", "添加属性集");
		opMap.put("AttrSetAction.update", "编辑属性集");
		opMap.put("AttrSetAction.delete", "删除属性集");
		
		opMap.put("CategoryAction.add", "创建商品分类");
		opMap.put("CategoryAction.update", "更新商品分类");
		opMap.put("CategoryAction.delete", "删除商品分类");
		
		opMap.put("IndivAction.update", "更新商品个体");
		
		opMap.put("SkuAction.add", "添加SKU");
		opMap.put("SkuAction.update", "更新SKU");
		opMap.put("SkuAction.delete", "删除SKU");
		
		opMap.put("SkuAttrAction.add", "添加SKU属性");
		opMap.put("SkuAttrAction.update", "更新SKU属性");
		opMap.put("SkuAttrAction.delete", "删除SKU属性");
		
		opMap.put("SkuMapAction.add", "添加SKU与第三方编码的映射");
		opMap.put("SkuMapAction.update", "更新SKU与第三方编码的映射");
		opMap.put("SkuMapAction.delete", "删除SKU与第三方编码的映射");
		
		opMap.put("WaresAction.add", "添加商品");
		opMap.put("WaresAction.update", "更新商品");
		opMap.put("WaresAction.delete", "删除商品");
	}
}
