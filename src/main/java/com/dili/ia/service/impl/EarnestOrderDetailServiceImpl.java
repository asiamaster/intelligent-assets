package com.dili.ia.service.impl;

import com.dili.ia.domain.EarnestOrderDetail;
import com.dili.ia.mapper.EarnestOrderDetailMapper;
import com.dili.ia.service.EarnestOrderDetailService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-10 17:43:43.
 */
@Service
public class EarnestOrderDetailServiceImpl extends BaseServiceImpl<EarnestOrderDetail, Long> implements EarnestOrderDetailService {

    public EarnestOrderDetailMapper getActualDao() {
        return (EarnestOrderDetailMapper)getDao();
    }
}