package com.dili.ia.domain.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.dili.ia.domain.MeterDetail;
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
 * @description: 水电费 - 缴水电费Dto
 */
public class MeterDetailDto extends MeterDetail {

    /**
     * 表类型
     */
    private Integer type;

    /**
     * 关键字(X费单号,表编号,客户姓名)
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
     * 部门
     */
    private String departmentName;

    /**
     * 部门 id
     */
    private Long departmentId;

    /**
     * 水电预存余额
     */
    private Long balance;

    /**
     * 业务类型(该用能是水表、电表)
     */
    private String bizTypes;

    /**
     * 状态数组
     */
    private String status;

    /**
     * 使用月份的第一天,用于查询
     */
    private Date startTime;

    /**
     * 使用月份的最后一天,用于查询
     */
    private Date endTime;

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
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

    public Long getBalance() {
        return balance;
    }

    public void setBalance(Long balance) {
        this.balance = balance;
    }

    public String getBizTypes() {
        return bizTypes;
    }

    public void setBizTypes(String bizTypes) {
        this.bizTypes = bizTypes;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}