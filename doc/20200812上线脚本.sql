--  客户模块 ------------------------------------------------

--客户市场表中,增加客户等级,客户行业等相关信息
ALTER TABLE customer_market ADD COLUMN `grade` TINYINT DEFAULT NULL COMMENT '客户等级' AFTER owner_id;
ALTER TABLE customer_market ADD COLUMN `profession` VARCHAR ( 32 ) DEFAULT NULL COMMENT '客户行业' AFTER grade;
ALTER TABLE customer_market ADD COLUMN `operating_area` VARCHAR ( 40 ) DEFAULT NULL COMMENT '经营地区' AFTER profession;
ALTER TABLE customer_market ADD COLUMN `operating_lng` VARCHAR ( 20 ) DEFAULT NULL COMMENT '经营地区经度' AFTER operating_area;
ALTER TABLE customer_market ADD COLUMN `operating_lat` VARCHAR ( 20 ) DEFAULT NULL COMMENT '经营地区纬度' AFTER operating_lng;
ALTER TABLE customer_market ADD COLUMN `other_title` VARCHAR ( 40 ) DEFAULT NULL COMMENT '其它头衔' AFTER operating_lat;
ALTER TABLE customer_market ADD COLUMN `main_category` VARCHAR ( 40 ) DEFAULT NULL COMMENT '主营品类' AFTER other_title;
--更新客户市场表的数据
UPDATE customer_market cm,
customer c
SET cm.type='inside_buyer',
cm.grade = c.grade,
cm.profession = c.profession,
cm.operating_area = c.operating_area,
cm.operating_lng = c.operating_lng,
cm.operating_lat = c.operating_lat,
cm.other_title = c.other_title,
cm.main_category = c.main_category
WHERE
	cm.customer_id = c.id;

--删除客户主表上的 客户等级,客户行业等信息
ALTER TABLE customer drop COLUMN `grade`;
ALTER TABLE customer drop COLUMN `profession`;
ALTER TABLE customer drop COLUMN `operating_area`;
ALTER TABLE customer drop COLUMN `operating_lng`;
ALTER TABLE customer drop COLUMN `operating_lat`;
ALTER TABLE customer drop COLUMN `other_title`;
ALTER TABLE customer drop COLUMN `main_category`;

--数据字典配置
INSERT INTO `uap`.`data_dictionary`(`code`, `name`, `system_code`, `description`, `created`, `modified`) VALUES ('cus_customer_type', '客户身份类型信息', 'CUSTOMER', '客户身份类型信息', now(), now());
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`) VALUES ('cus_customer_type', 1, '园内买家', 'inside_buyer', NULL, now(), now());
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`) VALUES ('cus_customer_type', 2, '园内卖家', 'inside_seller', NULL, now(), now());
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`) VALUES ('cus_customer_type', 3, '买卖家', 'purchaseSale', NULL, now(), now());
INSERT INTO `uap`.`data_dictionary_value`(`dd_code`, `order_number`, `name`, `code`, `description`, `created`, `modified`) VALUES ('cus_customer_type', 4, '司机', 'driver', NULL, now(), now());



--  租赁模块 ------------------------------------------------

-- 租赁单和退款单新增流程实例和定义id
ALTER TABLE `assets_lease_order`
	ADD COLUMN `is_invoice` TINYINT NULL DEFAULT '0' COMMENT '是否开票 1:是 0：否' AFTER `version`,
	ADD COLUMN `process_instance_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '流程实例id' AFTER `is_invoice`,
	ADD COLUMN `process_definition_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '流程定义id' AFTER `process_instance_id`,
	ADD COLUMN `approval_state` TINYINT NULL DEFAULT NULL COMMENT '审批状态(1: 待审批 2: 审批中 3:审批通过 4:审批拒绝)' AFTER `process_definition_id`;
ALTER TABLE `refund_order`
	ADD COLUMN `process_instance_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '流程实例id' AFTER `version`,
	ADD COLUMN `process_definition_id` VARCHAR(64) NULL DEFAULT NULL COMMENT '流程定义id' AFTER `process_instance_id`,
    ADD COLUMN `approval_state` TINYINT NULL DEFAULT NULL COMMENT '审批状态(1: 待审批 2: 审批中 3:审批通过 4:审批拒绝)' AFTER `process_definition_id`;

-- 创建审批表
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


