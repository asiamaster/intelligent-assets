package com.dili.ia.service.impl;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.mapper.RefundOrderMapper;
import com.dili.ia.service.RefundOrderService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-28 21:11:27.
 */
@Service
public class RefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderService {

    public RefundOrderMapper getActualDao() {
        return (RefundOrderMapper)getDao();
    }
}