package com.dili.ia.service.impl;

import com.dili.ia.domain.StockOut;
import com.dili.ia.mapper.StockOutMapper;
import com.dili.ia.service.StockOutService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockOutServiceImpl extends BaseServiceImpl<StockOut, Long> implements StockOutService {

    public StockOutMapper getActualDao() {
        return (StockOutMapper)getDao();
    }
}