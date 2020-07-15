package com.dili.ia.glossary;

/**
 * @author:      xiaosa
 * @date:        2020/7/13
 * @version:     农批业务系统重构
 * @description: 精品停车状态枚举
 */
public enum BoutiqueOrderStateEnum {
    SUBMIT(1,"已提交"),
    PAY(2,"已交费"),
    CANCEL(3,"已取消"),
    INVALID(4,"已作废"),
    REFUND(5,"已退款"),
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
