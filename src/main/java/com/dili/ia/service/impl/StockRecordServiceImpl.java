package com.dili.ia.service.impl;

import com.dili.ia.domain.StockRecord;
import com.dili.ia.mapper.StockRecordMapper;
import com.dili.ia.service.StockRecordService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-06-12 11:14:28.
 */
@Service
public class StockRecordServiceImpl extends BaseServiceImpl<StockRecord, Long> implements StockRecordService {

    public StockRecordMapper getActualDao() {
        return (StockRecordMapper)getDao();
    }
}