package com.dili.ia.glossary;

/**
 * @author:      xiaosa
 * @date:        2020/6/16
 * @version:     农批业务系统重构
 * @description: 表类别枚举
 */
public enum MeterDetailStateEnum {
    UNSUBMITED(1, "已创建"),
    SUBMITED(2, "已提交"),
    PAID(3, "已取消"),
    ABOLISHED(4, "已缴费"),
    ;

    private String name;
    private Integer code ;

    MeterDetailStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static MeterDetailStateEnum getMeterDetailStateEnum(Integer code) {
        for (MeterDetailStateEnum anEnum : MeterDetailStateEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getMeterDetailStateEnumName(Integer code) {
        for (MeterDetailStateEnum r : MeterDetailStateEnum.values()) {
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
