package com.dili.ia.glossary;


public enum CornerEnum {

    YES(1, "是"),
    NO(2, "否"),
    ;

    private String name;
    private Integer code ;

    CornerEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static CornerEnum getCornerEnum(Integer code) {
        for (CornerEnum anEnum : CornerEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }


    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
