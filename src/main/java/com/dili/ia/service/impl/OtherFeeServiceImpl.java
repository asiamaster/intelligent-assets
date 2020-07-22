package com.dili.ia.service.impl;

import com.dili.ia.domain.OtherFee;
import com.dili.ia.mapper.OtherFeeMapper;
import com.dili.ia.service.OtherFeeService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-07-21 18:08:01.
 */
@Service
public class OtherFeeServiceImpl extends BaseServiceImpl<OtherFee, Long> implements OtherFeeService {

    public OtherFeeMapper getActualDao() {
        return (OtherFeeMapper)getDao();
    }
}