package com.dili.ia.service.impl;

import com.dili.ia.domain.CustomerAccount;
import com.dili.ia.mapper.CustomerAccountMapper;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ss.base.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-10 17:43:43.
 */
@Service
public class CustomerAccountServiceImpl extends BaseServiceImpl<CustomerAccount, Long> implements CustomerAccountService {

    public CustomerAccountMapper getActualDao() {
        return (CustomerAccountMapper)getDao();
    }
}