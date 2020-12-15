package com.dili.ia.service.impl;

import com.dili.ia.domain.RefundOrder;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.mapper.RefundOrderMapper;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.settlement.enums.FeeTypeEnum;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.domain.BaseOutput;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 19:34:40.
 */
@Service
public class EarnestRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService {

    public RefundOrderMapper getActualDao() {
        return (RefundOrderMapper)getDao();
    }
    @SuppressWarnings("all")
    @Autowired
    DepartmentRpc departmentRpc;
    @Autowired
    CustomerAccountService customerAccountService;

    @Override
    public Set<String> getBizType() {
        return Sets.newHashSet(BizTypeEnum.EARNEST.getCode());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput submitHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput withdrawHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    @Override
    public BaseOutput cancelHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    @Override
    public BaseOutput<Map<String,Object>> buildBusinessPrintData(RefundOrder refundOrder) {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("printTemplateCode",PrintTemplateEnum.EARNEST_REFUND_ORDER.getCode());
        return BaseOutput.success().setData(resultMap);
    }

    @Override
    public List<SettleFeeItem> buildSettleFeeItem(RefundOrder refundOrder) {
        //组装费用项
        List<SettleFeeItem> settleFeeItemList = new ArrayList<>();
        SettleFeeItem sfItem = new SettleFeeItem();
        sfItem.setFeeType(FeeTypeEnum.定金.getCode()); //定金固定
        sfItem.setFeeName(FeeTypeEnum.定金.getName()); //定金固定
        sfItem.setAmount(refundOrder.getTotalRefundAmount());
        settleFeeItemList.add(sfItem);

        return settleFeeItemList;
    }
}