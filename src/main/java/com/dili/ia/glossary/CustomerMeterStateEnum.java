package com.dili.ia.glossary;

/**
 * @author:      xiaosa
 * @date:        2020/6/16
 * @version:     农批业务系统重构
 * @description: 表状态枚举
 */
public enum CustomerMeterStateEnum {
    CREATED(1, "已绑定"),
    CANCELD(2, "已解绑"),
    ;

    private String name;
    private Integer code ;

    CustomerMeterStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static CustomerMeterStateEnum getEarnestOrderStateEnum(Integer code) {
        for (CustomerMeterStateEnum anEnum : CustomerMeterStateEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getEarnestOrderStateEnumName(Integer code) {
        for (CustomerMeterStateEnum r : CustomerMeterStateEnum.values()) {
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
