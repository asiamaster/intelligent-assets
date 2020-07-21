package com.dili.ia.glossary;

/**
 * @author:      xiaosa
 * @date:        2020/7/13
 * @version:     农批业务系统重构
 * @description: 精品停车 接车部门 枚举
 */
public enum CarDepartmentEnum {
    HOUSE(1,"精品黄楼"),
    HOUSE_NO_STALL(2,"精品黄楼无档口"),
    ;

    private String name;
    private Integer code ;

    CarDepartmentEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static CarDepartmentEnum getCarTypeEnum(Integer code) {
        for (CarDepartmentEnum anEnum : CarDepartmentEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getCarTypeEnumName(Integer code) {
        for (CarDepartmentEnum r : CarDepartmentEnum.values()) {
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
