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

import javax.persistence.*;
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
     * 是否转角
     */
    private Integer corner;

    /**
     * 转角名称
     */
    @Transient
    private String cornerName;

    /**
     * 单位CODE
     */
    @Transient
    private String unit;

    /**
     * 单位名称
     */
    @Transient
    private String unitName;

    /**
     * 数量
     */
    @Transient
    private Double number;


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

    /**
     * 一级区域Id
     */
    private Long firstDistrictId;

    /**
     * 一级区域名称
     */
    private String firstDistrictName;

    /**
     * 二级区域Id
     */
    private Long secondDistrictId;

    /**
     * 二级区域名称
     */
    private String secondDistrictName;

    public Long getFirstDistrictId() {
        return firstDistrictId;
    }

    public void setFirstDistrictId(Long firstDistrictId) {
        this.firstDistrictId = firstDistrictId;
    }

    public String getFirstDistrictName() {
        return firstDistrictName;
    }

    public void setFirstDistrictName(String firstDistrictName) {
        this.firstDistrictName = firstDistrictName;
    }

    public Long getSecondDistrictId() {
        return secondDistrictId;
    }

    public void setSecondDistrictId(Long secondDistrictId) {
        this.secondDistrictId = secondDistrictId;
    }

    public String getSecondDistrictName() {
        return secondDistrictName;
    }

    public void setSecondDistrictName(String secondDistrictName) {
        this.secondDistrictName = secondDistrictName;
    }

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

    public Integer getCorner() {
        return corner;
    }

    public void setCorner(Integer corner) {
        this.corner = corner;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    public String getCornerName() {
        return cornerName;
    }

    public void setCornerName(String cornerName) {
        this.cornerName = cornerName;
    }

    public Double getNumber() {
        return number;
    }

    public void setNumber(Double number) {
        this.number = number;
    }

    public List<Long> getAssetsIds() {
        return assetsIds;
    }

    public void setAssetsIds(List<Long> assetsIds) {
        this.assetsIds = assetsIds;
    }
}