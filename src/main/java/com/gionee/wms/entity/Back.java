package com.gionee.wms.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Back {
	private Long id;
	private String backCode;
	private String shippingCode;
	private String shippingNo;
	private String orderCode;
	private String remarkBacking;
	private String remarkBacked;
	private String handledBy;
	private Date handledTime;
	private Date createTime;
	private Integer backStatus;
	private String warehouseCode;// 仓库编号
	/** 是否有检测单 */
	private Integer hasTestBill;
	/** 退货平台 */
	private String backPlatform;
	/** 换货单号 */
	private String exchangeShippingNo;
	/** 承担方 */
	private String bearParty;
	/** 退款金额 */
	private BigDecimal backMoney;
	/** 退货单类型 */
	private String backType;
	/** 备注 */
	private String remark;
	/** 订单来源 */
	private String orderSource;
	/** 退货时间 */
	private Date backedTime;
	/** 是否要退回发票 */
	private Integer invoice;
	/** 退换货商品 */
	private List<BackGoods> backGoods = new ArrayList<BackGoods>();
	/** 销售订单 */
	private SalesOrder salesOrder;
	/** 标记 */
	private Integer mark;

	public String getBackCode() {
		return backCode;
	}

	public void setBackCode(String backCode) {
		this.backCode = backCode;
	}

	public String getShippingCode() {
		return shippingCode;
	}

	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}

	public String getShippingNo() {
		return shippingNo;
	}

	public void setShippingNo(String shippingNo) {
		this.shippingNo = shippingNo;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getRemarkBacking() {
		return remarkBacking;
	}

	public void setRemarkBacking(String remarkBacking) {
		this.remarkBacking = remarkBacking;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRemarkBacked() {
		return remarkBacked;
	}

	public void setRemarkBacked(String remarkBacked) {
		this.remarkBacked = remarkBacked;
	}

	public String getHandledBy() {
		return handledBy;
	}

	public void setHandledBy(String handledBy) {
		this.handledBy = handledBy;
	}

	public Date getHandledTime() {
		return handledTime;
	}

	public void setHandledTime(Date handledTime) {
		this.handledTime = handledTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Integer getBackStatus() {
		return backStatus;
	}

	public void setBackStatus(Integer backStatus) {
		this.backStatus = backStatus;
	}

	public String getWarehouseCode() {
		return warehouseCode;
	}

	public void setWarehouseCode(String warehouseCode) {
		this.warehouseCode = warehouseCode;
	}

	/**
	 * @return the hasTestBill
	 */
	public Integer getHasTestBill() {
		return hasTestBill;
	}

	/**
	 * @param hasTestBill the hasTestBill
	 */
	public void setHasTestBill(Integer hasTestBill) {
		this.hasTestBill = hasTestBill;
	}

	/**
	 * @return the backPlatform
	 */
	public String getBackPlatform() {
		return backPlatform;
	}

	/**
	 * @param backPlatform the backPlatform
	 */
	public void setBackPlatform(String backPlatform) {
		this.backPlatform = backPlatform;
	}

	/**
	 * @return the exchangeShippingNo
	 */
	public String getExchangeShippingNo() {
		return exchangeShippingNo;
	}

	/**
	 * @param exchangeShippingNo the exchangeShippingNo
	 */
	public void setExchangeShippingNo(String exchangeShippingNo) {
		this.exchangeShippingNo = exchangeShippingNo;
	}

	/**
	 * @return the bearParty
	 */
	public String getBearParty() {
		return bearParty;
	}

	/**
	 * @param bearParty the bearParty
	 */
	public void setBearParty(String bearParty) {
		this.bearParty = bearParty;
	}

	/**
	 * @return the backMoney
	 */
	public BigDecimal getBackMoney() {
		return backMoney;
	}

	/**
	 * @param backMoney the backMoney
	 */
	public void setBackMoney(BigDecimal backMoney) {
		this.backMoney = backMoney;
	}

	/**
	 * @return the backType
	 */
	public String getBackType() {
		return backType;
	}

	/**
	 * @param backType the backType
	 */
	public void setBackType(String backType) {
		this.backType = backType;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark the remark
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the orderSource
	 */
	public String getOrderSource() {
		return orderSource;
	}

	/**
	 * @param orderSource the orderSource
	 */
	public void setOrderSource(String orderSource) {
		this.orderSource = orderSource;
	}

	/**
	 * @return the backedTime
	 */
	public Date getBackedTime() {
		return backedTime;
	}

	/**
	 * @param backedTime the backedTime
	 */
	public void setBackedTime(Date backedTime) {
		this.backedTime = backedTime;
	}

	/**
	 * @return the invoice
	 */
	public Integer getInvoice() {
		return invoice;
	}

	/**
	 * @param invoice the invoice
	 */
	public void setInvoice(Integer invoice) {
		this.invoice = invoice;
	}

	/**
	 * @return the backGoods
	 */
	public List<BackGoods> getBackGoods() {
		return backGoods;
	}

	/**
	 * @param backGoods the backGoods
	 */
	public void setBackGoods(List<BackGoods> backGoods) {
		this.backGoods = backGoods;
	}

	/**
	 * @return the salesOrder
	 */
	public SalesOrder getSalesOrder() {
		return salesOrder;
	}

	/**
	 * @param salesOrder the salesOrder
	 */
	public void setSalesOrder(SalesOrder salesOrder) {
		this.salesOrder = salesOrder;
	}

	/**
	 * @return the mark
	 */
	public Integer getMark() {
		return mark;
	}

	/**
	 * @param mark the mark
	 */
	public void setMark(Integer mark) {
		this.mark = mark;
	}

}
