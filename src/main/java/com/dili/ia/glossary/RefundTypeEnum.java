package com.dili.ia.glossary;

public enum RefundTypeEnum {
    CASH(1, "现金"),
    BANK(3, "银行卡转账"),
    ;
    private String name;
    private Integer code ;

    RefundTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static RefundTypeEnum getRefundTypeEnum(Integer code) {
        for (RefundTypeEnum anEnum : RefundTypeEnum.values()) {
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
