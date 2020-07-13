package com.dili.ia.service.impl;

import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.mapper.BoutiqueFeeOrderMapper;
import com.dili.ia.service.BoutiqueFeeOrderService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-13 10:49:05.
 */
@Service
public class BoutiqueFeeOrderServiceImpl extends BaseServiceImpl<BoutiqueFeeOrder, Long> implements BoutiqueFeeOrderService {

    public BoutiqueFeeOrderMapper getActualDao() {
        return (BoutiqueFeeOrderMapper)getDao();
    }
}