package com.gionee.wms.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Transfer {
	private Long transferId;
	private Long warehouseId;
	private String transferTo;
	private String remark;
	private Integer status;
	private Date createTime;
	private String logisticNo;
	private String warehouseName;
	private String handledBy;
	private String consignee;
	private String contact;
	private String po;
	private String logisticName;
	private Long transferSale;//售达方
	private Long transferSend;//送达方
	private Long transferInvoice;//开票方
	private BigDecimal orderAmount;// 订单总金额
	private Date shippingTime;//发货时间
	private List<TransferGoods> goodsList;
	private String tSale;
	private String tSend;
	private String tInvoice;
	private Integer transType;
	private String orderPushStatus;
	private String orderConfirmStatus;
	// 导出添加属性
	private Integer quantity;
	public Long getTransferId() {
		return transferId;
	}
	public void setTransferId(Long transferId) {
		this.transferId = transferId;
	}
	public Long getWarehouseId() {
		return warehouseId;
	}
	public void setWarehouseId(Long warehouseId) {
		this.warehouseId = warehouseId;
	}
	public String getTransferTo() {
		return transferTo;
	}
	public void setTransferTo(String transferTo) {
		this.transferTo = transferTo;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getLogisticNo() {
		return logisticNo;
	}
	public void setLogisticNo(String logisticNo) {
		this.logisticNo = logisticNo;
	}
	public String getWarehouseName() {
		return warehouseName;
	}
	public void setWarehouseName(String warehouseName) {
		this.warehouseName = warehouseName;
	}
	public String getHandledBy() {
		return handledBy;
	}
	public void setHandledBy(String handledBy) {
		this.handledBy = handledBy;
	}
	public String getConsignee() {
		return consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getPo() {
		return po;
	}
	public void setPo(String po) {
		this.po = po;
	}
	public String getLogisticName() {
		return logisticName;
	}
	public void setLogisticName(String logisticName) {
		this.logisticName = logisticName;
	}
	public Long getTransferSale() {
		return transferSale;
	}
	public void setTransferSale(Long transferSale) {
		this.transferSale = transferSale;
	}
	public Long getTransferSend() {
		return transferSend;
	}
	public void setTransferSend(Long transferSend) {
		this.transferSend = transferSend;
	}
	public Long getTransferInvoice() {
		return transferInvoice;
	}
	public void setTransferInvoice(Long transferInvoice) {
		this.transferInvoice = transferInvoice;
	}
	public BigDecimal getOrderAmount() {
		return orderAmount;
	}
	public void setOrderAmount(BigDecimal orderAmount) {
		this.orderAmount = orderAmount;
	}
	public Date getShippingTime() {
		return shippingTime;
	}
	public void setShippingTime(Date shippingTime) {
		this.shippingTime = shippingTime;
	}
	public List<TransferGoods> getGoodsList() {
		return goodsList;
	}
	public void setGoodsList(List<TransferGoods> goodsList) {
		this.goodsList = goodsList;
	}
	public String gettSale() {
		return tSale;
	}
	public void settSale(String tSale) {
		this.tSale = tSale;
	}
	public String gettSend() {
		return tSend;
	}
	public void settSend(String tSend) {
		this.tSend = tSend;
	}
	public String gettInvoice() {
		return tInvoice;
	}
	public void settInvoice(String tInvoice) {
		this.tInvoice = tInvoice;
	}
	public Integer getTransType() {
		return transType;
	}
	public void setTransType(Integer transType) {
		this.transType = transType;
	}
	public String getOrderPushStatus() {
		return orderPushStatus;
	}
	public void setOrderPushStatus(String orderPushStatus) {
		this.orderPushStatus = orderPushStatus;
	}
	public String getOrderConfirmStatus() {
		return orderConfirmStatus;
	}
	public void setOrderConfirmStatus(String orderConfirmStatus) {
		this.orderConfirmStatus = orderConfirmStatus;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	
}
