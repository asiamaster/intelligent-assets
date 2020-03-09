package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * 订单停租状态
 * @author jiangchengyong
 * @createTime 2020/02/10 18:43
 */
public enum StopRentStateEnum {

    NO_APPLY(1, "未发起"),
    WAIT_TIMER_EXE(2, "等待定时执行"),
    RENTED_OUT(3, "已停租"),
    ;

    private String name;
    private Integer code ;

    StopRentStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static StopRentStateEnum getStopRentStateEnum(Integer code) {
        for (StopRentStateEnum anEnum : StopRentStateEnum.values()) {
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
