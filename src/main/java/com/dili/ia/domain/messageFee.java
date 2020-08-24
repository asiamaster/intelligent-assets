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
 * This file was generated on 2020-08-24 16:16:50.
 */
@Table(name = "`message_fee`")
public class messageFee extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 业务编号
     */
    @Column(name = "`code`")
    private String code;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "`modify_time`")
    private Date modifyTime;

    @Column(name = "`customer_id`")
    private Long customerId;

    @Column(name = "`customer_name`")
    private String customerName;

    @Column(name = "`customer_phone`")
    private String customerPhone;

    /**
     * 客户证件号
     */
    @Column(name = "`customer_certificate_number`")
    private String customerCertificateNumber;

    @Column(name = "`department_id`")
    private Long departmentId;

    @Column(name = "`department_name`")
    private String departmentName;

    /**
     * 开始时间
     */
    @Column(name = "`start_date`")
    private Date startDate;

    /**
     * 结束时间
     */
    @Column(name = "`end_date`")
    private Date endDate;

    /**
     * 收费金额
     */
    @Column(name = "`amount`")
    private Long amount;

    /**
     * 转抵扣金额
     */
    @Column(name = "`transaction_amount`")
    private Long transactionAmount;

    /**
     * 支付金额
     */
    @Column(name = "`pay_amount`")
    private Long payAmount;

    /**
     * 备注
     */
    @Column(name = "`notes`")
    private String notes;

    /**
     * 状态
     */
    @Column(name = "`status`")
    private Byte status;

    /**
     * 推送消息中心状态
     */
    @Column(name = "`sync_status`")
    private Byte syncStatus;

    /**
     * 缴费单号
     */
    @Column(name = "`payment_order_code`")
    private String paymentOrderCode;

    /**
     * 操作人
     */
    @Column(name = "`operator_id`")
    private Long operatorId;

    @Column(name = "`operator_name`")
    private String operatorName;

    /**
     * 提交人
     */
    @Column(name = "`submitter_id`")
    private Long submitterId;

    @Column(name = "`submitor_name`")
    private String submitorName;

    /**
     * 创建人
     */
    @Column(name = "`creator_id`")
    private Long creatorId;

    @Column(name = "`creator_name`")
    private String creatorName;

    /**
     * 作废操作人
     */
    @Column(name = "`canceler_id`")
    private Long cancelerId;

    @Column(name = "`canceler_name`")
    private String cancelerName;

    /**
     * 作废原因
     */
    @Column(name = "`canceler_notes`")
    private String cancelerNotes;

    /**
     * 市场
     */
    @Column(name = "`market_id`")
    private Long marketId;

    @Column(name = "`market_code`")
    private String marketCode;

    /**
     * 版本号
     */
    @Column(name = "`version`")
    private Integer version;

    /**
     * @return id
     */
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
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
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
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
     * 获取更新时间
     *
     * @return modify_time - 更新时间
     */
    @FieldDef(label="更新时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置更新时间
     *
     * @param modifyTime 更新时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * @return customer_id
     */
    @FieldDef(label="customerId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * @param customerId
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * @return customer_name
     */
    @FieldDef(label="customerName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerName() {
        return customerName;
    }

    /**
     * @param customerName
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * @return customer_phone
     */
    @FieldDef(label="customerPhone", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerPhone() {
        return customerPhone;
    }

    /**
     * @param customerPhone
     */
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }

    /**
     * 获取客户证件号
     *
     * @return customer_certificate_number - 客户证件号
     */
    @FieldDef(label="客户证件号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerCertificateNumber() {
        return customerCertificateNumber;
    }

    /**
     * 设置客户证件号
     *
     * @param customerCertificateNumber 客户证件号
     */
    public void setCustomerCertificateNumber(String customerCertificateNumber) {
        this.customerCertificateNumber = customerCertificateNumber;
    }

    /**
     * @return department_id
     */
    @FieldDef(label="departmentId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * @param departmentId
     */
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * @return department_name
     */
    @FieldDef(label="departmentName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * @param departmentName
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * 获取开始时间
     *
     * @return start_date - 开始时间
     */
    @FieldDef(label="开始时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getStartDate() {
        return startDate;
    }

    /**
     * 设置开始时间
     *
     * @param startDate 开始时间
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * 获取结束时间
     *
     * @return end_date - 结束时间
     */
    @FieldDef(label="结束时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getEndDate() {
        return endDate;
    }

    /**
     * 设置结束时间
     *
     * @param endDate 结束时间
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * 获取收费金额
     *
     * @return amount - 收费金额
     */
    @FieldDef(label="收费金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAmount() {
        return amount;
    }

    /**
     * 设置收费金额
     *
     * @param amount 收费金额
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * 获取转抵扣金额
     *
     * @return transaction_amount - 转抵扣金额
     */
    @FieldDef(label="转抵扣金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * 设置转抵扣金额
     *
     * @param transactionAmount 转抵扣金额
     */
    public void setTransactionAmount(Long transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    /**
     * 获取支付金额
     *
     * @return pay_amount - 支付金额
     */
    @FieldDef(label="支付金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getPayAmount() {
        return payAmount;
    }

    /**
     * 设置支付金额
     *
     * @param payAmount 支付金额
     */
    public void setPayAmount(Long payAmount) {
        this.payAmount = payAmount;
    }

    /**
     * 获取备注
     *
     * @return notes - 备注
     */
    @FieldDef(label="备注", maxLength = 255)
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
     * 获取状态
     *
     * @return status - 状态
     */
    @FieldDef(label="状态")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态
     *
     * @param status 状态
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取推送消息中心状态
     *
     * @return sync_status - 推送消息中心状态
     */
    @FieldDef(label="推送消息中心状态")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Byte getSyncStatus() {
        return syncStatus;
    }

    /**
     * 设置推送消息中心状态
     *
     * @param syncStatus 推送消息中心状态
     */
    public void setSyncStatus(Byte syncStatus) {
        this.syncStatus = syncStatus;
    }

    /**
     * 获取缴费单号
     *
     * @return payment_order_code - 缴费单号
     */
    @FieldDef(label="缴费单号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPaymentOrderCode() {
        return paymentOrderCode;
    }

    /**
     * 设置缴费单号
     *
     * @param paymentOrderCode 缴费单号
     */
    public void setPaymentOrderCode(String paymentOrderCode) {
        this.paymentOrderCode = paymentOrderCode;
    }

    /**
     * 获取操作人
     *
     * @return operator_id - 操作人
     */
    @FieldDef(label="操作人")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * 设置操作人
     *
     * @param operatorId 操作人
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * @return operator_name
     */
    @FieldDef(label="operatorName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operatorName
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * 获取提交人
     *
     * @return submitter_id - 提交人
     */
    @FieldDef(label="提交人")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getSubmitterId() {
        return submitterId;
    }

    /**
     * 设置提交人
     *
     * @param submitterId 提交人
     */
    public void setSubmitterId(Long submitterId) {
        this.submitterId = submitterId;
    }

    /**
     * @return submitor_name
     */
    @FieldDef(label="submitorName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getSubmitorName() {
        return submitorName;
    }

    /**
     * @param submitorName
     */
    public void setSubmitorName(String submitorName) {
        this.submitorName = submitorName;
    }

    /**
     * 获取创建人
     *
     * @return creator_id - 创建人
     */
    @FieldDef(label="创建人")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * 设置创建人
     *
     * @param creatorId 创建人
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * @return creator_name
     */
    @FieldDef(label="creatorName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCreatorName() {
        return creatorName;
    }

    /**
     * @param creatorName
     */
    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    /**
     * 获取作废操作人
     *
     * @return canceler_id - 作废操作人
     */
    @FieldDef(label="作废操作人")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCancelerId() {
        return cancelerId;
    }

    /**
     * 设置作废操作人
     *
     * @param cancelerId 作废操作人
     */
    public void setCancelerId(Long cancelerId) {
        this.cancelerId = cancelerId;
    }

    /**
     * @return canceler_name
     */
    @FieldDef(label="cancelerName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCancelerName() {
        return cancelerName;
    }

    /**
     * @param cancelerName
     */
    public void setCancelerName(String cancelerName) {
        this.cancelerName = cancelerName;
    }

    /**
     * 获取作废原因
     *
     * @return canceler_notes - 作废原因
     */
    @FieldDef(label="作废原因", maxLength = 255)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCancelerNotes() {
        return cancelerNotes;
    }

    /**
     * 设置作废原因
     *
     * @param cancelerNotes 作废原因
     */
    public void setCancelerNotes(String cancelerNotes) {
        this.cancelerNotes = cancelerNotes;
    }

    /**
     * 获取市场
     *
     * @return market_id - 市场
     */
    @FieldDef(label="市场")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    /**
     * 设置市场
     *
     * @param marketId 市场
     */
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    /**
     * @return market_code
     */
    @FieldDef(label="marketCode", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getMarketCode() {
        return marketCode;
    }

    /**
     * @param marketCode
     */
    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    /**
     * 获取版本号
     *
     * @return version - 版本号
     */
    @FieldDef(label="版本号")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置版本号
     *
     * @param version 版本号
     */
    public void setVersion(Integer version) {
        this.version = version;
    }
}