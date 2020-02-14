package com.dili.ia.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import java.util.Date;
import javax.persistence.*;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

/**
 * 由MyBatis Generator工具自动生成
 * 定金和资产关系，定金可以不挂资产，定金也可以挂在多个资产上
 * This file was generated on 2020-02-14 10:18:23.
 */
@Table(name = "`earnest_order_detail`")
public interface EarnestOrderDetail extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    @EditMode(editor = FieldEditor.Number, required = true)
    Long getId();

    void setId(Long id);

    @Column(name = "`create_time`")
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getCreateTime();

    void setCreateTime(Date createTime);

    @Column(name = "`modify_time`")
    @FieldDef(label="修改时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    Date getModifyTime();

    void setModifyTime(Date modifyTime);

    @Column(name = "`earnest_order_id`")
    @FieldDef(label="定金业务单ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getEarnestOrderId();

    void setEarnestOrderId(Long earnestOrderId);

    @Column(name = "`assets_id`")
    @FieldDef(label="资产编号，资产包含摊位，冷库，公寓等")
    @EditMode(editor = FieldEditor.Number, required = false)
    Long getAssetsId();

    void setAssetsId(Long assetsId);

    @Column(name = "`assets_name`")
    @FieldDef(label="资产名称，资产包含摊位，冷库，公寓等", maxLength = 100)
    @EditMode(editor = FieldEditor.Text, required = false)
    String getAssetsName();

    void setAssetsName(String assetsName);
}