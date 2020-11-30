package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-02-26 15:23
 */
@Deprecated
public enum TransactionItemTypeEnum {
    EARNEST(1, "定金"),
    TRANSFER(2, "转抵"),
    DEPOSIT(3, "保证金"),
    ;

    private String name;
    private Integer code ;

    TransactionItemTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static TransactionItemTypeEnum getTransactionItemTypeEnum(Integer code) {
        for (TransactionItemTypeEnum anEnum : TransactionItemTypeEnum.values()) {
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
