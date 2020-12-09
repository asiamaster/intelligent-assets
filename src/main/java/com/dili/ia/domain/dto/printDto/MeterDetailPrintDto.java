package com.dili.ia.domain.dto.printDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import java.time.LocalDateTime;

/**
 * @author:       xiaosa
 * @date:         2020/7/14
 * @version:      农批业务系统重构
 * @description:  精品停车打印
 */
public class MeterDetailPrintDto {
    //打印时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime printTime;
    //开始日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime startTime;
    //结束日期
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime endTime;

    //补打标记
    private String reprint;
    // 业务编号
    private String code;
    //业务类型
    private String businessType;
    //客户名称
    private String customerName;
    //客户电话
    private String customerCellphone;
    //备注
    private String notes;
    // 收费金额
    private String amount;
    //结算方式
    private String settlementWay;
    //结算员
    private String settlementOperator;
    //提交人
    private String submitter;
    //结算详情
    private String settleWayDetails;

    /**
     * 水电费打印特殊字段值
     */
    /**
     * 上次指数
     */
    private Long lastAmount;

    /**
     * 本期指数
     */
    private Long thisAmount;

    /**
     * 实际用量
     */
    private Long usageAmount;

    /**
     * 水电费
     */
    private Long receivable;

    /**
     * 公摊费
     */
    private Long sharedAmount;

    /**
     * 单价
     */
    private Long price;

    /**
     * 表编号
     */
    private String number;

    /**
     * 截止月份
     */
    private LocalDateTime usageTime;

    /**
     * 表类别
     */
    private String assetsType;

    /**
     * 表地址,名称(摊位地址)
     */
    private String assetsName;

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public LocalDateTime getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(LocalDateTime usageTime) {
        this.usageTime = usageTime;
    }

    public String getAssetsType() {
        return assetsType;
    }

    public void setAssetsType(String assetsType) {
        this.assetsType = assetsType;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Long getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(Long lastAmount) {
        this.lastAmount = lastAmount;
    }

    public Long getThisAmount() {
        return thisAmount;
    }

    public void setThisAmount(Long thisAmount) {
        this.thisAmount = thisAmount;
    }

    public Long getUsageAmount() {
        return usageAmount;
    }

    public void setUsageAmount(Long usageAmount) {
        this.usageAmount = usageAmount;
    }

    public Long getReceivable() {
        return receivable;
    }

    public void setReceivable(Long receivable) {
        this.receivable = receivable;
    }

    public Long getSharedAmount() {
        return sharedAmount;
    }

    public void setSharedAmount(Long sharedAmount) {
        this.sharedAmount = sharedAmount;
    }

    public String getReprint() {
        return reprint;
    }

    public void setReprint(String reprint) {
        this.reprint = reprint;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public LocalDateTime getPrintTime() {
        return printTime;
    }

    public void setPrintTime(LocalDateTime printTime) {
        this.printTime = printTime;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSettlementWay() {
        return settlementWay;
    }

    public void setSettlementWay(String settlementWay) {
        this.settlementWay = settlementWay;
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

    public String getSettleWayDetails() {
        return settleWayDetails;
    }

    public void setSettleWayDetails(String settleWayDetails) {
        this.settleWayDetails = settleWayDetails;
    }
}
