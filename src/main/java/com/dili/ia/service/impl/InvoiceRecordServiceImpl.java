package com.dili.ia.service.impl;

import com.dili.ia.domain.InvoiceRecord;
import com.dili.ia.mapper.InvoiceRecordMapper;
import com.dili.ia.service.InvoiceRecordService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-30 11:20:01.
 */
@Service
public class InvoiceRecordServiceImpl extends BaseServiceImpl<InvoiceRecord, Long> implements InvoiceRecordService {

    public InvoiceRecordMapper getActualDao() {
        return (InvoiceRecordMapper)getDao();
    }
}