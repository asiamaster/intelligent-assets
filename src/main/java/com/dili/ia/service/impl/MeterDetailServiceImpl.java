package com.dili.ia.service.impl;

import com.dili.ia.domain.MeterDetail;
import com.dili.ia.mapper.MeterDetailMapper;
import com.dili.ia.service.MeterDetailService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author:      xiaosa
 * @date:        2020/6/23
 * @version:     农批业务系统重构
 * @description: 水电费 service 实现层
 */
@Service
public class MeterDetailServiceImpl extends BaseServiceImpl<MeterDetail, Long> implements MeterDetailService {

    public MeterDetailMapper getActualDao() {
        return (MeterDetailMapper)getDao();
    }
}