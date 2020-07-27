package com.dili.ia.glossary;

/**
 * @author:       xiaosa
 * @date:         2020/7/27
 * @version:      农批业务系统重构
 * @description:  通行证
 */
public enum PassportStateEnum {
    CREATED(1, "已创建"),
    SUBMITTED(2, "已提交"),
    CANCELLED(3, "已取消"),
    NOT_START(4, "未开始"),
    IN_FORCE(5, "未开始"),
    SUBMITTED_REFUND(6, "退款中"),
    REFUNDED(7, "已退款"),
    EXPIRED(8, "已到期"),
    ;

    private String name;
    private Integer code ;

    PassportStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static PassportStateEnum getPassportStateEnum(Integer code) {
        for (PassportStateEnum anEnum : PassportStateEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getPassportStateEnumName(Integer code) {
        for (PassportStateEnum r : PassportStateEnum.values()) {
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
