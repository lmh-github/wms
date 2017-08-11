<?xml version="1.0" encoding="utf-8"?>
<Request service="OrderService" lang="zh-CN">
    <Head>BSPdevelop</Head>
    <Body>
    <Order orderid="${order.orderCode!}"
           is_gen_bill_no="1"
           j_company="${j_company!}"
           j_contact="${j_contact!}"
           j_tel="400-779-6666"
           j_mobile=""
           j_province="${j_province!}"
           j_city="${j_city!}"
           j_county="${j_county!}"
           j_address="${j_address!}"
           d_company="{order.consignee?if_exists}"
           d_contact="{order.consignee?if_exists}"
           d_tel="${order.tel?if_exists}"
           d_mobile="${order.mobile?if_exists}"
           d_address="${order.fullAddress?if_exists}"
           express_type="1"
           pay_method="1"
           parcel_quantity="1"
           cargo_length="33"
           cargo_width="33"
           cargo_height="33"
           remark="">
        <Cargo name="LV1" count="3" unit="a" weight="" amount="" currency="" source_area=""></Cargo>
        <Cargo name="LV2" count="3" unit="a" weight="" amount="" currency="" source_area=""></Cargo>
        <AddedService name="COD" value="3000" value1="0123456789"></AddedService>
    </Order>
    </Body>
</Request>