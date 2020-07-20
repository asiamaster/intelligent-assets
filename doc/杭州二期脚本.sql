
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

-- 改表名
ALTER  TABLE lease_order RENAME TO assets_lease_order;
ALTER  TABLE lease_order_item RENAME TO assets_lease_order_item;

update assets_lease_order_item set total_amount = rent_amount + manage_amount;

--租赁单和退款单新增流程实例和定义id
ALTER TABLE `assets_lease_order`
	ADD COLUMN `process_instance_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '流程实例id' AFTER `version`,
	ADD COLUMN `process_definition_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '流程定义id' AFTER `process_instance_id`,
	ADD COLUMN `approval_state` TINYINT NULL DEFAULT NULL COMMENT '审批状态(1: 待审批 2: 审批中 3:审批通过 4:审批拒绝)' AFTER `process_definition_id`;
ALTER TABLE `refund_order`
	ADD COLUMN `process_instance_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '流程实例id' AFTER `version`,
	ADD COLUMN `process_definition_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '流程定义id' AFTER `process_instance_id`,
    ADD COLUMN `approval_state` TINYINT NULL DEFAULT NULL COMMENT '审批状态(1: 待审批 2: 审批中 3:审批通过 4:审批拒绝)' AFTER `process_definition_id`;

--创建审批表
create table approval_process
(
   id                   bigint(20) not null comment 'id',
   process_instance_id  varchar(64) comment '流程实例id',
   task_name            varchar(50) comment '审批状态(根据流程来定义)',
   task_id              varchar(64) comment '任务id(用于任务追踪)',
   assignee             bigint comment '办理人id',
   assignee_name        varchar(20) comment '办理人名称(冗余UAP用户名)',
   create_time          datetime comment '创建时间',
   opinion              varchar(120) comment '审批意见',
   result               varchar(20) comment '审批结果',
   business_key         varchar(20) comment '业务key',
   business_type        tinyint comment '业务类型(1:租赁交费 2:租赁退款)',
   process_name         varchar(40) comment '流程名称',
   firm_id              bigint comment '商户id',
   primary key (id)
);

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