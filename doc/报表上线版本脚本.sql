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

CREATE TABLE `deposit_balance`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `customer_id` bigint(20) NULL DEFAULT NULL COMMENT '客户ID',
  `customer_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `certificate_number` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户证件号',
  `customer_cellphone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户电话',
  `type_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '保证金类型，来源数据字典',
  `type_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '保证金类型名称',
  `assets_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产类型',
  `assets_id` bigint(20) NULL DEFAULT NULL COMMENT '资产ID',
  `assets_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产名称',
  `balance` bigint(20) NULL DEFAULT 0 COMMENT '保证金余额',
  `market_id` bigint(20) NULL DEFAULT NULL COMMENT '市场Id',
  `market_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市场CODE',
  `version` bigint(20) NULL DEFAULT 0 COMMENT '版本控制,乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;

-- ----------------------------
-- Table structure for deposit_order
-- ----------------------------
CREATE TABLE `deposit_order`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP(0) ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务编号',
  `customer_id` bigint(20) NULL DEFAULT NULL COMMENT '客户ID',
  `customer_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `certificate_number` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户证件号',
  `customer_cellphone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户电话',
  `department_id` bigint(20) NULL DEFAULT NULL COMMENT '业务所属部门ID',
  `department_name` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务所属部门名称',
  `type_code` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '保证金类型，来源数据字典',
  `type_name` varchar(120) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '保证金类型名称',
  `assets_type` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产类型',
  `assets_id` bigint(20) NULL DEFAULT NULL COMMENT '资产ID',
  `assets_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '资产名称',
  `amount` bigint(20) NULL DEFAULT 0 COMMENT '保证金金额= 已付金额 + 待付金额',
  `paid_amount` bigint(20) NULL DEFAULT 0 COMMENT '已付金额',
  `wait_amount` bigint(20) NULL DEFAULT 0 COMMENT '待付金额',
  `refund_amount` bigint(20) NULL DEFAULT 0 COMMENT '退款金额，用于多次退款记录',
  `notes` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注信息',
  `is_related` tinyint(1) NULL DEFAULT 0 COMMENT '是否关联订单1，是，0否',
  `business_id` bigint(20) NULL DEFAULT NULL COMMENT '关联订单ID',
  `biz_type` varchar(120) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL COMMENT '关联订单业务类型',
  `state` int(11) NULL DEFAULT NULL COMMENT '（1：已创建 2：已取消 3：已提交 4：已交费5：退款中 6：已退款）',
  `pay_state` tinyint(1) NULL DEFAULT NULL COMMENT '支付状态（1：未交费 2：未交清 3：已交清）',
  `refund_state` tinyint(1) NOT NULL DEFAULT 1 COMMENT '1:未退款  2：部分退款 3：全额退款',
  `approval_state` tinyint(1) NULL DEFAULT NULL COMMENT '审批状态',
  `process_instance_id` bigint(20) NULL DEFAULT NULL COMMENT ' 流程实例ID',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建操作员ID',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '创建人名称',
  `submitter_id` bigint(20) NULL DEFAULT NULL COMMENT '提交人ID',
  `submitter` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '提交人名称',
  `submit_time` datetime(0) NULL DEFAULT NULL COMMENT '提交时间',
  `withdraw_operator_id` bigint(20) NULL DEFAULT NULL COMMENT '撤回人ID',
  `withdraw_operator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '撤回人名称',
  `canceler_id` bigint(20) NULL DEFAULT NULL COMMENT '取消人ID',
  `canceler` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '取消人名称',
  `market_id` bigint(20) NULL DEFAULT NULL COMMENT '市场Id',
  `market_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '市场CODE',
  `version` bigint(20) NULL DEFAULT 0 COMMENT '版本控制,乐观锁',
  `is_import` tinyint(1) NULL DEFAULT 0 COMMENT '是否老数据导入订单，1是，0否',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = Compact;