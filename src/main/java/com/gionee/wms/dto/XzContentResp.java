package com.gionee.wms.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by Pengbin on 2017/3/10.
 */
@XStreamAlias("RESPONSE_FPXXXZ_NEW")
public class XzContentResp {

    /** 发票请求唯一流水号 */
    @XStreamAlias("FPQQLSH")
    private String fpqqlsh;
    /** 订单号 */
    @XStreamAlias("DDH")
    private String ddh;
    /** 开票流水号 */
    @XStreamAlias("KPLSH")
    private String kplsh;
    /** 校验码 */
    @XStreamAlias("FWM")
    private String fwm;
    /** 二维码 */
    @XStreamAlias("EWM")
    private String ewm;
    /** 发票种类代码 */
    @XStreamAlias("FPZL_DM")
    private String fpzlDm;
    /** 发票代码 */
    @XStreamAlias("FP_DM")
    private String fpDm;
    /** 发票号码 */
    @XStreamAlias("FP_HM")
    private String fpHm;
    /** 开票日期 */
    @XStreamAlias("KPRQ")
    private String kprq;
    /** 开票类型 */
    @XStreamAlias("KPLX")
    private String kplx;
    /** 不含税金额 */
    @XStreamAlias("HJBHSJE")
    private String hjbhsje;
    /** 税额 */
    @XStreamAlias("KPHJSE")
    private String kphjse;
    /** Base64（pdf文件） */
    @XStreamAlias("PDF_FILE")
    private String pdfFile;
    /**  */
    @XStreamAlias("PDF_URL")
    private String pdfUrl;
    /** 操作代码 */
    @XStreamAlias("CZDM")
    private String czdm;
    /** 结果代码 */
    @XStreamAlias("RETURNCODE")
    private String returnCode;
    /** 结果描述 */
    @XStreamAlias("RETURNMESSAGE")
    private String returnMessage;

    public String getFpqqlsh() {
        return fpqqlsh;
    }

    public void setFpqqlsh(String fpqqlsh) {
        this.fpqqlsh = fpqqlsh;
    }

    public String getDdh() {
        return ddh;
    }

    public void setDdh(String ddh) {
        this.ddh = ddh;
    }

    public String getKplsh() {
        return kplsh;
    }

    public void setKplsh(String kplsh) {
        this.kplsh = kplsh;
    }

    public String getFwm() {
        return fwm;
    }

    public void setFwm(String fwm) {
        this.fwm = fwm;
    }

    public String getEwm() {
        return ewm;
    }

    public void setEwm(String ewm) {
        this.ewm = ewm;
    }

    public String getFpzlDm() {
        return fpzlDm;
    }

    public void setFpzlDm(String fpzlDm) {
        this.fpzlDm = fpzlDm;
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

    public String getKprq() {
        return kprq;
    }

    public void setKprq(String kprq) {
        this.kprq = kprq;
    }

    public String getKplx() {
        return kplx;
    }

    public void setKplx(String kplx) {
        this.kplx = kplx;
    }

    public String getHjbhsje() {
        return hjbhsje;
    }

    public void setHjbhsje(String hjbhsje) {
        this.hjbhsje = hjbhsje;
    }

    public String getKphjse() {
        return kphjse;
    }

    public void setKphjse(String kphjse) {
        this.kphjse = kphjse;
    }

    public String getPdfFile() {
        return pdfFile;
    }

    public void setPdfFile(String pdfFile) {
        this.pdfFile = pdfFile;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public String getCzdm() {
        return czdm;
    }

    public void setCzdm(String czdm) {
        this.czdm = czdm;
    }

    public String getReturnCode() {
        return returnCode;
    }

    public void setReturnCode(String returnCode) {
        this.returnCode = returnCode;
    }

    public String getReturnMessage() {
        return returnMessage;
    }

    public void setReturnMessage(String returnMessage) {
        this.returnMessage = returnMessage;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("XzContentResp{");
        sb.append("fpqqlsh='").append(fpqqlsh).append('\'');
        sb.append(", ddh='").append(ddh).append('\'');
        sb.append(", kplsh='").append(kplsh).append('\'');
        sb.append(", fwm='").append(fwm).append('\'');
        sb.append(", ewm='").append(ewm).append('\'');
        sb.append(", fpzlDm='").append(fpzlDm).append('\'');
        sb.append(", fpDm='").append(fpDm).append('\'');
        sb.append(", fpHm='").append(fpHm).append('\'');
        sb.append(", kprq='").append(kprq).append('\'');
        sb.append(", kplx='").append(kplx).append('\'');
        sb.append(", hjbhsje='").append(hjbhsje).append('\'');
        sb.append(", kphjse='").append(kphjse).append('\'');
        sb.append(", pdfFile='").append(pdfFile).append('\'');
        sb.append(", pdfUrl='").append(pdfUrl).append('\'');
        sb.append(", czdm='").append(czdm).append('\'');
        sb.append(", returnCode='").append(returnCode).append('\'');
        sb.append(", returnMessage='").append(returnMessage).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
