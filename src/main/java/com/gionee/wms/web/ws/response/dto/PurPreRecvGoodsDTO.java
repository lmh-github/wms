package com.gionee.wms.web.ws.response.dto;

import com.gionee.wms.common.WmsConstants;
import org.apache.commons.lang3.builder.ToStringBuilder;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

/**
 * WS传输采购预收商品信息的DTO.
 * @author kevin
 */
@XmlRootElement
@XmlType(name = "PurPreRecvGoods", namespace = WmsConstants.TARGET_NS)
public class PurPreRecvGoodsDTO {
    /**
     * 物料编号
     */
    private String materialCode;
    /**
     * 物料名称
     */
    private String materialName;
    /**
     * 数量
     */
    private Integer quantity;
    /**
     * 生产批次号
     */
    private String productBatchNo;
    /**
     * 个体编码(IMEI码)
     */
    private List<String> indivCodeList;
    /** 箱号 */
    private String caseCode;

    public String getMaterialCode() {
        return materialCode;
    }

    public void setMaterialCode(String materialCode) {
        this.materialCode = materialCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getProductBatchNo() {
        return productBatchNo;
    }

    public void setProductBatchNo(String productBatchNo) {
        this.productBatchNo = productBatchNo;
    }

    public List<String> getIndivCodeList() {
        return indivCodeList;
    }

    public void setIndivCodeList(List<String> indivCodeList) {
        this.indivCodeList = indivCodeList;
    }

    public String getCaseCode() {
        return caseCode;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    /**
     * 直接打印DTO信息.
     */
    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
