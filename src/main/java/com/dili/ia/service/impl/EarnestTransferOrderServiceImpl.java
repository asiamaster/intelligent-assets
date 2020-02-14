package com.dili.ia.service.impl;

import com.dili.ia.domain.EarnestTransferOrder;
import com.dili.ia.mapper.EarnestTransferOrderMapper;
import com.dili.ia.service.EarnestTransferOrderService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Service
public class EarnestTransferOrderServiceImpl extends BaseServiceImpl<EarnestTransferOrder, Long> implements EarnestTransferOrderService {

    public EarnestTransferOrderMapper getActualDao() {
        return (EarnestTransferOrderMapper)getDao();
    }
}