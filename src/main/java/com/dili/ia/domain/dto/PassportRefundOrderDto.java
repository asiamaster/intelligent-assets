package com.dili.ia.domain.dto;

import com.dili.ia.domain.RefundFeeItem;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransferDeductionItem;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;

public class PassportRefundOrderDto {

    /**
     * 数据的主键id
     */
    private Long businessId;

    /**
     * 数据的code
     */
    private String businessCode;

    /**
     * 退款总金额
     */
    private Long totalRefundAmount;

    /**
     * 退款金额
     */
    private Long payeeAmount;

    /**
     * 收款人id
     */
    private Long payeeId;

    /**
     * 收款人name
     */
    private String payee ;

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
    private String refundType;

    /**
     * 退款原因
     */
    private String refundReason;

    /**
     * 转抵扣
     */
    private List<TransferDeductionItem> transferDeductionItems;

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public Long getPayeeAmount() {
        return payeeAmount;
    }

    public void setPayeeAmount(Long payeeAmount) {
        this.payeeAmount = payeeAmount;
    }

    public String getRefundReason() {
        return refundReason;
    }

    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

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

    public Long getPayeeId() {
        return payeeId;
    }

    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
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

    public String getRefundType() {
        return refundType;
    }

    public void setRefundType(String refundType) {
        this.refundType = refundType;
    }
}
