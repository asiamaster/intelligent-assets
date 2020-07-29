package com.dili.ia.glossary;

/**
 * @author:      xiaosa
 * @date:        2020/7/13
 * @version:     农批业务系统重构
 * @description: 精品停车状态枚举
 */
public enum PassportPeriodEnum {
    ONE_MONTH(1,"一月"),
    TWO_MONTH(2,"两月"),
    QUARTERLY(3,"一季度"),
    HALF_YEAR(4,"半年"),
    ONE_YEAR(5,"一年"),
    OHTER(6,"其他")
    ;

    private String name;
    private Integer code ;

    PassportPeriodEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static PassportPeriodEnum getPassportPeriodlEnum(Integer code) {
        for (PassportPeriodEnum anEnum : PassportPeriodEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getPassportPeriodlEnumName(Integer code) {
        for (PassportPeriodEnum r : PassportPeriodEnum.values()) {
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
