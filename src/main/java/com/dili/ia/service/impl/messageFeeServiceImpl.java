package com.dili.ia.service.impl;

import com.dili.ia.domain.messageFee;
import com.dili.ia.mapper.messageFeeMapper;
import com.dili.ia.service.messageFeeService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-08-24 16:16:50.
 */
@Service
public class messageFeeServiceImpl extends BaseServiceImpl<messageFee, Long> implements messageFeeService {

    public messageFeeMapper getActualDao() {
        return (messageFeeMapper)getDao();
    }
}