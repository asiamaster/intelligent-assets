package com.dili.ia.glossary;

/**
 * 审批结果
 */
public enum ApprovalResultEnum {
    SUBMIT(0, "提交审批"),
    AGREE(1, "同意"),
    DENY(2, "拒绝");


    private String name;
    private Integer code ;
    ApprovalResultEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static ApprovalResultEnum getApprovalStateEnum(Integer code) {
        for (ApprovalResultEnum anEnum : ApprovalResultEnum.values()) {
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
