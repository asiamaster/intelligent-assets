INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-摊位租赁退款单', 'hzsc_booth_lease_refundOrder', 'HZSCTKTW', 'yyyyMMdd', 4, '1', '2020-11-25 15:39:54', '2020-11-25 15:39:54', 1, 50);
INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-摊位租赁缴费单号', 'hzsc_booth_lease_paymentOrder', 'HZSCPOTW', 'yyyyMMdd', 5, '1', '2020-11-25 15:38:46', '2020-11-25 15:38:46', 1, 50);
INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-摊位租赁订单号', 'hzsc_booth_lease_leaseOrder', 'HZSCTW', 'yyyyMMdd', 4, '1', '2020-11-25 15:37:46', '2020-11-25 15:37:46', 1, 50);

INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-冷库租赁退款单', 'hzsc_location_lease_refundOrder', 'HZSCTKLK', 'yyyyMMdd', 4, '1', '2020-11-25 15:34:20', '2020-11-25 15:34:20', 1, 50);
INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-冷库租赁缴费单号', 'hzsc_location_lease_paymentOrder', 'HZSCPOLK', 'yyyyMMdd', 5, '1', '2020-11-25 15:32:57', '2020-11-25 15:32:57', 1, 50);
INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-冷库租赁订单号', 'hzsc_location_lease_leaseOrder', 'HZSCLK', 'yyyyMMdd', 4, '1', '2020-11-25 15:31:24', '2020-11-25 15:35:34', 1, 50);

INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-公寓租赁退款单', 'hzsc_lodging_lease_refundOrder', 'HZSCTKGY', 'yyyyMMdd', 4, '1', '2020-11-25 15:39:54', '2020-11-25 15:39:54', 1, 50);
INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-公寓租赁缴费单号', 'hzsc_lodging_lease_paymentOrder', 'HZSCPOGY', 'yyyyMMdd', 5, '1', '2020-11-25 15:38:46', '2020-11-25 15:38:46', 1, 50);
INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-公寓租赁订单号', 'hzsc_lodging_lease_leaseOrder', 'HZSCGY', 'yyyyMMdd', 4, '1', '2020-11-25 15:37:46', '2020-11-25 15:37:46', 1, 50);

update `dili_ia`.`assets_lease_order_item` set leases_num = number;
ALTER TABLE `dili_ia`.`assets_lease_order`
ADD COLUMN `biz_type` varchar(120) NULL COMMENT '业务类型（数据字典值编码 1：摊位租赁 4：冷库租赁 5：公寓租赁）' AFTER `assets_type`;
ALTER TABLE `dili_ia`.`assets_lease_order`
ADD COLUMN `mch_id` bigint(20) NULL COMMENT '商户ID' AFTER `market_code`;
ALTER TABLE `dili_ia`.`assets_lease_order_item`
ADD COLUMN `biz_type` varchar(120) NULL COMMENT '业务类型（数据字典值编码 1：摊位租赁 4：冷库租赁 5：公寓租赁）' AFTER `assets_type`;
update `dili_ia`.`assets_lease_order` set biz_type = '1';
update `dili_ia`.`assets_lease_order_item` set biz_type = '1';
/**添加区域ID及商户ID**/
ALTER TABLE `dili_ia`.`payment_order`
ADD COLUMN `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID' AFTER `market_code`,
ADD COLUMN `district_id` bigint(20) DEFAULT NULL COMMENT '区域ID（末级区域ID）' AFTER `mch_id`;

ALTER TABLE `dili_ia`.`refund_order`
ADD COLUMN `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID' AFTER `market_code`,
ADD COLUMN `district_id` bigint(20) DEFAULT NULL COMMENT '区域ID（末级区域ID）' AFTER `mch_id`;

ALTER TABLE `dili_ia`.`deposit_order`
ADD COLUMN `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID' AFTER `market_code`,
ADD COLUMN `first_district_id` bigint(20) DEFAULT NULL COMMENT '一级区域ID' AFTER `mch_id`,
ADD COLUMN `second_district_id` bigint(20) DEFAULT NULL COMMENT '二级区域ID' AFTER `first_district_id`;

ALTER TABLE `dili_ia`.`deposit_balance`
ADD COLUMN `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID' AFTER `market_code`,

ALTER TABLE `dili_ia`.`earnest_order`
ADD COLUMN `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID' AFTER `market_id`,
ADD COLUMN `first_district_id` bigint(20) DEFAULT NULL COMMENT '一级区域ID' AFTER `mch_id`,
ADD COLUMN `second_district_id` bigint(20) DEFAULT NULL COMMENT '二级区域ID' AFTER `first_district_id`;

ALTER TABLE `dili_ia`.`refund_order`
ADD COLUMN `remark` varchar(100) NULL COMMENT '备注' AFTER `canceler`;

ALTER TABLE `dili_ia`.`business_charge_item`
ADD COLUMN `rule_id` bigint(20) NULL COMMENT '规则ID' AFTER `charge_item_name`;
ALTER TABLE `dili_ia`.`business_charge_item`
ADD COLUMN `rule_name` varchar(100) NULL AFTER `rule_id`;

ALTER TABLE `dili_ia`.`assets_lease_order`
ADD COLUMN `batch_id` bigint(20) NULL DEFAULT NULL COMMENT '批次号' AFTER `mch_id`;