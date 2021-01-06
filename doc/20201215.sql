INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-摊位租赁退款单', 'hzsc_booth_lease_refundOrder', 'HZSCTKTW', 'yyyyMMdd', 4, '1', '2020-11-25 15:39:54', '2020-11-25 15:39:54', 1, 50);
INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-摊位租赁缴费单号', 'hzsc_booth_lease_paymentOrder', 'HZSCPOTW', 'yyyyMMdd', 5, '1', '2020-11-25 15:38:46', '2020-11-25 15:38:46', 1, 50);
INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-摊位租赁订单号', 'hzsc_booth_lease_leaseOrder', 'HZSCTW', 'yyyyMMdd', 4, '1', '2020-11-25 15:37:46', '2020-11-25 15:37:46', 1, 50);

INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-冷库租赁退款单', 'hzsc_location_lease_refundOrder', 'HZSCTKLK', 'yyyyMMdd', 4, '1', '2020-11-25 15:34:20', '2020-11-25 15:34:20', 1, 50);
INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-冷库租赁缴费单号', 'hzsc_location_lease_paymentOrder', 'HZSCPOLK', 'yyyyMMdd', 5, '1', '2020-11-25 15:32:57', '2020-11-25 15:32:57', 1, 50);
INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-冷库租赁订单号', 'hzsc_location_lease_leaseOrder', 'HZSCLK', 'yyyyMMdd', 4, '1', '2020-11-25 15:31:24', '2020-11-25 15:35:34', 1, 50);

INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-公寓租赁退款单', 'hzsc_lodging_lease_refundOrder', 'HZSCTKGY', 'yyyyMMdd', 4, '1', '2020-11-25 15:39:54', '2020-11-25 15:39:54', 1, 50);
INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-公寓租赁缴费单号', 'hzsc_lodging_lease_paymentOrder', 'HZSCPOGY', 'yyyyMMdd', 5, '1', '2020-11-25 15:38:46', '2020-11-25 15:38:46', 1, 50);
INSERT INTO `uap`.`biz_number_rule` (`name`, `type`, `prefix`, `date_format`, `length`, `range`, `create_time`, `update_time`, `is_enable`, `step`) VALUES ('杭水-公寓租赁订单号', 'hzsc_lodging_lease_leaseOrder', 'HZSCGY', 'yyyyMMdd', 4, '1', '2020-11-25 15:37:46', '2020-11-25 15:37:46', 1, 50);


update `dili_ia`.`assets_lease_order_item` set leases_num = number;
ALTER TABLE `dili_ia`.`assets_lease_order`
ADD COLUMN `biz_type` varchar(120) NULL COMMENT '业务类型（数据字典值编码 1：摊位租赁 4：冷库租赁 5：公寓租赁）' AFTER `assets_type`;
ALTER TABLE `dili_ia`.`assets_lease_order`
ADD COLUMN `mch_id` bigint(20) NULL COMMENT '商户ID' AFTER `market_code`;
UPDATE dili_ia.assets_lease_order set mch_id = market_id WHERE mch_id IS NULL;
ALTER TABLE `dili_ia`.`assets_lease_order`
ADD COLUMN `organization_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '组织类型,个人/企业' AFTER `customer_name`;
ALTER TABLE `dili_ia`.`assets_lease_order_item`
ADD COLUMN `biz_type` varchar(120) NULL COMMENT '业务类型（数据字典值编码 1：摊位租赁 4：冷库租赁 5：公寓租赁）' AFTER `assets_type`;
update `dili_ia`.`assets_lease_order` set biz_type = '1';
update `dili_ia`.`assets_lease_order_item` set biz_type = '1';
ALTER TABLE `dili_ia`.`assets_lease_order_item`
ADD COLUMN `corner` tinyint(4) NULL DEFAULT NULL COMMENT '是否转角 1:是 2：否 冗余资产' AFTER `leases_num`;
UPDATE `dili_ia`.`assets_lease_order_item` SET corner = 1 WHERE is_corner = '是';
UPDATE `dili_ia`.`assets_lease_order_item` SET corner = 2 WHERE is_corner = '否';
ALTER TABLE `dili_ia`.`assets_lease_order_item`
DROP COLUMN `is_corner`;

/**添加缴费单待付金额字段，用于补打显示**/
ALTER TABLE `dili_ia`.`payment_order`
ADD COLUMN `wait_amount` bigint(20) DEFAULT '0' COMMENT '待付金额' AFTER `amount`;

/**添加区域ID及商户ID**/
ALTER TABLE `dili_ia`.`payment_order`
ADD COLUMN `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID' AFTER `market_code`,
ADD COLUMN `district_id` bigint(20) DEFAULT NULL COMMENT '区域ID（末级区域ID）' AFTER `mch_id`;

