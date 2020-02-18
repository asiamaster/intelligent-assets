package com.dili.ia.service.impl;

import com.dili.ia.domain.CustomerAccount;
import com.dili.ia.mapper.CustomerAccountMapper;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.dto.DTOUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-02-14 10:18:23.
 */
@Service
public class CustomerAccountServiceImpl extends BaseServiceImpl<CustomerAccount, Long> implements CustomerAccountService {

    public CustomerAccountMapper getActualDao() {
        return (CustomerAccountMapper)getDao();
    }

    @Override
    public Boolean checkCustomerAccountExist(Long customerId, Long marketId) {
       CustomerAccount ca = this.getCustomerAccountByCustomerId(customerId, marketId);
        if (null != ca){
            return true;
        }
        return false;
    }

    @Override
    public CustomerAccount getCustomerAccountByCustomerId(Long customerId, Long marketId) {
        CustomerAccount customerAccount = DTOUtils.newDTO(CustomerAccount.class);
        customerAccount.setCustomerId(customerId);
        customerAccount.setMarketId(marketId);
        List<CustomerAccount> list = getActualDao().select(customerAccount);
        if (CollectionUtils.isEmpty(list) || list.size() > 1){
            //@TODO异常提醒处理
            return null;
        }
       return list.get(0);
    }
}