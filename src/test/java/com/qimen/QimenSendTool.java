package com.qimen;


import com.gionee.wms.common.WmsConstants;
import com.gionee.wms.common.excel.excelexport.util.DateUtil;
import com.qimen.api.DefaultQimenClient;
import com.qimen.api.QimenClient;
import com.qimen.api.request.EntryorderCreateRequest;
import com.qimen.api.request.OrderprocessReportRequest;
import com.qimen.api.request.StockoutCreateRequest;
import com.qimen.api.response.EntryorderCreateResponse;
import com.qimen.api.response.OrderprocessReportResponse;
import com.qimen.api.response.StockoutCreateResponse;
import com.taobao.api.ApiException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QimenSendTool {


    @Test
    public  void qimenToMWS1()  throws ApiException {
        String url = "http://qimenapi.tbsandbox.com/router/qimen/service";
        String appKey =  WmsConstants.QIMEN_APPKEY; // 可替换为您的沙箱环境应用的AppKey
        String appSecret =  WmsConstants.QIMEN_SECRET; // 可替换为您的沙箱环境应用的AppSecret
        QimenClient client = new DefaultQimenClient(url, appKey, appSecret);
        StockoutCreateRequest req = new StockoutCreateRequest();
        req.setCustomerId(WmsConstants.QIMEN_CUSTOMERID);
        req.setVersion("2.0");
        StockoutCreateRequest.DeliveryOrder obj1 = new StockoutCreateRequest.DeliveryOrder();
        obj1.setTotalOrderLines(12L);
        obj1.setDeliveryOrderCode("TB1234");
        obj1.setOrderType("PTCK");
        StockoutCreateRequest.RelatedOrder obj2 = new StockoutCreateRequest.RelatedOrder();
        obj2.setOrderType("CG");
        obj2.setOrderCode("GL1234");
        List< StockoutCreateRequest.RelatedOrder> l2=new ArrayList< StockoutCreateRequest.RelatedOrder>();
        l2.add(obj2);
        obj1.setRelatedOrders(l2);
        obj1.setWarehouseCode("CK1234");
        obj1.setCreateTime("2016-09-09 12:00:00");
        obj1.setScheduleDate("2017-09-09");
        obj1.setLogisticsCode("SF");
        obj1.setLogisticsName("顺丰");
        obj1.setSupplierCode("TB");
        obj1.setSupplierName("淘宝");
        obj1.setTransportMode("自提");
        StockoutCreateRequest.PickerInfo obj3 = new StockoutCreateRequest.PickerInfo();
        obj3.setCompany("天猫");
        obj3.setName("老王");
        obj3.setTel("897765");
        obj3.setMobile("123421234");
        obj3.setId("1232344322");
        obj3.setCarNo("XA1234");
        obj1.setPickerInfo(obj3);
        StockoutCreateRequest.SenderInfo obj4 = new StockoutCreateRequest.SenderInfo();
        obj4.setCompany("淘宝");
        obj4.setName("老王");
        obj4.setZipCode("043300");
        obj4.setTel("81020340");
        obj4.setMobile("13214567869");
        obj4.setEmail("345@gmail.com");
        obj4.setCountryCode("051532");
        obj4.setProvince("浙江省");
        obj4.setCity("杭州");
        obj4.setArea("余杭");
        obj4.setTown("横加桥");
        obj4.setDetailAddress("杭州市余杭区989号");
        obj4.setId("476543213245733");
        obj1.setSenderInfo(obj4);
        StockoutCreateRequest.ReceiverInfo obj5 = new StockoutCreateRequest.ReceiverInfo();
        obj5.setCompany("淘宝");
        obj5.setName("老王");
        obj5.setZipCode("043300");
        obj5.setTel("808786543");
        obj5.setMobile("13423456785");
        obj5.setIdType("1");
        obj5.setIdNumber("1234567");
        obj5.setEmail("878987654@qq.com");
        obj5.setCountryCode("045565");
        obj5.setProvince("浙江省");
        obj5.setCity("杭州");
        obj5.setArea("余杭");
        obj5.setTown("横加桥");
        obj5.setDetailAddress("杭州市余杭区989号");
        obj5.setId("4713242536");
        obj1.setReceiverInfo(obj5);
        obj1.setRemark("备注信息");
        req.setDeliveryOrder(obj1);
        StockoutCreateRequest.OrderLine obj6 = new StockoutCreateRequest.OrderLine();
        obj6.setOutBizCode("OB1234");
        obj6.setOrderLineNo("11");
        obj6.setOwnerCode("H1234");
        obj6.setItemCode("I1234");
        obj6.setItemId("W1234");
        obj6.setInventoryType("ZP");
        obj6.setItemName("淘公仔");
        obj6.setPlanQty(11L);
        obj6.setBatchCode("123");
        obj6.setProductDate("2016-07-06");
        obj6.setExpireDate("2016-07-06");
        obj6.setProduceCode("P11233");
        List<StockoutCreateRequest.OrderLine> l6=new ArrayList<StockoutCreateRequest.OrderLine>();
        l6.add(obj6);
        req.setOrderLines(l6);

        StockoutCreateResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());

    }


    //
    @Test
    public  void qimenToMWS2()  {

        String Url = WmsConstants.QIMEN_BIZURL+"?"  //线上地址     日常地址："http://qimenapi.tbsandbox.com/router/qimen/service?"
                + "app_key=" + WmsConstants.QIMEN_APPKEY
                + "&customerId=" + WmsConstants.QIMEN_CUSTOMERID
                + "&format=xml"
                + "&method=taobao.qimen.entryorder.create"
                + "&sign_method=md5"
                + "&timestamp=2017-01-04+10%3A22%3A11"
                +  "&v=2.0"
                + "&version=1";

       String body2 = "<?xml version=\"1.0\" encoding=\"utf-8\"?><request>" +
                "<entryOrder>" +
                    "<totalOrderLines>1</totalOrderLines>"+
                    "<ownerCode>12345</ownerCode>" +
                    "<entryOrderCode>LBX022157636973431</entryOrderCode>"+
                    "<warehouseCode>8610752</warehouseCode>"+
                        "<receiverInfo>" +
                            "<idType>1</idType>" +
                            "<tel>808786543</tel>" +
                            "<countryCode>045565</countryCode>" +
                            "<detailAddress>杭州市余杭区989号</detailAddress>" +
                            "<city>杭州</city>" +
                            "<area>余杭</area>" +
                            "<email>878987654@qq.com</email>" +
                            "<company>淘宝</company>" +
                            "<name>老王</name>" +
                            "<zipCode>043300</zipCode>" +
                            "<province>浙江省</province>" +
                            "<town>横加桥</town>" +
                            "<mobile>13423456785</mobile>" +
                            "<idNumber>12345</idNumber>" +
                        "</receiverInfo>"+
                        "<senderInfo>" +
                            "<area>余杭</area>" +
                            "<email>345@gmail.com</email>" +
                            "<tel>81020340</tel>" +
                            "<zipCode>043300</zipCode>" +
                            "<name>老王</name>" +
                            "<company>淘宝</company>" +
                            "<province>浙江省</province>" +
                            "<countryCode>051532</countryCode>" +
                            "<town>横加桥</town>" +
                            "<detailAddress>杭州市余杭区989号</detailAddress>" +
                            "<city>杭州</city>" +
                            "<mobile>13214567869</mobile>" +
                        "</senderInfo>"+
                "</entryOrder>" +
                "<orderLines>" +
                    "<orderLine>"+
                    "<itemName>金钢全网通3GB+16GB</itemName>"+
                    "<batchCode>1194</batchCode>" +
                    "<itemId>1194</itemId>" +
                    "<retailPrice>1</retailPrice>" +
                    "<produceCode>P1234</produceCode>" +
                    "<planQty>1</planQty>" +
                    "<skuProperty>属性</skuProperty>" +
                    "<inventoryType>ZP</inventoryType>" +
                    "<expireDate>2017-09-09</expireDate>" +
                    "<orderLineNo>EL123</orderLineNo>" +
                    "<outBizCode>O123</outBizCode>" +
                    "<ownerCode>O123</ownerCode>" +
                    "<productDate>2017-09-09</productDate>" +
                    "<itemCode>I123</itemCode>" +
                    "<purchasePrice>12.0</purchasePrice>" +
                    "</orderLine>"+
                "</orderLines>"+
                "</request>";//请求主题
        String body="<?xml version=\"1.0\" encoding=\"utf-8\"?>" +
                "<request>" +
                "<entryOrder>" +
                "<relatedOrders>" +
                    "<relatedOrder>"+
                        "<orderCode>G1234</orderCode>" +
                        "<orderType>CG</orderType>" +
                    "</relatedOrder>"+
                "</relatedOrders>" +
                "<supplierName>GN1234</supplierName>" +
                "<operateTime>2017-01-04 10:18:00</operateTime>" +
                "<remark>备注信息</remark>" +
                "<operatorCode>ON1234</operatorCode>" +
                "<orderType>SCRK</orderType>" +
                "<receiverInfo>" +
                "<idType>1</idType>" +
                "<tel>808786543</tel>" +
                "<countryCode>045565</countryCode>" +
                "<detailAddress>杭州市余杭区989号</detailAddress>" +
                "<city>杭州</city>" +
                "<area>余杭</area>" +
                "<email>878987654@qq.com</email>" +
                "<company>淘宝</company>" +
                "<name>老王</name>" +
                "<zipCode>043300</zipCode>" +
                "<province>浙江省</province>" +
                "<town>横加桥</town>" +
                "<mobile>13423456785</mobile>" +
                "<idNumber>12345</idNumber>" +
                "</receiverInfo>" +
                "<supplierCode>G1234</supplierCode>" +
                "<expressCode>Y1234</expressCode>" +
                "<purchaseOrderCode>C123455</purchaseOrderCode>" +
                "<warehouseCode>W1234</warehouseCode>" +
                "<orderCreateTime>2016-09-09 12:00:00</orderCreateTime>" +
                "<expectEndTime>2015-09-09 12:00:00</expectEndTime>" +
                "<entryOrderCode>E1234</entryOrderCode>" +
                "<logisticsCode>SF</logisticsCode>" +
                "<senderInfo>" +
                "<area>余杭</area>" +
                "<email>345@gmail.com</email>" +
                "<tel>81020340</tel>" +
                "<zipCode>043300</zipCode>" +
                "<name>老王</name>" +
                "<company>淘宝</company>" +
                "<province>浙江省</province>" +
                "<countryCode>051532</countryCode>" +
                "<town>横加桥</town>" +
                "<detailAddress>杭州市余杭区989号</detailAddress>" +
                "<city>杭州</city>" +
                "<mobile>13214567869</mobile>" +
                "</senderInfo>" +
                "<operatorName>老王</operatorName>" +
                "<totalOrderLines>12</totalOrderLines>" +
                "<logisticsName>顺丰</logisticsName>" +
                "<expectStartTime>2015-09-09 12:00:00</expectStartTime>" +
                "<ownerCode>O1234</ownerCode>" +
                "</entryOrder>" +
                "<orderLines>" +
                    "<orderLine>"+
                        "<itemName>金钢全网通3GB+16GB</itemName>"+
                        "<batchCode>1194</batchCode>" +
                        "<itemId>1194</itemId>" +
                        "<retailPrice>1</retailPrice>" +
                        "<produceCode>P1234</produceCode>" +
                        "<planQty>1</planQty>" +
                        "<skuProperty>属性</skuProperty>" +
                        "<inventoryType>ZP</inventoryType>" +
                        "<expireDate>2017-09-09</expireDate>" +
                        "<orderLineNo>EL123</orderLineNo>" +
                        "<outBizCode>O123</outBizCode>" +
                        "<ownerCode>O123</ownerCode>" +
                        "<productDate>2017-09-09</productDate>" +
                        "<itemCode>I123</itemCode>" +
                        "<purchasePrice>12.0</purchasePrice>" +
                    "</orderLine>"+
                "</orderLines>"+
                "</request>";
        String secretKey = WmsConstants.QIMEN_SECRET;
        try {
            String md5 = QimenSign.sign(Url, body, secretKey);
            System.out.println(md5);
            System.out.println(body);
          //  System.out.println(secretKey);
           String Url1 = Url + "&sign=" + md5;
            System.out.println(Url1);

            String response = QimenHttpclient.send(Url1, body);       //注意：签名的body和http请求的body必须完全一致，包括空格、换行等
            System.out.println("===================================");
            System.out.println(response);//奇门返回结果
            System.out.println("=================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public  void qimenToMWS3()  {
        String Url = "http://qimenapi.tbsandbox.com/router/qimen/service?"  //线上地址     日常地址："http://qimenapi.tbsandbox.com/router/qimen/service?"
                + "method=taobao.qimen.deliveryorder.confirm"
                + "&timestamp=" + DateUtil.formate(new Date())
                + "&format=xml"
                + "&app_key=" + WmsConstants.QIMEN_APPKEY
                + "&v=2.0"
                + "&sign_method=md5"
                + "&customerId=" + WmsConstants.QIMEN_CUSTOMERID;

        String body = "<?xml version=\"1.0\" encoding=\"utf-8\"?><request><storeId>110***28</storeId></request>";//请求主题
        String secretKey = WmsConstants.QIMEN_SECRET;
        try {
            String md5 = QimenSign.sign(Url, body, secretKey);
            System.out.println(md5);

            String Url1 = Url + "&sign=" + md5;
            System.out.println(Url1);

            String response = QimenHttpclient.send(Url1, body);       //注意：签名的body和http请求的body必须完全一致，包括空格、换行等
            System.out.println("===================================");
            System.out.println(response);//奇门返回结果
            System.out.println("=================================");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Test
    public void orderProcess() throws ApiException {
        QimenClient client = new DefaultQimenClient( WmsConstants.QIMEN_BIZURL, WmsConstants.QIMEN_APPKEY, WmsConstants.QIMEN_SECRET);
        EntryorderCreateRequest req = new EntryorderCreateRequest();
        req.setCustomerId(WmsConstants.QIMEN_CUSTOMERID);
        req.setVersion("2.0");
        EntryorderCreateRequest.EntryOrder obj1 = new EntryorderCreateRequest.EntryOrder();
        obj1.setTotalOrderLines(12L);
        obj1.setEntryOrderCode("E1234");
        obj1.setOwnerCode("O1234");
        obj1.setPurchaseOrderCode("C123455");
        obj1.setWarehouseCode("W1234");
        obj1.setOrderCreateTime("2016-09-09 12:00:00");
        obj1.setOrderType("SCRK");
        EntryorderCreateRequest.RelatedOrder obj2 = new EntryorderCreateRequest.RelatedOrder();
        obj2.setOrderType("CG");
        obj2.setOrderCode("G1234");
        List<EntryorderCreateRequest.RelatedOrder> relatedOrders=new ArrayList<EntryorderCreateRequest.RelatedOrder>();
        relatedOrders.add(obj2);
        obj1.setRelatedOrders(relatedOrders);
        obj1.setExpectStartTime("2015-09-09 12:00:00");
        obj1.setExpectEndTime("2015-09-09 12:00:00");
        obj1.setLogisticsCode("SF");
        obj1.setLogisticsName("顺丰");
        obj1.setExpressCode("Y1234");
        obj1.setSupplierCode("G1234");
        obj1.setSupplierName("GN1234");
        obj1.setOperatorCode("ON1234");
        obj1.setOperatorName("老王");
        obj1.setOperateTime("2017-09-09 12:00:00");
        EntryorderCreateRequest.SenderInfo obj3 = new EntryorderCreateRequest.SenderInfo();
        obj3.setCompany("淘宝");
        obj3.setName("老王");
        obj3.setZipCode("043300");
        obj3.setTel("81020340");
        obj3.setMobile("13214567869");
        obj3.setEmail("345@gmail.com");
        obj3.setCountryCode("051532");
        obj3.setProvince("浙江省");
        obj3.setCity("杭州");
        obj3.setArea("余杭");
        obj3.setTown("横加桥");
        obj3.setDetailAddress("杭州市余杭区989号");
        obj1.setSenderInfo(obj3);
        EntryorderCreateRequest.ReceiverInfo obj4 = new EntryorderCreateRequest.ReceiverInfo();
        obj4.setCompany("淘宝");
        obj4.setName("老王");
        obj4.setZipCode("043300");
        obj4.setTel("808786543");
        obj4.setMobile("13423456785");
        obj4.setIdType("1");
        obj4.setIdNumber("12345");
        obj4.setEmail("878987654@qq.com");
        obj4.setCountryCode("045565");
        obj4.setProvince("浙江省");
        obj4.setCity("杭州");
        obj4.setArea("余杭");
        obj4.setTown("横加桥");
        obj4.setDetailAddress("杭州市余杭区989号");
        obj1.setReceiverInfo(obj4);
        obj1.setRemark("备注信息");
        req.setEntryOrder(obj1);
        EntryorderCreateRequest.OrderLine obj5 = new EntryorderCreateRequest.OrderLine();
        obj5.setOutBizCode("O123");
        obj5.setOrderLineNo("EL123");
        obj5.setOwnerCode("O123");
        obj5.setItemCode("I123");
        obj5.setItemId("CI123");
        obj5.setItemName("IN123");
        obj5.setPlanQty(12L);
        obj5.setSkuProperty("属性");
        obj5.setPurchasePrice("12.0");
        obj5.setRetailPrice("12.0");
        obj5.setInventoryType("ZP");
        obj5.setProductDate("2017-09-09");
        obj5.setExpireDate("2017-09-09");
        obj5.setProduceCode("P1234");
        obj5.setBatchCode("PCODE123");
        List<EntryorderCreateRequest.OrderLine> orderLines=new ArrayList<EntryorderCreateRequest.OrderLine>();
        orderLines.add(obj5);
        req.setOrderLines(orderLines);
        EntryorderCreateResponse rsp = client.execute(req);
        System.out.println(rsp.getBody());
    }
}
