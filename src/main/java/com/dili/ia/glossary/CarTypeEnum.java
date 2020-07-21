package com.dili.ia.glossary;

/**
 * @author:      xiaosa
 * @date:        2020/7/13
 * @version:     农批业务系统重构
 * @description: 精品停车 车型 枚举
 */
public enum CarTypeEnum {
    TRAILER(1,"挂车"),
    CONTAINER_TRUCK(2,"柜车"),
    ;

    private String name;
    private Integer code ;

    CarTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static CarTypeEnum getCarTypeEnum(Integer code) {
        for (CarTypeEnum anEnum : CarTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getCarTypeEnumName(Integer code) {
        for (CarTypeEnum r : CarTypeEnum.values()) {
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
