package com.gionee.wms.vo;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.math.BigDecimal;

public class SapPostData {
    private Integer orderStatus;
    private String orderSource;
    private String payMethod;
    private String materialCode;
    private Integer quantity;
    private BigDecimal price;

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getPayMethod() {
        return payMethod;
    }

    public void setPayMethod(String payMethod) {
        this.payMethod = payMethod;
    }

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
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

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("orderStatus", orderStatus).append("orderSource", orderSource).append("payMethod", payMethod).append("materialCode", materialCode).append("quantity", quantity).append("price", price).toString();
    }
}
