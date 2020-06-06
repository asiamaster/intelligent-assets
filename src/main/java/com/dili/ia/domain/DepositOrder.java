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
 * This file was generated on 2020-06-06 15:34:22.
 */
@Table(name = "`deposit_order`")
public class DepositOrder extends BaseDomain {
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
     * 客户证件号
     */
    @Column(name = "`certificate_number`")
    private String certificateNumber;

    /**
     * 客户电话
     */
    @Column(name = "`customer_cellphone`")
    private String customerCellphone;

    /**
     * 业务所属部门ID
     */
    @Column(name = "`department_id`")
    private Long departmentId;

    /**
     * 业务所属部门名称
     */
    @Column(name = "`department_name`")
    private String departmentName;

    /**
     * 保证金类型code，来源数据字典
     */
    @Column(name = "`type_code`")
    private String typeCode;

    /**
     * 保证金类型名称
     */
    @Column(name = "`type_name`")
    private String typeName;

    /**
     * 资产类型code; booth,location,lodging,other
     */
    @Column(name = "`assets_type`")
    private String assetsType;

    /**
     * 对应编号ID
     */
    @Column(name = "`assets_id`")
    private Long assetsId;

    /**
     * 对应编号，名称
     */
    @Column(name = "`assets_name`")
    private String assetsName;

    /**
     * 保证金金额
     */
    @Column(name = "`amount`")
    private Long amount;

    /**
     * 退款金额，用于多次退款记录
     */
    @Column(name = "`refund_amount`")
    private Long refundAmount;

    /**
     * 扣款金额，退款时的扣款金额或者罚款金额
     */
    @Column(name = "`chargeback_amount`")
    private Long chargebackAmount;

    /**
     * 备注信息
     */
    @Column(name = "`notes`")
    private String notes;

    /**
     * （1：已创建 2：已取消 3：已提交 4：已交费5：已退款）
     */
    @Column(name = "`state`")
    private Integer state;

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
    private Long version;

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
    @FieldDef(label="业务编号", maxLength = 30)
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
     * 获取客户证件号
     *
     * @return certificate_number - 客户证件号
     */
    @FieldDef(label="客户证件号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCertificateNumber() {
        return certificateNumber;
    }

    /**
     * 设置客户证件号
     *
     * @param certificateNumber 客户证件号
     */
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
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
     * 获取业务所属部门ID
     *
     * @return department_id - 业务所属部门ID
     */
    @FieldDef(label="业务所属部门ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * 设置业务所属部门ID
     *
     * @param departmentId 业务所属部门ID
     */
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * 获取业务所属部门名称
     *
     * @return department_name - 业务所属部门名称
     */
    @FieldDef(label="业务所属部门名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * 设置业务所属部门名称
     *
     * @param departmentName 业务所属部门名称
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * 获取保证金类型code，来源数据字典
     *
     * @return type_code - 保证金类型code，来源数据字典
     */
    @FieldDef(label="保证金类型code，来源数据字典", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * 设置保证金类型code，来源数据字典
     *
     * @param typeCode 保证金类型code，来源数据字典
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * 获取保证金类型名称
     *
     * @return type_name - 保证金类型名称
     */
    @FieldDef(label="保证金类型名称", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getTypeName() {
        return typeName;
    }

    /**
     * 设置保证金类型名称
     *
     * @param typeName 保证金类型名称
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 获取资产类型code; booth,location,lodging,other
     *
     * @return assets_type - 资产类型code; booth,location,lodging,other
     */
    @FieldDef(label="资产类型code; booth,location,lodging,other", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getAssetsType() {
        return assetsType;
    }

    /**
     * 设置资产类型code; booth,location,lodging,other
     *
     * @param assetsType 资产类型code; booth,location,lodging,other
     */
    public void setAssetsType(String assetsType) {
        this.assetsType = assetsType;
    }

    /**
     * 获取对应编号ID
     *
     * @return assets_id - 对应编号ID
     */
    @FieldDef(label="对应编号ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAssetsId() {
        return assetsId;
    }

    /**
     * 设置对应编号ID
     *
     * @param assetsId 对应编号ID
     */
    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }

    /**
     * 获取对应编号，名称
     *
     * @return assets_name - 对应编号，名称
     */
    @FieldDef(label="对应编号，名称", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getAssetsName() {
        return assetsName;
    }

    /**
     * 设置对应编号，名称
     *
     * @param assetsName 对应编号，名称
     */
    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    /**
     * 获取保证金金额
     *
     * @return amount - 保证金金额
     */
    @FieldDef(label="保证金金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAmount() {
        return amount;
    }

    /**
     * 设置保证金金额
     *
     * @param amount 保证金金额
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * 获取退款金额，用于多次退款记录
     *
     * @return refund_amount - 退款金额，用于多次退款记录
     */
    @FieldDef(label="退款金额，用于多次退款记录")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getRefundAmount() {
        return refundAmount;
    }

    /**
     * 设置退款金额，用于多次退款记录
     *
     * @param refundAmount 退款金额，用于多次退款记录
     */
    public void setRefundAmount(Long refundAmount) {
        this.refundAmount = refundAmount;
    }

    /**
     * 获取扣款金额，退款时的扣款金额或者罚款金额
     *
     * @return chargeback_amount - 扣款金额，退款时的扣款金额或者罚款金额
     */
    @FieldDef(label="扣款金额，退款时的扣款金额或者罚款金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getChargebackAmount() {
        return chargebackAmount;
    }

    /**
     * 设置扣款金额，退款时的扣款金额或者罚款金额
     *
     * @param chargebackAmount 扣款金额，退款时的扣款金额或者罚款金额
     */
    public void setChargebackAmount(Long chargebackAmount) {
        this.chargebackAmount = chargebackAmount;
    }

    /**
     * 获取备注信息
     *
     * @return notes - 备注信息
     */
    @FieldDef(label="备注信息", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getNotes() {
        return notes;
    }

    /**
     * 设置备注信息
     *
     * @param notes 备注信息
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * 获取（1：已创建 2：已取消 3：已提交 4：已交费5：已退款）
     *
     * @return state - （1：已创建 2：已取消 3：已提交 4：已交费5：已退款）
     */
    @FieldDef(label="（1：已创建 2：已取消 3：已提交 4：已交费5：已退款）")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getState() {
        return state;
    }

    /**
     * 设置（1：已创建 2：已取消 3：已提交 4：已交费5：已退款）
     *
     * @param state （1：已创建 2：已取消 3：已提交 4：已交费5：已退款）
     */
    public void setState(Integer state) {
        this.state = state;
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