ALTER TABLE `dili_ia`.`refund_order`
ADD COLUMN `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID' AFTER `market_code`,
ADD COLUMN `district_id` bigint(20) DEFAULT NULL COMMENT '区域ID（末级区域ID）' AFTER `mch_id`;

ALTER TABLE `dili_ia`.`deposit_order`
ADD COLUMN `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID' AFTER `market_code`,
ADD COLUMN `first_district_id` bigint(20) DEFAULT NULL COMMENT '一级区域ID' AFTER `mch_id`,
ADD COLUMN `second_district_id` bigint(20) DEFAULT NULL COMMENT '二级区域ID' AFTER `first_district_id`;

ALTER TABLE `dili_ia`.`deposit_balance`
ADD COLUMN `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID' AFTER `market_code`;

ALTER TABLE `dili_ia`.`earnest_order`
ADD COLUMN `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID' AFTER `market_id`,
ADD COLUMN `first_district_id` bigint(20) DEFAULT NULL COMMENT '一级区域ID' AFTER `mch_id`,
ADD COLUMN `second_district_id` bigint(20) DEFAULT NULL COMMENT '二级区域ID' AFTER `first_district_id`;

ALTER TABLE `dili_ia`.`refund_order`
ADD COLUMN `remark` varchar(100) NULL COMMENT '备注' AFTER `canceler`;

ALTER TABLE `dili_ia`.`business_charge_item`
ADD COLUMN `rule_id` bigint(20) NULL COMMENT '规则ID' AFTER `charge_item_name`;
ALTER TABLE `dili_ia`.`business_charge_item`
ADD COLUMN `rule_name` varchar(100) NULL AFTER `rule_id`;
ALTER TABLE `dili_ia`.`business_charge_item`
ADD COLUMN `rule_amount` bigint(20) NULL COMMENT '规则计算金额' AFTER `rule_name`;

ALTER TABLE `dili_ia`.`assets_lease_order`
ADD COLUMN `batch_id` varchar(30) NULL DEFAULT NULL COMMENT '批次号' AFTER `mch_id`;

ALTER TABLE `dili_ia`.`assets_lease_order_item`
ADD COLUMN `first_district_id` bigint(20) NULL DEFAULT NULL COMMENT '一级区域ID' AFTER `stop_rent_state`,
ADD COLUMN `first_district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '一级区域名称' AFTER `first_district_id`,
ADD COLUMN `second_district_id` bigint(20) NULL DEFAULT NULL COMMENT '二级区域ID' AFTER `first_district_name`,
ADD COLUMN `second_district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二级区域名称' AFTER `second_district_id`;

ALTER TABLE `dili_ia`.`earnest_transfer_order`
ADD COLUMN `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID' AFTER `market_id`,
DROP COLUMN state,
DROP COLUMN payee_customer_account_id,
DROP COLUMN payer_transaction_details_code,
DROP COLUMN payee_transaction_code;

-- 处理线上老数据商户字段
UPDATE dili_ia.assets_lease_order set mch_id = market_id WHERE mch_id IS NULL;
UPDATE dili_ia.payment_order set mch_id = market_id WHERE mch_id IS NULL;
UPDATE dili_ia.refund_order set mch_id = market_id WHERE mch_id IS NULL;

-- 处理租赁单区域老数据
UPDATE `dili_ia`.assets_lease_order_item ali ,`dili-basic-data`.district dt
SET ali.first_district_id = (
	CASE dt.parent_id
	WHEN 0 THEN
		dt.id
	ELSE
		dt.parent_id
	END
),
ali.second_district_id = (
	CASE dt.parent_id
	WHEN 0 THEN
		NULL
	ELSE
		dt.id
	END
)
WHERE ali.district_id = dt.id;

-- 处理租赁单区域老数据
UPDATE `dili_ia`.assets_lease_order_item ali,`dili-basic-data`.district dt
set ali.first_district_name = dt.name
WHERE ali.first_district_id = dt.id;

-- 处理租赁单区域老数据
UPDATE `dili_ia`.assets_lease_order_item ali,`dili-basic-data`.district dt
set ali.second_district_name = dt.name
WHERE ali.second_district_id = dt.id;

-- 处理同步客户组织类型数据
UPDATE `dili_ia`.assets_lease_order alo,
`dili-customer`.customer cus
SET alo.organization_type = cus.organization_type
WHERE
	alo.customer_id = cus.id;

-- 表管理
CREATE TABLE `dili_ia`.`meter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime DEFAULT NULL COMMENT '创建日期',
  `modify_time` datetime DEFAULT NULL COMMENT '修改日期',
  `number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '表编号',
  `type` bigint(20) DEFAULT NULL COMMENT '表类型',
  `assets_type` tinyint(1) DEFAULT NULL COMMENT '资产类型code',
  `assets_id` bigint(20) DEFAULT NULL COMMENT '对应编号ID',
  `assets_name` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '对应编号,名称',
  `this_amount` bigint(20) DEFAULT NULL COMMENT '表初始值',
  `price` bigint(20) DEFAULT NULL COMMENT '单价',
  `creator_dep_id` bigint(20) DEFAULT NULL COMMENT '创建人所属于部门ID',
  `balance` bigint(20) DEFAULT NULL COMMENT '水电预存余额',
  `notes` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建操作员ID',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人名称',
  `market_id` bigint(20) DEFAULT NULL COMMENT '市场Id',
  `market_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '市场CODE',
  `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID',
  `mch_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商户名称',
  `first_district_id` bigint(20) DEFAULT NULL COMMENT '一级区域ID',
  `first_district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '一级区域名称',
  `second_district_id` bigint(20) DEFAULT NULL COMMENT '二级区域ID',
  `second_district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '二级区域名称',
  `version` int(11) DEFAULT NULL COMMENT '版本控制,乐观锁',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT;

