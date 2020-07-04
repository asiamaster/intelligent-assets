package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 退款单
 * This file was generated on 2020-07-03 14:18:29.
 */
@Table(name = "`refund_order`")
public class RefundOrder extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 退款编号
     */
    @Column(name = "`code`")
    private String code;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`create_time`")
    private LocalDateTime createTime;

    //修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`modify_time`")
    private LocalDateTime modifyTime;

    /**
     * 业务类型
     */
    @Column(name = "`biz_type`")
    private String bizType;

    /**
     * 业务订单项ID
     */
    @Column(name = "`business_item_id`")
    private Long businessItemId;

    /**
     * 业务订单项CODE
     */
    @Column(name = "`business_item_code`")
    private String businessItemCode;

    /**
     * 业务订单ID
     */
    @Column(name = "`business_id`")
    private Long businessId;

    /**
     * 业务单编号
     */
    @Column(name = "`business_code`")
    private String businessCode;

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
     * 状态（1：已创建 2：已取消 3：已提交 4：已退款）
     */
    @Column(name = "`state`")
    private Integer state;

    /**
     * 金额
     */
    @Column(name = "`total_refund_amount`")
    private Long totalRefundAmount;

    /**
     * 提交时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`submit_time`")
    private LocalDateTime submitTime;

    /**
     * 退款结算单号
     */
    @Column(name = "`settlement_code`")
    private String settlementCode;

    /**
     * 退款方式
     */
    @Column(name = "`refund_type`")
    private Integer refundType;

    /**
     * 开户行
     */
    @Column(name = "`bank`")
    private String bank;

    /**
     * 银行卡号
     */
    @Column(name = "`bank_card_no`")
    private String bankCardNo;

    /**
     * 创建人ID
     */
    @Column(name = "`creator_id`")
    private Long creatorId;

    /**
     * 创建人
     */
    @Column(name = "`creator`")
    private String creator;

    /**
     * 提交人ID
     */
    @Column(name = "`submitter_id`")
    private Long submitterId;

    /**
     * 提交人
     */
    @Column(name = "`submitter`")
    private String submitter;

    /**
     * 收款人ID
     */
    @Column(name = "`payee_id`")
    private Long payeeId;

    /**
     * 收款人客户名称
     */
    @Column(name = "`payee`")
    private String payee;

    /**
     * 收款人收款金额
     */
    @Column(name = "`payee_amount`")
    private Long payeeAmount;

    /**
     * 撤回操作人ID
     */
    @Column(name = "`withdraw_operator_id`")
    private Long withdrawOperatorId;

    /**
     * 撤回操作人
     */
    @Column(name = "`withdraw_operator`")
    private String withdrawOperator;

    /**
     * 取消操作人ID
     */
    @Column(name = "`canceler_id`")
    private Long cancelerId;

    /**
     * 取消人
     */
    @Column(name = "`canceler`")
    private String canceler;

    /**
     * 退款原因
     */
    @Column(name = "`refund_reason`")
    private String refundReason;

    /**
     * 退款时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`refund_time`")
    private Date refundTime;

    @Column(name = "`refund_operator_id`")
    private Long refundOperatorId;

    /**
     * 退款操作人
     */
    @Column(name = "`refund_operator`")
    private String refundOperator;

    /**
     * 营业执照状态
     */
    @Column(name = "`license_state`")
    private Integer licenseState;

    /**
     * 退款单对应的业务部门ID
     */
    @Column(name = "`department_id`")
    private Long departmentId;

    /**
     * 退款单对应的部门名称
     */
    @Column(name = "`department_name`")
    private String departmentName;

    /**
     * 市场ID
     */
    @Column(name = "`market_id`")
    private Long marketId;

    /**
     * 市场code
     */
    @Column(name = "`market_code`")
    private String marketCode;

    /**
     * 乐观锁，版本号
     */
    @Version
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
     * 获取退款编号
     *
     * @return code - 退款编号
     */
    @FieldDef(label="退款编号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置退款编号
     *
     * @param code 退款编号
     */
    public void setCode(String code) {
        this.code = code;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取业务类型
     *
     * @return biz_type - 业务类型
     */
    @FieldDef(label="业务类型", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getBizType() {
        return bizType;
    }

    /**
     * 设置业务类型
     *
     * @param bizType 业务类型
     */
    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    /**
     * 获取业务订单项ID
     *
     * @return business_item_id - 业务订单项ID
     */
    @FieldDef(label="业务订单项ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getBusinessItemId() {
        return businessItemId;
    }

    /**
     * 设置业务订单项ID
     *
     * @param businessItemId 业务订单项ID
     */
    public void setBusinessItemId(Long businessItemId) {
        this.businessItemId = businessItemId;
    }

    /**
     * 获取业务订单项CODE
     *
     * @return business_item_code - 业务订单项CODE
     */
    @FieldDef(label="业务订单项CODE", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getBusinessItemCode() {
        return businessItemCode;
    }

    /**
     * 设置业务订单项CODE
     *
     * @param businessItemCode 业务订单项CODE
     */
    public void setBusinessItemCode(String businessItemCode) {
        this.businessItemCode = businessItemCode;
    }

    /**
     * 获取业务订单ID
     *
     * @return business_id - 业务订单ID
     */
    @FieldDef(label="业务订单ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getBusinessId() {
        return businessId;
    }

    /**
     * 设置业务订单ID
     *
     * @param businessId 业务订单ID
     */
    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    /**
     * 获取业务单编号
     *
     * @return business_code - 业务单编号
     */
    @FieldDef(label="业务单编号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getBusinessCode() {
        return businessCode;
    }

    /**
     * 设置业务单编号
     *
     * @param businessCode 业务单编号
     */
    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
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
     * 获取状态（1：已创建 2：已取消 3：已提交 4：已退款）
     *
     * @return state - 状态（1：已创建 2：已取消 3：已提交 4：已退款）
     */
    @FieldDef(label="状态（1：已创建 2：已取消 3：已提交 4：已退款）")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态（1：已创建 2：已取消 3：已提交 4：已退款）
     *
     * @param state 状态（1：已创建 2：已取消 3：已提交 4：已退款）
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取金额
     *
     * @return total_refund_amount - 金额
     */
    @FieldDef(label="金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getTotalRefundAmount() {
        return totalRefundAmount;
    }

    /**
     * 设置金额
     *
     * @param totalRefundAmount 金额
     */
    public void setTotalRefundAmount(Long totalRefundAmount) {
        this.totalRefundAmount = totalRefundAmount;
    }

    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }

    /**
     * 获取退款结算单号
     *
     * @return settlement_code - 退款结算单号
     */
    @FieldDef(label="退款结算单号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getSettlementCode() {
        return settlementCode;
    }

    /**
     * 设置退款结算单号
     *
     * @param settlementCode 退款结算单号
     */
    public void setSettlementCode(String settlementCode) {
        this.settlementCode = settlementCode;
    }

    /**
     * 获取退款方式
     *
     * @return refund_type - 退款方式
     */
    @FieldDef(label="退款方式")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getRefundType() {
        return refundType;
    }

    /**
     * 设置退款方式
     *
     * @param refundType 退款方式
     */
    public void setRefundType(Integer refundType) {
        this.refundType = refundType;
    }

    /**
     * 获取开户行
     *
     * @return bank - 开户行
     */
    @FieldDef(label="开户行", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getBank() {
        return bank;
    }

    /**
     * 设置开户行
     *
     * @param bank 开户行
     */
    public void setBank(String bank) {
        this.bank = bank;
    }

    /**
     * 获取银行卡号
     *
     * @return bank_card_no - 银行卡号
     */
    @FieldDef(label="银行卡号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getBankCardNo() {
        return bankCardNo;
    }

    /**
     * 设置银行卡号
     *
     * @param bankCardNo 银行卡号
     */
    public void setBankCardNo(String bankCardNo) {
        this.bankCardNo = bankCardNo;
    }

    /**
     * 获取创建人ID
     *
     * @return creator_id - 创建人ID
     */
    @FieldDef(label="创建人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * 设置创建人ID
     *
     * @param creatorId 创建人ID
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * 获取创建人
     *
     * @return creator - 创建人
     */
    @FieldDef(label="创建人", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
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
     * 获取提交人
     *
     * @return submitter - 提交人
     */
    @FieldDef(label="提交人", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getSubmitter() {
        return submitter;
    }

    /**
     * 设置提交人
     *
     * @param submitter 提交人
     */
    public void setSubmitter(String submitter) {
        this.submitter = submitter;
    }

    /**
     * 获取收款人ID
     *
     * @return payee_id - 收款人ID
     */
    @FieldDef(label="收款人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getPayeeId() {
        return payeeId;
    }

    /**
     * 设置收款人ID
     *
     * @param payeeId 收款人ID
     */
    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    /**
     * 获取收款人客户名称
     *
     * @return payee - 收款人客户名称
     */
    @FieldDef(label="收款人客户名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPayee() {
        return payee;
    }

    /**
     * 设置收款人客户名称
     *
     * @param payee 收款人客户名称
     */
    public void setPayee(String payee) {
        this.payee = payee;
    }

    /**
     * 获取收款人收款金额
     *
     * @return payee_amount - 收款人收款金额
     */
    @FieldDef(label="收款人收款金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getPayeeAmount() {
        return payeeAmount;
    }

    /**
     * 设置收款人收款金额
     *
     * @param payeeAmount 收款人收款金额
     */
    public void setPayeeAmount(Long payeeAmount) {
        this.payeeAmount = payeeAmount;
    }

    /**
     * 获取撤回操作人ID
     *
     * @return withdraw_operator_id - 撤回操作人ID
     */
    @FieldDef(label="撤回操作人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getWithdrawOperatorId() {
        return withdrawOperatorId;
    }

    /**
     * 设置撤回操作人ID
     *
     * @param withdrawOperatorId 撤回操作人ID
     */
    public void setWithdrawOperatorId(Long withdrawOperatorId) {
        this.withdrawOperatorId = withdrawOperatorId;
    }

    /**
     * 获取撤回操作人
     *
     * @return withdraw_operator - 撤回操作人
     */
    @FieldDef(label="撤回操作人", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getWithdrawOperator() {
        return withdrawOperator;
    }

    /**
     * 设置撤回操作人
     *
     * @param withdrawOperator 撤回操作人
     */
    public void setWithdrawOperator(String withdrawOperator) {
        this.withdrawOperator = withdrawOperator;
    }

    /**
     * 获取取消操作人ID
     *
     * @return canceler_id - 取消操作人ID
     */
    @FieldDef(label="取消操作人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCancelerId() {
        return cancelerId;
    }

    /**
     * 设置取消操作人ID
     *
     * @param cancelerId 取消操作人ID
     */
    public void setCancelerId(Long cancelerId) {
        this.cancelerId = cancelerId;
    }

    /**
     * 获取取消人
     *
     * @return canceler - 取消人
     */
    @FieldDef(label="取消人", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCanceler() {
        return canceler;
    }

    /**
     * 设置取消人
     *
     * @param canceler 取消人
     */
    public void setCanceler(String canceler) {
        this.canceler = canceler;
    }

    /**
     * 获取退款原因
     *
     * @return refund_reason - 退款原因
     */
    @FieldDef(label="退款原因", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getRefundReason() {
        return refundReason;
    }

    /**
     * 设置退款原因
     *
     * @param refundReason 退款原因
     */
    public void setRefundReason(String refundReason) {
        this.refundReason = refundReason;
    }

    /**
     * 获取退款时间
     *
     * @return refund_time - 退款时间
     */
    @FieldDef(label="退款时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getRefundTime() {
        return refundTime;
    }

    /**
     * 设置退款时间
     *
     * @param refundTime 退款时间
     */
    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    /**
     * @return refund_operator_id
     */
    @FieldDef(label="refundOperatorId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getRefundOperatorId() {
        return refundOperatorId;
    }

    /**
     * @param refundOperatorId
     */
    public void setRefundOperatorId(Long refundOperatorId) {
        this.refundOperatorId = refundOperatorId;
    }

    /**
     * 获取退款操作人
     *
     * @return refund_operator - 退款操作人
     */
    @FieldDef(label="退款操作人", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getRefundOperator() {
        return refundOperator;
    }

    /**
     * 设置退款操作人
     *
     * @param refundOperator 退款操作人
     */
    public void setRefundOperator(String refundOperator) {
        this.refundOperator = refundOperator;
    }

    /**
     * 获取营业执照状态
     *
     * @return license_state - 营业执照状态
     */
    @FieldDef(label="营业执照状态")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getLicenseState() {
        return licenseState;
    }

    /**
     * 设置营业执照状态
     *
     * @param licenseState 营业执照状态
     */
    public void setLicenseState(Integer licenseState) {
        this.licenseState = licenseState;
    }

    /**
     * 获取退款单对应的业务部门ID
     *
     * @return department_id - 退款单对应的业务部门ID
     */
    @FieldDef(label="退款单对应的业务部门ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * 设置退款单对应的业务部门ID
     *
     * @param departmentId 退款单对应的业务部门ID
     */
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * 获取退款单对应的部门名称
     *
     * @return department_name - 退款单对应的部门名称
     */
    @FieldDef(label="退款单对应的部门名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * 设置退款单对应的部门名称
     *
     * @param departmentName 退款单对应的部门名称
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * 获取市场ID
     *
     * @return market_id - 市场ID
     */
    @FieldDef(label="市场ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    /**
     * 设置市场ID
     *
     * @param marketId 市场ID
     */
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    /**
     * 获取市场code
     *
     * @return market_code - 市场code
     */
    @FieldDef(label="市场code", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getMarketCode() {
        return marketCode;
    }

    /**
     * 设置市场code
     *
     * @param marketCode 市场code
     */
    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    /**
     * 获取乐观锁，版本号
     *
     * @return version - 乐观锁，版本号
     */
    @FieldDef(label="乐观锁，版本号")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置乐观锁，版本号
     *
     * @param version 乐观锁，版本号
     */
    public void setVersion(Integer version) {
        this.version = version;
    }
}