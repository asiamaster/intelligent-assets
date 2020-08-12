package com.dili.ia.domain.dto;

import com.dili.ia.domain.BoutiqueEntranceRecord;

import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/7/13
 * @version:      农批业务系统重构
 * @description:  精品停车记录表 Dto
 */
public class BoutiqueEntranceRecordDto extends BoutiqueEntranceRecord {

    /**
     * 归属市场ID
     */
    private Long marketId;

    /**
     * 取消原因
     */
    private String cancelReason;

    // 缴费单列表
    List<BoutiqueFeeOrderDto> orderDtoList;

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public List<BoutiqueFeeOrderDto> getOrderDtoList() {
        return orderDtoList;
    }

    public void setOrderDtoList(List<BoutiqueFeeOrderDto> orderDtoList) {
        this.orderDtoList = orderDtoList;
    }
}