-- 表用户
CREATE TABLE `dili_ia`.`customer_meter` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改日期',
  `meter_id` bigint(20) DEFAULT NULL COMMENT '表',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户 id',
  `customer_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '客户姓名',
  `customer_cellphone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '客户手机号',
  `certificate_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户证件号',
  `creator_dep_id` bigint(20) DEFAULT NULL COMMENT '创建人所属于部门ID',
  `state` int(11) DEFAULT NULL COMMENT '状态，已绑定，解除绑定',
  `notes` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建操作员ID',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人名称',
  `market_id` bigint(20) DEFAULT NULL COMMENT '市场Id',
  `market_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '市场CODE',
  `version` int(11) DEFAULT NULL COMMENT '版本控制,乐观锁',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_cus_meter_id` (`meter_id`) USING BTREE COMMENT '表列表的主键'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT;

-- 水电费
CREATE TABLE `dili_ia`.`meter_detail` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '业务编号',
  `meter_id` bigint(20) DEFAULT NULL COMMENT '表ID',
  `usage_time` datetime DEFAULT NULL COMMENT '截止月份',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `customer_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '客户姓名',
  `customer_cellphone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号',
  `department_id` bigint(20) DEFAULT NULL COMMENT '业务部门',
  `department_name` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '业务部门名称',
  `recorder_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '抄表员名称',
  `record_time` datetime DEFAULT NULL COMMENT '抄表时间',
  `state` int(11) NOT NULL COMMENT '状态，撤销/正常',
  `last_amount` bigint(20) DEFAULT NULL COMMENT '上次结算的数量',
  `this_amount` bigint(20) DEFAULT NULL COMMENT '本次结算的总数量',
  `usage_amount` bigint(20) DEFAULT NULL COMMENT '使用量',
  `receivable` bigint(20) DEFAULT NULL COMMENT '水电费',
  `amount` bigint(20) DEFAULT NULL COMMENT '总金额',
  `creator_dep_id` bigint(20) DEFAULT NULL COMMENT '创建人所属于部门ID',
  `notes` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `submitter_id` bigint(20) DEFAULT NULL COMMENT '提交人ID',
  `submitter` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '提交人名称',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `withdraw_operator_id` bigint(20) DEFAULT NULL COMMENT '撤回人ID',
  `withdraw_operator` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '撤回人名称',
  `canceler_id` bigint(20) DEFAULT NULL COMMENT '取消人ID',
  `canceler` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '取消人名称',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建操作员ID',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人名称',
  `market_id` bigint(20) DEFAULT NULL COMMENT '市场Id',
  `market_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '市场CODE',
  `version` int(11) DEFAULT NULL COMMENT '版本控制,乐观锁',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_met_det_meter_id` (`meter_id`) USING BTREE COMMENT '表列表的主键',
  KEY `idx_met_det_usage_time` (`usage_time`) USING BTREE COMMENT '使用月份查询'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COLLATE=utf8_bin ROW_FORMAT=COMPACT;

