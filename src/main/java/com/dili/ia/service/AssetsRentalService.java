package com.dili.ia.service;

import com.dili.ia.domain.AssetsRental;
import com.dili.ia.domain.dto.AssetsRentalDto;
import com.dili.ss.base.BaseService;

/**
 * @author:       xiaosa
 * @date:         2020/11/25
 * @version:      农批业务系统重构
 * @description:  资产出租预设
 */
public interface AssetsRentalService extends BaseService<AssetsRental, Long> {

    /**
     * 新增资产出租预设
     *
     * @param  assetsRentalDto
     * @return AssetsRental
     * @date   2020/11/26
     */
    AssetsRental addAssetsRental(AssetsRentalDto assetsRentalDto);

    /**
     * 修改资产出租预设
     *
     * @param  assetsRentalDto
     * @return AssetsRental
     * @date   2020/11/26
     */
    AssetsRental updateAssetsRental(AssetsRentalDto assetsRentalDto);
}