package com.dili.ia.service;

import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ss.base.BaseService;

import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/11/25
 * @version:      农批业务系统重构
 * @description:  资产出租预设 中间表
 */
public interface AssetsRentalItemService extends BaseService<AssetsRentalItem, Long> {
    
    /**
     * 根据关联主键删除对应的资产
     * 
     * @param   stallRentPresetId
     * @return
     * @date    2020/11/26
     */
    void deleteByRentalId(Long stallRentPresetId);

    /**
     * 根据 assetsIds 查询属于表中的 assetsId 的集合,摊位出租预设的主表状态是启用
     *
     * @param  assetsIds
     * @param  state
     * @return assetsIds
     * @date   2020/12/7
     */
    List<Long> listRentalItemsByAssetsIds(List<Long> assetsIds, Integer state);
}