package com.dili.ia.domain.dto;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransferDeductionItem;

import javax.persistence.Transient;
import java.util.List;

public class EarnestRefundOrderDto extends RefundOrder {
    @Transient
    private String logContent;


    public String getLogContent() {
        return logContent;
    }

    public void setLogContent(String logContent) {
        this.logContent = logContent;
    }
}
