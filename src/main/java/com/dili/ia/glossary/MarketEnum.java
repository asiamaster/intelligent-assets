package com.dili.ia.glossary;
/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description 市场名称及code枚举，和线上一致，市场code不可修改，用于代码中有根据市场硬编码时使用
 * @author qinkelan
 * @date 2020/12/17 11:43
 */
public enum MarketEnum {
    GROUP("group", "集团"),
    HD("hd", "哈尔滨哈达"),
    CD("cd", "成都聚合"),
    QQHE("qqhe", "齐齐哈尔地利农产品"),
    MDJ("mdj", "牡丹江国际农产品"),
    GY("gy", "贵阳地利农产品"),
    CC("cc", "长春地利农副产品"),
    SG("sg", "寿光地利农产品"),
    SY("sy", "沈阳地利农副产品"),
    DLK("dlk", "达利凯"),
    HZSC("hzsc", "杭州水产"),
    SZPT("szpt", "数字平台部"),
    HZVE("hzve", "杭州蔬菜"),
    HZGP("hzgp", "杭州果品"),
    JYGL("jygl", "经营管理办公室"),
    GX("gx", "革新"),
    ;

    private String name;
    private String code ;

    MarketEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static MarketEnum getMarketEnum(String code) {
        for (MarketEnum anEnum : MarketEnum.values()) {
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
