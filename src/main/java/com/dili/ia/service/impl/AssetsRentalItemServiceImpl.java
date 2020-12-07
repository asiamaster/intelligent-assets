package com.dili.ia.service.impl;

import com.dili.ia.domain.AssetsRentalItem;
import com.dili.ia.glossary.AssetsRentalStateEnum;
import com.dili.ia.mapper.AssetsRentalItemMapper;
import com.dili.ia.service.AssetsRentalItemService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

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
     * @param   assetsRentalId
     * @return
     * @date    2020/11/26
     */
    @Override
    public void deleteByRentalId(Long assetsRentalId) {
        this.getActualDao().deleteByRentalId(assetsRentalId);
    }

    /**
     * 根据 assetsIds 查询属于表中的 assetsId 的集合,摊位出租预设的主表状态是启用
     *
     * @param  assetsIds
     * @param  state
     * @return assetsIds
     * @date   2020/12/7
     */
    @Override
    public List<Long> listRentalItemsByAssetsIds(List<Long> assetsIds, Integer state) {
        return this.getActualDao().listRentalItemsByAssetsIds(assetsIds, state);
    }
}