package com.dili.ia.domain;

import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import javax.persistence.*;

/**
 * 由MyBatis Generator工具自动生成
 * 司磅图片
 * This file was generated on 2020-06-12 11:14:28.
 */
@Table(name = "`stock_weighman_img`")
public class StockWeighmanImg extends BaseDomain {
    /**
     * id
     */
    @Id
    @Column(name = "`id`")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 司磅记录ID
     */
    @Column(name = "`weighman_record_id`")
    private Long weighmanRecordId;

    /**
     * 类型 1.称重前,2.称重后  3,回皮前,4.回皮后
     */
    @Column(name = "`type`")
    private Byte type;

    /**
     * 地址
     */
    @Column(name = "`url`")
    private String url;

    @Column(name = "`market_id`")
    private Long marketId;

    @Column(name = "`market_code`")
    private String marketCode;

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
     * 获取司磅记录ID
     *
     * @return weighman_record_id - 司磅记录ID
     */
    @FieldDef(label="司磅记录ID")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getWeighmanRecordId() {
        return weighmanRecordId;
    }

    /**
     * 设置司磅记录ID
     *
     * @param weighmanRecordId 司磅记录ID
     */
    public void setWeighmanRecordId(Long weighmanRecordId) {
        this.weighmanRecordId = weighmanRecordId;
    }

    /**
     * 获取类型 1.称重前,2.称重后  3,回皮前,4.回皮后
     *
     * @return type - 类型 1.称重前,2.称重后  3,回皮前,4.回皮后
     */
    @FieldDef(label="类型 1.称重前,2.称重后  3,回皮前,4.回皮后")
    @EditMode(editor = FieldEditor.Text, required = false)
    public Byte getType() {
        return type;
    }

    /**
     * 设置类型 1.称重前,2.称重后  3,回皮前,4.回皮后
     *
     * @param type 类型 1.称重前,2.称重后  3,回皮前,4.回皮后
     */
    public void setType(Byte type) {
        this.type = type;
    }

    /**
     * 获取地址
     *
     * @return url - 地址
     */
    @FieldDef(label="地址", maxLength = 200)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getUrl() {
        return url;
    }

    /**
     * 设置地址
     *
     * @param url 地址
     */
    public void setUrl(String url) {
        this.url = url;
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