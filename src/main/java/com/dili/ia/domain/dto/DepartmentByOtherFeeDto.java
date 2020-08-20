package com.dili.ia.domain.dto;

import com.dili.ia.domain.DepartmentChargeItem;

import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/7/27
 * @version:      农批业务系统重构
 * @description:  通行证
 */
public class DepartmentByOtherFeeDto {

    /**
     * 部门id
     */
    private Long departmentId;

    /**
     * 部门名称
     */
    private String departmentName;

    public Long getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Long departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}
