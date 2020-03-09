package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @author jcy
 * @createTime 2020-02-17 18:51
 */
public enum PrintTemplateEnum {
    BOOTH_LEASE_NOT_PAID("boothLeaseNotPaid", "摊位租赁未交清"),
    BOOTH_LEASE_PAID("boothLeasePaid", "摊位租赁已交清"),
    ;

    private String name;
    private String code ;

    PrintTemplateEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static PrintTemplateEnum getPrintTemplateEnum(String code) {
        for (PrintTemplateEnum anEnum : PrintTemplateEnum.values()) {
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
