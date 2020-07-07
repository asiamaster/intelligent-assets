ALTER TABLE `dili-assets`.`lease_order`
ADD COLUMN `assets_type` tinyint(1) NULL COMMENT '资产类型 1：摊位 2：冷库' AFTER `modify_time`;
ALTER TABLE `dili-assets`.`lease_order`
MODIFY COLUMN `payment_id` bigint(20) NULL DEFAULT NULL COMMENT '缴费单ID(缴费中)' AFTER `wait_amount`;


ALTER TABLE `dili-assets`.`lease_order_item`
ADD COLUMN `paid_amount` bigint(20) NULL DEFAULT 0 COMMENT '已付金额' AFTER `total_amount`,
ADD COLUMN `wait_amount` bigint(20) NULL DEFAULT 0 COMMENT '待付金额' AFTER `paid_amount`;
ALTER TABLE `dili-assets`.`lease_order_item`
CHANGE COLUMN `booth_id` `assets_id` bigint(20) NULL DEFAULT NULL COMMENT '资产ID' AFTER `lease_order_code`,
CHANGE COLUMN `booth_name` `assets_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资产名称' AFTER `asset_id`,
ADD COLUMN `assets_type` tinyint(1) NULL COMMENT '资产类型 1：摊位 2：冷库' AFTER `lease_order_code`,
ADD COLUMN `leases_num` decimal(32, 2) NULL COMMENT '租赁数量' AFTER `number`;
ALTER TABLE `dili-assets`.`refund_order`
MODIFY COLUMN `biz_type` varchar(120) NULL DEFAULT NULL COMMENT '业务类型' AFTER `modify_time`;
ALTER TABLE `dili-assets`.`payment_order`
MODIFY COLUMN `biz_type` varchar(120) NULL DEFAULT NULL COMMENT '业务类型' AFTER `business_code`;

ALTER TABLE `dili-assets`.`transaction_details`
MODIFY COLUMN `biz_type` varchar(120) NULL DEFAULT NULL COMMENT '业务类型' ;

-- 字段删除脚本（数据迁移完后执行）
ALTER TABLE `dili-assets`.`lease_order`
DROP COLUMN `rent_amount`,
DROP COLUMN `deposit_amount`,
DROP COLUMN `manage_amount`,
DROP COLUMN `deposit_deduction`;

-- 字段删除脚本（数据迁移完后执行）
ALTER TABLE `dili-assets`.`lease_order_item`
DROP COLUMN `deposit_amount_flag`,
DROP COLUMN `deposit_amount_source_id`,
DROP COLUMN `deposit_amount`,
DROP COLUMN `manage_amount`,
DROP COLUMN `rent_amount`,
DROP COLUMN `refund_amount`,
DROP COLUMN `deposit_refund_amount`,
DROP COLUMN `manage_refund_amount`,
DROP COLUMN `rent_refund_amount`;

-- 改表名
ALTER  TABLE lease_order RENAME TO assets_lease_order;
ALTER  TABLE lease_order_item RENAME TO assets_lease_order_item;