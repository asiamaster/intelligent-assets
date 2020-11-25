package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2020-02-17 18:51
 */
public enum BizTypeEnum {
    BOOTH_LEASE("1", "摊位租赁","boothLease"),
    LOCATION_LEASE("4", "冷库租赁","locationLease"),
    LODGING_LEASE("5", "公寓租赁","lodgingLease"),
    EARNEST("2", "定金","earnest"),
    DEPOSIT_ORDER("3", "保证金","deposit"),
    ;

    private String name;
    private String enName;
    private String code ;

    BizTypeEnum(String code, String name, String enName){
        this.code = code;
        this.name = name;
        this.enName = enName;
    }

    public static BizTypeEnum getBizTypeEnum(String code) {
        for (BizTypeEnum anEnum : BizTypeEnum.values()) {
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

    public String getEnName() {
        return enName;
    }
}
