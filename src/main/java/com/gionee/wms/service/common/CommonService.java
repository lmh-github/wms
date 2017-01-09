package com.gionee.wms.service.common;


public interface CommonService {
	/**
	 * 采购预收单
	 */
	public static final int PUR_PRE_RECV = 1;
	/**
	 * 采购入库单
	 */
	public static final int PURCHASE_IN = 2;
	/**
	 * 快捷入库单
	 */
	public static final int SHORTCUT_IN = 7;
	/**
	 * 退货入库单
	 */
	public static final int RMA_IN = 3;
	/**
	 * 出库批次单
	 */
	public static final int BATCH_OUT = 4;
	/**
	 * 发货单
	 */
	public static final int DELIVERY = 5;
	/**
	 * 库存盘点单
	 */
	public static final int STOCK_CHECK = 6;
	
	/**
	 * 采购编号
	 */
	public static final int PURCHASE_NUM = 8;
	
	/**
	 * 调拨批次号
	 */
	public static final int TRANSFER = 9;
	/**
	 * 拒收入库单
	 */
	public static final int REFUSE_IN = 10;
	
	/** 退货 */
	public static final int ORDER_BACK = 11;

	/** 换货 */
	public static final int ORDER_EXCHANGE = 12;

	/**
	 * 取各业务流水号
	 * 
	 * @param bizType 业务类型
	 * @return
	 */
	String getBizCode(final int bizType,String... args);
}
