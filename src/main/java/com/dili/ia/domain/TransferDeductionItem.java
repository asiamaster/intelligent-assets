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
 * 转抵项
 * This file was generated on 2020-02-11 15:54:49.
 */
@Table(name = "`transfer_deduction_item`")
public interface TransferDeductionItem extends IBaseDomain {
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

    @Column(name = "`refund_order_id`")
    @FieldDef(label="退款单ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getRefundOrderId();

    void setRefundOrderId(Long refundOrderId);

    @Column(name = "`payee_amount`")
    @FieldDef(label="收款人收款金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getPayeeAmount();

    void setPayeeAmount(Long payeeAmount);
}