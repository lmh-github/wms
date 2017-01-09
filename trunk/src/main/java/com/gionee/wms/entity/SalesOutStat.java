/**
* @author jay_liang
* @date 2014-4-24 下午7:50:28
*/
package com.gionee.wms.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @=======================================
 * @Description TODO 
 * @author jay_liang
 * @date 2014-4-24 下午7:50:28
 * @=======================================
 */
public class SalesOutStat {
	private String id;
	private Long skuId;
	private String skuCode;
	private String skuName;
	private Integer quantity;
	private BigDecimal unitPrice;
	private Integer indivEnabled;
	private Long orderId;
	private String orderCode;
	private String payNo;
	private Date shippingTime;
	private String partnerCode;
	private String partnerName;
	private Integer outType;
	private BigDecimal orderAmount;
	private String materialCode;
	private BigDecimal invoiceAmount;
	private Integer invoiceStatus;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Long getSkuId() {
		return skuId;
	}
	public void setSkuId(Long skuId) {
		this.skuId = skuId;
	}
	public String getSkuCode() {
		return skuCode;
	}
	public void setSkuCode(String skuCode) {
		this.skuCode = skuCode;
	}
	public String getSkuName() {
		return skuName;
	}
	public void setSkuName(String skuName) {
		this.skuName = skuName;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Integer getIndivEnabled() {
		return indivEnabled;
	}
	public void setIndivEnabled(Integer indivEnabled) {
		this.indivEnabled = indivEnabled;
	}
	public Long getOrderId() {
		return orderId;
	}
	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getPayNo() {
		return payNo;
	}
	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}
	public Date getShippingTime() {
		return shippingTime;
	}
	public void setShippingTime(Date shippingTime) {
		this.shippingTime = shippingTime;
	}
	public String getPartnerCode() {
		return partnerCode;
	}
	public void setPartnerCode(String partnerCode) {
		this.partnerCode = partnerCode;
	}
	public String getPartnerName() {
		return partnerName;
	}
	public void setPartnerName(String partnerName) {
		this.partnerName = partnerName;
	}
	public Integer getOutType() {
		return outType;
	}
	public void setOutType(Integer outType) {
		this.outType = outType;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public Integer getInvoiceStatus() {
		return invoiceStatus;
	}
	public void setInvoiceStatus(Integer invoiceStatus) {
		this.invoiceStatus = invoiceStatus;
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public BigDecimal getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(BigDecimal invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
}
