package com.gionee.wms.dto;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2013-9-18 下午3:09:09
 * @=======================================
 */
public class ShippingSummary {

	private Long shippingId;
	private String shippingName;
	private Integer count;
	private String shippingCode;// 配送方式编号
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
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public String getShippingCode() {
		return shippingCode;
	}
	public void setShippingCode(String shippingCode) {
		this.shippingCode = shippingCode;
	}
}
