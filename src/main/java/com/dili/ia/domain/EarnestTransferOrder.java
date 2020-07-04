package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import tk.mybatis.mapper.annotation.Version;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 定金转移主的业务单
 * This file was generated on 2020-07-04 09:48:04.
 */
@Table(name = "`earnest_transfer_order`")
public class EarnestTransferOrder extends BaseDomain {
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
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    private LocalDateTime modifyTime;

    /**
     * 定金转移业务单编号
     */
    @Column(name = "`code`")
    private String code;

    /**
     * 状态
     */
    @Column(name = "`state`")
    private Integer state;

    /**
     * 转移金额
     */
    @Column(name = "`amount`")
    private Long amount;

    /**
     * 转出方客户余额账户ID
     */
    @Column(name = "`payer_customer_account_id`")
    private Long payerCustomerAccountId;

    /**
     * 转出方客户ID
     */
    @Column(name = "`payer_id`")
    private Long payerId;

    /**
     * 转出方客户证件号码
     */
    @Column(name = "`payer_certificate_number`")
    private String payerCertificateNumber;

    /**
     * 转出方客户名称
     */
    @Column(name = "`payer_name`")
    private String payerName;

    /**
     * 转出方客户电话
     */
    @Column(name = "`payer_cellphone`")
    private String payerCellphone;

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
     * 定金转移原因
     */
    @Column(name = "`transfer_reason`")
    private String transferReason;

    /**
     * 转入方客户余额账户ID
     */
    @Column(name = "`payee_customer_account_id`")
    private Long payeeCustomerAccountId;

    /**
     * 转入方客户ID
     */
    @Column(name = "`payee_id`")
    private Long payeeId;

    /**
     * 转入方客户名称
     */
    @Column(name = "`payee_name`")
    private String payeeName;

    /**
     * 转入方客户证件号码
     */
    @Column(name = "`payee_certificate_number`")
    private String payeeCertificateNumber;

    /**
     * 转入方客户电话
     */
    @Column(name = "`payee_cellphone`")
    private String payeeCellphone;

    /**
     * 转出方的定金转出流水号
     */
    @Column(name = "`payer_transaction_details_code`")
    private String payerTransactionDetailsCode;

    /**
     * 市场ID
     */
    @Column(name = "`market_id`")
    private Long marketId;

    /**
     * 转入方的定金转入流水号
     */
    @Column(name = "`payee_transaction_code`")
    private String payeeTransactionCode;

    /**
     * 转移成功时间
     */
    @Column(name = "`transfer_time`")
    private Date transferTime;