-- 其他收费
CREATE TABLE `dili_ia`.`other_fee` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `code` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务编号',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `customer_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户名称',
  `certificate_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户证件号',
  `customer_cellphone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户电话',
  `department_id` bigint(20) DEFAULT NULL COMMENT '业务所属部门ID',
  `department_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务所属部门名称',
  `charge_item_id` bigint(20) DEFAULT NULL COMMENT '收费项ID',
  `charge_item_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收费项名称',
  `assets_type` int(11) DEFAULT NULL COMMENT '资产类型，费用类型',
  `assets_id` bigint(20) DEFAULT NULL COMMENT '资产ID',
  `assets_name` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资产名称',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `category_id` bigint(20) DEFAULT NULL COMMENT '品类id',
  `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '品类名称',
  `amount` bigint(20) DEFAULT '0' COMMENT '金额',
  `refund_amount` bigint(20) DEFAULT '0' COMMENT '退款金额',
  `notes` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注信息',
  `state` int(11) DEFAULT NULL COMMENT '（1：已创建 2：已取消 3：已提交 4：已交费 5：退款中 6：已退款）',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建操作员ID',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '创建人名称',
  `withdraw_operator_id` bigint(20) DEFAULT NULL COMMENT '撤回人ID',
  `withdraw_operator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '撤回人名称',
  `canceler_id` bigint(20) DEFAULT NULL COMMENT '取消人ID',
  `canceler` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '取消人名称',
  `market_id` bigint(20) DEFAULT NULL COMMENT '市场Id',
  `market_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '市场CODE',
  `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID',
  `mch_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商户名称',
  `first_district_id` bigint(20) DEFAULT NULL COMMENT '一级区域ID',
  `first_district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '一级区域名称',
  `second_district_id` bigint(20) DEFAULT NULL COMMENT '二级区域ID',
  `second_district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '二级区域名称',
  `version` int(11) DEFAULT '0' COMMENT '版本控制,乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- 其他收费关联部门收费项
CREATE TABLE `dili_ia`.`department_charge_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `department_id` bigint(20) DEFAULT NULL COMMENT '业务所属部门ID',
  `department_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务所属部门名称',
  `charge_item_id` bigint(20) DEFAULT NULL COMMENT '收费项ID(对应数据字典的编码)',
  `charge_item_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '收费项名称',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建操作员ID',
  `creator` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '创建人名称',
  `market_id` bigint(20) DEFAULT NULL COMMENT '市场Id',
  `market_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '市场CODE',
  `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID',
  `mch_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商户名称',
  `version` int(11) DEFAULT '0' COMMENT '版本控制,乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC;

-- 通行证
CREATE TABLE `dili_ia`.`passport` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '业务编号',
  `license_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '通行证件类型代号',
  `license_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '证件号',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `customer_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '客户姓名',
  `customer_cellphone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号',
  `certificate_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户证件号',
  `gender` int(2) DEFAULT '1' COMMENT '客户性别',
  `department_id` bigint(20) DEFAULT NULL COMMENT '业务部门',
  `department_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '业务部门名称',
  `car_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '车牌号',
  `car_type` int(11) DEFAULT NULL COMMENT '车型',
  `valid_period` int(11) DEFAULT NULL COMMENT '有效期',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '截止时间',
  `toll_amount` bigint(20) DEFAULT NULL COMMENT '通行费',
  `amount` bigint(20) DEFAULT '0' COMMENT '金额',
  `state` int(11) NOT NULL COMMENT '状态',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建人ID',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人名称',
  `submitter_id` bigint(20) DEFAULT NULL COMMENT '提交人ID',
  `submitter` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '提交人名称',
  `submit_time` datetime DEFAULT NULL COMMENT '提交时间',
  `withdraw_operator_id` bigint(20) DEFAULT NULL COMMENT '撤回人ID',
  `withdraw_operator` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '撤回人名称',
  `withdraw_time` datetime DEFAULT NULL COMMENT '撤回时间',
  `canceler_id` bigint(20) DEFAULT NULL COMMENT '取消人ID',
  `canceler` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '取消人名称',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `notes` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `market_id` bigint(20) DEFAULT NULL COMMENT '市场ID',
  `market_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '市场CODE',
  `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID',
  `mch_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商户名称',
  `version` int(11) DEFAULT NULL COMMENT '版本控制,乐观锁',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_pas_cre_time` (`create_time`) USING BTREE COMMENT '创建时间字段',
  KEY `idx_pas_car_number` (`car_number`) USING BTREE COMMENT '车牌号字段',
  KEY `idx_pas_lic_number` (`license_number`) USING BTREE COMMENT '证件号字段'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT COMMENT='通行证';

