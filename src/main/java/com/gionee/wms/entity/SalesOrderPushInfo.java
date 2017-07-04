package com.gionee.wms.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * 顺丰订单回推信息
 * Created by Pengbin on 2017/5/28.
 */
public class SalesOrderPushInfo {
    /** */
    private Long id;
    /** 订单号 */
    private String orderCode;
    /** 回推信息 */
    private String content;
    /** 回推时间 */
    private Date pushDate;

    public SalesOrderPushInfo() {
    }

    public SalesOrderPushInfo(String orderCode, Date pushDate, String content) {
        this.orderCode = orderCode;
        this.pushDate = pushDate;
        this.content = content;
    }

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getPushDate() {
        return pushDate;
    }

    public void setPushDate(Date pushDate) {
        this.pushDate = pushDate;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("orderCode", orderCode).append("content", content).append("pushDate", pushDate).toString();
    }
}
