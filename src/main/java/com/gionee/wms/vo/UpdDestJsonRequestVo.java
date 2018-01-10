package com.gionee.wms.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * 发送到第三方系统 http://www.datatransetl.com:8085/dc/app/updDestJson.json
 * 请求参数值对象
 * Created by lmh on 2018/1/9.
 */
public class UpdDestJsonRequestVo {

    private List<IMEIAndAddress> arr = new ArrayList<IMEIAndAddress>();

    public List<IMEIAndAddress> getArr() {
        return arr;
    }

    public void setArr(List<IMEIAndAddress> arr) {
        this.arr = arr;
    }

    public void put(String imei, String destName){
        IMEIAndAddress imeiAndAddress = new IMEIAndAddress(destName, imei);
        arr.add(imeiAndAddress);
    }
}

/**
 * 出库的设备IMEI和地址
 */
class IMEIAndAddress {
    private String imei;
    private String destName;
    public IMEIAndAddress() {super();}

    public IMEIAndAddress(String imei, String destName) {
        this.imei = imei;
        this.destName = destName;
    }

    public String getImei() {
        return imei;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getDestName() {
        return destName;
    }

    public void setDestName(String destName) {
        this.destName = destName;
    }
}


