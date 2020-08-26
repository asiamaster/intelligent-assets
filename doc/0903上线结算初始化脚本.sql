-- 初始化表数据,注意配置配置各字段值
INSERT INTO `fund_account` (`market_id`, `app_id`, `amount`, `version`) VALUES
	(1, 101, 0, 1),
	(2, 101, 0, 1),
	(3, 101, 0, 1),
	(4, 101, 0, 1),
	(5, 101, 0, 1),
	(6, 101, 0, 1),
	(7, 101, 0, 1),
	(8, 101, 0, 1),
	(9, 101, 0, 1),
	(10, 101, 0, 1),
	(11, 101, 0, 1),
	(12, 101, 0, 1);

INSERT INTO `settle_config` (`group_code`, `code`, `val`, `state`, `notes`) VALUES
	(101, 1, '现金', 1, '初始数据(101支付方式)'),
	(101, 2, 'POS', 1, '初始数据(101支付方式)'),
	(101, 3, '银行卡转账', 1, '初始数据(101支付方式)'),
	(101, 4, '支付宝', 1, '初始数据(101支付方式)'),
	(101, 5, '微信', 1, '初始数据(101支付方式)'),
	(102, 1, '现金', 1, '初始数据(102退款方式)'),
	(102, 3, '银行卡转账', 1, '初始数据(102退款方式)');

INSERT INTO `application_config` (`app_id`, `group_code`, `code`, `val`, `state`, `notes`) VALUES
	(101, 101, 1, '摊位租赁', 1, 'group_code(101)表示业务类型'),
	(101, 101, 2, '定金', 1, 'group_code(101)表示业务类型'),
	(101, 102, 1, 'https://ia.diligrp.com/leaseOrder/view.action', 1, 'group_code(102)表示缴费业务详情URL'),
	(101, 102, 2, 'https://ia.diligrp.com/earnestOrder/view.action', 1, 'group_code(102)表示缴费业务详情URL'),
	(101, 103, 1, 'https://ia.diligrp.com/refundOrder/view.action', 1, 'group_code(103)表示退款业务详情URL'),
	(101, 103, 2, 'https://ia.diligrp.com/refundOrder/view.action', 1, 'group_code(103)表示退款业务详情URL'),
	(101, 104, 1, 'https://ia.diligrp.com/api/leaseOrder/queryPrintData', 1, 'group_code(104)表示缴费打印数据URL'),
	(101, 104, 2, 'https://ia.diligrp.com/api/earnestOrder/queryPrintData', 1, 'group_code(104)表示缴费打印数据URL'),
	(101, 105, 1, 'https://ia.diligrp.com/api/refundOrder/queryPrintData', 1, 'group_code(105)表示退款打印数据URL'),
	(101, 105, 2, 'https://ia.diligrp.com/api/refundOrder/queryPrintData', 1, 'group_code(105)表示退款打印数据URL'),
	(101, 201, 1, 'qaz@wsx', 1, 'group_code(201)表示回调签名KEY');

-- v1.1.0版本新增组合方式脚本
INSERT INTO `settle_config` (`group_code`, `code`, `val`, `state`, `notes`) VALUES
	(101, 6, '组合支付', 1, '初始数据(101支付方式)'),
	(101, 7, '虚拟支付', 1, '初始数据(101支付方式)');

-- 增加保证金业务类型相关脚本
INSERT INTO `application_config` (`app_id`, `group_code`, `code`, `val`, `state`, `notes`) VALUES
	(101, 101, 3, '保证金', 1, 'group_code(101)表示业务类型'),
	(101, 102, 3, 'https://ia.diligrp.com/depositOrder/view.action', 1, 'group_code(102)表示缴费业务详情URL'),
	(101, 103, 3, 'https://ia.diligrp.com/refundOrder/view.action', 1, 'group_code(103)表示退款业务详情URL'),
	(101, 104, 3, 'https://ia.diligrp.com/api/depositOrder/queryPrintData', 1, 'group_code(104)表示缴费打印数据URL'),
	(101, 105, 3, 'https://ia.diligrp.com/api/refundOrder/queryPrintData', 1, 'group_code(105)表示退款打印数据URL');