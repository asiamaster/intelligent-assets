package com.dili.ia.glossary;

/**
 * @author:      xiaosa
 * @date:        2020/11/26
 * @version:     农批业务系统重构
 * @description: 资产出租预设启用禁用枚举
 */
public enum AssetsRentalStateEnum {
    ENABLE(1, "启用"),
    DISABLE(2, "禁用"),
    ;

    private String name;
    private Integer code ;

    AssetsRentalStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static AssetsRentalStateEnum getAssetsRentalStateEnum(Integer code) {
        for (AssetsRentalStateEnum anEnum : AssetsRentalStateEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getAssetsRentalStateEnumName(Integer code) {
        for (AssetsRentalStateEnum r : AssetsRentalStateEnum.values()) {
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
