package com.gionee.wms.facade.dto;

import org.apache.commons.lang.builder.ToStringBuilder;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author Kevin
 * @ClassName: OrderInfoDTO
 * @Description: 订单信息传输对象
 * @date 2013-7-4 下午06:21:12
 */
public class OrderInfoDTO implements Serializable {
    private static final long serialVersionUID = -1495297191712858555L;
    /**
     * 订单编号
     */
    private String orderCode;

    /**
     * 下单用户
     */
    private String orderUser;

    /**
     * 下单时间
     */
    private Date orderTime;

    /**
     * 支付方式类型
     */
    private Integer paymentType;

    /**
     * 支付方式编号
     */
    private String paymentCode;

    /**
     * 支付方式名称
     */
    private String paymentName;

    /**
     * 支付流水号
     */
    private String payNo;

    /**
     * 已支付金额
     */
    private BigDecimal paidAmount;

    /**
     * 收货人的姓名
     */
    private String consignee;

    /**
     * 收货人的省份
     */
    private String province;

    /**
     * 收货人的城市
     */
    private String city;

    /**
     * 收货人的地区
     */
    private String district;

    /**
     * 收货人的详细地址
     */
    private String address;

    /**
     * 邮编
     */
    private String zipcode;

    /**
     * 收货电话
     */
    private String tel;

    /**
     * 收货手机
     */
    private String mobile;

    /**
     * 最佳收货时间
     */
    private String bestTime;

    /**
     * 配送方式编号
     */
    private String shippingCode;

    /**
     * 是否需要发票
     */
    private Integer invoiceEnabled;

    /**
     * 发票类型
     */
    private Integer invoiceType;

    /**
     * 发票抬头
     */
    private String invoiceTitle;

    /**
     * 发票金额
     */
    private BigDecimal invoiceAmount;

    /**
     * 发票内容
     */
    private String invoiceContent;

    /**
     * 订单用户附言
     */
    private String postscript;

    /**
     * 商品总金额
     */
    private BigDecimal goodsAmount;

    /**
     * 订单总金额
     */
    private BigDecimal orderAmount;

    /**
     * 应付金额
     */
    private BigDecimal payableAmount;

    /**
     * 商品清单
     */
    private List<OrderGoodsDTO> goodsList;

    /**
     * 订单来源
     */
    private String orderSource;

    /**
     * 支付时间
     */
    private Date paymentTime;

    /**
     * 发票接收手机号
     */
    private String invoiceMobile;
    /**
     * 发票接收邮箱
     */
    private String invoiceEmail;
    /**
     * 纳税人识别号
     */
    private String buyerTaxNo;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getOrderUser() {
        return orderUser;
    }

    public void setOrderUser(String orderUser) {
        this.orderUser = orderUser;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Integer getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(Integer paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentName(String paymentName) {
        this.paymentName = paymentName;
    }

    public String getPayNo() {
        return payNo;
    }

    public void setPayNo(String payNo) {
        this.payNo = payNo;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public String getConsignee() {
        return consignee;
    }

    public void setConsignee(String consignee) {
        this.consignee = consignee;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getBestTime() {
        return bestTime;
    }

    public void setBestTime(String bestTime) {
        this.bestTime = bestTime;
    }

    public String getShippingCode() {
        return shippingCode;
    }

    public void setShippingCode(String shippingCode) {
        this.shippingCode = shippingCode;
    }

    public Integer getInvoiceEnabled() {
        return invoiceEnabled;
    }

    public void setInvoiceEnabled(Integer invoiceEnabled) {
        this.invoiceEnabled = invoiceEnabled;
    }

    public Integer getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(Integer invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getInvoiceTitle() {
        return invoiceTitle;
    }

    public void setInvoiceTitle(String invoiceTitle) {
        this.invoiceTitle = invoiceTitle;
    }

    public BigDecimal getInvoiceAmount() {
        return invoiceAmount;
    }

    public void setInvoiceAmount(BigDecimal invoiceAmount) {
        this.invoiceAmount = invoiceAmount;
    }

    public String getInvoiceContent() {
        return invoiceContent;
    }

    public void setInvoiceContent(String invoiceContent) {
        this.invoiceContent = invoiceContent;
    }

    public String getPostscript() {
        return postscript;
    }

    public void setPostscript(String postscript) {
        this.postscript = postscript;
    }

    public BigDecimal getGoodsAmount() {
        return goodsAmount;
    }

    public void setGoodsAmount(BigDecimal goodsAmount) {
        this.goodsAmount = goodsAmount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getPayableAmount() {
        return payableAmount;
    }

    public void setPayableAmount(BigDecimal payableAmount) {
        this.payableAmount = payableAmount;
    }

    public List<OrderGoodsDTO> getGoodsList() {
        return goodsList;
    }

    public void setGoodsList(List<OrderGoodsDTO> goodsList) {
        this.goodsList = goodsList;
    }

    public String getOrderSource() {
        return orderSource;
    }

    public void setOrderSource(String orderSource) {
        this.orderSource = orderSource;
    }

    public Date getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(Date paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getInvoiceMobile() {
        return invoiceMobile;
    }

    public void setInvoiceMobile(String invoiceMobile) {
        this.invoiceMobile = invoiceMobile;
    }

    public String getInvoiceEmail() {
        return invoiceEmail;
    }

    public void setInvoiceEmail(String invoiceEmail) {
        this.invoiceEmail = invoiceEmail;
    }

    public String getBuyerTaxNo() {
        return buyerTaxNo;
    }

    public void setBuyerTaxNo(String buyerTaxNo) {
        this.buyerTaxNo = buyerTaxNo;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
