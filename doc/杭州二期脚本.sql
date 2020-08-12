-- 退款数据处理
UPDATE assets_lease_order set state = 6 WHERE state = 7;
UPDATE assets_lease_order_item set state = 6 WHERE state = 7;
-- 资产类型初始化
UPDATE assets_lease_order set assets_type = 1;
UPDATE assets_lease_order_item set assets_type = 1;

ALTER TABLE `apportion_record`
MODIFY COLUMN `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间' AFTER `id`,
ADD COLUMN `lease_order_id` bigint(20) NOT NULL COMMENT '订单ID' AFTER `create_time`,
MODIFY COLUMN `lease_item_id` bigint(20) NOT NULL COMMENT '订单项ID' AFTER `create_time`,
MODIFY COLUMN `charge_item_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收费项名称' AFTER `charge_item_id`;
ALTER TABLE `apportion_record`
CHANGE COLUMN `lease_item_id` `lease_order_item_id` bigint(20) NOT NULL COMMENT '订单项ID' AFTER `create_time`;

ALTER TABLE `assets_lease_order_item`
CHANGE COLUMN `booth_id` `assets_id` bigint(20) NULL DEFAULT NULL COMMENT '资产ID' AFTER `lease_order_code`,
CHANGE COLUMN `booth_name` `assets_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资产名称' AFTER `assets_id`;
ALTER TABLE `assets_lease_order_item`
ADD COLUMN `exit_time` datetime(0) NULL COMMENT '退场时间' AFTER `refund_state`;
update assets_lease_order_item set total_amount = rent_amount + manage_amount;

ALTER TABLE `refund_order`
ADD COLUMN `payee_certificate_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收款人证件号' AFTER `payee`;
ALTER TABLE `transfer_deduction_item`
ADD COLUMN `payee_certificate_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收款人证件号' AFTER `payee`;

-- 编号规则类型修改
delete from `biz_number` where type = 'leaseOrder';
delete from `biz_number_rule` where type = 'leaseOrder';

delete from `biz_number` where type = 'paymentOrder';
delete from `biz_number_rule` where type = 'paymentOrder';

delete from `biz_number` where type = 'leaseRefundOrder';
delete from `biz_number_rule` where type = 'leaseRefundOrder';

delete from `biz_number` where type = 'refundOrder';
delete from `biz_number_rule` where type = 'refundOrder';

INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_boothLease_leaseOrder', 202008100051, '摊位租赁订单号', '1');
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('摊位租赁订单号', 'hzsc_boothLease_leaseOrder', 'HZSCTW', 'yyyyMMdd', 4, '1');

INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_boothLease_paymentOrder', 2020081200051, '摊位租赁缴费单号', '1');
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('摊位租赁缴费单号', 'hzsc_boothLease_paymentOrder', 'HZSCPOTW', 'yyyyMMdd', 5, '1');

INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_boothLease_refundOrder', 202007290051, '摊位租赁退款单', '1');
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ( '摊位租赁退款单', 'hzsc_boothLease_refundOrder', 'HZSCTKTW', 'yyyyMMdd', 4, '1');


--  字段删除脚本（数据迁移完后执行）
ALTER TABLE `lease_order`
DROP COLUMN `rent_amount`,
DROP COLUMN `deposit_amount`,
DROP COLUMN `manage_amount`,
DROP COLUMN `deposit_deduction`;

--  字段删除脚本（数据迁移完后执行）
ALTER TABLE `lease_order_item`
DROP COLUMN `deposit_amount_flag`,
DROP COLUMN `deposit_amount_source_id`,
DROP COLUMN `deposit_amount`,
DROP COLUMN `manage_amount`,
DROP COLUMN `rent_amount`,
DROP COLUMN `refund_amount`,
DROP COLUMN `deposit_refund_amount`,
DROP COLUMN `manage_refund_amount`,
DROP COLUMN `rent_refund_amount`;