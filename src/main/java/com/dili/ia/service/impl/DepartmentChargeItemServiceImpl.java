package com.dili.ia.service.impl;

import com.dili.ia.domain.DepartmentChargeItem;
import com.dili.ia.domain.dto.DepartmentByOtherFeeDto;
import com.dili.ia.domain.dto.DepartmentChargeItemDto;
import com.dili.ia.mapper.DepartmentChargeItemMapper;
import com.dili.ia.service.DepartmentChargeItemService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/8/19
 * @version:      农批业务系统重构
 * @description:  其他收费 收费项绑定
 */
@Service
public class DepartmentChargeItemServiceImpl extends BaseServiceImpl<DepartmentChargeItem, Long> implements DepartmentChargeItemService {

    private final static Logger logger = LoggerFactory.getLogger(DepartmentChargeItemServiceImpl.class);

    public DepartmentChargeItemMapper getActualDao() {
        return (DepartmentChargeItemMapper)getDao();
    }

    /**
     * 根据收费项查询相关部门
     *
     * @param  chargeItemId
     * @return list
     * @date   2020/8/19
     */
    @Override
    public DepartmentChargeItemDto selectListByChargeItemId(Long chargeItemId) {
        List<DepartmentByOtherFeeDto> departmentDtoList = new ArrayList<>();
        DepartmentChargeItemDto itemDto = new DepartmentChargeItemDto();

        List<DepartmentChargeItem> itemList = this.getActualDao().selectListByChargeItemId(chargeItemId);
        if (itemList != null && itemList.size() > 0) {
            for (DepartmentChargeItem item : itemList) {
                DepartmentByOtherFeeDto departmentDto = new DepartmentByOtherFeeDto();
                departmentDto.setDepartmentId(item.getDepartmentId());
                departmentDto.setDepartmentName(item.getDepartmentName());
                departmentDtoList.add(departmentDto);
            }
            BeanUtils.copyProperties(itemList.get(0), itemDto);
            itemDto.setDepartmentList(departmentDtoList);
        }


        return itemDto;
    }

    /**
     * 新增绑定(也叫修改)
     *
     * @param
     * @param userTicket
     * @return
     * @date   2020/8/19
     */
    @Override
    public void addDepartmentChargeItems(DepartmentChargeItemDto departmentChargeItemDto, UserTicket userTicket) throws Exception {
        // 先根据费用类型查询
        List<DepartmentChargeItem> itemList = this.getActualDao().selectListByChargeItemId(departmentChargeItemDto.getId());
        if (itemList == null || itemList.size() == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "收费项已不存在");
        }

        // 拿到表中同一 chargeItemId 的部门id集合
        List<Long> departmentIdListByTable = new ArrayList<>();
        for (DepartmentChargeItem itemInfo : itemList) {
            departmentIdListByTable.add(itemInfo.getDepartmentId());
        }

        // 拿到参数中的部门id集合
        List<Long> departmentIdListByParam = new ArrayList<>();
        List<DepartmentByOtherFeeDto> departmentList = departmentChargeItemDto.getDepartmentList();
        for (DepartmentByOtherFeeDto departmentInfo : departmentList) {
            departmentIdListByParam.add(departmentInfo.getDepartmentId());
        }

        // 得到两个集合中，参数集合中有，表集合中没有的部门ids
        departmentIdListByParam.removeAll(departmentIdListByTable);

        // 参数中的部门集合，排除了表中含有的部门集合，剩下的是要插入数据库中的
        List<DepartmentChargeItem> itemParamList = new ArrayList<>();
        for (DepartmentByOtherFeeDto departmentInfo : departmentList) {
            if (departmentIdListByParam.contains(departmentInfo.getDepartmentId())) {
                DepartmentChargeItem itemParam = new DepartmentChargeItem();
                itemParam.setVersion(0);
                itemParam.setCreateTime(LocalDateTime.now());
                itemParam.setModifyTime(LocalDateTime.now());
                itemParam.setMarketId(userTicket.getFirmId());
                itemParam.setCreator(userTicket.getRealName());
                itemParam.setMarketCode(userTicket.getFirmCode());
                itemParam.setChargeItemId(departmentChargeItemDto.getChargeItemId());
                itemParam.setChargeItemName(departmentChargeItemDto.getChargeItemName());
                itemParam.setDepartmentId(departmentInfo.getDepartmentId());
                itemParam.setDepartmentName(departmentInfo.getDepartmentName());
                itemParamList.add(itemParam);
            }
        }

        if (this.batchInsert(itemParamList) == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作");
        }

    }

    /**
     * 新增其他收费的时候，选择部门，查询相关联的收费项
     *
     * @param  departmentId
     * @return List
     * @date   2020/8/20
     */
    @Override
    public List<DepartmentChargeItemDto> getChargeItemsByDepartment(Long departmentId) {
        List<DepartmentChargeItemDto> itemDtoList = this.getActualDao().getChargeItemsByDepartment(departmentId);
        return itemDtoList;
    }
}