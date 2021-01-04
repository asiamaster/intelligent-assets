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
 * 司磅记录
 * This file was generated on 2020-06-12 11:14:28.
 */
@Table(name = "`stock_weighman_record`")
public class StockWeighmanRecord extends BaseDomain {
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
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @Column(name = "`modify_time`")
    private LocalDateTime modifyTime;

    /**
     * 毛重
     */
    @Column(name = "`gross_weight`")
    private Long grossWeight;

    /**
     * 毛重时间
     */
    @Column(name = "`gross_weight_date`")
    private LocalDateTime grossWeightDate;

    /**
     * 皮重
     */
    @Column(name = "`tare_weight`")
    private Long tareWeight;

    /**
     * 皮重时间
     */
    @Column(name = "`tare_weight_date`")
    private LocalDateTime tareWeightDate;

    /**
     * 净重
     */
    @Column(name = "`new_weight`")
    private Long newWeight;

    @Column(name = "`operator_id`")
    private Long operatorId;

    @Column(name = "`operator_name`")
    private String operatorName;

    @Column(name = "`tare_operator_id`")
    private Long tareOperatorId;

    /**
     * 回皮司磅员
     */
    @Column(name = "`tare_operator_name`")
    private String tareOperatorName;

    @Column(name = "`market_id`")
    private Long marketId;

    @Column(name = "`market_code`")
    private String marketCode;

    @Column(name = "`images`")
    private String images;
    
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
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    @FieldDef(label="创建时间")
    @EditMode(editor = FieldEditor.Datetime, required = true)
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
    @EditMode(editor = FieldEditor.Datetime, required = true)
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
     * 获取毛重
     *
     * @return gross_weight - 毛重
     */
    @FieldDef(label="毛重")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getGrossWeight() {
        return grossWeight;
    }

    /**
     * 设置毛重
     *
     * @param grossWeight 毛重
     */
    public void setGrossWeight(Long grossWeight) {
        this.grossWeight = grossWeight;
    }

    /**
     * 获取毛重时间
     *
     * @return gross_weight_date - 毛重时间
     */
    @FieldDef(label="毛重时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getGrossWeightDate() {
        return grossWeightDate;
    }

    /**
     * 设置毛重时间
     *
     * @param grossWeightDate 毛重时间
     */
    public void setGrossWeightDate(LocalDateTime grossWeightDate) {
        this.grossWeightDate = grossWeightDate;
    }

    /**
     * 获取皮重
     *
     * @return tare_weight - 皮重
     */
    @FieldDef(label="皮重")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getTareWeight() {
        return tareWeight;
    }

    /**
     * 设置皮重
     *
     * @param tareWeight 皮重
     */
    public void setTareWeight(Long tareWeight) {
        this.tareWeight = tareWeight;
    }

    /**
     * 获取皮重时间
     *
     * @return tare_weight_date - 皮重时间
     */
    @FieldDef(label="皮重时间")
    @EditMode(editor = FieldEditor.Datetime, required = false)
    public LocalDateTime getTareWeightDate() {
        return tareWeightDate;
    }

    /**
     * 设置皮重时间
     *
     * @param tareWeightDate 皮重时间
     */
    public void setTareWeightDate(LocalDateTime tareWeightDate) {
        this.tareWeightDate = tareWeightDate;
    }

    /**
     * 获取净重
     *
     * @return new_weight - 净重
     */
    @FieldDef(label="净重")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getNewWeight() {
        return newWeight;
    }

    /**
     * 设置净重
     *
     * @param newWeight 净重
     */
    public void setNewWeight(Long newWeight) {
        this.newWeight = newWeight;
    }

    /**
     * @return operator_id
     */
    @FieldDef(label="operatorId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getOperatorId() {
        return operatorId;
    }

    /**
     * @param operatorId
     */
    public void setOperatorId(Long operatorId) {
        this.operatorId = operatorId;
    }

    /**
     * @return operator_name
     */
    @FieldDef(label="operatorName", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * @param operatorName
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }

    /**
     * @return tare_operator_id
     */
    @FieldDef(label="tareOperatorId")
    @EditMode(editor = FieldEditor.Number, required = false)
    public Long getTareOperatorId() {
        return tareOperatorId;
    }

    /**
     * @param tareOperatorId
     */
    public void setTareOperatorId(Long tareOperatorId) {
        this.tareOperatorId = tareOperatorId;
    }

    /**
     * 获取回皮司磅员
     *
     * @return tare_operator_name - 回皮司磅员
     */
    @FieldDef(label="回皮司磅员", maxLength = 40)
    @EditMode(editor = FieldEditor.Text, required = false)
    public String getTareOperatorName() {
        return tareOperatorName;
    }

    /**
     * 设置回皮司磅员
     *
     * @param tareOperatorName 回皮司磅员
     */
    public void setTareOperatorName(String tareOperatorName) {
        this.tareOperatorName = tareOperatorName;
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

	public String getImages() {
		return images;
	}

	public void setImages(String images) {
		this.images = images;
	}
    
    
}