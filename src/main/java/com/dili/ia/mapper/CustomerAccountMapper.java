package com.dili.ia.mapper;

import com.dili.ia.domain.CustomerAccount;
import com.dili.ss.base.MyMapper;
@Deprecated
public interface CustomerAccountMapper extends MyMapper<CustomerAccount> {

    Integer addEarnestBalance(Long id, Long amount);

    Integer deductEarnestBalance(Long id, Long amount);

    Integer addTransferBalance(Long id, Long amount);

    Integer deductTransferBalance(Long id, Long amount);

}