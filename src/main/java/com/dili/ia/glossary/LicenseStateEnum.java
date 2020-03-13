package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-03-02 21:00
 */
public enum LicenseStateEnum {
    NOT_HANDLED(1, "未办理"),
    CHANGED(2, "已变更"),
    NOT_LOGGED_OUT(3, "未注销"),
    LOGGED_OUT(4, "已注销"),
    ;

    private String name;
    private Integer code ;

    LicenseStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static LicenseStateEnum getEnabledState(Integer code) {
        for (LicenseStateEnum anEnum : LicenseStateEnum.values()) {
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
