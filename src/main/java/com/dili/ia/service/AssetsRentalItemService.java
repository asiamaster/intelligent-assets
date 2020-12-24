package com.dili.ia.service;

import com.dili.assets.sdk.dto.AssetsDTO;
import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ia.domain.dto.AssetsRentalDto;
import com.dili.ia.domain.dto.AssetsRentalItemDto;
import com.dili.ss.base.BaseService;
import org.apache.ibatis.annotations.Param;

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
     * 根据关联主键批量删除对应的资产
     *
     * @param   rentalIdList
     * @return
     * @date    2020/11/26
     */
    int deleteByRentalIdList(List<Long> rentalIdList);

    /**
     * 根据 assetsIds 查询属于表中的 assetsId 的集合,摊位出租预设的主表状态是启用
     *
     * @param  assetsIds
     * @param  state
     * @return assetsIds
     * @date   2020/12/7
     */
    List<Long> listRentalItemsByAssetsIds(List<Long> assetsIds, Integer state);

    /**
     * 过滤出不属于预设池中的摊位集合
     *
     * @param  assetsRentalDto
     * @return list
     * @date   2020/12/8
     */
    List<AssetsDTO> filterAssets(AssetsRentalDto assetsRentalDto);

    /**
     * 根据预设id查询预设的摊位信息
     *
     * @param  rentalId
     * @return list
     * @date   2020/12/24
     */
    List<AssetsRentalItem> listRentalItemsByRentalId(Long rentalId);
}