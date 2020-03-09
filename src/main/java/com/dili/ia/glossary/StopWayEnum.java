package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jiangchengyong
 * @createTime 2020/02/10 18:43
 */
public enum StopWayEnum {

    IMMEDIATELY(1, "立即"),
    TIMING(2, "选择日期"),
    ;

    private String name;
    private Integer code ;

    StopWayEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static StopWayEnum getStopWayEnum(Integer code) {
        for (StopWayEnum anEnum : StopWayEnum.values()) {
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
