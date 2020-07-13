package com.dili.ia.service.impl;

import com.dili.ia.domain.BoutiqueFreeSets;
import com.dili.ia.mapper.BoutiqueFreeSetsMapper;
import com.dili.ia.service.BoutiqueFreeSetsService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-13 10:49:05.
 */
@Service
public class BoutiqueFreeSetsServiceImpl extends BaseServiceImpl<BoutiqueFreeSets, Long> implements BoutiqueFreeSetsService {

    public BoutiqueFreeSetsMapper getActualDao() {
        return (BoutiqueFreeSetsMapper)getDao();
    }
}