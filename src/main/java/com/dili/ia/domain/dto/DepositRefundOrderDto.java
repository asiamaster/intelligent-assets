package com.dili.ia.domain.dto;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransferDeductionItem;

import javax.persistence.Transient;
import java.util.List;

public class DepositRefundOrderDto extends RefundOrder {
    //转抵扣
    private List<TransferDeductionItem> transferDeductionItems;
    @Transient
    private String logContent;

    public List<TransferDeductionItem> getTransferDeductionItems() {
        return transferDeductionItems;
    }

    public void setTransferDeductionItems(List<TransferDeductionItem> transferDeductionItems) {
        this.transferDeductionItems = transferDeductionItems;
    }

    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }
}
