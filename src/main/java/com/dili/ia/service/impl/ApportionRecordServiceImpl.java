package com.dili.ia.service.impl;

import com.dili.ia.domain.ApportionRecord;
import com.dili.ia.mapper.ApportionRecordMapper;
import com.dili.ia.service.ApportionRecordService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-24 11:15:42.
 */
@Service
public class ApportionRecordServiceImpl extends BaseServiceImpl<ApportionRecord, Long> implements ApportionRecordService {

    public ApportionRecordMapper getActualDao() {
        return (ApportionRecordMapper)getDao();
    }
}