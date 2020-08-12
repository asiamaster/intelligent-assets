package com.dili.ia.glossary;

/**
 * @Desciption 开票类型
 * @author wangmi
 * @Date 10:44 2020/7/30
 **/
public enum InvoiceTypeEnum {
    REGULAR(1, "普票"),
    SPECIAL(2, "专票"),
    ;

    private String name;
    private Integer code ;

    InvoiceTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static InvoiceTypeEnum getPayStateEnum(Integer code) {
        for (InvoiceTypeEnum anEnum : InvoiceTypeEnum.values()) {
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
