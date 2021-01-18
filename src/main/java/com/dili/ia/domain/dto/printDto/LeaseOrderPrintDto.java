package com.dili.ia.domain.dto.printDto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class LeaseOrderPrintDto {
    //打印时间
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime printTime;
    //补打标记
    private String reprint;
    //订单编号
    private String leaseOrderCode;
    //业务类型
    private String businessType;
    //客户名称
    private String customerName;
    //客户电话
    private String customerCellphone;
    //开始日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JSONField(format="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startTime;
    //结束日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JSONField(format="yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;
    //是否续签(1:是 2：否)
    private String isRenew;
    //经营品类
    private String categoryName;
    //备注
    private String notes;
    //总金额
    private String totalAmount;
    //抵扣
    private String deduction;
    //保证金转抵
    private String depositDeduction;
    //定金转抵
    private String earnestDeduction;
    //转抵扣
    private String transferDeduction;
    //应付金额
    private String payAmount;
    //本次付款金额
    private String amount;
    //待付金额
    private String waitAmount;
    //结算方式
    private String settlementWay;
    //结算详情
    private String settleWayDetails;
    //结算员
    private String settlementOperator;
    //提交人
    private String submitter;
    //订单项
    private List<LeaseOrderItemPrintDto> leaseOrderItems;
    //全量收费项
    private List<BusinessChargeItemDto> chargeItems;
    //管理员
    private String manager;

    public LocalDateTime getPrintTime() {
        return printTime;
    }

    public void setPrintTime(LocalDateTime printTime) {
        this.printTime = printTime;
    }

    public String getReprint() {
        return reprint;
    }

    public void setReprint(String reprint) {
        this.reprint = reprint;
    }

    public String getLeaseOrderCode() {
        return leaseOrderCode;
    }

    public void setLeaseOrderCode(String leaseOrderCode) {
        this.leaseOrderCode = leaseOrderCode;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerCellphone() {
        return customerCellphone;
    }

    public void setCustomerCellphone(String customerCellphone) {
        this.customerCellphone = customerCellphone;
    }

    public LocalDate getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDate startTime) {
        this.startTime = startTime;
    }

    public LocalDate getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDate endTime) {
        this.endTime = endTime;
    }

    public String getIsRenew() {
        return isRenew;
    }

    public void setIsRenew(String isRenew) {
        this.isRenew = isRenew;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDeduction() {
        return deduction;
    }

    public void setDeduction(String deduction) {
        this.deduction = deduction;
    }

    public String getDepositDeduction() {
        return depositDeduction;
    }

    public void setDepositDeduction(String depositDeduction) {
        this.depositDeduction = depositDeduction;
    }

    public String getEarnestDeduction() {
        return earnestDeduction;
    }

    public void setEarnestDeduction(String earnestDeduction) {
        this.earnestDeduction = earnestDeduction;
    }

    public String getTransferDeduction() {
        return transferDeduction;
    }

    public void setTransferDeduction(String transferDeduction) {
        this.transferDeduction = transferDeduction;
    }

    public String getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(String payAmount) {
        this.payAmount = payAmount;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getWaitAmount() {
        return waitAmount;
    }

    public void setWaitAmount(String waitAmount) {
        this.waitAmount = waitAmount;
    }

    public String getSettlementWay() {
        return settlementWay;
    }

    public void setSettlementWay(String settlementWay) {
        this.settlementWay = settlementWay;
    }

    public String getSettleWayDetails() {
        return settleWayDetails;
    }

    public void setSettleWayDetails(String settleWayDetails) {
        this.settleWayDetails = settleWayDetails;
    }

    public String getSettlementOperator() {
        return settlementOperator;
    }

    public void setSettlementOperator(String settlementOperator) {
        this.settlementOperator = settlementOperator;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public List<LeaseOrderItemPrintDto> getLeaseOrderItems() {
        return leaseOrderItems;
    }

    public void setLeaseOrderItems(List<LeaseOrderItemPrintDto> leaseOrderItems) {
        this.leaseOrderItems = leaseOrderItems;
    }

    public List<BusinessChargeItemDto> getChargeItems() {
        return chargeItems;
    }

    public void setChargeItems(List<BusinessChargeItemDto> chargeItems) {
        this.chargeItems = chargeItems;
    }

    public String getManager() {
        return manager;
    }

    public void setManager(String manager) {
        this.manager = manager;
    }
}
