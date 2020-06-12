package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-06-12 11:35:07.
 */
@Table(name = "`meter`")
public class Meter extends BaseDomain {
    /**
     * id
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建日期
     */
    @Column(name = "`create_time`")
    private Date createTime;

    /**
     * 修改日期
     */
    @Column(name = "`modify_time`")
    private Date modifyTime;

    /**
     * 表编号
     */
    @Column(name = "`number`")
    private String number;

    /**
     * 表类型
     */
    @Column(name = "`type`")
    private Long type;

    /**
     * 部门
     */
    @Column(name = "`department_id`")
    private Long departmentId;

    /**
     * 部门名称
     */
    @Column(name = "`department_name`")
    private String departmentName;

    /**
     * 资产类型code
     */
    @Column(name = "`assets_type`")
    private String assetsType;

    /**
     * 对应编号ID
     */
    @Column(name = "`assets_id`")
    private Long assetsId;

    /**
     * 对应编号,名称
     */
    @Column(name = "`assets_name`")
    private String assetsName;

    /**
     * 表初始值
     */
    @Column(name = "`init_amount`")
    private Long initAmount;

    /**
     * 单价
     */
    @Column(name = "`price`")
    private Long price;

    /**
     * 创建人所属于部门ID
     */
    @Column(name = "`creator_dep_id`")
    private Long creatorDepId;

    /**
     * 水电预存余额
     */
    @Column(name = "`balance`")
    private Long balance;

    /**
     * 备注
     */
    @Column(name = "`notes`")
    private String notes;

    /**
     * 创建操作员ID
     */
    @Column(name = "`creator_id`")
    private Long creatorId;

    /**
     * 创建人名称
     */
    @Column(name = "`creator`")
    private String creator;

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
     * 版本控制,乐观锁
     */
    @Column(name = "`version`")
    private Integer version;

    /**
     * 获取id
     *
     * @return id - id
     */
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getId() {
        return id;
    }

    /**
     * 设置id
     *
     * @param id id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取创建日期
     *
     * @return create_time - 创建日期
     */
    @FieldDef(label="创建日期")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建日期
     *
     * @param createTime 创建日期
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取修改日期
     *
     * @return modify_time - 修改日期
     */
    @FieldDef(label="修改日期")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getModifyTime() {
        return modifyTime;
    }

    /**
     * 设置修改日期
     *
     * @param modifyTime 修改日期
     */
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    /**
     * 获取表编号
     *
     * @return number - 表编号
     */
    @FieldDef(label="表编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getNumber() {
        return number;
    }

    /**
     * 设置表编号
     *
     * @param number 表编号
     */
    public void setNumber(String number) {
        this.number = number;
    }

    /**
     * 获取表类型
     *
     * @return type - 表类型
     */
    @FieldDef(label="表类型")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getType() {
        return type;
    }

    /**
     * 设置表类型
     *
     * @param type 表类型
     */
    public void setType(Long type) {
        this.type = type;
    }

    /**
     * 获取部门
     *
     * @return department_id - 部门
     */
    @FieldDef(label="部门")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * 设置部门
     *
     * @param departmentId 部门
     */
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * 获取部门名称
     *
     * @return department_name - 部门名称
     */
    @FieldDef(label="部门名称", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * 设置部门名称
     *
     * @param departmentName 部门名称
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * 获取资产类型code
     *
     * @return assets_type - 资产类型code
     */
    @FieldDef(label="资产类型code", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getAssetsType() {
        return assetsType;
    }

    /**
     * 设置资产类型code
     *
     * @param assetsType 资产类型code
     */
    public void setAssetsType(String assetsType) {
        this.assetsType = assetsType;
    }

    /**
     * 获取对应编号ID
     *
     * @return assets_id - 对应编号ID
     */
    @FieldDef(label="对应编号ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAssetsId() {
        return assetsId;
    }

    /**
     * 设置对应编号ID
     *
     * @param assetsId 对应编号ID
     */
    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }

    /**
     * 获取对应编号,名称
     *
     * @return assets_name - 对应编号,名称
     */
    @FieldDef(label="对应编号,名称", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getAssetsName() {
        return assetsName;
    }

    /**
     * 设置对应编号,名称
     *
     * @param assetsName 对应编号,名称
     */
    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    /**
     * 获取表初始值
     *
     * @return init_amount - 表初始值
     */
    @FieldDef(label="表初始值")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getInitAmount() {
        return initAmount;
    }

    /**
     * 设置表初始值
     *
     * @param initAmount 表初始值
     */
    public void setInitAmount(Long initAmount) {
        this.initAmount = initAmount;
    }

    /**
     * 获取单价
     *
     * @return price - 单价
     */
    @FieldDef(label="单价")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getPrice() {
        return price;
    }

    /**
     * 设置单价
     *
     * @param price 单价
     */
    public void setPrice(Long price) {
        this.price = price;
    }

    /**
     * 获取创建人所属于部门ID
     *
     * @return creator_dep_id - 创建人所属于部门ID
     */
    @FieldDef(label="创建人所属于部门ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorDepId() {
        return creatorDepId;
    }

    /**
     * 设置创建人所属于部门ID
     *
     * @param creatorDepId 创建人所属于部门ID
     */
    public void setCreatorDepId(Long creatorDepId) {
        this.creatorDepId = creatorDepId;
    }

    /**
     * 获取水电预存余额
     *
     * @return balance - 水电预存余额
     */
    @FieldDef(label="水电预存余额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getBalance() {
        return balance;
    }

    /**
     * 设置水电预存余额
     *
     * @param balance 水电预存余额
     */
    public void setBalance(Long balance) {
        this.balance = balance;
    }

    /**
     * 获取备注
     *
     * @return notes - 备注
     */
    @FieldDef(label="备注", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getNotes() {
        return notes;
    }

    /**
     * 设置备注
     *
     * @param notes 备注
     */
    public void setNotes(String notes) {
        this.notes = notes;
    }

    /**
     * 获取创建操作员ID
     *
     * @return creator_id - 创建操作员ID
     */
    @FieldDef(label="创建操作员ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorId() {
        return creatorId;
    }

    /**
     * 设置创建操作员ID
     *
     * @param creatorId 创建操作员ID
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
     * 获取版本控制,乐观锁
     *
     * @return version - 版本控制,乐观锁
     */
    @FieldDef(label="版本控制,乐观锁")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getVersion() {
        return version;
    }

    /**
     * 设置版本控制,乐观锁
     *
     * @param version 版本控制,乐观锁
     */
    public void setVersion(Integer version) {
        this.version = version;
    }
}