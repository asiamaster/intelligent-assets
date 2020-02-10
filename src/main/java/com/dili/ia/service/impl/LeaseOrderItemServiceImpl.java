package com.dili.ia.service.impl;

import com.dili.ia.domain.LeaseOrderItem;
import com.dili.ia.mapper.LeaseOrderItemMapper;
import com.dili.ia.service.LeaseOrderItemService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-10 12:31:39.
 */
@Service
public class LeaseOrderItemServiceImpl extends BaseServiceImpl<LeaseOrderItem, Long> implements LeaseOrderItemService {

    public LeaseOrderItemMapper getActualDao() {
        return (LeaseOrderItemMapper)getDao();
    }
}