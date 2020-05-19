package com.dili.ia.service.impl;

import com.dili.ia.domain.Deposit;
import com.dili.ia.mapper.DepositMapper;
import com.dili.ia.service.DepositService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-19 18:20:05.
 */
@Service
public class DepositServiceImpl extends BaseServiceImpl<Deposit, Long> implements DepositService {

    public DepositMapper getActualDao() {
        return (DepositMapper)getDao();
    }
}