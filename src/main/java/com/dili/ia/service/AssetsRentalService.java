package com.dili.ia.service;

import com.dili.ia.domain.AssetsRental;
import com.dili.ia.domain.dto.AssetsRentalDto;
import com.dili.ia.domain.dto.AssetsRentalItemDto;
import com.dili.ss.base.BaseService;
import com.dili.uap.sdk.domain.UserTicket;

import java.util.List;

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
     * @param userTicket
     * @return AssetsRental
     * @date   2020/11/26
     */
    AssetsRental addAssetsRental(AssetsRentalDto assetsRentalDto, UserTicket userTicket) throws Exception;

    /**
     * 修改资产出租预设
     *
     * @param  assetsRentalDto
     * @return AssetsRental
     * @date   2020/11/26
     */
    AssetsRental updateAssetsRental(AssetsRentalDto assetsRentalDto) throws Exception;

    /**
     * 启用或者禁用
     *
     * @param  id
     * @return
     * @date   2020/11/26
     */
    void enableOrDisable(Long id);

    /**
     * 根据摊位 id 查询相关的预设信息
     *
     * @param  assetsId
     * @return AssetsRentalDto
     * @date   2020/12/2
     */
    AssetsRentalDto getRentalByAssetsId(Long assetsId);

    /**
     * 根据同一批次、同一商户、名称模糊查询摊位出租预设信息集合
     *
     * @param  assetsRentalDto
     * @return list
     * @date   2020/12/7
     */
    List<AssetsRentalDto> listRentalsByRentalDtoAndKeyWord(AssetsRentalDto assetsRentalDto);

    /**
     * 根据是否属于预设池，传入 assetIds ，过滤掉不属于预设池的 ids
     *
     * @param  assetsIds
     * @return List
     * @date   2020/12/7
     */
    List<Long> filterAssetsIdsByTable(List<Long> assetsIds);

    /**
     * 根据摊位 ids 批量查询
     *
     * @param  assetsIds
     * @return BaseOutput
     * @date   2020/12/2
     */
    List<AssetsRentalDto> listByAssetsIds(List<Long> assetsIds);

    /**
     * 根据区域id删除对应的关联摊位
     *
     * @param
     * @return
     * @date   2020/12/8
     */
    void deleteAssetsByDistrictId(Long districtId) throws Exception;

}