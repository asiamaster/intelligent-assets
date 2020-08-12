package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2020-02-17 18:51
 */
public enum BizNumberTypeEnum {
    //编号规则 BizTypeEnum.enName + '_' + BizNumberTypeEnum.code
    LEASE_ORDER("leaseOrder", "租赁单"),
    //编号规则 BizTypeEnum.enName + '_' + BizNumberTypeEnum.code
    PAYMENT_ORDER("paymentOrder", "缴费单"),
    //编号规则 BizTypeEnum.enName + '_' + BizNumberTypeEnum.code
    REFUND_ORDER("refundOrder", "退款单"),

    EARNEST_ORDER("earnestOrder", "定金业务单"),
    EARNEST_REFUND_ORDER("earnesRefundtOrder", "定金退款单"),
    EARNEST_TRANSFER_ORDER("earnestTransferOrder", "定金转移单"),
    TRANSACTION_CODE("transactionCode", "客户账户流水号"),
    STOCK_IN_CODE("stockInCode", "入库单号"),
    STOCK_IN_DETAIL_CODE("stockInDetail", "入库详情单号"),
    WATER_ELECTRICITY_CODE("waterElectricity", "水电费单"),
    DEPOSIT_ORDER("depositOrder", "保证金业务单"),
    DEPOSIT_REFUND_ORDER("depositRefundOrder", "保证金退款单"),
    LABOR_VEST("laborVest", "劳务马甲单"),
    VEST_RL("vest_rl", "马甲号人力车"),
    VEST_DD("vest_dd", "马甲号电动车"),
    VEST_JZ("vest_jz", "马甲号精品业户电动车及人力车"),
    VEST_GX("vest_gx", "马甲号干果业户小型电动车及人力车"),
    VEST_GD("vest_gd", "马甲号干果业户大型电动车"),

    BOUTIQUE_ENTRANCE("boutique", "精品停车单"),
    PASSPORT("passport", "通行证"),
    WATER_CODE("water", "水费单"),
    ELECTRICITY_CODE("electricity", "电费单"),
    // 通行证的证件类型,后续的小写补全直接是代码获取字段值
    PASSPORT_LICENSE_CODE("passport_", "通行证证件营运编号"),
    ;

    private String name;
    private String code ;

    BizNumberTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static BizNumberTypeEnum getBizNumberTypeEnum(String code) {
        for (BizNumberTypeEnum anEnum : BizNumberTypeEnum.values()) {
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
