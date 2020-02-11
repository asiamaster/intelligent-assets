package com.dili.ia.service.impl;

import com.dili.ia.domain.LeaseOrder;
import com.dili.ia.mapper.LeaseOrderMapper;
import com.dili.ia.service.LeaseOrderService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-11 15:54:49.
 */
@Service
public class LeaseOrderServiceImpl extends BaseServiceImpl<LeaseOrder, Long> implements LeaseOrderService {

    public LeaseOrderMapper getActualDao() {
        return (LeaseOrderMapper)getDao();
    }
}