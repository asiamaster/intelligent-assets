package com.dili.ia.mapper;

import com.dili.ia.domain.DepositOrder;
import com.dili.ss.base.MyMapper;

import java.util.List;

public interface DepositOrderMapper extends MyMapper<DepositOrder> {
    Integer updateRelatedState(DepositOrder depositOrder);
}