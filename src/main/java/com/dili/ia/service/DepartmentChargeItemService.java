package com.dili.ia.service;

import com.dili.ia.domain.DepartmentChargeItem;
import com.dili.ia.domain.dto.DepartmentChargeItemDto;
import com.dili.ss.base.BaseService;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;

import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/8/19
 * @version:      农批业务系统重构
 * @description:  其他收费 收费项绑定
 */
public interface DepartmentChargeItemService extends BaseService<DepartmentChargeItem, Long> {

    /**
     * 打开其他收费 - 收费项绑定部门的页面，更新数据表
     *
     * @param
     * @param userTicket
     * @return
     * @date   2020/8/24
     */
    void batchUpdateChargeItems(UserTicket userTicket);

    /**
     * 根据收费项查询相关部门
     * 
     * @param  chargeItemId
     * @return list
     * @date   2020/8/19
     */
    DepartmentChargeItemDto selectListByChargeItemId(Long chargeItemId);


    /**
     * 新增绑定(也叫修改)
     * 
     * @param
     * @param userTicket
     * @return
     * @date   2020/8/19
     */
    void addDepartmentChargeItems(DepartmentChargeItemDto departmentChargeItemDto, UserTicket userTicket) throws BusinessException;

    /**
     * 新增其他收费的时候，选择部门，查询相关联的收费项
     * 
     * @param  departmentId
     * @return List
     * @date   2020/8/20
     */
    List<DepartmentChargeItemDto> getChargeItemsByDepartment(Long departmentId);

    /**
     * 查询所有收费项
     *
     * @param
     * @return
     * @date   2020/8/24
     */
    String listNoParam();

    /**
     * 根据收费项查询所属组织（商户）
     *
     * @param  chargeItemId
     * @return mchId
     * @date   2020/12/14
     */
    List<DepartmentChargeItem> getListByChargeItemId(Long chargeItemId);
}