<?xml version="1.0" encoding="utf-8"?>
<Request service="OrderService" lang="zh-CN">
    <Head>7699476943</Head>
    <Body>
    <Order orderid="${order.orderCode!}"
           is_gen_bill_no="1"
           custid="7693255199"
           j_company="${j_company!}"
           j_contact="${j_contact!}"
           j_tel="400-779-6666"
           j_mobile=""
           j_province="${j_province!}"
           j_city="${j_city!}"
           j_county="${j_county!}"
           j_address="${j_address!}"
           d_company="${order.consignee?if_exists}"
           d_contact="${order.consignee?if_exists}"
           d_tel="${order.tel?if_exists}"
           d_mobile="${order.mobile?if_exists}"
           d_address="${order.fullAddress?if_exists}"
           express_type="2"
           pay_method="1"
           parcel_quantity="1"
           remark="${order.orderCode!}">
    <#if orderGoods??>
        <#list orderGoods as g>
            <Cargo name="${g.skuCode}" count="${g.quantity?string}" unit="${g.measureUnit!}" weight="" amount="" currency="" source_area=""></Cargo>
        </#list>
    </#if>
    <#if order.paymentType==2>
        <AddedService name="COD" value="${(order.payableAmount?if_exists)?string}" value1="7693255199"></AddedService></#if>
        <AddedService name="INSURE" value="${(order.payableAmount?if_exists)?string}"></AddedService>
    </Order>
    </Body>
</Request>