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
 * This file was generated on 2020-02-10 12:31:39.
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
    @FieldDef(label="code", maxLength = 20)
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
    @FieldDef(label="bizType")
    @EditMode(editor = FieldEditor.Number, required = false)
    Boolean getBizType();

    void setBizType(Boolean bizType);

    @Column(name = "`order_item_code`")
    @FieldDef(label="orderItemCode", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOrderItemCode();

    void setOrderItemCode(String orderItemCode);

    @Column(name = "`order_id`")
    @FieldDef(label="orderId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getOrderId();

    void setOrderId(Long orderId);

    @Column(name = "`order_code`")
    @FieldDef(label="orderCode", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOrderCode();

    void setOrderCode(String orderCode);

    @Column(name = "`order_item_id`")
    @FieldDef(label="orderItemId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getOrderItemId();

    void setOrderItemId(Long orderItemId);

    @Column(name = "`customer_id`")
    @FieldDef(label="customerId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCustomerId();

    void setCustomerId(Long customerId);

    @Column(name = "`customer_name`")
    @FieldDef(label="customerName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerName();

    void setCustomerName(String customerName);

    @Column(name = "`certificate_number`")
    @FieldDef(label="certificateNumber", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCertificateNumber();

    void setCertificateNumber(String certificateNumber);

    @Column(name = "`customer_cellphone`")
    @FieldDef(label="customerCellphone", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCustomerCellphone();

    void setCustomerCellphone(String customerCellphone);

    @Column(name = "`refund_operator_id`")
    @FieldDef(label="操作员(经办人)")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getRefundOperatorId();

    void setRefundOperatorId(Long refundOperatorId);

    @Column(name = "`refund_operator_name`")
    @FieldDef(label="refundOperatorName", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getRefundOperatorName();

    void setRefundOperatorName(String refundOperatorName);

    @Column(name = "`state`")
    @FieldDef(label="状态")
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
    @FieldDef(label="settlementCode", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSettlementCode();

    void setSettlementCode(String settlementCode);

    @Column(name = "`refund_type`")
    @FieldDef(label="refundType")
    @EditMode(editor = FieldEditor.Number, required = false)
    Boolean getRefundType();

    void setRefundType(Boolean refundType);

    @Column(name = "`bank`")
    @FieldDef(label="bank", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getBank();

    void setBank(String bank);

    @Column(name = "`bank_card_no`")
    @FieldDef(label="bankCardNo", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getBankCardNo();

    void setBankCardNo(String bankCardNo);

    @Column(name = "`creator_id`")
    @FieldDef(label="creatorId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getCreatorId();

    void setCreatorId(Long creatorId);

    @Column(name = "`creator`")
    @FieldDef(label="creator", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getCreator();

    void setCreator(String creator);

    @Column(name = "`submitter_id`")
    @FieldDef(label="submitterId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getSubmitterId();

    void setSubmitterId(Long submitterId);

    @Column(name = "`submitter`")
    @FieldDef(label="submitter", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSubmitter();

    void setSubmitter(String submitter);

    @Column(name = "`payee_id`")
    @FieldDef(label="payeeId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getPayeeId();

    void setPayeeId(Long payeeId);

    @Column(name = "`payee`")
    @FieldDef(label="payee", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getPayee();

    void setPayee(String payee);

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

    @Column(name = "`refund_reason`")
    @FieldDef(label="refundReason", maxLength = 80)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getRefundReason();

    void setRefundReason(String refundReason);

    @Column(name = "`is_delete`")
    @FieldDef(label="isDelete")
    @EditMode(editor = FieldEditor.Number, required = false)
    Boolean getIsDelete();

    void setIsDelete(Boolean isDelete);

    @Column(name = "`license_state`")
    @FieldDef(label="营业执照状态")
    @EditMode(editor = FieldEditor.Number, required = false)
    Boolean getLicenseState();

    void setLicenseState(Boolean licenseState);

    @Column(name = "`market_id`")
    @FieldDef(label="marketId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getMarketId();

    void setMarketId(Long marketId);
}