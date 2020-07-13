package com.dili.ia.service.impl;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.mapper.BoutiqueEntranceRecordMapper;
import com.dili.ia.service.BoutiqueEntranceRecordService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-13 10:49:05.
 */
@Service
public class BoutiqueEntranceRecordServiceImpl extends BaseServiceImpl<BoutiqueEntranceRecord, Long> implements BoutiqueEntranceRecordService {

    public BoutiqueEntranceRecordMapper getActualDao() {
        return (BoutiqueEntranceRecordMapper)getDao();
    }
}