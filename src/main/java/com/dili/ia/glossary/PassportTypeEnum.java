package com.dili.ia.glossary;

/**
 * @author:      xiaosa
 * @date:        2020/7/13
 * @version:     农批业务系统重构
 * @description: 精品停车状态枚举
 */
public enum PassportTypeEnum {
    OPERATING_VEICLES(1,"营运车辆通行证"),
    SEAFOOD_LINE(2,"海鲜专线通行证"),
    SELF_USE(3,"自用通行证"),
    OTHER_CITY_CAR(4,"外市县上货车辆"),
    SUPERMARKET_CAR(5,"超市上货车辆"),
    OWNER_OUT_CAR(6,"业主出场上货车辆")
    ;

    private String name;
    private Integer code ;

    PassportTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static PassportTypeEnum getPassportTypeEnum(Integer code) {
        for (PassportTypeEnum anEnum : PassportTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getPassportTypeEnumName(Integer code) {
        for (PassportTypeEnum r : PassportTypeEnum.values()) {
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
