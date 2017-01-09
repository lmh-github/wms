package com.gionee.wms.entity;

import java.util.Date;

/**
 * Created by gaobo-pc on 2016/11/14.
 */
public class TransferRemoveDetail {
    private long id;
    private String transferId;//transfer单号 id
    private String skuCode;
    private  String remark;
    private String SkuName;

    private int totle;

    private int num;

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getTotle() {
        return totle;
    }

    public void setTotle(int totle) {
        this.totle = totle;
    }

    public String getSkuName() {
        return SkuName;
    }

    public void setSkuName(String skuName) {
        SkuName = skuName;
    }

    public void setSkuCode(String skuCode) {
        this.skuCode = skuCode;
    }


    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {

        return id;
    }

    public String getTransferId() {
        return transferId;
    }

    public void setTransferId(String transferId) {
        this.transferId = transferId;
    }

    public String getSkuCode() {
        return skuCode;
    }

    public String getRemark() {
        return remark;
    }
}
