package com.dili.ia.service;

import com.dili.ia.domain.BoutiqueFreeSets;
import com.dili.ia.domain.dto.BoutiqueFreeSetsDto;
import com.dili.ss.base.BaseService;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-13 10:49:05.
 */
public interface BoutiqueFreeSetsService extends BaseService<BoutiqueFreeSets, Long> {
    
    /**
     * 获取不同车型的免费时长
     * 
     * @param
     * @return 
     * @date   2020/8/11
     */
    BoutiqueFreeSetsDto getHour();

    /**
     * 修改免费时长
     *
     * @param
     * @return
     * @date   2020/8/11
     */
    void updateFeeSets(BoutiqueFreeSetsDto boutiqueFreeSetsDto);
}