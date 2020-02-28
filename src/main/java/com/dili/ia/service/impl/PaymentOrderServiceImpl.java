package com.dili.ia.service.impl;

import com.dili.ia.domain.PaymentOrder;
import com.dili.ia.mapper.PaymentOrderMapper;
import com.dili.ia.service.PaymentOrderService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-28 21:11:27.
 */
@Service
public class PaymentOrderServiceImpl extends BaseServiceImpl<PaymentOrder, Long> implements PaymentOrderService {

    public PaymentOrderMapper getActualDao() {
        return (PaymentOrderMapper)getDao();
    }
}