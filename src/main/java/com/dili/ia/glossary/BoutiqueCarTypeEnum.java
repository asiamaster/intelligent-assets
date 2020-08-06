package com.dili.ia.glossary;

/**
 * @author:      xiaosa
 * @date:        2020/7/13
 * @version:     农批业务系统重构
 * @description: 精品停车 车型 枚举
 */
public enum BoutiqueCarTypeEnum {
    TRAILER(1,"挂车"),
    CONTAINER_TRUCK(2,"柜车"),
    ;

    private String name;
    private Integer code ;

    BoutiqueCarTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static BoutiqueCarTypeEnum getCarTypeEnum(Integer code) {
        for (BoutiqueCarTypeEnum anEnum : BoutiqueCarTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getCarTypeEnumName(Integer code) {
        for (BoutiqueCarTypeEnum r : BoutiqueCarTypeEnum.values()) {
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
