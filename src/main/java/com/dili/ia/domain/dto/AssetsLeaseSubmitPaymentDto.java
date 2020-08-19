package com.dili.ia.domain.dto;

import com.dili.ia.domain.BusinessChargeItem;

import java.util.List;
import java.util.Map;

/**
 * 资产租赁提交付款DTO
 */
public class AssetsLeaseSubmitPaymentDto {
    //订单ID
    private Long leaseOrderId;
    //支付金额
    private Long payAmount;
    //租赁支付金额
    private Long leasePayAmount;
    //收费项分摊记录
    private List<BusinessChargeItem> businessChargeItems;
    //资产保证金
    private Map<Long,Long> depositAmountMap;

    public Long getLeaseOrderId() {
        return leaseOrderId;
    }

    public void setLeaseOrderId(Long leaseOrderId) {
        this.leaseOrderId = leaseOrderId;
    }

    public Long getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }

    public Long getLeasePayAmount() {
        return leasePayAmount;
    }

    public void setLeasePayAmount(Long leasePayAmount) {
        this.leasePayAmount = leasePayAmount;
    }

    public List<BusinessChargeItem> getBusinessChargeItems() {
        return businessChargeItems;
    }

    public void setBusinessChargeItems(List<BusinessChargeItem> businessChargeItems) {
        this.businessChargeItems = businessChargeItems;
    }

    public Map<Long, Long> getDepositAmountMap() {
        return depositAmountMap;
    }

    public void setDepositAmountMap(Map<Long, Long> depositAmountMap) {
        this.depositAmountMap = depositAmountMap;
    }
}
