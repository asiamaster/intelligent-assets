package com.dili.ia.mapper;

import com.dili.ia.domain.DepartmentChargeItem;
import com.dili.ia.domain.dto.DepartmentChargeItemDto;
import com.dili.ss.base.MyMapper;

import java.util.List;

public interface DepartmentChargeItemMapper extends MyMapper<DepartmentChargeItem> {

    /**
     * 根据收费项查询相关部门
     *
     * @param  chargeItemId
     * @return list
     * @date   2020/8/19
     */
    List<DepartmentChargeItem> selectListByChargeItemId(Long chargeItemId);

    /**
     * 新增其他收费的时候，选择部门，查询相关联的收费项
     *
     * @param  departmentId
     * @return List
     * @date   2020/8/20
     */
    List<DepartmentChargeItemDto> getChargeItemsByDepartment(Long departmentId);

    /**
     * 根据字段删除数据
     *
     * @param  chargeItemIdListInTable
     * @return
     * @date   2020/8/24
     */
    void deleteEntityListByChargeItemIds(List<String> chargeItemIdListInTable);
}