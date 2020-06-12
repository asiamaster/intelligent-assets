package com.dili.ia.service.impl;

import com.dili.ia.domain.CustomerMeter;
import com.dili.ia.mapper.CustomerMeterMapper;
import com.dili.ia.service.CustomerMeterService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:35:07.
 */
@Service
public class CustomerMeterServiceImpl extends BaseServiceImpl<CustomerMeter, Long> implements CustomerMeterService {

    public CustomerMeterMapper getActualDao() {
        return (CustomerMeterMapper)getDao();
    }
}