-- 精品黄楼停车记录
CREATE TABLE `dili_ia`.`boutique_entrance_record` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `customer_id` bigint(20) DEFAULT NULL COMMENT '客户ID',
  `customer_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '客户姓名',
  `customer_cellphone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '手机号',
  `certificate_number` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '客户证件号',
  `plate` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '挂号',
  `car_type_id` bigint(20) DEFAULT NULL COMMENT '车型id',
  `department_id` bigint(20) DEFAULT NULL COMMENT '接车部门',
  `department_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '部门名称',
  `enter_time` datetime DEFAULT NULL COMMENT '进场时间',
  `confirm_time` datetime DEFAULT NULL COMMENT '确认时间',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `count_time` datetime DEFAULT NULL COMMENT '计费时间',
  `leave_time` datetime DEFAULT NULL COMMENT '离场时间',
  `total_amount` bigint(20) DEFAULT '0' COMMENT '交费总额',
  `state` int(11) NOT NULL COMMENT '状态 1 待确认 2 计费中 3 已离场 4 已取消',
  `operator_id` bigint(20) DEFAULT NULL COMMENT '操作员ID',
  `operator_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '操作员名称',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `bid` bigint(20) DEFAULT NULL COMMENT '进门记录id',
  `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID',
  `mch_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商户名称',
  `version` int(11) DEFAULT NULL COMMENT '版本控制,乐观锁',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `idx_bou_ent_plate` (`plate`) USING BTREE COMMENT '挂号字段'
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='精品停车记录表';

-- 精品黄楼缴费
CREATE TABLE `dili_ia`.`boutique_fee_order` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `record_id` bigint(20) DEFAULT NULL COMMENT '进门记录id',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '业务编号',
  `start_time` datetime DEFAULT NULL COMMENT '开始时间',
  `end_time` datetime DEFAULT NULL COMMENT '结束时间',
  `amount` bigint(20) DEFAULT NULL COMMENT '金额',
  `state` int(11) NOT NULL COMMENT '状态 1 待交费 2 已交费',
  `submitter_id` bigint(20) DEFAULT NULL COMMENT '提交人ID',
  `submitter` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '提交人',
  `canceler_id` bigint(20) DEFAULT NULL COMMENT '取消人ID',
  `canceler` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '取消人名称',
  `cancel_reason` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '取消原因名称',
  `cancel_time` datetime DEFAULT NULL COMMENT '取消时间',
  `notes` varchar(250) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '备注',
  `market_id` bigint(20) DEFAULT NULL COMMENT '市场ID',
  `market_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '市场CODE',
  `version` int(11) DEFAULT NULL COMMENT '版本控制,乐观锁',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `record_id_index` (`record_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='精品停车缴费单表';

-- 精品黄楼停车时长设置
CREATE TABLE `dili_ia`.`boutique_free_sets` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `car_type_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '车型名称',
  `free_hours` int(11) DEFAULT NULL COMMENT '免费小时数',
  `version` int(11) DEFAULT NULL COMMENT '版本控制,乐观锁',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC COMMENT='精品停车免费时长设置表';

-- 摊位出租预设表
CREATE TABLE `dili_ia`.`assets_rental` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `batch_id` bigint(20) DEFAULT NULL COMMENT '一个批次的批次号',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '预设名称',
  `engage_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '经营范围CODE',
  `engage_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '经营范围',
  `category_id` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '品类id',
  `category_name` varchar(400) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '品类名称',
  `lease_term_code` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租赁形式CODE',
  `lease_term_name` varchar(120) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '租赁形式',
  `lease_days` int(11) DEFAULT NULL COMMENT '租赁天数',
  `start_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '开始时间',
  `end_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '结束时间',
  `state` tinyint(11) DEFAULT NULL COMMENT '（1启用,2禁用）',
  `creator_id` bigint(20) DEFAULT NULL COMMENT '创建操作员ID',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '创建人名称',
  `market_id` bigint(20) DEFAULT NULL COMMENT '市场Id',
  `market_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL COMMENT '市场CODE',
  `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID',
  `mch_name` varchar(20) COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '商户名称',
  `first_district_id` bigint(20) DEFAULT NULL COMMENT '一级区域ID',
  `first_district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '一级区域名称',
  `second_district_id` bigint(20) DEFAULT NULL COMMENT '二级区域ID',
  `second_district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '二级区域名称',
  `version` tinyint(4) DEFAULT '0' COMMENT '乐观锁，版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='资产出租预设';

-- 摊位出租详情表
CREATE TABLE `dili_ia`.`assets_rental_item` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `assets_rental_id` bigint(20) NOT NULL COMMENT '资产出租预设ID',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
  `assets_id` bigint(20) DEFAULT NULL COMMENT '资产ID',
  `assets_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '资产名称',
  `assets_type` tinyint(1) DEFAULT NULL COMMENT '资产类型 1：摊位 2：冷库',
  `version` tinyint(4) DEFAULT '0' COMMENT '乐观锁，版本号',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci ROW_FORMAT=DYNAMIC COMMENT='资产出租预设摊位关联表';

