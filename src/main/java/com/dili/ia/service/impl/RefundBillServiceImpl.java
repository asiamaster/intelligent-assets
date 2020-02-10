package com.dili.ia.service.impl;

import com.dili.ia.domain.RefundBill;
import com.dili.ia.mapper.RefundBillMapper;
import com.dili.ia.service.RefundBillService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-10 12:31:39.
 */
@Service
public class RefundBillServiceImpl extends BaseServiceImpl<RefundBill, Long> implements RefundBillService {

    public RefundBillMapper getActualDao() {
        return (RefundBillMapper)getDao();
    }
}