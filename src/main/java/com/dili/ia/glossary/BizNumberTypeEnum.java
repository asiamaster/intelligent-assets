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
