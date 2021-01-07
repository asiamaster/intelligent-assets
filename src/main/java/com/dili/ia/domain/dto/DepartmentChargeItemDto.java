package com.dili.ia.domain.dto;

import com.dili.ia.domain.DepartmentChargeItem;

import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/7/27
 * @version:      农批业务系统重构
 * @description:  通行证
 */
public class DepartmentChargeItemDto extends DepartmentChargeItem {
	
	String[] departmentIds;

	public String[] getDepartmentIds() {
		return departmentIds;
	}

	public void setDepartmentIds(String[] departmentIds) {
		this.departmentIds = departmentIds;
	}

	/**
     * 绑定为一对多关系
     */
    List<DepartmentByOtherFeeDto> departmentList ;

    public List<DepartmentByOtherFeeDto> getDepartmentList() {
        return departmentList;
    }

    public void setDepartmentList(List<DepartmentByOtherFeeDto> departmentList) {
        this.departmentList = departmentList;
    }
}
