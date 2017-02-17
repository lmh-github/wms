package com.gionee.wms.entity;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.Date;

/**
 * 发票
 */
public class InvoiceInfo {

    private String id;
    private String orderCode;
    private String fpDm;
    private String fpHm;
    private Date kprq;
    private String returnCode;
    private Date opDate;
    private String opUser;
    private String status;
    private String jsonData;
    private String invoiceType;
    private String mobile;
    private String email;
    private String previewImgUrl;
    private String pdfUrl;
    private String ewmUrl;
    private String kpLsh;
    private String chLsh;

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

    public String getFpDm() {
        return fpDm;
    }

    public void setFpDm(String fpDm) {
        this.fpDm = fpDm;
    }

    public String getFpHm() {
        return fpHm;
    }

    public void setFpHm(String fpHm) {
        this.fpHm = fpHm;
    }

    public Date getKprq() {
        return kprq;
    }

    public void setKprq(Date kprq) {
        this.kprq = kprq;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public Date getOpDate() {
        return opDate;
    }

    public void setOpDate(Date opDate) {
        this.opDate = opDate;
    }

    public String getOpUser() {
        return opUser;
    }

    public void setOpUser(String opUser) {
        this.opUser = opUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPreviewImgUrl() {
        return previewImgUrl;
    }

    public void setPreviewImgUrl(String previewImgUrl) {
        this.previewImgUrl = previewImgUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getEwmUrl() {
        return ewmUrl;
    }

    public void setEwmUrl(String ewmUrl) {
        this.ewmUrl = ewmUrl;
    }

    public String getKpLsh() {
        return kpLsh;
    }

    public void setKpLsh(String kpLsh) {
        this.kpLsh = kpLsh;
    }

    public String getChLsh() {
        return chLsh;
    }

    public void setChLsh(String chLsh) {
        this.chLsh = chLsh;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("orderCode", orderCode).append("fpDm", fpDm).append("fpHm", fpHm).append("kprq", kprq).append("returnCode", returnCode).append("opDate", opDate).append("opUser", opUser).append("status", status).append("jsonData", jsonData).append("invoiceType", invoiceType).append("mobile", mobile).append("email", email).append("previewImgUrl", previewImgUrl).append("pdfUrl", pdfUrl).append("ewmUrl", ewmUrl).append("kpLsh", kpLsh).append("chLsh", chLsh).toString();
    }
}