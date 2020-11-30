package com.dili.ia.service.impl;

import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ia.mapper.AssetsRentalItemMapper;
import com.dili.ia.service.AssetsRentalItemService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

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
     * @param   stallRentPresetId
     * @return
     * @date    2020/11/26
     */
    @Override
    public void deleteByRentalId(Long stallRentPresetId) {
        this.getActualDao().deleteByRentalId(stallRentPresetId);
    }
}