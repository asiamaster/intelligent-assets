package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.dili.uap.sdk.domain.UserTicket;

import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 
 * This file was generated on 2020-07-16 15:35:44.
 */
@Table(name = "`category_storage_cycle`")
public class CategoryStorageCycle extends BaseDomain {
    /**
     * 品类id
     */
    @Id
    @Column(name = "`id`")
    private Long id;

    /**
     * 品类code
     */
    @Column(name = "`code`")
    private String code;

    /**
     * 模块标签
     */
    @Column(name = "`module_label`")
    private String moduleLabel;

    /**
     * 存储天数
     */
    @Column(name = "`cycle`")
    private Integer cycle;

    /**
     * 备注
     */
    @Column(name = "`notes`")
    private String notes;

    /**
     * 1:启用,2禁用
     */
    @Column(name = "`state`")
    private Integer state;

    @Column(name = "`market_id`")
    private Long marketId;

    @Column(name = "`market_code`")
    private String marketCode;
    
    public CategoryStorageCycle() {
    	super();
    }
    public CategoryStorageCycle(UserTicket userTicket) {
    	super();
    	this.marketCode = userTicket.getFirmCode();
    	this.marketId = userTicket.getFirmId();
    }
    
    /**
     * 获取品类id
     *
     * @return id - 品类id
     */
    @FieldDef(label="品类id")
    @EditMode(editor = FieldEditor.Number, required = true)
    public Long getId() {
        return id;
    }

    /**
     * 设置品类id
     *
     * @param id 品类id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取品类code
     *
     * @return code - 品类code
     */
    @FieldDef(label="品类code", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getCode() {
        return code;
    }

    /**
     * 设置品类code
     *
     * @param code 品类code
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取模块标签
     *
     * @return module_label - 模块标签
     */
    @FieldDef(label="模块标签", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getModuleLabel() {
        return moduleLabel;
    }

    /**
     * 设置模块标签
     *
     * @param moduleLabel 模块标签
     */
    public void setModuleLabel(String moduleLabel) {
        this.moduleLabel = moduleLabel;
    }

    /**
     * 获取存储天数
     *
     * @return cycle - 存储天数
     */
    @FieldDef(label="存储天数")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Integer getCycle() {
        return cycle;
    }

    /**
     * 设置存储天数
     *
     * @param cycle 存储天数
     */
    public void setCycle(Integer cycle) {
        this.cycle = cycle;
    }

    /**
     * 获取备注
     *
     * @return notes - 备注
     */
    @FieldDef(label="备注", maxLength = 100)
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
     * 获取1:启用,2禁用
     *
     * @return state - 1:启用,2禁用
     */
    @FieldDef(label="1:启用,2禁用")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Integer getState() {
        return state;
    }

    /**
     * 设置1:启用,2禁用
     *
     * @param state 1:启用,2禁用
     */
    public void setState(Integer state) {
        this.state = state;
    }

    /**
     * @return market_id
     */
    @FieldDef(label="marketId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getMarketId() {
        return marketId;
    }

    /**
     * @param marketId
     */
    public void setMarketId(Long marketId) {
        this.marketId = marketId;
    }

    /**
     * @return market_code
     */
    @FieldDef(label="marketCode", maxLength = 20)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getMarketCode() {
        return marketCode;
    }

    /**
     * @param marketCode
     */
    public void setMarketCode(String marketCode) {
        this.marketCode = marketCode;
    }
}