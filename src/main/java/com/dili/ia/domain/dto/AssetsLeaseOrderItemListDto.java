package com.dili.ia.domain.dto;

import com.dili.ia.domain.AssetsLeaseOrderItem;
import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ss.domain.annotation.Operator;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 由MyBatis Generator工具自动生成
 * 租赁订单列表查询dto
 * This file was generated on 2020-02-11 15:54:49.
 */
public class AssetsLeaseOrderItemListDto extends AssetsLeaseOrderItem {
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
    @Column(name = "lease_order_id")
    private List<Long> leaseOrderIds;

    @Operator(Operator.IN)
    @Column(name = "assets_id")
    private List<Long> assetsIds;

    @Operator(Operator.IN)
    @Column(name = "district_id")
    private List<Long> districtIds;

    @Operator(Operator.IN)
    @Column(name = "state")
    private List<Integer> states;

    @Transient
    private List<BusinessChargeItem> businessChargeItems;

    /**
     * 业务收费项 {id:amount}｛1：200，2：300｝
     */
    @Transient
    private Map<String,String> businessChargeItem;

    /**
     * 保证金补交金额
     */
    @Transient
    private Long depositMakeUpAmount;

    //停租日期
    @Transient
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate stopDate;

    //证件号
    private String certificateNumber;

    //客户电话
    private String customerCellphone;

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getCustomerCellphone() {
        return customerCellphone;
    }

    public void setCustomerCellphone(String customerCellphone) {
        this.customerCellphone = customerCellphone;
    }

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

    public List<Long> getLeaseOrderIds() {
        return leaseOrderIds;
    }

    public void setLeaseOrderIds(List<Long> leaseOrderIds) {
        this.leaseOrderIds = leaseOrderIds;
    }

    public List<Long> getAssetsIds() {
        return assetsIds;
    }

    public void setAssetsIds(List<Long> assetsIds) {
        this.assetsIds = assetsIds;
    }

    public List<Long> getDistrictIds() {
        return districtIds;
    }

    public void setDistrictIds(List<Long> districtIds) {
        this.districtIds = districtIds;
    }

    public List<Integer> getStates() {
        return states;
    }

    public void setStates(List<Integer> states) {
        this.states = states;
    }

    public List<BusinessChargeItem> getBusinessChargeItems() {
        return businessChargeItems;
    }

    public void setBusinessChargeItems(List<BusinessChargeItem> businessChargeItems) {
        this.businessChargeItems = businessChargeItems;
    }

    public Map<String, String> getBusinessChargeItem() {
        return businessChargeItem;
    }

    public void setBusinessChargeItem(Map<String, String> businessChargeItem) {
        this.businessChargeItem = businessChargeItem;
    }

    public Long getDepositMakeUpAmount() {
        return depositMakeUpAmount;
    }

    public void setDepositMakeUpAmount(Long depositMakeUpAmount) {
        this.depositMakeUpAmount = depositMakeUpAmount;
    }

    public LocalDate getStopDate() {
        return stopDate;
    }

    public void setStopDate(LocalDate stopDate) {
        this.stopDate = stopDate;
    }
}