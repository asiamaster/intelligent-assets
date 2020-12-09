package com.dili.ia.service.impl;

import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ia.domain.dto.AssetsRentalItemDto;
import com.dili.ia.glossary.AssetsRentalStateEnum;
import com.dili.ia.mapper.AssetsRentalItemMapper;
import com.dili.ia.service.AssetsRentalItemService;
import com.dili.ss.base.BaseServiceImpl;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/11/25
 * @version:      农批业务系统重构
 * @description:  资产出租预设 中间表
 */
@Service
public class AssetsRentalItemServiceImpl extends BaseServiceImpl<AssetsRentalItem, Long> implements AssetsRentalItemService {

    public AssetsRentalItemMapper getActualDao() {
        return (AssetsRentalItemMapper)getDao();
    }

    /**
     * 根据关联主键删除对应的资产
     *
     * @param   assetsRentalId
     * @return
     * @date    2020/11/26
     */
    @Override
    public void deleteByRentalId(Long assetsRentalId) {
        this.getActualDao().deleteByRentalId(assetsRentalId);
    }

    /**
     * 根据 assetsIds 查询属于表中的 assetsId 的集合,摊位出租预设的主表状态是启用
     *
     * @param  assetsIds
     * @param  state
     * @return assetsIds
     * @date   2020/12/7
     */
    @Override
    public List<Long> listRentalItemsByAssetsIds(List<Long> assetsIds, Integer state) {
        return this.getActualDao().listRentalItemsByAssetsIds(assetsIds, state);
    }

    /**
     * 过滤出不属于预设池中的摊位集合
     *
     * @param  assetsRentalItemDtoList
     * @return list
     * @date   2020/12/8
     */
    @Override
    public List<AssetsRentalItemDto> filterAssets(List<AssetsRentalItemDto> assetsRentalItemDtoList) {
        List<AssetsRentalItemDto> assetsRentalItemDtoReturn = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(assetsRentalItemDtoList)) {
            // 根据前端传送摊位id集合查询预设池中摊位集合
            List<Long> assetsIds = new ArrayList<>();
            for (AssetsRentalItemDto assetsRentalItemDto : assetsRentalItemDtoList) {
                assetsIds.add(assetsRentalItemDto.getAssetsId());
            }
            List<AssetsRentalItemDto> assetsRentalItemDtoInfoList = this.getActualDao().listAssetsItemsByAssetsIds(assetsIds);

            // 预设池中摊位集合
            List<Long> assetsIdsInTable = new ArrayList<>();
            if (CollectionUtils.isNotEmpty(assetsRentalItemDtoInfoList)) {
                for (AssetsRentalItemDto assetsRentalItemDto : assetsRentalItemDtoInfoList) {
                    assetsIdsInTable.add(assetsRentalItemDto.getAssetsId());
                }
            }

            // 排除掉前端传递的摊位id集合有，预设池摊位集合中没有的
            assetsIds.removeAll(assetsIdsInTable);

            // 返回预设池中没有的摊位集合
            if (CollectionUtils.isNotEmpty(assetsIds)) {
                for (AssetsRentalItemDto assetsRentalItemDto : assetsRentalItemDtoList) {
                    for (Long assetsId : assetsIds) {
                        if (assetsId.equals(assetsRentalItemDto.getAssetsId())) {
                            assetsRentalItemDtoReturn.add(assetsRentalItemDto);
                        }
                    }
                }
            }
        }

        return assetsRentalItemDtoReturn;
    }

    /**
     * 修改摊位的信息
     *
     * @param  assetsRentalItemDto
     * @return
     * @date   2020/12/8
     */
    @Override
    public void updateAssetsToRental(AssetsRentalItemDto assetsRentalItemDto)throws Exception {
        assetsRentalItemDto.setModifyTime(LocalDateTime.now());
        this.getActualDao().updateByAssetsId(assetsRentalItemDto);
    }
}