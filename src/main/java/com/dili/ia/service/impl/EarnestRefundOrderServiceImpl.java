package com.dili.ia.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import com.dili.ia.glossary.ChargeItemCodeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.assets.sdk.enums.BusinessChargeItemEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.mapper.RefundOrderMapper;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.rpc.DepartmentRpc;
import com.google.common.collect.Sets;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 19:34:40.
 */
@Service
public class EarnestRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService {
    private static final Logger LOG = LoggerFactory.getLogger(EarnestRefundOrderServiceImpl.class);

    public RefundOrderMapper getActualDao() {
        return (RefundOrderMapper)getDao();
    }
    @SuppressWarnings("all")
    @Autowired
    DepartmentRpc departmentRpc;
    @Autowired
    CustomerAccountService customerAccountService;
    @Autowired
    BusinessChargeItemService businessChargeItemService;

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
        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.queryFixedBusinessChargeItemConfig(refundOrder.getMarketId(), BizTypeEnum.EARNEST.getCode(), YesOrNoEnum.YES.getCode(), YesOrNoEnum.YES.getCode(), ChargeItemCodeEnum.EARNEST.getCode());
        BusinessChargeItemDto chargeItemDto = chargeItemDtos.stream().findFirst().orElse(null);
        if (null == chargeItemDto){
            LOG.info("定金单业务没有查询到固定的【定金】收费项，code={}", ChargeItemCodeEnum.EARNEST.getCode());
            throw new BusinessException("定金单业务没有查询到固定的【定金】收费项，code={}", ChargeItemCodeEnum.EARNEST.getCode());
        }
        sfItem.setChargeItemId(chargeItemDto.getId()); //静态收费项
        sfItem.setChargeItemName(chargeItemDto.getChargeItem()); //静态收费项
        //定金费用类型固定，必须传，结算根据这个要做特殊处理，来源于动态收费项的（system_subject 系统科目）
        sfItem.setFeeType(chargeItemDto.getSystemSubject());
        Optional<BusinessChargeItemEnum.SystemSubjectType> instance = BusinessChargeItemEnum.SystemSubjectType.getInstance(chargeItemDto.getSystemSubject());
        if (instance.isPresent()){
            //定金费用类型名称
            sfItem.setFeeName(instance.get().getName());
        }else {
            throw new BusinessException(ResultCode.DATA_ERROR,"根据费用类型，未获取到费用类型名称");
        }
        sfItem.setAmount(refundOrder.getTotalRefundAmount());
        settleFeeItemList.add(sfItem);

        return settleFeeItemList;
    }
}