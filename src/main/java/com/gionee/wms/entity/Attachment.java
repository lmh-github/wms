package com.gionee.wms.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * Created by Pengbin on 2017/6/7.
 */
public class Attachment {
    private Long id;
    private String billCode;
    private String billType;
    private String fileName;
    private String abstractPath;
    private String fileType;
    private String createBy;
    private Date createTime;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillCode() {
        return billCode;
    }

    public void setBillCode(String billCode) {
        this.billCode = billCode;
    }

    public String getBillType() {
        return billType;
    }

    public void setBillType(String billType) {
        this.billType = billType;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getAbstractPath() {
        return abstractPath;
    }

    public void setAbstractPath(String abstractPath) {
        this.abstractPath = abstractPath;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("billCode", billCode).append("billType", billType).append("fileName", fileName).append("abstractPath", abstractPath).append("fileType", fileType).append("createBy", createBy).append("createTime", createTime).append("status", status).toString();
    }
}
