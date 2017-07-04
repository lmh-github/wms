package com.gionee.wms.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;
import java.util.List;

/**
 * 工单
 * Created by Pengbin on 2017/6/6.
 */
public class WorkOrder {

    private Long   id;
    private String workCode;
    private String orderCode;
    private String sponsor;
    private String worker;
    private String lv;
    private String platform;
    private String description;
    private String status;
    private String resultMsg;
    private String uper;
    private Date   upTime;
    private String suggest;
    private Date   createTime;
    private Date   acceptTime;
    private Date   lastTime;
    private List<Attachment> attachmentList;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWorkCode() {
        return workCode;
    }

    public void setWorkCode(String workCode) {
        this.workCode = workCode;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getWorker() {
        return worker;
    }

    public void setWorker(String worker) {
        this.worker = worker;
    }

    public String getLv() {
        return lv;
    }

    public void setLv(String lv) {
        this.lv = lv;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getUper() {
        return uper;
    }

    public void setUper(String uper) {
        this.uper = uper;
    }

    public Date getUpTime() {
        return upTime;
    }

    public void setUpTime(Date upTime) {
        this.upTime = upTime;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getAcceptTime() {
        return acceptTime;
    }

    public void setAcceptTime(Date acceptTime) {
        this.acceptTime = acceptTime;
    }

    public Date getLastTime() {
        return lastTime;
    }

    public void setLastTime(Date lastTime) {
        this.lastTime = lastTime;
    }

    public List<Attachment> getAttachmentList() {
        return attachmentList;
    }

    public void setAttachmentList(List<Attachment> attachmentList) {
        this.attachmentList = attachmentList;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("workCode", workCode).append("orderCode", orderCode).append("sponsor", sponsor).append("worker", worker).append("lv", lv).append("platform", platform).append("description", description).append("status", status).append("resultMsg", resultMsg).append("uper", uper).append("upTime", upTime).append("suggest", suggest).append("createTime", createTime).append("acceptTime", acceptTime).append("lastTime", lastTime).append("attachmentList", attachmentList).toString();
    }
}
