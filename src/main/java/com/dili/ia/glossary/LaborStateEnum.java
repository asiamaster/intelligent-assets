package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年6月12日
 */
public enum LaborStateEnum {
	CREATED(1, "已创建"),
	CANCELLED(2, "已取消"),
	SUBMITTED_PAY(3, "已提交"),
	NOT_STARTED(4, "未开始"),//已缴费
	IN_EFFECTIVE(5, "已生效"),//已缴费,有效时间内
	IN_RENAME(6, "更名中"),
	RENAME(7, "已更名"),
	IN_REMODEL(8, "更型中"),
	REMODEL(9, "已更型"),
	SUBMITTED_REFUND(10, "退款中"),
	REFUNDED(11, "已退款"),
	EXPIRED(12, "已到期"),
    ;
    private String name;
    private Integer code ;

    LaborStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static LaborStateEnum getLaborStateEnum(Integer code) {
        for (LaborStateEnum anEnum : LaborStateEnum.values()) {
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
