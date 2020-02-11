package com.dili.ia.service.impl;

import com.dili.ia.domain.PaymentBill;
import com.dili.ia.mapper.PaymentBillMapper;
import com.dili.ia.service.PaymentBillService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-11 15:54:49.
 */
@Service
public class PaymentBillServiceImpl extends BaseServiceImpl<PaymentBill, Long> implements PaymentBillService {

    public PaymentBillMapper getActualDao() {
        return (PaymentBillMapper)getDao();
    }
}