package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年8月25日
 */
public enum MessageFeeStateEnum {
	CREATED(1, "已创建"),
	CANCELLED(2, "已取消"),
	SUBMITTED_PAY(3, "已提交"),
	NOT_STARTED(4, "未开始"),//已缴费
	IN_EFFECTIVE(5, "已生效"),//已缴费,有效时间内
	SUBMITTED_REFUND(6, "退款中"),
	REFUNDED(7, "已退款"),
	EXPIRED(8, "已到期"),
    ;

    private String name;
    private Integer code ;

    MessageFeeStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static MessageFeeStateEnum getMeterDetailStateEnum(Integer code) {
        for (MessageFeeStateEnum anEnum : MessageFeeStateEnum.values()) {
            if (anEnum.getCode().equals(code)) {
                return anEnum;
            }
        }
        return null;
    }

    public static String getMessageFeeStateEnum(Integer code) {
        for (MessageFeeStateEnum r : MessageFeeStateEnum.values()) {
            if (r.getCode().equals(code)) {
                return r.getName();
            }
        }
        return "";
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
