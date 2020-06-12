package com.dili.ia.service.impl;

import com.dili.ia.domain.StockInDetail;
import com.dili.ia.mapper.StockInDetailMapper;
import com.dili.ia.service.StockInDetailService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockInDetailServiceImpl extends BaseServiceImpl<StockInDetail, Long> implements StockInDetailService {

    public StockInDetailMapper getActualDao() {
        return (StockInDetailMapper)getDao();
    }
}