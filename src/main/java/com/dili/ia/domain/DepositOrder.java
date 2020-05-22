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
 * 
 * This file was generated on 2020-05-22 17:54:56.
 */
@Table(name = "`deposit_order`")
public interface DepositOrder extends IBaseDomain {
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

    @Column(name = "`code`")
    @FieldDef(label="业务编号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCode();

    void setCode(String code);

    @Column(name = "`customer_id`")
    @FieldDef(label="客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`customer_name`")
    @FieldDef(label="客户名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerName();

    void setCustomerName(String customerName);

    @Column(name = "`certificate_number`")
    @FieldDef(label="客户证件号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);

    @Column(name = "`customer_cellphone`")
    @FieldDef(label="客户电话", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerCellphone();

    void setCustomerCellphone(String customerCellphone);

    @Column(name = "`department_id`")
    @FieldDef(label="业务所属部门ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDepartmentId();

    void setDepartmentId(Long departmentId);

    @Column(name = "`department_name`")
    @FieldDef(label="业务所属部门名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getDepartmentName();

    void setDepartmentName(String departmentName);

    @Column(name = "`type_code`")
    @FieldDef(label="保证金类型code，来源数据字典", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTypeCode();

    void setTypeCode(String typeCode);

    @Column(name = "`type_name`")
    @FieldDef(label="保证金类型名称", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getTypeName();

    void setTypeName(String typeName);

    @Column(name = "`addr_id`")
    @FieldDef(label="对应编号ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getAddrId();

    void setAddrId(Long addrId);

    @Column(name = "`addr`")
    @FieldDef(label="对应编号", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAddr();

    void setAddr(String addr);

    @Column(name = "`amount`")
    @FieldDef(label="保证金金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getAmount();

    void setAmount(Long amount);

    @Column(name = "`notes`")
    @FieldDef(label="备注信息", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);

    @Column(name = "`state`")
    @FieldDef(label="（1：已创建 2：已取消 3：已提交 4：已交费5：已退款）")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getState();

    void setState(Integer state);

    @Column(name = "`creator_id`")
    @FieldDef(label="创建操作员ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCreatorId();

    void setCreatorId(Long creatorId);

    @Column(name = "`creator`")
    @FieldDef(label="创建人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCreator();

    void setCreator(String creator);

    @Column(name = "`submitter_id`")
    @FieldDef(label="提交人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getSubmitterId();

    void setSubmitterId(Long submitterId);

    @Column(name = "`submitter`")
    @FieldDef(label="提交人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSubmitter();

    void setSubmitter(String submitter);

    @Column(name = "`submit_time`")
    @FieldDef(label="提交时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getSubmitTime();

    void setSubmitTime(Date submitTime);

    @Column(name = "`withdraw_operator_id`")
    @FieldDef(label="撤回人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getWithdrawOperatorId();

    void setWithdrawOperatorId(Long withdrawOperatorId);

    @Column(name = "`withdraw_operator`")
    @FieldDef(label="撤回人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getWithdrawOperator();

    void setWithdrawOperator(String withdrawOperator);

    @Column(name = "`canceler_id`")
    @FieldDef(label="取消人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCancelerId();

    void setCancelerId(Long cancelerId);

    @Column(name = "`canceler`")
    @FieldDef(label="取消人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCanceler();

    void setCanceler(String canceler);

    @Column(name = "`version`")
    @FieldDef(label="版本控制,乐观锁")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getVersion();

    void setVersion(Long version);

    @Column(name = "`market_id`")
    @FieldDef(label="市场Id")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getMarketId();

    void setMarketId(Long marketId);
}