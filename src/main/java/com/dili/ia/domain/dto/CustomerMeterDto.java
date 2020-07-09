package com.dili.ia.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ia.domain.CustomerMeter;
import com.dili.ss.domain.BaseDomain;
import com.dili.ss.metadata.FieldEditor;
import com.dili.ss.metadata.annotation.EditMode;
import com.dili.ss.metadata.annotation.FieldDef;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Table;
import java.util.Date;

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
     * 对应编号,名称(表地址)
     */
    private String assetsName;

    /**
     * 上期指数
     */
    private Long lastAmount;

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