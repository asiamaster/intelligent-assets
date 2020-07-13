package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 精品停车免费时长设置表
 * This file was generated on 2020-07-13 10:49:05.
 */
@Table(name = "`boutique_free_sets`")
public class BoutiqueFreeSets extends BaseDomain {
    /**
     * id
     */
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
     * 车型名称
     */
    @Column(name = "`car_type_name`")
    private String carTypeName;

    /**
     * 免费小时数
     */
    @Column(name = "`free_hours`")
    private Integer freeHours;

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
     * 获取车型名称
     *
     * @return car_type_name - 车型名称
     */
    @FieldDef(label="车型名称", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCarTypeName() {
        return carTypeName;
    }

    /**
     * 设置车型名称
     *
     * @param carTypeName 车型名称
     */
    public void setCarTypeName(String carTypeName) {
        this.carTypeName = carTypeName;
    }

    /**
     * 获取免费小时数
     *
     * @return free_hours - 免费小时数
     */
    @FieldDef(label="免费小时数")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getFreeHours() {
        return freeHours;
    }

    /**
     * 设置免费小时数
     *
     * @param freeHours 免费小时数
     */
    public void setFreeHours(Integer freeHours) {
        this.freeHours = freeHours;
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