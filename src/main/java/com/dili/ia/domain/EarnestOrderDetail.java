package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 定金和资产关系，定金可以不挂资产，定金也可以挂在多个资产上
 * This file was generated on 2020-07-04 09:48:04.
 */
@Table(name = "`earnest_order_detail`")
public class EarnestOrderDetail extends BaseDomain {
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
     * 定金业务单ID
     */
    @Column(name = "`earnest_order_id`")
    private Long earnestOrderId;

    /**
     * 资产编号，资产包含摊位，冷库，公寓等
     */
    @Column(name = "`assets_id`")
    private Long assetsId;

    /**
     * 资产名称，资产包含摊位，冷库，公寓等
     */
    @Column(name = "`assets_name`")
    private String assetsName;

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
     * 获取定金业务单ID
     *
     * @return earnest_order_id - 定金业务单ID
     */
    @FieldDef(label="定金业务单ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getEarnestOrderId() {
        return earnestOrderId;
    }

    /**
     * 设置定金业务单ID
     *
     * @param earnestOrderId 定金业务单ID
     */
    public void setEarnestOrderId(Long earnestOrderId) {
        this.earnestOrderId = earnestOrderId;
    }

    /**
     * 获取资产编号，资产包含摊位，冷库，公寓等
     *
     * @return assets_id - 资产编号，资产包含摊位，冷库，公寓等
     */
    @FieldDef(label="资产编号，资产包含摊位，冷库，公寓等")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getAssetsId() {
        return assetsId;
    }

    /**
     * 设置资产编号，资产包含摊位，冷库，公寓等
     *
     * @param assetsId 资产编号，资产包含摊位，冷库，公寓等
     */
    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }

    /**
     * 获取资产名称，资产包含摊位，冷库，公寓等
     *
     * @return assets_name - 资产名称，资产包含摊位，冷库，公寓等
     */
    @FieldDef(label="资产名称，资产包含摊位，冷库，公寓等", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getAssetsName() {
        return assetsName;
    }

    /**
     * 设置资产名称，资产包含摊位，冷库，公寓等
     *
     * @param assetsName 资产名称，资产包含摊位，冷库，公寓等
     */
    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }
}