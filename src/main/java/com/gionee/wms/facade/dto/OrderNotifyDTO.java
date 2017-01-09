package com.gionee.wms.facade.dto;

import java.util.List;

public class OrderNotifyDTO {
	private String orderSn;
	private String delverySn;
	private String orderStatus;
	private String shippingCode;
	private String shippingSn;
	private String shippingName;
	private String remark;
	private List<OrderNotifyGoodsDTO> goods;
	public List<OrderNotifyGoodsDTO> getGoods() {
		return goods;
	}
	public void setGoods(List<OrderNotifyGoodsDTO> goods) {
		this.goods = goods;
	}
	public String getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		this.orderStatus = orderStatus;
	}
	public String getOrderSn() {
		return orderSn;
	}
	public void setOrderSn(String orderSn) {
		this.orderSn = orderSn;
	}
	public String getDelverySn() {
		return delverySn;
	}
	public void setDelverySn(String delverySn) {
		this.delverySn = delverySn;
	}
	public String getShippingCode() {
		return shippingCode;
	}
	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}
	public String getShippingSn() {
		return shippingSn;
	}
	public void setShippingSn(String shippingSn) {
		this.shippingSn = shippingSn;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getShippingName() {
		return shippingName;
	}
	public void setShippingName(String shippingName) {
		this.shippingName = shippingName;
	}
	
}
