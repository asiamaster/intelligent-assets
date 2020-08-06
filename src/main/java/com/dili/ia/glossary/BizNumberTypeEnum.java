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
    LEASE_ORDER("leaseOrder", "摊位租赁"),
    PAYMENT_ORDER("paymentOrder", "缴费单"),
    REFUND_ORDER("refundOrder", "退款单"),
    EARNEST_ORDER("earnestOrder", "定金业务单"),
    EARNEST_REFUND_ORDER("earnesRefundtOrder", "定金退款单"),
    LEASE_REFUND_ORDER("leaseRefundOrder", "租赁退款单"),
    EARNEST_TRANSFER_ORDER("earnestTransferOrder", "定金转移单"),
    TRANSACTION_CODE("transactionCode", "客户账户流水号"),
    STOCK_IN_CODE("stockInCode", "入库单号"),
    STOCK_IN_DETAIL_CODE("stockInDetail", "入库详情单号"),
    WATER_ELECTRICITY_CODE("waterElectricity", "水电费单"),
    DEPOSIT_ORDER("depositOrder", "保证金业务单"),
    DEPOSIT_REFUND_ORDER("depositRefund", "保证金退款单"),
    BOUTIQUE_ENTRANCE("boutiqueEntrance", "精品停车单"),
    BOUTIQUE_ORDER_REFUND("boutiqueRefundOrder", "精品停车退款单"),
    LABOR_VEST("laborVest", "劳务马甲单"),
    VEST_RL("vest_rl", "马甲号人力车"),
    VEST_DD("vest_dd", "马甲号电动车"),
    VEST_JZ("vest_jz", "马甲号精品业户电动车及人力车"),
    VEST_GX("vest_gx", "马甲号干果业户小型电动车及人力车"),
    VEST_GD("vest_gd", "马甲号干果业户大型电动车"),
    BOUTIQUE_ENTRANCE_REFUND("boutiqueEntranceRefundOrder", "精品停车单"),
    PASSPORT("passport", "通行证"),
    PASSPORT_REFUND("passportRefundOrder", "通行证退款单"),
    WATER_CODE("water", "水费单"),
    ELECTRICITY_CODE("electricity", "电费单"),
    // 下面几个都是在通行证缴费成功回调时自动生成
//    PASSPORT_YY_CODE("passport_yy", "通行证证件营运编号"),
//    PASSPORT_HX_CODE("passport_hx", "通行证证件海鲜编号"),
//    PASSPORT_ZY_CODE("passport_zy", "通行证证件自用编号"),
//    PASSPORT_WS_CODE("passport_ws", "通行证证件外市编号"),
//    PASSPORT_CS_CODE("passport_cs", "通行证证件超市编号"),
//    PASSPORT_YZ_CODE("passport_yz", "通行证证件业主编号"),

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
