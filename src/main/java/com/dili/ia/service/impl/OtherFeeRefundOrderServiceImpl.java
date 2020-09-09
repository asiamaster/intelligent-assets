package com.dili.ia.service.impl;

import com.dili.ia.domain.OtherFee;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.TransferDeductionItem;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.OtherFeeStateEnum;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.OtherFeeService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.ia.service.TransferDeductionItemService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.google.common.collect.Sets;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
public class OtherFeeRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService {

    private final static Logger logger = LoggerFactory.getLogger(OtherFeeRefundOrderServiceImpl.class);

    @Autowired
    private OtherFeeService otherFeeService;

    @Autowired
    private TransferDeductionItemService transferDeductionItemService;

    @Autowired
    private CustomerAccountService customerAccountService;

    /**
     * 退款单 -- 提交(无需改变通行证缴费单的状态)
     * 
     * @param  refundOrder
     * @return BaseOutput
     * @date   2020/7/21
     */
    @Override
    public BaseOutput submitHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 撤回(无需改变通行证缴费单的状态)
     *
     * @param  refundOrder
     * @return BaseOutput
     * @date   2020/7/21
     */
    @Override
    public BaseOutput withdrawHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 退款成功回调(将通行证缴费单的状态由退款中修改为已退款)
     *
     * @param  settleOrder
     * @param  refundOrder
     * @return BaseOutput
     * @date   2020/7/21
     */
    @Override
    public BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {
        OtherFee otherFeeInfo = otherFeeService.getOtherFeeByCode(refundOrder.getBusinessCode());
        if (otherFeeInfo != null) {
            if (!OtherFeeStateEnum.REFUNDING.getCode().equals(otherFeeInfo.getState())) {
                throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
            }

            this.updateState(refundOrder.getBusinessCode(), otherFeeInfo.getVersion(), OtherFeeStateEnum.REFUND);

            //转抵扣充值
            TransferDeductionItem transferDeductionItemCondition = new TransferDeductionItem();
            transferDeductionItemCondition.setRefundOrderId(refundOrder.getId());
            List<TransferDeductionItem> transferDeductionItems = transferDeductionItemService.list(transferDeductionItemCondition);
            if (CollectionUtils.isNotEmpty(transferDeductionItems)) {
                transferDeductionItems.forEach(o -> {
                    BaseOutput accountOutput = customerAccountService.rechargTransfer(BizTypeEnum.OTHER_FEE.getCode(),
                            refundOrder.getId(), refundOrder.getCode(), o.getPayeeId(), o.getPayeeAmount(), refundOrder.getMarketId(),
                            refundOrder.getRefundOperatorId(), refundOrder.getRefundOperator());
                    if (!accountOutput.isSuccess()) {
                        logger.info("退款单转抵异常，【退款编号:{},收款人:{},收款金额:{},msg:{}】", refundOrder.getCode(), o.getPayee(), o.getPayeeAmount(), accountOutput.getMessage());
                        throw new BusinessException(ResultCode.DATA_ERROR, accountOutput.getMessage());
                    }
                });
            }
        }

        return BaseOutput.success();
    }

    /**
     * 退款单 -- 取消(将通行证缴费单的状态由退款中还原原状态 - 未生效/已生效/已过期)
     *
     * @param  refundOrder
     * @return BaseOutput
     * @date   2020/7/21
     */
    @Override
    public BaseOutput cancelHandler(RefundOrder refundOrder) {
        OtherFee otherFee = new OtherFee();
        otherFee.setCode(refundOrder.getBusinessCode());
        OtherFee otherFeeInfo = otherFeeService.selectByExample(otherFee).get(0);
        if (otherFeeInfo != null) {
            if (!OtherFeeStateEnum.REFUNDING.getCode().equals(otherFeeInfo.getState())) {
                throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
            }

            this.updateState(refundOrder.getBusinessCode(), otherFeeInfo.getVersion(), OtherFeeStateEnum.PAID);
        }

        return BaseOutput.success();
    }

    /**
     * 退款单 -- 业务数据加载
     *
     * @param  refundOrder
     * @return BaseOutput
     * @date   2020/7/21
     */
    @Override
    public BaseOutput<Map<String, Object>> buildBusinessPrintData(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 获取业务类型
     *
     * @return Set
     * @date   2020/7/21
     */
    @Override
    public Set<String> getBizType() {
        return Sets.newHashSet(BizTypeEnum.OTHER_FEE.getCode());
    }

    /**
     * 具体的修改操作
     */
    private void updateState(String code, Integer version, OtherFeeStateEnum state) {

        OtherFee domain = new OtherFee();
        domain.setVersion(version + 1);
        domain.setState(state.getCode());
        domain.setModifyTime(LocalDateTime.now());

        OtherFee condition = new OtherFee();
        condition.setCode(code);
        condition.setVersion(version);

        // 修改通行证交费单状态
        int row = otherFeeService.updateSelectiveByExample(domain, condition);
        if (row != 1) {
            throw new BusinessException(ResultCode.DATA_ERROR, "业务繁忙,稍后再试");
        }
    }
}
