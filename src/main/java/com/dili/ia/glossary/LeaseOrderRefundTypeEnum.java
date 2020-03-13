package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2020-02-17 18:51
 */
public enum LeaseOrderRefundTypeEnum {
    LEASE_ORDER_REFUND(1, "租赁订单退款"),
    LEASE_ORDER_ITEM_REFUND(2, "租赁订单项退款"),
    ;

    private String name;
    private Integer code ;

    LeaseOrderRefundTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static LeaseOrderRefundTypeEnum getBizTypeEnum(Integer code) {
        for (LeaseOrderRefundTypeEnum anEnum : LeaseOrderRefundTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
