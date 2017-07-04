/**
 * Project Name:wms
 * File Name:WmsSailOrderPushInfoHeader.java
 * Package Name:com.sf.integration.warehouse.request
 * Date:2014年8月28日上午11:06:00
 * Copyright (c) 2014 深圳市金立通信设备有限公司 版权所有 .
 */
package com.sf.integration.warehouse.request;

import com.sf.integration.warehouse.response.JaxbDateAdapter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Date;

/**
 * @author PengBin 00001550<br>
 * @date 2014年8月28日 上午11:06:00
 * @see com.sf.integration.warehouse.request.WmsSailOrderPushInfo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "header")
public class WmsSailOrderPushInfoHeader {
    @XmlElement
    private String company;
    @XmlElement
    private String warehouse;
    @XmlElement
    private String erp_order;
    @XmlElement
    private String shipment_id;
    @XmlElement
    private String waybill_no;
    @XmlElement
    private Date actual_ship_date_time;
    @XmlElement
    private String carrier;
    @XmlElement
    private String carrier_service;
    @XmlElement
    private String user_def1;
    @XmlElement
    private String user_def2;
    @XmlElement
    private String user_def3;
    @XmlElement
    private String user_def4;
    @XmlElement
    private String user_def5;
    @XmlElement
    private String user_def6;
    @XmlElement
    private String user_def7;
    @XmlElement
    private String user_def8;
    @XmlElement
    private String user_def9;
    @XmlElement
    private String user_def10;
    @XmlElement
    private String user_def11;
    @XmlElement
    private String user_def12;
    @XmlElement
    private String user_def13;
    @XmlElement
    private String user_def14;
    @XmlElement
    private String user_def15;
    @XmlElement
    private String user_def16;
    @XmlElement
    private String user_def17;
    @XmlElement
    private String user_def18;
    @XmlElement
    private String user_def19;
    @XmlElement
    private String user_def20;
    @XmlElement
    private String user_stamp;
    @XmlElement
    @XmlJavaTypeAdapter(JaxbDateAdapter.class)
    private Date status_time;
    @XmlElement
    private String status_code;
    @XmlElement
    private String status_remark;

    /**
     * @return the company
     */
    public String getCompany() {
        return company;
    }

    /**
     * @param company the company
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * @return the warehouse
     */
    public String getWarehouse() {
        return warehouse;
    }

    /**
     * @param warehouse the warehouse
     */
    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    /**
     * @return the erp_order
     */
    public String getErp_order() {
        return erp_order;
    }

    /**
     * @param erp_order the erp_order
     */
    public void setErp_order(String erp_order) {
        this.erp_order = erp_order;
    }

    /**
     * @return the shipment_id
     */
    public String getShipment_id() {
        return shipment_id;
    }

    /**
     * @param shipment_id the shipment_id
     */
    public void setShipment_id(String shipment_id) {
        this.shipment_id = shipment_id;
    }

    /**
     * @return the waybill_no
     */
    public String getWaybill_no() {
        return waybill_no;
    }

    /**
     * @param waybill_no the waybill_no
     */
    public void setWaybill_no(String waybill_no) {
        this.waybill_no = waybill_no;
    }

    /**
     * @return the actual_ship_date_time
     */
    public Date getActual_ship_date_time() {
        return actual_ship_date_time;
    }

    /**
     * @param actual_ship_date_time the actual_ship_date_time
     */
    public void setActual_ship_date_time(Date actual_ship_date_time) {
        this.actual_ship_date_time = actual_ship_date_time;
    }

    /**
     * @return the carrier
     */
    public String getCarrier() {
        return carrier;
    }

    /**
     * @param carrier the carrier
     */
    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    /**
     * @return the carrier_service
     */
    public String getCarrier_service() {
        return carrier_service;
    }

    /**
     * @param carrier_service the carrier_service
     */
    public void setCarrier_service(String carrier_service) {
        this.carrier_service = carrier_service;
    }

    /**
     * @return the user_def1
     */
    public String getUser_def1() {
        return user_def1;
    }

    /**
     * @param user_def1 the user_def1
     */
    public void setUser_def1(String user_def1) {
        this.user_def1 = user_def1;
    }

    /**
     * @return the user_def2
     */
    public String getUser_def2() {
        return user_def2;
    }

    /**
     * @param user_def2 the user_def2
     */
    public void setUser_def2(String user_def2) {
        this.user_def2 = user_def2;
    }

    /**
     * @return the user_def3
     */
    public String getUser_def3() {
        return user_def3;
    }

    /**
     * @param user_def3 the user_def3
     */
    public void setUser_def3(String user_def3) {
        this.user_def3 = user_def3;
    }

    /**
     * @return the user_def4
     */
    public String getUser_def4() {
        return user_def4;
    }

    /**
     * @param user_def4 the user_def4
     */
    public void setUser_def4(String user_def4) {
        this.user_def4 = user_def4;
    }

    /**
     * @return the user_def5
     */
    public String getUser_def5() {
        return user_def5;
    }

    /**
     * @param user_def5 the user_def5
     */
    public void setUser_def5(String user_def5) {
        this.user_def5 = user_def5;
    }

    /**
     * @return the user_def6
     */
    public String getUser_def6() {
        return user_def6;
    }

    /**
     * @param user_def6 the user_def6
     */
    public void setUser_def6(String user_def6) {
        this.user_def6 = user_def6;
    }

    /**
     * @return the user_def7
     */
    public String getUser_def7() {
        return user_def7;
    }

    /**
     * @param user_def7 the user_def7
     */
    public void setUser_def7(String user_def7) {
        this.user_def7 = user_def7;
    }

    /**
     * @return the user_def8
     */
    public String getUser_def8() {
        return user_def8;
    }

    /**
     * @param user_def8 the user_def8
     */
    public void setUser_def8(String user_def8) {
        this.user_def8 = user_def8;
    }

    /**
     * @return the user_def9
     */
    public String getUser_def9() {
        return user_def9;
    }

    /**
     * @param user_def9 the user_def9
     */
    public void setUser_def9(String user_def9) {
        this.user_def9 = user_def9;
    }

    /**
     * @return the user_def10
     */
    public String getUser_def10() {
        return user_def10;
    }

    /**
     * @param user_def10 the user_def10
     */
    public void setUser_def10(String user_def10) {
        this.user_def10 = user_def10;
    }

    /**
     * @return the user_def11
     */
    public String getUser_def11() {
        return user_def11;
    }

    /**
     * @param user_def11 the user_def11
     */
    public void setUser_def11(String user_def11) {
        this.user_def11 = user_def11;
    }

    /**
     * @return the user_def12
     */
    public String getUser_def12() {
        return user_def12;
    }

    /**
     * @param user_def12 the user_def12
     */
    public void setUser_def12(String user_def12) {
        this.user_def12 = user_def12;
    }

    /**
     * @return the user_def13
     */
    public String getUser_def13() {
        return user_def13;
    }

    /**
     * @param user_def13 the user_def13
     */
    public void setUser_def13(String user_def13) {
        this.user_def13 = user_def13;
    }

    /**
     * @return the user_def14
     */
    public String getUser_def14() {
        return user_def14;
    }

    /**
     * @param user_def14 the user_def14
     */
    public void setUser_def14(String user_def14) {
        this.user_def14 = user_def14;
    }

    /**
     * @return the user_def15
     */
    public String getUser_def15() {
        return user_def15;
    }

    /**
     * @param user_def15 the user_def15
     */
    public void setUser_def15(String user_def15) {
        this.user_def15 = user_def15;
    }

    /**
     * @return the user_def16
     */
    public String getUser_def16() {
        return user_def16;
    }

    /**
     * @param user_def16 the user_def16
     */
    public void setUser_def16(String user_def16) {
        this.user_def16 = user_def16;
    }

    /**
     * @return the user_def17
     */
    public String getUser_def17() {
        return user_def17;
    }

    /**
     * @param user_def17 the user_def17
     */
    public void setUser_def17(String user_def17) {
        this.user_def17 = user_def17;
    }

    /**
     * @return the user_def18
     */
    public String getUser_def18() {
        return user_def18;
    }

    /**
     * @param user_def18 the user_def18
     */
    public void setUser_def18(String user_def18) {
        this.user_def18 = user_def18;
    }

    /**
     * @return the user_def19
     */
    public String getUser_def19() {
        return user_def19;
    }

    /**
     * @param user_def19 the user_def19
     */
    public void setUser_def19(String user_def19) {
        this.user_def19 = user_def19;
    }

    /**
     * @return the user_def20
     */
    public String getUser_def20() {
        return user_def20;
    }

    /**
     * @param user_def20 the user_def20
     */
    public void setUser_def20(String user_def20) {
        this.user_def20 = user_def20;
    }

    /**
     * @return the user_stamp
     */
    public String getUser_stamp() {
        return user_stamp;
    }

    /**
     * @param user_stamp the user_stamp
     */
    public void setUser_stamp(String user_stamp) {
        this.user_stamp = user_stamp;
    }

    /**
     * @return the status_time
     */
    public Date getStatus_time() {
        return status_time;
    }

    /**
     * @param status_time the status_time
     */
    public void setStatus_time(Date status_time) {
        this.status_time = status_time;
    }

    /**
     * @return the status_code
     */
    public String getStatus_code() {
        return status_code;
    }

    /**
     * @param status_code the status_code
     */
    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    /**
     * @return the status_remark
     */
    public String getStatus_remark() {
        return status_remark;
    }

    /**
     * @param status_remark the status_remark
     */
    public void setStatus_remark(String status_remark) {
        this.status_remark = status_remark;
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return "WmsSailOrderPushInfoHeader [company=" + company + ", warehouse=" + warehouse + ", erp_order=" + erp_order + ", shipment_id=" + shipment_id + ", waybill_no=" + waybill_no + ", actual_ship_date_time=" + actual_ship_date_time + ", carrier=" + carrier + ", carrier_service=" + carrier_service + ", user_stamp=" + user_stamp + ", status_time=" + status_time + ", status_code=" + status_code + ", status_remark=" + status_remark + "]";
    }

}
