-- Add/modify columns 
alter table WMS_TRANSFER_GOODS add unit_price number(12,2);
-- Add comments to the columns 
comment on column WMS_TRANSFER_GOODS.unit_price
  is '单价';

-- Create table
create table WMS_SALES_OUT_STAT
(
  id            RAW(16) default sys_guid() not null,
  sku_id        VARCHAR2(32),
  sku_code      VARCHAR2(32),
  sku_name      VARCHAR2(32),
  quantity      NUMBER(8),
  unit_price    NUMBER(12,2),
  indiv_enabled VARCHAR2(2),
  order_id      VARCHAR2(32),
  order_code    VARCHAR2(32),
  pay_no        VARCHAR2(50),
  shipping_time DATE,
  partner_code  VARCHAR2(32),
  partner_name  VARCHAR2(50),
  out_type      VARCHAR2(2) default 0
);
-- Add comments to the columns 
comment on column WMS_SALES_OUT_STAT.sku_id
  is 'sku_id';
comment on column WMS_SALES_OUT_STAT.sku_code
  is 'sku编码';
comment on column WMS_SALES_OUT_STAT.sku_name
  is 'sku名称';
comment on column WMS_SALES_OUT_STAT.quantity
  is '数量';
comment on column WMS_SALES_OUT_STAT.unit_price
  is '单价';
comment on column WMS_SALES_OUT_STAT.pay_no
  is '支付流水号';
comment on column WMS_SALES_OUT_STAT.shipping_time
  is '发货时间';
comment on column WMS_SALES_OUT_STAT.out_type
  is '出货类型(0:订单, 1:调拨)';
-- Create/Recreate primary, unique and foreign key constraints 
alter table WMS_SALES_OUT_STAT
  add constraint WMS_SALES_OUT_AMOUNT_PK primary key (ID)
  using index;

-- Create table
create table WMS_SALE_STAT
(
  id            VARCHAR2(32) not null,
  order_type    VARCHAR2(16),
  sale_org      VARCHAR2(16),
  fx_channel    VARCHAR2(16),
  saler         VARCHAR2(16),
  sender        VARCHAR2(16),
  invoicer      VARCHAR2(16),
  shipper       VARCHAR2(16),
  order_reason  VARCHAR2(256),
  material_code VARCHAR2(16),
  order_num     NUMBER,
  purchase_code VARCHAR2(32),
  purchase_date DATE,
  shipping_type VARCHAR2(16),
  invoice_type  VARCHAR2(16),
  use           VARCHAR2(16),
  po_code       VARCHAR2(32),
  po_pro_code   VARCHAR2(32),
  unit_price    NUMBER(8,2),
  posting_date  DATE,
  factory       VARCHAR2(32),
  warehouse     VARCHAR2(32),
  stat_date     DATE,
  create_time   DATE default sysdate
);
-- Add comments to the columns 
comment on column WMS_SALE_STAT.id
  is 'PK';
comment on column WMS_SALE_STAT.order_type
  is '订单类型';
comment on column WMS_SALE_STAT.sale_org
  is '销售组织';
comment on column WMS_SALE_STAT.fx_channel
  is '分销渠道';
comment on column WMS_SALE_STAT.saler
  is '售达方';
comment on column WMS_SALE_STAT.sender
  is '送达方';
comment on column WMS_SALE_STAT.invoicer
  is '开票方';
comment on column WMS_SALE_STAT.shipper
  is '承运方';
comment on column WMS_SALE_STAT.order_reason
  is '订单原因';
comment on column WMS_SALE_STAT.material_code
  is '物料编码';
comment on column WMS_SALE_STAT.order_num
  is '订单数量';
comment on column WMS_SALE_STAT.purchase_code
  is '采购订单号';
comment on column WMS_SALE_STAT.purchase_date
  is '采购日期';
comment on column WMS_SALE_STAT.shipping_type
  is '运输方式';
comment on column WMS_SALE_STAT.invoice_type
  is '开票类型';
comment on column WMS_SALE_STAT.use
  is '使用';
comment on column WMS_SALE_STAT.po_code
  is 'PO编号';
comment on column WMS_SALE_STAT.po_pro_code
  is 'PO项目编号';
comment on column WMS_SALE_STAT.unit_price
  is '单价';
comment on column WMS_SALE_STAT.posting_date
  is '凭证日期';
comment on column WMS_SALE_STAT.factory
  is '工厂';
comment on column WMS_SALE_STAT.warehouse
  is '仓库';
comment on column WMS_SALE_STAT.stat_date
  is '统计日期';
comment on column WMS_SALE_STAT.create_time
  is '创建日期';
-- Create/Recreate primary, unique and foreign key constraints 
alter table WMS_SALE_STAT
  add constraint PK_WMS_SALE_STAT primary key (ID)
  using index;
