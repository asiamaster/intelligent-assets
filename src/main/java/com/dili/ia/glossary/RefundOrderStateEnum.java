package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-02 21:00
 */
public enum RefundOrderStateEnum {
    CREATED(1, "已创建"),
    CANCELD(2, "已取消"),
    SUBMITTED(3, "已提交"),
    REFUNDED(4, "已退款"),
    ;

    private String name;
    private Integer code ;

    RefundOrderStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static RefundOrderStateEnum getRefundOrderStateEnum(Integer code) {
        for (RefundOrderStateEnum anEnum : RefundOrderStateEnum.values()) {
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
