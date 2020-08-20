--dili-assets, 更新订单的开始和结束时间
update `dili-assets`.assets_lease_order set start_time = '1970-01-01 00:00:00' ,end_time = '1970-01-01 23:59:59' where `code` = 'HZSCTW202007060015';
--dili-basic-data 更新摊位占用的开始和结束时间
UPDATE `dili-basic-data`.booth_rent set `start` = "1970-01-01 00:00:00" , `end` = "1970-01-01 23:59:59"  WHERE order_id in (select id FROM `dili-assets`.assets_lease_order WHERE code='HZSCTW202007060015');


--dili-assets, 更新订单的开始和结束时间
update `dili-assets`.assets_lease_order set start_time = '2019-05-11 00:00:00' where `code` = 'HZSCTW202007070008';
--dili-basic-data 更新摊位占用的开始和结束时间
UPDATE `dili-basic-data`.booth_rent set `start` = "2019-05-11 00:00:00"  WHERE order_id in (select id FROM `dili-assets`.assets_lease_order WHERE code='HZSCTW202007070008');

