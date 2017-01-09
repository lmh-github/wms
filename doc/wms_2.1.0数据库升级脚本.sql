-- Create table
create table WMS_STOCK_DAILYSTOCK
(
  id              VARCHAR2(32) not null,
  sku_code        VARCHAR2(32),
  report_date     DATE,
  startstockqty   NUMBER(8) default 0,
  outstockqty     NUMBER(8) default 0,
  occupystockqty  NUMBER(8) default 0,
  endstockqty     NUMBER(8) default 0,
  createdate      DATE,
  startdefeqty    NUMBER(8) default 0,
  startnondefeqty NUMBER(8) default 0,
  outdefeqty      NUMBER(8) default 0,
  outnondefeqty   NUMBER(8) default 0,
  instockqty      NUMBER(8) default 0,
  indefeqty       NUMBER(8) default 0,
  innondefeqty    NUMBER(8) default 0,
  enddefeqty      NUMBER(8) default 0,
  endnondefeqty   NUMBER(8) default 0,
  sku_name        VARCHAR2(32)
);
-- Add comments to the table 
comment on table WMS_STOCK_DAILYSTOCK
  is '每日库存信息';
-- Add comments to the columns 
comment on column WMS_STOCK_DAILYSTOCK.id
  is '主键';
comment on column WMS_STOCK_DAILYSTOCK.sku_code
  is 'sku编码';
comment on column WMS_STOCK_DAILYSTOCK.report_date
  is '记录时间';
comment on column WMS_STOCK_DAILYSTOCK.startstockqty
  is '期初总库存';
comment on column WMS_STOCK_DAILYSTOCK.outstockqty
  is '本期出库量';
comment on column WMS_STOCK_DAILYSTOCK.occupystockqty
  is '占用库存量';
comment on column WMS_STOCK_DAILYSTOCK.endstockqty
  is '期末库存量';
comment on column WMS_STOCK_DAILYSTOCK.createdate
  is '创建时间';
comment on column WMS_STOCK_DAILYSTOCK.startdefeqty
  is '期初不可销库存';
comment on column WMS_STOCK_DAILYSTOCK.startnondefeqty
  is '期初可销库存';
comment on column WMS_STOCK_DAILYSTOCK.outdefeqty
  is '本期次品出库';
comment on column WMS_STOCK_DAILYSTOCK.outnondefeqty
  is '本期良品出库';
comment on column WMS_STOCK_DAILYSTOCK.instockqty
  is '本期入库';
comment on column WMS_STOCK_DAILYSTOCK.indefeqty
  is '本期次品入库';
comment on column WMS_STOCK_DAILYSTOCK.innondefeqty
  is '本期良品入库';
comment on column WMS_STOCK_DAILYSTOCK.enddefeqty
  is '期末不可销库存';
comment on column WMS_STOCK_DAILYSTOCK.endnondefeqty
  is '期末可销库存';
comment on column WMS_STOCK_DAILYSTOCK.sku_name
  is 'SKU名称';
-- Create/Recreate primary, unique and foreign key constraints 
alter table WMS_STOCK_DAILYSTOCK
  add constraint DAILYSTOCK_PK primary key (ID)
  using index;
  
-- Create table
create table WMS_STOCK_DAILYSTOCKTEMP
(
  id       VARCHAR2(32) not null,
  stock_id VARCHAR2(32),
  type     VARCHAR2(4),
  quantity NUMBER(8)
);
-- Add comments to the table 
comment on table WMS_STOCK_DAILYSTOCKTEMP
  is '每日库存信息过渡表';
-- Add comments to the columns 
comment on column WMS_STOCK_DAILYSTOCKTEMP.id
  is '主键';
comment on column WMS_STOCK_DAILYSTOCKTEMP.stock_id
  is '库存id';
comment on column WMS_STOCK_DAILYSTOCKTEMP.type
  is '类型：具体见wmsconstant类';
comment on column WMS_STOCK_DAILYSTOCKTEMP.quantity
  is '数量';
-- Create/Recreate primary, unique and foreign key constraints 
alter table WMS_STOCK_DAILYSTOCKTEMP
  add constraint DAILYSTOCKTEMP_PK primary key (ID)
  using index;
  
-- Create table
create table WMS_STOCK_HISTORY
(
  id               VARCHAR2(32) not null,
  sku_id           VARCHAR2(32),
  warehouse_id     VARCHAR2(32),
  total_quantity   NUMBER(8),
  sales_quantity   NUMBER(8),
  unsales_quantity NUMBER(8),
  occupy_quantity  NUMBER(8),
  lock_quantity    NUMBER(8),
  virtual_quantity NUMBER(8),
  air_quantity     NUMBER(8),
  limit_upper      NUMBER(8),
  check_id         VARCHAR2(32),
  check_code       VARCHAR2(32),
  nondefective_pl  NUMBER(8) default 0,
  defective_pl     NUMBER(8) default 0,
  limit_lower      NUMBER(8),
  ver              NUMBER(10) default 0,
  sync_status      VARCHAR2(2)
);
-- Create/Recreate primary, unique and foreign key constraints 
alter table WMS_STOCK_HISTORY
  add constraint PK_WMS_STOCK_HISTORY primary key (ID)
  using index;

alter table WMS_INDIV_FLOW add CASE_CODE varchar2(32);    --添加箱号
alter table WMS_INDIV add CASE_CODE varchar2(32);