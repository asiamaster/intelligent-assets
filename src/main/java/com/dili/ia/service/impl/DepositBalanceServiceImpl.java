package com.dili.ia.service.impl;

import com.dili.ia.domain.DepositBalance;
import com.dili.ia.mapper.DepositBalanceMapper;
import com.dili.ia.service.DepositBalanceService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-29 15:15:26.
 */
@Service
public class DepositBalanceServiceImpl extends BaseServiceImpl<DepositBalance, Long> implements DepositBalanceService {

    public DepositBalanceMapper getActualDao() {
        return (DepositBalanceMapper)getDao();
    }
}