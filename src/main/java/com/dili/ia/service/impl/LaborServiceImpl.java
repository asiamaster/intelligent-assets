package com.dili.ia.service.impl;

import com.dili.ia.domain.Labor;
import com.dili.ia.mapper.LaborMapper;
import com.dili.ia.service.LaborService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-27 14:50:45.
 */
@Service
public class LaborServiceImpl extends BaseServiceImpl<Labor, Long> implements LaborService {

    public LaborMapper getActualDao() {
        return (LaborMapper)getDao();
    }
}