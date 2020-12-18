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
ALTER TABLE `dili_ia`.`assets_lease_order_item`
ADD COLUMN `biz_type` varchar(120) NULL COMMENT '业务类型（数据字典值编码 1：摊位租赁 4：冷库租赁 5：公寓租赁）' AFTER `assets_type`;
update `dili_ia`.`assets_lease_order` set biz_type = '1';
update `dili_ia`.`assets_lease_order_item` set biz_type = '1';
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
ADD COLUMN `mch_id` bigint(20) DEFAULT NULL COMMENT '商户ID' AFTER `market_code`,

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

ALTER TABLE `dili_ia`.`assets_lease_order`
ADD COLUMN `batch_id` bigint(20) NULL DEFAULT NULL COMMENT '批次号' AFTER `mch_id`;

ALTER TABLE `dili_ia`.`assets_lease_order_item`
ADD COLUMN `first_district_id` bigint(20) NULL DEFAULT NULL COMMENT '一级区域ID' AFTER `stop_rent_state`,
ADD COLUMN `first_district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '一级区域名称' AFTER `first_district_id`,
ADD COLUMN `second_district_id` bigint(20) NULL DEFAULT NULL COMMENT '二级区域ID' AFTER `first_district_name`,
ADD COLUMN `second_district_name` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL DEFAULT NULL COMMENT '二级区域名称' AFTER `second_district_id`;

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

ALTER TABLE `dili_ia`.`assets_lease_order_item`
DROP COLUMN `district_id`,
DROP COLUMN `district_name`;

-- 表管理
CREATE TABLE `meter` (
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
CREATE TABLE `customer_meter` (
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
