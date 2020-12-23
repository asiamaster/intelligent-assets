package com.dili.ia.domain.dto;

import com.dili.ia.domain.AssetsRental;
import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.domain.annotation.Operator;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/11/25
 * @version:      农批业务系统重构
 * @description:  资产出租预设Dto
 */
public class AssetsRentalDto extends AssetsRental {

    /**
     * 资产ID
     */
    private Long assetsId;

    /**
     * 资产名称
     */
    private String assetsName;

    /**
     * 资产类型 1：摊位 2：冷库
     */
    private Integer assetsType;

    /**
     * 摊位id集合
     */
    private List<Long> assetsIds;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 数量小
     */
    private Double startNumber;

    /**
     * 数量大
     */
    private Double endNumber;

    // 一个出租预设可能有多个摊位
    private List<AssetsRentalItem> assetsRentalItemList;

    public List<AssetsRentalItem> getAssetsRentalItemList() {
        return assetsRentalItemList;
    }

    public void setAssetsRentalItemList(List<AssetsRentalItem> assetsRentalItemList) {
        this.assetsRentalItemList = assetsRentalItemList;
    }

    public Double getStartNumber() {
        return startNumber;
    }

    public void setStartNumber(Double startNumber) {
        this.startNumber = startNumber;
    }

    public Double getEndNumber() {
        return endNumber;
    }

    public void setEndNumber(Double endNumber) {
        this.endNumber = endNumber;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Long getAssetsId() {
        return assetsId;
    }

    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    public Integer getAssetsType() {
        return assetsType;
    }

    public void setAssetsType(Integer assetsType) {
        this.assetsType = assetsType;
    }

    public List<Long> getAssetsIds() {
        return assetsIds;
    }

    public void setAssetsIds(List<Long> assetsIds) {
        this.assetsIds = assetsIds;
    }
}