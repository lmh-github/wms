package com.gionee.wms.entity;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Date: 2017/10/30
 * Time: 15:13
 *
 * @author huyunfan
 */
public class OrderOutlier {
    private String id;
    private String name;
    private Date beginTime;
    private Date endTime;
    /**
     * 规则生效对应来源集合 ","隔开
     */
    private String orderSource;
    /**
     * 规则生效对应sku集合 ","隔开
     */
    private String skuIds;
    private String skuNames;
    private BigDecimal unitPrice;
    /**
     * 是否记录日志 0：是 1：否
     */
    private String logSwitch;
    private List<Sku> skuList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public String getSkuIds() {
        return skuIds;
    }

    public void setSkuIds(String skuIds) {
        this.skuIds = skuIds;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }

    public String getLogSwitch() {
        return logSwitch;
    }

    public void setLogSwitch(String logSwitch) {
        this.logSwitch = logSwitch;
    }

    public List<Sku> getSkuList() {
        return skuList;
    }

    public void setSkuList(List<Sku> skuList) {
        this.skuList = skuList;
    }

    public String getSkuNames() {
        return skuNames;
    }

    public void setSkuNames(String skuNames) {
        this.skuNames = skuNames;
    }
}
