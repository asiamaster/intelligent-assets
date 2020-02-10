package com.dili.ia.service.impl;

import com.dili.ia.domain.TransactionDetails;
import com.dili.ia.mapper.TransactionDetailsMapper;
import com.dili.ia.service.TransactionDetailsService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-10 17:43:44.
 */
@Service
public class TransactionDetailsServiceImpl extends BaseServiceImpl<TransactionDetails, Long> implements TransactionDetailsService {

    public TransactionDetailsMapper getActualDao() {
        return (TransactionDetailsMapper)getDao();
    }
}