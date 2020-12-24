package com.dili.ia.service;

import com.dili.ia.domain.BoutiqueFreeSets;
import com.dili.ia.domain.dto.BoutiqueFreeSetsDto;
import com.dili.ss.base.BaseService;

/**
 * @author:       xiaosa
 * @date:         2020/7/15
 * @version:      农批业务系统重构
 * @description:  精品停车免费时长
 */
public interface BoutiqueFreeSetsService extends BaseService<BoutiqueFreeSets, Long> {
    
    /**
     * 获取不同车型的免费时长
     * 
     * @return BoutiqueFreeSetsDto
     * @date   2020/8/11
     */
    BoutiqueFreeSetsDto getHour();

    /**
     * 修改免费时长
     *
     * @param  boutiqueFreeSetsDto
     * @date   2020/8/11
     */
    void updateFeeSets(BoutiqueFreeSetsDto boutiqueFreeSetsDto);

    /**
     * 根据车型查询免费时长
     *
     * @param  carTypeName
     * @date   2020/8/11
     */
    BoutiqueFreeSets getByCarTypeName(String carTypeName);
}