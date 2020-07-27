package com.dili.ia.service.impl;

import com.dili.ia.domain.Passport;
import com.dili.ia.mapper.PassportMapper;
import com.dili.ia.service.PassportService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * @author:      xiaosa
 * @date:        2020/7/27
 * @version:     农批业务系统重构
 * @description: 通行证
 */
@Service
public class PassportServiceImpl extends BaseServiceImpl<Passport, Long> implements PassportService {

    public PassportMapper getActualDao() {
        return (PassportMapper)getDao();
    }
}