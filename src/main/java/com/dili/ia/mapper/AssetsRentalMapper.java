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
     * 根据 assetId 和 预设状态为启用，查询相关的预设信息
     *
     * @param  assetsRentalDto
     * @return AssetsRentalDto
     * @date   2020/12/2
     */
    AssetsRentalDto getRentalByRentalDto(AssetsRentalDto assetsRentalDto);

    /**
     * 根据同一批次、同一商户，模糊查询摊位出租预设信息集合
     *
     * @param  assetsRentalDto
     * @return list
     * @date   2020/12/7
     */
    List<AssetsRentalDto> listRentalsByRentalDtoAndKeyWord(AssetsRentalDto assetsRentalDto);

    /**
     * 根据 assetsIds 批量查询摊位预设的 mchId 和 batchId
     *
     * @param  assetsIds
     * @return List
     * @date   2020/12/2
     */
    List<AssetsRentalDto> listByAssetsIds(@Param("assetsIds") List<Long> assetsIds);

}