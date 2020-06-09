package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author qinkelan
 * @createTime 2020-02-17 18:51
 */
public enum AssetsTypeEnum {
    BOOTH(1, "摊位", "booth","1"),
    LOCATION(2, "冷库", "location","4"),
    LODGING(3, "公寓", "lodging","5"),
    OTHER(100, "摊位", "other",null)
    ;

    private String name;
    private Integer code ;
    private String typeCode ;
    private String bizType;

    AssetsTypeEnum(Integer code, String name, String typeCode,String bizType){
        this.code = code;
        this.name = name;
        this.typeCode = typeCode;
        this.bizType = bizType;
    }

    public static AssetsTypeEnum getAssetsTypeEnum(Integer code) {
        for (AssetsTypeEnum anEnum : AssetsTypeEnum.values()) {
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
    public String getTypeCode() {
        return typeCode;
    }

    public String getBizType() {
        return bizType;
    }
}
