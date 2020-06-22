package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-02-11 16:31
 */
public enum DepositOrderStateEnum {
    CREATED(1, "已创建"),
    CANCELD(2, "已取消"),
    SUBMITTED(3, "已提交"),
    NOT_PAID(4, "未交清"),
    PAID(5, "已交清"),
    REFUND(6, "已退款"),
    ;

    private String name;
    private Integer code ;

    DepositOrderStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static DepositOrderStateEnum getDepositOrderStateEnum(Integer code) {
        for (DepositOrderStateEnum anEnum : DepositOrderStateEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getDepositOrderStateEnumName(Integer code) {
        for (DepositOrderStateEnum r : DepositOrderStateEnum.values()) {
            if (r.getCode().equals(code)) {
                return r.getName();
            }
        }
        return "";
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
