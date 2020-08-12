package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * 此枚举和数据字典里面的 depositType 里面的这3种类型是对应关系的，临时处理方案
 *
 * @author qinkelan
 * @createTime 2020-02-17 18:51
 */
public enum DepositTypeEnum {
    BOOTH("booth","摊位保证金"),
    LOCATION("location","冷库保证金"),
    LODGING("lodging","公寓保证金"),
    ;

    private String typeCode ;
    private String typeName;

    DepositTypeEnum(String typeCode, String typeName){
        this.typeCode = typeCode;
        this.typeName = typeName;
    }

    public static DepositTypeEnum getAssetsTypeEnum(String typeCode) {
        for (DepositTypeEnum anEnum : DepositTypeEnum.values()) {
            if (anEnum.getTypeCode().equals(typeCode)) {
                return anEnum;
            }
        }
        return null;
    }

    public String getTypeName() {
        return typeName;
    }
    public String getTypeCode() {
        return typeCode;
    }

}
