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
 * 退款单
 * This file was generated on 2020-02-11 15:54:49.
 */
@Table(name = "`refund_bill`")
public interface RefundBill extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`code`")
    @FieldDef(label="编号", maxLength = 20)
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

    @Column(name = "`biz_type`")
    @FieldDef(label="业务类型")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getBizType();

    void setBizType(Integer bizType);

    @Column(name = "`order_item_id`")
    @FieldDef(label="订单项ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getOrderItemId();

    void setOrderItemId(Long orderItemId);

    @Column(name = "`order_item_code`")
    @FieldDef(label="订单项编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOrderItemCode();

    void setOrderItemCode(String orderItemCode);

    @Column(name = "`order_id`")
    @FieldDef(label="订单ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getOrderId();

    void setOrderId(Long orderId);

    @Column(name = "`order_code`")
    @FieldDef(label="订单编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOrderCode();

    void setOrderCode(String orderCode);

    @Column(name = "`customer_id`")
    @FieldDef(label="客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`customer_name`")
    @FieldDef(label="客户编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerName();

    void setCustomerName(String customerName);

    @Column(name = "`certificate_number`")
    @FieldDef(label="证件号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);

    @Column(name = "`customer_cellphone`")
    @FieldDef(label="客户手机号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerCellphone();

    void setCustomerCellphone(String customerCellphone);

    @Column(name = "`refund_operator_id`")
    @FieldDef(label="退款操作人 ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getRefundOperatorId();

    void setRefundOperatorId(Long refundOperatorId);

    @Column(name = "`refund_operator_name`")
    @FieldDef(label="退款操作人", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getRefundOperatorName();

    void setRefundOperatorName(String refundOperatorName);

    @Column(name = "`state`")
    @FieldDef(label="状态（1：已创建 2：已取消 3：已提交 4：已退款）")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getState();

    void setState(Long state);

    @Column(name = "`total_refund_amount`")
    @FieldDef(label="金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getTotalRefundAmount();

    void setTotalRefundAmount(Long totalRefundAmount);

    @Column(name = "`submit_time`")
    @FieldDef(label="提交时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getSubmitTime();

    void setSubmitTime(Date submitTime);

    @Column(name = "`settlement_code`")
    @FieldDef(label="结算编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSettlementCode();

    void setSettlementCode(String settlementCode);

    @Column(name = "`refund_type`")
    @FieldDef(label="退款方式")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getRefundType();

    void setRefundType(Integer refundType);

    @Column(name = "`bank`")
    @FieldDef(label="开户行", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getBank();

    void setBank(String bank);

    @Column(name = "`bank_card_no`")
    @FieldDef(label="银行卡号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getBankCardNo();

    void setBankCardNo(String bankCardNo);

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

    @Column(name = "`submitter_id`")
    @FieldDef(label="提交人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getSubmitterId();

    void setSubmitterId(Long submitterId);

    @Column(name = "`submitter`")
    @FieldDef(label="提交人", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSubmitter();

    void setSubmitter(String submitter);

    @Column(name = "`payee_id`")
    @FieldDef(label="收款人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getPayeeId();

    void setPayeeId(Long payeeId);

    @Column(name = "`payee`")
    @FieldDef(label="收款人", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPayee();

    void setPayee(String payee);

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

    @Column(name = "`refund_reason`")
    @FieldDef(label="退款原因", maxLength = 80)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getRefundReason();

    void setRefundReason(String refundReason);

    @Column(name = "`is_delete`")
    @FieldDef(label="是否删除")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getIsDelete();

    void setIsDelete(Integer isDelete);

    @Column(name = "`license_state`")
    @FieldDef(label="营业执照状态")
    @EditMode(editor = FieldEditor.Number, required = false)
    Integer getLicenseState();

    void setLicenseState(Integer licenseState);

    @Column(name = "`market_id`")
    @FieldDef(label="市场ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getMarketId();

    void setMarketId(Long marketId);
}