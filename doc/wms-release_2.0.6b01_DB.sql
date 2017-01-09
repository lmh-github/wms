-- Add/modify columns 
alter table WMS_SALES_ORDER add payment_time date;
-- Add comments to the columns 
comment on column WMS_SALES_ORDER.payment_time
  is 'Ö§¸¶Ê±¼ä';