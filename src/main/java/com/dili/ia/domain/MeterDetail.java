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
    /**
     * id
     */
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

    /**
     * 获取id
     *
     * @return id - id
     */
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取业务编号
     *
     * @return code - 业务编号
     */
    @FieldDef(label="业务编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置业务编号
     *
     * @param code 业务编号
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取表ID
     *
     * @return meter_id - 表ID
     */
    @FieldDef(label="表ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMeterId() {
        return meterId;
    }

    /**
     * 设置表ID
     *
     * @param meterId 表ID
     */
    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    /**
     * 获取使用月份
     *
     * @return usage_time - 使用月份
     */
    @FieldDef(label="使用月份")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getUsageTime() {
        return usageTime;
    }

    /**
     * 设置使用月份
     *
     * @param usageTime 使用月份
     */
    public void setUsageTime(Date usageTime) {
        this.usageTime = usageTime;
    }

    /**
     * 获取客户ID
     *
     * @return customer_id - 客户ID
     */
    @FieldDef(label="客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * 设置客户ID
     *
     * @param customerId 客户ID
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * 获取客户姓名
     *
     * @return customer_name - 客户姓名
     */
    @FieldDef(label="客户姓名", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置客户姓名
     *
     * @param customerName 客户姓名
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取抄表员ID
     *
     * @return recorder_id - 抄表员ID
     */
    @FieldDef(label="抄表员ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getRecorderId() {
        return recorderId;
    }

    /**
     * 设置抄表员ID
     *
     * @param recorderId 抄表员ID
     */
    public void setRecorderId(Long recorderId) {
        this.recorderId = recorderId;
    }

    /**
     * 获取抄表员名称
     *
     * @return recorder_name - 抄表员名称
     */
    @FieldDef(label="抄表员名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getRecorderName() {
        return recorderName;
    }

    /**
     * 设置抄表员名称
     *
     * @param recorderName 抄表员名称
     */
    public void setRecorderName(String recorderName) {
        this.recorderName = recorderName;
    }

    /**
     * 获取状态，撤销/正常
     *
     * @return state - 状态，撤销/正常
     */
    @FieldDef(label="状态，撤销/正常")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态，撤销/正常
     *
     * @param state 状态，撤销/正常
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取上次结算的数量
     *
     * @return last_amount - 上次结算的数量
     */
    @FieldDef(label="上次结算的数量")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getLastAmount() {
        return lastAmount;
    }

    /**
     * 设置上次结算的数量
     *
     * @param lastAmount 上次结算的数量
     */
    public void setLastAmount(Long lastAmount) {
        this.lastAmount = lastAmount;
    }

    /**
     * 获取本次结算的总数量
     *
     * @return this_amount - 本次结算的总数量
     */
    @FieldDef(label="本次结算的总数量")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getThisAmount() {
        return thisAmount;
    }

    /**
     * 设置本次结算的总数量
     *
     * @param thisAmount 本次结算的总数量
     */
    public void setThisAmount(Long thisAmount) {
        this.thisAmount = thisAmount;
    }

    /**
     * 获取使用量
     *
     * @return usage_amount - 使用量
     */
    @FieldDef(label="使用量")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getUsageAmount() {
        return usageAmount;
    }

    /**
     * 设置使用量
     *
     * @param usageAmount 使用量
     */
    public void setUsageAmount(Long usageAmount) {
        this.usageAmount = usageAmount;
    }

    /**
     * 获取创建人所属于部门ID
     *
     * @return creator_dep_id - 创建人所属于部门ID
     */
    @FieldDef(label="创建人所属于部门ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorDepId() {
        return creatorDepId;
    }

    /**
     * 设置创建人所属于部门ID
     *
     * @param creatorDepId 创建人所属于部门ID
     */
    public void setCreatorDepId(Long creatorDepId) {
        this.creatorDepId = creatorDepId;
    }

    /**
     * 获取备注
     *
     * @return notes - 备注
     */
    @FieldDef(label="备注", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getNotes() {
        return notes;
    }

    /**
     * 设置备注
     *
     * @param notes 备注
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * 获取提交人ID
     *
     * @return submitter_id - 提交人ID
     */
    @FieldDef(label="提交人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getSubmitterId() {
        return submitterId;
    }

    /**
     * 设置提交人ID
     *
     * @param submitterId 提交人ID
     */
    public void setSubmitterId(Long submitterId) {
        this.submitterId = submitterId;
    }

    /**
     * 获取提交人名称
     *
     * @return submitter - 提交人名称
     */
    @FieldDef(label="提交人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getSubmitter() {
        return submitter;
    }

    /**
     * 设置提交人名称
     *
     * @param submitter 提交人名称
     */
    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    /**
     * 获取提交时间
     *
     * @return submit_time - 提交时间
     */
    @FieldDef(label="提交时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getSubmitTime() {
        return submitTime;
    }

    /**
     * 设置提交时间
     *
     * @param submitTime 提交时间
     */
    public void setSubmitTime(Date submitTime) {
        this.submitTime = submitTime;
    }

    /**
     * 获取撤回人ID
     *
     * @return withdraw_operator_id - 撤回人ID
     */
    @FieldDef(label="撤回人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getWithdrawOperatorId() {
        return withdrawOperatorId;
    }

    /**
     * 设置撤回人ID
     *
     * @param withdrawOperatorId 撤回人ID
     */
    public void setWithdrawOperatorId(Long withdrawOperatorId) {
        this.withdrawOperatorId = withdrawOperatorId;
    }

    /**
     * 获取撤回人名称
     *
     * @return withdraw_operator - 撤回人名称
     */
    @FieldDef(label="撤回人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getWithdrawOperator() {
        return withdrawOperator;
    }

    /**
     * 设置撤回人名称
     *
     * @param withdrawOperator 撤回人名称
     */
    public void setWithdrawOperator(String withdrawOperator) {
        this.withdrawOperator = withdrawOperator;
    }

    /**
     * 获取取消人ID
     *
     * @return canceler_id - 取消人ID
     */
    @FieldDef(label="取消人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCancelerId() {
        return cancelerId;
    }

    /**
     * 设置取消人ID
     *
     * @param cancelerId 取消人ID
     */
    public void setCancelerId(Long cancelerId) {
        this.cancelerId = cancelerId;
    }

    /**
     * 获取取消人名称
     *
     * @return canceler - 取消人名称
     */
    @FieldDef(label="取消人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCanceler() {
        return canceler;
    }

    /**
     * 设置取消人名称
     *
     * @param canceler 取消人名称
     */
    public void setCanceler(String canceler) {
        this.canceler = canceler;
    }

    /**
     * 获取创建操作员ID
     *
     * @return creator_id - 创建操作员ID
     */
    @FieldDef(label="创建操作员ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * 设置创建操作员ID
     *
     * @param creatorId 创建操作员ID
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * 获取创建人名称
     *
     * @return creator - 创建人名称
     */
    @FieldDef(label="创建人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人名称
     *
     * @param creator 创建人名称
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取市场Id
     *
     * @return market_id - 市场Id
     */
    @FieldDef(label="市场Id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    /**
     * 设置市场Id
     *
     * @param marketId 市场Id
     */
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    /**
     * 获取市场CODE
     *
     * @return market_code - 市场CODE
     */
    @FieldDef(label="市场CODE", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getMarketCode() {
        return marketCode;
    }

    /**
     * 设置市场CODE
     *
     * @param marketCode 市场CODE
     */
    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    /**
     * 获取版本控制,乐观锁
     *
     * @return version - 版本控制,乐观锁
     */
    @FieldDef(label="版本控制,乐观锁")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置版本控制,乐观锁
     *
     * @param version 版本控制,乐观锁
     */
    public void setVersion(Integer version) {
        this.version = version;
    }
}