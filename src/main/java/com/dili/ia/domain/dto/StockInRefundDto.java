package com.dili.ia.domain.dto;

import javax.validation.constraints.NotNull;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年6月28日
 */
public class StockInRefundDto {
	/**
	 * 入库单
	 */
	@NotNull(message = "code is null")
	private String code;
	
	/**
	 * 退款原因
	 */
	private String notes;
	
	/**
	 * 退款金额
	 */
	private Long amount;
	
	/**
	 * 收款人id
	 */
	private Long payeeId;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Long payeeId) {
		this.payeeId = payeeId;
	}
	
	
	
}
