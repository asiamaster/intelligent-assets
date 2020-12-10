package com.dili.ia.domain.dto;

import com.dili.ia.domain.RefundOrder;

import javax.persistence.Transient;

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
