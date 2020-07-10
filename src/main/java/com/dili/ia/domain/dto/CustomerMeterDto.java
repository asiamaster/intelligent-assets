package com.dili.ia.domain.dto;

import com.dili.ia.domain.CustomerMeter;

import javax.persistence.Column;

/**
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 水电费 - 表和用户关系Dto
 */
public class CustomerMeterDto extends CustomerMeter {

    /**
     * 部门
     */
    private String departmentName;

    /**
     * 部门 id
     */
    private Long departmentId;

    /**
     * 表类型
     */
    private Integer type;

    /**
     * 关键字
     */
    private String keyword;

    /**
     * 表编号
     */
    private String number;

    /**
     * 资产类型 id
     */
    private Long assetsId;

    /**
     * 资产类型 类别
     */
    private String assetsType;

    /**
     * 对应编号,名称(表地址)
     */
    private String assetsName;

    /**
     * 上期指数
     */
    private Long lastAmount;

    /**
     * 表主键
     */
    private Long meterId;

    /**
     * 单价
     */
    private Long price;

    public Long getAssetsId() {
        return assetsId;
    }

    public void setAssetsId(Long assetsId) {
        this.assetsId = assetsId;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getAssetsType() {
        return assetsType;
    }

    public void setAssetsType(String assetsType) {
        this.assetsType = assetsType;
    }

    @Override
    public Long getMeterId() {
        return meterId;
    }

    @Override
    public void setMeterId(Long meterId) {
        this.meterId = meterId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getAssetsName() {
        return assetsName;
    }

    public void setAssetsName(String assetsName) {
        this.assetsName = assetsName;
    }

    public Long getLastAmount() {
        return lastAmount;
    }

    public void setLastAmount(Long lastAmount) {
        this.lastAmount = lastAmount;
    }
}