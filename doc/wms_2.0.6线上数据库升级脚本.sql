ALTER TABLE wms_wares ADD wares_model varchar2(50);
ALTER TABLE wms_wares ADD wares_remark varchar2(50);
COMMENT ON COLUMN wms_wares.wares_model
  is '商品规格型号';
COMMENT ON COLUMN wms_wares.wares_remark
  is '商品备注';

UPDATE wms_wares SET wares_model=wares_name;

-- Add/modify columns 
alter table WMS_TRANSFER add transfer_sale VARCHAR2(32);
alter table WMS_TRANSFER add transfer_send VARCHAR2(32);
alter table WMS_TRANSFER add transfer_invoice VARCHAR2(32);
alter table WMS_TRANSFER add order_amount number(10,2);
-- Add comments to the columns 
comment on column WMS_TRANSFER.transfer_sale
  is '售达方';
comment on column WMS_TRANSFER.transfer_send
  is '送达方';
comment on column WMS_TRANSFER.transfer_invoice
  is '开票方';
comment on column WMS_TRANSFER.order_amount
  is '订单金额';

-- Add/modify columns 
alter table WMS_TRANSFER add shipping_time date;
-- Add comments to the columns 
comment on column WMS_TRANSFER.shipping_time
  is '发货时间';

-- Create table
create table WMS_TRANSFER_PARTNER
(
  id     VARCHAR2(32) not null,
  name   VARCHAR2(64),
  code   VARCHAR2(32),
  remark VARCHAR2(256)
)
tablespace WMS_DATA
  pctfree 10
  initrans 1
  maxtrans 255;
-- Add comments to the columns 
comment on column WMS_TRANSFER_PARTNER.id
  is '主键';
comment on column WMS_TRANSFER_PARTNER.name
  is '名称';
comment on column WMS_TRANSFER_PARTNER.code
  is '编码';
comment on column WMS_TRANSFER_PARTNER.remark
  is '备注';
-- Create/Recreate indexes 
create unique index UK_PARTNER_CODE on WMS_TRANSFER_PARTNER (CODE)
  tablespace WMS_DATA
  pctfree 10
  initrans 2
  maxtrans 255;
-- Create/Recreate primary, unique and foreign key constraints 
alter table WMS_TRANSFER_PARTNER
  add constraint PK_TRANSFER_PARTNER primary key (ID)
  using index 
  tablespace WMS_DATA
  pctfree 10
  initrans 2
  maxtrans 255;

INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '天猫旗舰店', '100314');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '官网在线支付', '');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '官网货到付款', '100315');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '官网招行直联', '100558');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '淘宝分销', '100316');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '唯品会入仓', '100504');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '唯品会直发', '100543');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '京东', '100503');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '易迅深圳', '100519');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '易迅北京', '100520');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '易迅武汉', '100521');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '易迅重庆', '100522');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '易迅西安', '100523');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '易迅上海', '100505');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '苏宁', '100553');
INSERT INTO wms_transfer_partner(ID, NAME, CODE) VALUES(wms_seq_common.nextval, '1号店', '110538');