package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 定金的业务单
 * This file was generated on 2020-10-28 16:31:00.
 */
@Table(name = "`earnest_order`")
public class EarnestOrder extends BaseDomain {
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`create_time`")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`modify_time`")
    private LocalDateTime modifyTime;

    /**
     * 客户ID
     */
    @Column(name = "`customer_id`")
    private Long customerId;

    /**
     * 客户名称
     */
    @Column(name = "`customer_name`")
    private String customerName;

    /**
     * 客户电话
     */
    @Column(name = "`customer_cellphone`")
    private String customerCellphone;

    /**
     * 客户证件号码
     */
    @Column(name = "`certificate_number`")
    private String certificateNumber;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`start_time`")
    private LocalDateTime startTime;

    /**
     * 截止时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`end_time`")
    private LocalDateTime endTime;

    /**
     * 业务单业务部门ID
     */
    @Column(name = "`department_id`")
    private Long departmentId;

    /**
     * 业务单业务部门名称
     */
    @Column(name = "`department_name`")
    private String departmentName;

    /**
     * 状态：1-已创建，2-已取消，3-已提交，4-已缴费
     */
    @Column(name = "`state`")
    private Integer state;

    /**
     * 资产类型，包含摊位，冷库，公寓等
     */
    @Column(name = "`assets_type`")
    private Integer assetsType;

    /**
     * 金额
     */
    @Column(name = "`amount`")
    private Long amount;

    /**
     * 定金业务单编号
     */
    @Column(name = "`code`")
    private String code;

    /**
     * 创建操作员ID
     */
    @Column(name = "`creator_id`")
    private Long creatorId;

    /**
     * 创建操作员名称
     */
    @Column(name = "`creator`")
    private String creator;

    /**
     * 提交人ID
     */
    @Column(name = "`submitter_id`")
    private Long submitterId;

    /**
     * 提交人名字
     */
    @Column(name = "`submitter`")
    private String submitter;

    /**
     * 提交时间
     */
    @Column(name = "`sub_date`")
    private LocalDateTime subDate;

    /**
     * 备注
     */
    @Column(name = "`notes`")
    private String notes;

    /**
     * 撤回人ID
     */
    @Column(name = "`withdraw_operator_id`")
    private Long withdrawOperatorId;

    /**
     * 撤回人名字
     */
    @Column(name = "`withdraw_operator`")
    private String withdrawOperator;

    /**
     * 取消人ID
     */
    @Column(name = "`canceler_id`")
    private Long cancelerId;

    /**
     * 取消人名字
     */
    @Column(name = "`canceler`")
    private String canceler;

    /**
     * 作废人ID
     */
    @Column(name = "`invalid_operator_id`")
    private Long invalidOperatorId;

    /**
     * 作废人名字
     */
    @Column(name = "`invalid_operator`")
    private String invalidOperator;

    /**
     * 作废时间
     */
    @Column(name = "`invalid_time`")
    private LocalDateTime invalidTime;

    /**
     * 作废原因
     */
    @Column(name = "`invalid_reason`")
    private String invalidReason;

    /**
     * 市场Id
     */
    @Column(name = "`market_id`")
    private Long marketId;

    /**
     * 版本控制,乐观锁
     */
    @Version
    @Column(name = "`version`")
    private Long version;

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
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
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
     * 获取客户名称
     *
     * @return customer_name - 客户名称
     */
    @FieldDef(label="客户名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置客户名称
     *
     * @param customerName 客户名称
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取客户电话
     *
     * @return customer_cellphone - 客户电话
     */
    @FieldDef(label="客户电话", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerCellphone() {
        return customerCellphone;
    }

    /**
     * 设置客户电话
     *
     * @param customerCellphone 客户电话
     */
    public void setCustomerCellphone(String customerCellphone) {
        this.customerCellphone = customerCellphone;
    }

    /**
     * 获取客户证件号码
     *
     * @return certificate_number - 客户证件号码
     */
    @FieldDef(label="客户证件号码", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCertificateNumber() {
        return certificateNumber;
    }

    /**
     * 设置客户证件号码
     *
     * @param certificateNumber 客户证件号码
     */
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    /**
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    @FieldDef(label="开始时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取截止时间
     *
     * @return end_time - 截止时间
     */
    @FieldDef(label="截止时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * 设置截止时间
     *
     * @param endTime 截止时间
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取业务单业务部门ID
     *
     * @return department_id - 业务单业务部门ID
     */
    @FieldDef(label="业务单业务部门ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * 设置业务单业务部门ID
     *
     * @param departmentId 业务单业务部门ID
     */
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * 获取业务单业务部门名称
     *
     * @return department_name - 业务单业务部门名称
     */
    @FieldDef(label="业务单业务部门名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * 设置业务单业务部门名称
     *
     * @param departmentName 业务单业务部门名称
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * 获取状态：1-已创建，2-已取消，3-已提交，4-已缴费
     *
     * @return state - 状态：1-已创建，2-已取消，3-已提交，4-已缴费
     */
    @FieldDef(label="状态：1-已创建，2-已取消，3-已提交，4-已缴费")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态：1-已创建，2-已取消，3-已提交，4-已缴费
     *
     * @param state 状态：1-已创建，2-已取消，3-已提交，4-已缴费
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取资产类型，包含摊位，冷库，公寓等
     *
     * @return assets_type - 资产类型，包含摊位，冷库，公寓等
     */
    @FieldDef(label="资产类型，包含摊位，冷库，公寓等")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getAssetsType() {
        return assetsType;
    }

    /**
     * 设置资产类型，包含摊位，冷库，公寓等
     *
     * @param assetsType 资产类型，包含摊位，冷库，公寓等
     */
    public void setAssetsType(Integer assetsType) {
        this.assetsType = assetsType;
    }

    /**
     * 获取金额
     *
     * @return amount - 金额
     */
    @FieldDef(label="金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAmount() {
        return amount;
    }

    /**
     * 设置金额
     *
     * @param amount 金额
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * 获取定金业务单编号
     *
     * @return code - 定金业务单编号
     */
    @FieldDef(label="定金业务单编号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置定金业务单编号
     *
     * @param code 定金业务单编号
     */
    public void setCode(String code) {
        this.code = code;
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
     * 获取创建操作员名称
     *
     * @return creator - 创建操作员名称
     */
    @FieldDef(label="创建操作员名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建操作员名称
     *
     * @param creator 创建操作员名称
     */
    public void setCreator(String creator) {
        this.creator = creator;
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
     * 获取提交人名字
     *
     * @return submitter - 提交人名字
     */
    @FieldDef(label="提交人名字", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getSubmitter() {
        return submitter;
    }

    /**
     * 设置提交人名字
     *
     * @param submitter 提交人名字
     */
    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    /**
     * 获取提交时间
     *
     * @return sub_date - 提交时间
     */
    @FieldDef(label="提交时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getSubDate() {
        return subDate;
    }

    /**
     * 设置提交时间
     *
     * @param subDate 提交时间
     */
    public void setSubDate(LocalDateTime subDate) {
        this.subDate = subDate;
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
     * 获取撤回人名字
     *
     * @return withdraw_operator - 撤回人名字
     */
    @FieldDef(label="撤回人名字", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getWithdrawOperator() {
        return withdrawOperator;
    }

    /**
     * 设置撤回人名字
     *
     * @param withdrawOperator 撤回人名字
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
     * 获取取消人名字
     *
     * @return canceler - 取消人名字
     */
    @FieldDef(label="取消人名字", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCanceler() {
        return canceler;
    }

    /**
     * 设置取消人名字
     *
     * @param canceler 取消人名字
     */
    public void setCanceler(String canceler) {
        this.canceler = canceler;
    }

    /**
     * 获取作废人ID
     *
     * @return invalid_operator_id - 作废人ID
     */
    @FieldDef(label="作废人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getInvalidOperatorId() {
        return invalidOperatorId;
    }

    /**
     * 设置作废人ID
     *
     * @param invalidOperatorId 作废人ID
     */
    public void setInvalidOperatorId(Long invalidOperatorId) {
        this.invalidOperatorId = invalidOperatorId;
    }

    /**
     * 获取作废人名字
     *
     * @return invalid_operator - 作废人名字
     */
    @FieldDef(label="作废人名字", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getInvalidOperator() {
        return invalidOperator;
    }

    /**
     * 设置作废人名字
     *
     * @param invalidOperator 作废人名字
     */
    public void setInvalidOperator(String invalidOperator) {
        this.invalidOperator = invalidOperator;
    }

    /**
     * 获取作废时间
     *
     * @return invalid_time - 作废时间
     */
    @FieldDef(label="作废时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getInvalidTime() {
        return invalidTime;
    }

    /**
     * 设置作废时间
     *
     * @param invalidTime 作废时间
     */
    public void setInvalidTime(LocalDateTime invalidTime) {
        this.invalidTime = invalidTime;
    }

    /**
     * 获取作废原因
     *
     * @return invalid_reason - 作废原因
     */
    @FieldDef(label="作废原因", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getInvalidReason() {
        return invalidReason;
    }

    /**
     * 设置作废原因
     *
     * @param invalidReason 作废原因
     */
    public void setInvalidReason(String invalidReason) {
        this.invalidReason = invalidReason;
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
     * 获取版本控制,乐观锁
     *
     * @return version - 版本控制,乐观锁
     */
    @FieldDef(label="版本控制,乐观锁")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getVersion() {
        return version;
    }

    /**
     * 设置版本控制,乐观锁
     *
     * @param version 版本控制,乐观锁
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}