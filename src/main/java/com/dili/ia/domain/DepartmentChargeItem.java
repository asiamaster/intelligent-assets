package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;

import java.time.LocalDateTime;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-07-21 18:08:01.
 */
@Table(name = "`department_charge_item`")
public class DepartmentChargeItem extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 创建时间
     */
    @Column(name = "`create_time`")
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    private LocalDateTime modifyTime;

    /**
     * 业务所属部门ID
     */
    @Column(name = "`department_id`")
    private Long departmentId;

    /**
     * 业务所属部门名称
     */
    @Column(name = "`department_name`")
    private String departmentName;

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
     * 商户Id
     */
    @Column(name = "`mch_id`")
    private Long mchId;

    /**
     * 商户名称
     */
    @Column(name = "`mch_name`")
    private String mchName;

    /**
     * 版本控制,乐观锁
     */
    @Column(name = "`version`")
    private Integer version;

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
     * 获取业务所属部门ID
     *
     * @return department_id - 业务所属部门ID
     */
    @FieldDef(label="业务所属部门ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getDepartmentId() {
        return departmentId;
    }

    /**
     * 设置业务所属部门ID
     *
     * @param departmentId 业务所属部门ID
     */
    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    /**
     * 获取业务所属部门名称
     *
     * @return department_name - 业务所属部门名称
     */
    @FieldDef(label="业务所属部门名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getDepartmentName() {
        return departmentName;
    }

    /**
     * 设置业务所属部门名称
     *
     * @param departmentName 业务所属部门名称
     */
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    /**
     * 获取收费项ID
     *
     * @return charge_item_id - 收费项ID
     */
    @FieldDef(label="收费项ID")
    @EditMode(editor = FieldEditor.Text, required = false)
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

    public Long getMchId() {
        return mchId;
    }

    public void setMchId(Long mchId) {
        this.mchId = mchId;
    }

    public String getMchName() {
        return mchName;
    }

    public void setMchName(String mchName) {
        this.mchName = mchName;
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