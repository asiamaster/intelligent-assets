package com.dili.ia.service.impl;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransactionDetails;
import com.dili.ia.domain.dto.RefundOrderPrintDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.glossary.TransactionItemTypeEnum;
import com.dili.ia.glossary.TransactionSceneTypeEnum;
import com.dili.ia.mapper.RefundOrderMapper;
import com.dili.ia.rpc.SettlementRpc;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.ia.service.TransactionDetailsService;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.google.common.collect.Sets;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 19:34:40.
 */
@Service
public class EarnestRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService {

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
        return Sets.newHashSet(BizTypeEnum.EARNEST.getCode());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput submitHandler(RefundOrder refundOrder) {
        customerAccountService.frozenEarnest(refundOrder.getCustomerId(), refundOrder.getMarketId(), refundOrder.getPayeeAmount());

        Integer bizType = BizTypeEnum.EARNEST.getCode();
        Integer itemType = TransactionItemTypeEnum.EARNEST.getCode();
        Integer sceneType = TransactionSceneTypeEnum.FROZEN.getCode();
        TransactionDetails transactionDetails = transactionDetailsService.buildByConditions(sceneType, bizType, itemType, refundOrder.getPayeeAmount(), refundOrder.getOrderId(), refundOrder.getOrderCode(), refundOrder.getCustomerId(), refundOrder.getRefundReason(), refundOrder.getMarketId());
        transactionDetailsService.insertSelective(transactionDetails);
        return BaseOutput.success();
    }

    @Override
    public BaseOutput withdrawHandler(RefundOrder refundOrder) {
        //@TODO 解冻客户资金，写入解冻记录
        customerAccountService.unfrozenEarnest(refundOrder.getCustomerId(), refundOrder.getMarketId(), refundOrder.getPayeeAmount());
        Integer bizType = BizTypeEnum.EARNEST.getCode();
        Integer itemType = TransactionItemTypeEnum.EARNEST.getCode();
        Integer sceneType = TransactionSceneTypeEnum.UNFROZEN.getCode();
        TransactionDetails transactionDetails = transactionDetailsService.buildByConditions(sceneType, bizType, itemType, refundOrder.getPayeeAmount(), refundOrder.getOrderId(), refundOrder.getOrderCode(), refundOrder.getCustomerId(), refundOrder.getRefundReason(), refundOrder.getMarketId());
        transactionDetailsService.insertSelective(transactionDetails);
        return BaseOutput.success();
    }

    @Override
    public BaseOutput refundSuccessHandler(Long refundOrderId) {
        //@TODO 解冻客户资金，扣除客户余额，写入解冻，扣除记录记录

        return null;
    }

    @Override
    public BaseOutput<RefundOrderPrintDto> buildBusinessPrintData(RefundOrderPrintDto refundOrderPrintDto) {
        refundOrderPrintDto.setPrintTemplateCode(PrintTemplateEnum.EARNEST_REFUND_ORDER.getCode());
        return BaseOutput.success().setData(refundOrderPrintDto);
    }
}