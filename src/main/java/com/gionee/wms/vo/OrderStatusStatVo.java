/*
 * @(#)OrderStatusStatVo.java 2014-1-21
 *
 * Copyright 2013 Shenzhen Gionee,Inc. All rights reserved.
 */
package com.gionee.wms.vo;

import com.gionee.wms.common.WmsConstants;

/**
 * 订单状态统计VO
 * 
 * @author ZuoChangjun 2014-1-21
 */
public class OrderStatusStatVo {
	private Long shippingId;// 物流公司Id
	private String shippingName;// 物流公司名

	private int canceledCount;// -1 取消数
	private int filteredCount;// 0 已筛单数
	private int printedCount;// 2 已打单数
	private int pickingCount;// 8 配货中数
	private int pickedCount;// 3 已配货数
	private int shippingCount;// 9 待出库数
	private int shippedCount;// 1 已出库数
	private int receivedCount;// 5 已签收数
	private int refuseingCount;// 7 拒收中数
	private int refusedCount;// 10 已拒收数
	private int backingCount;// 6 退货中数
	private int backedCount;// 4 已退货数
	
	
	public Long getShippingId() {
		return shippingId;
	}
	public void setShippingId(Long shippingId) {
		this.shippingId = shippingId;
	}
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	public int getCanceledCount() {
		return canceledCount;
	}
	public void setCanceledCount(int canceledCount) {
		this.canceledCount = canceledCount;
	}

	public int getPickingCount() {
		return pickingCount;
	}
	public void setPickingCount(int pickingCount) {
		this.pickingCount = pickingCount;
	}
	public int getPickedCount() {
		return pickedCount;
	}
	public void setPickedCount(int pickedCount) {
		this.pickedCount = pickedCount;
	}
	public int getShippingCount() {
		return shippingCount;
	}
	public void setShippingCount(int shippingCount) {
		this.shippingCount = shippingCount;
	}
	public int getShippedCount() {
		return shippedCount;
	}
	public void setShippedCount(int shippedCount) {
		this.shippedCount = shippedCount;
	}
	public int getFilteredCount() {
		return filteredCount;
	}
	public void setFilteredCount(int filteredCount) {
		this.filteredCount = filteredCount;
	}
	public int getPrintedCount() {
		return printedCount;
	}
	public void setPrintedCount(int printedCount) {
		this.printedCount = printedCount;
	}
	public int getReceivedCount() {
		return receivedCount;
	}
	public void setReceivedCount(int receivedCount) {
		this.receivedCount = receivedCount;
	}
	public int getRefuseingCount() {
		return refuseingCount;
	}
	public void setRefuseingCount(int refuseingCount) {
		this.refuseingCount = refuseingCount;
	}
	public int getRefusedCount() {
		return refusedCount;
	}
	public void setRefusedCount(int refusedCount) {
		this.refusedCount = refusedCount;
	}
	public int getBackingCount() {
		return backingCount;
	}
	public void setBackingCount(int backingCount) {
		this.backingCount = backingCount;
	}
	public int getBackedCount() {
		return backedCount;
	}
	public void setBackedCount(int backedCount) {
		this.backedCount = backedCount;
	}
	
	public void setOrderCount(OrderStatusVo orderStatusVo){
		if(orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.FILTERED.getCode()){
			this.setFilteredCount(orderStatusVo.getOrderCount());
		}
		else if(orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.PRINTED.getCode()){
			this.setPrintedCount(orderStatusVo.getOrderCount());
		}
		else if(orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.PICKED.getCode()){
			this.setPickedCount(orderStatusVo.getOrderCount());
		}
		else if(orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.SHIPPED.getCode()){
			this.setShippedCount(orderStatusVo.getOrderCount());
		}
		else if(orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.BACKED.getCode()){
			this.setBackedCount(orderStatusVo.getOrderCount());
		}
		else if(orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.RECEIVED.getCode()){
			this.setReceivedCount(orderStatusVo.getOrderCount());
		}
		else if(orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.BACKING.getCode()){
			this.setBackingCount(orderStatusVo.getOrderCount());
		}
		else if(orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.REFUSEING.getCode()){
			this.setRefuseingCount(orderStatusVo.getOrderCount());
		}
		else if(orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.PICKING.getCode()){
			this.setPickingCount(orderStatusVo.getOrderCount());
		}
		else if(orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.SHIPPING.getCode()){
			this.setShippingCount(orderStatusVo.getOrderCount());
		}
		else if(orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.REFUSED.getCode()){
			this.setRefusedCount(orderStatusVo.getOrderCount());
		}
		else if(orderStatusVo.getOrderStatus() == WmsConstants.OrderStatus.CANCELED.getCode()){
			this.setCanceledCount(orderStatusVo.getOrderCount());
		}
	}
}
