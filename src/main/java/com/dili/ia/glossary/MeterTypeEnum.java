package com.dili.ia.glossary;

/**
 * @author:      xiaosa
 * @date:        2020/6/16
 * @version:     农批业务系统重构
 * @description: 表类别枚举
 */
public enum MeterTypeEnum {
    CREATED(1, "水表"),
    CANCELD(2, "电表"),
    ;

    private String name;
    private Integer code ;

    MeterTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static MeterTypeEnum getMeterTypeEnum(Integer code) {
        for (MeterTypeEnum anEnum : MeterTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getMeterTypeEnumName(Integer code) {
        for (MeterTypeEnum r : MeterTypeEnum.values()) {
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
