package com.dili.ia.domain.dto;

import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * <B>Description</B> 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播 <B>农丰时代科技有限公司</B>
 *
 * @Description 退款单创建,可增加需要字段
 * @author yangfan
 * @date 2020年6月23日
 */
public class RefundInfoDto {
	/**
	 * 业务单
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
	private Long payeeAmount;

	/**
	 * 收款人id
	 */
	private Long payeeId;

	/**
	 * 数据的code
	 */
	private String businessCode;

	/**
	 * 退款总金额
	 */
	private Long totalRefundAmount;

	/**
	 * 收款人name
	 */
	private String payee;

	/**
	 * 证件号
	 */
	private String payeeCertificateNumber;

	/**
	 * 手机号
	 */
	private String payeeCellphone;

	/**
	 * 退款类型
	 */
	private Integer refundType;
	
	/**
	 * 银行
	 */
	private String bank;
	
	/**
	 * 银行卡号
	 */
	private String bankCardNo;

	/**
	 * 转抵扣
	 */
	private List<TransferDeductionItem> transferDeductionItems;

	public String getPayeeCellphone() {
		return payeeCellphone;
	}

	public void setPayeeCellphone(String payeeCellphone) {
		this.payeeCellphone = payeeCellphone;
	}

	public List<TransferDeductionItem> getTransferDeductionItems() {
		return transferDeductionItems;
	}

	public void setTransferDeductionItems(List<TransferDeductionItem> transferDeductionItems) {
		this.transferDeductionItems = transferDeductionItems;
	}

	public String getBusinessCode() {
		return businessCode;
	}

	public void setBusinessCode(String businessCode) {
		this.businessCode = businessCode;
	}

	public Long getTotalRefundAmount() {
		return totalRefundAmount;
	}

	public void setTotalRefundAmount(Long totalRefundAmount) {
		this.totalRefundAmount = totalRefundAmount;
	}

	public String getPayee() {
		return payee;
	}

	public void setPayee(String payee) {
		this.payee = payee;
	}

	public String getPayeeCertificateNumber() {
		return payeeCertificateNumber;
	}

	public void setPayeeCertificateNumber(String payeeCertificateNumber) {
		this.payeeCertificateNumber = payeeCertificateNumber;
	}

	public Integer getRefundType() {
		return refundType;
	}

	public void setRefundType(Integer refundType) {
		this.refundType = refundType;
	}

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

	public Long getPayeeAmount() {
		return payeeAmount;
	}

	public void setPayeeAmount(Long payeeAmount) {
		this.payeeAmount = payeeAmount;
	}

	public Long getPayeeId() {
		return payeeId;
	}

	public void setPayeeId(Long payeeId) {
		this.payeeId = payeeId;
	}

	public String getBank() {
		return bank;
	}

	public void setBank(String bank) {
		this.bank = bank;
	}

	public String getBankCardNo() {
		return bankCardNo;
	}

	public void setBankCardNo(String bankCardNo) {
		this.bankCardNo = bankCardNo;
	}
	
	

}
