package com.dili.ia.service.impl;

import com.dili.ia.domain.StockIn;
import com.dili.ia.mapper.StockInMapper;
import com.dili.ia.service.StockInService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockInServiceImpl extends BaseServiceImpl<StockIn, Long> implements StockInService {

    public StockInMapper getActualDao() {
        return (StockInMapper)getDao();
    }
}