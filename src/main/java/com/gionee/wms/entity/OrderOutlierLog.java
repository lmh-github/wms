package com.gionee.wms.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Date: 2017/10/31
 * Time: 15:17
 *
 * @author huyunfan
 */
public class OrderOutlierLog {

    private String id;
    private String orderCode;
    private String skuCode;
    private Date handledTime;
    private String handledBy;
    /**
     * 预警单价
     */
    private BigDecimal minUnitPrice;
    private BigDecimal unitPrice;
    private String outlierId;
    private String orderSource;

    private OrderOutlier orderOutlier;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }

    public Date getHandledTime() {
        return handledTime;
    }

    public void setHandledTime(Date handledTime) {
        this.handledTime = handledTime;
    }

    public String getHandledBy() {
        return handledBy;
    }

    public void setHandledBy(String handledBy) {
        this.handledBy = handledBy;
    }

    public BigDecimal getMinUnitPrice() {
        return minUnitPrice;
    }

    public void setMinUnitPrice(BigDecimal minUnitPrice) {
        this.minUnitPrice = minUnitPrice;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getOutlierId() {
        return outlierId;
    }

    public void setOutlierId(String outlierId) {
        this.outlierId = outlierId;
    }


    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public OrderOutlier getOrderOutlier() {
        return orderOutlier;
    }

    public void setOrderOutlier(OrderOutlier orderOutlier) {
        this.orderOutlier = orderOutlier;
    }
}
