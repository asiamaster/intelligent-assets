package com.dili.ia.service.impl;

import com.dili.ia.domain.EarnestOrder;
import com.dili.ia.mapper.EarnestOrderMapper;
import com.dili.ia.service.EarnestOrderService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-10 17:43:43.
 */
@Service
public class EarnestOrderServiceImpl extends BaseServiceImpl<EarnestOrder, Long> implements EarnestOrderService {

    public EarnestOrderMapper getActualDao() {
        return (EarnestOrderMapper)getDao();
    }

    @Override
    public Boolean checkCustomerExist(Long customerId, Long marketId) {
        return null;
    }
}