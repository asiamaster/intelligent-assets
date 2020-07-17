package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jiangchengyong
 * @createTime 2020/02/10 18:43
 */
public enum LeaseRefundStateEnum {

    WAIT_APPLY(1, "未退款"),
    REFUNDING(2, "退款中"),
    REFUNDED(3, "已退款"),
    PARTIAL_REFUND(4, "部分退款"),
    ;

    private String name;
    private Integer code ;

    LeaseRefundStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static LeaseRefundStateEnum getRefundStateEnum(Integer code) {
        for (LeaseRefundStateEnum anEnum : LeaseRefundStateEnum.values()) {
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
