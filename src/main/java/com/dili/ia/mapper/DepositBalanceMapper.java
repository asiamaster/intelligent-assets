package com.dili.ia.mapper;

import com.dili.ia.domain.DepositBalance;
import com.dili.ia.domain.dto.DepositBalanceQuery;
import com.dili.ss.base.MyMapper;

public interface DepositBalanceMapper extends MyMapper<DepositBalance> {
    Long sumBalance(DepositBalanceQuery depositBalanceQuery);
}