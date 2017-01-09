package com.gionee.wms.common;

/**
 * 渠道标识
 * 
 * @ClassName: ChannelEnum
 * @Description: 渠道标识
 * @author Kevin
 * @date 2011-8-23 下午05:16:31
 * 
 */
public class PermissionConstants {

	/**
	 * 商品分类
	 */
	public static final String CAT_ADD = "category:save";
	public static final String CAT_DELETE = "category:delete";
	public static final String CAT_EDIT = "category:edit";
	public static final String CAT_VIEW = "category:view";

	/**
	 * 属性集
	 */
	public static final String ATTRSET_ADD = "attrSet:save";
	public static final String ATTRSET_DELETE = "attrSet:delete";
	public static final String ATTRSET_EDIT = "attrSet:edit";
	public static final String ATTRSET_VIEW = "attrSet:view";

	/**
	 * 仓库管理
	 */
	public static final String WAREHOUSE_ADD = "warehouse:save";
	public static final String WAREHOUSE_EDIT = "warehouse:edit";
	public static final String WAREHOUSE_VIEW = "warehouse:view";
	/**
	 * 供应商管理
	 */
	public static final String SUPPLIER_ADD = "supplier:save";
	public static final String SUPPLIER_EDIT = "supplier:edit";
	public static final String SUPPLIER_VIEW = "supplier:view";
	/**
	 * 配送方式管理
	 */
	public static final String SHIPPING_ADD = "shipping:save";
	public static final String SHIPPING_EDIT = "shipping:edit";
	public static final String SHIPPING_VIEW = "shipping:view";
	public static final String SHIPPING_TEMPLATE_EDIT = "shipping:editTemplate";//编辑运单模板

	/**
	 * 盘点
	 */
	public static final String CHECK_VIEW = "stockCheck:view";//查看盘点单列表
	public static final String CHECK_ADD = "stockCheck:save"; //添加盘点单
	public static final String CHECK_DOWNLOAD = "stockCheck:download";//下载盘点明细
	public static final String CHECK_UPLOAD = "stockCheck:upload";//上传盘点结果
	public static final String CHECK_CONFIRM = "stockCheck:confirm";//确认盘点结果
	
	/**
	 * 入库
	 */
	public static final String IN_VIEW = "in:view";//查看入库单列表
	public static final String PURCHASE_IN_ADD = "purchaseIn:save";//添加采购入库单
	public static final String PURCHASE_IN_DELETE = "purchaseIn:delete";//删除采购入库单
	public static final String PURCHASE_IN_EDIT = "purchaseIn:edit";//编辑采购入库单
	public static final String RMA_IN_ADD = "rmaIn:save";//添加退货入库单
	public static final String RMA_IN_DELETE = "rmaIn:delete";//删除退货入库单
	public static final String RMA_IN_EDIT = "rmaIn:edit";//编辑退货入库单
	
	/**
	 * 出库
	 */
	public static final String OUT_VIEW = "out:view"; //查看出库单列表
	public static final String SALES_OUT_ADD = "out:addSalesOut";//创建出库批次
	public static final String SALES_OUT_DELETE = "out:deleteSalesOut";//删除出库批次
	public static final String SALES_OUT_CONFIRM = "out:confirmSalesOut";//确认销售出库
//	public static final String SALES_OUT_SUMMARY_VIEW = "out:viewSalesOutSummary";//查看销售出库明细汇总
	public static final String SALES_OUT_SUMMARY_PRINT = "out:printSalesOutSummary";//打印销售出库汇总
	public static final String SALES_OUT_SHIPPING_EDIT = "out:editSalesOutShipping";//编辑配送信息
	public static final String SALES_OUT_INVOICE_EDIT = "out:editSalesOutInvoice";//编辑发票状态
	public static final String SALES_OUT_INDIV_EDIT = "out:editSalesOutIndiv";//编辑商品编码
	
	/**
	 * 销售订单
	 */
	public static final String SALES_ORDER_VIEW = "salesOrder:view"; //查看销售订单列表
	
	/**
	 * 打印模板
	 */
	public static final String TEMPLATE_SHOPPING_LIST_EDIT = "printableTemplate:editShoppingListTemplate"; //编辑购物清单模板
	public static final String TEMPLATE_SHIPPING_EDIT = "printableTemplate:editShippingTemplate"; //编辑运单模板

}