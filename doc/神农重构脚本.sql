--水电费
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-水费业务单', 'hzsc_water', 'HZSCSF', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_water', 2020081200001, '杭州水产-水费业务单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-电费业务单', 'hzsc_electricity', 'HZSCDF', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_electricity', 2020081200001, '杭州水产-电费业务单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-水费缴费单', 'hzsc_water_paymentOrder', 'HZSCSF', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_water_paymentOrder', 2020081200001, '杭州水产-水费缴费单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-电费缴费单', 'hzsc_electricity_paymentOrder', 'HZSCDF', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_electricity_paymentOrder', 2020081200001, '杭州水产-电费费缴费单', '1');

--精品黄楼
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-精品停车业务单', 'hzsc_boutique', 'HZSCJPTC', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_boutique', 2020081200001, '杭州水产-精品停车业务单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-精品停车缴费单', 'hzsc_boutique_paymentOrder', 'HZSCJPTC', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_boutique_paymentOrder', 2020081200001, '杭州水产-精品停车缴费单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-精品停车退款单', 'hzsc_boutique_refundOrder', 'HZSCTKJPTC', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_boutique_refundOrder', 2020081200001, '杭州水产-精品停车退款单', '1');

--通行证
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-通行证业务单', 'hzsc_passport', 'HZSCTXZ', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_passport', 2020081200001, '杭州水产-通行证业务单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-通行证缴费单', 'hzsc_passport_paymentOrder', 'HZSCTXZ', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_passport_paymentOrder', 2020081200001, '杭州水产-通行证缴费单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-通行证退款单', 'hzsc_passport_refundOrder', 'HZSCTKTXZ', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_passport_refundOrder', 2020081200001, '杭州水产-通行证退款单', '1');

--其他收费
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-其他收费业务单', 'hzsc_otherFee', 'HZSCQT', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_otherFee', 2020081200001, '杭州水产-其他收费业务单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-其他收费缴费单', 'hzsc_otherFee_paymentOrder', 'HZSCQT', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_otherFee_paymentOrder', 2020081200001, '杭州水产-其他收费缴费单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-其他收费退款单', 'hzsc_otherFee_refundOrder', 'HZSCTKQT', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_otherFee_refundOrder', 2020081200001, '杭州水产-其他收费退款单', '1');

--通行证类型
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-通行证证件类型业主', 'hzsc_passport_yz', 'YZ', 'yy', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_passport_yz', 2020081200001, '杭州水产-通行证证件类型业主', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-通行证证件类型超市', 'hzsc_passport_cs', 'CS', 'yy', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_passport_cs', 2020081200001, '杭州水产-通行证证件类型超市', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-通行证证件类型外市', 'hzsc_passport_ws', 'WS', 'yy', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_passport_ws', 2020081200001, '杭州水产-通行证证件类型外市', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-通行证证件类型自用', 'hzsc_passport_zy', 'ZY', 'yy', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_passport_zy', 2020081200001, '杭州水产-通行证证件类型自用', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-通行证证件类型海鲜', 'hzsc_passport_hx', 'HX', 'yy', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_passport_hx', 2020081200001, '杭州水产-通行证证件类型海鲜', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-通行证证件类型营运', 'hzsc_passport_yy', 'YY', 'yy', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_passport_yy', 2020081200001, '杭州水产-通行证证件类型营运', '1');


INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-冷库入库单', 'hzsc_stockInCode', 'HZSCSI', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_stockInCode', 2020081200001, '杭州水产-冷库入库单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-冷库入库子单', 'hzsc_stockInDetail', 'HZSCSID', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_stockInDetail', 2020081200001, '杭州水产-冷库入库子单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-冷库出库单', 'hzsc_stockOut', 'HZSCSO', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_stockOut', 2020081200001, '杭州水产-冷库出库单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-劳务马甲单', 'hzsc_laborVest', 'HSSCMJ', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_laborVest', 2020081200001, '杭州水产-劳务马甲单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-劳务人力马甲号', 'hzsc_vest_rl', 'HZSCRL', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_vest_rl', 2020081200001, '杭州水产-劳务人力马甲号', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-劳务电动马甲号', 'hzsc_vest_dd', 'HZSCDD', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_vest_dd', 2020081200001, '杭州水产-劳务电动马甲号', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-劳务精品业户电动车及人力车马甲号', 'hzsc_vest_jz', 'HZSCJZ', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_vest_jz', 2020081200001, '杭州水产-劳务精品业户电动车及人力车马甲号', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-劳务干果业户小型电动车及人力车马甲号', 'hzsc_vest_gx', 'HZSCGX', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_vest_gx', 2020081200001, '杭州水产-劳务干果业户小型电动车及人力车马甲号', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-劳务干果业户大型电动车马甲号', 'hzsc_vest_gd', 'HZSCGD', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_vest_gd', 2020081200001, '杭州水产-劳务干果业户大型电动车马甲号', '1');


INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-冷库缴费单', 'hzsc_stockIn_paymentOrder', 'HZSCPOSI', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_stockIn_paymentOrder', 2020081200001, '杭州水产-冷库缴费单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-劳务马甲缴费单', 'hzsc_laborVest_paymentOrder', 'HZSCPOMJ', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_laborVest_paymentOrder', 2020081200001, '杭州水产-劳务马甲缴费单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-信息费缴费单', 'hzsc_messageFee_paymentOrder', 'HZSCPOXXF', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_messageFee_paymentOrder', 2020081200001, '杭州水产-信息费缴费单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-冷库退款单', 'hzsc_stockIn_refundOrder', 'HZSCTKSI', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_stockIn_refundOrder', 2020081200001, '杭州水产-冷库退款单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-劳务马甲退款单', 'hzsc_laborVest_refundOrder', 'HZSCTKMJ', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_laborVest_refundOrder', 2020081200001, '杭州水产-劳务马甲退款单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-信息费退款单', 'hzsc_messageFee_refundOrder', 'HZSCTKMJ', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_messageFee_refundOrder', 2020081200001, '杭州水产-信息费退款单', '1');

INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-信息费单', 'hzsc_messageFee', 'HZSCXXF', 'yyyyMMdd', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_messageFee', 2020081200001, '杭州水产-信息费单', '1');



-- 摊位出租预设表添加三个字段
  ALTER TABLE `dili_ia`.`assets_rental_item`
   ADD COLUMN `number` decimal(32,2) DEFAULT NULL COMMENT '数量',
   ADD COLUMN `unit` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci DEFAULT NULL COMMENT '单位',
   ADD COLUMN `corner` tinyint(4) DEFAULT NULL COMMENT '是否转角';

-- 精品黄楼停车冗余车型名称
  ALTER TABLE `dili_ia`.`boutique_entrance_record`
   ADD COLUMN `car_type_name` varchar(30) CHARACTER SET utf8 COLLATE utf8_general_ci DEFAULT NULL COMMENT '车型名称';

-- 摊位出租预设修改批次字段类型
ALTER TABLE `dili_ia`.`assets_rental` MODIFY COLUMN `batch_id` varchar(30) NULL DEFAULT NULL COMMENT '一个批次的批次号' AFTER `id`;

-- 摊位出租预设批次号
INSERT INTO `biz_number_rule` ( `name`, `type`, `prefix`, `date_format`, `length`, `range`) VALUES ('杭州水产-摊位出租预设批次号', 'hzsc_assetsRental', 'HZSCTWYS', 'yyyyMM', 4, '1');
INSERT INTO `biz_number` (`type`, `value`, `memo`, `version`) VALUES ('hzsc_assetsRental', 2020081200001, '杭州水产-摊位出租预设批次号', '1');

-- 冷库入库添加城市中文名
ALTER TABLE `dili_ia`.`stock_in` 
ADD COLUMN `origin_path` varchar(100) NULL COMMENT '城市名称' AFTER `origin`;


