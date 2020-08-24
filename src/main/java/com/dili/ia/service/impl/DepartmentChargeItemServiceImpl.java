package com.dili.ia.service.impl;

import com.dili.ia.domain.DepartmentChargeItem;
import com.dili.ia.domain.dto.DepartmentByOtherFeeDto;
import com.dili.ia.domain.dto.DepartmentChargeItemDto;
import com.dili.ia.glossary.BizNumberTypeEnum;
import com.dili.ia.mapper.DepartmentChargeItemMapper;
import com.dili.ia.service.DepartmentChargeItemService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.metadata.ValuePair;
import com.dili.ss.metadata.ValuePairImpl;
import com.dili.uap.sdk.domain.DataDictionaryValue;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.rpc.DataDictionaryRpc;
import com.google.common.collect.Lists;
import io.seata.spring.annotation.GlobalTransactional;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    DataDictionaryRpc dataDictionaryRpc;

    /**
     * 打开其他收费 - 收费项绑定部门的页面，更新数据表
     *
     * @param
     * @return
     * @date   2020/8/24
     */
    @Override
    @GlobalTransactional
    public void batchUpdateChargeItems() {

        // 根据数据字典的键查询相关 其他收费项 的相关数据
        List<DataDictionaryValue> list = dataDictionaryRpc.listDataDictionaryValueByDdCode(BizNumberTypeEnum.OTHER_FEE_CHARGE_ITEM.getCode()).getData();
        if (!CollectionUtils.isEmpty(list)) {
            List<ValuePair<?>> valuePairs = Lists.newArrayList();

            for (int i = 0; i < list.size(); i++) {
                DataDictionaryValue dataDictionaryValue = list.get(i);
                valuePairs.add(new ValuePairImpl(dataDictionaryValue.getName(), dataDictionaryValue.getCode()));
            }

            // 查询表，数据字典中不存在，则删除，数据字典多出来的，则添加
            List<DepartmentChargeItem> itemInfoList = this.list(new DepartmentChargeItem());
            List<String> chargeItemIdListInTable = new ArrayList<>();
            List<String> chargeItemIdListInTableToAdd = new ArrayList<>();
            for (DepartmentChargeItem itemInfo : itemInfoList) {
                chargeItemIdListInTable.add(itemInfo.getChargeItemId());
                chargeItemIdListInTableToAdd.add(itemInfo.getChargeItemId());
            }

            // 数据字典的收费项id集合
            List<String> chargeItemIdListByDate = new ArrayList<>();
            for (ValuePair<?> valuePair : valuePairs) {
                String chargeItemId = (String) valuePair.getValue();
                chargeItemIdListByDate.add(chargeItemId);
            }

            // 求出表中有的，数据字典没有收费项 ids，然后删除这些 ids
            chargeItemIdListInTable.removeAll(chargeItemIdListByDate);
            if (chargeItemIdListInTable != null && chargeItemIdListInTable.size() > 0) {
                this.getActualDao().deleteEntityListByChargeItemIds(chargeItemIdListInTable);
            }

            // 求出数据字典中游，表中没有的 ids，然后添加这些 ids
            chargeItemIdListByDate.removeAll(chargeItemIdListInTableToAdd);
            if (chargeItemIdListByDate != null && chargeItemIdListByDate.size() > 0) {
                List<DepartmentChargeItem> departmentChargeItemList = new ArrayList<>();
                for (ValuePair<?> valuePair : valuePairs) {
                    String chargeItemId = (String) valuePair.getValue();
                    if (chargeItemIdListByDate.contains(chargeItemId)) {
                        DepartmentChargeItem departmentChargeItem = new DepartmentChargeItem();
                        String chargeItemName = valuePair.getText();
                        departmentChargeItem.setVersion(0);
                        departmentChargeItem.setChargeItemId(chargeItemId);
                        departmentChargeItem.setChargeItemName(chargeItemName);
                        departmentChargeItem.setCreateTime(LocalDateTime.now());
                        departmentChargeItem.setModifyTime(LocalDateTime.now());
                        departmentChargeItemList.add(departmentChargeItem);
                    }
                }
                this.batchInsert(departmentChargeItemList);
            }
        }
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

        if (departmentIdListByParam != null && departmentIdListByParam.size() > 0) {
            // 参数中的部门集合，排除了表中含有的部门集合，剩下的是要插入数据库中的
            List<DepartmentChargeItem> itemParamList = new ArrayList<>();
            for (DepartmentByOtherFeeDto departmentInfo : departmentList) {
                if (departmentIdListByParam.contains(departmentInfo.getDepartmentId())) {
                    DepartmentChargeItem itemParam = new DepartmentChargeItem();
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