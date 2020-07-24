package com.dili.ia.glossary;

/**
 * @author:      xiaosa
 * @date:        2020/7/13
 * @version:     农批业务系统重构
 * @description: 精品停车状态枚举
 */
public enum BoutiqueOrderStateEnum {
    CREATED(1, "已创建"),
    CANCELLED(2, "已取消"),
    SUBMITTED_PAY(3, "已提交"),
    PAID(4, "已缴费"),
    SUBMITTED_REFUND(5, "退款中"),
    REFUNDED(6, "已退款"),
    ;

    private String name;
    private Integer code ;

    BoutiqueOrderStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static BoutiqueOrderStateEnum getBoutiqueOrderStateEnum(Integer code) {
        for (BoutiqueOrderStateEnum anEnum : BoutiqueOrderStateEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getBoutiqueOrderStateEnumName(Integer code) {
        for (BoutiqueOrderStateEnum r : BoutiqueOrderStateEnum.values()) {
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
