package com.dili.ia.domain.dto;

import com.dili.ia.domain.AssetLeaseOrder;
import com.dili.ia.domain.AssetLeaseOrderItem;
import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.domain.LeaseOrderItem;
import com.dili.ss.domain.annotation.Like;
import com.dili.ss.domain.annotation.Operator;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * 租赁订单列表查询dto
 * This file was generated on 2020-02-11 15:54:49.
 */
public class AssetLeaseOrderListDto extends AssetLeaseOrder {
    @Column(name = "`create_time`")
    @Operator(Operator.GREAT_EQUAL_THAN)
    private LocalDateTime createdStart;

    @Column(name = "`create_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private LocalDateTime createdEnd;

    @Operator(Operator.IN)
    @Column(name = "code")
    private List<String> codes;

    @Operator(Operator.IN)
    @Column(name = "id")
    private List<Long> ids;

    @Operator(Operator.IN)
    @Column(name = "market_id")
    private List<Long> marketIds;

    @Operator(Operator.IN)
    @Column(name = "department_id")
    private List<Long> departmentIds;

    /**
     * 昵称模糊查询
     * @return
     */
    @Column(name = "customer_name")
    @Like
    private String likeCustomerName;

    @Transient
    private String assetName;

    @Transient
    private List<AssetLeaseOrderItem> leaseOrderItems;

    @Column(name = "`start_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private LocalDateTime startTimeLT;

    @Column(name = "`start_time`")
    @Operator(Operator.LITTLE_EQUAL_THAN)
    private LocalDateTime endTimeLT;

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

    public List<String> getCodes() {
        return codes;
    }

    public void setCodes(List<String> codes) {
        this.codes = codes;
    }

    public List<Long> getIds() {
        return ids;
    }

    public void setIds(List<Long> ids) {
        this.ids = ids;
    }

    public List<Long> getMarketIds() {
        return marketIds;
    }

    public void setMarketIds(List<Long> marketIds) {
        this.marketIds = marketIds;
    }

    public List<Long> getDepartmentIds() {
        return departmentIds;
    }

    public void setDepartmentIds(List<Long> departmentIds) {
        this.departmentIds = departmentIds;
    }

    public String getLikeCustomerName() {
        return likeCustomerName;
    }

    public void setLikeCustomerName(String likeCustomerName) {
        this.likeCustomerName = likeCustomerName;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public List<AssetLeaseOrderItem> getLeaseOrderItems() {
        return leaseOrderItems;
    }

    public void setLeaseOrderItems(List<AssetLeaseOrderItem> leaseOrderItems) {
        this.leaseOrderItems = leaseOrderItems;
    }

    public LocalDateTime getStartTimeLT() {
        return startTimeLT;
    }

    public void setStartTimeLT(LocalDateTime startTimeLT) {
        this.startTimeLT = startTimeLT;
    }

    public LocalDateTime getEndTimeLT() {
        return endTimeLT;
    }

    public void setEndTimeLT(LocalDateTime endTimeLT) {
        this.endTimeLT = endTimeLT;
    }
}