package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-02-26 15:21
 */
public enum TransactionSceneTypeEnum {
    PAYMENT(1, "交费"),
    DEDUCT_USE(2, "抵扣消费"),
    EARNEST_IN(3, "定金转入"),
    EARNEST_OUT(4, "定金转出"),
    REFUND(5, "退款"),
    FROZEN(6, "冻结"),
    UNFROZEN(7, "解冻"),
    TRANSFER_IN(8, "转抵转入"),
    ;

    private String name;
    private Integer code ;

    TransactionSceneTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static TransactionSceneTypeEnum getEnabledState(Integer code) {
        for (TransactionSceneTypeEnum anEnum : TransactionSceneTypeEnum.values()) {
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
