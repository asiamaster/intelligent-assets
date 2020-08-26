
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
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-定金退款单', 'hzsc_earnest_refundOrder', 'HZSCTKDJ', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_earnest_refundOrder', 202008120001, '杭州水产-定金退款单', '1');
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

-- 流程控制中心
--动态表单管理
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('managerApprovalForm', 'managerApproval', NULL, 'https://ia.diligrp.com/leaseOrder/1/approval.html', NULL, '2020-07-14 17:03:01', '2020-07-31 14:01:30', '摊位租赁负责人审批');
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('viceGeneralManagerApprovalForm', 'viceGeneralManagerApproval', NULL, 'https://ia.diligrp.com/leaseOrder/1/approval.html', NULL, '2020-07-16 16:06:04', '2020-07-31 14:01:24', '分管领导审批');
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('generalManagerApprovalForm', 'generalManagerApproval', NULL, 'https://ia.diligrp.com/leaseOrder/1/approval.html', NULL, '2020-07-16 16:17:49', '2020-07-31 14:01:27', NULL);
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('processFormKey', 'rentalApprovalProcess', NULL, 'https://ia.diligrp.com/leaseOrder/1/approvalDetail.html', NULL, '2020-07-16 16:54:12', '2020-07-31 14:01:20', '摊位审批流程详情页');
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('processFormKey', 'rentalRefundApprovalProcess', NULL, 'https://ia.diligrp.com/refundOrder/1/approvalDetail.html', NULL, '2020-07-20 17:36:18', '2020-07-20 17:46:43', '摊位退款单审批流程详情页');
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('refundOrderManagerApprovalForm', 'managerApproval', NULL, 'https://ia.diligrp.com/refundOrder/1/approval.html', NULL, '2020-07-20 17:52:19', '2020-07-20 17:52:19', NULL);
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('refundOrderCheckerApprovalForm', 'checkerApprovalForm', NULL, 'https://ia.diligrp.com/refundOrder/1/approval.html', NULL, '2020-07-20 17:52:58', '2020-07-20 17:52:58', NULL);
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('refundOrderViceGeneralManagerApprovalForm', 'viceGeneralManagerApprovalForm', NULL, 'https://ia.diligrp.com/refundOrder/1/approval.html', NULL, '2020-07-20 17:53:13', '2020-07-20 17:53:13', NULL);
INSERT INTO `act_form` (`form_key`, `def_key`, `action_url`, `task_url`, `redirect_url`, `created`, `modified`, `description`) VALUES ('refundOrderGeneralManagerApprovalForm', 'generalManagerApprovalForm', NULL, 'https://ia.diligrp.com/refundOrder/1/approval.html', NULL, '2020-07-20 17:53:28', '2020-07-20 17:53:28', NULL);

--动态任务人分配
INSERT INTO `task_assignment` (`assignee`, `candidate_user`, `candidate_group`, `handler_url`, `task_definition_key`, `process_definition_key`, `creater_id`, `modifier_id`, `created`, `modified`) VALUES (NULL, NULL, NULL, 'https://ia.diligrp.com/api/rentalApprovalProcessTaskAssignmentApi/managerApprovalAssignment', 'managerApproval', 'rentalApprovalProcess', 1, 1, '2020-07-13 17:41:32', '2020-07-13 17:43:20');
INSERT INTO `task_assignment` (`assignee`, `candidate_user`, `candidate_group`, `handler_url`, `task_definition_key`, `process_definition_key`, `creater_id`, `modifier_id`, `created`, `modified`) VALUES (NULL, NULL, NULL, 'https://ia.diligrp.com/api/rentalApprovalProcessTaskAssignmentApi/viceGeneralManagerApprovalAssignment', 'viceGeneralManagerApproval', 'rentalApprovalProcess', 1, 1, '2020-07-13 17:42:17', '2020-07-13 17:43:23');
INSERT INTO `task_assignment` (`assignee`, `candidate_user`, `candidate_group`, `handler_url`, `task_definition_key`, `process_definition_key`, `creater_id`, `modifier_id`, `created`, `modified`) VALUES (NULL, NULL, NULL, 'https://ia.diligrp.com/api/rentalApprovalProcessTaskAssignmentApi/generalManagerApprovalAssignment', 'generalManagerApproval', 'rentalApprovalProcess', 1, 1, '2020-07-13 17:42:39', '2020-07-13 17:43:26');
INSERT INTO `task_assignment` (`assignee`, `candidate_user`, `candidate_group`, `handler_url`, `task_definition_key`, `process_definition_key`, `creater_id`, `modifier_id`, `created`, `modified`) VALUES (NULL, NULL, NULL, 'https://ia.diligrp.com/api/rentalApprovalProcessTaskAssignmentApi/managerApprovalAssignment', 'managerApproval', 'rentalRefundApprovalProcess', 1, 1, '2020-07-17 17:34:47', '2020-07-17 17:34:47');
INSERT INTO `task_assignment` (`assignee`, `candidate_user`, `candidate_group`, `handler_url`, `task_definition_key`, `process_definition_key`, `creater_id`, `modifier_id`, `created`, `modified`) VALUES (NULL, NULL, NULL, 'https://ia.diligrp.com/api/rentalApprovalProcessTaskAssignmentApi/viceGeneralManagerApprovalAssignment', 'viceGeneralManagerApproval', 'rentalRefundApprovalProcess', 1, 1, '2020-07-17 17:35:16', '2020-07-17 17:35:16');
INSERT INTO `task_assignment` (`assignee`, `candidate_user`, `candidate_group`, `handler_url`, `task_definition_key`, `process_definition_key`, `creater_id`, `modifier_id`, `created`, `modified`) VALUES (NULL, NULL, NULL, 'https://ia.diligrp.com/api/rentalApprovalProcessTaskAssignmentApi/generalManagerApprovalAssignment', 'generalManagerApproval', 'rentalRefundApprovalProcess', 1, 1, '2020-07-17 17:35:47', '2020-07-17 17:35:47');
INSERT INTO `task_assignment` (`assignee`, `candidate_user`, `candidate_group`, `handler_url`, `task_definition_key`, `process_definition_key`, `creater_id`, `modifier_id`, `created`, `modified`) VALUES (NULL, NULL, NULL, 'https://ia.diligrp.com/api/rentalApprovalProcessTaskAssignmentApi/checkerApprovalAssignment', 'checkerApproval', 'rentalRefundApprovalProcess', 1, 1, '2020-07-17 17:36:33', '2020-07-17 17:36:33');
