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
    BOOTH_LEASE(1, "摊位租赁"),
    EARNEST(2, "定金"),
    STOCKIN(3, "入库单"),
    WATER_METER(4, "水表"),
    ELECTRIC_METER(5, "电表"),
    ;

    private String name;
    private Integer code ;

    BizTypeEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static BizTypeEnum getBizTypeEnum(Integer code) {
        for (BizTypeEnum anEnum : BizTypeEnum.values()) {
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
