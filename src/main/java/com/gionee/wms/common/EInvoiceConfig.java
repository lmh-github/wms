package com.gionee.wms.common;

import static java.lang.System.getProperty;

/**
 * 电子发票配置
 * Created by Pengbin on 2017/4/5.
 */
public final class EInvoiceConfig {

    public static final String E_APP_ID = getProperty("einvoice.e.appid", "ZZS_PT_DZFP");
    public static final String E_USER_NAME = getProperty("einvoice.e.username", "17109066");
    public static final String E_PASSWORD = getProperty("einvoice.e.password", "5600163257ixlV0FoSGORAjfsesyE+oQ==");
    public static final String E_TAX_PAYER_ID = getProperty("einvoice.e.tax_payer_id", "110101TRDX8RQU1");
    public static final String E_AUTHORIZATION_CODE = getProperty("einvoice.e.authorization_code", "442637415X");
    public static final String E_REQUEST_CODE = getProperty("einvoice.e.request.code", "P0000001");
    public static final String E_RESPONSE_CODE = getProperty("einvoice.e.response.code", "121");

    public static final String E_INVOICE_API_URL = System.getProperty("einvoice.e.api.url", "http://ei.szhtxx.com:9001/front/request/4458");

    /////

    public static final String E_DSPTBM = getProperty("einvoice.e.dsptbm", "14410101");
    public static final String E_NSRSBH = getProperty("einvoice.e.nsrsbh", "440300349724458");
    public static final String E_NSRMC = getProperty("einvoice.e.nsrmc", "深圳市金立通信设备有限公司");
    public static final String E_XHF_NSRSBH = getProperty("einvoice.e.xhf_nsrsbh", "440300349724458");
    public static final String E_XHFMC = getProperty("einvoice.e.xhfmc", "深圳市金立通信设备有限公司");
    public static final String E_XHF_DZ = getProperty("einvoice.e.xhf_dz", "深圳市福田区深南大道7028号时代科技大厦21楼");
    public static final String E_XHF_DH = getProperty("einvoice.e.xhf_dh", "0755-83581704");
    public static final String E_KPY = getProperty("einvoice.e.kpy", "冯盼盼");
    public static final String E_SKY = getProperty("einvoice.e.sky", "李慧");
    public static final String E_FHR = getProperty("einvoice.e.fhr", "冯盼盼");


    /////

    public static final String W_APP_ID = getProperty("einvoice.w.appid", "00");
    public static final String W_VERSION= getProperty("einvoice.w.version", "1.0");
    public static final String W_USER_NAME = getProperty("einvoice.w.username", "p0000006");
    public static final String W_PASSWORD = getProperty("einvoice.w.password", "10000006");
    public static final String W_TAX_PAYER_ID = getProperty("einvoice.w.tax_payer_id", "GZ0000000000001");
    public static final String W_ENTERPRISE_CODE = getProperty("einvoice.w.enterprise_code", "gz000001");
    public static final String W_SERIAL_ID = getProperty("einvoice.w.serial_id", "190909166");

    public static final String W_REQUEST_URL = getProperty("einvoice.w.request.url","http://apitest.5ifapiao.com/api3.0/outerdispatcher/ewmGenerate");


    /////

    public static final String MAIL_SUBJECT = getProperty("einvoice.mail_subject", "金立手机电子发票，请查收，谢谢");


}
