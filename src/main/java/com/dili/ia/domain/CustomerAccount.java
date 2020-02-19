package com.dili.ia.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 客户余额管理,包含定金，转抵金额。定金可用来源于定金缴费，别人转移。
转抵金额只来源于租赁退款
 * This file was generated on 2020-02-19 17:29:54.
 */
@Table(name = "`customer_account`")
public interface CustomerAccount extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`create_time`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getCreateTime();

    void setCreateTime(Date createTime);

    @Column(name = "`modify_time`")
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getModifyTime();

    void setModifyTime(Date modifyTime);

    @Column(name = "`customer_id`")
    @FieldDef(label="客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`customer_name`")
    @FieldDef(label="客户名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerName();

    void setCustomerName(String customerName);

    @Column(name = "`customer_cellphone`")
    @FieldDef(label="客户电话", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerCellphone();

    void setCustomerCellphone(String customerCellphone);

    @Column(name = "`customer_certificate_number`")
    @FieldDef(label="客户电话", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerCertificateNumber();

    void setCustomerCertificateNumber(String customerCertificateNumber);

    @Column(name = "`earnest_balance`")
    @FieldDef(label="定金余额= 定金可用余额 + 定金冻结金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getEarnestBalance();

    void setEarnestBalance(Long earnestBalance);

    @Column(name = "`transfer_balance`")
    @FieldDef(label="转抵余额=转抵可用余额 + 转抵冻结金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getTransferBalance();

    void setTransferBalance(Long transferBalance);

    @Column(name = "`earnest_available_balance`")
    @FieldDef(label="定金可用余额=定金余额 - 定金冻结金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getEarnestAvailableBalance();

    void setEarnestAvailableBalance(Long earnestAvailableBalance);

    @Column(name = "`transfer_available_balance`")
    @FieldDef(label="转抵可用余额=转抵余额 - 转抵冻结金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getTransferAvailableBalance();

    void setTransferAvailableBalance(Long transferAvailableBalance);

    @Column(name = "`earnest_frozen_amount`")
    @FieldDef(label="定金冻结金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getEarnestFrozenAmount();

    void setEarnestFrozenAmount(Long earnestFrozenAmount);

    @Column(name = "`transfer_frozen_amount`")
    @FieldDef(label="转抵冻结金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getTransferFrozenAmount();

    void setTransferFrozenAmount(Long transferFrozenAmount);

    @Column(name = "`transfer_version`")
    @FieldDef(label="转抵数据修改版本控制")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getTransferVersion();

    void setTransferVersion(Long transferVersion);

    @Column(name = "`earnest_version`")
    @FieldDef(label="定金数据修改版本控制")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getEarnestVersion();

    void setEarnestVersion(Long earnestVersion);

    @Column(name = "`market_id`")
    @FieldDef(label="marketId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getMarketId();

    void setMarketId(Long marketId);
}