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
 * 缴费单
 * This file was generated on 2020-02-10 12:31:39.
 */
@Table(name = "`payment_bill`")
public interface PaymentBill extends IBaseDomain {
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

    @Column(name = "`lease_order_id`")
    @FieldDef(label="leaseOrderId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getLeaseOrderId();

    void setLeaseOrderId(Long leaseOrderId);

    @Column(name = "`order_code`")
    @FieldDef(label="租赁", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getOrderCode();

    void setOrderCode(String orderCode);

    @Column(name = "`biz_type`")
    @FieldDef(label="bizType")
    @EditMode(editor = FieldEditor.Number, required = false)
    Boolean getBizType();

    void setBizType(Boolean bizType);

    @Column(name = "`state`")
    @FieldDef(label="状态")
    @EditMode(editor = FieldEditor.Number, required = false)
    Boolean getState();

    void setState(Boolean state);

    @Column(name = "`amount`")
    @FieldDef(label="金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getAmount();

    void setAmount(Long amount);

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

    @Column(name = "`payed_time`")
    @FieldDef(label="payedTime")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getPayedTime();

    void setPayedTime(Date payedTime);

    @Column(name = "`settlement_code`")
    @FieldDef(label="settlementCode", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getSettlementCode();

    void setSettlementCode(String settlementCode);

    @Column(name = "`market_id`")
    @FieldDef(label="marketId")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getMarketId();

    void setMarketId(Long marketId);
}