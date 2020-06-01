package com.dili.ia.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 退款项目
 * This file was generated on 2020-05-29 16:13:04.
 */
@Table(name = "`refund_fee_item`")
public class RefundFeeItem extends BaseDomain {
    /**
     * id
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 退款单ID
     */
    @Column(name = "`refund_order_id`")
    private Long refundOrderId;

    /**
     * 退款单code
     */
    @Column(name = "`refund_order_code`")
    private String refundOrderCode;

    /**
     * 收费项ID
     */
    @Column(name = "`charge_id`")
    private Long chargeId;

    /**
     * 收费项名称
     */
    @Column(name = "`charge_name`")
    private String chargeName;

    /**
     * 金额
     */
    @Column(name = "`amount`")
    private Long amount;

    //创建时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`create_time`")
    private LocalDateTime createTime;

    //修改时间
    @JSONField(format = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`modify_time`")
    private LocalDateTime modifyTime;


    @Override
    public Long getId() {
        return id;
    }

    @Override
    public void setId(Long id) {
        this.id = id;
    }

    public Long getRefundOrderId() {
        return refundOrderId;
    }

    public void setRefundOrderId(Long refundOrderId) {
        this.refundOrderId = refundOrderId;
    }

    public String getRefundOrderCode() {
        return refundOrderCode;
    }

    public void setRefundOrderCode(String refundOrderCode) {
        this.refundOrderCode = refundOrderCode;
    }

    public Long getChargeId() {
        return chargeId;
    }

    public void setChargeId(Long chargeId) {
        this.chargeId = chargeId;
    }

    public String getChargeName() {
        return chargeName;
    }

    public void setChargeName(String chargeName) {
        this.chargeName = chargeName;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
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
}