package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 明细流水
 * This file was generated on 2020-07-04 09:48:04.
 */
@Table(name = "`transaction_details`")
public class TransactionDetails extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    private Date modifyTime;

    /**
     * 交易流水号
     */
    @Column(name = "`code`")
    private String code;

    /**
     * 场景：1-交费,2-抵扣消费,3-定金转入,4-定金转出,5-退款,6-冻结，7-解冻,8-转抵转入
     */
    @Column(name = "`scene_type`")
    private Integer sceneType;

    /**
     * 1-定金，2-转抵，3-保证金
     */
    @Column(name = "`item_type`")
    private Integer itemType;

    /**
     * 摊位租赁，定金管理等等
     */
    @Column(name = "`biz_type`")
    private Integer bizType;

    /**
     * 发生订单ID
     */
    @Column(name = "`order_id`")
    private Long orderId;

    /**
     * 发生订单业务单编号
     */
    @Column(name = "`order_code`")
    private String orderCode;

    /**
     * 发生交易的客户ID
     */
    @Column(name = "`customer_id`")
    private Long customerId;

    /**
     * 客户名称
     */
    @Column(name = "`customer_name`")
    private String customerName;

    /**
     * 发生交易的客户证件号
     */
    @Column(name = "`certificate_number`")
    private String certificateNumber;

    /**
     * 发生交易的客户电话
     */
    @Column(name = "`customer_cellphone`")
    private String customerCellphone;

    /**
     * 金额，收入为正，支付为负
     */
    @Column(name = "`amount`")
    private Long amount;

    /**
     * 创建人ID
     */
    @Column(name = "`creator_id`")
    private Long creatorId;

    /**
     * 创建人名称
     */
    @Column(name = "`creator`")
    private String creator;

    /**
     * 详情记录，
     */
    @Column(name = "`notes`")
    private String notes;

    /**
     * 市场ID
     */
    @Column(name = "`market_id`")
    private Long marketId;

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
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取交易流水号
     *
     * @return code - 交易流水号
     */
    @FieldDef(label="交易流水号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置交易流水号
     *
     * @param code 交易流水号
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取场景：1-交费,2-抵扣消费,3-定金转入,4-定金转出,5-退款,6-冻结，7-解冻,8-转抵转入
     *
     * @return scene_type - 场景：1-交费,2-抵扣消费,3-定金转入,4-定金转出,5-退款,6-冻结，7-解冻,8-转抵转入
     */
    @FieldDef(label="场景：1-交费,2-抵扣消费,3-定金转入,4-定金转出,5-退款,6-冻结，7-解冻,8-转抵转入")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getSceneType() {
        return sceneType;
    }

    /**
     * 设置场景：1-交费,2-抵扣消费,3-定金转入,4-定金转出,5-退款,6-冻结，7-解冻,8-转抵转入
     *
     * @param sceneType 场景：1-交费,2-抵扣消费,3-定金转入,4-定金转出,5-退款,6-冻结，7-解冻,8-转抵转入
     */
    public void setSceneType(Integer sceneType) {
        this.sceneType = sceneType;
    }

    /**
     * 获取1-定金，2-转抵，3-保证金
     *
     * @return item_type - 1-定金，2-转抵，3-保证金
     */
    @FieldDef(label="1-定金，2-转抵，3-保证金")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getItemType() {
        return itemType;
    }

    /**
     * 设置1-定金，2-转抵，3-保证金
     *
     * @param itemType 1-定金，2-转抵，3-保证金
     */
    public void setItemType(Integer itemType) {
        this.itemType = itemType;
    }

    /**
     * 获取摊位租赁，定金管理等等
     *
     * @return biz_type - 摊位租赁，定金管理等等
     */
    @FieldDef(label="摊位租赁，定金管理等等")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getBizType() {
        return bizType;
    }

    /**
     * 设置摊位租赁，定金管理等等
     *
     * @param bizType 摊位租赁，定金管理等等
     */
    public void setBizType(Integer bizType) {
        this.bizType = bizType;
    }

    /**
     * 获取发生订单ID
     *
     * @return order_id - 发生订单ID
     */
    @FieldDef(label="发生订单ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getOrderId() {
        return orderId;
    }

    /**
     * 设置发生订单ID
     *
     * @param orderId 发生订单ID
     */
    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取发生订单业务单编号
     *
     * @return order_code - 发生订单业务单编号
     */
    @FieldDef(label="发生订单业务单编号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getOrderCode() {
        return orderCode;
    }

    /**
     * 设置发生订单业务单编号
     *
     * @param orderCode 发生订单业务单编号
     */
    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    /**
     * 获取发生交易的客户ID
     *
     * @return customer_id - 发生交易的客户ID
     */
    @FieldDef(label="发生交易的客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * 设置发生交易的客户ID
     *
     * @param customerId 发生交易的客户ID
     */
    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    /**
     * 获取客户名称
     *
     * @return customer_name - 客户名称
     */
    @FieldDef(label="客户名称", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerName() {
        return customerName;
    }

    /**
     * 设置客户名称
     *
     * @param customerName 客户名称
     */
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取发生交易的客户证件号
     *
     * @return certificate_number - 发生交易的客户证件号
     */
    @FieldDef(label="发生交易的客户证件号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCertificateNumber() {
        return certificateNumber;
    }

    /**
     * 设置发生交易的客户证件号
     *
     * @param certificateNumber 发生交易的客户证件号
     */
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    /**
     * 获取发生交易的客户电话
     *
     * @return customer_cellphone - 发生交易的客户电话
     */
    @FieldDef(label="发生交易的客户电话", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerCellphone() {
        return customerCellphone;
    }

    /**
     * 设置发生交易的客户电话
     *
     * @param customerCellphone 发生交易的客户电话
     */
    public void setCustomerCellphone(String customerCellphone) {
        this.customerCellphone = customerCellphone;
    }

    /**
     * 获取金额，收入为正，支付为负
     *
     * @return amount - 金额，收入为正，支付为负
     */
    @FieldDef(label="金额，收入为正，支付为负")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAmount() {
        return amount;
    }

    /**
     * 设置金额，收入为正，支付为负
     *
     * @param amount 金额，收入为正，支付为负
     */
    public void setAmount(Long amount) {
        this.amount = amount;
    }

    /**
     * 获取创建人ID
     *
     * @return creator_id - 创建人ID
     */
    @FieldDef(label="创建人ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * 设置创建人ID
     *
     * @param creatorId 创建人ID
     */
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    /**
     * 获取创建人名称
     *
     * @return creator - 创建人名称
     */
    @FieldDef(label="创建人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人名称
     *
     * @param creator 创建人名称
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    /**
     * 获取详情记录，
     *
     * @return notes - 详情记录，
     */
    @FieldDef(label="详情记录，", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getNotes() {
        return notes;
    }

    /**
     * 设置详情记录，
     *
     * @param notes 详情记录，
     */
    public void setNotes(String notes) {
        this.notes = notes;
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
}