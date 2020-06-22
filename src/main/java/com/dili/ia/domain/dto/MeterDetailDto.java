package com.dili.ia.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 水电费 - 缴水电费Dto
 */
public class MeterDetailDto extends BaseDomain {

    /**
     * 创建日期
     */
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    /**
     * 修改日期
     */
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date modifyTime;

    /**
     * 业务编号
     */
    private String code;

    /**
     * 表ID
     */
    private Long meterId;

    /**
     * 使用月份
     */
    @JSONField(format = "yyyy-MM")
    @JsonFormat(pattern = "yyyy-MM")
    @DateTimeFormat(pattern = "yyyy-MM")
    private Date usageTime;

    /**
     * 客户ID
     */
    private Long customerId;

    /**
     * 客户姓名
     */
    private String customerName;

    /**
     * 抄表员ID
     */
    private Long recorderId;

    /**
     * 抄表员名称
     */
    private String recorderName;

    /**
     * 状态，撤销/正常
     */
    private Integer state;

    /**
     * 上次结算的数量
     */
    private Long lastAmount;

    /**
     * 本次结算的总数量
     */
    private Long thisAmount;

    /**
     * 使用量
     */
    private Long usageAmount;

    /**
     * 创建人所属于部门ID
     */
    private Long creatorDepId;

    /**
     * 备注
     */
    private String notes;

    /**
     * 提交人ID
     */
    private Long submitterId;

    /**
     * 提交人名称
     */
    private String submitter;

    /**
     * 提交时间
     */
    @JSONField(format = "yyyy-MM-dd")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date submitTime;

    /**
     * 撤回人ID
     */
    private Long withdrawOperatorId;

    /**
     * 撤回人名称
     */
    private String withdrawOperator;

    /**
     * 取消人ID
     */
    private Long cancelerId;

    /**
     * 取消人名称
     */
    private String canceler;

    /**
     * 创建操作员ID
     */
    private Long creatorId;

    /**
     * 创建人名称
     */
    private String creator;

    /**
     * 市场Id
     */
    private Long marketId;

    /**
     * 市场CODE
     */
    private String marketCode;

    /**
     * 版本控制,乐观锁
     */
    private Integer version;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public Date getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(Date usageTime) {
        this.usageTime = usageTime;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public Long getRecorderId() {
        return recorderId;
    }

    public void setRecorderId(Long recorderId) {
        this.recorderId = recorderId;
    }

    public String getRecorderName() {
        return recorderName;
    }

    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
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

    public Long getCreatorDepId() {
        return creatorDepId;
    }

    public void setCreatorDepId(Long creatorDepId) {
        this.creatorDepId = creatorDepId;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(Long submitterId) {
        this.submitterId = submitterId;
    }

    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    public Long getWithdrawOperatorId() {
        return withdrawOperatorId;
    }

    public void setWithdrawOperatorId(Long withdrawOperatorId) {
        this.withdrawOperatorId = withdrawOperatorId;
    }

    public String getWithdrawOperator() {
        return withdrawOperator;
    }

    public void setWithdrawOperator(String withdrawOperator) {
        this.withdrawOperator = withdrawOperator;
    }

    public Long getCancelerId() {
        return cancelerId;
    }

    public void setCancelerId(Long cancelerId) {
        this.cancelerId = cancelerId;
    }

    public String getCanceler() {
        return canceler;
    }

    public void setCanceler(String canceler) {
        this.canceler = canceler;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}