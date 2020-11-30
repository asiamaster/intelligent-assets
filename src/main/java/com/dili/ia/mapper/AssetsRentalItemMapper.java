package com.dili.ia.mapper;

import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

/**
 * @author:       xiaosa
 * @date:         2020/11/25
 * @version:      农批业务系统重构
 * @description:  资产出租预设 中间表
 */
public interface AssetsRentalItemMapper extends MyMapper<AssetsRentalItem> {

    /**
     * 根据关联主键删除对应的资产
     *
     * @param   stallRentPresetId
     * @return
     * @date    2020/11/26
     */
    void deleteByRentalId(@Param("stallRentPresetId") Long stallRentPresetId);
}