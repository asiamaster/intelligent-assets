package com.dili.ia.domain.dto;

import java.util.List;

public class BatchDepositBalanceQueryDto {
    private String bizType;
    private Integer assetsType;
    private Long customerId;
    private List<Long> assetsIds;

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    public Integer getAssetsType() {
        return assetsType;
    }

    public void setAssetsType(Integer assetsType) {
        this.assetsType = assetsType;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public List<Long> getAssetsIds() {
        return assetsIds;
    }

    public void setAssetsIds(List<Long> assetsIds) {
        this.assetsIds = assetsIds;
    }
}
