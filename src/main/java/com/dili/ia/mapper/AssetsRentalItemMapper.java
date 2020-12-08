package com.dili.ia.mapper;

import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ia.domain.dto.AssetsRentalItemDto;
import com.dili.ss.base.MyMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
     * @param   assetsRentalId
     * @return
     * @date    2020/11/26
     */
    void deleteByRentalId(@Param("assetsRentalId")Long assetsRentalId);

    /**
     * 根据 assetsIds 查询属于表中的 assetsId 的集合,摊位出租预设的主表状态是启用
     *
     * @param  assetsIds
     * @param  state
     * @return assetsIds
     * @date   2020/12/7
     */
    List<Long> listRentalItemsByAssetsIds(@Param("assetsIds")List<Long> assetsIds, @Param("state")Integer state);

    /**
     * 根据 assetsIds 查询属于表中的 assetsId 的集合
     *
     * @param  assetsIds
     * @return assetsIds
     * @date   2020/12/7
     */
    List<AssetsRentalItemDto> listAssetsItemsByAssetsIds(@Param("assetsIds")List<Long> assetsIds);

    /**
     * 修改摊位的信息
     *
     * @param  assetsRentalItemDto
     * @return
     * @date   2020/12/8
     */
    void updateByAssetsId(AssetsRentalItemDto assetsRentalItemDto);
}