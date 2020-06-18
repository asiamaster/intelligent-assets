package com.dili.ia.service.impl;

import com.dili.ia.domain.ChargebackOrder;
import com.dili.ia.mapper.ChargebackOrderMapper;
import com.dili.ia.service.ChargebackOrderService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-18 11:36:18.
 */
@Service
public class ChargebackOrderServiceImpl extends BaseServiceImpl<ChargebackOrder, Long> implements ChargebackOrderService {

    public ChargebackOrderMapper getActualDao() {
        return (ChargebackOrderMapper)getDao();
    }
}