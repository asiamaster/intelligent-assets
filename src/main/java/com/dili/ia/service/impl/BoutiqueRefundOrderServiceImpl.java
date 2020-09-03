package com.dili.ia.service.impl;

import com.dili.ia.domain.BoutiqueEntranceRecord;
import com.dili.ia.domain.BoutiqueFeeOrder;
import com.dili.ia.domain.RefundOrder;
import com.dili.ia.domain.StockIn;
import com.dili.ia.domain.TransferDeductionItem;
import com.dili.ia.domain.dto.BoutiqueFeeOrderDto;
import com.dili.ia.glossary.BizTypeEnum;
import com.dili.ia.glossary.BoutiqueOrderStateEnum;
import com.dili.ia.glossary.StockInStateEnum;
import com.dili.ia.service.BoutiqueEntranceRecordService;
import com.dili.ia.service.BoutiqueFeeOrderService;
import com.dili.ia.service.CustomerAccountService;
import com.dili.ia.service.RefundOrderDispatcherService;
import com.dili.ia.service.TransferDeductionItemService;
import com.dili.settlement.domain.SettleOrder;
import com.dili.ss.base.BaseServiceImpl;
import com.dili.ss.constant.ResultCode;
import com.dili.ss.domain.BaseOutput;
import com.dili.ss.exception.BusinessException;
import com.dili.uap.sdk.domain.User;
import com.dili.uap.sdk.domain.UserTicket;
import com.dili.uap.sdk.session.SessionContext;
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
public class BoutiqueRefundOrderServiceImpl extends BaseServiceImpl<RefundOrder, Long> implements RefundOrderDispatcherService {

    private final static Logger logger = LoggerFactory.getLogger(BoutiqueRefundOrderServiceImpl.class);


    @Autowired
    private BoutiqueFeeOrderService boutiqueFeeOrderService;

    @Autowired
    private BoutiqueEntranceRecordService boutiqueEntranceRecordService;

    @Autowired
    private TransferDeductionItemService transferDeductionItemService;

    @Autowired
    private CustomerAccountService customerAccountService;

    /**
     * 退款单 -- 提交(退款的提交无需改变通行证缴费单的信息)
     * 
     * @param
     * @return 
     * @date   2020/7/21
     */
    @Override
    public BaseOutput submitHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 撤回(退款的撤回无需改变通行证缴费单的信息)
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public BaseOutput withdrawHandler(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 退款成功回调(精品停车的交费单状态由退款中修改为已退款)
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public BaseOutput refundSuccessHandler(SettleOrder settleOrder, RefundOrder refundOrder) {

        BoutiqueFeeOrderDto orderDto = boutiqueEntranceRecordService.getBoutiqueAndOrderByCode(refundOrder.getBusinessCode());
        if (orderDto != null) {
            if (!BoutiqueOrderStateEnum.SUBMITTED_REFUND.getCode().equals(orderDto.getState())) {
                throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
            }

            this.updateState(refundOrder.getBusinessCode(), orderDto.getVersion(), BoutiqueOrderStateEnum.REFUNDED);

            //转抵扣充值
            TransferDeductionItem transferDeductionItemCondition = new TransferDeductionItem();
            transferDeductionItemCondition.setRefundOrderId(refundOrder.getId());
            List<TransferDeductionItem> transferDeductionItems = transferDeductionItemService.list(transferDeductionItemCondition);
            if (CollectionUtils.isNotEmpty(transferDeductionItems)) {
                transferDeductionItems.forEach(o -> {
                    BaseOutput accountOutput = customerAccountService.rechargTransfer(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode(), refundOrder.getId(),
                            refundOrder.getCode(), o.getPayeeId(), o.getPayeeAmount(), refundOrder.getMarketId(), refundOrder.getRefundOperatorId(),
                            refundOrder.getRefundOperator());
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
     * 退款单 -- 取消(精品停车的交费单状态由退款中修改为已缴费)
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public BaseOutput cancelHandler(RefundOrder refundOrder) {

        BoutiqueFeeOrderDto orderDto = boutiqueEntranceRecordService.getBoutiqueAndOrderByCode(refundOrder.getBusinessCode());
        if (orderDto != null) {
            if (!BoutiqueOrderStateEnum.SUBMITTED_REFUND.getCode().equals(orderDto.getState())) {
                throw new BusinessException(ResultCode.DATA_ERROR, "数据状态已改变,请刷新页面重试");
            }

            this.updateState(refundOrder.getBusinessCode(), orderDto.getVersion(), BoutiqueOrderStateEnum.PAID);
        }
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 业务数据加载
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public BaseOutput<Map<String, Object>> buildBusinessPrintData(RefundOrder refundOrder) {
        return BaseOutput.success();
    }

    /**
     * 退款单 -- 获取业务类型
     *
     * @param
     * @return
     * @date   2020/7/21
     */
    @Override
    public Set<String> getBizType() {
        return Sets.newHashSet(BizTypeEnum.BOUTIQUE_ENTRANCE.getCode());
    }

    private void updateState(String code, Integer version, BoutiqueOrderStateEnum state) {

        BoutiqueFeeOrder domain = new BoutiqueFeeOrder();
        domain.setVersion(version + 1);
        domain.setState(state.getCode());
        domain.setModifyTime(LocalDateTime.now());

        BoutiqueFeeOrder condition = new BoutiqueFeeOrder();
        condition.setCode(code);
        condition.setVersion(version);

        // 修改精品停车交费单状态
        int row = boutiqueFeeOrderService.updateSelectiveByExample(domain, condition);
        if (row != 1) {
            throw new BusinessException(ResultCode.DATA_ERROR, "业务繁忙,稍后再试");
        }
    }
}
