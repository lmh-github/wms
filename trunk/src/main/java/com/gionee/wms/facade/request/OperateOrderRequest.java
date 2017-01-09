package com.gionee.wms.facade.request;

import java.io.Serializable;
import java.util.List;

import com.gionee.wms.facade.dto.OrderGoodsDTO;

public class OperateOrderRequest implements Serializable {
	private static final long serialVersionUID = 6459294146717042264L;

	/**
	 * 时间戳
	 */
	private String timestamp;

	/**
	 * 签名
	 */
	private String signature;
	
	/**
	 * 应用标识
	 */
	private String appFlag;
	
	/**
	 * 操作标志
	 * BACK（退货）
	 * CANCELBACK(取消退货)
	 */
	private String operFlag;
	
	private String backCode;
	private String shippingCode;
	private String shippingNo;
	private String orderCode;
	private String remark;

	private List<OrderGoodsDTO> goods;

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getSignature() {
		return signature;
	}

	public void setSignature(String signature) {
		this.signature = signature;
	}

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

	public String getOperFlag() {
		return operFlag;
	}

	public void setOperFlag(String operFlag) {
		this.operFlag = operFlag;
	}

	public List<OrderGoodsDTO> getGoods() {
		return goods;
	}

	public void setGoods(List<OrderGoodsDTO> goods) {
		this.goods = goods;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getAppFlag() {
		return appFlag;
	}

	public void setAppFlag(String appFlag) {
		this.appFlag = appFlag;
	}

}
