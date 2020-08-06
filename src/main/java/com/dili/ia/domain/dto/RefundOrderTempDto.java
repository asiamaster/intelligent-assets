package com.dili.ia.domain.dto;

import com.dili.ia.domain.RefundOrder;

import javax.persistence.Transient;

/**
 * 临时对象，用于数据迁移退款单，对象构建
 *
 *  2020-08-06 15:03:49.
 */
public class RefundOrderTempDto extends RefundOrder {
    /**
     * 资产ID
     *
     */
    @Transient
    private Long assetsId;

    public Long getAssetsId() {
        return assetsId;
    }

    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }
}
