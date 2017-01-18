package com.gionee.wms.entity;

import java.math.BigDecimal;
import java.util.Date;

public class SaleStat {
	private Long id;
	private String orderType;//订单类型
	private String saleOrg;//销售组织
	private String fxChannel;//分销渠道
	private String saler;//售达方
	private String sender;//送达方
	private String invoicer;//开票方
	private String shipper;//承运方
	private String orderReason;//订单原因
	private String materialCode;//物料编码
	private Integer orderNum;//订单数量
	private String purchaseCode;//采购订单号
	private Date purchaseDate;//采购日期
	private String shippingType;//运输方式
	private String invoiceType;//开票类型
	private String use;//使用
	private String poCode;//PO编号
	private String poProCode;//PO项目编号
	private BigDecimal unitPrice;//单价
	private Date postingDate;//凭证日期
	private String factory;//工厂
	private String warehouse;//仓库
	private Date statDate;//统计日期
	private Date createTime;//创建日期
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getOrderType() {
		return orderType;
	}
	public void setOrderType(String orderType) {
		this.orderType = orderType;
	}
	public String getSaleOrg() {
		return saleOrg;
	}
	public void setSaleOrg(String saleOrg) {
		this.saleOrg = saleOrg;
	}
	public String getFxChannel() {
		return fxChannel;
	}
	public void setFxChannel(String fxChannel) {
		this.fxChannel = fxChannel;
	}
	public String getSaler() {
		return saler;
	}
	public void setSaler(String saler) {
		this.saler = saler;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public String getInvoicer() {
		return invoicer;
	}
	public void setInvoicer(String invoicer) {
		this.invoicer = invoicer;
	}
	public String getShipper() {
		return shipper;
	}
	public void setShipper(String shipper) {
		this.shipper = shipper;
	}
	public String getOrderReason() {
		return orderReason;
	}
	public void setOrderReason(String orderReason) {
		this.orderReason = orderReason;
	}
	public String getMaterialCode() {
		return materialCode;
	}
	public void setMaterialCode(String materialCode) {
		this.materialCode = materialCode;
	}
	public Integer getOrderNum() {
		return orderNum;
	}
	public void setOrderNum(Integer orderNum) {
		this.orderNum = orderNum;
	}
	public String getPurchaseCode() {
		return purchaseCode;
	}
	public void setPurchaseCode(String purchaseCode) {
		this.purchaseCode = purchaseCode;
	}
	public Date getPurchaseDate() {
		return purchaseDate;
	}
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}
	public String getShippingType() {
		return shippingType;
	}
	public void setShippingType(String shippingType) {
		this.shippingType = shippingType;
	}
	public String getInvoiceType() {
		return invoiceType;
	}
	public void setInvoiceType(String invoiceType) {
		this.invoiceType = invoiceType;
	}
	public String getUse() {
		return use;
	}
	public void setUse(String use) {
		this.use = use;
	}
	public String getPoCode() {
		return poCode;
	}
	public void setPoCode(String poCode) {
		this.poCode = poCode;
	}
	public String getPoProCode() {
		return poProCode;
	}
	public void setPoProCode(String poProCode) {
		this.poProCode = poProCode;
	}
	public BigDecimal getUnitPrice() {
		return unitPrice;
	}
	public void setUnitPrice(BigDecimal unitPrice) {
		this.unitPrice = unitPrice;
	}
	public Date getPostingDate() {
		return postingDate;
	}
	public void setPostingDate(Date postingDate) {
		this.postingDate = postingDate;
	}
	public String getFactory() {
		return factory;
	}
	public void setFactory(String factory) {
		this.factory = factory;
	}
	public String getWarehouse() {
		return warehouse;
	}
	public void setWarehouse(String warehouse) {
		this.warehouse = warehouse;
	}
	public Date getStatDate() {
		return statDate;
	}
	public void setStatDate(Date statDate) {
		this.statDate = statDate;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	
}
