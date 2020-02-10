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
 * 租赁订单
 * This file was generated on 2020-02-10 12:31:39.
 */
@Table(name = "`lease_order`")
public interface LeaseOrder extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`code`")
    @FieldDef(label="出租编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCode();

    void setCode(String code);

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

    @Column(name = "`start_time`")
    @FieldDef(label="开始时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getStartTime();

    void setStartTime(Date startTime);

    @Column(name = "`end_time`")
    @FieldDef(label="截止时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getEndTime();

    void setEndTime(Date endTime);

    @Column(name = "`contract_no`")
    @FieldDef(label="合同编号", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getContractNo();

    void setContractNo(String contractNo);

    @Column(name = "`lease_term`")
    @FieldDef(label="租赁形式")
    @EditMode(editor = FieldEditor.Number, required = false)
    Boolean getLeaseTerm();

    void setLeaseTerm(Boolean leaseTerm);

    @Column(name = "`engage`")
    @FieldDef(label="engage")
    @EditMode(editor = FieldEditor.Number, required = false)
    Boolean getEngage();

    void setEngage(Boolean engage);

    @Column(name = "`category_id`")
    @FieldDef(label="品类id")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCategoryId();

    void setCategoryId(Long categoryId);

    @Column(name = "`category_name`")
    @FieldDef(label="品类名称", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCategoryName();

    void setCategoryName(String categoryName);

    @Column(name = "`department_id`")
    @FieldDef(label="departmentId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDepartmentId();

    void setDepartmentId(Long departmentId);

    @Column(name = "`department_name`")
    @FieldDef(label="departmentName", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getDepartmentName();

    void setDepartmentName(String departmentName);

    @Column(name = "`state`")
    @FieldDef(label="状态")
    @EditMode(editor = FieldEditor.Number, required = false)
    Boolean getState();

    void setState(Boolean state);

    @Column(name = "`pay_state`")
    @FieldDef(label="payState")
    @EditMode(editor = FieldEditor.Number, required = false)
    Boolean getPayState();

    void setPayState(Boolean payState);

    @Column(name = "`total_amount`")
    @FieldDef(label="金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getTotalAmount();

    void setTotalAmount(Long totalAmount);

    @Column(name = "`deposit_deduction`")
    @FieldDef(label="depositDeduction")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDepositDeduction();

    void setDepositDeduction(Long depositDeduction);

    @Column(name = "`earnest_deduction`")
    @FieldDef(label="earnestDeduction")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getEarnestDeduction();

    void setEarnestDeduction(Long earnestDeduction);

    @Column(name = "`transfer_deduction`")
    @FieldDef(label="transferDeduction")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getTransferDeduction();

    void setTransferDeduction(Long transferDeduction);

    @Column(name = "`pay_amount`")
    @FieldDef(label="payAmount")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getPayAmount();

    void setPayAmount(Long payAmount);

    @Column(name = "`paid_amount`")
    @FieldDef(label="paidAmount")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getPaidAmount();

    void setPaidAmount(Long paidAmount);

    @Column(name = "`creator_id`")
    @FieldDef(label="操作员ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCreatorId();

    void setCreatorId(Long creatorId);

    @Column(name = "`creator`")
    @FieldDef(label="creator", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCreator();

    void setCreator(String creator);

    @Column(name = "`withdraw_operator_id`")
    @FieldDef(label="withdrawOperatorId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getWithdrawOperatorId();

    void setWithdrawOperatorId(Long withdrawOperatorId);

    @Column(name = "`withdraw_operator`")
    @FieldDef(label="withdrawOperator", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getWithdrawOperator();

    void setWithdrawOperator(String withdrawOperator);

    @Column(name = "`canceler_id`")
    @FieldDef(label="cancelerId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCancelerId();

    void setCancelerId(Long cancelerId);

    @Column(name = "`canceler`")
    @FieldDef(label="canceler", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCanceler();

    void setCanceler(String canceler);

    @Column(name = "`notes`")
    @FieldDef(label="备注", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);

    @Column(name = "`refund_amount`")
    @FieldDef(label="refundAmount")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getRefundAmount();

    void setRefundAmount(Long refundAmount);

    @Column(name = "`is_delete`")
    @FieldDef(label="isDelete")
    @EditMode(editor = FieldEditor.Number, required = false)
    Boolean getIsDelete();

    void setIsDelete(Boolean isDelete);

    @Column(name = "`market_id`")
    @FieldDef(label="marketId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getMarketId();

    void setMarketId(Long marketId);
}