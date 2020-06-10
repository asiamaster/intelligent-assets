package com.dili.ia.domain.dto;

import com.dili.ia.domain.AssetLeaseOrderItem;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * 租赁订单列表查询dto
 * This file was generated on 2020-02-11 15:54:49.
 */
public class AssetLeaseOrderItemListDto extends AssetLeaseOrderItem {
    @Column(name = "`create_time`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private LocalDateTime createdStart;

    @Column(name = "`create_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private LocalDateTime createdEnd;

    @Column(name = "`stop_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private LocalDateTime stopTimeLet;


    @Operator(Operator.IN)
    @Column(name = "id")
    private List<Long> ids;

    @Operator(Operator.IN)
    @Column(name = "booth_id")
    private List<Long> boothIds;

    @Operator(Operator.IN)
    @Column(name = "state")
    private List<Long> states;

    /**
     * 业务收费项
     */
    @Transient
    private Map<String,String> businessChargeItem;

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

    public LocalDateTime getStopTimeLet() {
        return stopTimeLet;
    }

    public void setStopTimeLet(LocalDateTime stopTimeLet) {
        this.stopTimeLet = stopTimeLet;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getBoothIds() {
        return boothIds;
    }

    public void setBoothIds(List<Long> boothIds) {
        this.boothIds = boothIds;
    }

    public List<Long> getStates() {
        return states;
    }

    public void setStates(List<Long> states) {
        this.states = states;
    }

    public Map<String, String> getBusinessChargeItem() {
        return businessChargeItem;
    }

    public void setBusinessChargeItem(Map<String, String> businessChargeItem) {
        this.businessChargeItem = businessChargeItem;
    }
}