package com.dili.ia.service.impl;

import com.dili.ia.domain.MeterDetail;
import com.dili.ia.mapper.MeterDetailMapper;
import com.dili.ia.service.MeterDetailService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:35:07.
 */
@Service
public class MeterDetailServiceImpl extends BaseServiceImpl<MeterDetail, Long> implements MeterDetailService {

    public MeterDetailMapper getActualDao() {
        return (MeterDetailMapper)getDao();
    }
}