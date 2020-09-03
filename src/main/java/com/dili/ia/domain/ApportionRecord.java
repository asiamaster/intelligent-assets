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
 * 
 * This file was generated on 2020-07-24 11:15:42.
 */
@Table(name = "`apportion_record`")
public class ApportionRecord extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //创建时间
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`create_time`")
    private LocalDateTime createTime;

    /**
     * 订单项ID
     */
    @Column(name = "`lease_order_item_id`")
    private Long leaseOrderItemId;

    /**
     * 订单ID
     */
    @Column(name = "`lease_order_id`")
    private Long leaseOrderId;

    /**
     * 缴费单单ID
     */
    @Column(name = "`payment_order_id`")
    private Long paymentOrderId;

    /**
     * 收费项ID
     */
    @Column(name = "`charge_item_id`")
    private Long chargeItemId;

    /**
     * 收费项名称
     */
    @Column(name = "`charge_item_name`")
    private String chargeItemName;

    /**
     * 分摊金额
     */
    @Column(name = "`amount`")
    private Long amount;

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

    public Long getLeaseOrderId() {
        return leaseOrderId;
    }

    public void setLeaseOrderId(Long leaseOrderId) {
        this.leaseOrderId = leaseOrderId;
    }

    public Long getPaymentOrderId() {
        return paymentOrderId;
    }

    public void setPaymentOrderId(Long paymentOrderId) {
        this.paymentOrderId = paymentOrderId;
    }

    public Long getLeaseOrderItemId() {
        return leaseOrderItemId;
    }

    public void setLeaseOrderItemId(Long leaseOrderItemId) {
        this.leaseOrderItemId = leaseOrderItemId;
    }

    /**
     * 获取收费项ID
     *
     * @return charge_item_id - 收费项ID
     */
    @FieldDef(label="收费项ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getChargeItemId() {
        return chargeItemId;
    }

    /**
     * 设置收费项ID
     *
     * @param chargeItemId 收费项ID
     */
    public void setChargeItemId(Long chargeItemId) {
        this.chargeItemId = chargeItemId;
    }

    /**
     * 获取收费项名称
     *
     * @return charge_item_name - 收费项名称
     */
    @FieldDef(label="收费项名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getChargeItemName() {
        return chargeItemName;
    }

    /**
     * 设置收费项名称
     *
     * @param chargeItemName 收费项名称
     */
    public void setChargeItemName(String chargeItemName) {
        this.chargeItemName = chargeItemName;
    }

    /**
     * 获取分摊金额
     *
     * @return amount - 分摊金额
     */
    @FieldDef(label="分摊金额")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getAmount() {
        return amount;
    }

    /**
     * 设置分摊金额
     *
     * @param amount 分摊金额
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }
}