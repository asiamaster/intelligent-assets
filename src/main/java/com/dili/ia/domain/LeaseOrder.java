package com.dili.ia.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import tk.mybatis.mapper.annotation.Version;

import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 租赁期限
 * This file was generated on 2020-02-11 15:54:49.
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
    @FieldDef(label="出租编号", maxLength = 30)
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

    @Column(name = "`certificate_number`")
    @FieldDef(label="证件号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);

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

    @Column(name = "`days`")
    @FieldDef(label="天数")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getDays();

    void setDays(Integer days);

    @Column(name = "`contract_no`")
    @FieldDef(label="合同编号")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getContractNo();

    void setContractNo(String contractNo);

    @Column(name = "`lease_term_code`")
    @FieldDef(label="租赁形式")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getLeaseTermCode();

    void setLeaseTermCode(String leaseTermCode);

    @Column(name = "`lease_term_name`")
    @FieldDef(label="租赁形式")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getLeaseTermName();

    void setLeaseTermName(String leaseTermName);

    @Column(name = "`engage_code`")
    @FieldDef(label="经营范围code")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getEngageCode();

    void setEngageCode(String engageCode);

    @Column(name = "`engage_name`")
    @FieldDef(label="经营范围名称")
    @EditMode(editor = FieldEditor.Text, required = false)
    String getEngageName();

    void setEngageName(String engageName);

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
    @FieldDef(label="部门名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getDepartmentName();

    void setDepartmentName(String departmentName);

    @Column(name = "`state`")
    @FieldDef(label="状态（1：已创建 2：已取消 3：已提交 4：未生效 5：已生效 6：已停租 7：已退款 8：已过期）")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getState();

    void setState(Integer state);

    @Column(name = "`pay_state`")
    @FieldDef(label="支付状态（1：未交清 2：已交清）")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getPayState();

    void setPayState(Integer payState);

    @Column(name = "`total_amount`")
    @FieldDef(label="金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getTotalAmount();

    void setTotalAmount(Long totalAmount);

    @Column(name = "`deposit_amount`")
    @FieldDef(label="保证金")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDepositAmount();

    void setDepositAmount(Long depositAmount);

    @Column(name = "`manage_amount`")
    @FieldDef(label="物管费")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getManageAmount();

    void setManageAmount(Long manageAmount);

    @Column(name = "`rent_amount`")
    @FieldDef(label="租金")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getRentAmount();

    void setRentAmount(Long rentAmount);

    @Column(name = "`deposit_deduction`")
    @FieldDef(label="保证金抵扣")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getDepositDeduction();

    void setDepositDeduction(Long depositDeduction);

    @Column(name = "`earnest_deduction`")
    @FieldDef(label="定金抵扣")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getEarnestDeduction();

    void setEarnestDeduction(Long earnestDeduction);

    @Column(name = "`transfer_deduction`")
    @FieldDef(label="转低金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getTransferDeduction();

    void setTransferDeduction(Long transferDeduction);

    @Column(name = "`pay_amount`")
    @FieldDef(label="实付金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getPayAmount();

    void setPayAmount(Long payAmount);

    @Column(name = "`paid_amount`")
    @FieldDef(label="已付金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getPaidAmount();

    void setPaidAmount(Long paidAmount);

    @Column(name = "`creator_id`")
    @FieldDef(label="创建人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCreatorId();

    void setCreatorId(Long creatorId);

    @Column(name = "`creator`")
    @FieldDef(label="创建人", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCreator();

    void setCreator(String creator);

    @Column(name = "`withdraw_operator_id`")
    @FieldDef(label="撤回人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getWithdrawOperatorId();

    void setWithdrawOperatorId(Long withdrawOperatorId);

    @Column(name = "`withdraw_operator`")
    @FieldDef(label="撤回人", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getWithdrawOperator();

    void setWithdrawOperator(String withdrawOperator);

    @Column(name = "`canceler_id`")
    @FieldDef(label="取消人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCancelerId();

    void setCancelerId(Long cancelerId);

    @Column(name = "`canceler`")
    @FieldDef(label="取消人", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCanceler();

    void setCanceler(String canceler);

    @Column(name = "`wait_amount`")
    @FieldDef(label="待付金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getWaitAmount();

    void setWaitAmount(Long waitAmount);

    @Column(name = "`payment_id`")
    @FieldDef(label="缴费单ID(缴费中)")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getPaymentId();

    void setPaymentId(Long paymentId);

    @Column(name = "`notes`")
    @FieldDef(label="备注", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getNotes();

    void setNotes(String notes);

    @Column(name = "`refund_amount`")
    @FieldDef(label="退款金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getRefundAmount();

    void setRefundAmount(Long refundAmount);

    @Column(name = "`refund_state`")
    @FieldDef(label="退款状态状态（1：未发起申请 2：退款中 3：已退款）")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getRefundState();

    void setRefundState(Integer refundState);

    @Column(name = "`is_renew`")
    @FieldDef(label="是否为续租")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getIsRenew();

    void setIsRenew(Integer isRenew);

    @Column(name = "`is_delete`")
    @FieldDef(label="是否删除")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getIsDelete();

    void setIsDelete(Integer isDelete);

    @Column(name = "`market_id`")
    @FieldDef(label="市场")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getMarketId();

    void setMarketId(Long marketId);

    @Column(name = "`market_code`")
    @FieldDef(label="市场Code", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getMarketCode();

    void setMarketCode(String marketCode);

    @Column(name = "`version`")
    @FieldDef(label="版本号")
    @EditMode(editor = FieldEditor.Number, required = false)
    @Version
    Integer getVersion();

    void setVersion(Integer version);
}