package com.dili.ia.service.impl;

import com.dili.ia.domain.Meter;
import com.dili.ia.mapper.MeterMapper;
import com.dili.ia.service.MeterService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author:      xiaosa
 * @date:        2020/6/12
 * @version:     农批业务系统重构
 * @description: 水电费 - 表的相关业务 impl 层
 */
@Service
public class MeterServiceImpl extends BaseServiceImpl<Meter, Long> implements MeterService {

    public MeterMapper getActualDao() {
        return (MeterMapper)getDao();
    }
}