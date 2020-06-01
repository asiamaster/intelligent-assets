package com.dili.ia.service.impl;

import com.dili.ia.domain.LeaseChargeItem;
import com.dili.ia.mapper.LeaseChargeItemMapper;
import com.dili.ia.service.LeaseChargeItemService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-05-29 16:13:04.
 */
@Service
public class LeaseChargeItemServiceImpl extends BaseServiceImpl<LeaseChargeItem, Long> implements LeaseChargeItemService {

    public LeaseChargeItemMapper getActualDao() {
        return (LeaseChargeItemMapper)getDao();
    }
}