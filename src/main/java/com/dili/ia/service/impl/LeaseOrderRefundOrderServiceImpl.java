package com.dili.ia.service.impl;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.dto.RefundOrderPrintDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.mapper.RefundOrderMapper;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.ia.service.TransactionDetailsService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 19:34:40.
 */
@Service
public class LeaseOrderRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService {

    public RefundOrderMapper getActualDao() {
        return (RefundOrderMapper)getDao();
    }
    @Autowired
    SettlementRpc settlementRpc;
    @Autowired
    DepartmentRpc departmentRpc;
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    TransactionDetailsService transactionDetailsService;

    @Override
    public Set<Integer> getBizType() {
        return Sets.newHashSet(BizTypeEnum.BOOTH_LEASE.getCode());
    }

    @Override
    public BaseOutput submitHandler(RefundOrder refundOrder) {
        return null;
    }

    @Override
    public BaseOutput withdrawHandler(RefundOrder refundOrder) {
        return null;
    }

    @Override
    public BaseOutput refundSuccessHandler(RefundOrder refundOrder) {
        return null;
    }

    @Override
    public BaseOutput cancelHandler(RefundOrder refundOrder) {

        return null;
    }

    @Override
    public BaseOutput<RefundOrderPrintDto> buildBusinessPrintData(RefundOrderPrintDto refundOrderPrintDto) {
        return null;
    }
}