package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-06-12 11:35:07.
 */
@Table(name = "`meter_detail`")
public class MeterDetail extends BaseDomain {

    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    private Date modifyTime;

    /**
     * 业务编号
     */
    @Column(name = "`code`")
    private String code;

    /**
     * 表ID
     */
    @Column(name = "`meter_id`")
    private Long meterId;

    /**
     * 使用月份
     */
    @Column(name = "`usage_time`")
    private Date usageTime;

    /**
     * 客户ID
     */
    @Column(name = "`customer_id`")
    private Long customerId;

    /**
     * 客户姓名
     */
    @Column(name = "`customer_name`")
    private String customerName;

    /**
     * 抄表员ID
     */
    @Column(name = "`recorder_id`")
    private Long recorderId;

    /**
     * 抄表员名称
     */
    @Column(name = "`recorder_name`")
    private String recorderName;

    /**
     * 状态，撤销/正常
     */
    @Column(name = "`state`")
    private Integer state;

    /**
     * 上次结算的数量
     */
    @Column(name = "`last_amount`")
    private Long lastAmount;

    /**
     * 本次结算的总数量
     */
    @Column(name = "`this_amount`")
    private Long thisAmount;

    /**
     * 使用量
     */
    @Column(name = "`usage_amount`")
    private Long usageAmount;

    /**
     * 创建人所属于部门ID
     */
    @Column(name = "`creator_dep_id`")
    private Long creatorDepId;

    /**
     * 备注
     */
    @Column(name = "`notes`")
    private String notes;

    /**
     * 提交人ID
     */
    @Column(name = "`submitter_id`")
    private Long submitterId;

    /**
     * 提交人名称
     */
    @Column(name = "`submitter`")
    private String submitter;

    /**
     * 提交时间
     */
    @Column(name = "`submit_time`")
    private Date submitTime;

    /**
     * 撤回人ID
     */
    @Column(name = "`withdraw_operator_id`")
    private Long withdrawOperatorId;

    /**
     * 撤回人名称
     */
    @Column(name = "`withdraw_operator`")
    private String withdrawOperator;

    /**
     * 取消人ID
     */
    @Column(name = "`canceler_id`")
    private Long cancelerId;

    /**
     * 取消人名称
     */
    @Column(name = "`canceler`")
    private String canceler;

    /**
     * 创建操作员ID
     */
    @Column(name = "`creator_id`")
    private Long creatorId;

    /**
     * 创建人名称
     */
    @Column(name = "`creator`")
    private String creator;

    /**
     * 市场Id
     */
    @Column(name = "`market_id`")
    private Long marketId;

    /**
     * 市场CODE
     */
    @Column(name = "`market_code`")
    private String marketCode;

    /**
     * 版本控制,乐观锁
     */
    @Column(name = "`version`")
    private Integer version;

    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @FieldDef(label="业务编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @FieldDef(label="表ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMeterId() {
        return meterId;
    }

    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    @FieldDef(label="使用月份")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getUsageTime() {
        return usageTime;
    }

    public void setUsageTime(Date usageTime) {
        this.usageTime = usageTime;
    }

    @FieldDef(label="客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    @FieldDef(label="客户姓名", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    @FieldDef(label="抄表员ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getRecorderId() {
        return recorderId;
    }

    public void setRecorderId(Long recorderId) {
        this.recorderId = recorderId;
    }

    @FieldDef(label="抄表员名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getRecorderName() {
        return recorderName;
    }

    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName;
    }

    @FieldDef(label="状态，撤销/正常")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    @FieldDef(label="上次结算的数量")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(Long lastAmount) {
        this.lastAmount = lastAmount;
    }

    @FieldDef(label="本次结算的总数量")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getThisAmount() {
        return thisAmount;
    }

    public void setThisAmount(Long thisAmount) {
        this.thisAmount = thisAmount;
    }

    @FieldDef(label="使用量")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getUsageAmount() {
        return usageAmount;
    }

    public void setUsageAmount(Long usageAmount) {
        this.usageAmount = usageAmount;
    }

    @FieldDef(label="创建人所属于部门ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorDepId() {
        return creatorDepId;
    }

    public void setCreatorDepId(Long creatorDepId) {
        this.creatorDepId = creatorDepId;
    }

    @FieldDef(label="备注", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @FieldDef(label="提交人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(Long submitterId) {
        this.submitterId = submitterId;
    }

    @FieldDef(label="提交人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getSubmitter() {
        return submitter;
    }

    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    @FieldDef(label="提交时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    @FieldDef(label="撤回人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getWithdrawOperatorId() {
        return withdrawOperatorId;
    }

    public void setWithdrawOperatorId(Long withdrawOperatorId) {
        this.withdrawOperatorId = withdrawOperatorId;
    }

    @FieldDef(label="撤回人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getWithdrawOperator() {
        return withdrawOperator;
    }

    public void setWithdrawOperator(String withdrawOperator) {
        this.withdrawOperator = withdrawOperator;
    }

    @FieldDef(label="取消人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCancelerId() {
        return cancelerId;
    }

    public void setCancelerId(Long cancelerId) {
        this.cancelerId = cancelerId;
    }

    @FieldDef(label="取消人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCanceler() {
        return canceler;
    }

    public void setCanceler(String canceler) {
        this.canceler = canceler;
    }

    @FieldDef(label="创建操作员ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @FieldDef(label="创建人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @FieldDef(label="市场Id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    @FieldDef(label="市场CODE", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    @FieldDef(label="版本控制,乐观锁")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}