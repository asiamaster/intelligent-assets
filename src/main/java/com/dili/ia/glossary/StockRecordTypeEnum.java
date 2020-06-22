package com.dili.ia.glossary;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年6月22日
 */
public enum StockRecordTypeEnum {
	STOCK_OUT(2, "出库"), 
	STOCK_IN(1, "入库");


	private String name;
	private Integer code;

	StockRecordTypeEnum(Integer code, String name) {
		this.code = code;
		this.name = name;
	}

	public static StockRecordTypeEnum getRefundTypeEnum(Integer code) {
		for (StockRecordTypeEnum anEnum : StockRecordTypeEnum.values()) {
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
