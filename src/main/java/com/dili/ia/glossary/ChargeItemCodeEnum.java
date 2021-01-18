package com.dili.ia.glossary;
/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2021-01-18 11:51
 */
public enum ChargeItemCodeEnum {
    //静态收费项，费用项 code 定义，和费用项配置里面静态收费项的code值一样，用于获取静态收费项
    DEPOSIT("deposit", "保证金"),
    EARNEST("earnest", "定金"),
    BOUTIQUE("boutique", "精品停车费"),
    WATER("water", "水费"),
    ELECTRICITY("electricity", "电费"),
    PASSPORT("passport", "通行证"),
    ;

    private String code;
    private String name;

    ChargeItemCodeEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static ChargeItemCodeEnum getByCode(String code) {
        for (ChargeItemCodeEnum temp : ChargeItemCodeEnum.values()) {
            if (temp.getCode().equals(code)) {
                return temp;
            }
        }
        return null;
    }

    public static String getNameByCode(String code) {
        for (ChargeItemCodeEnum temp : ChargeItemCodeEnum.values()) {
            if (temp.getCode().equals(code)) {
                return temp.getName();
            }
        }
        return null;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
