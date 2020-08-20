package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年8月19日
 */
public enum LaborActionEnum {
	ADD("add", "创建"),
	UPDATE("update", "修改"),
	RENAME("rename", "更名"),
	REMODEL("remodel", "更型"),
	RENEW("renew", "续费");
    private String name;
    private String code ;

    LaborActionEnum(String code, String name){
        this.code = code;
        this.name = name;
    }

    public static LaborActionEnum getLaborActionEnum(String code) {
        for (LaborActionEnum anEnum : LaborActionEnum.values()) {
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
