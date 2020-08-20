--dili-assets, 更新订单的开始和结束时间
update assets_lease_order set start_time = '1970-01-01 00:00:00' ,end_time = '1970-01-01 23:59:59' where `code` = 'HZSCTW202005180013';
--dili-basic-data 更新摊位占用的开始和结束时间
UPDATE booth_rent set `start` = "1970-01-01 00:00:00" , `end` = "1970-01-01 23:59:59"  WHERE order_id = 34;