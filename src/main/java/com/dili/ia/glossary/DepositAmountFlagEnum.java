package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-02-17 18:51
 */
public enum DepositAmountFlagEnum {
    PRE_TRANSFER(1, "预转入"),
    TRANSFERRED(2, "已转入"),
    FROZEN(3, "已冻结"),
    TRANSFERRED_OUT(4, "已转出"),
    ;

    private String name;
    private Integer code ;

    DepositAmountFlagEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static DepositAmountFlagEnum getEnabledState(Integer code) {
        for (DepositAmountFlagEnum anEnum : DepositAmountFlagEnum.values()) {
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
