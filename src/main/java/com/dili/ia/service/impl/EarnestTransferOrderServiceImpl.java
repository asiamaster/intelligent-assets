package com.dili.ia.service.impl;

import com.dili.ia.domain.EarnestTransferOrder;
import com.dili.ia.mapper.EarnestTransferOrderMapper;
import com.dili.ia.service.EarnestTransferOrderService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 17:07:49.
 */
@Service
public class EarnestTransferOrderServiceImpl extends BaseServiceImpl<EarnestTransferOrder, Long> implements EarnestTransferOrderService {

    public EarnestTransferOrderMapper getActualDao() {
        return (EarnestTransferOrderMapper)getDao();
    }
}