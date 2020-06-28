package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;

/**
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 水电费 - 表的实体类，对应数据库
 */
@Table(name = "`meter`")
public class Meter extends BaseDomain {

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
    private Integer type;

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
     * 对应编号,名称(摊位地址)
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

    @FieldDef(label="创建日期")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @FieldDef(label="修改日期")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    @FieldDef(label="表编号", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    @FieldDef(label="表类型")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    @FieldDef(label="部门")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    @FieldDef(label="部门名称", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    @FieldDef(label="资产类型code", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getAssetsType() {
        return assetsType;
    }

    public void setAssetsType(String assetsType) {
        this.assetsType = assetsType;
    }

    @FieldDef(label="对应编号ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAssetsId() {
        return assetsId;
    }

    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }

    @FieldDef(label="对应编号,名称", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    @FieldDef(label="表初始值")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getInitAmount() {
        return initAmount;
    }

    public void setInitAmount(Long initAmount) {
        this.initAmount = initAmount;
    }

    @FieldDef(label="单价")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    @FieldDef(label="创建人所属于部门ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorDepId() {
        return creatorDepId;
    }

    public void setCreatorDepId(Long creatorDepId) {
        this.creatorDepId = creatorDepId;
    }

    @FieldDef(label="水电预存余额")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    @FieldDef(label="备注", maxLength = 250)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    @FieldDef(label="创建操作员ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    @FieldDef(label="创建人名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    @FieldDef(label="市场Id")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    @FieldDef(label="市场CODE", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getMarketCode() {
        return marketCode;
    }

    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }


    @FieldDef(label="版本控制,乐观锁")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}