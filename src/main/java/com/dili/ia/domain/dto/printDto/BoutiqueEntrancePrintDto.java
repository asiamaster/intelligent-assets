package com.dili.ia.domain.dto.printDto;

import com.dili.ia.domain.TransferDeductionItem;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/7/14
 * @version:      农批业务系统重构
 * @description:  精品停车打印
 */
public class BoutiqueEntrancePrintDto {
    //打印时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime printTime;
    //开始日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startTime;
    //结束日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endTime;

    //补打标记
    private String reprint;
    //订单编号
    private String code;
    //业务类型
    private String businessType;
    //客户名称
    private String customerName;
    //客户电话
    private String customerCellphone;
    //备注
    private String notes;
    //总金额
    private String amount;
    //结算方式
    private String settlementWay;
    //结算员
    private String settlementOperator;
    //提交人
    private String submitter;
    //结算详情
    private String settleWayDetails;

    /**
     * 车牌号
     */
    private String plate;

    /**
     * 确认时间
     */
    private LocalDateTime confirmTime;

    /**
     * 退款字段 收款人
     */
    private String payee;

    /**
     * 收款金额
     */
    private Long payeeAmount;

    /**
     * 退款方式
     */
    private String refundMethod;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 银行卡号
     */
    private String bankNo;

    /**
     * 园区卡号
     */
    private String accountCardNo;

    /**
     * 转抵信息
     */
    private List<TransferDeductionItem> transferDeductionItems;

    /**
     * 退款原因
     */
    private String refundReason;

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
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

    public LocalDateTime getConfirmTime() {
        return confirmTime;
    }

    public void setConfirmTime(LocalDateTime confirmTime) {
        this.confirmTime = confirmTime;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
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

    public LocalDateTime getPrintTime() {
        return printTime;
    }

    public void setPrintTime(LocalDateTime printTime) {
        this.printTime = printTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSettlementWay() {
        return settlementWay;
    }

    public void setSettlementWay(String settlementWay) {
        this.settlementWay = settlementWay;
    }

    public String getSettlementOperator() {
        return settlementOperator;
    }

    public void setSettlementOperator(String settlementOperator) {
        this.settlementOperator = settlementOperator;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public String getSettleWayDetails() {
        return settleWayDetails;
    }

    public void setSettleWayDetails(String settleWayDetails) {
        this.settleWayDetails = settleWayDetails;
    }
}
