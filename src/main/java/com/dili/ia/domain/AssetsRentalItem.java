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
 * 资产出租预设摊位关联表
 * This file was generated on 2020-11-25 15:03:41.
 */
@Table(name = "`assets_rental_item`")
public class AssetsRentalItem extends BaseDomain {
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 资产出租预设ID
     */
    @Column(name = "`stall_rent_preset_id`")
    private Long stallRentPresetId;

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
     * 资产类型 1：摊位 2：冷库
     */
    @Column(name = "`assets_type`")
    private Integer assetsType;

    /**
     * 乐观锁，版本号
     */
    @Column(name = "`version`")
    private Integer version;

    /**
     * 获取资产出租预设ID
     *
     * @return stall_rent_preset_id - 资产出租预设ID
     */
    @FieldDef(label="资产出租预设ID")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getStallRentPresetId() {
        return stallRentPresetId;
    }

    /**
     * 设置资产出租预设ID
     *
     * @param stallRentPresetId 资产出租预设ID
     */
    public void setStallRentPresetId(Long stallRentPresetId) {
        this.stallRentPresetId = stallRentPresetId;
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
    @FieldDef(label="资产名称", maxLength = 20)
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
     * 获取资产类型 1：摊位 2：冷库
     *
     * @return assets_type - 资产类型 1：摊位 2：冷库
     */
    @FieldDef(label="资产类型 1：摊位 2：冷库")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getAssetsType() {
        return assetsType;
    }

    /**
     * 设置资产类型 1：摊位 2：冷库
     *
     * @param assetsType 资产类型 1：摊位 2：冷库
     */
    public void setAssetsType(Integer assetsType) {
        this.assetsType = assetsType;
    }

    /**
     * 获取乐观锁，版本号
     *
     * @return version - 乐观锁，版本号
     */
    @FieldDef(label="乐观锁，版本号")
    @EditMode(editor = FieldEditor.Number, required = false)
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