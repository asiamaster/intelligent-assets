package com.dili.ia.mapper;

import com.dili.ia.domain.DepositBalance;
import com.dili.ia.domain.dto.DepositBalanceParam;
import com.dili.ia.domain.dto.DepositBalanceQuery;
import com.dili.ss.base.MyMapper;

import java.util.List;

public interface DepositBalanceMapper extends MyMapper<DepositBalance> {
    Long sumBalance(DepositBalanceQuery depositBalanceQuery);

    DepositBalance getDepositBalanceExact(DepositBalanceParam depositBalanceParam);

    Integer deductDepositBalance(Long id, Long amount);
}