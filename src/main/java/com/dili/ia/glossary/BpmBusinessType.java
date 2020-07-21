package com.dili.ia.glossary;

/**
 * 业务类型（流程用）
 * 对应approver_assignment表的business_type
 * @author wangmi
 */
public enum BpmBusinessType {
    RENTAL_APPROVAL(1, "租赁审批"),
    REFUND_APPROVAL(2, "退款审批"),
    ;

    private String name;
    private Integer code ;

    BpmBusinessType(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static BpmBusinessType getBpmBusinessType(Integer code) {
        for (BpmBusinessType anEnum : BpmBusinessType.values()) {
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
