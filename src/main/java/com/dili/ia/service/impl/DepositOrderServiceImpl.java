package com.dili.ia.service.impl;

import com.dili.ia.domain.DepositOrder;
import com.dili.ia.mapper.DepositOrderMapper;
import com.dili.ia.service.DepositOrderService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-20 17:29:10.
 */
@Service
public class DepositOrderServiceImpl extends BaseServiceImpl<DepositOrder, Long> implements DepositOrderService {

    public DepositOrderMapper getActualDao() {
        return (DepositOrderMapper)getDao();
    }
}