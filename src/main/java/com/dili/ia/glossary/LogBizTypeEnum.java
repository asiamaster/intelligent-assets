package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2020-02-17 18:51
 */
public enum LogBizTypeEnum {
    BOOTH_LEASE("booth_lease", "摊位租赁"),
    EARNEST_ORDER("earnest_order", "定金管理"),
    REFUND_ORDER("refund_order", "退款单"),
    ;

    private String name;
    private String code ;

    LogBizTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static LogBizTypeEnum getLogBizTypeEnum(String code) {
        for (LogBizTypeEnum anEnum : LogBizTypeEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
