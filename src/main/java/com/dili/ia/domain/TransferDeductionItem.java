package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 转抵项
 * This file was generated on 2020-07-03 16:06:17.
 */
@Table(name = "`transfer_deduction_item`")
public class TransferDeductionItem extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`create_time`")
    private LocalDateTime createTime;

    //修改时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`modify_time`")
    private LocalDateTime modifyTime;

    /**
     * 收款人ID
     */
    @Column(name = "`payee_id`")
    private Long payeeId;

    /**
     * 收款人客户名称
     */
    @Column(name = "`payee`")
    private String payee;


    /**
     * 收款人证件号
     */
    @Column(name = "`payee_certificate_number`")
    private String payeeCertificateNumber;


    /**
     * 退款单ID
     */
    @Column(name = "`refund_order_id`")
    private Long refundOrderId;

    /**
     * 收款人收款金额
     */
    @Column(name = "`payee_amount`")
    private Long payeeAmount;

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

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取收款人ID
     *
     * @return payee_id - 收款人ID
     */
    @FieldDef(label="收款人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getPayeeId() {
        return payeeId;
    }

    /**
     * 设置收款人ID
     *
     * @param payeeId 收款人ID
     */
    public void setPayeeId(Long payeeId) {
        this.payeeId = payeeId;
    }

    /**
     * 获取收款人客户名称
     *
     * @return payee - 收款人客户名称
     */
    @FieldDef(label="收款人客户名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getPayee() {
        return payee;
    }

    /**
     * 设置收款人客户名称
     *
     * @param payee 收款人客户名称
     */
    public void setPayee(String payee) {
        this.payee = payee;
    }

    public String getPayeeCertificateNumber() {
        return payeeCertificateNumber;
    }

    public void setPayeeCertificateNumber(String payeeCertificateNumber) {
        this.payeeCertificateNumber = payeeCertificateNumber;
    }

    /**
     * 获取退款单ID
     *
     * @return refund_order_id - 退款单ID
     */
    @FieldDef(label="退款单ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getRefundOrderId() {
        return refundOrderId;
    }

    /**
     * 设置退款单ID
     *
     * @param refundOrderId 退款单ID
     */
    public void setRefundOrderId(Long refundOrderId) {
        this.refundOrderId = refundOrderId;
    }

    /**
     * 获取收款人收款金额
     *
     * @return payee_amount - 收款人收款金额
     */
    @FieldDef(label="收款人收款金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getPayeeAmount() {
        return payeeAmount;
    }

    /**
     * 设置收款人收款金额
     *
     * @param payeeAmount 收款人收款金额
     */
    public void setPayeeAmount(Long payeeAmount) {
        this.payeeAmount = payeeAmount;
    }
}