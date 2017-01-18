package com.gionee.wms.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by gaobo-pc on 2016/11/14.
 */
public class TransferRemove {
        private Long id;
        private String backPlatform;//平台
        private int quality; //品质 1良品 0 次品
        private String remark; //备注
        private String createBy;
        private Date createTime;
        private int status; // 状态

         private String transferCode;


    /** 退换货商品 */
    private List<TransferRemoveDetail> backGoods = new ArrayList<TransferRemoveDetail>();


    public List<TransferRemoveDetail> getBackGoods() {
        return backGoods;
    }

    public void setBackGoods(List<TransferRemoveDetail> backGoods) {
        this.backGoods = backGoods;
    }




    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public void setStatus(int status) {
        this.status = status;
    }




    public String getRemark() {
        return remark;
    }

    public String getCreateBy() {
        return createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public int getStatus() {
        return status;
    }

    public String getTransferCode() {
        return transferCode;
    }

    public void setTransferCode(String transferCode) {
        this.transferCode = transferCode;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBackPlatform() {
        return backPlatform;
    }

    public void setBackPlatform(String backPlatform) {
        this.backPlatform = backPlatform;
    }

    public int getQuality() {
        return quality;
    }

    public void setQuality(int quality) {
        this.quality = quality;
    }
}
