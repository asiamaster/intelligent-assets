package com.dili.ia.domain.dto;

import com.dili.ia.domain.BoutiqueEntranceRecord;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/7/13
 * @version:      农批业务系统重构
 * @description:  精品停车记录表 Dto
 */
public class BoutiqueEntranceRecordDto extends BoutiqueEntranceRecord {

    /**
     * 查询字段进场开始时间
     */
    private LocalDateTime enterTimeStart;

    /**
     * 查询字段进场结束时间
     */
    private LocalDateTime enterTimeEnd;

    /**
     * 查询字段确认开始时间
     */
    private LocalDateTime confirmTimeStart;

    /**
     * 查询字段确认结束时间
     */
    private LocalDateTime confirmTimeEnd;

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

    public LocalDateTime getEnterTimeStart() {
        return enterTimeStart;
    }

    public void setEnterTimeStart(LocalDateTime enterTimeStart) {
        this.enterTimeStart = enterTimeStart;
    }

    public LocalDateTime getEnterTimeEnd() {
        return enterTimeEnd;
    }

    public void setEnterTimeEnd(LocalDateTime enterTimeEnd) {
        this.enterTimeEnd = enterTimeEnd;
    }

    public LocalDateTime getConfirmTimeStart() {
        return confirmTimeStart;
    }

    public void setConfirmTimeStart(LocalDateTime confirmTimeStart) {
        this.confirmTimeStart = confirmTimeStart;
    }

    public LocalDateTime getConfirmTimeEnd() {
        return confirmTimeEnd;
    }

    public void setConfirmTimeEnd(LocalDateTime confirmTimeEnd) {
        this.confirmTimeEnd = confirmTimeEnd;
    }
}
