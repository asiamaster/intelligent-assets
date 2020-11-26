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
 * @author:       xiaosa
 * @date:         2020/11/25
 * @version:      农批业务系统重构
 * @description:  资产出租预设
 */
@Table(name = "`assets_rental`")
public class AssetsRental extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 一批资产属于一个批次的批次号
     */
    @Column(name = "`batch_id`")
    private Long batchId;

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
     * 预设名称
     */
    @Column(name = "`name`")
    private String name;

    /**
     * 经营范围CODE
     */
    @Column(name = "`engage_code`")
    private String engageCode;

    /**
     * 经营范围
     */
    @Column(name = "`engage_name`")
    private String engageName;

    /**
     * 品类id
     */
    @Column(name = "`category_id`")
    private String categoryId;

    /**
     * 品类名称
     */
    @Column(name = "`category_name`")
    private String categoryName;

    /**
     * 租赁形式CODE
     */
    @Column(name = "`lease_term_code`")
    private String leaseTermCode;

    /**
     * 租赁形式
     */
    @Column(name = "`lease_term_name`")
    private String leaseTermName;

    /**
     * 租赁天数
     */
    @Column(name = "`lease_days`")
    private Integer leaseDays;

    /**
     * 开始时间
     */
    @Column(name = "`start_time`")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Column(name = "`end_time`")
    private LocalDateTime endTime;

    /**
     * （1启用,2禁用）
     */
    @Column(name = "`state`")
    private Integer state;

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
     * 乐观锁，版本号
     */
    @Column(name = "`version`")
    private Integer version;


    public Long getBatchId() {
        return batchId;
    }

    public void setBatchId(Long batchId) {
        this.batchId = batchId;
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
     * 获取预设名称
     *
     * @return name - 预设名称
     */
    @FieldDef(label="预设名称", maxLength = 50)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getName() {
        return name;
    }

    /**
     * 设置预设名称
     *
     * @param name 预设名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取经营范围CODE
     *
     * @return engage_code - 经营范围CODE
     */
    @FieldDef(label="经营范围CODE", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getEngageCode() {
        return engageCode;
    }

    /**
     * 设置经营范围CODE
     *
     * @param engageCode 经营范围CODE
     */
    public void setEngageCode(String engageCode) {
        this.engageCode = engageCode;
    }

    /**
     * 获取经营范围
     *
     * @return engage_name - 经营范围
     */
    @FieldDef(label="经营范围", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getEngageName() {
        return engageName;
    }

    /**
     * 设置经营范围
     *
     * @param engageName 经营范围
     */
    public void setEngageName(String engageName) {
        this.engageName = engageName;
    }

    /**
     * 获取品类id
     *
     * @return category_id - 品类id
     */
    @FieldDef(label="品类id", maxLength = 400)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCategoryId() {
        return categoryId;
    }

    /**
     * 设置品类id
     *
     * @param categoryId 品类id
     */
    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    /**
     * 获取品类名称
     *
     * @return category_name - 品类名称
     */
    @FieldDef(label="品类名称", maxLength = 400)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCategoryName() {
        return categoryName;
    }

    /**
     * 设置品类名称
     *
     * @param categoryName 品类名称
     */
    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    /**
     * 获取租赁形式CODE
     *
     * @return lease_term_code - 租赁形式CODE
     */
    @FieldDef(label="租赁形式CODE", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getLeaseTermCode() {
        return leaseTermCode;
    }

    /**
     * 设置租赁形式CODE
     *
     * @param leaseTermCode 租赁形式CODE
     */
    public void setLeaseTermCode(String leaseTermCode) {
        this.leaseTermCode = leaseTermCode;
    }

    /**
     * 获取租赁形式
     *
     * @return lease_term_name - 租赁形式
     */
    @FieldDef(label="租赁形式", maxLength = 120)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getLeaseTermName() {
        return leaseTermName;
    }

    /**
     * 设置租赁形式
     *
     * @param leaseTermName 租赁形式
     */
    public void setLeaseTermName(String leaseTermName) {
        this.leaseTermName = leaseTermName;
    }

    /**
     * 获取租赁天数
     *
     * @return lease_days - 租赁天数
     */
    @FieldDef(label="租赁天数")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getLeaseDays() {
        return leaseDays;
    }

    /**
     * 设置租赁天数
     *
     * @param leaseDays 租赁天数
     */
    public void setLeaseDays(Integer leaseDays) {
        this.leaseDays = leaseDays;
    }

    /**
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    @FieldDef(label="开始时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    @FieldDef(label="结束时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    /**
     * 获取（1启用,2禁用）
     *
     * @return state - （1启用,2禁用）
     */
    @FieldDef(label="（1启用,2禁用）")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Integer getState() {
        return state;
    }

    /**
     * 设置（1启用,2禁用）
     *
     * @param state （1启用,2禁用）
     */
    public void setState(Integer state) {
        this.state = state;
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
}