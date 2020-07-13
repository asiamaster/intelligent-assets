package com.dili.ia.glossary;

public enum ApprovalStateEnum {
    WAIT_SUBMIT_APPROVAL(1, "待提审批"),
    IN_REVIEW(2, "审批中"),
    APPROVED(3, "审批通过"),
    APPROVAL_DENIED(4, "审批拒绝"),
    ;


    private String name;
    private Integer code ;
    ApprovalStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static ApprovalStateEnum getApprovalStateEnum(Integer code) {
        for (ApprovalStateEnum anEnum : ApprovalStateEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getName() {
        return name;
    }

    public Integer getCode() {
        return code;
    }

}
