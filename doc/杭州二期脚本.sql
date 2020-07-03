ALTER TABLE `dili-assets`.`lease_order`
ADD COLUMN `asset_type` tinyint(1) NULL COMMENT '资产类型 1：摊位 2：冷库' AFTER `modify_time`;

ALTER TABLE `dili-assets`.`lease_order_item`
CHANGE COLUMN `booth_id` `asset_id` bigint(20) NULL DEFAULT NULL COMMENT '资产ID' AFTER `lease_order_code`,
CHANGE COLUMN `booth_name` `asset_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资产名称' AFTER `asset_id`,
ADD COLUMN `asset_type` tinyint(1) NULL COMMENT '资产类型 1：摊位 2：冷库' AFTER `lease_order_code`,
ADD COLUMN `leases_num` decimal(32, 2) NULL COMMENT '租赁数量' AFTER `number`;
ALTER TABLE `dili-assets`.`refund_order`
MODIFY COLUMN `biz_type` varchar(120) NULL DEFAULT NULL COMMENT '业务类型' AFTER `modify_time`;

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