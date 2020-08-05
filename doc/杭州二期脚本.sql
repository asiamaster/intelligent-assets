ALTER TABLE `dili-assets`.`apportion_record`
MODIFY COLUMN `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间' AFTER `id`,
ADD COLUMN `lease_order_id` bigint(20) NOT NULL COMMENT '订单ID' AFTER `create_time`,
MODIFY COLUMN `lease_item_id` bigint(20) NOT NULL COMMENT '订单项ID' AFTER `create_time`,
MODIFY COLUMN `charge_item_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收费项名称' AFTER `charge_item_id`;
ALTER TABLE `dili-assets`.`apportion_record`
CHANGE COLUMN `lease_item_id` `lease_order_item_id` bigint(20) NOT NULL COMMENT '订单项ID' AFTER `create_time`;

ALTER TABLE `dili-assets`.`assets_lease_order_item`
CHANGE COLUMN `booth_id` `assets_id` bigint(20) NULL DEFAULT NULL COMMENT '资产ID' AFTER `lease_order_code`,
CHANGE COLUMN `booth_name` `assets_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资产名称' AFTER `asset_id`;
ALTER TABLE `dili-assets`.`assets_lease_order_item`
ADD COLUMN `exit_time` datetime(0) NULL COMMENT '退场时间' AFTER `refund_state`;
update assets_lease_order_item set total_amount = rent_amount + manage_amount;

ALTER TABLE `dili-assets`.`refund_order`
ADD COLUMN `payee_certificate_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收款人证件号' AFTER `payee`;
ALTER TABLE `dili-assets`.`transfer_deduction_item`
ADD COLUMN `payee_certificate_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收款人证件号' AFTER `payee`;

--租赁单和退款单新增流程实例和定义id
ALTER TABLE `assets_lease_order`
	ADD COLUMN `is_invoice` TINYINT NULL DEFAULT '0' COMMENT '是否开票 1:是 0：否' AFTER `version`,
	ADD COLUMN `process_instance_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '流程实例id' AFTER `is_invoice`,
	ADD COLUMN `process_definition_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '流程定义id' AFTER `process_instance_id`,
	ADD COLUMN `approval_state` TINYINT NULL DEFAULT NULL COMMENT '审批状态(1: 待审批 2: 审批中 3:审批通过 4:审批拒绝)' AFTER `process_definition_id`;
ALTER TABLE `refund_order`
	ADD COLUMN `process_instance_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '流程实例id' AFTER `version`,
	ADD COLUMN `process_definition_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '流程定义id' AFTER `process_instance_id`,
    ADD COLUMN `approval_state` TINYINT NULL DEFAULT NULL COMMENT '审批状态(1: 待审批 2: 审批中 3:审批通过 4:审批拒绝)' AFTER `process_definition_id`;

--创建审批表
CREATE TABLE `approval_process` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
	`process_instance_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '流程实例id' COLLATE 'utf8mb4_general_ci',
	`task_name` VARCHAR(50) NULL DEFAULT NULL COMMENT '任务名(根据流程来定义)' COLLATE 'utf8mb4_general_ci',
	`task_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '任务id(用于任务追踪)' COLLATE 'utf8mb4_general_ci',
	`assignee` BIGINT(20) NULL DEFAULT NULL COMMENT '办理人id',
	`assignee_name` VARCHAR(20) NULL DEFAULT NULL COMMENT '办理人名称(冗余UAP用户名)' COLLATE 'utf8mb4_general_ci',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间，即办理时间',
	`opinion` VARCHAR(120) NULL DEFAULT NULL COMMENT '审批意见' COLLATE 'utf8mb4_general_ci',
	`result` TINYINT(4) NULL DEFAULT NULL COMMENT '审批结果, 1:同意， 2:拒绝',
	`business_key` VARCHAR(20) NULL DEFAULT NULL COMMENT '业务key' COLLATE 'utf8mb4_general_ci',
	`business_type` TINYINT(4) NULL DEFAULT NULL COMMENT '业务类型(1:租赁交费 2:租赁退款)',
	`process_name` VARCHAR(40) NULL DEFAULT NULL COMMENT '流程名称' COLLATE 'utf8mb4_general_ci',
	`task_time` DATETIME NULL DEFAULT NULL COMMENT '任务开始时间，用于计算任务耗时',
	`firm_id` BIGINT(20) NULL DEFAULT NULL COMMENT '商户id',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='审批流程表，记录每个市场，每种业务下的审批记录'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=94
;


--任务人分配
CREATE TABLE `approver_assignment` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT,
	`district_id` BIGINT(20) NOT NULL COMMENT '区域id，可能是一级或二级',
	`assignee` BIGINT(20) NULL DEFAULT NULL COMMENT '办理用户id',
	`task_definition_key` VARCHAR(50) NOT NULL COMMENT '任务定义key' COLLATE 'utf8_general_ci',
	`process_definition_key` VARCHAR(64) NOT NULL COMMENT '流程定义key' COLLATE 'utf8_general_ci',
	`modify_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='任务人分配'
COLLATE='utf8_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=108
;

--开票记录
CREATE TABLE `invoice_record` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
	`business_key` VARCHAR(64) NULL DEFAULT NULL COMMENT '业务号' COLLATE 'utf8mb4_general_ci',
	`type` TINYINT(4) NULL DEFAULT NULL COMMENT '开票类型, 1:普票， 2: 专票',
	`target` VARCHAR(64) NULL DEFAULT NULL COMMENT '开票主体' COLLATE 'utf8mb4_general_ci',
	`target_id` BIGINT(20) NULL DEFAULT NULL COMMENT '开票主体id',
	`amount` VARCHAR(64) NULL DEFAULT NULL COMMENT '开票金额' COLLATE 'utf8mb4_general_ci',
	`invoice_date` DATE NULL DEFAULT NULL COMMENT '开票日期',
	`creator_id` BIGINT(20) NULL DEFAULT NULL COMMENT '开票人',
	`creator` VARCHAR(20) NULL DEFAULT NULL COMMENT '开票人名称' COLLATE 'utf8mb4_general_ci',
	`notes` VARCHAR(120) NULL DEFAULT NULL COMMENT '备注' COLLATE 'utf8mb4_general_ci',
	`create_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
	`modify_time` DATETIME NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
	`firm_id` BIGINT(20) NULL DEFAULT NULL COMMENT '市场id',
	PRIMARY KEY (`id`) USING BTREE
)
COMMENT='开票记录'
COLLATE='utf8mb4_general_ci'
ENGINE=InnoDB
AUTO_INCREMENT=3
;

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