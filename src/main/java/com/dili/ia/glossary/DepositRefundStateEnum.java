package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jiangchengyong
 * @createTime 2020/02/10 18:43
 */
public enum DepositRefundStateEnum {

    NO_REFUNDED(1, "未退款"),
    PART_REFUND(2, "部分退款"),
    REFUNDED(3, "全额退款"),
    ;

    private String name;
    private Integer code ;

    DepositRefundStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static DepositRefundStateEnum getDepositRefundStateEnum(Integer code) {
        for (DepositRefundStateEnum anEnum : DepositRefundStateEnum.values()) {
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
