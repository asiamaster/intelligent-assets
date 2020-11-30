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
 * 客户余额管理,包含定金，转抵金额。定金可用来源于定金缴费，别人转移。
转抵金额只来源于租赁退款
 * This file was generated on 2020-07-04 09:48:04.
 */
@Deprecated
@Table(name = "`customer_account`")
public class CustomerAccount extends BaseDomain {
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
     * 客户证件号
     */
    @Column(name = "`certificate_number`")
    private String certificateNumber;

    /**
     * 定金余额= 定金可用余额 + 定金冻结金额
     */
    @Column(name = "`earnest_balance`")
    private Long earnestBalance;

    /**
     * 转抵余额=转抵可用余额 + 转抵冻结金额
     */
    @Column(name = "`transfer_balance`")
    private Long transferBalance;

    /**
     * 定金可用余额=定金余额 - 定金冻结金额
     */
    @Column(name = "`earnest_available_balance`")
    private Long earnestAvailableBalance;

    /**
     * 转抵可用余额=转抵余额 - 转抵冻结金额
     */
    @Column(name = "`transfer_available_balance`")
    private Long transferAvailableBalance;

    /**
     * 定金冻结金额
     */
    @Column(name = "`earnest_frozen_amount`")
    private Long earnestFrozenAmount;

    /**
     * 转抵冻结金额
     */
    @Column(name = "`transfer_frozen_amount`")
    private Long transferFrozenAmount;

    /**
     * 市场ID
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
     * 获取定金余额= 定金可用余额 + 定金冻结金额
     *
     * @return earnest_balance - 定金余额= 定金可用余额 + 定金冻结金额
     */
    @FieldDef(label="定金余额= 定金可用余额 + 定金冻结金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getEarnestBalance() {
        return earnestBalance;
    }

    /**
     * 设置定金余额= 定金可用余额 + 定金冻结金额
     *
     * @param earnestBalance 定金余额= 定金可用余额 + 定金冻结金额
     */
    public void setEarnestBalance(Long earnestBalance) {
        this.earnestBalance = earnestBalance;
    }

    /**
     * 获取转抵余额=转抵可用余额 + 转抵冻结金额
     *
     * @return transfer_balance - 转抵余额=转抵可用余额 + 转抵冻结金额
     */
    @FieldDef(label="转抵余额=转抵可用余额 + 转抵冻结金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getTransferBalance() {
        return transferBalance;
    }

    /**
     * 设置转抵余额=转抵可用余额 + 转抵冻结金额
     *
     * @param transferBalance 转抵余额=转抵可用余额 + 转抵冻结金额
     */
    public void setTransferBalance(Long transferBalance) {
        this.transferBalance = transferBalance;
    }

    /**
     * 获取定金可用余额=定金余额 - 定金冻结金额
     *
     * @return earnest_available_balance - 定金可用余额=定金余额 - 定金冻结金额
     */
    @FieldDef(label="定金可用余额=定金余额 - 定金冻结金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getEarnestAvailableBalance() {
        return earnestAvailableBalance;
    }

    /**
     * 设置定金可用余额=定金余额 - 定金冻结金额
     *
     * @param earnestAvailableBalance 定金可用余额=定金余额 - 定金冻结金额
     */
    public void setEarnestAvailableBalance(Long earnestAvailableBalance) {
        this.earnestAvailableBalance = earnestAvailableBalance;
    }

    /**
     * 获取转抵可用余额=转抵余额 - 转抵冻结金额
     *
     * @return transfer_available_balance - 转抵可用余额=转抵余额 - 转抵冻结金额
     */
    @FieldDef(label="转抵可用余额=转抵余额 - 转抵冻结金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getTransferAvailableBalance() {
        return transferAvailableBalance;
    }

    /**
     * 设置转抵可用余额=转抵余额 - 转抵冻结金额
     *
     * @param transferAvailableBalance 转抵可用余额=转抵余额 - 转抵冻结金额
     */
    public void setTransferAvailableBalance(Long transferAvailableBalance) {
        this.transferAvailableBalance = transferAvailableBalance;
    }

    /**
     * 获取定金冻结金额
     *
     * @return earnest_frozen_amount - 定金冻结金额
     */
    @FieldDef(label="定金冻结金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getEarnestFrozenAmount() {
        return earnestFrozenAmount;
    }

    /**
     * 设置定金冻结金额
     *
     * @param earnestFrozenAmount 定金冻结金额
     */
    public void setEarnestFrozenAmount(Long earnestFrozenAmount) {
        this.earnestFrozenAmount = earnestFrozenAmount;
    }

    /**
     * 获取转抵冻结金额
     *
     * @return transfer_frozen_amount - 转抵冻结金额
     */
    @FieldDef(label="转抵冻结金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getTransferFrozenAmount() {
        return transferFrozenAmount;
    }

    /**
     * 设置转抵冻结金额
     *
     * @param transferFrozenAmount 转抵冻结金额
     */
    public void setTransferFrozenAmount(Long transferFrozenAmount) {
        this.transferFrozenAmount = transferFrozenAmount;
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