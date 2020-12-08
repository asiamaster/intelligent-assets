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
 * 
 * This file was generated on 2020-06-29 15:15:26.
 */
@Table(name = "`deposit_balance`")
public class DepositBalance extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "`create_time`")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
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
     * 客户证件号
     */
    @Column(name = "`certificate_number`")
    private String certificateNumber;

    /**
     * 客户电话
     */
    @Column(name = "`customer_cellphone`")
    private String customerCellphone;

    /**
     * 保证金类型，来源数据字典
     */
    @Column(name = "`type_code`")
    private String typeCode;

    /**
     * 保证金类型名称
     */
    @Column(name = "`type_name`")
    private String typeName;

    /**
     * 资产类型
     */
    @Column(name = "`assets_type`")
    private Integer assetsType;

    /**
     * 资产ID
     */
    @Column(name = "`assets_id`")
    private Long assetsId;

    /**
     * 资产名称
     */
    @Column(name = "`assets_name`")
    private String assetsName;

    /**
     * 保证金余额
     */
    @Column(name = "`balance`")
    private Long balance;

    /**
     * 市场Id
     */
    @Column(name = "`market_id`")
    private Long marketId;

    /**
     * 市场CODE
     */
    @Column(name = "`market_code`")
    private String marketCode;

    /**
     * 商户ID
     */
    @Column(name = "`mch_id`")
    private Long mchId;

    /**
     * 版本控制,乐观锁
     */
    @Version
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
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改时间
     *
     * @return modify_time - 修改时间
     */
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改时间
     *
     * @param modifyTime 修改时间
     */
    public void setModifyTime(LocalDateTime modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取客户ID
     *
     * @return customer_id - 客户ID
     */
    @FieldDef(label="客户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCustomerId() {
        return customerId;
    }

    /**
     * 设置客户ID
     *
     * @param customerId 客户ID
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
     * 获取客户证件号
     *
     * @return certificate_number - 客户证件号
     */
    @FieldDef(label="客户证件号", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCertificateNumber() {
        return certificateNumber;
    }

    /**
     * 设置客户证件号
     *
     * @param certificateNumber 客户证件号
     */
    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    /**
     * 获取客户电话
     *
     * @return customer_cellphone - 客户电话
     */
    @FieldDef(label="客户电话", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCustomerCellphone() {
        return customerCellphone;
    }

    /**
     * 设置客户电话
     *
     * @param customerCellphone 客户电话
     */
    public void setCustomerCellphone(String customerCellphone) {
        this.customerCellphone = customerCellphone;
    }

    /**
     * 获取保证金类型，来源数据字典
     *
     * @return type_code - 保证金类型，来源数据字典
     */
    @FieldDef(label="保证金类型，来源数据字典", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * 设置保证金类型，来源数据字典
     *
     * @param typeCode 保证金类型，来源数据字典
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * 获取保证金类型名称
     *
     * @return type_name - 保证金类型名称
     */
    @FieldDef(label="保证金类型名称", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getTypeName() {
        return typeName;
    }

    /**
     * 设置保证金类型名称
     *
     * @param typeName 保证金类型名称
     */
    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    /**
     * 获取资产类型
     *
     * @return assets_type - 资产类型
     */
    @FieldDef(label="资产类型", maxLength = 50)
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getAssetsType() {
        return assetsType;
    }

    /**
     * 设置资产类型
     *
     * @param assetsType 资产类型
     */
    public void setAssetsType(Integer assetsType) {
        this.assetsType = assetsType;
    }

    /**
     * 获取资产ID
     *
     * @return assets_id - 资产ID
     */
    @FieldDef(label="资产ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAssetsId() {
        return assetsId;
    }

    /**
     * 设置资产ID
     *
     * @param assetsId 资产ID
     */
    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }

    /**
     * 获取资产名称
     *
     * @return assets_name - 资产名称
     */
    @FieldDef(label="资产名称", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getAssetsName() {
        return assetsName;
    }

    /**
     * 设置资产名称
     *
     * @param assetsName 资产名称
     */
    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    /**
     * 获取保证金余额
     *
     * @return balance - 保证金余额
     */
    @FieldDef(label="保证金余额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getBalance() {
        return balance;
    }

    /**
     * 设置保证金余额
     *
     * @param balance 保证金余额
     */
    public void setBalance(Long balance) {
        this.balance = balance;
    }

    /**
     * 获取市场Id
     *
     * @return market_id - 市场Id
     */
    @FieldDef(label="市场Id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    /**
     * 设置市场Id
     *
     * @param marketId 市场Id
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
     * 获取商户ID
     *
     * @return mch_id - 商户ID
     */
    @FieldDef(label="商户ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMchId() {
        return mchId;
    }

    /**
     * 设置商户ID
     *
     * @param mchId 商户ID
     */
    public void setMchId(Long mchId) {
        this.mchId = mchId;
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