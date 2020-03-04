package com.dili.ia.domain;

import com.dili.ss.dto.IBaseDomain;
import com.dili.ss.metadata.annotation.FieldDef;

import javax.persistence.*;
import java.util.Date;

/**
 * 由MyBatis Generator工具自动生成
 * 打印模板 ;
 * This file was generated on 2020-03-03 14:30:11.
 */
@Table(name = "`print_template`")
public interface PrintTemplate extends IBaseDomain {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "`id`")
    @FieldDef(label="id")
    Long getId();

    void setId(Long id);

    @Column(name = "`name`")
    @FieldDef(label="模板名称", maxLength = 32)
    String getName();

    void setName(String name);

    @Column(name = "`path`")
    @FieldDef(label="模板路径", maxLength = 32)
    String getPath();

    void setPath(String path);

    @Column(name = "`market_code`")
    @FieldDef(label="市场")
    String getMarketCode();

    void setMarketCode(String marketId);

    @Column(name = "`creator_id`")
    @FieldDef(label="创建人")
    Long getCreatorId();

    void setCreatorId(Long creatorId);

    @Column(name = "`create_time`")
    @FieldDef(label="创建时间")
    Date getCreateTime();

    void setCreateTime(Date createTime);

    @Column(name = "`modify_time`")
    @FieldDef(label="更新时间")
    Date getModifyTime();

    void setModifyTime(Date modifyTime);
}