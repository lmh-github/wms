package com.gionee.wms.dto;

import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by Pengbin on 2017/3/10.
 */
@XStreamAlias("RESPONSE_FPKJ")
public class KpContentResp {

    @XStreamAlias("RETURNCODE")
    private String returnCode;
    @XStreamAlias("RETURNMESSAGE")
    private String returnMessage;
    @XStreamAlias("HJBHSJE")
    private String hjbhsje;
    @XStreamAlias("HJSE")
    private String hjse;
    @XStreamAlias("KPRQ")
    private String kprq;
    @XStreamAlias("SSYF")
    private String ssyf;
    @XStreamAlias("FP_DM")
    private String fpDm;
    @XStreamAlias("FP_HM")
    private String fpHm;
    @XStreamAlias("XHQDBZ")
    private String xhqdbz;
    @XStreamAlias("RETCODE")
    private String retCode;
    @XStreamAlias("FWMW")
    private String fwmw;
    @XStreamAlias("JYM")
    private String jym;
    @XStreamAlias("SZQM")
    private String szqm;
    @XStreamAlias("EWM")
    private String ewm;
    @XStreamAlias("PDF_URL")
    private String pdfUrl;

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

    public String getHjbhsje() {
        return hjbhsje;
    }

    public void setHjbhsje(String hjbhsje) {
        this.hjbhsje = hjbhsje;
    }

    public String getHjse() {
        return hjse;
    }

    public void setHjse(String hjse) {
        this.hjse = hjse;
    }

    public String getKprq() {
        return kprq;
    }

    public void setKprq(String kprq) {
        this.kprq = kprq;
    }

    public String getSsyf() {
        return ssyf;
    }

    public void setSsyf(String ssyf) {
        this.ssyf = ssyf;
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

    public String getXhqdbz() {
        return xhqdbz;
    }

    public void setXhqdbz(String xhqdbz) {
        this.xhqdbz = xhqdbz;
    }

    public String getRetCode() {
        return retCode;
    }

    public void setRetCode(String retCode) {
        this.retCode = retCode;
    }

    public String getFwmw() {
        return fwmw;
    }

    public void setFwmw(String fwmw) {
        this.fwmw = fwmw;
    }

    public String getJym() {
        return jym;
    }

    public void setJym(String jym) {
        this.jym = jym;
    }

    public String getSzqm() {
        return szqm;
    }

    public void setSzqm(String szqm) {
        this.szqm = szqm;
    }

    public String getEwm() {
        return ewm;
    }

    public void setEwm(String ewm) {
        this.ewm = ewm;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("KpContentResp{");
        sb.append("returnCode='").append(returnCode).append('\'');
        sb.append(", returnMessage='").append(returnMessage).append('\'');
        sb.append(", hjbhsje='").append(hjbhsje).append('\'');
        sb.append(", hjse='").append(hjse).append('\'');
        sb.append(", kprq='").append(kprq).append('\'');
        sb.append(", ssyf='").append(ssyf).append('\'');
        sb.append(", fpDm='").append(fpDm).append('\'');
        sb.append(", fpHm='").append(fpHm).append('\'');
        sb.append(", xhqdbz='").append(xhqdbz).append('\'');
        sb.append(", retCode='").append(retCode).append('\'');
        sb.append(", fwmw='").append(fwmw).append('\'');
        sb.append(", jym='").append(jym).append('\'');
        sb.append(", szqm='").append(szqm).append('\'');
        sb.append(", ewm='").append(ewm).append('\'');
        sb.append(", pdfUrl='").append(pdfUrl).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
