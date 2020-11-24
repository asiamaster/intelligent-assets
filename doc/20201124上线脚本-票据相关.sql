ALTER TABLE `dili_ia`.`assets_lease_order_item`
ADD COLUMN `deposit_balance` bigint(20) NULL default 0 COMMENT '保证金余额快照' AFTER `district_name`;