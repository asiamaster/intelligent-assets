package com.dili.ia.service;

import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ss.base.BaseService;

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
}