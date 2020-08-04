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
    BOOTH_LEASE("1", "摊位租赁"),
    EARNEST("2", "定金"),
    DEPOSIT_ORDER("3", "保证金"),
    WATER_ELECTRICITY("5", "水电费"),
    BOUTIQUE_ENTRANCE("6", "精品停车费"),
    PASSPORT("7", "通行证"),
    STOCKIN("8", "冷库入库"),
    STOCKOUT("9", "冷库出库"),
    OTHER_FEE("10", "其它收费"),
    LABOR_VEST("11", "劳务马甲"),
    LABOR_VEST_RENAME("12", "劳务马甲更名费"),
    LABOR_VEST_REMODEL("13", "劳务马甲更型费"),
    ;

    private String name;
    private String code ;

    BizTypeEnum(String code, String name){
        this.code = code;
        this.name = name;
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
}
