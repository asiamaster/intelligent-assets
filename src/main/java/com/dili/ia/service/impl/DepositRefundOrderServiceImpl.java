package com.dili.ia.service.impl;

import com.dili.assets.sdk.dto.BusinessChargeItemDto;
import com.dili.assets.sdk.enums.BusinessChargeItemEnum;
import com.dili.commons.glossary.YesOrNoEnum;
import com.dili.ia.domain.DepositOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.glossary.*;
import com.dili.ia.mapper.RefundOrderMapper;
import com.dili.ia.service.BusinessChargeItemService;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.DepositOrderService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.settlement.domain.SettleFeeItem;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.ss.util.MoneyUtils;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * 由MyBatis Generator工具自动生成
 * This file was generated on 2020-03-09 19:34:40.
 */
@Service
public class DepositRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService {
    private final static Logger LOG = LoggerFactory.getLogger(DepositRefundOrderServiceImpl.class);


    public RefundOrderMapper getActualDao() {
        return (RefundOrderMapper)getDao();
    }

    @Autowired
    BusinessChargeItemService businessChargeItemService;
    @Autowired
    DepositOrderService depositOrderService;
    @Autowired
    CustomerAccountService customerAccountService;
    @Override
    public Set<String> getBizType() {
        return Sets.newHashSet(BizTypeEnum.DEPOSIT_ORDER.getCode());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput submitHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    @Override
    public BaseOutput withdrawHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    @Override
    public BaseOutput updateHandler(RefundOrder refundOrder) {
        if (refundOrder.getBusinessId() == null){
            return BaseOutput.failure("参数BusinessId 不能为空！");
        }
        DepositOrder depositOrder = depositOrderService.get(refundOrder.getBusinessId());
        Long totalRefundAmount = refundOrder.getPayeeAmount() + depositOrder.getRefundAmount();
        if (depositOrder.getPaidAmount() < totalRefundAmount){
            return BaseOutput.failure("【退款总金额】不能大于订单已交费总金额: " + MoneyUtils.centToYuan(depositOrder.getPaidAmount()));
        }
        return BaseOutput.success();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
        try{
            return depositOrderService.refundSuccessHandler(refundOrder);
        }catch (Exception e){
            LOG.info("租赁退款单成功回调异常",e);
            return BaseOutput.failure(e.getMessage());
        }
    }

    @Override
    public BaseOutput cancelHandler(RefundOrder refundOrder) {
        DepositOrder depositOrder = depositOrderService.get(refundOrder.getBusinessId());
        //保证金退款单取消，如果保证金业务单的【退款状态】是【未退款】，那么取消保证金退款单，保证金业务状态回退到【已交费】，否则的话回退到【已退款】
        if (depositOrder.getRefundState().equals(DepositRefundStateEnum.NO_REFUNDED.getCode())){
            depositOrder.setState(DepositOrderStateEnum.PAID.getCode());
        }else{
            depositOrder.setState(DepositOrderStateEnum.REFUND.getCode());
        }
        if (depositOrderService.updateSelective(depositOrder) == 0) {
            LOG.info("取消保证金退款单【修改保证金状态失败】 ,乐观锁生效！【保证金单ID:{}】", depositOrder.getId());
            throw new BusinessException(ResultCode.DATA_ERROR, "多人操作，请重试！");
        }
        return BaseOutput.success();
    }

    @Override
    public BaseOutput<Map<String,Object>> buildBusinessPrintData(RefundOrder refundOrder) {
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("printTemplateCode", PrintTemplateEnum.DEPOSIT_REFUND_ORDER.getCode());
        DepositOrder depositOrder = depositOrderService.get(refundOrder.getBusinessId());
        //保证金类型
        resultMap.put("typeName", depositOrder.getTypeName());
        //资产类型
        resultMap.put("assetsType", AssetsTypeEnum.getAssetsTypeEnum(depositOrder.getAssetsType()).getName());
        //资产名称
        resultMap.put("assetsName", depositOrder.getAssetsName());
        return BaseOutput.success().setData(resultMap);
    }

    @Override
    public List<SettleFeeItem> buildSettleFeeItem(RefundOrder refundOrder) {
        //组装费用项
        List<SettleFeeItem> settleFeeItemList = new ArrayList<>();
        SettleFeeItem sfItem = new SettleFeeItem();
        List<BusinessChargeItemDto> chargeItemDtos = businessChargeItemService.queryFixedBusinessChargeItemConfig(refundOrder.getMarketId(), BizTypeEnum.DEPOSIT_ORDER.getCode(), YesOrNoEnum.YES.getCode(), YesOrNoEnum.YES.getCode(), ChargeItemCodeEnum.DEPOSIT.getCode());
        BusinessChargeItemDto chargeItemDto = chargeItemDtos.stream().findFirst().orElse(null);
        if (null == chargeItemDto){
            LOG.info("保证金单业务没有查询到固定的【保证金】收费项，code={}", ChargeItemCodeEnum.DEPOSIT.getCode());
            throw new BusinessException(ResultCode.DATA_ERROR, "保证金单业务没有查询到固定的【保证金】收费项");
        }
        //静态收费项
        sfItem.setChargeItemId(chargeItemDto.getId());
        //静态收费项
        sfItem.setChargeItemName(chargeItemDto.getChargeItem());
        //保证金费用类型固定，必须传，结算根据这个要做特殊处理，来源于动态收费项的（system_subject 系统科目）
        sfItem.setFeeType(chargeItemDto.getSystemSubject());
        Optional<BusinessChargeItemEnum.SystemSubjectType> instance = BusinessChargeItemEnum.SystemSubjectType.getInstance(chargeItemDto.getSystemSubject());
        if (instance.isPresent()){
            //保证金费用类型名称
            sfItem.setFeeName(instance.get().getName());
        }else {
            throw new BusinessException(ResultCode.DATA_ERROR,"根据费用类型，未获取到费用类型名称");
        }
        sfItem.setAmount(refundOrder.getTotalRefundAmount());
        settleFeeItemList.add(sfItem);

        return settleFeeItemList;
    }
}