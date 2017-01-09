select t.batch_code as "拣货批次",
       t.order_code as "订单号",
       t.consignee as "收货人",
       t.address as "收货地址",
       t.mobile as "联系电话",
       case t.payment_type
         when 1 then
          '在线支付'
         when 2 then
          '货到付款'
       end as "付款方式",
       t.shipping_name as "快递类型",
       t.shipping_no as "运单号",
       t.order_time as "下单时间",
       t.pay_no as "交易号",
       t.order_amount as "订单金额",
       t.payable_amount as "应付金额",
       t.order_status as "订单状态",
       t.shipping_time as "发货时间",
       t1.sku_name as "SKU名称",
       t1.quantity as "数量",
       t1.unit_price as "商品单价",
       t1.subtotal_price as "商品总价"
  from WMS_SALES_ORDER t
  join wms_sales_order_goods t1
    on t.id = t1.order_id
 where t.shipping_time >
       to_date('2014-05-01 00:00:00', 'yyyy-mm-dd hh24:mi:ss')
       order by t.shipping_time
