package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.DepartmentChargeItem;
import com.dili.ia.domain.dto.DepartmentByOtherFeeDto;
import com.dili.ia.domain.dto.DepartmentChargeItemDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.mapper.DepartmentChargeItemMapper;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.DepartmentChargeItemService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.EasyuiPageOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.UserTicket;
import com.github.pagehelper.Page;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

/**
 * @author:       xiaosa
 * @date:         2020/8/19
 * @version:      农批业务系统重构
 * @description:  其他收费 收费项绑定
 */
@Service
public class DepartmentChargeItemServiceImpl extends BaseServiceImpl<DepartmentChargeItem, Long> implements DepartmentChargeItemService {

    public DepartmentChargeItemMapper getActualDao() {
        return (DepartmentChargeItemMapper)getDao();
    }


    @Autowired
    private BusinessChargeItemService businessChargeItemService;

    /**
     * 打开 其他收费 - 收费项绑定部门 页面，更新数据表
     *
     * @date   2020/8/24
     * @param userTicket
     */
    @Override
    public void batchUpdateChargeItems(UserTicket userTicket) {
        // 收费项从动态收费项里获取
        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.
                queryBusinessChargeItemConfig(userTicket.getFirmId(), BizTypeEnum.OTHER_FEE.getCode(), YesOrNoEnum.YES.getCode());

        if (!CollectionUtils.isEmpty(chargeItemDtos)) {
            // 查询表中的收费项，数据字典中不存在，则从表中删除，数据字典多出来的，则添加到表中
            List<DepartmentChargeItem> itemInfoList = this.list(new DepartmentChargeItem());
            List<String> chargeItemIdListInTable = new ArrayList<>();
            List<String> chargeItemIdListInTableToAdd = new ArrayList<>();
            for (DepartmentChargeItem itemInfo : itemInfoList) {
                chargeItemIdListInTable.add(itemInfo.getChargeItemId());
                chargeItemIdListInTableToAdd.add(itemInfo.getChargeItemId());
            }

            // 收费项配置 里的 其他收费 收费项 ids
            List<String> chargeItemIdListByDate = new ArrayList<>();
            for (BusinessChargeItemDto chargeItemDto : chargeItemDtos) {
                String chargeItemId = chargeItemDto.getId().toString();
                chargeItemIdListByDate.add(chargeItemId);
            }

            // 求出表中有的，数据字典没有收费项 ids，然后删除表中的这些 ids
            chargeItemIdListInTable.removeAll(chargeItemIdListByDate);
            if (chargeItemIdListInTable != null && chargeItemIdListInTable.size() > 0) {
                this.getActualDao().deleteEntityListByChargeItemIds(chargeItemIdListInTable);
            }

            // 求出数据字典中有，表中没有的 ids，然后添加这些 ids 到表中
            chargeItemIdListByDate.removeAll(chargeItemIdListInTableToAdd);
            if (chargeItemIdListByDate != null && chargeItemIdListByDate.size() > 0) {
                List<DepartmentChargeItem> departmentChargeItemList = new ArrayList<>();
                for (BusinessChargeItemDto chargeItemDto : chargeItemDtos) {
                    String chargeItemId = chargeItemDto.getId().toString();
                    if (chargeItemIdListByDate.contains(chargeItemId)) {
                        DepartmentChargeItem departmentChargeItem = new DepartmentChargeItem();
                        String chargeItemName = chargeItemDto.getChargeItem();
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
     * @return DepartmentChargeItemDto
     * @date   2020/8/19
     */
    @Override
    public DepartmentChargeItemDto selectListByChargeItemId(String chargeItemId) {
        List<DepartmentByOtherFeeDto> departmentDtoList = new ArrayList<>();
        DepartmentChargeItemDto departmentChargeItemDto = new DepartmentChargeItemDto();

        List<DepartmentChargeItem> itemList = this.getActualDao().selectListByChargeItemId(chargeItemId);
        if (itemList != null && itemList.size() > 0) {
            for (DepartmentChargeItem item : itemList) {
                DepartmentByOtherFeeDto departmentDto = new DepartmentByOtherFeeDto();
                departmentDto.setDepartmentId(item.getDepartmentId());
                departmentDto.setDepartmentName(item.getDepartmentName());
                departmentDtoList.add(departmentDto);
            }
            BeanUtils.copyProperties(itemList.get(0), departmentChargeItemDto);
            departmentChargeItemDto.setDepartmentList(departmentDtoList);
        }

        return departmentChargeItemDto;
    }

    /**
     * 新增绑定(也叫修改)
     *
     * @param departmentChargeItemDto
     * @param userTicket
     * @date  2020/8/19
     */
    @Override
    public void addDepartmentChargeItems(DepartmentChargeItemDto departmentChargeItemDto, UserTicket userTicket) throws BusinessException {
        // 根据收费项查询关联的部门集合
        List<DepartmentChargeItem> itemList = this.getActualDao().selectListByChargeItemId(departmentChargeItemDto.getChargeItemId());
        if (itemList == null || itemList.size() == 0) {
            throw new BusinessException(ResultCode.DATA_ERROR, "收费项已不存在");
        }

        // 根据 chargeItemId 删除原有关联的部门，插入新增的部门
        List<String> chargeItemIds = new ArrayList<>();
        chargeItemIds.add(departmentChargeItemDto.getChargeItemId());
        this.getActualDao().deleteEntityListByChargeItemIds(chargeItemIds);

        List<DepartmentByOtherFeeDto> departmentList = departmentChargeItemDto.getDepartmentList();
        if (departmentList != null && departmentList.size() > 0) {
            List<DepartmentChargeItem> itemParamList = new ArrayList<>();
            for (DepartmentByOtherFeeDto departmentInfo : departmentList) {
                DepartmentChargeItem itemParam = new DepartmentChargeItem();
                itemParam.setCreateTime(LocalDateTime.now());
                itemParam.setModifyTime(LocalDateTime.now());
                itemParam.setMarketId(userTicket.getFirmId());
                itemParam.setCreator(userTicket.getRealName());
                itemParam.setMarketCode(userTicket.getFirmCode());
                itemParam.setChargeItemId(departmentChargeItemDto.getChargeItemId());
                itemParam.setChargeItemName(itemList.get(0).getChargeItemName());
                itemParam.setDepartmentId(departmentInfo.getDepartmentId());
                itemParam.setDepartmentName(departmentInfo.getDepartmentName());

                // 只有沈阳才需要存入商户ID和商户名称
                if (departmentChargeItemDto.getMchId() != null) {
                    itemParam.setMchId(departmentChargeItemDto.getMchId());
                }
                if (StringUtils.isNotEmpty(departmentChargeItemDto.getMchName())) {
                    itemParam.setMchName(departmentChargeItemDto.getMchName());
                }

                itemParam.setVersion(0);
                itemParamList.add(itemParam);
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

    /**
     * 查询所有收费项
     *
     * @param
     * @return
     * @date   2020/8/24
     */
    @Override
    public String listNoParam() {
        DepartmentChargeItem departmentChargeItem = new DepartmentChargeItem();

        List<DepartmentChargeItem> departmentChargeItemList = this.list(departmentChargeItem);
        List<DepartmentChargeItem> chargeItemListByGroup = this.getActualDao().listGroupByChargeItemId();
        if (departmentChargeItemList != null && departmentChargeItemList.size() > 0) {
            // 将收费项id去重
            TreeSet ItemIdSet = new TreeSet();
            for (DepartmentChargeItem itemInfo : departmentChargeItemList) {
                ItemIdSet.add(itemInfo.getChargeItemId());
            }

            for (Object Item : ItemIdSet) {
                // 将相同收费项的部门名称放一起
                StringBuffer departmentNameStrBuff = new StringBuffer();
                for (DepartmentChargeItem itemInfo : departmentChargeItemList) {
                    if (itemInfo.getChargeItemId().equals(Item) && StringUtils.isNotEmpty(itemInfo.getDepartmentName())) {
                        departmentNameStrBuff.append(itemInfo.getDepartmentName()).append("，");
                    }
                }

                // 给收费项的部门名称设置值
                String departmentName = "";
                if (StringUtils.isNotEmpty(departmentNameStrBuff.toString())) {
                    departmentName = departmentNameStrBuff.toString().substring(0, departmentNameStrBuff.toString().length() - 1);
                }

                // 给每一个收费项设置关联的部门名称
                for (DepartmentChargeItem itemInfo : chargeItemListByGroup) {
                    if (itemInfo.getChargeItemId().equals(Item)) {
                        itemInfo.setDepartmentName(departmentName);
                    }
                }
            }
        }

        long total = chargeItemListByGroup instanceof Page ? ((Page)chargeItemListByGroup).getTotal() : (long)chargeItemListByGroup.size();
        return new EasyuiPageOutput(total, chargeItemListByGroup).toString();
    }

    /**
     * 根据收费项查询所属组织（商户）
     *
     * @param  chargeItemId
     * @return mchId
     * @date   2020/12/14
     */
    @Override
    public List<DepartmentChargeItem> getListByChargeItemId(String chargeItemId) {
        return this.getActualDao().selectListByChargeItemId(chargeItemId);
    }
}