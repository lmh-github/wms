package com.gionee.wms.vo;

import com.gionee.wms.common.WmsConstants;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 发货明细
 */
public class DeliveryDetails {
    /** 订单来源 */
    private String orderSource;
    /** 发货日期 */
    private Date deliveryDate;
    /** 订单号 */
    private String orderCode;
    /** 付款方式 */
    private String payment;
    /** 交易号 */
    private String payNo;
    /** SKU名称 */
    private String skuName;
    /** 数量 */
    private Integer quantity;
    /** 单价 */
    private BigDecimal price;
    /** 发票金额 */
    private BigDecimal invoiceAmount;
    /** 收货人 */
    private String consignee;
    /** 快递类型 */
    private String expressType;
    /** 快递单号 */
    private String expressNo;
    /** SAP物料号 */
    private String materialCode;
    /** 商品类型 */
    private String goodsType;
    /** 发出地 */
    private String departure;
    /** 订单状态 */
    private Integer orderStatus;

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public Date getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getExpressType() {
        return expressType;
    }

    public void setExpressType(String expressType) {
        this.expressType = expressType;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }
}
