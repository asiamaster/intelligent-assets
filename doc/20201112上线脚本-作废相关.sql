ALTER TABLE `dili_ia`.`earnest_order`
ADD COLUMN  `invalid_operator_id` bigint(20) DEFAULT NULL COMMENT '作废人ID' AFTER `canceler`,
ADD COLUMN  `invalid_operator` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作废人名字' AFTER `invalid_operator_id`,
ADD COLUMN  `invalid_time` datetime DEFAULT NULL COMMENT '作废时间' AFTER `invalid_operator`,
ADD COLUMN  `invalid_reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作废原因' AFTER `invalid_time`;

ALTER TABLE `dili_ia`.`deposit_order`
ADD COLUMN  `invalid_operator_id` bigint(20) DEFAULT NULL COMMENT '作废人ID' AFTER `canceler`,
ADD COLUMN  `invalid_operator` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作废人名字' AFTER `invalid_operator_id`,
ADD COLUMN  `invalid_time` datetime DEFAULT NULL COMMENT '作废时间' AFTER `invalid_operator`,
ADD COLUMN  `invalid_reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作废原因' AFTER `invalid_time`;

ALTER TABLE `dili_ia`.`assets_lease_order`
ADD COLUMN `invalid_operator_id` bigint(20) DEFAULT NULL COMMENT '作废人ID' AFTER `canceler`,
ADD COLUMN `invalid_operator` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作废人名字' AFTER `invalid_operator_id`,
ADD COLUMN `invalid_time` datetime DEFAULT NULL COMMENT '作废时间' AFTER `invalid_operator`,
ADD COLUMN `invalid_reason` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '作废原因' AFTER `invalid_time`;

ALTER TABLE `dili_ia`.`payment_order`
ADD COLUMN `is_reverse` tinyint(1) NULL COMMENT '是否红冲（1：是 0：否）' AFTER `is_settle`;