-- 冷库库存表
CREATE TABLE `dili_ia`.`stock`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `district_id` bigint(20) NULL DEFAULT NULL COMMENT '区域id',
  `district_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '区域',
  `assets_id` bigint(20) NULL DEFAULT NULL COMMENT '资产 冷库id',
  `assets_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '资产 冷库名称',
  `category_id` bigint(20) NULL DEFAULT NULL,
  `category_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `customer_id` bigint(20) NULL DEFAULT NULL,
  `customer_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `customer_cellphone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `quantity` bigint(20) NULL DEFAULT NULL,
  `weight` bigint(20) NULL DEFAULT NULL,
  `version` int(11) NULL DEFAULT NULL COMMENT '版本号',
  `market_id` bigint(20) NULL DEFAULT NULL COMMENT '市场id',
  `market_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mch_id` bigint(20) NULL DEFAULT NULL COMMENT '商户ID',
  `department_id` bigint(20) NULL DEFAULT NULL COMMENT '部门',
  `department_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `stock_index`(`assets_id`, `category_id`, `customer_id`) USING BTREE COMMENT '仓库,品类,用户对应唯一数据'
) ENGINE = InnoDB AUTO_INCREMENT = 31 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '库存' ROW_FORMAT = Dynamic;

-- 冷库入库单
CREATE TABLE `dili_ia`.`stock_in`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `stock_in_date` datetime(0) NULL DEFAULT NULL COMMENT '入库时间',
  `customer_id` bigint(20) NULL DEFAULT NULL COMMENT '客户id',
  `customer_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `customer_cellphone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT '操作员id',
  `operator_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `weight` bigint(20) NULL DEFAULT NULL COMMENT '总重量',
  `quantity` bigint(20) NULL DEFAULT NULL COMMENT '总数量',
  `amount` bigint(20) NULL DEFAULT NULL COMMENT '总金额',
  `state` tinyint(4) NULL DEFAULT NULL COMMENT '状态',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '入库类型',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '品类id',
  `category_name` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `origin` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '产地',
  `expire_date` datetime(0) NULL DEFAULT NULL COMMENT '过期时间',
  `department_id` bigint(20) NULL DEFAULT NULL COMMENT '部门',
  `department_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `uom` int(11) NULL DEFAULT NULL COMMENT '计价单位',
  `unit_price` bigint(20) NULL DEFAULT NULL COMMENT '单价',
  `version` int(11) NULL DEFAULT NULL,
  `market_id` bigint(20) NULL DEFAULT NULL COMMENT '市场id',
  `market_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mch_id` bigint(20) NULL DEFAULT NULL COMMENT '商户ID',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `creator` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `submitter_id` bigint(20) NULL DEFAULT NULL COMMENT '提交人ID',
  `submitter` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '提交人名字',
  `withdraw_operator_id` bigint(20) NULL DEFAULT NULL COMMENT '撤回人ID',
  `withdraw_operator` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '撤回人名字',
  `canceler_id` bigint(20) NULL DEFAULT NULL COMMENT '取消人ID',
  `canceler` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '取消人名字',
  `tollman_id` bigint(20) NULL DEFAULT NULL COMMENT '收费员id',
  `tollman` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '收费员',
  `payment_order_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '缴费单号',
  `pay_date` datetime(0) NULL DEFAULT NULL COMMENT '支付时间',
  `certificate_number` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '证件号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_index_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 122 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '入库' ROW_FORMAT = Dynamic;

-- 冷库详情表
CREATE TABLE `dili_ia`.`stock_in_detail`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `stock_in_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库单号',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '入库详情编号',
  `unit_weight` bigint(20) UNSIGNED ZEROFILL NULL DEFAULT NULL COMMENT '单件重量',
  `check_operator_id` bigint(20) NULL DEFAULT NULL COMMENT '查件管理员',
  `check_operator` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `pickup_number` varchar(30) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '接车单号',
  `weightman_id` bigint(20) NULL DEFAULT NULL COMMENT '司磅记录',
  `assets_id` bigint(20) NULL DEFAULT NULL COMMENT '库位ID',
  `assets_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `notes` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `first_district_id` bigint(20) NULL DEFAULT NULL COMMENT '父级区域',
  `second_district_id` bigint(20) NULL DEFAULT NULL,
  `second_district_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `receivable` bigint(20) NULL DEFAULT NULL COMMENT '应收款',
  `cope` bigint(20) NULL DEFAULT NULL COMMENT '实收款',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '品类id',
  `category_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类名称',
  `quantity` bigint(20) NULL DEFAULT NULL COMMENT '入库数量',
  `weight` bigint(20) NULL DEFAULT NULL COMMENT '入库总量',
  `amount` bigint(20) NULL DEFAULT NULL COMMENT '入库金额',
  `version` int(11) NULL DEFAULT NULL,
  `market_id` bigint(20) NULL DEFAULT NULL COMMENT '市场id',
  `market_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `car_plate` varchar(10) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车牌号',
  `car_type_public_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '汽车编号,类型',
  `car_type_public_name` varchar(40) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '车型名',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_index_code`(`code`) USING BTREE,
  INDEX `index_stockInCode`(`stock_in_code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 199 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '入库详情(子单)' ROW_FORMAT = Dynamic;

-- 冷库出库记录表
CREATE TABLE `dili_ia`.`stock_out`  (
  `id` bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '出库单号',
  `customer_id` bigint(20) NULL DEFAULT NULL COMMENT '客户ID',
  `customer_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户名称',
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '操作员',
  `creator` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员名称',
  `category_id` bigint(20) NULL DEFAULT NULL COMMENT '品类',
  `category_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `customer_cellphone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户手机号',
  `quantity` bigint(20) NULL DEFAULT NULL COMMENT '数量',
  `stock_out_date` datetime(0) NULL DEFAULT NULL COMMENT '出库时间',
  `department_id` bigint(20) NULL DEFAULT NULL COMMENT '部门id',
  `department_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '部门名称',
  `district_id` bigint(20) NULL DEFAULT NULL COMMENT '区域id',
  `district_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '区域名称',
  `notes` varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `assets_id` bigint(20) NULL DEFAULT NULL COMMENT '冷库ID',
  `assets_name` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '冷库编码',
  `market_id` bigint(20) NULL DEFAULT NULL COMMENT '市场id',
  `market_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `mch_id` bigint(20) NULL DEFAULT NULL COMMENT '商户ID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_index_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 41 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '出库' ROW_FORMAT = Dynamic;


--冷库库存流水表
CREATE TABLE `dili_ia`.`stock_record`  (
  `id` bigint(20) UNSIGNED ZEROFILL NOT NULL AUTO_INCREMENT,
  `quantity` bigint(20) NULL DEFAULT NULL COMMENT '操作数量',
  `weight` bigint(20) NULL DEFAULT NULL COMMENT '操作重量',
  `business_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '业务操作编号(入库单号,出库单号)',
  `type` tinyint(4) NULL DEFAULT NULL COMMENT '入库 1,出库 2,取消 3',
  `stock_id` bigint(20) NULL DEFAULT NULL COMMENT '库存',
  `market_id` bigint(20) NULL DEFAULT NULL COMMENT '市场id',
  `market_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  `operation_day` datetime(0) NULL DEFAULT NULL COMMENT '操作日期',
  `stock_quantity` bigint(20) NULL DEFAULT NULL COMMENT '库存数量',
  `stock_weight` bigint(20) NULL DEFAULT NULL COMMENT '库存重量',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 99 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '库存流水记录' ROW_FORMAT = Dynamic;

-- 冷库司磅记录表
CREATE TABLE `dili_ia`.`stock_weighman_record`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `create_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `gross_weight` bigint(20) NULL DEFAULT NULL COMMENT '毛重',
  `gross_weight_date` timestamp(0) NULL DEFAULT NULL COMMENT '毛重时间',
  `tare_weight` bigint(20) NULL DEFAULT NULL COMMENT '皮重',
  `tare_weight_date` timestamp(0) NULL DEFAULT NULL COMMENT '皮重时间',
  `new_weight` bigint(20) NULL DEFAULT NULL COMMENT '净重',
  `operator_id` bigint(20) NULL DEFAULT NULL,
  `operator_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `tare_operator_id` bigint(20) NULL DEFAULT NULL,
  `tare_operator_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '' COMMENT '回皮司磅员',
  `market_id` bigint(20) NULL DEFAULT NULL,
  `market_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 26 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '司磅记录' ROW_FORMAT = Dynamic;

-- 信息费表
CREATE TABLE `dili_ia`.`message_fee`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '业务编号',
  `create_time` datetime(0) NOT NULL COMMENT '创建时间',
  `modify_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '更新时间',
  `customer_id` bigint(20) NULL DEFAULT NULL,
  `customer_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `customer_cellphone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `customer_certificate_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户证件号',
  `department_id` bigint(20) NULL DEFAULT NULL,
  `department_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `start_date` datetime(0) NULL DEFAULT NULL COMMENT '开始时间',
  `end_date` datetime(0) NULL DEFAULT NULL COMMENT '结束时间',
  `amount` bigint(20) NULL DEFAULT NULL COMMENT '收费金额',
  `transaction_amount` bigint(20) NULL DEFAULT NULL COMMENT '转抵扣金额',
  `pay_amount` bigint(20) NULL DEFAULT NULL COMMENT '支付金额',
  `notes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `state` tinyint(4) NULL DEFAULT NULL COMMENT '状态',
  `sync_status` tinyint(4) NOT NULL DEFAULT 0 COMMENT '推送消息中心状态0-未同步,1-同步成功,2-同步失败',
  `payment_order_code` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缴费单号',
  `operator_id` bigint(20) NULL DEFAULT NULL COMMENT '操作人',
  `operator_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `submitter_id` bigint(20) NULL DEFAULT NULL COMMENT '提交人',
  `submitor_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `creator_id` bigint(20) NULL DEFAULT NULL COMMENT '创建人',
  `creator_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `canceler_id` bigint(20) NULL DEFAULT NULL COMMENT '作废操作人',
  `canceler_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `canceler_notes` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '作废原因',
  `market_id` bigint(20) NULL DEFAULT NULL COMMENT '市场',
  `market_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mch_id` bigint(20) NULL DEFAULT NULL COMMENT '商户ID',
  `version` int(11) NULL DEFAULT NULL COMMENT '版本号',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_index_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 19 CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

-- 劳务马甲表
CREATE TABLE `dili_ia`.`labor`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL,
  `models` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '运营车型',
  `invoice_number` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '发票编号',
  `work_card` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '马甲号',
  `license_plate` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '车牌号',
  `interval` bigint(20) NULL DEFAULT NULL COMMENT '办理周期',
  `start_date` datetime(0) NULL DEFAULT NULL COMMENT '开始日期',
  `end_date` datetime(0) NULL DEFAULT NULL COMMENT '结束日期',
  `state` tinyint(4) NULL DEFAULT NULL COMMENT '状态',
  `pre_state` tinyint(4) NULL DEFAULT NULL COMMENT '上一步状态',
  `notes` varchar(200) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '备注',
  `create_time` datetime(0) NULL DEFAULT NULL COMMENT '创建日期',
  `modify_time` datetime(0) NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '修改时间',
  `operator` bigint(20) NULL DEFAULT NULL COMMENT '操作员',
  `operator_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '操作员姓名',
  `amount` bigint(20) NULL DEFAULT NULL COMMENT '金额',
  `payment_order_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '缴费单',
  `extra_charge_type` bigint(20) NULL DEFAULT NULL COMMENT '加收类型',
  `extra_charge_days` bigint(20) NULL DEFAULT NULL COMMENT '加收天数',
  `extra_charge` bigint(20) NULL DEFAULT NULL COMMENT '加收费用',
  `customer_id` bigint(20) NULL DEFAULT NULL COMMENT '客户ID',
  `customer_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户姓名',
  `customer_cellphone` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '客户手机号',
  `customer_gender` tinyint(4) NULL DEFAULT NULL COMMENT '客户性别',
  `certificate_number` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '证件号',
  `half_year` bigint(20) NULL DEFAULT NULL COMMENT '上下半年',
  `labor_type` varchar(11) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT '1' COMMENT '劳务类型(1:马甲证,2:自用证)',
  `cancel_time` datetime(0) NULL DEFAULT NULL COMMENT '取消时间',
  `canceler_id` bigint(20) NULL DEFAULT NULL COMMENT '取消人',
  `canceler` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '取消人id',
  `rename_fee` bigint(20) NULL DEFAULT NULL COMMENT '更名费用',
  `rename_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更名主单id',
  `remodel_fee` bigint(20) NULL DEFAULT NULL COMMENT '更型费用',
  `remodel_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '更型主单id',
  `renew_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '续费父单ID',
  `market_id` bigint(20) NULL DEFAULT NULL,
  `market_code` varchar(20) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `mch_id` bigint(20) NULL DEFAULT NULL COMMENT '商户ID',
  `department_id` bigint(20) NULL DEFAULT NULL,
  `department_name` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `version` int(11) NULL DEFAULT NULL,
  `creator` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  `creator_id` bigint(20) NULL DEFAULT NULL,
  `submitter_id` bigint(20) NULL DEFAULT NULL,
  `submitter` varchar(40) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `unique_index_code`(`code`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 57 CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = '劳务管理' ROW_FORMAT = Dynamic;

--品类周期表
CREATE TABLE `dili_ia`.`category_storage_cycle`  (
  `id` bigint(20) UNSIGNED NOT NULL COMMENT '品类id',
  `code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '品类code',
  `module_label` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '模块标签',
  `cycle` int(4) NULL DEFAULT NULL COMMENT '存储天数',
  `notes` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '备注',
  `state` tinyint(4) NULL DEFAULT NULL COMMENT '1:启用,2禁用',
  `market_id` bigint(20) NULL DEFAULT NULL,
  `market_code` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Dynamic;
