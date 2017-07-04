package com.gionee.wms.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * Created by Pengbin on 2017/6/7.
 */
public class UcUser {

    private Long   id;
    private String ucAccount;
    private String userName;
    private String status;
    private Date   createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUcAccount() {
        return ucAccount;
    }

    public void setUcAccount(String ucAccount) {
        this.ucAccount = ucAccount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("ucAccount", ucAccount).append("userName", userName).append("status", status).append("createTime", createTime).toString();
    }
}
