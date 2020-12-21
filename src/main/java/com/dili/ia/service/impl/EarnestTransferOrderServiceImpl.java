package com.dili.ia.service.impl;

import com.dili.ia.domain.EarnestTransferOrder;
import com.dili.ia.mapper.EarnestTransferOrderMapper;
import com.dili.ia.service.EarnestTransferOrderService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-12-21 16:39:34.
 */
@Service
public class EarnestTransferOrderServiceImpl extends BaseServiceImpl<EarnestTransferOrder, Long> implements EarnestTransferOrderService {

    public EarnestTransferOrderMapper getActualDao() {
        return (EarnestTransferOrderMapper)getDao();
    }
}