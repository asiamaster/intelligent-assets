package com.dili.ia.mapper;

import com.dili.ia.domain.AssetsRental;
import com.dili.ia.domain.dto.AssetsRentalDto;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author:       xiaosa
 * @date:         2020/11/25
 * @version:      农批业务系统重构
 * @description:  资产出租预设
 */
public interface AssetsRentalMapper extends MyMapper<AssetsRental> {

    /**
     * 根据摊位 id 查询相关的预设信息
     *
     * @param  assetsRentalDto
     * @return BaseOutput
     * @date   2020/12/2
     */
    AssetsRentalDto getRentalByAssetsId(AssetsRentalDto assetsRentalDto);

    /**
     * 根据摊位 ids 查询是否属于一个批次
     *
     * @param  assetsIds
     * @return BaseOutput
     * @date   2020/12/2
     */
    List<Long> belongsBatchByAssetsIds(@Param("assetsIds") List<Long> assetsIds);
}