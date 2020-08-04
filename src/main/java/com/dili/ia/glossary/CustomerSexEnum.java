package com.dili.ia.glossary;

/**
 * @author:      xiaosa
 * @date:        2020/7/13
 * @version:     农批业务系统重构
 * @description: 精品停车 车型 枚举
 */
public enum CustomerSexEnum {
    MAN(1,"男"),
    WOMEN(2,"女"),
    ;

    private String name;
    private Integer code ;

    CustomerSexEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static CustomerSexEnum getCustomerSexEnum(Integer code) {
        for (CustomerSexEnum anEnum : CustomerSexEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getCustomerSexEnumName(Integer code) {
        for (CustomerSexEnum r : CustomerSexEnum.values()) {
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
