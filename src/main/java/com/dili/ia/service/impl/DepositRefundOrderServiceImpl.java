package com.dili.ia.service.impl;

import com.dili.ia.domain.DepositOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.DepositOrderStateEnum;
import com.dili.ia.glossary.DepositRefundStateEnum;
import com.dili.ia.glossary.PrintTemplateEnum;
import com.dili.ia.mapper.RefundOrderMapper;
import com.dili.ia.service.DepositOrderService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
    DepositOrderService depositOrderService;

    @Override
    public Set<String> getBizType() {
        return Sets.newHashSet(BizTypeEnum.DEPOSIT_ORDER.getCode());
    }

    @Override
    public BaseOutput submitHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    @Override
    public BaseOutput withdrawHandler(RefundOrder refundOrder) {
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
        resultMap.put("depositTypeName", depositOrder.getTypeName());
        return BaseOutput.success().setData(resultMap);
    }
}