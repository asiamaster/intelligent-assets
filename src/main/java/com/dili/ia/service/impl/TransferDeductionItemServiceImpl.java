package com.dili.ia.service.impl;

import com.dili.ia.domain.TransferDeductionItem;
import com.dili.ia.mapper.TransferDeductionItemMapper;
import com.dili.ia.service.TransferDeductionItemService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-10 12:31:39.
 */
@Service
public class TransferDeductionItemServiceImpl extends BaseServiceImpl<TransferDeductionItem, Long> implements TransferDeductionItemService {

    public TransferDeductionItemMapper getActualDao() {
        return (TransferDeductionItemMapper)getDao();
    }
}