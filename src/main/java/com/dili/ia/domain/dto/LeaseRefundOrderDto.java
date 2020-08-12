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

public class LeaseRefundOrderDto extends RefundOrder {
    @Column(name = "`create_time`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private LocalDateTime createdStart;

    @Column(name = "`create_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private LocalDateTime createdEnd;

    @Operator(Operator.IN)
    @Column(name = "code")
    private List<String> codes;

    /**
     * 昵称模糊查询
     * @return
     */
    @Column(name = "customer_name")
    @Like
    private String likeCustomerName;

    @Operator(Operator.IN)
    @Column(name = "id")
    private List<Long> ids;

    //转抵扣
    private List<TransferDeductionItem> transferDeductionItems;

    //业务退款项
    private List<RefundFeeItem> refundFeeItems;

    @Transient
    private LocalDateTime exitTime;
    @Transient
    private String logContent;

    public LocalDateTime getCreatedStart() {
        return createdStart;
    }

    public void setCreatedStart(LocalDateTime createdStart) {
        this.createdStart = createdStart;
    }

    public LocalDateTime getCreatedEnd() {
        return createdEnd;
    }

    public void setCreatedEnd(LocalDateTime createdEnd) {
        this.createdEnd = createdEnd;
    }

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public String getLikeCustomerName() {
        return likeCustomerName;
    }

    public void setLikeCustomerName(String likeCustomerName) {
        this.likeCustomerName = likeCustomerName;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public List<TransferDeductionItem> getTransferDeductionItems() {
        return transferDeductionItems;
    }

    public void setTransferDeductionItems(List<TransferDeductionItem> transferDeductionItems) {
        this.transferDeductionItems = transferDeductionItems;
    }

    public List<RefundFeeItem> getRefundFeeItems() {
        return refundFeeItems;
    }

    public void setRefundFeeItems(List<RefundFeeItem> refundFeeItems) {
        this.refundFeeItems = refundFeeItems;
    }

    public LocalDateTime getExitTime() {
        return exitTime;
    }

    public void setExitTime(LocalDateTime exitTime) {
        this.exitTime = exitTime;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }
}