-- 任务人分配
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

-- 开票记录
CREATE TABLE `invoice_record` (
	`id` BIGINT(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
	`business_key` VARCHAR(64) NULL DEFAULT NULL COMMENT '业务号' COLLATE 'utf8mb4_general_ci',
	`type` TINYINT(4) NULL DEFAULT NULL COMMENT '开票类型, 1:普票， 2: 专票',
	`target` VARCHAR(64) NULL DEFAULT NULL COMMENT '开票主体' COLLATE 'utf8mb4_general_ci',
	`target_id` BIGINT(20) NULL DEFAULT NULL COMMENT '开票主体id',
	`amount` BIGINT(20) NULL DEFAULT NULL COMMENT '开票金额' COLLATE 'utf8mb4_general_ci',
	`total_amount` BIGINT(20) NULL DEFAULT NULL COMMENT '开票总金额' COLLATE 'utf8mb4_general_ci',
	`invoice_date` DATE NULL DEFAULT NULL COMMENT '开票日期',
	`creator_id` BIGINT(20) NULL DEFAULT NULL COMMENT '开票人',
	`creator` VARCHAR(20) NULL DEFAULT NULL COMMENT '开票人名称' COLLATE 'utf8mb4_general_ci',
	`notes` VARCHAR(50) NULL DEFAULT NULL COMMENT '备注' COLLATE 'utf8mb4_general_ci',
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

-- 删除基础数据表
DROP TABLE `dili-assets`.``category`;
DROP TABLE `dili-assets`.`district`;
DROP TABLE `dili-assets`.`booth`;
DROP TABLE `dili-assets`.`booth_rent`;


-- 报表模块 ------------------------------------------------
新建报表数据库: dili_report, utf8mb4
########################################
##### 时间维度表
########################################

CREATE TABLE `dim_time` (
  `id` bigint(20) NOT NULL COMMENT '主键, 以小时为最细粒度, %Y%m%d%H',
  `dt` datetime DEFAULT NULL COMMENT '日期时间, %Y-%m-%d %H:00:00',
  `date` char(10) DEFAULT NULL COMMENT '日期, %Y-%m-%d',
  `y` smallint(4) DEFAULT NULL COMMENT '年 (0-9999)',
  `m` smallint(2) DEFAULT NULL COMMENT '月 (1-12)',
  `d` smallint(2) DEFAULT NULL COMMENT '日 (1-31)',
  `h` smallint(2) DEFAULT NULL COMMENT '时 (1-24)',
  `q` smallint(1) DEFAULT NULL COMMENT '季度 (1-4)',
  `ym` char(7) DEFAULT NULL COMMENT '年月',
  `msd` char(10) DEFAULT NULL COMMENT '月的起始日期',
  `med` char(10) DEFAULT NULL COMMENT '月的截止日期',
  `yw` char(7) DEFAULT NULL COMMENT '年周, 年尾周的天数大于3天则计算到该年, 例如2015年, 相反小于等于3天则计算到下一年, 例如2014年。换言之，即看星期四在哪一年',
  `ywy` smallint(4) DEFAULT NULL COMMENT '年周所对应的年',
  `w` smallint(2) DEFAULT NULL COMMENT '周 (1-53)',
  `wd` smallint(1) DEFAULT NULL COMMENT '周的天数 (1-7), 即星期几',
  `wsd` char(10) DEFAULT NULL COMMENT '周的起始日期',
  `wed` char(10) DEFAULT NULL COMMENT '周的截止日期',
  `w_name` varchar(20) DEFAULT NULL COMMENT '周显示名称, 周数, 起始日期, 截止日期, 例如 W52(12.23-12.29), W1(12.30-1.5)',
  PRIMARY KEY (`id`),
  KEY `date` (`date`) USING BTREE,
  KEY `y` (`y`) USING BTREE,
  KEY `m` (`m`) USING BTREE,
  KEY `d` (`d`) USING BTREE,
  KEY `ym` (`ym`) USING BTREE,
  KEY `yw` (`yw`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='时间维度表';


/*
# init
SET @sdt = '2015-01-01 00:00:00';
-- SET @edt = '2015-01-01 23:59:59';
SET @edt = '2030-12-31 23:59:59';
SET @xdt = DATE_SUB(@sdt, INTERVAL 1 HOUR);

# do insert
INSERT INTO `dim_time` (`id`, `dt`, `date`, `y`, `m`, `d`, `h`, `q`, `ym`, `msd`, `med`, `yw`, `ywy`, `w`, `wd`, `wsd`, `wed`, `w_name`)
SELECT
  # integer ID that allowsimmediate understanding
  DATE_FORMAT(@xdt := DATE_ADD(@xdt, INTERVAL 1 HOUR), '%Y%m%d%H') AS 'id',
  DATE_FORMAT(@xdt, '%Y-%m-%d %H:00:00') AS 'dt',
  DATE_FORMAT(@xdt, '%Y-%m-%d') AS 'date',
  YEAR(@xdt) AS 'y',
  MONTH(@xdt) AS 'm',
  DAY(@xdt) AS 'd',
  HOUR(@xdt) AS 'h',
  QUARTER(@xdt) AS 'q',
  DATE_FORMAT(@xdt, '%Y-%m') AS 'ym',
  DATE_FORMAT(@xdt, '%Y-%m-01') AS 'msd',
  DATE_FORMAT(DATE_SUB(DATE_ADD(DATE_FORMAT(@xdt, '%Y-%m-01'), INTERVAL 1 MONTH), INTERVAL 1 DAY), '%Y-%m-%d') AS 'med',
  # 周 (1-53) 星期一是一周的第一天
  # 临界周的周四在哪一年即算作那一年
  DATE_FORMAT(@xdt, '%x-%v') AS 'yw',
  DATE_FORMAT(@xdt, '%x') AS 'ywy',
  WEEK(@xdt, 3) AS 'w',
  WEEKDAY(@xdt)+1 AS 'wd',
  DATE_FORMAT(DATE_SUB(@xdt, INTERVAL WEEKDAY(@xdt) DAY), '%Y-%m-%d') AS 'wsd',
  DATE_FORMAT(DATE_SUB(@xdt, INTERVAL WEEKDAY(@xdt)-6 DAY), '%Y-%m-%d') AS 'wed',
  CONCAT(
    'W',
    WEEK(@xdt, 3),
    '(',
    DATE_FORMAT(DATE_SUB(@xdt, INTERVAL WEEKDAY(@xdt) DAY), '%c.%e'),
    '-',
    DATE_FORMAT(DATE_SUB(@xdt, INTERVAL WEEKDAY(@xdt) - 6 DAY), '%c.%e'),
    ')'
  ) AS 'w_name'
FROM
# 使用多次全连接产生更多的数据
# 4^10 = 1048576‬
# 1048576‬ / (365 * 24) = 119.7
(SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1) AS t1,
(SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1) AS t2,
(SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1) AS t3,
(SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1) AS t4,
(SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1) AS t5,
(SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1) AS t6,
(SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1) AS t7,
(SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1) AS t8,
(SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1) AS t9,
(SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1 UNION ALL SELECT 1) AS t10
WHERE DATE_ADD(@xdt, INTERVAL 1 HOUR) <= @edt
ORDER BY dt;
*/


########################################
##### 摊位资产日统计
########################################

CREATE TABLE `stat_assets_booth_daily` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `time_key` char(10) NOT NULL COMMENT '统计日期',
  `stat_year` int(11) NOT NULL COMMENT '统计年度',
  `f_area_id` bigint(20) NOT NULL COMMENT '一级区域ID',
  `s_area_id` bigint(20) NOT NULL COMMENT '二级区域ID',
  `f_area_name` varchar(255) DEFAULT NULL COMMENT '一级区域名称',
  `s_area_name` varchar(255) DEFAULT NULL COMMENT '二级区域名称',
  `cnt_od` bigint(20) NOT NULL COMMENT '订单量，相当于客户的人次统计',
  `cnt_cust` bigint(20) NOT NULL COMMENT '去重以后的客户数',
  `bt_area_total` decimal(32,2) NOT NULL COMMENT '摊位总面积',
  `bt_area_used` decimal(32,2) NOT NULL COMMENT '摊位已使用面积',
  `bt_area_remain` decimal(32,2) NOT NULL COMMENT '摊位剩余面积',
  `bt_number_total` decimal(32,2) NOT NULL COMMENT '摊位总个数',
  `bt_number_used` decimal(32,2) NOT NULL COMMENT '摊位已使用个数',
  `bt_number_remain` decimal(32,2) NOT NULL COMMENT '摊位剩余个数',
  `rate_used` varchar(32) NOT NULL COMMENT '摊位剩余个数',
  `record_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录刷新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_key_date_year_d_cust_deli` (`time_key`, `stat_year`, `f_area_id`, `s_area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='摊位资产日统计表, T-1';




########################################
##### 摊位租赁统计
########################################

CREATE TABLE `stat_assets_booth_lease_annual` (
  `time_key` char(4) NOT NULL COMMENT '统计年度',
  `f_area_id` bigint(20) NOT NULL COMMENT '一级区域ID',
  `s_area_id` bigint(20) NOT NULL COMMENT '二级区域ID',
  `f_area_name` varchar(255) DEFAULT NULL COMMENT '一级区域名称',
  `s_area_name` varchar(255) DEFAULT NULL COMMENT '二级区域名称',
  `lease_1` bigint(20) DEFAULT NULL COMMENT '1月收租',
  `lease_2` bigint(20) DEFAULT NULL COMMENT '2月收租',
  `lease_3` bigint(20) DEFAULT NULL COMMENT '3月收租',
  `lease_4` bigint(20) DEFAULT NULL COMMENT '4月收租',
  `lease_5` bigint(20) DEFAULT NULL COMMENT '5月收租',
  `lease_6` bigint(20) DEFAULT NULL COMMENT '6月收租',
  `lease_7` bigint(20) DEFAULT NULL COMMENT '7月收租',
  `lease_8` bigint(20) DEFAULT NULL COMMENT '8月收租',
  `lease_9` bigint(20) DEFAULT NULL COMMENT '9月收租',
  `lease_10` bigint(20) DEFAULT NULL COMMENT '10月收租',
  `lease_11` bigint(20) DEFAULT NULL COMMENT '11月收租',
  `lease_12` bigint(20) DEFAULT NULL COMMENT '12月收租',
  `lease_year` bigint(20) DEFAULT NULL COMMENT '年收租',
  `refund_1` bigint(20) DEFAULT NULL COMMENT '1月退租',
  `refund_2` bigint(20) DEFAULT NULL COMMENT '2月退租',
  `refund_3` bigint(20) DEFAULT NULL COMMENT '3月退租',
  `refund_4` bigint(20) DEFAULT NULL COMMENT '4月退租',
  `refund_5` bigint(20) DEFAULT NULL COMMENT '5月退租',
  `refund_6` bigint(20) DEFAULT NULL COMMENT '6月退租',
  `refund_7` bigint(20) DEFAULT NULL COMMENT '7月退租',
  `refund_8` bigint(20) DEFAULT NULL COMMENT '8月退租',
  `refund_9` bigint(20) DEFAULT NULL COMMENT '9月退租',
  `refund_10` bigint(20) DEFAULT NULL COMMENT '10月退租',
  `refund_11` bigint(20) DEFAULT NULL COMMENT '11月退租',
  `refund_12` bigint(20) DEFAULT NULL COMMENT '12月退租',
  `refund_year` bigint(20) DEFAULT NULL COMMENT '年退租',
  `record_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录刷新时间',
  UNIQUE KEY `uni_key_date_d_cust_deli` (`time_key`, `f_area_id`, `s_area_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='摊位资产租赁年统计表';




########################################
##### 支付退款日统计
########################################

CREATE TABLE `stat_payment_refund_order_daily` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'id',
  `opt_date` char(10) NOT NULL COMMENT '操作日期，分别为支付日期和退款日期',
  `opt_dim` bigint(20) NOT NULL COMMENT '操作时间维度',
  `biz_type` varchar(255) DEFAULT NULL COMMENT '业务类型，参照数据字典，目前2-定金, 3-保证金, 1-摊位租赁，4-冷库租赁，5-公寓租赁',
  `settle_way` tinyint(4) DEFAULT NULL COMMENT '支付方式',
  `settle_way_name` varchar(255) DEFAULT NULL COMMENT '支付方式名称',
  `settle_type` tinyint(4) DEFAULT NULL COMMENT '结算单类型，1-交款，2-退款',
  `total_amount` bigint(20) DEFAULT NULL COMMENT '收款金额统计(分)',
  `record_update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '记录刷新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_key_date_biz_way_type` (`opt_date`, `biz_type`, `settle_way`, `settle_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='支付和退款订单日统计';





