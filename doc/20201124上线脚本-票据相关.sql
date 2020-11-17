ALTER TABLE `dili_ia`.`deposit_order`
ADD COLUMN  `balance` bigint(20) DEFAULT '0' COMMENT '创建时该保证金维度余额快照' AFTER `refund_amount`;