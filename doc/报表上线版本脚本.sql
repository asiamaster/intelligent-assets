--begin 报表 上线版本
ALTER TABLE `dili-assets`.`payment_order`
ADD COLUMN `customer_id` bigint(20) NULL DEFAULT NULL COMMENT '客户ID' AFTER `modify_time`,
ADD COLUMN `customer_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '客户名称' AFTER `customer_id`;
UPDATE
  payment_order p
	LEFT JOIN assets_lease_order alo ON p.business_id = alo.id
	SET p.customer_id = alo.customer_id,
	p.customer_name = alo.customer_name
WHERE
	p.biz_type = 1
	
UPDATE payment_order po
LEFT JOIN earnest_order eo ON po.business_id=eo.id
SET po.customer_id=eo.customer_id, po.customer_name=eo.customer_name
WHERE po.biz_type=2
---end