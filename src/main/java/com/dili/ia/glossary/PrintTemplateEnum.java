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
    BOOTH_LEASE_PAID("BoothLeasePaid", "摊位租赁已交清"),
    BOOTH_LEASE_REFUND_NOT_PAID("BoothLeaseRefundNotPaid", "摊位租赁退款未交清"),
    BOOTH_LEASE_REFUND_PAID("BoothLeaseRefundPaid", "摊位租赁退款已交清"),
    EARNEST_ORDER("EarnestOrder", "定金单"),
    EARNEST_REFUND_ORDER("EarnestRefundOrder", "定金退款单"),
    DEPOSIT_ORDER("DepositOrder", "保证金单"),
    DEPOSIT_REFUND_ORDER("DepositRefundOrder", "保证金退款单"),
    STOCKIN_ORDER("StockInOrder", "入库单"),
    STOCK_OUT_ORDER("StockOutInOrder", "出库单"),
    WATER_FEE("waterFee", "水费"),
    ELECTRICITY_FEE("electricityFee", "电费"),
    BOUTIQUE_ENTRANCE("boutique_entrance", "精品停车费"),
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
