package com.dili.ia.domain.dto;

import com.dili.ia.domain.BusinessChargeItem;
import com.dili.ia.domain.MeterDetail;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 水电费 - 缴水电费Dto
 */
public class MeterDetailDto extends MeterDetail {

    /**
     * 动态收费项
     */
    private List<BusinessChargeItem> businessChargeItems;

    /**
     * 表类型
     */
    private Integer type;

    /**
     * 表类型在缴费单中的字段值
     */
    private String bizType;

    /**
     * 关键字(X费单号,表编号,客户姓名)
     */
    private String keyword;

    /**
     * 表编号
     */
    private String number;

    /**
     * 对应编号,名称(类别)
     */
    private String assetsType;


    /**
     * 对应编号,名称(表地址)
     */
    private String assetsName;

    /**
     * 水电预存余额
     */
    private Long balance;

    /**
     * 状态数组
     */
    private int[] status;

    /**
     * 使用月份的第一天,用于查询
     */
    private LocalDateTime startTime;

    /**
     * 使用月份的最后一天,用于查询
     */
    private LocalDateTime endTime;

    /**
     * 单价
     */
    private Long price;

    /**
     * 公摊费用
     */
    private Long sharedAmount;

    /**
     * 使用月份
     */
    private String UsageMonth;

    public String getUsageMonth() {
        return UsageMonth;
    }

    public void setUsageMonth(String usageMonth) {
        UsageMonth = usageMonth;
    }

    public List<BusinessChargeItem> getBusinessChargeItems() {
        return businessChargeItems;
    }

    public void setBusinessChargeItems(List<BusinessChargeItem> businessChargeItems) {
        this.businessChargeItems = businessChargeItems;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
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

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public int[] getStatus() {
        return status;
    }

    public void setStatus(int[] status) {
        this.status = status;
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

    public Long getSharedAmount() {
        return sharedAmount;
    }

    public void setSharedAmount(Long sharedAmount) {
        this.sharedAmount = sharedAmount;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getBizType() {
        return bizType;
    }

    public void setBizType(String bizType) {
        this.bizType = bizType;
    }
}