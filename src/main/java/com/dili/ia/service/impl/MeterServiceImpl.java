package com.dili.ia.service.impl;

import com.dili.ia.domain.Meter;
import com.dili.ia.mapper.MeterMapper;
import com.dili.ia.service.MeterService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:35:07.
 */
@Service
public class MeterServiceImpl extends BaseServiceImpl<Meter, Long> implements MeterService {

    public MeterMapper getActualDao() {
        return (MeterMapper)getDao();
    }
}