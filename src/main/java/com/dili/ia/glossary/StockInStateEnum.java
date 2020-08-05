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
public enum StockInStateEnum {
	CREATED(1, "已创建"),
	CANCELLED(2, "已取消"),
	SUBMITTED_PAY(3, "已提交"),
	PAID(4, "已缴费"),
	SUBMITTED_REFUND(5, "退款中"),
	REFUNDED(6, "已退款"),
	EXPIRE(7, "已到期")
    ;
    private String name;
    private Integer code ;

    StockInStateEnum(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public static StockInStateEnum getStockInStateEnum(Integer code) {
        for (StockInStateEnum anEnum : StockInStateEnum.values()) {
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
