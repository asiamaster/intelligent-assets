package com.dili.ia.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.ss.domain.BaseDomain;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class StallDetailsReport extends BaseDomain {

    //摊位id
    private Long assetsId;

    //订单id
    private Long orderItemId;

    //摊位名称
    private String assetsName;

    //订单开始时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    //订单结束时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

    //第一区id
    private String firstDistrictId;

    //第一区name
    private String firstDistrictName;

    //第二区id
    private String secDistrictId;

    //第二区name
    private String secDistrictName;

    //数量
    private Integer number;

    //单位
    private String unit;

    //是否转交
    private Integer corner;

    private Integer state;

    //订单支付状态
    private Integer payState;

    //保证金应交
    private Long amount;

    //保证金已交
    private Long balance;

    //订单编号
    private String code;

    //订单金额
    private Long totalAmount;

    //已付金额
    private Long paidAmount;

    //退款状态
    private Long refundState;

    //查询时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date searchDate;

    //停租时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime stopTime;

    //是否是出租状态
    private String isLease;

    //是否是已出租
    private Integer isLeseStatus;


    //查询使用,市场id
    private Long marketId;

    //查询使用，部门编号
    private Long departmentId;
    //收费项
    List<BusinessChargeItemDto> chargeItems;

    Map<String, String> businessChargeItem;

    private String concatSql;

    public Long getAssetsId() {
        return assetsId;
    }

    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }

    public Long getOrderItemId() {
        return orderItemId;
    }

    public void setOrderItemId(Long orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
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

    public String getFirstDistrictName() {
        return firstDistrictName;
    }

    public void setFirstDistrictName(String firstDistrictName) {
        this.firstDistrictName = firstDistrictName;
    }

    public String getSecDistrictName() {
        return secDistrictName;
    }

    public void setSecDistrictName(String secDistrictName) {
        this.secDistrictName = secDistrictName;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getCorner() {
        return corner;
    }

    public void setCorner(Integer corner) {
        this.corner = corner;
    }

    public Integer getPayState() {
        return payState;
    }

    public void setPayState(Integer payState) {
        this.payState = payState;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(Long paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Long getRefundState() {
        return refundState;
    }

    public void setRefundState(Long refundState) {
        this.refundState = refundState;
    }

    public Date getSearchDate() {
        return searchDate;
    }

    public void setSearchDate(Date searchDate) {
        this.searchDate = searchDate;
    }

    public LocalDateTime getStopTime() {
        return stopTime;
    }

    public void setStopTime(LocalDateTime stopTime) {
        this.stopTime = stopTime;
    }

    public String getIsLease() {
        return isLease;
    }

    public void setIsLease(String isLease) {
        this.isLease = isLease;
    }

    public String getFirstDistrictId() {
        return firstDistrictId;
    }

    public void setFirstDistrictId(String firstDistrictId) {
        this.firstDistrictId = firstDistrictId;
    }

    public String getSecDistrictId() {
        return secDistrictId;
    }

    public void setSecDistrictId(String secDistrictId) {
        this.secDistrictId = secDistrictId;
    }


    public Integer getIsLeseStatus() {
        return isLeseStatus;
    }

    public void setIsLeseStatus(Integer isLeseStatus) {
        this.isLeseStatus = isLeseStatus;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getConcatSql() {
        return concatSql;
    }

    public void setConcatSql(String concatSql) {
        this.concatSql = concatSql;
    }

    public List<BusinessChargeItemDto> getChargeItems() {
        return chargeItems;
    }

    public void setChargeItems(List<BusinessChargeItemDto> chargeItems) {
        this.chargeItems = chargeItems;
    }

    public Map<String, String> getBusinessChargeItem() {
        return businessChargeItem;
    }

    public void setBusinessChargeItem(Map<String, String> businessChargeItem) {
        this.businessChargeItem = businessChargeItem;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}
