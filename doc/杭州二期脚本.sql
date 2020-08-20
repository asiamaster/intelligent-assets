-- 退款数据处理
UPDATE assets_lease_order set state = 6 WHERE state = 7;
UPDATE assets_lease_order_item set state = 6 WHERE state = 7;
-- 资产类型初始化
UPDATE assets_lease_order set assets_type = 1;
UPDATE assets_lease_order_item set assets_type = 1;

ALTER TABLE `apportion_record`
MODIFY COLUMN `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP(0) COMMENT '创建时间' AFTER `id`,
ADD COLUMN `lease_order_id` bigint(20) NOT NULL COMMENT '订单ID' AFTER `create_time`,
MODIFY COLUMN `lease_item_id` bigint(20) NOT NULL COMMENT '订单项ID' AFTER `create_time`,
MODIFY COLUMN `charge_item_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '收费项名称' AFTER `charge_item_id`;
ALTER TABLE `apportion_record`
CHANGE COLUMN `lease_item_id` `lease_order_item_id` bigint(20) NOT NULL COMMENT '订单项ID' AFTER `create_time`;

ALTER TABLE `assets_lease_order_item`
CHANGE COLUMN `booth_id` `assets_id` bigint(20) NULL DEFAULT NULL COMMENT '资产ID' AFTER `lease_order_code`,
CHANGE COLUMN `booth_name` `assets_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资产名称' AFTER `assets_id`;
ALTER TABLE `assets_lease_order_item`
ADD COLUMN `exit_time` datetime(0) NULL COMMENT '退场时间' AFTER `refund_state`;
update assets_lease_order_item set total_amount = rent_amount + manage_amount;

ALTER TABLE `refund_order`
ADD COLUMN `payee_certificate_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收款人证件号' AFTER `payee`;
ALTER TABLE `transfer_deduction_item`
ADD COLUMN `payee_certificate_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收款人证件号' AFTER `payee`;

-- 编号规则类型修改
delete from `biz_number` where type = 'leaseOrder';
delete from `biz_number_rule` where type = 'leaseOrder';

delete from `biz_number` where type = 'paymentOrder';
delete from `biz_number_rule` where type = 'paymentOrder';

delete from `biz_number` where type = 'leaseRefundOrder';
delete from `biz_number_rule` where type = 'leaseRefundOrder';

delete from `biz_number` where type = 'refundOrder';
delete from `biz_number_rule` where type = 'refundOrder';

delete from `biz_number` where type = 'settleOrder';
delete from `biz_number_rule` where type = 'settleOrder';

delete from `biz_number` where type = 'earnestOrder';
delete from `biz_number_rule` where type = 'earnestOrder';

delete from `biz_number` where type = 'earnestTransferOrder';
delete from `biz_number_rule` where type = 'earnestTransferOrder';

delete from `biz_number` where type = 'transactionCode';
delete from `biz_number_rule` where type = 'transactionCode';

delete from `biz_number` where type = 'earnesRefundtOrder';
delete from `biz_number_rule` where type = 'earnesRefundtOrder';

INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_boothLease_leaseOrder', 202008100051, '杭州水产-摊位租赁订单号', '1');
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-摊位租赁订单号', 'hzsc_boothLease_leaseOrder', 'HZSCTW', 'yyyyMMdd', 4, '1');

INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_boothLease_paymentOrder', 2020081200051, '杭州水产-摊位租赁缴费单号', '1');
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-摊位租赁缴费单号', 'hzsc_boothLease_paymentOrder', 'HZSCPOTW', 'yyyyMMdd', 5, '1');

INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_boothLease_refundOrder', 202007290051, '杭州水产-摊位租赁退款单', '1');
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ( '杭州水产-摊位租赁退款单', 'hzsc_boothLease_refundOrder', 'HZSCTKTW', 'yyyyMMdd', 4, '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-定金缴费单', 'hzsc_earnest_paymentOrder', 'HZSCPODJ', 'yyyyMMdd', 5, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_earnest_paymentOrder', 2020081200001, '杭州水产-定金缴费单', '1');
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-保证金缴费单', 'hzsc_deposit_paymentOrder', 'HZSCPOBZJ', 'yyyyMMdd', 5, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_deposit_paymentOrder', 2020081200001, '杭州水产-保证金缴费单', '1');
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-定金业务单', 'hzsc_earnestOrder', 'HZSCDJ', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_earnestOrder', 202008120001, '杭州水产-定金业务单', '1');
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-定金退款单', 'hzsc_earnes_refundtOrder', 'HZSCTKDJ', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_earnes_refundtOrder', 202008120001, '杭州水产-定金退款单', '1');
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-定金转移单', 'hzsc_earnestTransferOrder', 'HZSCZY', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_earnestTransferOrder', 202008120001, '杭州水产-定金转移单', '1');
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-客户账户流水号', 'hzsc_transactionCode', 'HZSCLS', 'yyyyMMdd', 6, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_transactionCode', 20200812000001, '杭州水产-客户账户流水号', '1');
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-保证金业务单', 'hzsc_depositOrder', 'HZSCBZJ', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_depositOrder', 202008120001, '杭州水产-保证金业务单', '1');
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-保证金退款单', 'hzsc_deposit_refundOrder', 'HZSCTKBZJ', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_deposit_refundOrder', 202008120001, '杭州水产-保证金退款单', '1');
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-结算单', 'hzsc_settleOrder', 'HZSCJS', 'yyyyMMdd', 5, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_settleOrder', 2020081200001, '杭州水产-结算单', '1');

--  字段删除脚本（数据迁移完后执行）
ALTER TABLE `assets_lease_order`
DROP COLUMN `rent_amount`,
DROP COLUMN `deposit_amount`,
DROP COLUMN `manage_amount`,
DROP COLUMN `deposit_deduction`;

--  字段删除脚本（数据迁移完后执行）
ALTER TABLE `assets_lease_order_item`
DROP COLUMN `deposit_amount_flag`,
DROP COLUMN `deposit_amount_source_id`,
DROP COLUMN `deposit_amount`,
DROP COLUMN `manage_amount`,
DROP COLUMN `rent_amount`,
DROP COLUMN `refund_amount`,
DROP COLUMN `deposit_refund_amount`,
DROP COLUMN `manage_refund_amount`,
DROP COLUMN `rent_refund_amount`;


-- 结算 dili-settlement
INSERT INTO `application_config` (`app_id`, `group_code`, `code`, `val`, `state`, `notes`) VALUES
 (101, 101, 3, '保证金', 1, 'group_code(101)表示业务类型'),
 (101, 102, 3, 'http://ia.diligrp.com:8381/depositOrder/view.action', 1, 'group_code(102)表示缴费业务详情URL'),
 (101, 103, 3, 'http://ia.diligrp.com:8381/refundOrder/view.action', 1, 'group_code(103)表示退款业务详情URL'),
 (101, 104, 3, 'http://ia.diligrp.com:8381/api/depositOrder/queryPrintData', 1, 'group_code(104)表示缴费打印数据URL'),
 (101, 105, 3, 'http://ia.diligrp.com:8381/api/refundOrder/queryPrintData', 1, 'group_code(105)表示退款打印数据URL');

-- 流程控制中心
--动态表单管理
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('managerApprovalForm', 'managerApproval', NULL, 'http://ia.diligrp.com:8381/leaseOrder/1/approval.html', NULL, '2020-07-14 17:03:01', '2020-07-31 14:01:30', '摊位租赁负责人审批');
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('viceGeneralManagerApprovalForm', 'viceGeneralManagerApproval', NULL, 'http://ia.diligrp.com:8381/leaseOrder/1/approval.html', NULL, '2020-07-16 16:06:04', '2020-07-31 14:01:24', '分管领导审批');
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('generalManagerApprovalForm', 'generalManagerApproval', NULL, 'http://ia.diligrp.com:8381/leaseOrder/1/approval.html', NULL, '2020-07-16 16:17:49', '2020-07-31 14:01:27', NULL);
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('processFormKey', 'rentalApprovalProcess', NULL, 'http://ia.diligrp.com:8381/leaseOrder/1/approvalDetail.html', NULL, '2020-07-16 16:54:12', '2020-07-31 14:01:20', '摊位审批流程详情页');
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('processFormKey', 'rentalRefundApprovalProcess', NULL, 'http://ia.diligrp.com:8381/refundOrder/1/approvalDetail.html', NULL, '2020-07-20 17:36:18', '2020-07-20 17:46:43', '摊位退款单审批流程详情页');
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('refundOrderManagerApprovalForm', 'managerApproval', NULL, 'http://ia.diligrp.com:8381/refundOrder/1/approval.html', NULL, '2020-07-20 17:52:19', '2020-07-20 17:52:19', NULL);
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('refundOrderCheckerApprovalForm', 'checkerApprovalForm', NULL, 'http://ia.diligrp.com:8381/refundOrder/1/approval.html', NULL, '2020-07-20 17:52:58', '2020-07-20 17:52:58', NULL);
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('refundOrderViceGeneralManagerApprovalForm', 'viceGeneralManagerApprovalForm', NULL, 'http://ia.diligrp.com:8381/refundOrder/1/approval.html', NULL, '2020-07-20 17:53:13', '2020-07-20 17:53:13', NULL);
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('refundOrderGeneralManagerApprovalForm', 'generalManagerApprovalForm', NULL, 'http://ia.diligrp.com:8381/refundOrder/1/approval.html', NULL, '2020-07-20 17:53:28', '2020-07-20 17:53:28', NULL);

--动态任务人分配
INSERT INTO `task_assignment` (`assignee`, `candidate_user`, `candidate_group`, `handler_url`, `task_definition_key`, `process_definition_key`, `creater_id`, `modifier_id`, `created`, `modified`) VALUES (NULL, NULL, NULL, 'http://ia.diligrp.com:8381/api/rentalApprovalProcessTaskAssignmentApi/managerApprovalAssignment', 'managerApproval', 'rentalApprovalProcess', 1, 1, '2020-07-13 17:41:32', '2020-07-13 17:43:20');
INSERT INTO `task_assignment` (`assignee`, `candidate_user`, `candidate_group`, `handler_url`, `task_definition_key`, `process_definition_key`, `creater_id`, `modifier_id`, `created`, `modified`) VALUES (NULL, NULL, NULL, 'http://ia.diligrp.com:8381/api/rentalApprovalProcessTaskAssignmentApi/viceGeneralManagerApprovalAssignment', 'viceGeneralManagerApproval', 'rentalApprovalProcess', 1, 1, '2020-07-13 17:42:17', '2020-07-13 17:43:23');
INSERT INTO `task_assignment` (`assignee`, `candidate_user`, `candidate_group`, `handler_url`, `task_definition_key`, `process_definition_key`, `creater_id`, `modifier_id`, `created`, `modified`) VALUES (NULL, NULL, NULL, 'http://ia.diligrp.com:8381/api/rentalApprovalProcessTaskAssignmentApi/generalManagerApprovalAssignment', 'generalManagerApproval', 'rentalApprovalProcess', 1, 1, '2020-07-13 17:42:39', '2020-07-13 17:43:26');
INSERT INTO `task_assignment` (`assignee`, `candidate_user`, `candidate_group`, `handler_url`, `task_definition_key`, `process_definition_key`, `creater_id`, `modifier_id`, `created`, `modified`) VALUES (NULL, NULL, NULL, 'http://ia.diligrp.com:8381/api/rentalApprovalProcessTaskAssignmentApi/managerApprovalAssignment', 'managerApproval', 'rentalRefundApprovalProcess', 1, 1, '2020-07-17 17:34:47', '2020-07-17 17:34:47');
INSERT INTO `task_assignment` (`assignee`, `candidate_user`, `candidate_group`, `handler_url`, `task_definition_key`, `process_definition_key`, `creater_id`, `modifier_id`, `created`, `modified`) VALUES (NULL, NULL, NULL, 'http://ia.diligrp.com:8381/api/rentalApprovalProcessTaskAssignmentApi/viceGeneralManagerApprovalAssignment', 'viceGeneralManagerApproval', 'rentalRefundApprovalProcess', 1, 1, '2020-07-17 17:35:16', '2020-07-17 17:35:16');
INSERT INTO `task_assignment` (`assignee`, `candidate_user`, `candidate_group`, `handler_url`, `task_definition_key`, `process_definition_key`, `creater_id`, `modifier_id`, `created`, `modified`) VALUES (NULL, NULL, NULL, 'http://ia.diligrp.com:8381/api/rentalApprovalProcessTaskAssignmentApi/generalManagerApprovalAssignment', 'generalManagerApproval', 'rentalRefundApprovalProcess', 1, 1, '2020-07-17 17:35:47', '2020-07-17 17:35:47');
INSERT INTO `task_assignment` (`assignee`, `candidate_user`, `candidate_group`, `handler_url`, `task_definition_key`, `process_definition_key`, `creater_id`, `modifier_id`, `created`, `modified`) VALUES (NULL, NULL, NULL, 'http://ia.diligrp.com:8381/api/rentalApprovalProcessTaskAssignmentApi/checkerApprovalAssignment', 'checkerApproval', 'rentalRefundApprovalProcess', 1, 1, '2020-07-17 17:36:33', '2020-07-17 17:36:33');
