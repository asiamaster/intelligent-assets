package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2020-02-17 18:51
 */
public enum PrintTemplateEnum {
    BOOTH_LEASE_NOT_PAID("BoothLeaseNotPaid", "摊位租赁未交清"),
    LEASE_SETTLEMENT_PAY_BILL("LeaseSettlementPayBill", "租赁结算交费票据"),
    LEASE_SETTLEMENT_REFUND_BILL("LeaseSettlementRefundBill", "租赁结算退款票据"),
    EARNEST_ORDER("EarnestOrder", "定金单"),
    EARNEST_REFUND_ORDER("EarnestRefundOrder", "定金退款单"),
    DEPOSIT_ORDER("DepositOrder", "保证金单"),
    DEPOSIT_REFUND_ORDER("DepositRefundOrder", "保证金退款单"),
    STOCKIN_ORDER("stockIn_pay", "冷库入库缴费凭据"),
    STOCK_OUT_ORDER("stockout", "出库单"),
    STOCKIN_REFUND ("stockIn_refund", "冷库退款"),
    WATER_PAY("water_pay", "水费交费"),
    ELECTRICITY_PAY("electricity_pay", "电费交费"),
    BOUTIQUE_PAY("boutique_pay", "精品停车交费"),
    BOUTIQUE_REFUND("boutique_refund", "精品停车退款"),
    PASSPORT_PAY("passport_pay", "通行证交费"),
    PASSPORT_REFUND("passport_refund", "通行证退款"),
    PASSPORT_PRINT("passport_print", "通行证打印证件"),
    LABOR_VEST_PAY("laborVest_pay","劳务马甲收款单"),
    LABOR_VEST_REFUND("laborVest_refund","劳务马甲退款单"),
    MESSAGEFEE_PAY("messageFee_pay","信息费收款单"),
    MESSAGEFEE_REFUND("messageFee_refund","信息费退款单"),
    OTHER_FEE_PAY("otherFee_pay","其他收费交费"),
    OTHER_FEE_REFUND("otherFee_refund","其他收费退款"),
    LEASE_CONTRACT_SIGNING_BILL("LeaseContractSigningBill", "租赁合同签订单"),
    LEASE_PAYMENT_BILL("LeasePaymentBill", "租赁交款单"),
    ;

    private String name;
    private String code ;

    PrintTemplateEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static PrintTemplateEnum getPrintTemplateEnum(String code) {
        for (PrintTemplateEnum anEnum : PrintTemplateEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
