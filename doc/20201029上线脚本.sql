ALTER TABLE `dili_ia`.`assets_lease_order`
ADD COLUMN `manager_id` bigint(20) NULL COMMENT '管理员ID' AFTER `canceler`,
ADD COLUMN `manager` varchar(20) NULL COMMENT '管理员' AFTER `manager_id`;