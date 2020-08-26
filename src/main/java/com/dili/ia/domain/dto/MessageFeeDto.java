package com.dili.ia.domain.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年8月24日
 */
public class MessageFeeDto {

	/**
     * 业务编号
     */
    private String code;

    @NotNull(message = "客户信息不能为空!")
    private Long customerId;

    private String customerName;

    private String customerPhone;

    /**
     * 客户证件号
     */
    private String customerCertificateNumber;


    /**
     * 开始时间
     */
    @NotNull(message = "开始时间不能为空!")
    private LocalDateTime startDate;

    /**
     * 结束时间
     */
    @NotNull(message = "结算时间不能为空!")
    private LocalDateTime endDate;

    /**
     * 收费金额
     */
    @NotNull(message = "收费金额不能为空!")
    private Long amount;

    /**
     * 转抵扣金额
     */
    private Long transactionAmount;

    /**
     * 支付金额
     */
    private Long payAmount;

    /**
     * 备注
     */
    private String notes;

    /**
     * 缴费单号
     */
    private String paymentOrderCode;

    /**
     * 作废原因
     */
    private String cancelerNotes;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getCustomerCertificateNumber() {
		return customerCertificateNumber;
	}

	public void setCustomerCertificateNumber(String customerCertificateNumber) {
		this.customerCertificateNumber = customerCertificateNumber;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Long getAmount() {
		return amount;
	}

	public void setAmount(Long amount) {
		this.amount = amount;
	}

	public Long getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Long transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public Long getPayAmount() {
		return payAmount;
	}

	public void setPayAmount(Long payAmount) {
		this.payAmount = payAmount;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getPaymentOrderCode() {
		return paymentOrderCode;
	}

	public void setPaymentOrderCode(String paymentOrderCode) {
		this.paymentOrderCode = paymentOrderCode;
	}

	public String getCancelerNotes() {
		return cancelerNotes;
	}

	public void setCancelerNotes(String cancelerNotes) {
		this.cancelerNotes = cancelerNotes;
	}
	
    
    
}