    /**
     * 版本控制,乐观锁
     */
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
     * 获取定金转移业务单编号
     *
     * @return code - 定金转移业务单编号
     */
    @FieldDef(label="定金转移业务单编号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置定金转移业务单编号
     *
     * @param code 定金转移业务单编号
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取状态
     *
     * @return state - 状态
     */
    @FieldDef(label="状态")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态
     *
     * @param state 状态
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取转移金额
     *
     * @return amount - 转移金额
     */
    @FieldDef(label="转移金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAmount() {
        return amount;
    }

    /**
     * 设置转移金额
     *
     * @param amount 转移金额
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * 获取转出方客户余额账户ID
     *
     * @return payer_customer_account_id - 转出方客户余额账户ID
     */
    @FieldDef(label="转出方客户余额账户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getPayerCustomerAccountId() {
        return payerCustomerAccountId;
    }

    /**
     * 设置转出方客户余额账户ID
     *
     * @param payerCustomerAccountId 转出方客户余额账户ID
     */
    public void setPayerCustomerAccountId(Long payerCustomerAccountId) {
        this.payerCustomerAccountId = payerCustomerAccountId;
    }

    /**
     * 获取转出方客户ID
     *
     * @return payer_id - 转出方客户ID
     */
    @FieldDef(label="转出方客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getPayerId() {
        return payerId;
    }

    /**
     * 设置转出方客户ID
     *
     * @param payerId 转出方客户ID
     */
    public void setPayerId(Long payerId) {
        this.payerId = payerId;
    }

    /**
     * 获取转出方客户证件号码
     *
     * @return payer_certificate_number - 转出方客户证件号码
     */
    @FieldDef(label="转出方客户证件号码", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPayerCertificateNumber() {
        return payerCertificateNumber;
    }

    /**
     * 设置转出方客户证件号码
     *
     * @param payerCertificateNumber 转出方客户证件号码
     */
    public void setPayerCertificateNumber(String payerCertificateNumber) {
        this.payerCertificateNumber = payerCertificateNumber;
    }

    /**
     * 获取转出方客户名称
     *
     * @return payer_name - 转出方客户名称
     */
    @FieldDef(label="转出方客户名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPayerName() {
        return payerName;
    }

    /**
     * 设置转出方客户名称
     *
     * @param payerName 转出方客户名称
     */
    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    /**
     * 获取转出方客户电话
     *
     * @return payer_cellphone - 转出方客户电话
     */
    @FieldDef(label="转出方客户电话", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPayerCellphone() {
        return payerCellphone;
    }

    /**
     * 设置转出方客户电话
     *
     * @param payerCellphone 转出方客户电话
     */
    public void setPayerCellphone(String payerCellphone) {
        this.payerCellphone = payerCellphone;
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
     * 获取定金转移原因
     *
     * @return transfer_reason - 定金转移原因
     */
    @FieldDef(label="定金转移原因", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getTransferReason() {
        return transferReason;
    }

    /**
     * 设置定金转移原因
     *
     * @param transferReason 定金转移原因
     */
    public void setTransferReason(String transferReason) {
        this.transferReason = transferReason;
    }

    /**
     * 获取转入方客户余额账户ID
     *
     * @return payee_customer_account_id - 转入方客户余额账户ID
     */
    @FieldDef(label="转入方客户余额账户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getPayeeCustomerAccountId() {
        return payeeCustomerAccountId;
    }

    /**
     * 设置转入方客户余额账户ID
     *
     * @param payeeCustomerAccountId 转入方客户余额账户ID
     */
    public void setPayeeCustomerAccountId(Long payeeCustomerAccountId) {
        this.payeeCustomerAccountId = payeeCustomerAccountId;
    }

    /**
     * 获取转入方客户ID
     *
     * @return payee_id - 转入方客户ID
     */
    @FieldDef(label="转入方客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getPayeeId() {
        return payeeId;
    }

    /**
     * 设置转入方客户ID
     *
     * @param payeeId 转入方客户ID
     */
    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    /**
     * 获取转入方客户名称
     *
     * @return payee_name - 转入方客户名称
     */
    @FieldDef(label="转入方客户名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPayeeName() {
        return payeeName;
    }

    /**
     * 设置转入方客户名称
     *
     * @param payeeName 转入方客户名称
     */
    public void setPayeeName(String payeeName) {
        this.payeeName = payeeName;
    }

    /**
     * 获取转入方客户证件号码
     *
     * @return payee_certificate_number - 转入方客户证件号码
     */
    @FieldDef(label="转入方客户证件号码", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPayeeCertificateNumber() {
        return payeeCertificateNumber;
    }

    /**
     * 设置转入方客户证件号码
     *
     * @param payeeCertificateNumber 转入方客户证件号码
     */
    public void setPayeeCertificateNumber(String payeeCertificateNumber) {
        this.payeeCertificateNumber = payeeCertificateNumber;
    }

    /**
     * 获取转入方客户电话
     *
     * @return payee_cellphone - 转入方客户电话
     */
    @FieldDef(label="转入方客户电话", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPayeeCellphone() {
        return payeeCellphone;
    }

    /**
     * 设置转入方客户电话
     *
     * @param payeeCellphone 转入方客户电话
     */
    public void setPayeeCellphone(String payeeCellphone) {
        this.payeeCellphone = payeeCellphone;
    }

    /**
     * 获取转出方的定金转出流水号
     *
     * @return payer_transaction_details_code - 转出方的定金转出流水号
     */
    @FieldDef(label="转出方的定金转出流水号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPayerTransactionDetailsCode() {
        return payerTransactionDetailsCode;
    }

    /**
     * 设置转出方的定金转出流水号
     *
     * @param payerTransactionDetailsCode 转出方的定金转出流水号
     */
    public void setPayerTransactionDetailsCode(String payerTransactionDetailsCode) {
        this.payerTransactionDetailsCode = payerTransactionDetailsCode;
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
     * 获取转入方的定金转入流水号
     *
     * @return payee_transaction_code - 转入方的定金转入流水号
     */
    @FieldDef(label="转入方的定金转入流水号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPayeeTransactionCode() {
        return payeeTransactionCode;
    }

    /**
     * 设置转入方的定金转入流水号
     *
     * @param payeeTransactionCode 转入方的定金转入流水号
     */
    public void setPayeeTransactionCode(String payeeTransactionCode) {
        this.payeeTransactionCode = payeeTransactionCode;
    }

    /**
     * 获取转移成功时间
     *
     * @return transfer_time - 转移成功时间
     */
    @FieldDef(label="转移成功时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getTransferTime() {
        return transferTime;
    }

    /**
     * 设置转移成功时间
     *
     * @param transferTime 转移成功时间
     */
    public void setTransferTime(Date transferTime) {
        this.transferTime = transferTime;
    }

    /**
     * 获取版本控制,乐观锁
     *
     * @return version - 版本控制,乐观锁
     */
    @Version
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