package com.gionee.wms.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * Created by Pengbin on 2017/5/24.
 */
public class SalesOrderNodeInfo {

    /** */
    private Long id;
    /** 订单号 */
    private String orderCode;
    /** 目标发货仓库 */
    private String targetWarehouse;
    /** 实际发货仓库 */
    private String realWarehouse;
    /** 订单创建时间 */
    private Date orderCreateTime;
    /** 筛单时间 */
    private Date orderFilterTime;
    /** 推送时间 */
    private Date orderPushTime;
    /** 打单时间 */
    private Date orderPrintTime;
    /** 出库时间 */
    private Date orderSendTime;
    /** 签收时间 */
    private Date orderFinishTime;
    /** 订单主商品信息 */
    private String orderSkuInfo;
    /** 订单收货信息 */
    private String orderReceiveInfo;
    /** 物流信息 */
    private String express;
    /** 物流号 */
    private String expressNo;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getTargetWarehouse() {
        return targetWarehouse;
    }

    public void setTargetWarehouse(String targetWarehouse) {
        this.targetWarehouse = targetWarehouse;
    }

    public String getRealWarehouse() {
        return realWarehouse;
    }

    public void setRealWarehouse(String realWarehouse) {
        this.realWarehouse = realWarehouse;
    }

    public Date getOrderCreateTime() {
        return orderCreateTime;
    }

    public void setOrderCreateTime(Date orderCreateTime) {
        this.orderCreateTime = orderCreateTime;
    }

    public Date getOrderFilterTime() {
        return orderFilterTime;
    }

    public void setOrderFilterTime(Date orderFilterTime) {
        this.orderFilterTime = orderFilterTime;
    }

    public Date getOrderPushTime() {
        return orderPushTime;
    }

    public void setOrderPushTime(Date orderPushTime) {
        this.orderPushTime = orderPushTime;
    }

    public Date getOrderPrintTime() {
        return orderPrintTime;
    }

    public void setOrderPrintTime(Date orderPrintTime) {
        this.orderPrintTime = orderPrintTime;
    }

    public Date getOrderSendTime() {
        return orderSendTime;
    }

    public void setOrderSendTime(Date orderSendTime) {
        this.orderSendTime = orderSendTime;
    }

    public Date getOrderFinishTime() {
        return orderFinishTime;
    }

    public void setOrderFinishTime(Date orderFinishTime) {
        this.orderFinishTime = orderFinishTime;
    }

    public String getOrderSkuInfo() {
        return orderSkuInfo;
    }

    public void setOrderSkuInfo(String orderSkuInfo) {
        this.orderSkuInfo = orderSkuInfo;
    }

    public String getOrderReceiveInfo() {
        return orderReceiveInfo;
    }

    public void setOrderReceiveInfo(String orderReceiveInfo) {
        this.orderReceiveInfo = orderReceiveInfo;
    }

    public String getExpress() {
        return express;
    }

    public void setExpress(String express) {
        this.express = express;
    }

    public String getExpressNo() {
        return expressNo;
    }

    public void setExpressNo(String expressNo) {
        this.expressNo = expressNo;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("orderCode", orderCode).append("targetWarehouse", targetWarehouse).append("realWarehouse", realWarehouse).append("orderCreateTime", orderCreateTime).append("orderFilterTime", orderFilterTime).append("orderPushTime", orderPushTime).append("orderPrintTime", orderPrintTime).append("orderSendTime", orderSendTime).append("orderFinishTime", orderFinishTime).append("orderSkuInfo", orderSkuInfo).append("orderReceiveInfo", orderReceiveInfo).append("express", express).append("expressNo", expressNo).toString();
    }
}
