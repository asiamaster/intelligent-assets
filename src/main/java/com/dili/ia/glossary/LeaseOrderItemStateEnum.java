package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * 订单项状态
 * @author jiangchengyong
 * @createTime 2020/02/10 18:43
 */
public enum LeaseOrderItemStateEnum {

    CREATED(1, "已创建"),
    CANCELD(2, "已取消"),
    SUBMITTED(3, "已提交"),
    NOT_ACTIVE(4, "未生效"),
    EFFECTIVE(5, "已生效"),
    RENTED_OUT(6, "已停租"),
    REFUNDED(7, "已退款"),
    EXPIRED(8, "已过期"),
    ;

    private String name;
    private Integer code ;

    LeaseOrderItemStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static LeaseOrderItemStateEnum getEnabledState(Integer code) {
        for (LeaseOrderItemStateEnum anEnum : LeaseOrderItemStateEnum.values()) {
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
