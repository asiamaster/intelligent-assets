package com.dili.ia.service.impl;

import com.dili.ia.domain.Stock;
import com.dili.ia.mapper.StockMapper;
import com.dili.ia.service.StockService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockServiceImpl extends BaseServiceImpl<Stock, Long> implements StockService {

    public StockMapper getActualDao() {
        return (StockMapper)getDao();
    }
}