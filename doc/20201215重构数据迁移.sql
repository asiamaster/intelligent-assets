/**业务【定金余额】数据迁移到结算【定金余额】**/
INSERT INTO dili_settlement.customer_account
(`id`,`market_id` ,`mch_id`,`customer_id`,`customer_name`,`customer_phone`,`customer_certificate`,`amount`,`frozen_amount`,`version`)
(SELECT `id`,`market_id`,`market_id`,`customer_id`,`customer_name`,`customer_cellphone`,`certificate_number`,`earnest_balance`,`earnest_frozen_amount`,`version` FROM dili_ia.customer_account);