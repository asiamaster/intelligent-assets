package com.dili.ia.glossary;


public enum BoothStateEnum {

    ON(1, "启用"),
    OFF(2, "禁用"),
    ;

    private String name;
    private Integer code ;

    BoothStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static BoothStateEnum getStateEnum(Integer code) {
        for (BoothStateEnum anEnum : BoothStateEnum.values()) {
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
