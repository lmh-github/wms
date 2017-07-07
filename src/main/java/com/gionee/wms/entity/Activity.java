package com.gionee.wms.entity;

import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

/**
 * Created by gionee on 2017/7/5.
 */
public class Activity {
    private Long id;
    private String sponsor;
    private String platform;
    private Date startTime;
    private Date endTime;
    private String area;
    private String status;
    private String giving;
    private String gift;
    private String prize;
    private byte[] accessory;
    private String fileName;
    private String regulation;
    private String remarks;

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getGiving() {
        return giving;
    }

    public void setGiving(String giving) {
        this.giving = giving;
    }

    public String getGift() {
        return gift;
    }

    public void setGift(String gift) {
        this.gift = gift;
    }

    public String getPrize() {
        return prize;
    }

    public void setPrize(String prize) {
        this.prize = prize;
    }

    public byte[] getAccessory() {
        return accessory;
    }

    public void setAccessory(byte[] accessory) {
        this.accessory = accessory;
    }

    public String getRegulation() {
        return regulation;
    }

    public void setRegulation(String regulation) {
        this.regulation = regulation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
