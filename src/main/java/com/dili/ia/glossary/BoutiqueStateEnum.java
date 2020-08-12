package com.dili.ia.glossary;

/**
 * @author:      xiaosa
 * @date:        2020/7/13
 * @version:     农批业务系统重构
 * @description: 精品停车状态枚举
 */
public enum BoutiqueStateEnum {
    NOCONFIRM(1,"待确认"),
    COUNTING(2,"计费中"),
    LEAVE(3,"已离场"),
    REVOKE(4,"已撤消")
    ;

    private String name;
    private Integer code ;

    BoutiqueStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static BoutiqueStateEnum getBoutiqueStateEnum(Integer code) {
        for (BoutiqueStateEnum anEnum : BoutiqueStateEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getBoutiqueStateEnumName(Integer code) {
        for (BoutiqueStateEnum r : BoutiqueStateEnum.values()) {
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
