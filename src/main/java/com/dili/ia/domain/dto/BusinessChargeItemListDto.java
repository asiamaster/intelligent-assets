package com.dili.ia.domain.dto;

import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import java.util.List;

public class BusinessChargeItemListDto extends BusinessChargeItem {
    @Operator(Operator.IN)
    @Column(name = "business_id")
    private List<Long> businessIds;

    public List<Long> getBusinessIds() {
        return businessIds;
    }

    public void setBusinessIds(List<Long> businessIds) {
        this.businessIds = businessIds;
    }
}
