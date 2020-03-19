package com.dili.ia.glossary;

public enum RefundTypeEnum {
    CASH(1, "现金"),
    BANK(3, "银行卡转帐"),
    ;
    private String name;
    private Integer code ;

    RefundTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static BizTypeEnum getRefundTypeEnum(Integer code) {
        for (BizTypeEnum anEnum : BizTypeEnum.values()) {
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
