/**业务【定金余额】数据迁移到结算【定金余额】**/
INSERT INTO dili_settlement.customer_account
(`id`,`market_id` ,`mch_id`,`customer_id`,`customer_name`,`customer_phone`,`customer_certificate`,`amount`,`frozen_amount`,`version`)
(SELECT `id`,`market_id`,`market_id`,`customer_id`,`customer_name`,`customer_cellphone`,`certificate_number`,`earnest_balance`,`earnest_frozen_amount`,`version` FROM dili_ia.customer_account);

-- 缴费单抵扣数据处理
UPDATE `dili_ia`.payment_order po,
`dili_ia`.assets_lease_order alo
SET po.amount = po.amount + alo.earnest_deduction + alo.transfer_deduction
WHERE
	po.business_id = alo.id
	AND po.id IN (
	SELECT
		result.id
	FROM
		(
		SELECT
			MIN( po.id ) AS id
		FROM
			``dili_ia`.assets_lease_order` alo
			INNER JOIN `dili_ia`.payment_order po ON alo.id = po.business_id
		WHERE
			alo.assets_type = 1
			AND po.state != 3
			AND ( alo.earnest_deduction > 0 OR alo.transfer_deduction > 0 )
		GROUP BY
			alo.id
		) result
	);

-- 租赁单抵扣数据处理
UPDATE `dili_ia`.assets_lease_order
SET total_amount = total_amount + earnest_deduction + transfer_deduction
WHERE
	assets_type = 1
	AND ( earnest_deduction > 0 OR transfer_deduction > 0 );