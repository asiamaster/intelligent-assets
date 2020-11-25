package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import tk.mybatis.mapper.annotation.Version;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 由MyBatis Generator工具自动生成
 * 缴费单
 * This file was generated on 2020-07-03 15:46:47.
 */
@Table(name = "`payment_order`")
public class PaymentOrder extends BaseDomain implements Cloneable{
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 编号
     */
    @Column(name = "`code`")
    private String code;

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
     * 客户ID
     */
    @Column(name = "`customer_id`")
    private Long customerId;

    /**
     * 客户名称
     */
    @Column(name = "`customer_name`")
    private String customerName;


    /**
     * 业务单ID
     */
    @Column(name = "`business_id`")
    private Long businessId;

    /**
     * 业务单编号
     */
    @Column(name = "`business_code`")
    private String businessCode;

    /**
     * 业务类型
     */
    @Column(name = "`biz_type`")
    private String bizType;

    /**
     * 状态
     */
    @Column(name = "`state`")
    private Integer state;

    /**
     * 业务是否结清（1：是 0：否）
     */
    @Column(name = "`is_settle`")
    private Integer isSettle;

    /**
     * 是否红冲（1：是 0：否）
     */
    @Column(name = "`is_reverse`")
    private Integer isReverse;

    /**
     * 金额
     */
    @Column(name = "`amount`")
    private Long amount;

    /**
     * 创建人ID
     */
    @Column(name = "`creator_id`")
    private Long creatorId;

    /**
     * 创建人
     */
    @Column(name = "`creator`")
    private String creator;

    /**
     * 交费时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`payed_time`")
    private LocalDateTime payedTime;

    /**
     * 结算编号
     */
    @Column(name = "`settlement_code`")
    private String settlementCode;

    /**
     * 结算方式（冗余 来自结算）
     */
    @Column(name = "`settlement_way`")
    private Integer settlementWay;

    /**
     * 结算员
     */
    @Column(name = "`settlement_operator`")
    private String settlementOperator;

    @Column(name = "`market_id`")
    private Long marketId;

    /**
     * 市场编码
     */
    @Column(name = "`market_code`")
    private String marketCode;

    /**
     * 父ID（红冲原单ID）
     */
    @Column(name = "`parent_id`")
    private Long parentId;

    /**
     * 乐观锁，版本号
     */
    @Version
    @Column(name = "`version`")
    private Integer version;

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
     * 获取编号
     *
     * @return code - 编号
     */
    @FieldDef(label="编号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置编号
     *
     * @param code 编号
     */
    public void setCode(String code) {
        this.code = code;
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

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    /**
     * 获取业务单ID
     *
     * @return business_id - 业务单ID
     */
    @FieldDef(label="业务单ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getBusinessId() {
        return businessId;
    }

    /**
     * 设置业务单ID
     *
     * @param businessId 业务单ID
     */
    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    /**
     * 获取业务单编号
     *
     * @return business_code - 业务单编号
     */
    @FieldDef(label="业务单编号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getBusinessCode() {
        return businessCode;
    }

    /**
     * 设置业务单编号
     *
     * @param businessCode 业务单编号
     */
    public void setBusinessCode(String businessCode) {
        this.businessCode = businessCode;
    }

    /**
     * 获取业务类型
     *
     * @return biz_type - 业务类型
     */
    @FieldDef(label="业务类型")
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getBizType() {
        return bizType;
    }

    /**
     * 设置业务类型
     *
     * @param bizType 业务类型
     */
    public void setBizType(String bizType) {
        this.bizType = bizType;
    }

    /**
     * 获取状态
     *
     * @return state - 状态
     */
    @FieldDef(label="状态")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getState() {
        return state;
    }

    /**
     * 设置状态
     *
     * @param state 状态
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * 获取业务是否结清（1：是 0：否）
     *
     * @return is_settle - 业务是否结清（1：是 0：否）
     */
    @FieldDef(label="业务是否结清（1：是 0：否）")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getIsSettle() {
        return isSettle;
    }

    /**
     * 设置业务是否结清（1：是 0：否）
     *
     * @param isSettle 业务是否结清（1：是 0：否）
     */
    public void setIsSettle(Integer isSettle) {
        this.isSettle = isSettle;
    }

    public Integer getIsReverse() {
        return isReverse;
    }

    public void setIsReverse(Integer isReverse) {
        this.isReverse = isReverse;
    }

    /**
     * 获取金额
     *
     * @return amount - 金额
     */
    @FieldDef(label="金额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAmount() {
        return amount;
    }

    /**
     * 设置金额
     *
     * @param amount 金额
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
     * 获取创建人
     *
     * @return creator - 创建人
     */
    @FieldDef(label="创建人", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCreator() {
        return creator;
    }

    /**
     * 设置创建人
     *
     * @param creator 创建人
     */
    public void setCreator(String creator) {
        this.creator = creator;
    }

    public LocalDateTime getPayedTime() {
        return payedTime;
    }

    public void setPayedTime(LocalDateTime payedTime) {
        this.payedTime = payedTime;
    }

    /**
     * 获取结算编号
     *
     * @return settlement_code - 结算编号
     */
    @FieldDef(label="结算编号", maxLength = 30)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getSettlementCode() {
        return settlementCode;
    }

    /**
     * 设置结算编号
     *
     * @param settlementCode 结算编号
     */
    public void setSettlementCode(String settlementCode) {
        this.settlementCode = settlementCode;
    }

    /**
     * 获取结算方式（冗余 来自结算）
     *
     * @return settlement_way - 结算方式（冗余 来自结算）
     */
    @FieldDef(label="结算方式（冗余 来自结算）")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getSettlementWay() {
        return settlementWay;
    }

    /**
     * 设置结算方式（冗余 来自结算）
     *
     * @param settlementWay 结算方式（冗余 来自结算）
     */
    public void setSettlementWay(Integer settlementWay) {
        this.settlementWay = settlementWay;
    }

    /**
     * 获取结算员
     *
     * @return settlement_operator - 结算员
     */
    @FieldDef(label="结算员", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getSettlementOperator() {
        return settlementOperator;
    }

    /**
     * 设置结算员
     *
     * @param settlementOperator 结算员
     */
    public void setSettlementOperator(String settlementOperator) {
        this.settlementOperator = settlementOperator;
    }

    /**
     * @return market_id
     */
    @FieldDef(label="marketId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    /**
     * @param marketId
     */
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    /**
     * 获取市场编码
     *
     * @return market_code - 市场编码
     */
    @FieldDef(label="市场编码", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getMarketCode() {
        return marketCode;
    }

    /**
     * 设置市场编码
     *
     * @param marketCode 市场编码
     */
    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    /**
     * 获取乐观锁，版本号
     *
     * @return version - 乐观锁，版本号
     */
    @FieldDef(label="乐观锁，版本号")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置乐观锁，版本号
     *
     * @param version 乐观锁，版本号
     */
    public void setVersion(Integer version) {
        this.version = version;
    }

    @Override
    public PaymentOrder clone() {
        try {
            return (PaymentOrder) super.clone();
        } catch(CloneNotSupportedException e) {
            throw new AssertionError(); // Can't happen
        }
    }

}