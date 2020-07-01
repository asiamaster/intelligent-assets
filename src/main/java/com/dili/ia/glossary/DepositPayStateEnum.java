package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jiangchengyong
 * @createTime 2020/02/10 18:43
 */
public enum DepositPayStateEnum {

    UNPAID(1, "未交费"),
    NOT_PAID(2, "未交清"),
    PAID(3, "已交清"),
    ;

    private String name;
    private Integer code ;

    DepositPayStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static DepositPayStateEnum getPayStateEnum(Integer code) {
        for (DepositPayStateEnum anEnum : DepositPayStateEnum.values()) {
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
