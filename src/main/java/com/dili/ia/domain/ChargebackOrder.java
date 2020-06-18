package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 客户缴纳的保证金，有时在出现违约的情况，市场在退款客户保证金的时候会对保证金进行扣款处--罚款转为市场收入
 * This file was generated on 2020-06-18 11:36:18.
 */
@Table(name = "`chargeback_order`")
public class ChargebackOrder extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "`create_time`")
    private Date createTime;

    @Column(name = "`modify_time`")
    private Date modifyTime;

    /**
     * 退款单ID
     */
    @Column(name = "`refund_order_id`")
    private Long refundOrderId;

    @Column(name = "`deposit_order_id`")
    private Long depositOrderId;

    /**
     * 扣款金额
     */
    @Column(name = "`amount`")
    private Long amount;

    /**
     * 扣款收费项ID
     */
    @Column(name = "`charge_item_id`")
    private Long chargeItemId;

    /**
     * 扣款收费项名称
     */
    @Column(name = "`charge_item_name`")
    private String chargeItemName;

    /**
     * 退款处理成功及扣款单处理成功时间
     */
    @Column(name = "`settlement_time`")
    private Date settlementTime;

    @Column(name = "`state`")
    private Integer state;

    /**
     * 市场ID
     */
    @Column(name = "`market_id`")
    private Long marketId;

    /**
     * 市场CODE
     */
    @Column(name = "`market_code`")
    private String marketCode;

    /**
     * 版本控制,乐观锁
     */
    @Column(name = "`version`")
    private Long version;

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

    /**
     * @return create_time
     */
    @FieldDef(label="createTime")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return modify_time
     */
    @FieldDef(label="modifyTime")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * @param modifyTime
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
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
     * @return deposit_order_id
     */
    @FieldDef(label="depositOrderId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getDepositOrderId() {
        return depositOrderId;
    }

    /**
     * @param depositOrderId
     */
    public void setDepositOrderId(Long depositOrderId) {
        this.depositOrderId = depositOrderId;
    }

    /**
     * 获取扣款金额
     *
     * @return amount - 扣款金额
     */
    @FieldDef(label="扣款金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAmount() {
        return amount;
    }

    /**
     * 设置扣款金额
     *
     * @param amount 扣款金额
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * 获取扣款收费项ID
     *
     * @return charge_item_id - 扣款收费项ID
     */
    @FieldDef(label="扣款收费项ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getChargeItemId() {
        return chargeItemId;
    }

    /**
     * 设置扣款收费项ID
     *
     * @param chargeItemId 扣款收费项ID
     */
    public void setChargeItemId(Long chargeItemId) {
        this.chargeItemId = chargeItemId;
    }

    /**
     * 获取扣款收费项名称
     *
     * @return charge_item_name - 扣款收费项名称
     */
    @FieldDef(label="扣款收费项名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getChargeItemName() {
        return chargeItemName;
    }

    /**
     * 设置扣款收费项名称
     *
     * @param chargeItemName 扣款收费项名称
     */
    public void setChargeItemName(String chargeItemName) {
        this.chargeItemName = chargeItemName;
    }

    /**
     * 获取退款处理成功及扣款单处理成功时间
     *
     * @return settlement_time - 退款处理成功及扣款单处理成功时间
     */
    @FieldDef(label="退款处理成功及扣款单处理成功时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getSettlementTime() {
        return settlementTime;
    }

    /**
     * 设置退款处理成功及扣款单处理成功时间
     *
     * @param settlementTime 退款处理成功及扣款单处理成功时间
     */
    public void setSettlementTime(Date settlementTime) {
        this.settlementTime = settlementTime;
    }

    /**
     * @return state
     */
    @FieldDef(label="state")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getState() {
        return state;
    }

    /**
     * @param state
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取市场ID
     *
     * @return market_id - 市场ID
     */
    @FieldDef(label="市场ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    /**
     * 设置市场ID
     *
     * @param marketId 市场ID
     */
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    /**
     * 获取市场CODE
     *
     * @return market_code - 市场CODE
     */
    @FieldDef(label="市场CODE", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getMarketCode() {
        return marketCode;
    }

    /**
     * 设置市场CODE
     *
     * @param marketCode 市场CODE
     */
    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    /**
     * 获取版本控制,乐观锁
     *
     * @return version - 版本控制,乐观锁
     */
    @FieldDef(label="版本控制,乐观锁")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getVersion() {
        return version;
    }

    /**
     * 设置版本控制,乐观锁
     *
     * @param version 版本控制,乐观锁
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}