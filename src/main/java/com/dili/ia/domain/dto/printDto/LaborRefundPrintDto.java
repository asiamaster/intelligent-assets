package com.dili.ia.domain.dto.printDto;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <B>Description</B>
 * 本软件源代码版权归农丰时代及其团队所有,未经许可不得任意复制与传播
 * <B>农丰时代科技有限公司</B>
 *
 * @Description TODO(用一句话描述该文件做什么)
 * @author yangfan
 * @date 2020年8月5日
 */
public class LaborRefundPrintDto {
	// 打印时间
	@JSONField(format = "yyyy-MM-dd HH:mm:ss")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime printTime;
	// 补打标记
	private String reprint;
	// 订单编号
	private String code;
	// 业务类型
	private String businessType;
	// 客户名称
	private String customerName;
	// 客户电话
	private String customerCellphone;
	// 总金额
	private String totalAmount;
	// 提交人
	private String submitter;
	// 结算员
	private String settlementOperator;
	// 备注信息
	private String notes;
	// 马甲号
	private String workCard;
	// 收款人
	private String payee;
	// 收款金额
	private Long payeeAmount;
	// 退款方式
	private String refundMethod;
	// 开户行
	private String bankName;
	// 银行卡号
	private String bankNo;
	// 园区卡号
	private String accountCardNo;
	// 转抵信息
	private List<TransferDeductionItem> transferDeductionItems;
	public LocalDateTime getPrintTime() {
		return printTime;
	}
	public void setPrintTime(LocalDateTime printTime) {
		this.printTime = printTime;
	}
	public String getReprint() {
		return reprint;
	}
	public void setReprint(String reprint) {
		this.reprint = reprint;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getBusinessType() {
		return businessType;
	}
	public void setBusinessType(String businessType) {
		this.businessType = businessType;
	}
	public String getCustomerName() {
		return customerName;
	}
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}
	public String getCustomerCellphone() {
		return customerCellphone;
	}
	public void setCustomerCellphone(String customerCellphone) {
		this.customerCellphone = customerCellphone;
	}
	public String getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(String totalAmount) {
		this.totalAmount = totalAmount;
	}
	public String getSubmitter() {
		return submitter;
	}
	public void setSubmitter(String submitter) {
		this.submitter = submitter;
	}
	public String getSettlementOperator() {
		return settlementOperator;
	}
	public void setSettlementOperator(String settlementOperator) {
		this.settlementOperator = settlementOperator;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getWorkCard() {
		return workCard;
	}
	public void setWorkCard(String workCard) {
		this.workCard = workCard;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	
	public Long getPayeeAmount() {
		return payeeAmount;
	}
	public void setPayeeAmount(Long payeeAmount) {
		this.payeeAmount = payeeAmount;
	}
	public String getRefundMethod() {
		return refundMethod;
	}
	public void setRefundMethod(String refundMethod) {
		this.refundMethod = refundMethod;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBankNo() {
		return bankNo;
	}
	public void setBankNo(String bankNo) {
		this.bankNo = bankNo;
	}
	public String getAccountCardNo() {
		return accountCardNo;
	}
	public void setAccountCardNo(String accountCardNo) {
		this.accountCardNo = accountCardNo;
	}
	public List<TransferDeductionItem> getTransferDeductionItems() {
		return transferDeductionItems;
	}
	public void setTransferDeductionItems(List<TransferDeductionItem> transferDeductionItems) {
		this.transferDeductionItems = transferDeductionItems;
	}
	
}

