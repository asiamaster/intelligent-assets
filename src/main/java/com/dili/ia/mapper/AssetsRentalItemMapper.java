package com.dili.ia.mapper;

import com.dili.ia.domain.AssetsRentalItem;
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
}